package com.tao.category.web.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tao.category.domain.Category;
import com.tao.category.service.CategoryService;
import com.tao.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class CategoryServlet extends BaseServlet {
	
	CategoryService categoryService=new CategoryService();
	
	/**
	 * 查找所有的分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		
		List<Category> parents=categoryService.findAll();
		request.setAttribute("parents", parents);
		
		return "f:jsps/left.jsp";
	}
	
	
	
	

}
