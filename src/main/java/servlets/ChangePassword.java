package servlets;

import java.io.IOException;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import applicationVariables.ApplicationVariables;

/**
 * Servlet implementation class ChangePassword
 */

public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassword() {
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
		String newPassword = (String) request.getAttribute("newPassword");
		
		try
		{
			
			PreparedStatement pstmt = ApplicationVariables.dbConnection.prepareStatement("update Admin set userPassword = ? where adminUserId like ? and userPassword like ? and AdminStatus like 'ACTIVE'");
			pstmt.setString(1, newPassword);
			pstmt.setString(2, adminId);
			pstmt.setString(3, password);
			
			synchronized (pstmt)
			{
				int result = pstmt.executeUpdate();
				if (result == 1)
					responseObject.put("Message", "The password was successfully updated!");
				else
					responseObject.put("Message", "User not found!");
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
