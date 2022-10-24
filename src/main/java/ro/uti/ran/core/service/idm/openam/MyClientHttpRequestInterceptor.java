package ro.uti.ran.core.service.idm.openam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.*;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-10-09 01:00
 */
public class MyClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(MyClientHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        logger.debug("===========================request begin================================================");

        logger.debug("URI : " + request.getURI());
        logger.debug("Method : " + request.getMethod());
        logger.debug("Request Body : " + new String(body, "UTF-8"));
        logger.debug("==========================request end================================================");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        logger.debug("============================response begin==========================================");
        logger.debug("status code: " + response.getStatusCode());
        logger.debug("status text: " + response.getStatusText());

        InputStream inputStream = response.getBody();
        if (!inputStream.markSupported()) {
            inputStream = new BufferedInputStream(inputStream);
        }
        inputStream.mark(1000);
        try {
            byte[]b = new byte[1000];
            inputStream.read(b);
            System.out.println("Response body:");
            System.out.println(new String(b));
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            inputStream.reset();
        }
        logger.debug("=======================response end=================================================");
    }
}
