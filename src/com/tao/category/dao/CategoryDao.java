package com.tao.category.dao;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.tao.category.domain.Category;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class CategoryDao {
	TxQueryRunner txQuery=new TxQueryRunner();
	
	
	
	
	/**
	 * 将一个map转换成Category
	 * @param map
	 * @return
	 */
	private Category toCategory(Map<String,Object> map){
		
		Category category=CommonUtils.toBean(map, Category.class);
		String pid=(String) map.get("pid");
		if(pid != null){
			Category parent=new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		
		return category;
	}
	
	/**
	 * 将一个List<Map<String,Object>>转换成List<Category>
	 * @param categoryListMap
	 * @return
	 */
	private List<Category> toCategoryList(List<Map<String,Object>> categoryListMap){
		List<Category> list=new ArrayList<Category>();
		for (Map map : categoryListMap) {
			Category category=toCategory(map);
			list.add(category);
		}
		return list;
	}
	
	
	/**
	 * 查找所有的分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findAll() throws SQLException{
		
		String psql ="SELECT * FROM t_category WHERE pid  IS NULL ORDER BY orderBy";
		List<Map<String,Object>> listMap=txQuery.query(psql, new MapListHandler());
		List<Category> list=toCategoryList(listMap);
		for (Category parent : list) {
			List<Category> childs=findChildsByPid(parent.getCid());
			parent.setChilds(childs);
		}
		return list;
	}
	
	
	public List<Category> findChildsByPid(String pid) throws SQLException{
		
		String csql="SELECT * FROM t_category WHERE pid=? ORDER BY orderBy";
		List<Map<String,Object>> listMap=txQuery.query(csql, new MapListHandler(), pid);
		List<Category> list=toCategoryList(listMap);
		return list;
	}
	
	/**
	 * 查找父节点，用于回选
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findParents() throws SQLException{
		
		String sql ="SELECT * FROM t_category WHERE pid  IS NULL ORDER BY orderBy";
		List<Map<String,Object>> mapList=txQuery.query(sql, new MapListHandler());
		List<Category> list=toCategoryList(mapList);
		return list;
	}
	
	/**
	 * 添加分类
	 * @return
	 * @throws SQLException
	 */
	public void add(Category category,String pid) throws SQLException{
		
		String sql="INSERT INTO t_category (cid,cname,pid,`desc`) VALUES(?,?,?,?)";
		txQuery.update(sql, category.getCid(),category.getCname(),pid,category.getDesc());
	}
	
	/**
	 * 修改分类
	 * @param category
	 * @param pid
	 * @throws SQLException
	 */
	public void edit(Category category,String pid) throws SQLException{
		String sql="UPDATE t_category SET cname=?,pid=?,`desc`=? WHERE cid=?";
		txQuery.update(sql, category.getCname(),pid,category.getDesc(),category.getCid());
	}
	
	/**
	 * 查找Category，用于修改的回选数据
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public Category findCategory(String cid) throws SQLException{
		String sql="SELECT * FROM t_category WHERE cid=?";
		Map<String,Object> map=txQuery.query(sql, new MapHandler(), cid);
		Category category=toCategory(map);
		return category;
	}
	
	
	public Boolean isDeleteParsent(String pid) throws SQLException{
		
		String csql="SELECT * FROM t_category WHERE pid=? ORDER BY orderBy";
		List<Map<String,Object>> listMap=txQuery.query(csql, new MapListHandler(), pid);
		if(listMap.size()==0 || listMap.isEmpty()){
			return true;
		}
		return false;
	}
	public void delete(String cid) throws SQLException{
		String sql="DELETE FROM t_category WHERE cid=?";
		txQuery.update(sql, cid);
	}
	
	
	public Category finParentBypid(String pid) throws SQLException{
		
		String sql="SELECT * FROM t_category WHERE cid=? ORDER BY orderBy";
		Map<String,Object> map=txQuery.query(sql, new MapHandler(), pid);
		Category category=CommonUtils.toBean(map, Category.class);
		Category parent=new Category();
		parent.setCid((String)map.get("pid"));
		category.setParent(parent);
		return category;
	}
	
	
	
}
