package com.tao.order.service;

import java.sql.SQLException;

import com.tao.order.dao.OrderDao;
import com.tao.order.domain.Order;
import com.tao.page.PageBean;

import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderService {
	
	OrderDao orderDao=new OrderDao();
	
	
	
	
	public PageBean<Order> findByStatus(String status,int pc){
		
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb=orderDao.findByStatus(status, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	
	public void editStatus(String oid,int statue){
		try {
			orderDao.editStatus(oid,statue);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public PageBean<Order> findByOid(String oid,int pc){
		
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb=orderDao.findByOid(oid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}
	
	
	public PageBean<Order> findByUser(String uid,int pc){
		
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb=orderDao.findByUser(uid, pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
	}

	public void createOrder(Order order) {
		// TODO Auto-generated method stub
		try {
			JdbcUtils.beginTransaction();
			orderDao.createOrder(order);
			JdbcUtils.commitTransaction();
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
		
	}


	public PageBean<Order> findAll(int pc) {
		// TODO Auto-generated method stub
		try {
			JdbcUtils.beginTransaction();
			PageBean<Order> pb=orderDao.findAll(pc);
			JdbcUtils.commitTransaction();
			return pb;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {}
			throw new RuntimeException(e);
		}
		
		
	}



}
