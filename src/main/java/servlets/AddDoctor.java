package servlets;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import applicationVariables.ApplicationVariables;
import dataBaseUpdater.AddNewDoctor;

/**
 * Servlet implementation class AddDoctor
 */
public class AddDoctor extends HttpServlet
{
	private static final long serialVersionUID = 1L;

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
			String specialist = ((String) request.getAttribute("specialist"));
			String qualification = ((String) request.getAttribute("qualification"));
			String startAvailableTime = ((String) request.getAttribute("startAvailableTime"));
			String amOrPm = ((String) request.getAttribute("amOrPm"));
			String totalAvailTime = ((String) request.getAttribute("totalAvailTime"));
			
			PreparedStatement addDocStatement = ApplicationVariables.dbConnection.prepareStatement("select hopitalName from Admin where adminUserId like ?");
			addDocStatement.setString(1, adminId);
			ResultSet resultSet = addDocStatement.executeQuery();
			resultSet.next();
			
			addDocStatement = ApplicationVariables.dbConnection.prepareStatement("select count(*) from Doctor where managedBy like ?");
			addDocStatement.setString(1, adminId);
			ResultSet resultSet2 = addDocStatement.executeQuery();
			resultSet2.next();
			

			String docId = "AZDOC-" + resultSet.getString(1).charAt(0) + (Integer.valueOf(resultSet2.getString(1)) + 1);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
			Calendar tempCal = Calendar.getInstance();
			tempCal.set(Calendar.HOUR, Integer.valueOf(startAvailableTime));
			
			if (amOrPm.equals("AM"))
				tempCal.set(Calendar.AM_PM, Calendar.AM);
			else if (amOrPm.equals("PM"))
				tempCal.set(Calendar.AM_PM, Calendar.PM);
			
			tempCal.set(Calendar.MINUTE, 0);
			tempCal.set(Calendar.SECOND, 0);
			
			Date tempDate = tempCal.getTime();
			String timing = dateFormat.format(tempDate).toString();
			
			tempCal.add(Calendar.HOUR, Integer.valueOf(totalAvailTime));
			tempDate = tempCal.getTime();
			timing = timing + " - " + dateFormat.format(tempDate).toString();
			
			String[] values = {docId, name, adminId, phoneNumber, specialist, timing, qualification};
			
			AddNewDoctor addNewDoctor = new AddNewDoctor(values);
			addNewDoctor.start();
			
			response.setStatus(200);
			responseJsonObject.put("Message", "The doctor was added successfully");
			
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
