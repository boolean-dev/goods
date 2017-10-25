package com.tao.cart.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tao.cart.dao.CartItemDao;
import com.tao.cart.domain.CartItem;

import cn.itcast.commons.CommonUtils;

/**
 * 购物车service层
 * @author tao
 *
 */
public class CartItemService {
	CartItemDao cartItemDao=new CartItemDao();
	
	
	/**
	 * 删除购物车某本图书
	 * @param cartItemid
	 */
	public void delete(String cartItemid){
		try {
			cartItemDao.delete(cartItemid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 修改图书数量
	 * @param cartItemid 购物车的id
	 * @param quantity
	 */
	public CartItem updateQuantity(String cartItemid,int quantity){
		try {
			cartItemDao.updateQuantity(cartItemid, quantity);
			return cartItemDao.findByCartItemid(cartItemid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 添加一本图书到购物车
	 * @param cartItem 购物车实体类
	 */
	public void addBookItem(CartItem cartItem){
		
		/*
		 * 添加图书到购物车
		 * 如果该该用户已经有这本书，则将该用户的图书数量+1
		 * 否则新建一个购物车条目
		 */
		CartItem cart;
		try {
			cart=cartItemDao.findByCartItemAndBook(cartItem.getUser().getUid(), cartItem.getBook().getBid());
			if(cart ==null){
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.add(cartItem);
			}else{
				cartItemDao.updateQuantity(cart.getCartItemId(),cart.getQuantity()+cartItem.getQuantity() );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 通过用户id查询结果集
	 * @param uid
	 * @return
	 */
	public List<CartItem> findByUserid(String uid){
		List<CartItem> list=new ArrayList<CartItem>();
		try {
			list = cartItemDao.findByUserid(uid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return list;
	}


	public CartItem findByCartId(String cartItemId) {
		// TODO Auto-generated method stub
		CartItem cartItem=new CartItem();
		try {
			cartItem = cartItemDao.findByCartItemid(cartItemId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		return cartItem;
	}
	

}
