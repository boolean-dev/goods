package com.tao.admin.book.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.tao.book.domain.Book;
import com.tao.book.service.BookService;
import com.tao.category.domain.Category;
import com.tao.category.service.CategoryService;

import cn.itcast.commons.CommonUtils;

public class AdminBookAddServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//创建工厂
		FileItemFactory factory =new DiskFileItemFactory();
		
		//创建解析器
		ServletFileUpload sfu=new ServletFileUpload(factory);
		
		//设置文件上传的大小文80K以下
		sfu.setFileSizeMax(150*1024);
		
		
		//从页面得到文件上传的表单数据
		List<FileItem> fileItemList=null;
		try {
			fileItemList=sfu.parseRequest(request);
		} catch (FileUploadException e) {
			error("你上传的文件大于80K", request, response);
			return;
		}
		
		//遍历表单数据，将普通表单数据封装到map中
		Map<String, Object> map=new HashMap<String,Object>();
		for (FileItem fileItem : fileItemList) {
			if(fileItem.isFormField()){
				map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
			}
		}
		
		//将表单中的普通数据得到，封装到book中
		Book book=CommonUtils.toBean(map, Book.class);
		Category category=CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		
		//获取大图文件数据
		FileItem fileItem=fileItemList.get(1);
		
		//得到大图的名字
		String filename=fileItem.getName();
		
		//去除绝对路径，消除浏览器差异
		int index=filename.lastIndexOf("\\");
		if(index != -1){
			filename=filename.substring(index+1);
		}
		
		//给图片名添加uuid，防止文件重名
		filename=CommonUtils.uuid()+"_"+filename;
		if(!filename.toLowerCase().endsWith(".jpg")){
			error("请上传jpg格式的图片", request, response);
			return;
		}
		
		//得到book_image目录在硬盘上的真实目录
		String savepath=this.getServletContext().getRealPath("/book_img");
		
		//设置该图片的存储在硬盘上的真实路径
		File destfile=new File(savepath,filename);
		
		//将图片写入硬盘
		try {
			fileItem.write(destfile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		//创建一个iconimage对象，并传入图片的真实路径
		ImageIcon icon=new ImageIcon(destfile.getAbsolutePath());
		
		//创建一个image对象,将icon图片传入
		Image image=icon.getImage();
		
		//判断图片的高度和宽度是否大于350
		if(image.getWidth(null) >350 || image.getHeight(null)>350){
			error("你上传的相片大图大于350*350", request, response);
			destfile.delete();
			return;
		}
		
		//将图片的名称传入book中
		book.setImage_w("book_img/"+filename);
		
		
		
		//获取小图文件数据
		fileItem=fileItemList.get(2);
		
		//得到大图的名字
		filename=fileItem.getName();
		
		//去除绝对路径，消除浏览器差异
		index=filename.lastIndexOf("\\");
		if(index != -1){
			filename=filename.substring(index+1);
		}
		
		//给图片名添加uuid，防止文件重名
		filename=CommonUtils.uuid()+"_"+filename;
		if(!filename.toLowerCase().endsWith(".jpg")){
			error("请上传jpg格式的图片", request, response);
			return;
		}
		
		//得到book_image目录在硬盘上的真实目录
		savepath=this.getServletContext().getRealPath("/book_img");
		
		//设置该图片的存储在硬盘上的真实路径
		destfile=new File(savepath,filename);
		
		//将图片写入硬盘
		try {
			fileItem.write(destfile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		//创建一个iconimage对象，并传入图片的真实路径
		icon=new ImageIcon(destfile.getAbsolutePath());
		
		//创建一个image对象,将icon图片传入
		image=icon.getImage();
		
		//判断图片的高度和宽度是否大于350
		if(image.getWidth(null) >200 || image.getHeight(null)>200){
			error("你上传的相片小图大于350*350", request, response);
			destfile.delete();
			return;
		}
		
		//将图片的名称传入book中
		book.setImage_b("book_img/"+filename);
				
		
		book.setBid(CommonUtils.uuid());
		BookService bookService=new BookService();
		bookService.addBook(book);
		
		request.setAttribute("msg", "图书添加成功");
		request.getRequestDispatcher("/adminjsps/msg.jsp").
		forward(request, response);
	}
	
	
	private void error(String msg,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setAttribute("msg", msg);
		request.setAttribute("parents", new CategoryService().findParents());
		request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").
		forward(request, response);
		
	}

}
