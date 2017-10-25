<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>cartlist.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script src="<c:url value='/js/round.js'/>"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/list.css'/>">
<script type="text/javascript">
$(function() {
	showTotal();//显示合计
	// 给全选按钮添加点击事件
	$("#selectAll").click(function() {
		var flag = $(this).attr("checked");//获取全选的状态
		setAll(flag);//让所有条目复选框与全选同步
		setJieSuanStyle(flag);//让结算按钮与全选同步
	});
	
	// 给条目复选框添加事件
	$(":checkbox[name=checkboxBtn]").click(function() {
		var selectedCount = $(":checkbox[name=checkboxBtn][checked=true]").length;//被勾选复选框个数
		var allCount = $(":checkbox[name=checkboxBtn]").length;//所有条目复选框个数
		if(selectedCount == allCount) {//全选了
			$("#selectAll").attr("checked", true);//勾选全选复选框
			setJieSuanStyle(true);//使结算按钮可用
		} else if(selectedCount == 0) {//全撤消了
			$("#selectAll").attr("checked", false);//撤消全选复选框
			setJieSuanStyle(false);//使结算按钮不可用			
		} else {//未全选
			$("#selectAll").attr("checked", false);//撤消全选复选框
			setJieSuanStyle(true);//使结算按钮可用
		}
		showTotal();//重新计算合计
	});
	
	// 给jia、jian添加事件
	$(".jian").click(function() {
		var cartItemId = $(this).attr("id").substring(0, 32);
		var quantity = Number($("#" + cartItemId + "Quantity").val());
		if(quantity == 1) {
			if(confirm("您是否真要删除该条目？")) {
				location = "/goods/CartItemServlet?method=delCardItem&cartItemId=" + cartItemId;
				alert("删除成功！");		
			}
		} else {
			sendUpdate(cartItemId, quantity-1);
		}
	});
	$(".jia").click(function() {
		var cartItemId = $(this).attr("id").substring(0, 32);
		var quantity = Number($("#" + cartItemId + "Quantity").val());
		sendUpdate(cartItemId, quantity+1);
	});
});

// 异步请求，修改数量
function sendUpdate(cartItemId, quantity) {
	/*
	 1. 通过cartItemId找到输入框元素
	 2. 通过cartItemId找到小计元素
	*/
	
	$.ajax({
		async:false,
		cache:false,
		url:"/goods/CartItemServlet",
		data:{method:"updateQuantity",cartItemId:cartItemId,quantity:quantity},
		type:"POST",
		dataType:"json",
		success:function(result) {
			//1. 修改数量
			$("#" + cartItemId + "Quantity").val(result.quantity);
			//2. 修改小计
			$("#" + cartItemId + "Subtotal").text(result.subtotal);
			//3. 重新计算总计
			showTotal();
		}
	});
/* 	
	var input = $("#" + cartItemId + "Quantity");
	var subtotal = $("#" + cartItemId + "Subtotal");
	var currPrice = $("#" + cartItemId + "CurrPrice");

	input.val(quantity);
	subtotal.text(round(currPrice.text() * quantity, 2));
	showTotal(); */
}

// 设置所有条目复选框
function setAll(flag) {
	$(":checkbox[name=checkboxBtn]").attr("checked", flag);//让所有条目的复选框与参数flag同步
	showTotal();//重新设置合计
}

// 设置结算按钮的样式
function setJieSuanStyle(flag) {
	if(flag) {// 有效状态
		$("#jiesuan").removeClass("kill").addClass("jiesuan");//切换样式
		$("#jiesuan").unbind("click");//撤消“点击无效”
	} else {// 无效状态
		$("#jiesuan").removeClass("jiesuan").addClass("kill");//切换样式
		$("#jiesuan").click(function() {//使其“点击无效”
			return false;
		});
	}
}

// 显示合计
function showTotal() {
	var total = 0;//创建total，准备累加
	/*
	1. 获取所有被勾选的复选框，遍历之
	*/
	$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
		/*
		2. 通过复选框找到小计
		*/
		var subtotal = Number($("#" + $(this).val() + "Subtotal").text());
		total += subtotal;
	});
	/*
	3. 设置合计
	*/
	$("#total").text(round(total, 2));
}


/*
 * 批量删除
 */
