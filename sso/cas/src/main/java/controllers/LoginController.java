package controllers;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cas.server.Constants;
import database.DB;
import domains.ServiceTicket;
import domains.User;
@WebServlet(value="/login.do")
public class LoginController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String LOCAL_SERVICE=request.getParameter("LOCAL_SERVICE");
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Constants.CAS_TGS)) {
					// 为简化设计TGS=ST
					String CAS_TGS = cookie.getValue();
					String CAS_ST = CAS_TGS;
					ServiceTicket st = DB.findServiceTicketbySt(CAS_ST);
					if (st != null) {
						response.sendRedirect(LOCAL_SERVICE + "?"
								+ Constants.CAS_ST + "=" + CAS_ST + "&"
								+ Constants.LOCAL_SERVICE + "=" + LOCAL_SERVICE);
						return;
					}
					// 有cookie
				}
			}
		}
		//CAS—Server检测到请求信息中没有TGC，所以跳转到自己的登录页；
		request.setAttribute(Constants.LOCAL_SERVICE, LOCAL_SERVICE);
		request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id=request.getParameter("id");
		String pwd=request.getParameter("pwd");
		String LOCAL_SERVICE=request.getParameter("LOCAL_SERVICE");
		User user =DB.findUser(id,pwd);
		HttpSession session=request.getSession();
		if (user != null) {
			session.setAttribute("isLogin", true);
			String token=UUID.randomUUID().toString();
			DB.addServiceTicket(user,token);
			if (LOCAL_SERVICE != null && !LOCAL_SERVICE.equals("")) {
				response.sendRedirect(LOCAL_SERVICE + "?"
						+ Constants.TOKEN + "=" + token+ "&"
						+ Constants.LOCAL_SERVICE + "=" + LOCAL_SERVICE);
			} else
				response.sendRedirect(request.getContextPath()+"/main.do");
		} else {
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
		}
	}

}
