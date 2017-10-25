package com.tao.user.domain;

import java.io.Serializable;

/**
 * 实体类
 * 来源：1.数据库user表
 * 		2.表单
 * @author tao
 *
 */

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uid;		//user表主键
	private String loginname;	//用户名
	private String loginpass;	//用户密码
	private String reloginpass;	//用户修改密码
	private String email;	//用户邮箱
	private boolean status;		//用户激活状态,true或者false
	private String verifyCode;	//登录验证码
	private String activationCode;	//用户激活码
	private String newloginpass;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getLoginpass() {
		return loginpass;
	}
	public void setLoginpass(String loginpass) {
		this.loginpass = loginpass;
	}
	public String getReloginpass() {
		return reloginpass;
	}
	public void setReloginpass(String reloginpass) {
		this.reloginpass = reloginpass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public String getNewloginpass() {
		return newloginpass;
	}
	public void setNewloginpass(String newloginpass) {
		this.newloginpass = newloginpass;
	}
	@Override
	public String toString() {
		return "User [uid=" + uid + ", loginname=" + loginname + ", loginpass="
				+ loginpass + ", reloginpass=" + reloginpass + ", email="
				+ email + ", status=" + status + ", verifycode=" + verifyCode
				+ ", activationCode=" + activationCode + "]";
	}
}
