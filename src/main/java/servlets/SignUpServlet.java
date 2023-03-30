package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import sessionManage.SignUpManager;

/**
 * Servlet implementation class SignupServlet
 */

public class SignUpServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		String[] resultArray = SignUpManager.getInstance().validateSignUp(request);
		
		JSONObject responseJsonObject = new JSONObject();
		responseJsonObject.put("Message", resultArray[0]);
		response.setStatus(Integer.valueOf(resultArray[2]));
		
		if (resultArray[1] != null)
		{
			Cookie resposeCookie = new Cookie("SESSION", resultArray[1]);
			response.addCookie(resposeCookie);
			responseJsonObject.put("userName", resultArray[3]);
			responseJsonObject.put("userId", resultArray[4]);
			responseJsonObject.put("phoneNumber", resultArray[5]);
			responseJsonObject.put("hospitalName", resultArray[6]);
		}
		else
		{
			response.setStatus(400);
			responseJsonObject.put("name", ((resultArray[3] != null)? resultArray[3]: ""));
			responseJsonObject.put("phoneNumber", ((resultArray[4] != null)? resultArray[4]: ""));
			responseJsonObject.put("password", ((resultArray[5] != null)? resultArray[5]: ""));
			responseJsonObject.put("confirmPassword", ((resultArray[6] != null)? resultArray[6]: ""));
			responseJsonObject.put("hospitalName", ((resultArray[7] != null)? resultArray[7]: ""));
		}
		
		response.getWriter().append(responseJsonObject.toString());
		
	}

}
