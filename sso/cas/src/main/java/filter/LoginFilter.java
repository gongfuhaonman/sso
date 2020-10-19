package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebFilter(urlPatterns="/*")
public class LoginFilter implements Filter{
	
	private String CAS_LOGIN_URL="http://localhost:8080/cas/login.do";
	private String CAS_USER_URL="http://localhost:8080/cas/getUser.do";
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
	    HttpServletResponse res = (HttpServletResponse) response;
	    HttpSession session = req.getSession();
		if((boolean) session.getAttribute("isLogin")) {
			//已登录，放行
			chain.doFilter(request, response);
		}
		//跳转至sso登录
		res.sendRedirect(CAS_LOGIN_URL);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
