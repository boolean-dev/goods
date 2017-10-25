package com.tao.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.tao.book.domain.Book;
import com.tao.order.domain.Order;
import com.tao.order.domain.OrderItem;
import com.tao.page.Expression;
import com.tao.page.PageBean;
import com.tao.page.PageConstants;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	
	TxQueryRunner qr=new TxQueryRunner();
	
	
	/**
	 * 通过订单状态查询订单
	 * @param oid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByStatus(String status,int pc) throws SQLException{
		
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("status", "=", status));
		PageBean<Order> pb=findByCriteria(list, pc);
		for (Order order  : pb.getBeanList()) {
			List<OrderItem> orderItems=toOrderItemList(order.getOid());
			order.setOrderItems(orderItems);
		}
		return pb;
	}
	
	public PageBean<Order> findAll(int pc) throws SQLException {
		// TODO Auto-generated method stub
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("1", "=", "1"));
		PageBean<Order> pb=findByCriteria(list, pc);
		for (Order order  : pb.getBeanList()) {
			List<OrderItem> orderItems=toOrderItemList(order.getOid());
			order.setOrderItems(orderItems);
		}
		return pb;
	}
	
	
	
	/**
	 * 取消订单
	 * @param oid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public void  editStatus(String oid,int status) throws SQLException{
		String sql ="UPDATE t_order SET `status`=? WHERE oid=?";
		qr.update(sql,status,oid);
		
	}
	
	
	
	/**
	 * 通过订单的oid查询订单内的详细信息
	 * @param oid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByOid(String oid,int pc) throws SQLException{
		
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("oid", "=", oid));
		PageBean<Order> pb=findByCriteria(list, pc);
		for (Order order  : pb.getBeanList()) {
			List<OrderItem> orderItems=toOrderItemList(order.getOid());
			order.setOrderItems(orderItems);
		}
		return pb;
	}
	
	
	/**
	 * 通过用户名查找
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Order> findByUser(String uid,int pc) throws SQLException{
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("uid", "=", uid));
		PageBean<Order> pb=findByCriteria(list, pc);
		for (Order order  : pb.getBeanList()) {
			List<OrderItem> orderItems=toOrderItemList(order.getOid());
			order.setOrderItems(orderItems);
		}
		return pb;
	}
	
	/**
	 * 通过order的iod，查询该order中所包含的orderitems
	 * @param oid order的iod
	 * @return	查询得到的List<OrderItem>集
	 * @throws SQLException
	 */
	private List<OrderItem> toOrderItemList(String oid) throws SQLException{
		String sql="SELECT * FROM t_orderitem WHERE oid=?";
		List<Map<String, Object>> orderItems=qr.query(sql, new MapListHandler(), oid);
		
		List<OrderItem> list=new ArrayList<OrderItem>();
		for(Map<String, Object> orderItem:orderItems){
			OrderItem or=CommonUtils.toBean(orderItem, OrderItem.class);
			Book book=CommonUtils.toBean(orderItem, Book.class);
			or.setBook(book);
			list.add(or);
		}
		return list;
	}
	
	
	/**
	 * 设置标准的查询方法
	 * 其余查询方法都调用这个方法
	 * @param expreList需要查询的参数
	 * @param pc查询的当前页码
	 * @return
	 * @throws SQLException 
	 */
	private PageBean<Order> findByCriteria(List<Expression> expreList,int pc) throws SQLException{
		
		
		
		int ps=PageConstants.ORDER_PAGE_SIZE;
		
		StringBuilder whereSql=new StringBuilder(" WHERE 1=1");
		List<Object> params=new ArrayList<Object>();
		
		for (Expression expression : expreList) {
			whereSql.append(" ").append("AND ")
			.append(expression.getName()).append(" ")
			.append(expression.getOperator());
			if(!expression.getOperator().equalsIgnoreCase("IS NULL")){
				whereSql.append(" ").append("?");
				params.add(expression.getValue());
			}
		}
		
		String countSql="SELECT COUNT(*) FROM t_order"+whereSql;
		System.out.println(countSql);
		Number num=(Number) qr.query(countSql, new ScalarHandler(), params.toArray());
		int tr=num.intValue();
		System.out.println();
		
		String sql="SELECT * FROM t_order"+whereSql+" ORDER BY ordertime desc LIMIT ?,?";
		System.out.println(sql);
		params.add((pc-1)*ps);
		params.add(ps);
		List<Order> listBook=qr.query(sql, new BeanListHandler<Order>(Order.class), params.toArray());
		
		PageBean<Order> pb=new PageBean<Order>();
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		pb.setBeanList(listBook);
		return pb;
	}

	public void createOrder(Order order) throws SQLException {
		// TODO Auto-generated method stub
		/*
		 * 1. 插入订单
		 */
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] params = {order.getOid(), order.getOrdertime(),
				order.getTotal(),order.getStatus(),order.getAddress(),
				order.getOwner().getUid()};
		qr.update(sql, params);
		
		/*
		 * 2. 循环遍历订单的所有条目,让每个条目生成一个Object[]
		 * 多个条目就对应Object[][]
		 * 执行批处理，完成插入订单条目
		 */
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		int len = order.getOrderItems().size();
		Object[][] objs = new Object[len][];
		for(int i = 0; i < len; i++){
			OrderItem item = order.getOrderItems().get(i);
			objs[i] = new Object[]{item.getOrderItemId(),item.getQuantity(),
					item.getSubtotal(),item.getBook().getBid(),
					item.getBook().getBname(),item.getBook().getCurrPrice(),
					item.getBook().getImage_b(),order.getOid()};
		}
		qr.batch(sql, objs);
		
	}

	
	
	

}
