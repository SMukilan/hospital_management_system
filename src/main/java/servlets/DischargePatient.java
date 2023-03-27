package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import applicationVariables.ApplicationVariables;

/**
 * Servlet implementation class DischargePatient
 */

public class DischargePatient extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
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
			int admissionNum = Integer.valueOf(fullJsonObject.get("admissionNum") + "");
			
			PreparedStatement pstmt = ApplicationVariables.dbConnection.prepareStatement("select admissionStatus from Patient where managedBy like ? and admissionNum like ?");
			pstmt.setString(1, adminId);
			pstmt.setInt(2, admissionNum);
			ResultSet resultSet = pstmt.executeQuery();
			
			if (resultSet.next())
			{
				
				if (resultSet.getString(1).equals("ADMITTED"))
				{
					pstmt = ApplicationVariables.dbConnection.prepareStatement("update Patient set admissionStatus = 'DISCHARGED', dischargedDate = ? where managedBy like ? and admissionNum like ?");
					pstmt.setString(1, LocalDateTime.now().format(dateTimeFormat));
					pstmt.setString(2, adminId);
					pstmt.setInt(3, admissionNum);
				
					synchronized (pstmt)
					{
						int result = pstmt.executeUpdate();
						if (result == 1)
							responseJsonObject.put("Message", "The patient successfully discharged");
						else
							responseJsonObject.put("Message", "Patient not found!");
					}
					
				}
				else
					responseJsonObject.put("Message", "This patient was already discharged!");
				
			}
			else
				responseJsonObject.put("Message", "Patient not found!");
			
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
