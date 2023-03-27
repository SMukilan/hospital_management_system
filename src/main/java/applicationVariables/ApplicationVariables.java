package applicationVariables;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import enums.AdmissionStatus;
import enums.DoctorStatus;
import enums.Gender;
import model.Doctor;
import model.Patient;

public class ApplicationVariables
{

	public static Connection dbConnection = null;
	public static ApplicationVariables instance = null;
	public static Logger mainLogger = Logger.getLogger(ApplicationVariables.class.getName());
	
	private ApplicationVariables() {}
	
	public synchronized static ApplicationVariables getInstance()
	{
		if (instance == null)
			instance = new ApplicationVariables();
		
		return instance;
	} 
	
	public synchronized static void getConnection(String dbname, String username, String password) throws Exception
	{
		if (dbConnection == null)
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname, username, password);
		}
	}
	
	public List<Doctor> getExistingDoctors(String adminId)
	{
		
		List<Doctor> doctors = new LinkedList<Doctor>();
		
		// Adding doctors list managed by admin
		
		try
		{
			
			PreparedStatement preStateToReadyDocs = ApplicationVariables.dbConnection.prepareStatement("select * from Doctor where managedBy like ?");

			preStateToReadyDocs.setString(1, adminId);
			ResultSet resultSetDocs = preStateToReadyDocs.executeQuery();
			
			while (resultSetDocs.next())
			{
				Doctor doctor = new Doctor(resultSetDocs.getString(1), resultSetDocs.getString(2), resultSetDocs.getString(3), resultSetDocs.getString(4),
						resultSetDocs.getString(5), resultSetDocs.getString(6), resultSetDocs.getString(7), DoctorStatus.valueOf(resultSetDocs.getString(8)));
				doctors.add(doctor);
			}
			
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doctors;
		
	}
	
	public List<Patient> getExistingPatients(String adminId)
	{
		
		List<Patient> patients = new LinkedList<Patient>();
		

		// Adding patients list managed by admin
		
		try
		{
			PreparedStatement preStateToReadyPatients = ApplicationVariables.dbConnection.prepareStatement("select * from Patient where managedBy like ?");

			preStateToReadyPatients.setString(1, adminId);
			ResultSet resultSetPatients = preStateToReadyPatients.executeQuery();
			
			while (resultSetPatients.next())
			{
				Patient patient = new Patient(resultSetPatients.getInt(1), resultSetPatients.getString(2), resultSetPatients.getString(3), resultSetPatients.getString(4),
						resultSetPatients.getInt(5), resultSetPatients.getString(6), Gender.valueOf(resultSetPatients.getString(7)), resultSetPatients.getString(8),
						resultSetPatients.getString(9), resultSetPatients.getDouble(10), resultSetPatients.getString(11), AdmissionStatus.valueOf(resultSetPatients.getString(12)));
				patients.add(patient);
			}
			
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patients;
		
	}
	
}
