package controllers;

import java.io.IOException;

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
import domains.TGT;
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
				if (cookie.getName().equals(Constants.CAS_TGC)) {
					//TGC鍗砈ession id
					String CAS_TGC = cookie.getValue();
					//System.out.printf("cookie涓幏鍙栧埌鐨凜AS_TGC涓�%s\n",CAS_TGC);
					//鑾峰彇鐢ㄦ埛
					User user=DB.getUserByTGC(CAS_TGC);
					if(user!=null) {
					
					//绛惧彂鏂扮殑CAS_ST
					String CAS_ST = user.getId() + System.nanoTime();
					//娣诲姞
					DB.addTGT(user,CAS_ST,CAS_TGC);
					DB.addServiceTicket(CAS_TGC,user);	
					
						response.sendRedirect(LOCAL_SERVICE + "?"
								+ Constants.CAS_ST + "=" + CAS_ST + "&"
								+ Constants.LOCAL_SERVICE + "=" + LOCAL_SERVICE);
						return;
					}
					// 鏈塩ookie
				}
			}
		}
		//CAS鈥擲erver妫�娴嬪埌璇锋眰淇℃伅涓病鏈塗GC锛屾墍浠ヨ烦杞埌鑷繁鐨勭櫥褰曢〉
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
			session.setAttribute("user", user);
			//闅忔満鐢熸垚涓�涓湇鍔＄エ鎹�擲T
			String CAS_ST = user.getId() + System.nanoTime();
			
			//TGC灏辨槸杩欐爣璇嗚繖涓猄ession瀛樺埌Cookie涓殑SessionID
			String CAS_TGC=session.getId();
			Cookie cookie = new Cookie(Constants.CAS_TGC,CAS_TGC );
			cookie.setMaxAge(-1);
			response.addCookie(cookie);
			
			//CAS鈥擲erver浼氱敓鎴愮櫥褰曠エ鎹�擳GT锛堥泦鎴愪簡鐢ㄦ埛淇℃伅涓嶴T锛�

			DB.addTGT(user,CAS_ST,CAS_TGC);
			DB.addServiceTicket(CAS_TGC,user);
			
			if (LOCAL_SERVICE != null && !LOCAL_SERVICE.equals("")) {
				response.sendRedirect(LOCAL_SERVICE + "?"
						+ Constants.CAS_ST + "=" + CAS_ST + "&"
						+ Constants.LOCAL_SERVICE + "=" + LOCAL_SERVICE);
			} else
				response.sendRedirect(request.getContextPath()+"/main.do");
		} else {
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
		}
	}

}
