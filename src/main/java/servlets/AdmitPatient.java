package servlets;

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

import applicationVariables.ApplicationVariables;
import dataBaseUpdater.AdmitNewPatient;

/**
 * Servlet implementation class AdmitPatient
 */
public class AdmitPatient extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");
		String adminId = (String) request.getAttribute("loggedInUser");
		JSONObject responseJsonObject = new JSONObject();
		try
		{
			
			String name = (String) request.getAttribute("name");
			String phoneNumber = ((String) request.getAttribute("phoneNumber"));
			String age = String.valueOf(request.getAttribute("age"));
			String gender = ((String) request.getAttribute("gender"));
			String disease = ((String) request.getAttribute("disease"));
			
			PreparedStatement admitPatiStatement = ApplicationVariables.dbConnection.prepareStatement("select count(*) from Patient where managedBy like ?");
			admitPatiStatement.setString(1, adminId);
			ResultSet resultSet = admitPatiStatement.executeQuery();
			resultSet.next();
			
			String admissionNum = (resultSet.getInt(1) + 1) + "";
			String admittedTime = LocalDateTime.now().format(dateTimeFormat);
			
			String[] values = {admissionNum, name, adminId, age, phoneNumber, gender, admittedTime, disease};
			
			AdmitNewPatient admitNewPatient = new AdmitNewPatient(values);
			admitNewPatient.start();
			
			response.setStatus(200);
			responseJsonObject.put("Message", "The patient was admitted successfully");
			
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
