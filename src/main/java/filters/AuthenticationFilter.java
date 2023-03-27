package filters;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
public class AuthenticationFilter extends HttpFilter implements Filter
{
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
//		System.out.println("Hii");
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		Cookie[] cookies = (httpRequest.getCookies() == null)? new Cookie[]{}: httpRequest.getCookies();
		JSONObject responseObject = new JSONObject();
		
		boolean validated = false;
		for(int i = 0; i < cookies.length; i++)
		{
			
			if (cookies[i].getName().equals("SESSION"))
			{
				
				String sessionId = cookies[i].getValue();
				try
				{
					
					PreparedStatement psmt = applicationVariables.ApplicationVariables.dbConnection.prepareStatement("select * from Sessions where Session like ?");
					psmt.setString(1, sessionId);
					ResultSet rs = psmt.executeQuery();
					if (rs.next())
					{
						validated = true;
						String userid = rs.getString(2);
						request.setAttribute("loggedInUser", userid);
					}
					else
					{
						((HttpServletResponse)response).setStatus(400);
						responseObject.put("Message", "Authentication failed");
					}
					break;
					
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
					((HttpServletResponse)response).setStatus(400);
					responseObject.put("Message", "Some unexpected error occured. please contact Adminstrator");
				}
				
			}
			
		}
		if (validated)
		{
			chain.doFilter(request, response);	
		}
		else
		{
			((HttpServletResponse)response).setStatus(400);
			responseObject.put("Message", "Authentication failed");
			response.getWriter().append(responseObject.toString());
		}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
