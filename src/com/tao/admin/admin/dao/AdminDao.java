package com.tao.admin.admin.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.BeanHandler;

import com.tao.admin.admin.domain.Admin;

import cn.itcast.jdbc.TxQueryRunner;

public class AdminDao {
	
	TxQueryRunner qr=new TxQueryRunner();
	public Admin find(String adminname,String adminpwd) throws SQLException{
		
		String sql="SELECT * FROM t_admin WHERE adminname=? AND adminpwd=?";
		Admin admin=qr.query(sql, new BeanHandler<Admin>(Admin.class), adminname,adminpwd);
		
		return admin;
	}
}
