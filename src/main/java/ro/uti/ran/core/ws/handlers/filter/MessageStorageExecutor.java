package ro.uti.ran.core.ws.handlers.filter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.config.AsyncLayerConfig;
import ro.uti.ran.core.exception.MessageStoreException;
import ro.uti.ran.core.messages.MessageRequestData;
import ro.uti.ran.core.messages.MessageResponseData;
import ro.uti.ran.core.service.messages.MessageService;
import ro.uti.ran.core.service.parametru.ParametruService;

@Component
public class MessageStorageExecutor {

	private static final ThreadLocal<Context> context = new ThreadLocal<Context>();
	private static final Log log = LogFactory.getLog(MessageStorageExecutor.class);
	
	@Autowired
	private MessageService messageService;
	
    @Autowired
    private ParametruService parametruService;
	
	@Autowired
	@Qualifier("messageRequestExecutor")
	private Executor messageRequestExecutor;
	
	@Autowired
	@Qualifier("messageResponseExecutor")
	private Executor messageResponseExecutor;
	
	private void setId(Long id) {
		context.get().setIdRequest(id);
	}
	
	private Long getId(boolean wait) {
		return context.get().getIdRequest(wait);
	}
	
	public void setRequestMessage(MessageRequestData messageRequestData) {
		initContext(false);
		context.get().setRequest(messageRequestData);
	}
	
	public MessageRequestData getMessageRequest() {
		return context.get().getRequest();
	}
	
	public Long storeMessageRequest() throws MessageStoreException {
		
		if(getId(false) != null) {
			return getId(false);
		}
		
		setId(messageService.storeMessageRequest(context.get().getRequest()));
		return getId(false);
	}
	
	public void setResponseMessage(MessageResponseData messageResponseData)  {
		context.get().setResponse(messageResponseData);
	}
	
	public Long storeMessageResponse() throws MessageStoreException {
		return messageService.storeMessageResponse(context.get().getResponse(), context.get().getIdRequest(true));
	}
	
	public boolean isException() {
		return context.get().getResponse().getFault() != null;
	}
	
	public void setContext(Context _context) {
		context.set(_context);
	}
	
	public Context getContext() {
		return context.get();
	}
	
	public void initContext() {
		context.set(new Context());
	}
	
	private void initContext(boolean forceNew) {
		if(forceNew || context.get() == null) { 
			initContext();
		}
	}
	
	public void executeTask(Runnable task, String executorName) {
		if(isAsync()) {
			
			if(executorName.equals(AsyncLayerConfig.MESSAGE_REQUEST_EXECUTOR)) {
				messageRequestExecutor.execute(task);
				return;
			}
			
			messageResponseExecutor.execute(task);
			return;
		}
		
		task.run();
	}
	
	private boolean isAsync() {
		try {
			return Boolean.valueOf(parametruService.getParametru("messages.store.async").getValoare());
		} catch(Exception e) {
			log.error("Parametrul \"messages.store.async\" nu putut fi gasit!", e);
			return true;
		}
	}
	
	public class Context {
		
		private static final long _LATCH_TIMEOUT = 60; //60 sec
		
		private MessageRequestData request;
		private MessageResponseData response;
		private Long idRequest;
		private CountDownLatch idAcquired;
		
		public Context() {
			idAcquired = new CountDownLatch(1);
		}
		
		private MessageRequestData getRequest() {
			return request;
		}
		private void setRequest(MessageRequestData request) {
			this.request = request;
		}
		private MessageResponseData getResponse() {
			return response;
		}
		private void setResponse(MessageResponseData response) {
			this.response = response;
		}
		private Long getIdRequest(boolean wait) {
			if(wait) {
				waitLatch();
			}
			return idRequest;
		}
		private void setIdRequest(Long idRequest) {
			this.idRequest = idRequest;
			idAcquired.countDown();
		}
		
		private void waitLatch() {
			
			try {
				idAcquired.await(_LATCH_TIMEOUT, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	
	}

}
