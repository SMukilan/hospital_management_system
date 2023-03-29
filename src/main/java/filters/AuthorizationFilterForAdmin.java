package filters;

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

/**
 * Servlet Filter implementation class AuthorizationFilter
 */
public class AuthorizationFilterForAdmin extends HttpFilter implements Filter
{
       
    private static final long serialVersionUID = 1L;

	public AuthorizationFilterForAdmin()
    {
        super();
        //  Auto-generated constructor stub
    }

	public void destroy() {
		//  Auto-generated method stub
	}

	
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{

		System.out.println("hello");
		JSONObject responseObject = new JSONObject();
		String adminId = (String) request.getAttribute("loggedInUser");
		
		try
		{
			
			PreparedStatement pstmt = applicationVariables.ApplicationVariables.dbConnection.prepareStatement("select * from Admin where adminUserId like ?");
			pstmt.setString(1, adminId);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				chain.doFilter(request, response);
			}
			else
			{
				((HttpServletResponse)response).setStatus(400);
				responseObject.put("Message", "You don't have enough permission");
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
		//  Auto-generated method stub
	}

}
