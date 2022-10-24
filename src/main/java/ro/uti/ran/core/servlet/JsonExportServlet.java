package ro.uti.ran.core.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

/**
 * Servlet implementation class JsonExportServlet
 */
@WebServlet("/JsonExportServlet")
public class JsonExportServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    private static final String _TYPE = "type";
    private static final String _CONTENT_TYPE = "application/json";
    private static final String _UTF_8 = "UTF-8";
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public JsonExportServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			String json = ExportConnectorBridge.getExportConnector(request.getParameter(_TYPE)).getData(request).toString();
			
			response.setContentType(_CONTENT_TYPE);
		    response.setCharacterEncoding(_UTF_8);
		    response.getWriter().write(json);
		    
		} catch (JSONException e) {
			e.printStackTrace();
		}
	   
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
