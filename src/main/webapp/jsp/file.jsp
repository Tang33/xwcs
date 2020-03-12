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

<title>文件管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="js/jquery.js" charset="utf-8"></script>

<script src="./static/layer/layer.js" charset="utf-8"></script>
<link href="./static/layui/css/layui.css" rel="stylesheet">
<script src="./static/layui/layui.js" charset="utf-8"></script>


<link
	href="https://cdn.bootcss.com/jquery-contextmenu/2.6.3/jquery.contextMenu.min.css"
	rel="stylesheet">
<script
	src="https://cdn.bootcss.com/jquery-contextmenu/2.6.3/jquery.contextMenu.min.js"></script>


<link rel="stylesheet" type="text/css"
	href="./static/easyui/themes/material-teal/easyui.css">
<link rel="stylesheet" type="text/css"
	href="./static/easyui/themes/icon.css">
<!-- <script type="text/javascript"
	src="./static/easyui/jquery.min.js"></script>  -->
<script type="text/javascript"
	src="./static/easyui/jquery.easyui.all.js"></script>

<link rel="stylesheet"
	href="./static/tree/tree_files/font-awesome.min.css">
<!-- page specific plugin styles -->
<!-- ace styles -->
<link rel="stylesheet" href="./static/tree/tree_files/ace.min.css">
<!--[if !IE]> -->
<!-- <![endif]-->
<script src="./static/tree/tree_files/bootstrap.min.js"></script>
<!-- page specific plugin scripts -->
<script src="./static/tree/tree_files/fuelux.tree.min.js"></script>
<!-- ace scripts -->
<script src="./static/tree/tree_files/ace-elements.min.js"></script>
<script src="./static/tree/tree_files/ace.min.js"></script>
<!-- inline scripts related to this page -->

<style type="text/css">
</style>
</head>

