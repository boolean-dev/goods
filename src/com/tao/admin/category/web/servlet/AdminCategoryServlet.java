package com.tao.admin.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tao.category.domain.Category;
import com.tao.category.service.CategoryService;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	
	CategoryService categoryService=new CategoryService();

	
	
	public String findAllForBook (HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		List<Category> parents=categoryService.findAll();
		request.setAttribute("parents", parents);
		
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	/**
	 * 查找所有分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String findAll(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		List<Category> parents=categoryService.findAll();
		request.setAttribute("parents", parents);
		
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加父分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String addParent(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Category category=CommonUtils.toBean(request.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category, null);
		return findAll(request, response);
	}
	
	
	/**
	 * 添加子分类之前的回选
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String addChildPre(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		List<Category> parents=categoryService.findParents();
		String pid=(String) request.getParameter("pid");
		request.setAttribute("pid",pid);
		request.setAttribute("parents", parents);
		
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	
	
	/**
	 * 添加子分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public  String addChild(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Category category=CommonUtils.toBean(request.getParameterMap(), Category.class);
		String pid=(String) request.getParameter("pid");
		category.setCid(CommonUtils.uuid());
		request.setAttribute("pid",pid);
		if(pid!=null){
			categoryService.add(category, pid);
		}
		return findAll(request, response);
	}
	
	
	public  String editParentPre(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cid =request.getParameter("cid");
		Category parent=categoryService.findCategory(cid);
		request.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	
	public  String editParent(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Category category=CommonUtils.toBean(request.getParameterMap(), Category.class);
		categoryService.edit(category, null);
		return findAll(request, response);
	}
	
	public  String editChildPre(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cid =request.getParameter("cid");
		List<Category> parents=categoryService.findParents();
		Category child=categoryService.findCategory(cid);
		String pid = request.getParameter("pid");
		request.setAttribute("pid", pid);
		request.setAttribute("child", child);
		request.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	
	public  String editChild(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Category category=CommonUtils.toBean(request.getParameterMap(), Category.class);
		String pid=request.getParameter("pid");
		if(pid!=null){
			categoryService.edit(category, pid);
		}
		
		return findAll(request, response);
	}
	
	public  String deleteParent(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cid=request.getParameter("cid");
		if(categoryService.isDeleteParsent(cid)){
			categoryService.delete(cid);
		}
		return findAll(request, response);
	}
	
	
	public  String deleteChild(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cid=request.getParameter("cid");
		categoryService.delete(cid);
		return findAll(request, response);
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	/*
	public  String findParents(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		List<Category> parents=categoryService.findParent();
		request.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	*/
	
	

}
