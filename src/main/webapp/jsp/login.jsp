<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>登录页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

<style>
* {
	margin: 0 auto;
	padding: 0;
	font-family: "Microsoft Yahei", "Microsoft Yahei", "微软雅黑", sans-serif;
}

body {
	background: url('./static/img/login1.jpg') no-repeat fixed;
	background-size: 100%;
    height: 100%;
    width: 100%;
    overflow: hidden;
}

#ff {
	background: url("./static/img/bj1.png") no-repeat;
	
	-moz-background-size: 100% 100%;
	/* background-size: 100% 100%; */
	width: 35%;
	height: 80%;
	margin: 0 auto;
	margin-top: 10%;
	border-radius:5px;
}

#form {
	position: relative;
	top: 100px;
}

.login-submit {
	margin: 0px;
	-webkit-border-radius: 0 5px 5px 0;
	-moz-border-radius: 0 5px 5px 0;
	border-radius: 0 5px 5px 0;
	background-color: #2EBFB2;
	display: inline-block;
	height: 42px;
	line-height: 42px;
	padding: 0 auto;
	color: #fff;
	white-space: nowrap;
	text-align: center;
	font-size: 20px;
	border: none;
	border-radius: 20px;
	cursor: pointer;
	opacity: .9;
	position: relative;
	margin-top: 50px;
	filter: alpha(opacity = 90);
	cursor: pointer;
}

#btn-login-down {
	margin: 0px;
	-webkit-border-radius: 0 5px 5px 0;
	-moz-border-radius: 0 5px 5px 0;
	border-radius: 0 5px 5px 0;
	background-color: #2EBFB2;
	display: inline-block;
	height: 42px;
	line-height: 42px;
	padding: 0 auto;
	color: #fff;
	white-space: nowrap;
	text-align: center;
	font-size: 20px;
	border: none;
	border-radius: 20px;
	cursor: pointer;
	opacity: .9;
	position: relative;
	margin-top: 25px;
	filter: alpha(opacity = 90);
	cursor: pointer;
}

.tou {
	background: url("static/img/tou.png") no-repeat;
	width: 97px;
	height: 92px;
	position: relative;
	top: 50px;
	left: 50px;
}

.left_hand {
	background: url("static/img/left_hand.png") no-repeat;
	width: 32px;
	height: 37px;
	position: relative;
	top: 40px;
	left: -10px;
}

.right_hand {
	background: url("static/img/right_hand.png") no-repeat;
	width: 32px;
	height: 37px;
	position: relative;
	top: 40px;
	left: 108px;
}

.initial_left_hand {
	background: url("static/img/hand.png") no-repeat;
	width: 30px;
	height: 20px;
	position: relative;
	top: 40px;
	left: -10px;
}

.initial_right_hand {
	background: url("static/img/hand.png") no-repeat;
	width: 30px;
	height: 20px;
	position: relative;
	top: 20px;
	left: 108px;
}

.left_handing {
	background: url("static/img/left-handing.png") no-repeat;
	width: 30px;
	height: 20px;
	position: relative;
	top: 40px;
	left: -10px;
}

.right_handinging {
	background: url("static/img/right_handing.png") no-repeat;
	width: 30px;
	height: 20px;
	position: relative;
	top: 40px;
	left: 108px;
}

.box {
	width: 80%;
	position: relative;
}

.box .icon-search1 {
	background: url(static/img/账号1.png) no-repeat;
	-moz-background-size: 80% 80%; background-size : 80% 80%;
	width: 40px;
	height: 40px;
	position: absolute;
	left: 36;
	top: 4px;
	z-index: 1;
	background-size: 80% 80%;
}

.box .icon-search2 {
	background: url(static/img/密码1.png) no-repeat;
	-moz-background-size: 80% 80%;
	background-size: 80% 80%;
	width: 40px;
	height: 40px;
	position: absolute;
	left: 36;
	top: 4px;
	z-index: 1;
}


#activeUser, #activePwd {
	border-radius:12px;
	padding-left: 50px;
	font-size: 16px;
}

input {
	padding-left: 50px;
	width: 90%;
	display: block;
	border: 0;
	margin: 0 auto;
	height: 42px;
	line-height: 42px;
	padding: 0 1em;
	border-radius: 5px;
	margin-bottom: 11px;
	-webkit-border-radius: 2px 0 0 2px;
	-moz-border-radius: 2px 0 0 2px;
	border-radius: 5px 0 0 5px;
}
</style>
<script src="./js/jquery.js" charset="utf-8"></script>
<script src="./static/layui/layui.js" charset="utf-8"></script>