<body style="overflow:hidden">
	<div style="width: 10%;height: 100%;float:left;">
		<div class="col-sm-6">
			<div class="widget-box">
				<div class="widget-header header-color-blue">
					<h4 class="lighter smaller">fast.app</h4>
				</div>
				<div class="widget-body">
					<div class="widget-main padding-8">
						<div id="box2" class="box">
							<div id="tree2" class="tree tree-unselectable"
								style="height: 92%;">

								<div class="tree-item" style="display: block;">
									<div class="tree-item-name">
										<i class="icon-file-text "></i> ReadMe.txt
									</div>
								</div>
								<div class="tree-item" style="display: block;">
									<div class="tree-item-name">
										<i class="icon-book blue"></i> Manual.html
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div style="width: 90%;height: 100%;float:right;" on>
		<div id="tt" class="easyui-tabs" style="width:100%;height:100%;">
			<div title="Home"></div>
		</div>
	</div>
	<script type="text/javascript">
		function addTab(title, url) {
				if ($('#tt').tabs('exists', title)) {
					$('#tt').tabs('select', title);
				} else {
					var content1 = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
					$('#tt').tabs('add', {
						title : title,
						content : content1,
						closable : true
					});
				}
			}
		layer.ready(function() {
			
			layui.use('form', function() {});
			var cd = "";
			var count = -1;
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				url : "ajax.do?ctrl=Run_file",
				data : {},
				dataType : "json",
				success : function(objs) {
					if (objs != null && objs.code == '000') {
						var datas = objs.data;
						var str = "";
						for (var i = 0; i < datas.length; i++) {
							str += '<div id="boxx' + i + '"><div class="tree-item" style="display: block;"><div class="tree-item-name"><i class="icon-book blue"></i><a onclick="addTab(\'' + datas[i] +
								'\',\'jsp/run.jsp?file_name=' + datas[i] + '&sms=111\');">' + datas[i] + '</a></div></div></div>';
						}
						$("#tree2").html(str);
						count = datas.length;
						for (var i = 0; i < count; i++) {
							//右键监听
							var idd = '#boxx' + i;
							$.contextMenu({
								selector : idd, //右键选择器
								callback : function(key, options) { //点击回调处理
									var text = $(options.selector).text();
									if (key == 'add') {
									var ly=	layer.open({
											type : 1,
											title : "新增在线开发类，包含(...java...jsp...js...css)", //不显示标题栏   title : false/标题
											area : [ '400px', '260px;' ],
											shade : 0.8,
											id : 'LAY_layuipro', //设定一个id，防止重复弹出
											resize : false,
											btn : [ '新增', '取消' ],
											btnAlign : 'c',
											skin : 'layer-ext-moon',
											moveType : 1, //拖拽模式，0或者1
											content : $("#tcc"),
											success : function(index, layero) {},
											yes : function(index, layero) {
												var names = layero.find("#names").val();
												var sms = layero.find("#sms").val();
												console.log(names + "," + sms);
												var tree2 = $("#tree2").html();
												count += 1;
												var str = '<div id="boxx' + count + '"><div class="tree-item" style="display: block;"><div class="tree-item-name"><i class="icon-book blue"></i><a onclick="addTab(\'' + names +
													'\',\'jsp/run.jsp?file_name=' + names + '&sms=' + sms + '\');">' + names + '</a></div></div></div>';
												$("#tree2").append(str);
												var hh = 'package fast.app;\n' +
													'\n' +
													'import java.util.Map;\n' +
													'import fast.main.conf.ApiBody;\n' +
													'import fast.main.conf.ApiHead;\n' +
													'import fast.main.util.Super;\n' +
													'' +
													'@ApiHead(value="' + sms + '")\n' +
													'public class ' + names + ' extends Super{\n' +
													'	@ApiBody(name = "初始化", context = "方法说明(在首页API文档详情中显示)", type = "Post")\n' +
													'	public String init(Map<String, Object> rmap) {\n' +
													'		try {\n' +
													'			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）\n' +
													'			initMap(rmap);\n' +
													'			return "xtgl/' + names + '";\n' +
													'		} catch (Exception e) {\n' +
													'			e.printStackTrace();\n' +
													'			return "xtgl/' + names + '";\n' +
													'		}\n' +
													'	}\n\n\n\n' +
													'}\n\n\n\n';
												$.ajax({
													type : "post", //请求方式
													async : false, //是否异步
													url : "./ajax.do?ctrl=Index_save",
													data : {
														url : names,
														type : "java",
														str : hh
													},
													dataType : "json",
													success : function(obj) {},
													error : function(XMLHttpRequest, textStatus, errorThrown) {
														// 状态码
														alert(XMLHttpRequest.status);
														// 状态
														alert(XMLHttpRequest.readyState);
														// 错误信息  
														alert(textStatus);
													}
												});
												addTab(names, 'jsp/run.jsp?file_name=' + names + '&sms=' + sms);
												layer.close(ly);
											},
											cancel : function(index, layero) {
												//右上角关闭回调
												//return false 开启该代码可禁止点击该按钮关闭
											}
										});
									} else if (key == 'edit') {
										if (text != null && text != undefined && text != '') {
	
										} else {
											layer.alert('请先选择文件', {
												icon : 6
											});
										}
									} else if (key == 'copy') {
										if (text != null && text != undefined && text != '') {
	
										} else {
											layer.alert('请先选择文件', {
												icon : 6
											});
										}
									} else if (key == 'delete') {
										if (text != null && text != undefined && text != '') {
	
										} else {
											layer.alert('请先选择文件', {
												icon : 6
											});
										}
									}
									
									
								},
								items : { //菜单列表配置
									"add" : {
										name : "Add",
										icon : "add"
									},
									"edit" : {
										name : "Edit",
										icon : "edit"
									},
									"sep1" : "---------",
									"copy" : {
										name : "Copy",
										icon : "copy"
									},
									"paste" : {
										name : "Paste",
										icon : "paste"
									},
									"sep2" : "---------",
									"delete" : {
										name : "Delete",
										icon : "delete"
									},
								}
							});
						}
	
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					// 状态码
					alert(XMLHttpRequest.status);
					// 状态
					alert(XMLHttpRequest.readyState);
					// 错误信息  
					alert(textStatus);
				}
			});
	
	
			$.contextMenu({
				selector : '#box2', //右键选择器
				callback : function(key, options) { //点击回调处理
				var ly=	layer.open({
						type : 1,
						title : "新增在线开发类，包含(...java...jsp...js...css)", //不显示标题栏   title : false/标题
						area : [ '400px', '260px;' ],
						shade : 0.8,
						id : 'LAY_layuipro', //设定一个id，防止重复弹出
						resize : false,
						btn : [ '新增', '取消' ],
						btnAlign : 'c',
						skin : 'layer-ext-moon',
						moveType : 1, //拖拽模式，0或者1
						content : $("#tcc"),
						success : function(index, layero) {},
						yes : function(index, layero) {
							var names = layero.find("#names").val();
							var sms = layero.find("#sms").val();
							var tree2 = $("#tree2").html();
							count += 1;
							var str = '<div id="boxx' + count + '"><div class="tree-item" style="display: block;"><div class="tree-item-name"><i class="icon-book blue"></i><a onclick="addTab(\'' + names +
								'\',\'jsp/run.jsp?file_name=' + names + '&sms=' + sms + '\');">' + names + '</a></div></div></div>';
							$("#tree2").append(str);
							var hh = 'package fast.app;\n' +
								'\n' +
								'import java.util.Map;\n' +
								'import fast.main.conf.ApiBody;\n' +
								'import fast.main.conf.ApiHead;\n' +
								'import fast.main.util.Super;\n' +
								'' +
								'@ApiHead(value="' + sms + '")\n' +
								'public class ' + names + ' extends Super{\n' +
								'	@ApiBody(name = "初始化", context = "方法说明(在首页API文档详情中显示)", type = "Post")\n' +
								'	public String init(Map<String, Object> rmap) {\n' +
								'		try {\n' +
								'			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）\n' +
								'			initMap(rmap);\n' +
								'			return "xtgl/' + names + '";\n' +
								'		} catch (Exception e) {\n' +
								'			e.printStackTrace();\n' +
								'			return "xtgl/' + names + '";\n' +
								'		}\n' +
								'	}\n\n\n\n' +
								'}\n\n\n\n';
							$.ajax({
								type : "post", //请求方式
								async : false, //是否异步
								url : "./ajax.do?ctrl=Index_save",
								data : {
									url : names,
									type : "java",
									str : hh
								},
								dataType : "json",
								success : function(obj) {},
								error : function(XMLHttpRequest, textStatus, errorThrown) {
									// 状态码
									alert(XMLHttpRequest.status);
									// 状态
									alert(XMLHttpRequest.readyState);
									// 错误信息  
									alert(textStatus);
								}
							});
							addTab(names, 'jsp/run.jsp?file_name=' + names + '&sms=' + sms);
							layer.close(ly);
						},
						cancel : function(index, layero) {
							//右上角关闭回调
							//return false 开启该代码可禁止点击该按钮关闭
						}
					});
				},
				items : { //菜单列表配置
					"add" : {
						name : "Add",
						icon : "add"
					},
				}
			});
			
		});
		
	</script>
</body>
<div id="tcc"
	style="display:none;padding: 30px; line-height: 22px; background-color:white;">

	<div class="layui-inline">
		<label class="layui-form-label">名称：</label>
		<div class="layui-input-inline">
			<input type="text" name="names" id="names" lay-verify="required"
				placeholder="名称必须为英文" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-inline" style="margin-top: 10px;">
		<label class="layui-form-label">说明：</label>
		<div class="layui-input-inline">
			<input type="text" name="sms" id="sms" lay-verify="required"
				style="border-radius: 5px;" placeholder="请输入该功能说明"
				autocomplete="off" class="layui-input">
		</div>
	</div>

</div>
</html>
