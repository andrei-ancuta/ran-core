package ro.uti.ran.core.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.impl.FtpServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Stanciu Neculai on 15.Dec.2015.
 */
@Component
public class FtpServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(FtpServlet.class);
    private static final String FTP_STATUS_RUNNING = "RUNNING";
    private static final String FTP_STATUS_STOPPED = "STOPPED";
    private static final String FTP_STATUS_SUSPENDED = "SUSPENDED";
    public static final String APPLICATION_JSON = "application/json";
    public static final String FTP_SERVER_CONTEXT = "ftpServerContext";
    @Autowired
    private FtpServer ftpServer;


    @Override
    public void init(ServletConfig config) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ftpStatus = null;
        if (ftpServer.isStopped()) {
             ftpStatus = FTP_STATUS_STOPPED;
        } else if (ftpServer.isSuspended()) {
            ftpStatus = FTP_STATUS_SUSPENDED;
        } else {
            ftpStatus = FTP_STATUS_RUNNING;
        }
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType(APPLICATION_JSON);
        if(ftpStatus != null) {
            respWriter.write(getFtpStatusAsJson(ftpStatus,Boolean.TRUE));
        } else {
            respWriter.write(getFtpStatusAsJson(ftpStatus,Boolean.FALSE));
        }
    }

    private String getFtpStatusAsJson(String ftpStatus,Boolean success){
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
                    .append(ftpStatus)
                    .append("\"");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //restart server
        try {
            if (ftpServer.isStopped()) {
                   return;
            } else if(ftpServer.isSuspended()){
                ftpServer.resume();
            } else {
                ftpServer.suspend();
                ftpServer.resume();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        resp.sendRedirect("/");
    }
}
