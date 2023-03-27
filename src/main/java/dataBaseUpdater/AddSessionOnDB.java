package dataBaseUpdater;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import applicationVariables.ApplicationVariables;

public class AddSessionOnDB extends Thread
{
	
	private String[] values = null;
	
	public AddSessionOnDB(String[] values)
	{
		this.values = values;
	}
	
	public void run()
	{
		executeCommand();
	}
	
	private void executeCommand()
	{
		
		PreparedStatement addSessionPreState = null;
		try
		{
			
			addSessionPreState = ApplicationVariables.dbConnection.prepareStatement("insert into Sessions values(?, ?)");
			
			addSessionPreState.setString(1, values[0]);
			addSessionPreState.setString(2, values[1]);
			
			synchronized (addSessionPreState)
			{
				addSessionPreState.executeUpdate();
			}
			
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}