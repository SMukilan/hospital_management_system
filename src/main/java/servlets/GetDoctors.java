package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import applicationVariables.ApplicationVariables;
import enums.AdminStatus;
import enums.DoctorStatus;
import model.Admin;
import model.DocSortByNameAscend;
import model.DocSortByNameDescend;
import model.Doctor;

/**
 * Servlet implementation class GetDoctorsList
 */

public class GetDoctors extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@SuppressWarnings({ "unchecked" })
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		JSONObject responseJsonObject = new JSONObject();
		Gson gson = new GsonBuilder().create();
		List<Doctor> doctors = new LinkedList<Doctor>();
		Admin admin = null;
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
			
			PreparedStatement pstmt = applicationVariables.ApplicationVariables.dbConnection.prepareStatement("select * from Admin where adminUserId like ?");
			pstmt.setString(1, adminId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			admin = new Admin(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), AdminStatus.valueOf(rs.getString(6)), null, null);
			admin.setDoctorsToManage(ApplicationVariables.getInstance().getExistingDoctors(adminId));
			admin.setPatientsToManage(ApplicationVariables.getInstance().getExistingPatients(adminId));
			
			String filter = (String) fullJsonObject.get("filter");
			String sortby = (String) fullJsonObject.get("sortby");
			String searchKey = ((String) fullJsonObject.get("searchKey")).toLowerCase();
			
			for (Doctor doctor : admin.getDoctorsToManage())
			{
				if (doctor.getDoctorStatus().equals(DoctorStatus.AVAILABLE))
					doctors.add(doctor);
			}
			int totalDocs = doctors.size();
			
			if (filter.equals("removed"))
			{
				doctors.clear();
				for (Doctor doctor : admin.getDoctorsToManage())
				{
					if (doctor.getDoctorStatus().equals(DoctorStatus.ARCHIVED))
						doctors.add(doctor);
				}
			}
			else if (filter.equals("all"))
			{
				doctors = admin.getDoctorsToManage();
			}
			
			if (sortby.equals("a-z"))
			{
				Collections.sort(doctors, new DocSortByNameAscend());
			}
			else if (sortby.equals("z-a"))
			{
				Collections.sort(doctors, new DocSortByNameDescend());
			}
			
			LinkedList<Doctor> searchedDocs = new LinkedList<Doctor>();
			for (Doctor doctor : doctors)
			{
				if (doctor.getDoctorName().toLowerCase().contains(searchKey) 
						|| doctor.getDocId().toLowerCase().contains(searchKey)
						|| doctor.getPhNumber().toLowerCase().contains(searchKey))
					searchedDocs.add(doctor);
			}
			doctors = searchedDocs;
			
			String responseJson = gson.toJson(doctors);
			response.getWriter().write(responseJson + totalDocs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response.setStatus(400);
			responseJsonObject.put("Message", "Error occured please give valid input !!");
			response.getWriter().write(responseJsonObject.toJSONString());
		}
		
	}

}
