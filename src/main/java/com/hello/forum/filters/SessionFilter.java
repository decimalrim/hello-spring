package com.hello.forum.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * Servlet Filter implementation class SessionFilter
 */
public class SessionFilter extends HttpFilter implements Filter {
       
    /**
     * @see HttpFilter#HttpFilter()
     */
    public SessionFilter() {
        super();
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// Request를 Servlet(DispatcherServlet)에게 보내기 전에 필터링 코드 실행
		// 세션을 가져와서 로그인 여부를 판단.
		// Session을 가져오려면, HttpServletRequest에서 가져와야 한다.
		// request 파라미터는 ServletRequest
		// HttpServletRequest is a ServletRequest
		// HttpServletRequest는 ServletRequest를 상속한 클래스.
		HttpServletRequest httpRequest = (HttpServletRequest) request; // 형변환
		HttpSession session = httpRequest.getSession();
		
		// 로그인을 안했다면, 로그인 페이지로 이동.
		if (session.getAttribute("_LOGIN_USER_") == null) {
			// sendRedirect() 는 HttpServletResponse 에서 사용가능.
			// HttpServletResponse 는 ServletResponse 를 상속한 클래스.
			// HttpServletResponse is a ServletResponse
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			// return "redirect:/member/login"; 과 같다.
			httpResponse.sendRedirect("/member/login");
			return;
		}
		
		// place your code here
		// pass the request along the filter chain
		chain.doFilter(request, response);
		
		// Servlet(DispatcherServlet)에서 보내준 Response를 검사하는 필터링 코드 실행.
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
