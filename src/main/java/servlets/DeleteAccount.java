package servlets;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import applicationVariables.ApplicationVariables;

/**
 * Servlet implementation class DeleteAccount
 */

public class DeleteAccount extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAccount() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		JSONObject responseObject = new JSONObject();
		String adminId = (String) request.getAttribute("loggedInUser");
		String password = (String) request.getAttribute("password");
		PreparedStatement pstmt;
		try
		{
			
			pstmt = ApplicationVariables.dbConnection.prepareStatement("update Admin set AdminStatus = 'DELETED' where adminUserId like ? and userPassword like ?");
			pstmt.setString(1, adminId);
			pstmt.setString(2, password);
			
			int result = pstmt.executeUpdate();
			if (result == 1)
			{
				
				Cookie[] cookies = (request.getCookies() == null)? new Cookie[]{}: request.getCookies();
				response.addCookie(new Cookie("SESSION", ""));
				
				for(Cookie cookie: cookies)
				{
					if (cookie.getName().equals("SESSION"))
					{
						String sessionId = cookie.getValue();
						pstmt = ApplicationVariables.dbConnection.prepareStatement("delete from Sessions where Session like ?");
						pstmt.setString(1, sessionId);
						pstmt.executeUpdate();
						break;
					}
				}
				responseObject.put("Message", "Your account was succussfully deleted.!");
				
			}
			
			else
			{
				response.setStatus(400);
				responseObject.put("Message", "User not found.!");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.setStatus(400);
			responseObject.put("Message", "Unexpected error occured. Please contact system administrator.");
		}
		response.getWriter().append(responseObject.toString());
		
	}

}
