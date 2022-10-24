package ro.uti.ran.core.rapoarte.servlet;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ro.uti.ran.core.exception.WsAuthenticationException;
import ro.uti.ran.core.rapoarte.model.Rapoarte;
import ro.uti.ran.core.rapoarte.service.UserService;
import ro.uti.ran.core.rapoarte.task.RapoarteXmlGen;
import ro.uti.ran.core.rapoarte.task.RaportSchemaGen;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by miroslav.rusnac on 29/03/2016.
 */
@WebServlet("/download")

public class DownloadServlet extends HttpServlet {


    private static final Logger LOG = LoggerFactory.getLogger(RapoarteServlet.class);


//    @Autowired
//    private Environment env;

    public DownloadServlet(){
        super();

    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        ServletContext application = getServletConfig().getServletContext();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            StringTokenizer st = new StringTokenizer(authHeader);
            if (st.hasMoreTokens()) {
                String basic = st.nextToken();

                if (basic.equalsIgnoreCase("Basic")){

                    try {
                        String credentials = new String(Base64.decodeBase64(st.nextToken()), "UTF-8");
                        LOG.debug("Credentials: " + credentials);
                        int p = credentials.indexOf(":");
                        if (p != -1) {
                            String _username = credentials.substring(0, p).trim();
                            String _password = credentials.substring(p + 1).trim();
                            ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(application);
                            UserService userService = ac.getBean(UserService.class);
                            try {

                                RanAuthorization ranAuthorization = userService.checkUser(_username, _password);

                            } catch (WsAuthenticationException e) {
                                unauthorized(response, "Bad credentials");
                            }

                            String filePath = userService.getProperty("reports.path")+_username+"/";
                            String fileName=request.getParameter("file");
                            PrintWriter out=response.getWriter();

                            response.setContentType("APPLICATION/OCTET-STREAM");
                            response.setHeader("Content-Disposition","attachment;fileName=\""+fileName+"\"");
                            int i;
                            FileInputStream file=new FileInputStream(filePath +fileName);
                            while((i=file.read()) !=-1){
                                out.write(i);
                            }
                            file.close();
                            out.close();

                        } else {
                            unauthorized(response, "Invalid authentication token");
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new Error("Couldn't retrieve authentication", e);
                    }
                }
            }
        } else {
            unauthorized(response);
        }
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"Protected\"");
        response.sendError(401, message);
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        unauthorized(response, "Unauthorized");
    }
}