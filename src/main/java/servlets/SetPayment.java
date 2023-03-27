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
 * Servlet implementation class SetPayment
 */
public class SetPayment extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetPayment() {
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
			String admissionNum = fullJsonObject.get("admissionNum").toString();
			double amount = Double.valueOf(fullJsonObject.get("payment").toString());
			
			if (amount > 0)
			{
				
				PreparedStatement editPatientStatement = ApplicationVariables.dbConnection.prepareStatement("select admissionStatus from Patient where managedBy like ? and admissionNum = ?");
				editPatientStatement.setString(1, adminId);
				editPatientStatement.setString(2, admissionNum);
				ResultSet resultSet = editPatientStatement.executeQuery();
				if (resultSet.next())
				{
					
					if (resultSet.getString(1).equals("ADMITTED"))
					{
						
						editPatientStatement = ApplicationVariables.dbConnection.prepareStatement("update Patient set feePaid = ? where managedBy like ? and admissionNum = ?");
						
						editPatientStatement.setDouble(1, amount);
						editPatientStatement.setString(2, adminId);
						editPatientStatement.setInt(3, Integer.valueOf(admissionNum));
						
						synchronized (editPatientStatement)
						{
							int result = editPatientStatement.executeUpdate();
							if (result == 1)
								responseJsonObject.put("Message", "The patient's payment details was successfully updated.");
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
			else
			{
				response.setStatus(400);
				responseJsonObject.put("payment", "Enter a valid amount!");
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.setStatus(400);
			responseJsonObject.put("Message", "Some unexpected error occured. please contact Adminstrator");
			response.getWriter().append(responseJsonObject.toString());
		}
		response.getWriter().append(responseJsonObject.toString());
		
	}

}
