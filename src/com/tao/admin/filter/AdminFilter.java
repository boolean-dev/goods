package com.tao.admin.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.tao.admin.admin.domain.Admin;


public class AdminFilter implements Filter {
	
    public AdminFilter() {
    }

	public void destroy() {
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest=(HttpServletRequest) request;
		Object admin=httpRequest.getSession().getAttribute("admin");
		if(admin==null){
//			request.setAttribute("msg", "你还没有登录，请登录！");
//			request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
			chain.doFilter(request, response);
		}else{
			chain.doFilter(request, response);
		}
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
