package dataBaseUpdater;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import applicationVariables.ApplicationVariables;
import enums.AdminStatus;

public class AddNewAdminOnDB extends Thread
{
	
	private String[] values = null;
	
	public AddNewAdminOnDB(String[] values)
	{
		this.values = values;
	}
	
	public void run()
	{
		executeCommand();
	}
	
	private void executeCommand()
	{
		
		PreparedStatement signUpPreState = null;
		try
		{
			signUpPreState = ApplicationVariables.dbConnection.prepareStatement("insert into Admin values(?, ?, ?, ?, ?, ?)");
			
			signUpPreState.setString(1, values[0]);
			signUpPreState.setString(2, values[1]);
			signUpPreState.setString(3, values[2]);
			signUpPreState.setString(4, values[3]);
			signUpPreState.setString(5, values[4]);
			signUpPreState.setString(6, AdminStatus.ACTIVE.toString());
			
			synchronized (signUpPreState)
			{
				signUpPreState.executeUpdate();
			}
			
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
