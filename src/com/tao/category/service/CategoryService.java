package com.tao.category.service;

import java.sql.SQLException;
import java.util.List;

import com.tao.category.dao.CategoryDao;
import com.tao.category.domain.Category;

public class CategoryService {
	CategoryDao categoryDao=new CategoryDao();
	
	/**
	 * 查找所有的分类
	 * @return
	 */
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 添加分类
	 * @param category
	 * @param pid
	 */
	public void add(Category category,String pid){
		try {
			categoryDao.add(category, pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 查找父分类
	 * @param category
	 * @param pid
	 * @return 
	 */
	public List<Category> findParents(){
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 编辑分类
	 * @param category
	 * @param pid
	 */
	public void edit(Category category,String pid){
		try {
			categoryDao.edit(category, pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查找一个Category
	 * @param cid
	 * @return
	 */
	public Category findCategory(String cid){
		try {
			return categoryDao.findCategory(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public Boolean isDeleteParsent(String cid){
		try {
			if(categoryDao.isDeleteParsent(cid)){
				return true;
			}
			return false;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void delete(String cid){
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Category> findChilds(String cid){
		try {
			return categoryDao.findChildsByPid(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
 	
	
	
	
}