function batchDelete() {
	// 1. 获取所有被选中条目的复选框
	// 2. 创建一数组，把所有被选中的复选框的值添加到数组中
	// 3. 指定location为CartItemServlet，参数method=batchDelete，参数cartItemIds=数组的toString()
	var cartItemIdArray = new Array();
	$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
		cartItemIdArray.push($(this).val());//把复选框的值添加到数组中
	});
	window.location = "/goods/CartItemServlet?method=batchDelete&cartItemIds=" + cartItemIdArray.toString();
}


/*
 * 结算
 */
 
 function jiesuan() {
		// 1. 获取所有被选择的条目的id，放到数组中
		var cartItemIdArray = new Array();
		$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
			cartItemIdArray.push($(this).val());//把复选框的值添加到数组中
		});	
		// 2. 把数组的值toString()，然后赋给表单的cartItemIds这个hidden
		$("#cartItemIds").val(cartItemIdArray.toString());
		// 把总计的值，也保存到表单中
		$("#hiddenTotal").val($("#total").text());
		// 3. 提交这个表单
		$("#jieSuanForm").submit();
	}

</script>



  </head>
  <body>
<c:choose>
	<c:when test="${empty listCartItem}">
		<table width="95%" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td align="right">
					<img align="top" src="<c:url value='/images/icon_empty.png'/>"/>
				</td>
				<td>
					<span class="spanEmpty">您的购物车中暂时没有商品</span>
				</td>
			</tr>
		</table>  
	
	<br/>
	<br/>
	
	</c:when>
	
 	<c:otherwise> 
	
		<table width="95%" align="center" cellpadding="0" cellspacing="0">
			<tr align="center" bgcolor="#efeae5">
				<td align="left" width="50px">
					<input type="checkbox" id="selectAll" checked="checked"/><label for="selectAll">全选</label>
				</td>
				<td colspan="2">商品名称</td>
				<td>单价</td>
				<td>数量</td>
				<td>小计</td>
				<td>操作</td>
			</tr>
		
		
		<c:forEach items="${listCartItem }" var="cartItem">
		
			<tr align="center">
				<td align="left">
					<input value="${cartItem.cartItemId }" type="checkbox" name="checkboxBtn" checked="checked"/>
				</td>
				<td align="left" width="70px">
					<a class="linkImage" href="<c:url value='/BookServlet?method=load&bid=${cartItem.book.bid }'/>"><img border="0" width="54" align="top" src="<c:url value='${cartItem.book.image_b }'/>"/></a>
				</td>
				<td align="left" width="400px">
				    <a href="<c:url value='/BookServlet?method=load&bid=${cartItem.book.bid }'/>"><span>${cartItem.book.bname }</span></a>
				</td>
				<td><span>&yen;<span class="currPrice" id="12345CurrPrice">${cartItem.book.currPrice }</span></span></td>
				<td>
					<a class="jian" id="${cartItem.cartItemId }}Jian"></a><input class="quantity" readonly="readonly" id="${cartItem.cartItemId }Quantity" type="text" value="${cartItem.quantity }"/><a class="jia" id="${cartItem.cartItemId }Jia"></a>
				</td>
				<td width="100px">
					<span class="price_n">&yen;<span class="subTotal" id="${cartItem.cartItemId }Subtotal">${cartItem.subtotal }</span></span>
				</td>
				<td>
					<a href="<c:url value='/CartItemServlet?method=delCardItem&cartItemId=${cartItem.cartItemId }'/>">删除</a>
				</td>
			</tr>
		</c:forEach>
	
	 	

	
	<tr>
		<td colspan="4" class="tdBatchDelete">
			<a href="javascript:batchDelete()">批量删除</a>
		</td>
		<td colspan="3" align="right" class="tdTotal">
			<span>总计：</span><span class="price_t">&yen;<span id="total"></span></span>
		</td>
	</tr>
	<tr>
		<td colspan="7" align="right">
			<a href="javascript:jiesuan()" id="jiesuan" class="jiesuan"></a>
		</td>
	</tr>
</table>
	<form id="jieSuanForm" action="<c:url value='/CartItemServlet'/>" method="post">
		<input type="hidden" name="cartItemIds" id="cartItemIds"/>
		<input type="hidden" name="method" value="loadCartItems"/>
	</form>
	
	</c:otherwise>
</c:choose>

  </body>
</html>
