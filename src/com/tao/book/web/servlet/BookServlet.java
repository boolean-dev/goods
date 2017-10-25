package com.tao.book.web.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.enterprise.context.spi.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tao.book.domain.Book;
import com.tao.book.service.BookService;
import com.tao.page.PageBean;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	
	BookService bookService=new BookService();
	
	
	
	
	
	/**
	 * 获取当前页码数
	 * @param req
	 * @return
	 */
	private int getPc(HttpServletRequest req){
		
		int pc=1;
		String parmas=(String) req.getParameter("pc");
		if(parmas!=null && !parmas.isEmpty()){
			pc=Integer.parseInt(parmas);
		}
		return pc;
	}
	
	
	/**
	 * 获取url
	 * @param req
	 * @return
	 */
	private String getUrl(HttpServletRequest req){
		String url=req.getRequestURI()+"?"+req.getQueryString();
		int index=url.lastIndexOf("&pc=");
		if(index != -1){
			url=url.substring(0,index);
		}
		
		return url;
	}
	
	
	/**
	 * 加载图书的servlet层方法
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Book book=new Book();
		String bid=req.getParameter("bid");
		book=bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
	
	/**
	 * 通過图书分类查找图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取pc
		int pc=getPc(req);
		//获取url
		
		String url=getUrl(req);
		String cid=(String) req.getParameter("cid");
		
		PageBean<Book> pb=new PageBean<Book>();
		
		pb=bookService.findByCategory(cid, pc);
		pb.setUrl(url);
		pb.setPc(pc);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 通过图书名来查找图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByName(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取pc
		int pc=getPc(req);
		//获取url
		String url=getUrl(req);
		String bname=(String) req.getParameter("bname");
		
		PageBean<Book> pb=new PageBean<Book>();
		
		pb=bookService.finByName(bname, pc);
		pb.setUrl(url);
		
		req.setAttribute("pb", pb);
		
		
		return "f:/jsps/book/list.jsp";
	}
	
	
	
	
	
	/**
	 * 通过图书id来查找图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByBookid(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取pc
		int pc=getPc(req);
		//获取url
		String url=getUrl(req);
		String bid=(String) req.getParameter("bid");
		
		PageBean<Book> pb=new PageBean<Book>();
		
		pb=bookService.findByBookid(bid, pc);
		pb.setUrl(url);
		
		req.setAttribute("pb", pb);
		
		return "f:/jsps/book/list.jsp";
	}
	
	
	
	/**
	 * 通过作者名来查询图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取pc
		int pc=getPc(req);
		//获取url
		String url=getUrl(req);
		String author=(String) req.getParameter("author");
		
		PageBean<Book> pb=new PageBean<Book>();
		
		pb=bookService.findByAuthor(author, pc);
		pb.setUrl(url);
		
		req.setAttribute("pb", pb);
		
		return "f:/jsps/book/list.jsp";
	}
	
	
	/**
	 * 通过出版社查询图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByPress(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		req.setCharacterEncoding("UTF-8");
		//获取pc
		int pc=getPc(req);
		//获取url
		String url=getUrl(req);
		
		String[] pre=req.getParameterValues("press");
		String press=req.getParameter("press");
		System.out.println(press);
		
		PageBean<Book> pb=new PageBean<Book>();
		
		pb=bookService.findByPress(press, pc);
		pb.setUrl(url);
		
		req.setAttribute("pb", pb);
		
		return "f:/jsps/book/list.jsp";
	}
	
	
	/**
	 * 多条件组合查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCombination(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取pc
		int pc=getPc(req);
		//获取url
		String url=getUrl(req);
		
		Book book = CommonUtils.toBean(req.getParameterMap(), Book.class);
		
		
		PageBean<Book> pb=new PageBean<Book>();
		
		pb=bookService.findByCombination(book, pc);
		pb.setUrl(url);
		
		req.setAttribute("pb", pb);
		
		return "f:/jsps/book/list.jsp";
	}
	
	
	
	
	

}
