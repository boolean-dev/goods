package com.tao.user.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.text.MessageFormat;

import javax.mail.Session;
import javax.management.RuntimeErrorException;

import org.junit.Test;

import com.tao.user.dao.UserDao;
import com.tao.user.domain.User;
import com.tao.user.service.exception.UserException;

import cn.itcast.commons.CommonUtils;
import com.tao.utils.mail.*;
/*import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;*/


/**
 * 用户业务逻辑层
 * @author tao
 *
 */


public class UserService{
	
	private UserDao userDao = new UserDao();
	
	/**
	 * 校验用户名是否被注册，调用dao方法
	 * @param loginname
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		if(userDao.ajaxValidateLoginname(loginname)){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 校验邮箱是否被注册，调用dao方法
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		if(userDao.ajaxValidateEmail(email)){
			return true;
		}else{
			return false;
		}
	}
	
	public void regist(User user) {
		//uid,name,password,email,statue,activecode
		try{
			/*
			 * 1.补全信息
			 */
			user.setUid(CommonUtils.uuid());
			user.setStatus(false);
			user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());
			
			/*
			 * 调用dao方法，添加用户
			 */
			try {
				userDao.add(user);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			 * 发送邮件
			 */
			
			Properties properties=new Properties();
			properties.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
			String host=properties.getProperty("host");
			String username=properties.getProperty("username");
			String password=properties.getProperty("password");
			String from=properties.getProperty("from");
			String to=user.getEmail();
			String subject=properties.getProperty("subject");
//			System.out.println(user.getActivationCode());
			String content=MessageFormat.format(properties.getProperty("content"), 
					user.getActivationCode());
//			System.out.println(content);
			Session session=MailUtils.createSession(host, username, password);
			Mail mail=new Mail(from, to, subject, content);
			MailUtils.send(session, mail);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 先把激活码传过去，然后调用dao层的findByActivationCode()方法，得到当前User
	 * 如果user为空，则抛UserException异常
	 * 如果用户已经激活，也抛UserException异常
	 * @param activationCode
	 * @throws UserException
	 */
	public void activation(String activationCode) throws UserException{
		
		try {
			User user=userDao.findByActivationCode(activationCode);
			if(user==null) throw new UserException("激活码错误");
			if(user.isStatus()) throw new UserException("用户以激活！");
			userDao.updateStatus(user.getUid(), true);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 登录方法
	 * 调用dao层的logindByLoginnameAndLoginpass()方法
	 * 传入一个user对象，返回一个user对象
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public User login(User user) throws SQLException{
		return userDao.logindByLoginnameAndLoginpass(user);
	}
	
	/**
	 * 更新密码service层操作
	 * 调用相应dao层的操作
	 * 如果出错，跑UserException异常
	 * @param uid
	 * @param oldPass
	 * @param newPass
	 * @throws UserException
	 */
	public void updatePassword(String uid,String oldPass,String newPass) throws UserException{
		try {
			if(userDao.findByUidAndPass(uid, oldPass)){
				userDao.updatePassword(uid, newPass);
			}else{
				throw new UserException("用户和旧密码不匹配！");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

}
