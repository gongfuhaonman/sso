package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cas.server.Constants;
import cas.server.JWTUtils;
import database.DB;
import domains.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;



/**
 * Servlet Filter implementation class SingleSignOutFilter
 */
@WebFilter(filterName="F1",urlPatterns="/*")
public class JWTFilter implements Filter {
	private String CAS_LOGIN_URL="http://localhost:8080/cas/login.do";
    /**
     * Default constructor. 
     */
    public JWTFilter() {
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest=(HttpServletRequest)request;
		HttpServletResponse httpResponse=(HttpServletResponse)response;
		//HttpSession session=httpRequest.getSession();
		
		String LOCAL_SERVICE=request.getParameter("LOCAL_SERVICE");
		Cookie cookies[] = httpRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(Constants.CAS_ST)) {
			
					String CAS_ST = cookie.getValue();
					System.out.println("CAS_ST:"+CAS_ST);
					User user=null;
					Claims claims=null;
					try {
						claims = JWTUtils.parseJWT(CAS_ST);
					}catch(ExpiredJwtException e1) {
						System.out.println("凭证已过期");
						user=DB.getUser(e1.getClaims().getSubject());
						try {
							CAS_ST = JWTUtils.createJWT(JWTUtils.getUUID(),user.getId() , 10);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cookie = new Cookie(Constants.CAS_ST,CAS_ST );
						cookie.setMaxAge(-1);
						httpResponse.addCookie(cookie);
						httpResponse.sendRedirect(LOCAL_SERVICE + "?"
								+ Constants.CAS_ST + "=" + CAS_ST + "&"
								+ Constants.LOCAL_SERVICE + "=" + LOCAL_SERVICE);
						return;
					}catch( UnsupportedJwtException e2) {
						System.out.println("非法访问");
						httpResponse.sendRedirect(CAS_LOGIN_URL + "?"
								+ Constants.LOCAL_SERVICE + "="
								+ httpRequest.getRequestURL());
						return;
					}
				}
			}
		}
		chain.doFilter(httpRequest, httpResponse);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}



}

