package com.tao.admin.admin.service;

import java.sql.SQLException;

import com.tao.admin.admin.dao.AdminDao;
import com.tao.admin.admin.domain.Admin;

public class AdminService {
	AdminDao adminDao=new AdminDao();
	
	public Admin login(Admin admin){
		try {
			return adminDao.find(admin.getAdminname(), admin.getAdminpwd());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
