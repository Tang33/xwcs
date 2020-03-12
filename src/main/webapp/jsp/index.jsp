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

<title>网上玄武财税 - 税收管理系统</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<link rel="stylesheet" href="static/layuicms/layui/css/layui.css"
	media="all" />
<link rel="stylesheet" href="static/layuicms/css/index.css" media="all" />
<style type="text/css">
body .layui-nav .layui-nav-more{display: none}
</style>
</head>

<body class="main_body">
	<div class="layui-layout layui-layout-admin showMenu">
		<!-- 顶部 -->
		<div class="layui-header header" style="background-color:#2A97E9;">
			<div class="layui-main mag0">
				<a href="index.do?ctrl=Index" style="width:180px" class="logo" style="background-color:#036CB9;">网上玄武财税</a>
				<!-- 显示/隐藏菜单 -->
				<a href="javascript:void(0);" class="seraph hideMenu icon-caidan" style="padding-left:20px;">
				</a>
				<!-- 顶级菜单 -->
				<ul class="layui-nav mobileTopLevelMenus" mobile>
					<li class="layui-nav-item" data-menu="contentManagement"><a
						href="javascript:void(0);"><i class="seraph icon-caidan"></i><cite>网上玄武财税 - 税收管理系统</cite></a>
						<dl class="layui-nav-child" id="sjcd"></dl>
					</li>
				</ul>
				<ul class="layui-nav topLevelMenus" id="lbt" style=" width: 80%;overflow-x: auto;overflow-y: hidden;" pc></ul>
				<!-- 顶部右侧菜单 -->
				<ul class="layui-nav top_menu">
					 <li class="layui-nav-item">
					    <a href="javascript:;" id="qcxx"></a>
					    <dl class="layui-nav-child" id="qcxxChild"> <!-- 二级菜单 -->
					    </dl>
					  </li>
					<!-- <li class="layui-nav-item" pc><a href="javascript:;"
						class="clearCache"><i class="layui-icon" data-icon="&#xe640;">&#xe640;</i><cite>清除缓存</cite><span
							class="layui-badge-dot"></span></a></li> -->
					<li class="layui-nav-item lockcms" pc><a href="javascript:;"><i
							class="seraph icon-lock"></i><cite>锁屏</cite></a></li>
					<li class="layui-nav-item" id="userInfo"><a
						href="javascript:;">
							<img src="static/layuicms/images/face.jpg"
							class="layui-nav-img userAvatar" width="35" height="35" id="ysj"><cite
							class="adminName">${name }</cite></a>
						<dl class="layui-nav-child">
							<dd>
								<a href="javascript:;"
									data-url="static/layuicms/page/user/userInfo.html"><i
									class="seraph icon-ziliao" data-icon="icon-ziliao"></i><cite>个人资料</cite></a>
							</dd>
							<dd>
								<a href="javascript:;"
									data-url="static/layuicms/page/user/changePwd.html"><i
									class="seraph icon-xiugai" data-icon="icon-xiugai"></i><cite>修改密码</cite></a>
							</dd>
							<dd pc>
								<a href="javascript:;" class="functionSetting"><i
									class="layui-icon">&#xe620;</i><cite>功能设定</cite><span
									class="layui-badge-dot"></span></a>
							</dd>
							<dd pc>
								<a href="javascript:;" class="changeSkin"><i
									class="layui-icon">&#xe61b;</i><cite>更换皮肤</cite></a>
							</dd>
							<dd pc>
								<a href="index.do?ctrl=Index" class="" onclick="refresh();"><i
									class="layui-icon">&#xe669;</i><cite>刷新页面</cite></a>
							</dd>
							<dd>
								<a href="javascript:void(0);" class="signOut"
									onclick="Loginout();"><i class="seraph icon-tuichu"></i><cite>退出</cite></a>
							</dd>
						</dl></li>
				</ul>
			</div>


			<div class="layui-side layui-bg-black" style="width:250px">
				<div class="user-photo">
					<a class="img" title="我的头像"><img
						src="static/layuicms/images/face.jpg" class="userAvatar" id="userAvatar" /></a>
					<p>
						你好！<span class="userName">${name }</span>, 欢迎登录
					</p>
				</div>
				<!-- 搜索 -->
				<div class="layui-form component">
					<select name="search" id="search" lay-search lay-filter="searchPage">
						<option value="">搜索页面或功能</option> 
						<option value="1">layer</option>
						<option value="2">form</option>
					</select> <i class="layui-icon" style="position:absolute;  top:-10px;float:right;">&#xe615;</i>
				</div>
				<div class="navBar layui-side-scroll" id="navBar">
					<ul class="layui-nav layui-nav-tree">
						<li class="layui-nav-item layui-this"><a href="javascript:;"
							data-url="jsp/main2.jsp"><i class="layui-icon" data-icon=""></i><cite>后台首页</cite></a>
						</li>
					</ul>
					
				</div>
			</div>
		</div>
		<!-- 右侧内容 -->
		<div class="layui-body layui-form" style="margin-left:50.5px">
			<div class="layui-tab mag0" lay-filter="bodyTab" id="top_tabs_box">
				<ul class="layui-tab-title top_tab" id="top_tabs">
					<li class="layui-this" lay-id=""><i class="layui-icon">&#xe68e;</i>
						<cite>后台首页</cite></li>
				</ul>
				<ul class="layui-nav closeBox">
					<li class="layui-nav-item"><a href="javascript:;"><i
							class="layui-icon caozuo">&#xe643;</i> 页面操作</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="javascript:;" class="refresh refreshThis"><i
									class="layui-icon">&#x1002;</i> 刷新当前</a>
							</dd>
							<dd>
								<a href="javascript:;" class="closePageOther"><i
									class="seraph icon-prohibit"></i> 关闭其他</a>
							</dd>
							<dd>
								<a href="javascript:;" class="closePageAll"><i
									class="seraph icon-guanbi"></i> 关闭全部</a>
							</dd>
						</dl>
					</li>
				</ul>
				<div class="layui-tab-content clildFrame">
					<div class="layui-tab-item layui-show">
						<iframe src="jsp/main2.jsp"></iframe>
					</div>
				</div>
			</div>
		</div>
		<!-- 底部 -->
		<div class="layui-footer footer">
			<p>
				<span>copyright 2018-2019 © 网上玄武财税 - 税收管理系统</span> 
			</p>
		</div>
	</div>
	<!-- 移动导航 -->
	<div class="site-tree-mobile">
		<i class="layui-icon">&#xe602;</i>
	</div>
	<div class="site-mobile-shade"></div>

	<script type="text/javascript" src="static/layuicms/layui/layui.js"></script>
	<script type="text/javascript" src="static/layuicms/js/index.js"></script>
	<script type="text/javascript" src="static/layuicms/js/cache.js"></script>
	<script src="./static/js/jquery-2.2.4.min.js" charset="utf-8"></script>
	<script>
	
		//JavaScript代码区域
		window.onload = function() {
			init();
			tx();
			queryQYQC();
			/* setInterval(function() {
				queryQYQC();
			}, 5000); */
		}
	
		function queryQYQC() {
			$.ajax({
				url : "indexs/queryQYQC.do",
				type : 'post',
				data : "",
				dataType : "json",
				async:true,
				success : function(obj) {
					$("#qcxx").html("");
					$("#qcxxChild").html("");
					if(obj.code == '000'&&obj.data!=null&&obj.data!=undefined){
						var data = obj.data;
						var str="";
						var str1="";
						for (var i = 0; i < data.length; i++) {
							//大于10条时  只显示10条数据
								if(i >= 0 && i < 10){
								str ='迁出消息<span class="layui-badge-dot"></span>';
								str1 +='<dd><a>'+data[i].NSRMC+'</a></dd>';
								}
								
							}
							$("#qcxx").html(str);
							$("#qcxxChild").html(str1);
						}
						
					}
					
			})
			
		}
		//点击跳转页面到qrqy
		$("#qcxxChild").click(function(){

			window.open("index.do?ctrl=qyqccl");
			
		})
		
		function tx(){
			$.ajax({
				url:"download.do",
				type:"post",
				data:{
					lx:"rygl",
					name:"tx"
				},
	            success: function(obj){
	            	console.log(obj);
	            	if(obj==null||obj==''||obj=='null'){
	            		obj="static/layuicms/images/face.jpg";
	            	}
	            	$('#userAvatar').attr('src', obj); //图片链接（base64）
	            	$('#ysj').attr('src', obj); //图片链接（base64）
	            	
				},
				error:function(){
					alert("读取文件异常");
				}
				
			});
		}
		function refresh(){
			
			$.ajax({
				url : "sjjg_refresh.do",
				type : 'post',
				dataType : "json",
				success : function(obj) {
				
				},
				error : function(XMLHttpRequest, textStatus,
						errorThrown) {
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
		function Loginout() {
			layui.use('layer', function() { //独立版的layer无需执行这一句
				var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句

				layer.confirm('确认要退出本系统吗？', {
					btn : [ '确认', '再想想' ]
				//按钮
				}, function() {
					$.ajax({
						url : "login/LoginOut.do",
						type : 'post',
						data : {},
						dataType : "json",
						success : function(obj) {
							window.location.href = "jsp/login.jsp";
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown) {
							window.location.href = "jsp/login.jsp";
							// 状态码
							alert(XMLHttpRequest.status);
							// 状态
							alert(XMLHttpRequest.readyState);
							// 错误信息  
							alert(textStatus);
						}
					});
				});
			});

		}
		
		
		function init(){
			$.ajax({
						url : "indexs/load.do",
						type : 'post',
						data : {
							gndm : 0
						},
						dataType : "json",
						success : function(obj) {
							$("#navBar").html("");
							if (obj.code == "000") {
								var data = obj.data;
								var str="";
								var str1="";
								for (var i = 0; i < data.length; i++) {
									if(i==0){
										str += '<li class="layui-nav-item layui-this" data-menu="contentManagement">'+
												'<a id="adj'+data[i].GNDM+'" href="javascript:;" onclick="load('+data[i].GNDM+');"><i class="layui-icon"  data-icon="&#xe620;">&#xe620;</i><cite>'+data[i].GNMC+'</cite></a></li>';
										str1+='<dd data-menu="memberCenter">'+
												'<a href="javascript:;" onclick="load('+data[i].GNDM+');"><i class="layui-icon"'+
												'data-icon="&#xe705;">&#xe705; </i><cite>'+data[i].GNMC+'</cite></a></dd>';
									}else{
										str += '<li class="layui-nav-item " data-menu="contentManagement">'+
												'<a id="adj'+data[i].GNDM+'" href="javascript:;" onclick="load('+data[i].GNDM+');"><i class="layui-icon"  data-icon="&#xe620;">&#xe620;</i><cite>'+data[i].GNMC+'</cite></a></li>';
										str1+='<dd data-menu="memberCenter">'+
												'<a href="javascript:;" onclick="load('+data[i].GNDM+');"><i class="layui-icon"'+
												'data-icon="&#xe705;">&#xe705; </i><cite>'+data[i].GNMC+'</cite></a></dd>';
									}	
								}
								$("#lbt").html(str);
								$("#sjcd").html(str1);
								//load(0);
							}
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown) {
							/* window.location.href = "jsp/login.jsp"; */
							// 状态码
							alert(XMLHttpRequest.status);
							// 状态
							alert(XMLHttpRequest.readyState);
							// 错误信息  
							alert(textStatus);
						}
					});
			
		}
		
		function load(lb) {
			
				$(".layui-layout-admin").attr("class",'layui-layout layui-layout-admin');
		
			$(".layui-nav-item").attr("class",'layui-nav-item');
			$("#adj"+lb).parent().attr("class","layui-nav-item layui-this");
			$.ajax({
						url : "indexs/load.do",
						type : 'post',
						data : {
							gndm : lb
						},
						dataType : "json",
						success : function(obj) {
							$("#navBar").html("");
							if (obj.code == "000") {
								var data = obj.data;
								/* window.location.href = "jsp/login.jsp"; */
								var str = '<ul class="layui-nav layui-nav-tree" >';
								var strsearcg='<option value="">搜索功能</option>';
								for (var i = 0; i < data.length; i++) {
									str += '<li class="layui-nav-item" title="'+data[i].GNMC+'"><a id="'+data[i].GNDM+'" style="width:230px" href="javascript:void(0);" data-url="'
											+ data[i].URL
											+ '" onclick="addClass(this);"><i class="layui-icon" style="margin-left:-8px" data-icon=""></i><cite>'
											+ data[i].GNMC + '</cite></a></li>';
											strsearcg+='<option value="'+data[i].GNDM+'" ">'+data[i].GNMC+'</option>';
											
								}
								str += '</ul>';

								$("#navBar").html(str);
								$("#search").html(strsearcg);
								layui.form.render();
								layui.form.on('select(searchPage)', function(data){
									console.log(data);
									$("#"+data.value).click();
								});
							}
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown) {
							/* window.location.href = "jsp/login.jsp"; */
							// 状态码
							alert(XMLHttpRequest.status);
							// 状态
							alert(XMLHttpRequest.readyState);
							// 错误信息  
							alert(textStatus);
						}
					});
		}
		function addClass(cl) {
			//$(".layui-nav-item layui-this").attr("class","layui-nav-item");
			$(".layui-nav-item").removeClass("layui-this");
			var pa = cl.parentNode;
			pa.setAttribute("class", "layui-nav-item layui-this");

		}
		
		
	</script>
</body>
</html>
