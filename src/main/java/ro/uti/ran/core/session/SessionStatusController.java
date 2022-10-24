package ro.uti.ran.core.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ro.uti.ran.core.service.idm.IdmService;
import ro.uti.ran.core.service.idm.SessionInfo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by neculai.stanciu on 15.12.2016.
 */
@Component
public class SessionStatusController extends HttpServlet {
    public static final Logger log = LoggerFactory.getLogger(SessionStatusController.class);
    public static final String TOKEN_ID_KEY = "tokenId";
    public static final String APPLICATION_JSON = "application/json";

    @Autowired
    private IdmService idmService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tokenId = req.getParameter(TOKEN_ID_KEY);
        PrintWriter respWriter = resp.getWriter();
        if(tokenId == null){
            respWriter.write(getResponseAsJson(null,Boolean.FALSE));
            return;
        }
        try {
            SessionInfo sessionInfo = idmService.getSessionInfo(tokenId);
            resp.setContentType(APPLICATION_JSON);
            if(sessionInfo != null && sessionInfo.isValid()) {
                respWriter.write(getResponseAsJson(sessionInfo.isValid(),Boolean.TRUE));
                return;
            } else {
                respWriter.write(getResponseAsJson(sessionInfo.isValid(),Boolean.FALSE));
                return;
            }
        } catch (Exception e) {
            respWriter.write(getResponseAsJson(null,Boolean.FALSE));
            return;
        }


    }

    private String getResponseAsJson(Boolean status,Boolean success){
        StringBuilder sb = new StringBuilder(20);
        sb.append("{")
                .append("\"success\"")
                .append(":")
                .append(success);
        if(success.equals(Boolean.TRUE)) {
            sb.append(",")
                    .append("\"status\"")
                    .append(":")
                    .append("\"")
                    .append(status)
                    .append("\"");
        }
        sb.append("}");
        return sb.toString();
    }
}