<script src="./static/js/cvi_busy_lib.js" charset="utf-8"></script>
</head>
<body>
	<div id="win10-login">
		<center>
			<div id="ff">
				<div style="background: url(static/img/logo.png) no-repeat; -moz-background-size: 100% 100%;background-size: 100% 100%;
					width: 60px; height: 60px; position: relative ; top: 30px;left:-250px; z-index: 1;"></div>
				<!-- <div class="win10-login-box-square"
					style="position: relative; left: -38px;">
					<div style="width: 165px; height: 96px; position: relative; top: 8px; z-index: 2;">
						<div class="tou"></div>
						<div id="left_hand" class="initial_left_hand"></div>
						<div id="right_hand" class="initial_right_hand"></div>
					</div>
				</div> -->
				
				<!-- <p style="background: url(static/img/br.png) no-repeat; -moz-background-size: 80% 80%;  -->
					<p style="width: 100%; height: 10px; position: relative; top: 56px; z-index: 1;"></p>
				<form id="form" target="_self" method="post"
					action="index.do?ctrl=Login">

					<!--用户名-->
					<div class="box" style="padding-bottom: 15px;">
						<i class="icon-search1"></i> <input type="text"
							placeholder="请输入登录名" id="activeUser" name="activeUser"
							class="login-username">
					</div>
					<!--密码-->
					<div class="box">
						<i class="icon-search2"></i> <input type="password"
							placeholder="请输入密码" id="activePwd" name="activePwd"
							class="login-password">
					</div>

					<!--登陆按钮-->
					<div class="box">
						<i class="icon-search3"></i> <input type="button" value="登    录"
							id="btn-login" class="login-submit" />
							<input type="button" value="下载浏览器"
							id="btn-login-down"  />
					</div>

				</form>

			</div>
		</center>
	</div>
	<script type="text/javascript">
		layui.use([ 'element', 'layer' ], function() {
			var element = layui.element;
			var layer = layui.layer;

			//监听折叠
			element.on('collapse(test)', function(data) {
				layer.msg('展开状态：' + data.show);
			});

		});
		$(function() {
			//下载浏览器
			$("#btn-login-down").click(function() {
				location.href="downloadllq.do";
			});
			
			//得到焦点
			$("#activeUser").focus(function() {
				$("#left_hand").attr("class", "initial_left_hand");
				$("#left_hand").attr("style", "left:-10px;top:40px;");
				$("#right_hand").attr("class", "initial_right_hand");
				$("#right_hand").attr("style", "right:108px;top:20px");
			});

			//得到焦点
			$("#activePwd").focus(function() {

				$("#left_hand").animate({
					left : "28",
					top : " 0"
				}, {
					step : function() {
						if (parseInt($("#left_hand").css("left")) > -40) {
							$("#left_hand").attr("class", "left_hand");
						}
					}
				}, 3000);
				$("#right_hand").animate({
					left : "68",
					top : "-38px"
				}, {
					step : function() {
						if (parseInt($("#right_hand").css("left")) < 108) {
							$("#right_hand").attr("class", "right_hand");
						}
					}
				}, 3000);
			});
			//失去焦点
			$("#activePwd").blur(function() {
				$("#left_hand").attr("class", "initial_left_hand");
				$("#left_hand").attr("style", "left:-10px;top:40px;");
				$("#right_hand").attr("class", "initial_right_hand");
				$("#right_hand").attr("style", "right:108px;top:20px");
			});
			$("#btn-login").on('click', click);
			$('#activeUser').bind('keyup', function(event) {
				if (event.keyCode == "13") {
					//回车执行查询
					click();
				}
			});
			$('#activePwd').bind('keyup', function(event) {
				if (event.keyCode == "13") {
					//回车执行查询
					click();
				}
			});
		});

		function click() {
			var activeUser = $('#activeUser').val();
			var activePwd = $('#activePwd').val();
			var xval = getBusyOverlay(
					'viewport',
					{
						color : 'gray',
						opacity : 0.75,
						text : 'viewport: loading...',
						style : 'text-shadow: 0 0 3px black;font-weight:bold;font-size:16px;color:white'
					}, {
						color : '#fff',
						size : 120,
						type : 'o'
					});

			if (activeUser == null || activeUser == ''
					|| activeUser == 'undefined' || activeUser == undefined) {
				alert("请输入用户名！");
			} else if (activePwd == null || activePwd == ''
					|| activePwd == 'undefined' || activePwd == undefined) {
				alert("请输入密码！");
			} else {
				$.ajax({
					url : "login/init.do",
					type : 'post',
					data : $("#form").serialize(),
					dataType : "json",
					beforeSend : function() {
						if (xval) {
							xval.settext("正在登录中，请稍后......");
						}
					},
					success : function(obj) {
						xval.remove();
						if (obj != null && obj.code == '000') {
							layer.msg('登录成功,欢迎您:' + obj.data.name + '!');
							if (window.location.href.lastIndexOf("/jsp/")>-1) {
								window.location.href = window.location.href.substring(0,window.location.href.lastIndexOf("/jsp/")+1)+"index.do?ctrl=Index";
							}else{
								window.location.href = "index.do?ctrl=Index";
							}
							
						} else if (obj != null && obj.code == '002') {
							layer.msg('登录失败,帐号密码不正确或账号被禁用！');
						} else {
							window.location.href = "jsp/login.jsp";
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						window.location.href = "jsp/login.jsp";
						// 状态码
						alert(XMLHttpRequest.status);
						// 状态
						alert(XMLHttpRequest.readyState);
						// 错误信息  
						alert(textStatus);
					}
				});
			}

			xval.remove();
		}
		window.onload = function () {
			if(self!=top){
			    top.location.href="jsp/login.jsp";
		    }
		}
	</script>
</body>
</html>