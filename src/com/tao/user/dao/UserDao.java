package com.tao.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.tao.user.domain.User;

import cn.itcast.jdbc.JdbcUtils;
import cn.itcast.jdbc.TxQueryRunner;


/**
 * 数据库访问层
 * @author tao
 *
 */
public class UserDao {
	QueryRunner query = new TxQueryRunner();
	
	
	/**
	 * 服务器校验用户名是否注册
	 * @param loginname
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql="select count(*)from t_user where(loginname=?)";
		Number num=(Number) query.query(sql, new ScalarHandler(),loginname);
		int bool=num.intValue();
		if(bool!=0){
			return true;
		}else{
			return false;
		}
//		return num == null ? false : num.intValue() > 0;
	}
	
	/**
	 * 校验验证码是否被注册
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql="select count(*)from t_user where(email=?)";
		Number num=(Number) query.query(sql, new ScalarHandler(),email);
		int bool=num.intValue();
		if(bool!=0){
			return true;
		}else{
			return false;
		}
//		return num == null ? false : num.intValue() > 0;
	}
	
	/**
	 * 添加用户
	 * @param user
	 * @throws SQLException
	 */
	public void add(User user) throws SQLException{
		
		
		//uid,name,password,email,statue,activecode
		String sql="insert into t_user values(?,?,?,?,?,?)";
		Object[] params={user.getUid(),user.getLoginname(),user.getLoginpass(),
				user.getEmail(),user.isStatus(),user.getActivationCode()};
		query.update(sql, params);
	}
	
	/**
	 * 通过激活码查找用户，并返回User
	 * @param activationCode
	 * @return
	 * @throws SQLException
	 */
	public User findByActivationCode(String activationCode) throws SQLException{
		String sql="select *from t_user where activationCode=?";
		User user=(User) query.query(sql, new BeanHandler<User>(User.class), activationCode);
		return user;
	}
	
	/**
	 * 通过用户的UId，来更新用户的激活状态
	 * @param uid
	 * @param status
	 * @throws SQLException
	 */
	public void updateStatus(String uid,boolean status) throws SQLException{
		String sql="update t_user set status=1 where uid=?";
		query.update(sql, uid);
	}
	
	
	/**
	 * 通过用户名和密码查询数据库中是否存在用户
	 * 如果存在用户，则登录用户
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public User logindByLoginnameAndLoginpass(User user) throws SQLException{
		String sql="select *from t_user where loginname=? and loginpass=?";
		return query.query(sql, new BeanHandler<User>(User.class), user.getLoginname(),user.getLoginpass());
	}
	
	
	/**
	 * 修改密码之前的查询操作
	 * 通过uid和passworld查找用户是否存在
	 * @param uid
	 * @param pass
	 * @return
	 * @throws SQLException
	 */
	public boolean findByUidAndPass(String uid,String pass) throws SQLException{
		String sql="select count(*)from t_user where(uid=? and loginpass=?)";
		Number num=(Number) query.query(sql, new ScalarHandler(), uid,pass);
		int result=num.intValue();
		if(result>0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 修改密码的dao操作
	 * 通过uid和新密码更新数据库
	 * @param uid
	 * @param newPassword
	 * @throws SQLException
	 */
	public void updatePassword(String uid,String newPassword) throws SQLException{
		String sql="update t_user set loginpass=? where uid=?";
		query.update(sql, newPassword,uid);
	}

}
