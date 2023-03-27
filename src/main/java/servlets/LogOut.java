package servlets;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import applicationVariables.ApplicationVariables;

/**
 * Servlet implementation class LogOut
 */

public class LogOut extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogOut()
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		Cookie[] cookies = (request.getCookies() == null)? new Cookie[]{}: request.getCookies();
		response.addCookie(new Cookie("SESSION", ""));
		for(int i = 0; i < cookies.length; i++)
		{
			
			if (cookies[i].getName().equals("SESSION"))
			{
				
				String sessionId = cookies[i].getValue();
				try
				{
					
					PreparedStatement sessionDeleteStatement = ApplicationVariables.dbConnection.prepareStatement("delete from Sessions where Session like ?");
					sessionDeleteStatement.setString(1, sessionId);
					sessionDeleteStatement.executeUpdate();
					break;
					
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				
			}
			
		}
		
	}

}
