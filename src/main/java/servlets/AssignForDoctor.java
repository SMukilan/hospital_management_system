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
 * Servlet implementation class AssignForDoctor
 */
public class AssignForDoctor extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AssignForDoctor()
    {
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
			String docId = fullJsonObject.get("docId").toString();
			
			PreparedStatement editPatientStatement = ApplicationVariables.dbConnection.prepareStatement("select doctorStatus from Doctor where managedBy = ? and docId = ?");
			editPatientStatement.setString(1, adminId);
			editPatientStatement.setString(2, docId);
			ResultSet resultSet = editPatientStatement.executeQuery();
			
			if (resultSet.next() && resultSet.getString(1).equals("AVAILABLE"))
			{
				
				editPatientStatement = ApplicationVariables.dbConnection.prepareStatement("select admissionStatus from Patient where managedBy like ? and admissionNum = ?");
				editPatientStatement.setString(1, adminId);
				editPatientStatement.setString(2, admissionNum);
				resultSet = editPatientStatement.executeQuery();
				if (resultSet.next())
				{
					
					if (resultSet.getString(1).equals("ADMITTED"))
					{
						
						editPatientStatement = ApplicationVariables.dbConnection.prepareStatement("update Patient set treatedBy = ? where managedBy like ? and admissionNum = ?");
						
						editPatientStatement.setString(1, docId);
						editPatientStatement.setString(2, adminId);
						editPatientStatement.setInt(3, Integer.valueOf(admissionNum));
						
						synchronized (editPatientStatement)
						{
							int result = editPatientStatement.executeUpdate();
							if (result == 1)
								responseJsonObject.put("Message", "The patient's was successfully assigned for the doctor " + docId + ".");
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
				responseJsonObject.put("docId", "Doctor not found!");
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
