package filters;

import java.io.BufferedReader;
import java.io.IOException;
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

/**
 * Servlet Filter implementation class AdmitPatientFilter
 */

public class AdmitPatientFilter extends HttpFilter implements Filter
{
       
    private static final long serialVersionUID = 1L;

	/**
     * @see HttpFilter#HttpFilter()
     */
    public AdmitPatientFilter() {
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
			int age = Integer.valueOf(((String) fullJsonObject.get("age")).trim());
			String gender = ((String) fullJsonObject.get("gender")).trim();
			String disease = ((String) fullJsonObject.get("disease")).trim();
			
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
			else if (age > 150 || age < 1)
			{
				((HttpServletResponse)response).setStatus(400);
				responseJsonObject.put("age", "Enter a valid age between 1 - 150");
				response.getWriter().append(responseJsonObject.toString());
			}
			else if (gender == null || gender.isEmpty() || !(gender.equals("male") || gender.equals("female") || gender.equals("other")))
			{
				((HttpServletResponse)response).setStatus(400);
				responseJsonObject.put("gender", "Enter only Male or Female or Other");
				response.getWriter().append(responseJsonObject.toString());
			}
			else if (!disease.matches("[a-zA-Z.\s]*") || disease.isEmpty() || disease == null)
			{
				((HttpServletResponse)response).setStatus(400);
				responseJsonObject.put("disease", "Enter a valid input. Don't use numbers and special characters!");
				response.getWriter().append(responseJsonObject.toString());
			}
			else
			{
				request.setAttribute("name", name);
				request.setAttribute("phoneNumber", phoneNumber);
				request.setAttribute("age", age);
				request.setAttribute("gender", gender);
				request.setAttribute("disease", disease);
				chain.doFilter(request, response);
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
