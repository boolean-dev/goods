package com.tao.book.service;

import java.sql.SQLException;

import com.tao.book.dao.BookDao;
import com.tao.book.domain.Book;
import com.tao.page.PageBean;

public class BookService {
	
	BookDao bookdao=new BookDao();
	
	/**
	 * 加载客户具体的图书service层方法
	 * @param bid
	 * @return
	 */
	public Book load(String bid){
		try {
			return bookdao.load(bid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过图书分类来查找
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid,int pc){
		
		try {
			return bookdao.findByCategory(cid, pc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public PageBean<Book> findByBookid(String bid,int pc){
		
		try {
			return bookdao.findByBookId(bid, pc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 通过图书名字查找
	 * @param name
	 * @param pc
	 * @return
	 */
	public PageBean<Book> finByName(String name,int pc){
		
		try {
			return bookdao.finByName(name, pc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过作者名字查询
	 * @param authorName
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String authorName,int pc){
		
		try {
			return bookdao.findByAuthor(authorName, pc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过出版社查找
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press,int pc){
		
		try {
			return bookdao.findByPress(press, pc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 多条件组合查询
	 * @param book
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book book,int pc){
		
		try {
			return bookdao.findByCombination(book, pc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	/**
	 * 添加图书
	 * @param book
	 */
	public void addBook(Book book) {
		// TODO Auto-generated method stub
		try {
			bookdao.addBook(book);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public void editBook(Book book) {
		// TODO Auto-generated method stub
		try {
			bookdao.editBook(book);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public void deleteBook(String cid){
		try {
			bookdao.deleteBook(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	

}
