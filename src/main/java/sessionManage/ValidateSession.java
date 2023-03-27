package sessionManage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.Cookie;

import applicationVariables.ApplicationVariables;

public class ValidateSession
{
	
	private static ValidateSession instance = null;
	private ValidateSession() {}
	
	public static ValidateSession getInstance()
	{
		if (instance == null)
			instance = new ValidateSession();
		
		return instance;
	}
	
	public boolean vlidateSession(Cookie[] cookies)
	{
		
		if (cookies == null)
			return false;
		
		for (Cookie cookie : cookies)
		{
			
			if (cookie.getName().equals("SESSION"))
			{

				try
				{
					
					PreparedStatement sessionPreState = ApplicationVariables.dbConnection.prepareStatement("select count(*) from Sessions where Session like ?");
					sessionPreState.setString(1, cookie.getValue());
					
					ResultSet sessionSet = sessionPreState.executeQuery();
					sessionSet.next();
					
					return sessionSet.getInt(1) == 1;
					
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				
			}
			
		}
		return false;
		
	}
	
}
