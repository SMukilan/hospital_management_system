package servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import applicationVariables.ApplicationVariables;

/**
 * Servlet implementation class EditProfile
 */

public class EditProfile extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProfile() {
        super();
        // TODO Auto-generated constructor stub
    }
    
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String adminId = (String) request.getAttribute("loggedInUser");
		JSONObject responseJsonObject = new JSONObject();
		try
		{
			String name = (String) request.getAttribute("name");
			String phoneNumber = ((String) request.getAttribute("phoneNumber"));
			String hospitalName = ((String) request.getAttribute("hospitalName"));
			
			PreparedStatement updateUserStatement = ApplicationVariables.dbConnection.prepareStatement("select AdminStatus from Admin where adminUserId like ?");
			updateUserStatement.setString(1, adminId);
			ResultSet resultSet = updateUserStatement.executeQuery();
			
			if (resultSet.next())
			{
				
				if (resultSet.getString(1).equals("ACTIVE"))
				{
					
					updateUserStatement = ApplicationVariables.dbConnection.prepareStatement("update Admin set userName = ?, phoneNumber = ?, hopitalName = ?, where adminUserId like ?");
					
					updateUserStatement.setString(1, name);
					updateUserStatement.setString(2, phoneNumber);
					updateUserStatement.setString(3, hospitalName);
					updateUserStatement.setString(4, adminId);
				
					synchronized (updateUserStatement)
					{
						int result = updateUserStatement.executeUpdate();
						if (result == 1)
							responseJsonObject.put("Message", "The user's details were successfully updated.");
						else
							responseJsonObject.put("Message", "User not found!");
					}
					
				}
				else
				{
					response.setStatus(400);
					responseJsonObject.put("Message", "User not found!");
				}
			}
			else
			{
				response.setStatus(400);
				responseJsonObject.put("Message", "User not found!");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.setStatus(400);
			responseJsonObject.put("Message", "Some unexpected error occured. please contact Adminstrator");
		}
		
	}

}
