package sessionManage;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import applicationVariables.ApplicationVariables;
import dataBaseUpdater.AddSessionOnDB;

public class SignInManager
{
	
	private static SignInManager instance = null;
	private SignInManager() {}
	
	public static SignInManager getInstance()
	{
		if (instance == null)
			instance = new SignInManager();
		
		return instance;
	}
	
	public String[] validateSignIn(HttpServletRequest request) throws IOException
	{
		
		String[] resultArray = new String[7];
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
		
		try
		{
			
			JSONObject fullJsonObject = (JSONObject) jsonParser.parse(jsonString);
			
			String userId = (String) fullJsonObject.get("userId");
			String password = (String) fullJsonObject.get("password");
			
			PreparedStatement signInPreStmt =
					ApplicationVariables.dbConnection.prepareStatement("select * from Admin where (adminUserId like ? and userPassword like ? and AdminStatus like ?) "
							+ "or (phoneNumber like ? and userPassword like ? and AdminStatus like ?)");
			signInPreStmt.setString(1, userId);
			signInPreStmt.setString(2, password);
			signInPreStmt.setString(3, "ACTIVE");
			signInPreStmt.setString(4, userId);
			signInPreStmt.setString(5, password);
			signInPreStmt.setString(6, "ACTIVE");
			
			ResultSet result = signInPreStmt.executeQuery();
			
			if (result.next())
			{
				
				String session = UUID.randomUUID().toString();
				String[] valuesToSessionDB = {session, result.getString(1)};
				
				AddSessionOnDB addSessionOnDB = new AddSessionOnDB(valuesToSessionDB);
				addSessionOnDB.start();
				
				resultArray[1] = session;
				resultArray[2] = "200";
				resultArray[3] = result.getString(2);
				resultArray[4] = result.getString(1);
				resultArray[5] = result.getString(3);
				resultArray[6] = result.getString(5);
				resultArray[0] = "Signed in successfully.!";
				
			}
			else
			{
				resultArray[2] = "400";
				resultArray[0] = "Incorrect user id or password..! Please enter valid inputs";
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resultArray[2] = "400";
			resultArray[0] = "Error occured please give valid input !!";
		}
		
		return resultArray;
		
	}
	
}