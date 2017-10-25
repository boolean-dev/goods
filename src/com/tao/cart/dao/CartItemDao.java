package com.tao.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.mchange.v2.codegen.bean.BeangenUtils;
import com.tao.book.domain.Book;
import com.tao.cart.domain.CartItem;
import com.tao.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.TxQueryRunner;


/**
 * 购物车dao层
 * @author tao
 *
 */
public class CartItemDao {
	TxQueryRunner qr=new TxQueryRunner();
	
	
	
	/**
	 * 删除购物车中某本书
	 * @param cartItemId 购物车数据id
	 * @throws SQLException
	 */
	public void delete(String cartItemId) throws SQLException{
		String sql="DELETE FROM t_cartitem WHERE cartItemId=?";
		qr.update(sql, cartItemId);
	}
	
	/**
	 * 修改购物车书本数目
	 * @param cartItemid 购物车id
	 * @param count 书本数目
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemid,int count) throws SQLException{
		String sql="UPDATE t_cartitem SET quantity=? WHERE cartItemId=?";
		qr.update(sql, count,cartItemid);
	}
	
	public CartItem findByCartItemid(String cartItemid) throws SQLException{
		
		
		String sql="SELECT * FROM t_cartitem c,t_book b WHERE c.cartItemId=? AND c.bid=b.bid";
		Map<String,Object> map=qr.query(sql, new MapHandler(),cartItemid);
		CartItem cartItem=toBean(map);
		return cartItem;
	}
	
	
	/**
	 * 通过购物车id和书本id查找购物车内是否有数据
	 * @param cartItemId 购物车id
	 * @param bid 书本id
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByCartItemAndBook(String uid,String bid) throws SQLException{
		String sql="SELECT *FROM t_cartitem WHERE uid=? AND bid=?";
		Map<String,Object> map=qr.query(sql, new MapHandler(),uid,bid);
		if(map==null || map.size()==0){
			return null;
		}else{
			CartItem cartItem=CommonUtils.toBean(map, CartItem.class);
			return cartItem;
		}
		
	}
	
	/**
	 * 添加图书到购物车
	 * @param cartItem 购物和实体类
	 * @throws SQLException
	 */
	public void add(CartItem cartItem) throws SQLException{
		String sql="INSERT INTO t_cartitem(cartItemId,quantity,bid,uid) VALUES(?,?,?,?)";
		qr.update(sql, cartItem.getCartItemId(),cartItem.getQuantity(),
				cartItem.getBook().getBid(),cartItem.getUser().getUid());
		
	}
	
	
	/**
	 * 通过用户的id查询购物车
	 * @param uid 用户的id
	 * @return 查询得到的List<CartItem>,其中Book数据也在其中
	 * @throws SQLException
	 */
	public List<CartItem> findByUserid(String uid) throws SQLException{
		
		String sql="SELECT * FROM t_cartitem c,t_book b WHERE c.uid=? AND c.bid=b.bid";
		List<Map<String,Object>> result=qr.query(sql, new MapListHandler(), uid);
		return toBeanList(result);
	}
	
	
	
	
	
	/**
	 * 将查询出来的一个map集合封装成一个CartItem
	 * @param map需要封装的map集合
	 * @return CartItem对象
	 */
	private CartItem toBean(Map<String, Object> map){
		
		CartItem cartItem=CommonUtils.toBean(map, CartItem.class);
		Book book=CommonUtils.toBean(map, Book.class);
		User user=CommonUtils.toBean(map, User.class);
		cartItem.setUser(user);
		cartItem.setBook(book);
		return cartItem;
	}
	
	/**
	 * 将查询出来的ResultList转换成BeanList
	 * @param listMap 要处理的结果集
	 * @return 返回一个CartItem的List集
	 */
	private List<CartItem> toBeanList(List<Map<String,Object>> listMap){
		
		List<CartItem> beanList=new ArrayList<CartItem>();
		for(Map<String,Object> map:listMap){
			CartItem cartItem=new CartItem();
			cartItem=toBean(map);
			beanList.add(cartItem);
		}
		return beanList;
		
	}
	
}
