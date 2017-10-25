package com.tao.user.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUtils;

import com.sun.mail.util.BASE64EncoderStream;
import com.tao.user.domain.User;
import com.tao.user.service.UserService;
import com.tao.user.service.exception.UserException;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import cn.itcast.vcode.utils.VerifyCode;
import net.sf.json.JSONObject;

/**
 * web持久层
 * @author tao
 *
 */

public class UserServlet extends BaseServlet {
	
	private UserService userService=new UserService();
	
	/**
	 * 注册功能	
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException 
	 */
	public String regist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		/*
		 *封装表单数据到user 
		 */
		User user=CommonUtils.toBean(request.getParameterMap(), User.class);
		
		/*
		 * 对数据进行服务器校验
		 */
		Map<String,String> error=ValidateRegist(user,request);
		if(error!=null && error.size()>0){
			request.setAttribute("error", error);
			request.setAttribute("user", user);
			return "f:jsps/user/regist.jsp";
		}
		
		/*
		 * 调用注册界面注册
		 */
		userService.regist(user);
		/*
		 * 保存注册信息，转发到msg.jsp
		 */
		request.setAttribute("code", "success");
		request.setAttribute("msg", "恭喜你注册成功！请马上前往邮箱激活！");
		return "f:jsps/msg.jsp";
	}
	
	
	private Map<String, String> ValidateRegist(User user, HttpServletRequest request) throws SQLException {
		// TODO Auto-generated method stub
		Map<String,String> error=new HashMap<String, String>();
		/*
		 * 用户名校验 
		 */
		String loginname=user.getLoginname();
		if(loginname==null || loginname.isEmpty()){
			error.put("loginname", "用户名不能为空");
		}else if(loginname.length()<3 || loginname.length()>10){
			error.put("loginname", "用户名长度不在3-10字符之间");
		}else if(userService.ajaxValidateLoginname(loginname)){
			error.put("loginname", "用户名已被注册");
		}
		
		
		/*
		 * 用户密码校验 
		 */
		String loginpass=user.getLoginpass();
		if(loginpass==null || loginpass.isEmpty()){
			error.put("loginpass", "用户名不能为空");
		}else if(loginpass.length()<3 || loginpass.length()>10){
			error.put("loginpass", "用户名长度不在3-10字符之间");
		}
		
		/*
		 * 用户确认密码校验
		 */
		String reloginpass=user.getReloginpass();
		if(reloginpass==null || loginpass.isEmpty()){
			error.put("reloginpass", "用户名不能为空");
		}else if(!reloginpass.equals(loginpass)){
			error.put("reloginpass", "两次密码不同");
		}
		
		/*
		 * 用户名校验 
		 */
		String email=user.getEmail();
		if(email==null || email.isEmpty()){
			error.put("email", "邮箱不能为空");
		}else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
			error.put("email", "邮箱格式不正确");
		}else if(userService.ajaxValidateEmail(email)){
			error.put("email", "邮箱已被注册");
		}
		
		/*
		 * 验证码校验 
		 */
		String verifycode=user.getVerifyCode();
