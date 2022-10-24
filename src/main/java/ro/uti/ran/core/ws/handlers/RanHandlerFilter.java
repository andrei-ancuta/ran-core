package ro.uti.ran.core.ws.handlers;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ro.uti.ran.core.config.AsyncLayerConfig;
import ro.uti.ran.core.exception.WsAuthenticationException;
import ro.uti.ran.core.messages.MessageRequestData;
import ro.uti.ran.core.messages.MessageResponseData;
import ro.uti.ran.core.rapoarte.service.UserService;
import ro.uti.ran.core.ws.handlers.filter.MessageStorageExecutor;
import ro.uti.ran.core.ws.handlers.filter.WrapperRequest;
import ro.uti.ran.core.ws.handlers.filter.WrapperResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import ro.uti.ran.core.ws.handlers.filter.MessageStorageExecutor.Context;
import ro.uti.ran.core.ws.internal.RanAuthorization;

/**
 * Created by adrian.boldisor on 5/12/2016.
 */
@Component
public class RanHandlerFilter implements Filter {

    private static final String _GET = "get";


    private static ThreadLocal<WrapperRequest> requestWrapper = null;
    private static ThreadLocal<WrapperResponse> responseWrapper = null;

    private static MessageStorageExecutor messageStorageExecutor;


    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //  System.out.println("init=> RanHandlerFilter "+ filterConfig);
        requestWrapper = new ThreadLocal<WrapperRequest>();
        responseWrapper = new ThreadLocal<WrapperResponse>();
        ServletContext application = filterConfig.getServletContext();
        ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(application);
        userService = ac.getBean(UserService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String url = httpServletRequest.getRequestURI();


        String opName = "";
        //  report case

        if (!httpServletRequest.getMethod().equalsIgnoreCase(_GET)) {
            chain.doFilter(request, response);
            return;
        }


        Boolean isDownload = (httpServletRequest.getParameter("download") == null ? false : true);

        opName += (isDownload) ? "descarcare raport" : "listare.";
        generateLog(httpServletRequest, httpServletResponse, chain, url, opName);
        return;


    }


    private void generateLog(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain, String url, String opName) throws IOException, ServletException {

        Map<String, String[]> keyValues = httpServletRequest.getParameterMap();
        String operationType = "";

        for (Map.Entry<String, String[]> entry : keyValues.entrySet()) {
            String[] valuesArray = entry.getValue();
            String joinedString = StringUtils.join(valuesArray);
            operationType += entry.getKey() + "/" + joinedString;

        }


        messageStorageExecutor.initContext();

        MessageRequestData messageRequest = new MessageRequestData();
        messageStorageExecutor.setRequestMessage(messageRequest);

        MessageResponseData messageResponse = new MessageResponseData();
        messageStorageExecutor.setResponseMessage(messageResponse);

        if (httpServletRequest.getHeader("authorization") == null) {

            //  autentificationLog(httpServletRequest,httpServletResponse,chain,url);
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String usernameLabel = "user.";
        String _username = "";
        String _password = "";

        String authHeader = httpServletRequest.getHeader("authorization");
        StringTokenizer st = new StringTokenizer(authHeader);
        String basic = st.nextToken();

        if (basic.equalsIgnoreCase("Basic")) {
            String credentials = new String(Base64.decodeBase64(st.nextToken()), "UTF-8");
            int p = credentials.indexOf(":");
            if (p != -1) {
                _username += credentials.substring(0, p).trim();
                _password = credentials.substring(p + 1).trim();
                HttpSession httpSession = httpServletRequest.getSession();
                try {
                    userService.checkUser(_username, _password);

                    if (!"true".equals(httpSession.getAttribute("isAutentificate"))) {
                        httpSession.setAttribute("isAutentificate", "true");
                        opName = "autentificare";
                        operationType = opName;
                    }
                } catch (WsAuthenticationException e) {
                    httpSession.setAttribute("isAutentificate", "false");
                    httpServletResponse.setHeader("authorization", null);
                    chain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }

                usernameLabel += _username + ".";

                messageRequest.setIpAddress(httpServletRequest.getRemoteAddr());
                messageRequest.setHostName(InetAddress.getLocalHost().getCanonicalHostName());
                messageRequest.setWsdlOperationName(usernameLabel + operationType);
                messageRequest.setWsdlServiceName(url);
                messageRequest.setRanOperationType(opName + url + "." + operationType);


                requestWrapper.set(new WrapperRequest(httpServletRequest));
                responseWrapper.set(new WrapperResponse(httpServletResponse));


                chain.doFilter(requestWrapper.get(), responseWrapper.get());
                new RequestLoggerJob(messageStorageExecutor.getContext());
                new ResponseLoggerJob(messageStorageExecutor.getContext());
            }
        }



    }


    protected static void setMessageStorageExecutor(MessageStorageExecutor messageExecutor) {
        messageStorageExecutor = messageExecutor;
    }

    @Override
    public void destroy() {

    }


    private abstract class LoggerJob implements Runnable {

        protected MessageStorageExecutor.Context context = null;

        private LoggerJob(MessageStorageExecutor.Context context) {
            this.context = context;
            go();
        }

        private LoggerJob() {
        }

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
        public RequestLoggerJob(Context context) {
            super(context);
        }

        @Override
        protected String getExecutorName() {
            return AsyncLayerConfig.MESSAGE_REQUEST_EXECUTOR;
        }

        @Override
        public void run() {

            super.run();
            //isMessageStorable()


            try {
                messageStorageExecutor.storeMessageRequest();
            } catch (Throwable t) {
                t.printStackTrace();
            }


        }

    }

    private class ResponseLoggerJob extends LoggerJob {

        private ResponseLoggerJob(Context context) {
            super(context);
        }

        @Override
        public void run() {

            super.run();


                try {
                    messageStorageExecutor.storeMessageResponse();
                } catch (Throwable t) {
                    t.printStackTrace();
                }


        }

        @Override
        protected String getExecutorName() {
            return AsyncLayerConfig.MESSAGE_RESPONSE_EXECUTOR;
        }
    }
}
