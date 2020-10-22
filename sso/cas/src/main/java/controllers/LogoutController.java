package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cas.server.Constants;
import cas.server.JWTUtils;
import database.DB;
import domains.SessionStorage;
import domains.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
@WebServlet(value="/logout.do")
public class LogoutController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		session.invalidate();
		String LOCAL_SERVICE=request.getParameter("LOCAL_SERVICE");
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Constants.CAS_ST)) {
					String CAS_ST = cookie.getValue();
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					Claims claims = JWTUtils.parseJWT(CAS_ST);
					User user =DB.getUser(claims.getSubject()) ;
					user =DB.getUser(claims.getSubject()) ;
					List<SessionStorage> list=null;
					if(user!=null)  list =DB.findSessionStorage(user.getId());
					try {
						for (SessionStorage item : list) {
							URL url = new URL(item.getLocalService()
									+ "?logout=true&sessionId="
									+ item.getSessionId());
							BufferedReader reader = new BufferedReader(
									new InputStreamReader(url.openStream()));
							reader.readLine();
							reader.close();
						}
					} catch (Exception e) {
						e.printStackTrace();

					}
					DB.deleteSessionStorage(user.getId());

				}
			}
		}
		response.sendRedirect(request.getContextPath()+"/login.do");
	}

}

