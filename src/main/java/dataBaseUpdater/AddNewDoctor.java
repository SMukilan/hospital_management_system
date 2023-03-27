package dataBaseUpdater;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import applicationVariables.ApplicationVariables;
import enums.DoctorStatus;

public class AddNewDoctor extends Thread
{
	
	private String[] values = null;
	
	public AddNewDoctor(String[] values)
	{
		this.values = values;
	}
	
	public void run()
	{
		executeCommand();
	}
	
	private void executeCommand()
	{
		
		PreparedStatement addDocPreState = null;
		try
		{
			addDocPreState = ApplicationVariables.dbConnection.prepareStatement("insert into Doctor values(?, ?, ?, ?, ?, ?, ?, ?)");
			
			addDocPreState.setString(1, values[0]);
			addDocPreState.setString(2, values[1]);
			addDocPreState.setString(3, values[2]);
			addDocPreState.setString(4, values[3]);
			addDocPreState.setString(5, values[4]);
			addDocPreState.setString(6, values[5]);
			addDocPreState.setString(7, values[6]);
			addDocPreState.setString(8, DoctorStatus.AVAILABLE.toString());
			
			synchronized (addDocPreState)
			{
				addDocPreState.executeUpdate();
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
}