//		String vCode=(String) request.getSession().getAttribute("vCode");
//		System.out.println(vCode);
//		String verifyCode=request.getParameter("verifyCode");
//		System.out.println(verifyCode);
//		boolean flag=verifyCode.equalsIgnoreCase(vCode);
		String vCode= (String) request.getSession().getAttribute("vCode");
		String verifyCode=request.getParameter("verifyCode");
		boolean flag=vCode.equalsIgnoreCase(verifyCode);
		if(verifycode==null || verifycode.isEmpty()){
			error.put("verifycode", "验证码不能为空");
		}else if(!flag){
			error.put("verifycode", "验证码错误");
		}
		return error;
	}


	/**
	 * 校验用户名是否被注册
	 * 从jsp页面得到数据，然后一次调用下层
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String ajaxValidateLoginname(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String loginname=request.getParameter("loginname");
		boolean flag=userService.ajaxValidateLoginname(loginname);
		response.getWriter().print(flag+"");
		return null;
	}
	
	/**
	 * 校验邮箱是否被注册
	 * 从jsp得到数据，调用下层方法
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String ajaxValidateEmail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String email=request.getParameter("email");
		boolean flag=userService.ajaxValidateEmail(email);
		response.getWriter().print(flag+"");
		return null;
	}
	
	/**
	 * 校验验证码模块
	 * 利用验证码工具类，从sessio得到验证码，然后从jsp得到用户输入的验证码，判断是否相同
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String ajaxValidateVerifycode(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String vCode=(String) request.getSession().getAttribute("vCode");
//		System.out.println(vCode);
		String verifyCode=request.getParameter("verifyCode");
//		System.out.println(verifyCode);
		boolean flag=verifyCode.equalsIgnoreCase(vCode);
		response.getWriter().print(flag+"");
		return null;
	}
	
	/**
	 * 首先得到激活码
	 * 再调用service层的activation()方法，激活
	 * 如果service层抛出异常，则说明激活失败
	 * 则我们捕获异常，将激活成功与失败信息传到msg.jsp页面中
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String activation(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		String activationCode=request.getParameter("activationCode");
//		System.out.println(activationCode);
		try {
			userService.activation(activationCode);
			request.setAttribute("msg", "你已激活成功，请登录！");
			request.setAttribute("code", "success");
		} catch (UserException e) {
			// TODO Auto-generated catch block
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("code", "error");
		}
		return "f:jsps/msg.jsp";
	}
	
	
	/**
	 * 用户登录
	 * 先将表单内容封装到user，然后校验数据是否有错
	 * 调用service层的login()方法
	 * 如果service传过来的User为空，则登录失败
	 * 否则，登录成功，保存成功信息，并将user保存到session将username保存到cookie用于回显
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		
		/*
		 * 得到userbean
		 */
		User formUser=CommonUtils.toBean(request.getParameterMap(), User.class);
		
		
		/**
		 * 一周免登陆
		 */
		String isSave=request.getParameter("isSave");
		/*
		 * 对数据进行服务器校验
		 */
		Map<String,String> error=ValidateLogin(formUser,request);
		if(error!=null && error.size()>0){
			request.setAttribute("error", error);
			request.setAttribute("user", formUser);
			return "f:jsps/user/login.jsp";
		}
		
		/*
		 * 调用userservice中的regiest()
		 * 返回一个user对象
		 */
		User user=userService.login(formUser);
		if(user == null){
			request.setAttribute("msg", "用户名错误或密码错误");
			request.setAttribute("user", formUser);
			System.out.println("用户名错误或密码错误");
			return "f:jsps/user/login.jsp";
		}
		if(!user.isStatus()){
			request.setAttribute("msg", "用户未激活！");
			
			return "f:jsps/user/login.jsp";
		}
		
		
		request.getSession().setAttribute("sessionUser", user);
		String loginname =URLEncoder.encode(user.getLoginname(),"utf-8");
		Cookie cookie=new Cookie("uname", loginname);
		
		cookie.setMaxAge(60*60*24*5);
		
		if(null!=isSave && isSave.equals("true")){
			
			user.setLoginname(URLEncoder.encode(user.getLoginname(),"utf-8"));
			//将user转化为json对象
			JSONObject json=JSONObject.fromObject(user);
//			System.out.println(json);
			//将json对象转化成user对象
			String str=json.toString();
//			System.out.println(str);
			Cookie cookie2=new Cookie("saveUser",str);
			
			cookie2.setMaxAge(60*60*24*7);
			response.addCookie(cookie2);
		}
		response.addCookie(cookie);
		return "r:/index.jsp";
	}

	
	/**
	 * 登录的服务器校验
	 * 校验用户名、密码和验证码
	 * @param user
	 * @param request
	 * @return
	 */
	
	private Map<String, String> ValidateLogin(User user, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String,String> error=new HashMap<String, String>();
		/*
		 * 用户名校验 
		 */
		String loginname=user.getLoginname();
		if(loginname==null || loginname.isEmpty()){
			error.put("loginname", "用户名不能为空");
		}else if(loginname.length()<3 || loginname.length()>10){
			error.put("loginname", "用户名长度不在3-10字符之间");
		}
		
		
		/*
		 * 用户密码校验 
		 */
		String loginpass=user.getLoginpass();
		if(loginpass==null || loginpass.isEmpty()){
			error.put("loginpass", "用户名不能为空");
		}else if(loginpass.length()<3 || loginpass.length()>10){
			error.put("loginpass", "用户名长度不在3-10字符之间");
		}
		
		
		/*
		 * 验证码校验 
		 */
		String verifycode=user.getVerifyCode();
		String vCode= (String) request.getSession().getAttribute("vCode");
		String verifyCode=request.getParameter("verifyCode");
		boolean flag=vCode.equalsIgnoreCase(verifyCode);
		if(verifycode==null || verifycode.isEmpty()){
			error.put("verifycode", "验证码不能为空");
		}else if(!flag){
			error.put("email", "验证码错误！");
		}
		return error;
	}
	
	
	
	
	/**
	 * 校验密码修改页面
	 * @param user
	 * @param request
	 * @return
	 */
	private Map<String, String> ValidateUpdatePass(User user, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Map<String,String> error=new HashMap<String, String>();
		
		/*
		 * 用户密码校验 
		 */
		String loginpass=user.getLoginpass();
		if(loginpass==null || loginpass.isEmpty()){
			error.put("loginpass", "用户名不能为空");
		}else if(loginpass.length()<3 || loginpass.length()>10){
			error.put("loginpass", "用户名长度不在3-10字符之间");
		}
		
		/*
		 * 校验新密码
		 */
		
		String newloginpass=user.getNewloginpass();
		
		if(newloginpass==null || newloginpass.isEmpty()){
			error.put("re", "新密码不能为空");
		}else if(newloginpass.length()<3 || newloginpass.length()>10){
			error.put("loginpass", "用户名长度不在3-10字符之间");
		}
		
		/*
		 * 校验新密码新密码确认密码
		 */
		String reloginpass=user.getReloginpass();

		if(reloginpass==null || reloginpass.isEmpty()){
			error.put("re", "新密码不能为空");
		}else if(reloginpass.length()<3 || reloginpass.length()>10){
			error.put("loginpass", "用户名长度不在3-10字符之间");
		}else if(!newloginpass.equals(reloginpass)){
			error.put("loginpass", "两次密码不一样！");
		}
		
		
		/*
		 * 验证码校验 
		 */
		String verifycode=user.getVerifyCode();
		String vCode= (String) request.getSession().getAttribute("vCode");
		String verifyCode=request.getParameter("verifyCode");
		boolean flag=vCode.equalsIgnoreCase(verifyCode);
		if(verifycode==null || verifycode.isEmpty()){
			error.put("verifycode", "验证码不能为空");
		}else if(!flag){
			error.put("email", "验证码错误！");
		}
		return error;
	}
	
	/**
	 * 更新密码操作
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public String updatePassword(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		System.out.println("修改密码");
		/*
		 *封装表单数据到user 
		 */
		User user=CommonUtils.toBean(request.getParameterMap(), User.class);
		
		/*
		 * 对数据进行服务器校验
		 */
		Map<String,String> error=ValidateUpdatePass(user,request);
		if(error!=null && error.size()>0){
			request.setAttribute("error", error);
			request.setAttribute("user", user);
			return "f:jsps/user/pwd.jsp";
		}
		User sessionuser=(User) request.getSession().getAttribute("sessionUser");
		String uid=sessionuser.getUid();
		try {
			userService.updatePassword(uid, user.getLoginpass(), user.getNewloginpass());
			request.setAttribute("code", "success");
			request.setAttribute("msg", "恭喜你修改密码成功！请登录！");
			return "f:jsps/msg.jsp";
		} catch (UserException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			request.setAttribute("msg", e.getMessage());
			return "f:jsps/user/pwd.jsp";
		}
		
	}
	
	/**
	 * 退出操作
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 * @throws Exception
	 */
	public String quit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException, Exception {
		Cookie[] cookies=request.getCookies();
		for (Cookie cookie : cookies) {
			if("saveUser".equals(cookie.getName())){
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
		}
		
		request.getSession().invalidate();
		return "/jsps/user/login.jsp";
	}
}
