package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cas.server.Constants;
import cas.server.JWTUtils;
import database.DB;
import domains.Mapping;

import domains.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
@WebServlet(value="/getUser.do")
public class GetUserController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String CAS_ST=request.getParameter("CAS_ST");
		String host=request.getParameter("host");
		String app=request.getParameter("app");
		String LOCAL_SERVICE=request.getParameter("LOCAL_SERVICE");
		String sessionId=request.getParameter("sessionId");
		Claims claims = JWTUtils.parseJWT(CAS_ST);
		User user =DB.getUser(claims.getSubject()) ;
		if(user==null) {
			System.out.println("");
			return;
		}	
		DB.addSessionStorage(LOCAL_SERVICE,user.getId(),sessionId);
		
		Mapping mapping = DB.findMappingByHostAndAppAndCasUser(host,app,user);
		
		response.getWriter().println(mapping.getLocalUser());
	}

}
