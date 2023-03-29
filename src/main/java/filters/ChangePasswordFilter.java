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
 * Servlet Filter implementation class ChangePasswordFilter
 */

public class ChangePasswordFilter extends HttpFilter implements Filter
{
       
    private static final long serialVersionUID = 1L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public ChangePasswordFilter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{

		JSONObject responseObject = new JSONObject();
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
			String password = (String) fullJsonObject.get("password");
			String newPassword = (String) fullJsonObject.get("newPassword");
			String confirmPass = (String) fullJsonObject.get("confirmPass");
			
			PreparedStatement pstmt = ApplicationVariables.dbConnection.prepareStatement("select userName from Admin where adminUserId like ? and AdminStatus like 'ACTIVE'");
			pstmt.setString(1, adminId);
			ResultSet resultSet = pstmt.executeQuery();
			
			if (resultSet.next())
			{
				
				pstmt = ApplicationVariables.dbConnection.prepareStatement("select userName from Admin where adminUserId like ? and userPassword like ?");
				pstmt.setString(1, adminId);
				pstmt.setString(2, password);
				resultSet = pstmt.executeQuery();
				if (resultSet.next())
				{
					
					if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$"))
					{
						((HttpServletResponse)response).setStatus(400);
						responseObject.put("newPassword", "Use a strong password!");
						response.getWriter().append(responseObject.toString());
					}
					else if (!newPassword.equals(confirmPass))
					{
						((HttpServletResponse)response).setStatus(400);
						responseObject.put("newPassword", "Passwords doesn't match!");
						responseObject.put("confirmPass", "Passwords doesn't match!");
						response.getWriter().append(responseObject.toString());
					}
					else
					{
						request.setAttribute("password", password);
						request.setAttribute("newPassword", newPassword);
						chain.doFilter(request, response);
					}
					
				}
				else
				{
					((HttpServletResponse)response).setStatus(400);
					responseObject.put("password", "Wrong password!");
					response.getWriter().append(responseObject.toString());
				}
				
			}
			else
			{
				((HttpServletResponse)response).setStatus(400);
				responseObject.put("Message", "User not found!");
				response.getWriter().append(responseObject.toString());
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			((HttpServletResponse)response).setStatus(400);
			responseObject.put("Message", "Unexpected error occured. Please contact system administrator.");
			response.getWriter().append(responseObject.toString());
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
