package ro.uti.ran.core.ws.handlers;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import ro.uti.ran.core.config.AsyncLayerConfig;
import ro.uti.ran.core.exception.MessageStoreException;
import ro.uti.ran.core.ws.handlers.filter.MessageStorageExecutor;
import ro.uti.ran.core.ws.handlers.filter.MessageStorageExecutor.Context;
import ro.uti.ran.core.ws.handlers.filter.WrapperRequest;
import ro.uti.ran.core.ws.handlers.filter.WrapperResponse;

@Component
public class RanWsHandlerFilter implements Filter {

	private static final String _POST = "post";
	
    private static ThreadLocal<WrapperRequest> requestWrapper = null;
    private static ThreadLocal<WrapperResponse> responseWrapper = null;

    private static MessageStorageExecutor messageStorageExecutor;
    
    public static void setMessageStorageExecutor(MessageStorageExecutor messageExecutor) {
        messageStorageExecutor = messageExecutor;
    }
    
    public static RanWsPayload getRequest() {
        return requestWrapper.get();
    }

    public static RanWsPayload getResponse() {
        return responseWrapper.get();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        requestWrapper = new ThreadLocal<WrapperRequest>();
        responseWrapper = new ThreadLocal<WrapperResponse>();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        
//        WSDL case
        if (!httpServletRequest.getMethod().equalsIgnoreCase(_POST)) {
            chain.doFilter(request, response);
            return;
        }

        requestWrapper.set(new WrapperRequest(httpServletRequest));
        responseWrapper.set(new WrapperResponse(httpServletResponse));

        messageStorageExecutor.initContext();
        
        chain.doFilter(requestWrapper.get(), responseWrapper.get());
        
        new RequestLoggerJob(messageStorageExecutor.getContext());
        new ResponseLoggerJob(messageStorageExecutor.getContext());
       
    }

    @Override
    public void destroy() {
    }

	public static String getMessageId() throws MessageStoreException {
		return messageStorageExecutor.storeMessageRequest().toString();
	}
	
	private abstract class LoggerJob implements Runnable {
		
		protected Context context = null;
		
		private LoggerJob(Context context) {
			this.context = context;
			go();
		}
		
		private LoggerJob() {}
		
		protected boolean isMessageStorable() {
          	return RanWsHandlerUtil.isMessageStorable(messageStorageExecutor.getMessageRequest().getWsdlServiceName(), messageStorageExecutor.getMessageRequest().getWsdlOperationName());
      	}
		
		
		@Override
		public void run() {
			messageStorageExecutor.setContext(context);
		}
		
		private void go() {
			messageStorageExecutor.executeTask(this, getExecutorName());   
		}
		
		protected abstract String getExecutorName();
	}
	
	private class RequestLoggerJob extends LoggerJob {
		
		private RequestLoggerJob(Context context) {
			super(context);
		}
		
		@Override
		public void run() {
			
			super.run();
			
			if(isMessageStorable()) {
				try {
					messageStorageExecutor.storeMessageRequest();
	            } catch (Throwable t) {
	            	t.printStackTrace();
	            }
		    }
			
		}

		@Override
		protected String getExecutorName() {
			return AsyncLayerConfig.MESSAGE_REQUEST_EXECUTOR;
		}
	}
	
	private class ResponseLoggerJob extends LoggerJob {
		
		private ResponseLoggerJob(Context context) {
			super(context);
		}
		
		@Override
		public void run() {
			
			super.run();
			
			if(isMessageStorable() || messageStorageExecutor.isException()) {
				 try {
		              messageStorageExecutor.storeMessageResponse();
		         } catch (Throwable t) {
		            	t.printStackTrace();
		         }
			}
			
		}
		
		@Override
		protected String getExecutorName() {
			return AsyncLayerConfig.MESSAGE_RESPONSE_EXECUTOR;
		}
	}
    
}
