package ro.uti.ran.core.rapoarte.servlet;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ro.uti.ran.core.exception.WsAuthenticationException;
import ro.uti.ran.core.model.portal.Context;
import ro.uti.ran.core.rapoarte.model.Rapoarte;
import ro.uti.ran.core.rapoarte.service.UserService;
import ro.uti.ran.core.rapoarte.task.RapoarteXmlGen;
import ro.uti.ran.core.rapoarte.task.RaportSchemaGen;
import ro.uti.ran.core.rapoarte.task.summary.RapoarteSummary;
import ro.uti.ran.core.rapoarte.task.summary.SumarRapoarte;
import ro.uti.ran.core.ws.internal.RanAuthorization;

import javax.mail.internet.MimeUtility;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.*;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import static org.apache.commons.lang.StringEscapeUtils.unescapeHtml;

/**
 * Created by miroslav.rusnac on 28/03/2016.
 */
@Component
public class RapoarteServlet extends HttpServlet {


    private static final Logger LOG = LoggerFactory.getLogger(RapoarteServlet.class);


    //@Autowired
    //private Environment env;

    public RapoarteServlet(){
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

                if (basic.equalsIgnoreCase("Basic")) try {
                    String credentials = new String(Base64.decodeBase64(st.nextToken()), "UTF-8");
                    LOG.debug("Credentials: " + credentials);
                    int p = credentials.indexOf(":");
                    if (p != -1) {
                        String _username = credentials.substring(0, p).trim();
                        String _password = credentials.substring(p + 1).trim();
                        ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(application);
                        UserService userService = ac.getBean(UserService.class);
                        boolean doNext = false;
                        try {
                            RanAuthorization ranAuthorization = userService.checkUser(_username, _password);
                            if ( userService.checkPermission(ranAuthorization.getContext())) {
                                Exception noPermision = new Exception("Rol " + ranAuthorization.getContext() + " acces blocat");
                                throw noPermision;
                            }
                            doNext =true;
                        } catch (WsAuthenticationException e) {
                            unauthorized(response, e.getMessage());

                        } catch (Exception e) {
                            unauthorized(response, e.getMessage());
                        }

                        //String path = env.getProperty("reports.path");
                        if (doNext) {

                            String path = userService.getProperty("reports.path") + _username;

                            Enumeration<String> params = request.getParameterNames();

                            List<String> list = new ArrayList<String>(Collections.list(params));


                            if (list.size()>0 && list.get(0).equals("download")) {

                                String fileName = unescapeHtml(request.getParameter("download"));
                                PrintWriter out = response.getWriter();

                                response.setContentType("APPLICATION/OCTET-STREAM");
                                response.setHeader("Content-Disposition", "attachment;filename=\"" + MimeUtility.encodeWord(fileName, "utf-8", "Q") +  "\"");
                                int i;
                                FileInputStream file = new FileInputStream(path + "/" + fileName);
                                while ((i = file.read()) != -1) {
                                    out.write(i);
                                }
                                file.close();
                                out.close();
                            } else {
                                File[] listOfFiles = new File[]{};
                                File folder = new File(path);
                                if (folder.exists()) {
                                    listOfFiles = folder.listFiles();
                                }

                                if (list.size()>0 && list.get(0).equals("json")) {
                                    List<Rapoarte> reports = new ArrayList<>();
                                    for (int i = 0; i < listOfFiles.length; i++) {
                                        if (listOfFiles[i].isFile()) {
                                            Rapoarte toAdd = new Rapoarte(listOfFiles[i].getName());
                                            reports.add(toAdd);
                                        }
                                    }
                                    String json = new Gson().toJson(reports);
                                    response.setContentType("application/json;charset=UTF-8");
                                    response.getWriter().write(json);
                                } else if (list.size()>0 && list.get(0).equals("xsd")) {
                                    String xsd = ac.getBean(RaportSchemaGen.class).generateListaRapoarteXsdSchema();
                                    response.setContentType("application/xml;charset=UTF-8");
                                    response.getWriter().write(xsd);
                                } else if (list.size()>0 && list.get(0).equals("xml")) {

                                    String xml = ac.getBean(RapoarteXmlGen.class).generateRapoarteXml(listOfFiles);
                                    response.setContentType("application/xml;charset=UTF-8");
                                    response.getWriter().write(xml);
                                }
                                else if(list.size()==0){

                                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/rapoarte.jsp");
                                    dispatcher.forward(request, response);

                                }
                                else{
                                    response.getWriter().write("<H1>Eroare: URL necunoscut</H1>");
                                }
                            }
                        }
                    } else {
                        unauthorized(response, "Invalid authentication token");
                    }
                } catch (UnsupportedEncodingException e) {
                    throw new Error("Couldn't retrieve authentication", e);
                } catch (JAXBException e) {
                    e.printStackTrace();
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


