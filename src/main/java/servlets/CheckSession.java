package servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import applicationVariables.ApplicationVariables;

/**
 * Servlet implementation class CheckSession
 */

public class CheckSession extends HttpServlet
{
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		JSONObject responseJsonObject = new JSONObject();
		String adminId = (String) request.getAttribute("loggedInUser");
		try
		{
			PreparedStatement sessionPreState = ApplicationVariables.dbConnection.prepareStatement("select userName, phoneNumber, userPassword, hopitalName from Admin where adminUserId like ?");
			sessionPreState.setString(1, adminId);
			ResultSet resultSet = sessionPreState.executeQuery();
			
			if (resultSet.next())
			{
				responseJsonObject.put("Message", "validSession");
				responseJsonObject.put("userId", adminId);
				responseJsonObject.put("userName", resultSet.getString(1));
				responseJsonObject.put("phoneNumber", resultSet.getString(2));
				responseJsonObject.put("password", "*".repeat(resultSet.getString(3).length()));
				responseJsonObject.put("hopitalName", resultSet.getString(4));
			}
			else
			{
				responseJsonObject.put("Message", "notValid");
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			responseJsonObject.put("Message", "notValid");
		}
		response.getWriter().append(responseJsonObject.toString());
		
	}

}
