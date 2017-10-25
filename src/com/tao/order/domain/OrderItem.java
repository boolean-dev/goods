package com.tao.order.domain;

import com.tao.book.domain.Book;
import com.tao.user.domain.User;

public class OrderItem {
	
	private String OrderItemId;//订单条目id
	private int quantity;//条目的数量
	private double subtotal;//条目的总计金额
	private Book book;//所包含的书籍
	private Order order;//所属的订单
	
	
	public String getOrderItemId() {
		return OrderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		OrderItemId = orderItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	
	

}
