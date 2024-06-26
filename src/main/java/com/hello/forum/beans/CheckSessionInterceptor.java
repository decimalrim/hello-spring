package com.hello.forum.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.utils.StringUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckSessionInterceptor implements HandlerInterceptor {
	
	private Logger logger = LoggerFactory.getLogger(CheckSessionInterceptor.class);
	
	
	/**
	 * Controller 실행 전에 Interceptor가 개입
	 * @param request 브라우저가 서버에게 요청한 정보
	 * @param response 서버가 브라우저에게 응답할 정보
	 * @param handler 실행할 Controller
	 * @return Controller 실행을 계속 할 것인지 여부 (false 일 경우, 컨트롤러 실행을 하지 않고 즉시 응답해버린다.)
	 * 
	 */
	@Override // 세션 체크의 경우 preHandle만 써도 충분!
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("_LOGIN_USER_");
		
		// 세션이 없는 경우
		if (memberVO == null) {
			
			// 원래 가려했던 URL 정보 -> 현재 요청중인 URL을 확인.
			// GET, POST, PUT, DELETE, FETCH 등등...
			String httpMethod = request.getMethod().toLowerCase();
			String uri = request.getRequestURI();
//			String url = request.getRequestURL().toString();
			String queryString = request.getQueryString();
			
			logger.debug("HttpMethod: " + httpMethod);
			logger.debug("RequestURI: " + uri);
//			logger.debug("RequestURL: " + url);
			logger.debug("queryString: " + queryString);
			
			if(httpMethod.equals("get")) {
				
				String nextUrl = uri;
				if (! StringUtils.isEmpty(queryString)) {
					nextUrl += "?" + queryString;
				}
				
				// Model 로 로그인 이후에 요청할 주소를 보내준다.
				request.setAttribute("nextUrl", nextUrl);
			}
			
			
			// 로그인 화면을 보여주기 위한 RequestDispatcher 객체 생성
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/Views/member/memberlogin.jsp");
			// 로그인 화면으로 이동시킨다.
			// url은 바뀌지 않는다.
			rd.forward(request, response);
			// 컨트롤러 실행을 하지 않는다.
			return false;
		}
		// 컨트롤러 실행을 계속 한다.
		return true;
	}
	
	/**
	 * Controller 실행된 후에 Interceptor가 개입
	 * @param request 브라우저가 서버에게 요청한 정보
	 * @param response 서버가 브라우저에게 응답할 정보
	 * @param handler 실행한 Controller
	 * @param modelAndView Controller가 반환한 ModelAndView 객체
	 *                     Controller가 String을 반환해도 ModelAndView로 변환된다. 
	 */
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//	}

	/**
	 * 요청과 응답처리가 모두 완료된 후에 Interceptor가 개입
	 * @param request 브라우저가 서버에게 요청한 정보
	 * @param response 서버가 브라우저에게 응답할 정보
	 * @param handler 실행한 Controller
	 * @param ex 실행 중 발생한 예외 객체. 예외가 발생하지 않았으면 null이다.
	 */
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//			throws Exception {
//		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
