package com.hello.forum.beans;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hello.forum.filters.SessionFilter;

import jakarta.servlet.Filter;

@Configuration // Spring Interceptor, Servlet Filter, MVC 설정.
@Configurable
@EnableWebMvc // MVC와 관련된 여러가지 기능들이 활성화 된다.
              // MVC와 관련된 설정들은 이 파일에 작성해야 한다.
              // 그 중 하나가 파라미터 유효성 검사.
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${app.authentication.check-url-pattern:/**}")
	private String authCheckUrlPattern;
	
	@Value("${app.authentication.ignore-url-patterns:}")
	private List<String> authCheckIgnorePatterns;
	
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/Views/",".jsp");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**") // /js/로 시작하는 모든 URL
				.addResourceLocations("classPath:/static/js/");
		registry.addResourceHandler("/css/**") // /css/로 시작하는 모든 URL
				.addResourceLocations("classPath:/static/css/");		
	}
	
	
	// Filter 등록.
//	@Bean // @Bean쓰면 아래 public은 안써도 o
//	FilterRegistrationBean<Filter> filter() {
//		
//		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
//		filterRegistrationBean.setFilter(new SessionFilter());
//		filterRegistrationBean.setUrlPatterns(List.of("/board/write", "/board/modify/*", "/board/delete/*"));
//		return filterRegistrationBean;
//		
//	}
	
	// Interceptor 등록
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		List<String> excludePatterns = new ArrayList<>();
//		excludePatterns.add("/member/regist/**");
//		excludePatterns.add("/member/login");
//		excludePatterns.add("/board/list");
//		excludePatterns.add("/js/**");
//		excludePatterns.add("/css/**");
//		excludePatterns.add("/error/**");
		
		registry.addInterceptor(new CheckSessionInterceptor())
				.addPathPatterns(this.authCheckUrlPattern) // 인터셉터가 개입할 url
				.excludePathPatterns(this.authCheckIgnorePatterns); // 인터셉터가 개입x url
		
		registry.addInterceptor(new BlockDuplicateLoginInterceptor()) 
				.addPathPatterns("/member/login", "/ajax/member/login", "/member/regist", "/ajax/member/regist");
		
	}

}
