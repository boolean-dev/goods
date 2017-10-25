package com.tao.admin.book.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tao.book.domain.Book;
import com.tao.book.service.BookService;
import com.tao.category.domain.Category;
import com.tao.category.service.CategoryService;
import com.tao.page.PageBean;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {

	BookService bookService=new BookService();
	CategoryService categoryService=new CategoryService();
	
	
	
	
	
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
		List<Category> parents=categoryService.findParents();
		req.setAttribute("parents", parents);
		
		Book book=new Book();
		String bid=req.getParameter("bid");
		book=bookService.load(bid);
		List<Category> childs=categoryService.findChilds(book.getCategory().getParent().getCid());
		req.setAttribute("childs", childs);
		req.setAttribute("book", book);
		
		return "f:/adminjsps/admin/book/desc.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		
		
		return "f:/adminjsps/admin/book/list.jsp";
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
		
		return "f:/adminjsps/admin/book/list.jsp";
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
		
		return "f:/adminjsps/admin/book/list.jsp";
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
		
		return "f:/adminjsps/admin/book/list.jsp";
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
		
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	
	/**
	 * 添加图书之前得到一级分类信息，显示在页面上
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		List<Category> parents=categoryService.findParents();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
	
	/**
	 * 将二级分类信息对象的数据形式改写成json
	 * @param category
	 * @return
	 */
	public String toJSON(Category category){
		StringBuffer sb=new StringBuffer("{");
		sb.append("\"cid\":").append("\"").append(category.getCid()).append("\"").append(",");
		sb.append("\"cname\":").append("\"").append(category.getCname()).append("\"").append("}");
		return sb.toString();
	}
	//[{xxx:xxx,yyy:yyy},{}]
	
	/**
	 * ajax调用的方法，通过一级分类传过来的id，寻找到二级分类
	 * 发送到页面上的ajax中
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String loadChild(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String cid=req.getParameter("cid");
		List<Category> childs=categoryService.findChilds(cid);
		StringBuffer sb=new StringBuffer("[");
		for (int i=0;i<childs.size()-1;i++) {
			sb.append(toJSON(childs.get(i)));
			sb.append(",");
		}
		sb.append(toJSON(childs.get(childs.size()-1)));
		sb.append("]");
		System.out.println(sb);
		resp.getWriter().print(sb+"");
		return null;
	}
	
	
	public String editBook(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		Map<String, String[]> map=req.getParameterMap();
		Book book=CommonUtils.toBean(map, Book.class);
		Category category=CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		bookService.editBook(book);
		
		req.setAttribute("msg", "图书修改成功");
		return "f:/adminjsps/msg.jsp";
	}
	
	public String deleteBook(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String bid=req.getParameter("bid");
		bookService.deleteBook(bid);
		req.setAttribute("msg", "删除成功");
		return "f:/adminjsps/msg.jsp";
	}
	
	
}
