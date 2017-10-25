package com.tao.admin.admin.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tao.admin.admin.domain.Admin;
import com.tao.admin.admin.service.AdminService;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminServlet extends BaseServlet {
	
	AdminService adminService=new AdminService();
	
	public String login(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		Admin form=CommonUtils.toBean(request.getParameterMap(), Admin.class);
		Admin admin=adminService.login(form);
		if(admin == null){
			request.setAttribute("msg", "用户名或密码输入错误");
			return "adminjsps/login.jsp";
		}else{
			request.getSession().setAttribute("admin", admin);
			return "r:/adminjsps/admin/index.jsp";
		}
		
	}
	
	public String exit(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		request.getSession().invalidate();
//		request.getSession().removeAttribute("admin");
		return "f:/adminjsps/login.jsp";
		
	}

}
