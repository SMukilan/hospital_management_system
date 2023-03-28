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

/**
 * Servlet implementation class EditDoctor
 */
public class EditDoctor extends HttpServlet
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
			
			String docId = (String) request.getAttribute("docId");
			String name = (String) request.getAttribute("name");
			String phoneNumber = ((String) request.getAttribute("phoneNumber"));
			String specialist = ((String) request.getAttribute("specialist"));
			String qualification = ((String) request.getAttribute("qualification"));
			String startAvailableTime = ((String) request.getAttribute("startAvailableTime"));
			String amOrPm = ((String) request.getAttribute("amOrPm"));
			String totalAvailTime = ((String) request.getAttribute("totalAvailTime"));
			
			PreparedStatement pstmt = ApplicationVariables.dbConnection.prepareStatement("select doctorStatus from Doctor where managedBy like ? and docId like ?");
			pstmt.setString(1, adminId);
			pstmt.setString(2, docId);
			ResultSet resultSet = pstmt.executeQuery();
			
			if (resultSet.next())
			{
				
				if (resultSet.getString(1).equals("AVAILABLE"))
				{
					
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
					
					pstmt = ApplicationVariables.dbConnection.prepareStatement("update Doctor set doctorName = ?, phoneNumber = ?, specialist = ?, "
							+ "timing = ?, qualification = ? where managedBy like ? and docId like ?");
					
					pstmt.setString(1, name);
					pstmt.setString(2, phoneNumber);
					pstmt.setString(3, specialist);
					pstmt.setString(4, timing);
					pstmt.setString(5, qualification);
					pstmt.setString(6, adminId);
					pstmt.setString(7, docId);
				
					synchronized (pstmt)
					{
						int result = pstmt.executeUpdate();
						if (result == 1)
							responseJsonObject.put("Message", "The doctor's details were successfully updated.");
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
			responseJsonObject.put("Message", "Some unexpected error occured. please contact Adminstrator");
		}
		response.getWriter().append(responseJsonObject.toString());
		
	}

}
