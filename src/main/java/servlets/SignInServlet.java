package servlets;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import applicationVariables.ApplicationVariables;
import sessionManage.SignInManager;

/**
 * Servlet implementation class SignInServlet
 */

public class SignInServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
    
    @Override
    public void init() throws ServletException
    {
    	
    	ServletContext context =  getServletContext();
		String databaseName = context.getInitParameter("database_name");
		String databaseUserName = context.getInitParameter("database_username");
		String databasePassword = context.getInitParameter("database_password");
		try
		{
			ApplicationVariables.getInstance();
			ApplicationVariables.getConnection(databaseName, databaseUserName, databasePassword);
		}
		catch (Exception ex)
		{
			System.out.println(ex);
		}
				
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String[] resultArray = SignInManager.getInstance().validateSignIn(request);
		
		JSONObject responseJsonObject = new JSONObject();
		responseJsonObject.put("Message", resultArray[0]);
		responseJsonObject.put("userName", resultArray[3]);
		responseJsonObject.put("userId", resultArray[4]);
		response.setStatus(Integer.valueOf(resultArray[2]));
		
		if (resultArray[1] != null)
		{
			Cookie resposeCookie = new Cookie("SESSION", resultArray[1]);
			response.addCookie(resposeCookie);
		}
		
		response.getWriter().append(responseJsonObject.toString());
		
	}

}
