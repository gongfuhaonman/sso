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
	private String CAS_LOGIN_URL="http://localhost:8080/cas/login.do";
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
			//妫�娴嬫湰鍦癝ession娌℃湁缂撳瓨鏈夌敤鎴蜂俊鎭�
			System.out.println("LOCAL_USER_ID==null");
			String CAS_ST = httpRequest.getParameter(Constants.CAS_ST);
			if(CAS_ST==null) {
				System.out.println("CAS_ST==null");
				//妫�娴嬪埌璇锋眰淇℃伅涓病鏈塖T
				//灏嗚姹傞噸瀹氬悜鍒癈AS鈥擲erver锛屽苟浼犻�� LOCAL_Service
				httpResponse.sendRedirect(CAS_LOGIN_URL + "?"
						+ Constants.LOCAL_SERVICE + "="
						+ httpRequest.getRequestURL());
				
			}else {
				System.out.println("CAS_ST!=null");
				//ST涓嶄负绌�,妫�楠孲T骞惰浆鍙�
				//鍒╃敤httpclient宸ュ叿璁块棶cas 鏈嶅姟鐨�/serviceValidate 鎺ュ彛,
				//灏唖t 銆乻ervice 閮戒紶鍒版鎺ュ彛锛岀敱姝ゆ帴鍙ｉ獙璇乼icket 鐨勬湁鏁堟�э紝
				
					// 鑾峰彇LOCAL_USER_ID
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
						//System.out.printf("璇诲彇鍒扮殑LOCAL_USER_ID鏄�%s",LOCAL_USER_ID);
						reader.close();
						session.setAttribute(Constants.LOCAL_USER_ID,
								LOCAL_USER_ID);
						User user=DB.getUser(LOCAL_USER_ID);
						session.setAttribute("user", user);
						String LOCAL_SERVICE=httpRequest.getRequestURL().toString();
						httpResponse.sendRedirect(LOCAL_SERVICE);
					} catch (Exception e) {
						e.printStackTrace();
						// 璺宠浆鍒癈AS鐧诲綍
						httpResponse.sendRedirect(CAS_LOGIN_URL + "?"
								+ Constants.LOCAL_SERVICE + "="
								+ httpRequest.getRequestURL());
					}
					//sendRedirect闇�瑕佸鐞嗗畬鎴愭暣涓〉闈㈡墠浼氳烦杞紝鎵�浠ユ澶勪笉鑳藉姞chain.dofilter
					//chain.doFilter(request, response);
			}
		}else {
			//LOCAL_USER_ID!=null姝ｅ父璁块棶
			System.out.println("LOCAL_USER_ID!=null姝ｅ父璁块棶");
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
