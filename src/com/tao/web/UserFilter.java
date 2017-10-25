package com.tao.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.tao.user.domain.User;

/**
 * Servlet Filter implementation class UserFilter
 */
@WebFilter(
		urlPatterns = { 
				"/UserFilter", 
				"/jsps/order/*", 
				"/jsps/cart/*"
		}, 
		servletNames = { 
				"CartItemServlet", 
				"OrderServlet"
		})
public class UserFilter implements Filter {

    /**
     * Default constructor. 
     */
    public UserFilter() {
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
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
				HttpServletRequest request=(HttpServletRequest) req;
				User user=(User) request.getSession().getAttribute("sessionUser");
				if(user == null ){
					request.setAttribute("code", "error");
					request.setAttribute("msg", "你没有登录，无法进入购物车！");
					request.getRequestDispatcher("/jsps/msg.jsp").forward(req, response);
				}else{
					chain.doFilter(req, response);
				}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
