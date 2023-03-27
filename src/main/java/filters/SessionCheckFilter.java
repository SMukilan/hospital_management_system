package filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import sessionManage.ValidateSession;

/**
 * Servlet Filter implementation class SessionCheckFilter
 */

public class SessionCheckFilter extends HttpFilter implements Filter
{
       
    private static final long serialVersionUID = 1L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public SessionCheckFilter() {
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
		
		JSONObject responseJsonObject = new JSONObject();
		Cookie[] cookies = ((HttpServletRequest)request).getCookies();
		
		if (ValidateSession.getInstance().vlidateSession(cookies))
		{
			chain.doFilter(request, response);
		}
		else
		{
			responseJsonObject.put("Message", "notValid");
			response.getWriter().append(responseJsonObject.toString());
		}
		
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		// TODO Auto-generated method stub
	}

}
