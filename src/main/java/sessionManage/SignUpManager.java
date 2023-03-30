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
import dataBaseUpdater.AddNewAdminOnDB;
import dataBaseUpdater.AddSessionOnDB;
import enums.AdminStatus;

public class SignUpManager
{
	
	private static SignUpManager instance = null;
	private SignUpManager() {}
	
	public static SignUpManager getInstance()
	{
		if (instance == null)
			instance = new SignUpManager();
		
		return instance;
	}
	
	public String[] validateSignUp(HttpServletRequest request) throws IOException
	{
		
		String[] resultArray = new String[10];
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
		
			String name = (String) fullJsonObject.get("name");
			String phoneNumber = (String) fullJsonObject.get("phoneNumber");
			String password = (String) fullJsonObject.get("password");
			String confirmPassword = (String) fullJsonObject.get("confirmPassword");
			String hospitalName = (String) fullJsonObject.get("hospitalName");
			
			PreparedStatement signUpPreState = ApplicationVariables.dbConnection.prepareStatement("select count(*) from Admin where phoneNumber like ?");
			signUpPreState.setString(1, phoneNumber);
			
			ResultSet result = signUpPreState.executeQuery();
			result.next();
			
			if (result.getInt(1) != 0)
			{
				resultArray[2] = "400";
				resultArray[4] = "This phone number was already taken.!";
			}
			else
			{
				if (!name.matches("[a-zA-Z.\s]*") || name.isEmpty() || name == null)
				{
					resultArray[2] = "400";
					resultArray[3] = "Enter a valid name. Don't use numbers and special characters!";
				}
				else if (!phoneNumber.matches("[0-9]*") || phoneNumber.length() != 10 || phoneNumber == null)
				{
					resultArray[2] = "400";
					resultArray[4] = "Please enter a valid phone number";
				}
				else if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$"))
				{
					resultArray[2] = "400";
					resultArray[5] = "Use a strong password";
				}
				else if (!confirmPassword.equals(password))
				{
					resultArray[2] = "400";
					resultArray[5] = "Password doesn't match.!";
					resultArray[6] = "Password doesn't match.!";
				}
				else if (!hospitalName.matches("[a-zA-Z\s.]*") || hospitalName.isEmpty() || hospitalName == null)
				{
					resultArray[2] = "400";
					resultArray[7] = "Enter a valid hospital name. Don't use numbers and special characters!";
				}
				else
				{
					
					signUpPreState = ApplicationVariables.dbConnection.prepareStatement("select count(*) from Admin");
					ResultSet resultSet = signUpPreState.executeQuery();
					resultSet.next();
					
					String adminUserId = "AZADM-" + hospitalName.charAt(0) + (resultSet.getInt(1) + 1);
					String[] values = {adminUserId, name, phoneNumber, password, hospitalName, AdminStatus.ACTIVE.toString()};
					
					AddNewAdminOnDB newAdminOnDB = new AddNewAdminOnDB(values);
					newAdminOnDB.start();
					
					String session = UUID.randomUUID().toString();
					String[] valuesToSessionDB = {session, adminUserId};
					
					AddSessionOnDB addSessionOnDB = new AddSessionOnDB(valuesToSessionDB);
					addSessionOnDB.start();
					
					resultArray[1] = session;
					resultArray[2] = "200";
					resultArray[3] = name;
					resultArray[4] = adminUserId;
					resultArray[5] = phoneNumber;
					resultArray[6] = hospitalName;
					resultArray[0] = "Signed up successfully.!\nYour user id is " + adminUserId + ".";
					
				}
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
