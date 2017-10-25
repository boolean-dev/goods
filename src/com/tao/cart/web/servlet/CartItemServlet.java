package com.tao.cart.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tao.book.domain.Book;
import com.tao.cart.domain.CartItem;
import com.tao.cart.service.CartItemService;
import com.tao.user.domain.User;

import cn.itcast.servlet.BaseServlet;


/**
 * 购物车servlet层
 * @author tao
 *
 */
public class CartItemServlet extends BaseServlet {
	
	CartItemService cartItemService=new CartItemService();

	
	/**
	 * 通过用户查找购物车
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByUserid(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		User user=(User) request.getSession().getAttribute("sessionUser");
		String uid=user.getUid();
		List<CartItem> listCartItem=cartItemService.findByUserid(uid);
		request.setAttribute("listCartItem", listCartItem);
		return "f:/jsps/cart/list.jsp";
		
	}
	
	
	public String addCardItem(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		CartItem cartItem = new CartItem();
		String bid=request.getParameter("bid");
		Book book = new Book();
		book.setBid(bid);
		User user=(User) request.getSession().getAttribute("sessionUser");
		String qu=request.getParameter("quantity");
		int quantity = Integer.parseInt(qu);
		
		cartItem.setBook(book);
		cartItem.setUser(user);
		cartItem.setQuantity(quantity);
		
		cartItemService.addBookItem(cartItem);
		/*List<CartItem> listCartItem=cartItemService.findByUserid(user.getUid());
		request.setAttribute("listCartItem", listCartItem);*/
		String url=findByUserid(request, response);
		return url;
		
	} 
	
	
	/**
	 * 删除该购物车
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delCardItem(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String cartItemId=request.getParameter("cartItemId");
		User user=(User) request.getSession().getAttribute("sessionUser");
		cartItemService.delete(cartItemId);
		String url=findByUserid(request, response);
		return url;
		
	} 
	
	/**
	 * 批量删除
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String batchDelete(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String  ids=request.getParameter("cartItemIds");
		User user=(User) request.getSession().getAttribute("sessionUser");
		String[] cartItemIds=ids.split(",");
		for(String cartItemId:cartItemIds){
			cartItemService.delete(cartItemId);
		}
		
		String url=findByUserid(request, response);
		return url;
		
	} 
	
	
	
	/**
	 * 更新购物车书籍的数量
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateQuantity(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String cartItemid=request.getParameter("cartItemId");
		String qu=request.getParameter("quantity");
		int quantity=Integer.parseInt(qu);
		CartItem cartItem =cartItemService.updateQuantity(cartItemid, quantity);
		
		StringBuilder sb=new StringBuilder("{");
		sb.append("\"quantity\"").append(":").append(cartItem.getQuantity()).append(",");
		sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
		sb.append("}");
		response.getWriter().print(sb);
		return null;
	}
	
	public  String loadCartItems(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String  ids=request.getParameter("cartItemIds");
		request.setAttribute("cartItemIds", ids);
		User user=(User) request.getSession().getAttribute("sessionUser");
		if(user==null){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "你没有登录，无法进入购物车！");
			return "r:/jsps/msg.jsp";
		}
		String[] cartItemIds=ids.split(",");
		List<CartItem> list=new ArrayList<CartItem>();
		for(String cartItemId:cartItemIds){
			CartItem ci=cartItemService.findByCartId(cartItemId);
			list.add(ci);
		}
		
		request.setAttribute("cartitems", list);
		return "f:/jsps/cart/showitem.jsp";
		
	} 

	
	
	

}
