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
 * Servlet implementation class EditPatient
 */

public class EditPatient extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditPatient()
    {
        super();
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
			
			String admissionNum = (String) request.getAttribute("admissionNum");
			String name = (String) request.getAttribute("name");
			String phoneNumber = ((String) request.getAttribute("phoneNumber"));
			String age = String.valueOf(request.getAttribute("age"));
			String gender = ((String) request.getAttribute("gender"));
			String disease = ((String) request.getAttribute("disease"));
			
			PreparedStatement editPatientStatement = ApplicationVariables.dbConnection.prepareStatement("select admissionStatus from Patient where managedBy like ? and admissionNum = ?");
			editPatientStatement.setString(1, adminId);
			editPatientStatement.setString(2, admissionNum);
			ResultSet resultSet = editPatientStatement.executeQuery();
			if (resultSet.next())
			{
				
				if (resultSet.getString(1).equals("ADMITTED"))
				{
					
					editPatientStatement = ApplicationVariables.dbConnection.prepareStatement("update Patient set patientName = ?, phoneNumber = ?, age = ?, "
							+ "gender = ?, disease = ? where managedBy like ? and admissionNum = ?");
					
					editPatientStatement.setString(1, name);
					editPatientStatement.setString(2, phoneNumber);
					editPatientStatement.setInt(3, Integer.valueOf(age));
					editPatientStatement.setString(4, gender);
					editPatientStatement.setString(5, disease);
					editPatientStatement.setString(6, adminId);
					editPatientStatement.setInt(7, Integer.valueOf(admissionNum));
					
					synchronized (editPatientStatement)
					{
						int result = editPatientStatement.executeUpdate();
						if (result == 1)
							responseJsonObject.put("Message", "The patient's details were successfully updated.");
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
			responseJsonObject.put("Message", "Some unexpected error occured. please contact Adminstrator");
		}
		response.getWriter().append(responseJsonObject.toString());
		
	}

}
