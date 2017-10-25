$(function() {
	/**
	 * 1.得到所有的错误信息，循环遍历
	 * 调用一个方法来确定是否显示错误信息
	 */
	$(".labelError").each(function() {//得到class为labelError的元素，循环遍历
		showError($(this));//调用showError函数，且传入当前labelError的id
	});
	
	/**
	 * 2.切换注册按钮的图片
	 */
	$("#submit").hover(
			function(){
				$("#submit").attr("src","/goods/images/regist2.jpg");
			},
			function(){
				$("#submit").attr("src","/goods/images/regist1.jpg")
			}
	);
	
	/**
	 * 当得到输入框的焦点后，该输入框的错误信息消失
	 */
	$(".input").focus(function() {
		var lablId=$(this).attr("id")+"Error";//得到当前的labelError的id
		$("#"+lablId).text("");//将当前labelError的文本内容置为空
		showError($("#"+lablId));//调用showError()函数
	});
	
	/**
	 * 输入框失去焦点，则校验输入框的内容
	 */
	$(".input").blur(function() {
		//得到当前输入框的id的值
		var input=$(this).attr("id");
		//得到当前id所应该调用的方法，方法名得到的方法为(calidate+input的id值的第一个字母大写+剩余的id值+（）
		var funName="validate"+input.substring(0,1).toUpperCase()+input.substring(1)+"()";
		//调用名为funname的方法
		eval(funName);
	});
	
	/**
	 * 提交之前进行校验
	 */
	$("#registForm").submit(function() {
		var bool=true;
		
		if(!validateLoginname()){
			bool=false;
		}
		if(!validateLoginpass()){
			bool=false;
		}
		if(!validateReloginpass()){
			bool=false;
		}
		if(!validateEmail()){
			bool=false;
		}
		if(!validateVerifyCode()){
			bool=false;
		}
		return bool;
	});
			
});

/**
 * 用户名校验
 */

function validateLoginname() {
	
//	alert(1);
	var id="loginname";
	var value=$("#"+id).val();
	/**
	 * 非空校验
	 */
	if(!value){
		$("#"+id+"Error").text("你输入的用户名为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	
	/**
	 * 用户名长度校验
	 */
	if(value.length<3 || value.length>10){
		$("#"+id+"Error").text("请输入3-10个字符串！");
		showError($("#"+id+"Error"));
		return false;
	}
	
	/**
	 * 用户名数据库校验
	 */
	$.ajax({
		
		cache:false,
		async:false,
		type:"POST",
		dataType: "json",
		data:{method:"ajaxValidateLoginname",loginname:value},
		url:"/goods/UserServlet",
		success: function(flag){
			console.log(flag);
			if(flag){
				$("#"+id+"Error").text("用户名已经存在！");
				showError($("#"+id+"Error"));
				return false;
				
			}
		}
	});
	
	return true;
}

/**
 * 登录密码校验
 */

function validateLoginpass() {
	var id="loginpass";
	var value=$("#"+id).val();
	
	/**
	 * 空值校验
	 */
	if(!value){
		$("#"+id+"Error").text("你输入的密码为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	
	/**
	 * 密码长度校验
	 */
	
	if(value.length<3 || value.length>20){
		$("#"+id+"Error").text("请输入的密码少于8位！");
		showError($("#"+id+"Error"));
		return false;
	}
	
	return true;
	
}

/**
 * 确认密码校验
 */

function validateReloginpass() {
	var id="reloginpass";
	var value=$("#"+id).val();
	var repass=$("#loginpass").val();
	
	/**
	 * 空值校验
	 */
	if(!value){
		$("#"+id+"Error").text("你输入的密码为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	
	/**
	 * 密码确认校验
	 */
	if(value!=repass){
		$("#"+id+"Error").text("你输入的两次密码不同！");
		showError($("#"+id+"Error"));
		return false;
	}
	return true;
}

/**
 * 邮箱校验
 */

function validateEmail() {
	
	var id="email";
	var value=$("#"+id).val();
	/**
	 * 空值校验
	 */
	if(!value){
		$("#"+id+"Error").text("你输入的邮箱为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	
	/**
	 * 邮箱格式校验
	 */
	if(!(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value))){  
		$("#"+id+"Error").text("你的邮箱格式错误！");
		showError($("#"+id+"Error"));
		return false;
	}  
	/**
	 * 服务器校验
	 */
	
	$.ajax({
		cache:false,
		async:false,
		type:"POST",
		dataType: "json",
		data:{method:"ajaxValidateEmail",email:value},
		url:"/goods/UserServlet",
		success: function(flag){
			if(flag){
				$("#"+id+"Error").text("邮箱已被注册！");
				showError($("#"+id+"Error"));
				return false;
			}
		}
	});
	
	return true;
}

/**
 * 验证码校验
 */

function validateVerifyCode() {
	
	var id="verifyCode";
	var value=$("#"+id).val();
	/**
	 * 空值校验
	 */
	if(!value){
		$("#"+id+"Error").text("你输入的验证码为空！");
		showError($("#"+id+"Error"));
		return false;
	}
	
	/**
	 * 验证码长度校验
	 */
	if(value.length!=4){  
		$("#"+id+"Error").text("验证码错误！");
		showError($("#"+id+"Error"));
		return false;
	}  
	/**
	 * 服务器校验
	 */
	$.ajax({
		cache:false,
		async:false,
		type:"POST",
		dataType: "json",
		data:{method:"ajaxValidateVerifycode",verifyCode:value},
		url:"/goods/UserServlet",
		success: function(flag){
			if(!flag){
//				alert("111");
				$("#"+id+"Error").text("验证码错误！");
				showError($("#"+id+"Error"));
				return false;
			}
		}
	});
	
	return true;
}


/**
 * 判断当前元素是否有内容，如果有，则不显示，如果没有，则显示
 * @param ele
 */
function showError(ele) {
	var text =ele.text();//获取元素的内容,你输入的用户名为空
	if(!text){//如果没有内容
//		ele.css("display","none");//隐藏元素
		ele.hide();
	}else{//如果有内容
//		ele.css("dispaly","block");//显示元素
		ele.show();
	}
}