package dataBaseUpdater;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import applicationVariables.ApplicationVariables;
import enums.AdmissionStatus;
import enums.Gender;

public class AdmitNewPatient extends Thread
{

	private String[] values = null;
	
	public AdmitNewPatient(String[] values)
	{
		this.values = values;
	}
	
	public void run()
	{
		executeCommand();
	}
	
	private void executeCommand()
	{
		
		PreparedStatement admiPatientPreState = null;
		try
		{
			admiPatientPreState = ApplicationVariables.dbConnection.prepareStatement("insert into Patient values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			admiPatientPreState.setInt(1, Integer.valueOf(values[0]));
			admiPatientPreState.setString(2, values[1]);
			admiPatientPreState.setString(3, values[2]);
			admiPatientPreState.setString(4, "Yet to treat");
			admiPatientPreState.setInt(5, Integer.valueOf(values[3]));
			admiPatientPreState.setString(6, values[4]);
			admiPatientPreState.setString(7, Gender.valueOf(values[5].toUpperCase()).toString());
			admiPatientPreState.setString(8, values[6]);
			admiPatientPreState.setString(9, "Yet to discharge");
			admiPatientPreState.setDouble(10, 0);
			admiPatientPreState.setString(11, values[7]);
			admiPatientPreState.setString(12, AdmissionStatus.ADMITTED.toString());
			
			synchronized (admiPatientPreState)
			{
				admiPatientPreState.executeUpdate();
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}

}
