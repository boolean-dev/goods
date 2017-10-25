package com.tao.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import com.tao.book.domain.Book;
import com.tao.category.dao.CategoryDao;
import com.tao.category.domain.Category;
import com.tao.page.Expression;
import com.tao.page.PageBean;
import com.tao.page.PageConstants;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	
	
	TxQueryRunner qr=new TxQueryRunner();
	
	/**
	 * 通过图书的id来加载图书
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public Book load(String bid) throws SQLException{
		String sql="SELECT * FROM t_book WHERE bid=?";
		Map<String,Object> map=qr.query(sql, new MapHandler(), bid);
		Book book=CommonUtils.toBean(map, Book.class);
		String cid=(String) map.get("cid");
		Category category=new CategoryDao().finParentBypid(cid);
		book.setCategory(category);
		return book;
	}
	
	/**
	 * 通过图书分类查找
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	public PageBean<Book> findByCategory(String cid,int pc) throws SQLException{
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("cid", "=", cid));
		return findByCriteria(list, pc);
	}
	
	/**
	 * 通过图书id查找
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByBookId(String bid,int pc) throws SQLException{
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("bid", "=", bid));
		return findByCriteria(list, pc);
	}
	
	/**
	 * 通过图书名模糊查找
	 * @param name
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> finByName(String name,int pc) throws SQLException{
		List<Expression> list=new ArrayList<Expression>();
		list.add(new Expression("bname","like","%"+name+"%"));
		return findByCriteria(list, pc);
	}
	
	
	/**
	 * 根据作者名模糊查询
	 * @param authorName
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String authorName,int pc) throws SQLException{
		List<Expression> list = new ArrayList<Expression>();
		list.add(new Expression("author","like","%"+authorName+"%"));
		return findByCriteria(list, pc);
	}
	
	
	/**
	 * 根据出版社模糊查询
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByPress(String press,int pc) throws SQLException{
		List<Expression> list = new ArrayList<Expression>();
		list.add(new Expression("press","like","%"+press+"%"));
		return findByCriteria(list, pc);
	}
	
	
	
	
	
	/**
	 * 根据多条件组合查询
	 * @param book
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCombination(Book book,int pc) throws SQLException{
		List<Expression> list = new ArrayList<Expression>();
		list.add(new Expression("bname","like","%"+book.getBname()+"%"));
		list.add(new Expression("author","like","%"+book.getAuthor()+"%"));
		list.add(new Expression("press","like","%"+book.getPress()+"%"));
		return findByCriteria(list, pc);
		
	}
	
	
	/**
	 * 设置标准的查询方法
	 * 其余查询方法都调用这个方法
	 * @param expreList需要查询的参数
	 * @param pc查询的当前页码
	 * @return
	 * @throws SQLException 
	 */
	private PageBean<Book> findByCriteria(List<Expression> expreList,int pc) throws SQLException{
		
		
		
		int ps=PageConstants.BOOK_PAGE_SIZE;
		
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
		
		String countSql="SELECT COUNT(*) FROM t_book"+whereSql;
		Number num=(Number) qr.query(countSql, new ScalarHandler(), params.toArray());
		int tr=num.intValue();
		
		String sql="SELECT * FROM t_book"+whereSql+" ORDER BY orderBy LIMIT ?,?";
		params.add((pc-1)*ps);
		params.add(ps);
		List<Book> listBook=qr.query(sql, new BeanListHandler<Book>(Book.class), params.toArray());
		
		PageBean<Book> pb=new PageBean<Book>();
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		pb.setBeanList(listBook);
		return pb;
	}

	public void addBook(Book book) throws SQLException {
		// TODO Auto-generated method stub
		
		String sql="INSERT INTO t_book ("
				+ "bid, bname, author,price,currPrice,"
				+ "discount,press,publishtime,edition,pageNum,"
				+ "wordNum,printtime,booksize,paper,cid,image_w,image_b) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Object []params={book.getBid(),book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(),book.getCategory().getCid(),
				book.getImage_w(),book.getImage_b()};
		qr.update(sql, params);
		
	}

	public void editBook(Book book) throws SQLException {
		// UPDATE `goods`.`t_book` SET `price` = '12' , `currPrice` = '12' , `discount` = '4' WHERE `bid` = 'xiaoxiao'; 
		String sql="UPDATE t_book SET "
				+ "bname=?, author=?,price=?,currPrice=?,"
				+ "discount=?,press=?,publishtime=?,edition=?,pageNum=?,"
				+ "wordNum=?,printtime=?,booksize=?,paper=?,cid=? "
				+ "WHERE bid=?";
		Object []params={book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(),book.getCategory().getCid(),
				book.getBid()};
		qr.update(sql, params);
	}
	
	
	public void deleteBook(String cid) throws SQLException{
		String sql="DELETE FROM t_book WHERE bid=?";
		qr.update(sql, cid);
	}
	/*@Test
	public void test() throws SQLException{
		BookDao bookDao = new BookDao();
		List<Expression> exprList = new ArrayList<Expression>();
//		exprList.add(new Expression("bid", "=", "1"));
//		exprList.add(new Expression("bname", "like", "%java%"));
//		exprList.add(new Expression("edition", "is null", null));
		bookDao.findByCriteria(exprList, 1);

	}
	*/
	
}
