package com.tao.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;

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

import com.tao.user.domain.User;
import com.tao.user.service.UserService;

import net.sf.json.JSONObject;

@WebFilter({ "/SaveUserFilter", "/jsps/*" })
public class SaveUserFilter implements Filter {

    public SaveUserFilter() {
    }

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		HttpServletResponse resp=(HttpServletResponse) response;
		
		Cookie[] cookies=req.getCookies();
		for (Cookie cookie : cookies) {
			if("saveUser".equals(cookie.getName())){
				String str=cookie.getValue();
//				System.out.println("json:"+str);
				User user=JSON2User(str);
//				System.out.println(user.getLoginname());
				String loginname=URLDecoder.decode(user.getLoginname(),"utf-8");
				user.setLoginname(loginname);
//				System.out.println(user.getLoginname());
				UserService userService=new UserService();
				try {
					User user1=userService.login(user);
					if(user1!=null){
						req.getSession().setAttribute("sessionUser",user1 );
						chain.doFilter(req, resp);
						return;
//						resp.sendRedirect("/index.jsp");
					}else{
						chain.doFilter(req, resp);
						return;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		chain.doFilter(req, resp);
		return;
	}
	
	public User JSON2User(String jsonStr){
		if(jsonStr.indexOf("[") != -1){  
            jsonStr = jsonStr.replace("[", "");  
        }  
        if(jsonStr.indexOf("]") != -1){  
            jsonStr = jsonStr.replace("]", "");  
        }
        JSONObject obj=new JSONObject().fromObject(jsonStr);
        return (User) JSONObject.toBean(obj,User.class);
	}
	
	
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
