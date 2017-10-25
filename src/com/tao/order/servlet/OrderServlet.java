package com.tao.order.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tao.cart.domain.CartItem;
import com.tao.cart.service.CartItemService;
import com.tao.order.domain.Order;
import com.tao.order.domain.OrderItem;
import com.tao.order.service.OrderService;
import com.tao.page.PageBean;
import com.tao.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	
	OrderService orderService=new OrderService();
	CartItemService cartItemService=new CartItemService();
	
	
	
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
	
	
	public String payPre(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String oid=request.getParameter("oid");
		String total=request.getParameter("total");
		request.setAttribute("oid", oid);
		request.setAttribute("total", total);
		return "f:/jsps/order/pay.jsp";
	}
	
	public String editStatue(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String oid=request.getParameter("oid");
		String sta=request.getParameter("statue");
		int status=Integer.parseInt(sta);
		orderService.editStatus(oid,status);
		return findByOid(request, response);
	}
	
	public String findByOid(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String oid=request.getParameter("oid");
		int pc=getPc(request);
		PageBean<Order> pb=orderService.findByOid(oid, pc);
		pb.setUrl(getUrl(request));
		request.setAttribute("pb", pb);
		return "f:jsps/order/desc.jsp";
	}
	
	public String findByUser(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		User user=(User) request.getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		int pc=getPc(request);
		PageBean<Order> pb=orderService.findByUser(uid, pc);
		pb.setUrl(getUrl(request));
		request.setAttribute("pb", pb);
		return "f:jsps/order/list.jsp";
	}
	
	public String addOrder(HttpServletRequest req, HttpServletResponse response) 
			throws ServletException, IOException {
		
		/*
		 * 1. 获取所有购物车条目的id，查询之
		 */
		String ids = req.getParameter("cartItemIds");
		String[] cartItemIds=ids.split(",");
		List<CartItem> cartItemList=new ArrayList<CartItem>();
		for(String cartItemId:cartItemIds){
			CartItem ci=cartItemService.findByCartId(cartItemId);
			cartItemList.add(ci);
		}
		
		/*
		 * 2. 创建Order
		 */
		Order order = new Order();
		order.setOid(CommonUtils.uuid());//设置主键
		order.setOrdertime(String.format("%tF %<tT", new Date()));//下单时间
		order.setStatus(1);//设置状态，1表示未付款
		order.setAddress(req.getParameter("address"));//设置收货地址
		User owner = (User)req.getSession().getAttribute("sessionUser");
		order.setOwner(owner);//设置订单所有者
		
		BigDecimal total = new BigDecimal("0");
		for(CartItem cartItem : cartItemList) {
			total = total.add(new BigDecimal(cartItem.getSubtotal() + ""));
		}
		order.setTotal(total.doubleValue());//设置总计
		
		/*
		 * 3. 创建List<OrderItem>
		 * 一个CartItem对应一个OrderItem
		 */
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(CartItem cartItem : cartItemList) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemId(CommonUtils.uuid());//设置主键
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setBook(cartItem.getBook());
			orderItem.setOrder(order);
			orderItemList.add(orderItem);
		}
		order.setOrderItems(orderItemList);
		
		/*
		 * 4. 调用service完成添加
		 */
		orderService.createOrder(order);
		
		// 删除购物车条目
		for(String cartItemId:cartItemIds){
			cartItemService.delete(cartItemId);
		}
		/*
		 * 5. 保存订单，转发到ordersucc.jsp
		 */
		req.setAttribute("order", order);
		return "f:/jsps/order/ordersucc.jsp";
	}
	
	
	
	
	

}
