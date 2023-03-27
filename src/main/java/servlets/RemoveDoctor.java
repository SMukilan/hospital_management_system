package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import applicationVariables.ApplicationVariables;

/**
 * Servlet implementation class RemoveDoctor
 */
public class RemoveDoctor extends HttpServlet
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
		
		String currentLine = "";
		String jsonString = "";
		BufferedReader reader = request.getReader();
		currentLine = reader.readLine();
		
		while (currentLine != null)
		{
			jsonString += currentLine;
			currentLine = reader.readLine();
		}
		JSONParser jsonParser = new JSONParser();
		JSONObject fullJsonObject = null;
		
		try
		{
			
			fullJsonObject = (JSONObject) jsonParser.parse(jsonString);
			String docId = (String) fullJsonObject.get("docId");
			
			PreparedStatement pstmt = ApplicationVariables.dbConnection.prepareStatement("select doctorStatus from Doctor where managedBy like ? and docId like ?");
			pstmt.setString(1, adminId);
			pstmt.setString(2, docId);
			ResultSet resultSet = pstmt.executeQuery();
			
			if (resultSet.next())
			{
				
				if (resultSet.getString(1).equals("AVAILABLE"))
				{
					pstmt = ApplicationVariables.dbConnection.prepareStatement("update Doctor set doctorStatus = 'ARCHIVED' where managedBy like ? and docId like ?");
					pstmt.setString(1, adminId);
					pstmt.setString(2, docId);
				
					synchronized (pstmt)
					{
						int result = pstmt.executeUpdate();
						if (result == 1)
							responseJsonObject.put("Message", "The doctor successfully removed");
						else
							responseJsonObject.put("Message", "Doctor not found!");
					}
					
				}
				else
					responseJsonObject.put("Message", "This doctor was already removed!");
				
			}
			else
				responseJsonObject.put("Message", "Doctor not found!");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.setStatus(400);
			responseJsonObject.put("Message", "Error occured please give valid input !!");
		}
		response.getWriter().write(responseJsonObject.toJSONString());
		
	}

}
