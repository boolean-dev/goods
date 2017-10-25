package com.tao.cart.domain;

import java.math.BigDecimal;

import com.tao.book.domain.Book;
import com.tao.user.domain.User;

public class CartItem {
	
	private String cartItemId;//购物车id
	private int quantity;//数量
	private Book book;//书籍id
	private User user;//用户id
	
	
	/**
	 * 得到书的总价
	 * 使用BigDecimal不会丢失精读
	 * 构造方法要使用字符串
	 * @return
	 */
	public double getSubtotal(){
		BigDecimal b1=new BigDecimal(book.getCurrPrice()+"");
		BigDecimal b2=new BigDecimal(quantity+"");
		BigDecimal b3=b1.multiply(b2);
		System.out.println(b3);
		return b3.doubleValue();
	}
	
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
