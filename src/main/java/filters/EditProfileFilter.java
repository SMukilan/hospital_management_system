package filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import applicationVariables.ApplicationVariables;

/**
 * Servlet Filter implementation class EditProfileFilter
 */

public class EditProfileFilter extends HttpFilter implements Filter
{
       
    private static final long serialVersionUID = 1L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public EditProfileFilter()
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{

		String adminId = (String) request.getAttribute("loggedInUser");
		JSONObject responseJsonObject = new JSONObject();
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
			String name = ((String) fullJsonObject.get("name")).trim();
			String phoneNumber = ((String) fullJsonObject.get("phoneNumber")).trim();
			String hospitalName = ((String) fullJsonObject.get("hospitalName")).trim();
			
			if (!name.matches("[a-zA-Z.\s]*") || name.isEmpty() || name == null)
			{
				((HttpServletResponse)response).setStatus(400);
				responseJsonObject.put("name", "Enter a valid name. Don't use numbers and special characters!");
				response.getWriter().append(responseJsonObject.toString());
			}
			else if (!phoneNumber.matches("[0-9]*") || phoneNumber.length() != 10 || phoneNumber == null)
			{
				((HttpServletResponse)response).setStatus(400);
				responseJsonObject.put("phoneNumber", "Enter a valid phone number");
				response.getWriter().append(responseJsonObject.toString());
			}
			else if (!hospitalName.matches("[a-zA-Z.\s]*") || hospitalName.isEmpty() || hospitalName == null)
			{
				((HttpServletResponse)response).setStatus(400);
				responseJsonObject.put("specialist", "Enter a valid name. Don't use numbers and special characters!");
				response.getWriter().append(responseJsonObject.toString());
			}
			else
			{
				PreparedStatement validateStatement = ApplicationVariables.dbConnection.prepareStatement("select adminUserId from Admin where phoneNumber like ?");
				validateStatement.setString(1, phoneNumber);
				ResultSet resultSet = validateStatement.executeQuery();
				
				if (!resultSet.next())
				{
					request.setAttribute("name", name);
					request.setAttribute("phoneNumber", phoneNumber);
					request.setAttribute("hospitalName", hospitalName);
					chain.doFilter(request, response);
				}
				else
				{
					
					if (resultSet.getString(1).equals(adminId))
					{
						request.setAttribute("name", name);
						request.setAttribute("phoneNumber", phoneNumber);
						request.setAttribute("hospitalName", hospitalName);
						chain.doFilter(request, response);
					}
					else
					{
						((HttpServletResponse)response).setStatus(400);
						responseJsonObject.put("phoneNumber", "This phone number was already taken");
						response.getWriter().append(responseJsonObject.toString());	
					}
					
				}
				
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			((HttpServletResponse)response).setStatus(400);
			responseJsonObject.put("Message", "Some unexpected error occured. please contact Adminstrator");
			response.getWriter().append(responseJsonObject.toString());
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
