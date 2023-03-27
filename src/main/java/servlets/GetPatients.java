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
import enums.AdmissionStatus;
import model.Admin;
import model.Patient;
import model.PatientSortByAdmissionNoAscend;
import model.PatientSortByAdmissionNoDescend;
import model.PatientSortByNameAscend;
import model.PatientSortByNameDescend;

/**
 * Servlet implementation class GetPatients
 */
public class GetPatients extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		JSONObject responseJsonObject = new JSONObject();
		Gson gson = new GsonBuilder().create();
		List<Patient> patients = new LinkedList<Patient>();
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
			String treatedBy = (String) fullJsonObject.get("treatedBy");
			String searchKey = ((String) fullJsonObject.get("searchKey")).toLowerCase();
			
			for (Patient patient : admin.getPatientsToManage())
			{
				if (patient.getAdmissionStatus().equals(AdmissionStatus.ADMITTED))
					patients.add(patient);
			}
			int totalPatients = patients.size();
			
			if (filter.equals("discharged"))
			{
				patients.clear();
				for (Patient patient : admin.getPatientsToManage())
				{
					if (patient.getAdmissionStatus().equals(AdmissionStatus.DISCHARGED))
						patients.add(patient);
				}
			}
			else if (filter.equals("all"))
			{
				patients = admin.getPatientsToManage();
			}
			
			if (!treatedBy.equals("all"))
			{
				List<Patient> docSortedPatients = new LinkedList<Patient>();
				for (Patient patient : patients)
				{
					if (patient.getTreatedBy().endsWith(treatedBy))
						docSortedPatients.add(patient);
				}
				patients = docSortedPatients;
			}
			
			if (sortby.equals("firstAdmitted"))
			{
				Collections.sort(patients, new PatientSortByAdmissionNoAscend());
			}
			else if (sortby.equals("lastAdmitted"))
			{
				Collections.sort(patients, new PatientSortByAdmissionNoDescend());
			}
			else if (sortby.equals("a-z"))
			{
				Collections.sort(patients, new PatientSortByNameAscend());
			}
			else if (sortby.equals("z-a"))
			{
				Collections.sort(patients, new PatientSortByNameDescend());
			}
			
			LinkedList<Patient> searchedPatients = new LinkedList<Patient>();
			for (Patient patient : patients)
			{
				if (patient.getPatientName().toLowerCase().contains(searchKey) 
						|| (patient.getAdmissionNum() + "").toLowerCase().contains(searchKey)
						|| patient.getPhNumber().toLowerCase().contains(searchKey))
					searchedPatients.add(patient);
			}
			patients = searchedPatients;
			
			String responseJson = gson.toJson(patients);
			response.getWriter().write(responseJson + totalPatients);
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
