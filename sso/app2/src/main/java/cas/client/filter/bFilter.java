package cas.client.filter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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

import cas.client.Constants;
import database.DB;
import domains.User;

/**
 * Servlet Filter implementation class SingleSignOnFilter
 */
@WebFilter(filterName="F2",urlPatterns="/*")
public class bFilter implements Filter {
	private String CAS_LOGIN_URL="http://localhost:8080/cas/login.do";//穿衣服走人了麦乐鸡腿堡和炸鸡腿还有麦旋风你只需要
	private String CAS_USER_URL="http://localhost:8080/cas/getUser.do";

	/**
	 * Default constructor.
	 */
	public bFilter() {
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
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		System.out.println("F2");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession();
		String LOCAL_USER_ID = (String) session
				.getAttribute(Constants.LOCAL_USER_ID);
		if(LOCAL_USER_ID==null) {
			//检测本地Session没有缓存有用户信息
			System.out.println("LOCAL_USER_ID==null");
			String CAS_ST = httpRequest.getParameter(Constants.CAS_ST);
			if(CAS_ST==null) {
				System.out.println("CAS_ST==null");
				//检测到请求信息中没有ST
				//将请求重定向到CAS—Server，并传递 LOCAL_Service
				httpResponse.sendRedirect(CAS_LOGIN_URL + "?"
						+ Constants.LOCAL_SERVICE + "="
						+ httpRequest.getRequestURL());
				
			}else {
				System.out.println("CAS_ST!=null");
				//ST不为空,检验ST并转发
				//利用httpclient工具访问cas 服务的/serviceValidate 接口,
				//将st 、service 都传到此接口，由此接口验证ticket 的有效性，
				
					// 获取LOCAL_USER_ID
					try {
						URL url = new URL(CAS_USER_URL + "?" + Constants.CAS_ST
								+ "=" + CAS_ST + "&host="
								+ httpRequest.getServerName() + "&app="
								+ httpRequest.getContextPath() + "&"
								+ Constants.LOCAL_SERVICE + "="
								+ httpRequest.getRequestURL() + "&sessionId="
								+ session.getId());
						System.out.println(url);
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(url.openStream()));
						LOCAL_USER_ID = reader.readLine();
						//System.out.printf("读取到的LOCAL_USER_ID是%s",LOCAL_USER_ID);
						reader.close();
						session.setAttribute(Constants.LOCAL_USER_ID,
								LOCAL_USER_ID);
						User user=DB.getUser(LOCAL_USER_ID);
						session.setAttribute("user", user);
						String LOCAL_SERVICE=httpRequest.getRequestURL().toString();
						httpResponse.sendRedirect(LOCAL_SERVICE);
					} catch (Exception e) {
						e.printStackTrace();
						// 跳转到CAS登录
						httpResponse.sendRedirect(CAS_LOGIN_URL + "?"
								+ Constants.LOCAL_SERVICE + "="
								+ httpRequest.getRequestURL());
					}
				
					//chain.doFilter(request, response);
			}
		}else {
			//LOCAL_USER_ID!=null正常访问
			chain.doFilter(request, response);
		}
			
			
		
	}
	
	


	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
//		CAS_LOGIN_URL = fConfig.getInitParameter(Constants.CAS_LOGIN_URL);
//		CAS_USER_URL = fConfig.getInitParameter(Constants.CAS_USER_URL);
	}

}
