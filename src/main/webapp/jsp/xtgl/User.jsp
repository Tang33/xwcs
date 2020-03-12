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

<title>系统管理-后台页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page='<%="../load.jsp"%>'></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->

<style type="text/css">
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
}
</style>
</head>

<body>
	<blockquote class="layui-elem-quote layui-text">
		本功能用于对登录的“用户”进行操作管理！(非数据库用户)</blockquote>
	<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">

					<div class="layui-row">
						<div class="layui-col-md3" style="width:35%">
							<div class="layui-inline">
								<label class="layui-form-label">账号：</label>
								<div class="layui-input-inline">
									<input type="text" name="uNo" id="uNo"
										autocomplete="off" class="layui-input">
								</div>
							</div>
							<div class="layui-inline">
								<label class="layui-form-label">用户名：</label>
								<div class="layui-input-inline">
									<input type="text" name="uName" id="uName"
										autocomplete="off" class="layui-input">
								</div>
							</div>
						</div>
						<div class="layui-col-md3">
							<div style="text-align: left;">
								<!-- <button class="layui-btn layui-btn-normal" id="button" type="button">查 询</button> -->
								<button class="layui-btn layui-btn-normal" id="button"
									type="button" lay-submit="" lay-filter="button">查 询</button>
								<button class="layui-btn layui-btn-normal" id="add"
									type="button" lay-submit="" lay-filter="add">新建用户</button>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>

			<table class="layui-table" id="table" lay-filter="user">
				 
				<tbody id="ttbody">

				</tbody>
			</table>
			<div id="page1"></div>
			<script type="text/html" id="bar">
  				<a class="layui-btn layui-btn-xs" lay-event="edit">修改</a>
  				<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
			</script>
		</fieldset>

	</form>
	<div id="querymain" style="display: none;">
		<form class="layui-form" id="form1" action="">
			<table class="layui-hide" id="table_col" lay-filter="user_col"></table>
		</form>
	</div>
	<div id="editmain" style="display: none;">
		<form class="layui-form" id="form4" action=""
			style="margin-top: 20px;">
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">账号：</label>
				<div class="layui-input-inline">
					<input type="text" name="uno1" id="uno1" lay-verify="required"
						class="layui-input" /> <input type="hidden" name="uuid1"
						id="uuid1" lay-verify="required" class="layui-input" />
				</div>
				<label class="layui-form-label">用户名：</label>
				<div class="layui-input-inline">
					<input type="text" name="uname1" id="uname1" lay-verify="required"
						class="layui-input" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">密码：</label>
				<div class="layui-input-inline">
					<input type="text" name="upwd1" id="upwd1" lay-verify="required"
						class="layui-input" />
				</div>
				<label class="layui-form-label">所属单位：</label>
				<div class="layui-input-inline">
					<select name="ssdwedit" id="ssdwedit" lay-verify="required" lay-search=""
						lay-filter="ssdwedit">
					</select>
				</div>

			</div>
			<div class="layui-form-item">
				<label class="layui-form-label"  style="width: 150px;">是否启用：</label>
				<div class="layui-input-inline">
					<input type="checkbox" checked="" id="yxbz1" name="yxbz1"
						lay-skin="switch" lay-filter="yxbz1" title="是否启用" /> <input
						type="hidden" id="yxbz3" name="yxbz3" value="Y" />
				</div>
			</div>
		</form>
	</div>
	<div id="addmain" style="display: none;">
		<form class="layui-form" id="form3" action=""
			style="margin-top: 20px;">
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">账号：</label>
				<div class="layui-input-inline">
					<input type="text" name="uno" id="uno" lay-verify="required"
						class="layui-input" />
				</div>
				<label class="layui-form-label">用户名：</label>
				<div class="layui-input-inline">
					<input type="text" name="uname" id="uname" lay-verify="required"
						class="layui-input" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">密码：</label>
				<div class="layui-input-inline">
					<input type="text" name="upwd" id="upwd" lay-verify="required"
						class="layui-input" />
				</div>
				<label class="layui-form-label" >所属单位：</label>
				<div class="layui-input-inline">
					<select name="ssdw" id="ssdw" lay-verify="required"
						lay-search="" lay-filter="ssdw">
					</select>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label"  style="width: 150px;">是否启用：</label>
				<div class="layui-input-inline">
					<input type="checkbox" checked="" id="yxbz" name="yxbz" lay-skin="switch" lay-filter="yxbz" title="是否启用" />
					<input type="hidden" id="yxbz2" name="yxbz2" value="Y" />
				</div>
			</div>
		</form>
	</div>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>

	$(function() {
		$("#table").attr("lay-data","{width:" + document.body.clientWidth + "}");
		
		getData();
		initdw(); 
	});

	function initdw() {
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "user/querydw.do",
			data : {},
			dataType : "json",
			success : function(obj) {
				if (obj != null && obj.code == '000') {
					var str = "<option value=''>请选择上级单位</option>";
					for (var i = 0; i < obj.data.length; i++) {
						str += "<option value='" + obj.data[i].SSDW_DM + "'>"
								+ obj.data[i].SSDW_MC + "</option>";
					}
					$("#ssdw").html(str);
					$("#ssdwedit").html(str);
					layui.use([ 'form' ], function() {
						var form = layui.form;
						form.render('select');
					});

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
		})
	} 

	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 10;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
	function getData() {
		
		layui.use(['table'], function() {
			loadlayer = layer.load();
			layui.table.render({
				elem: '#table',
				method: 'post',
				url: 'user/query.do',
				limit: 10,
				page: 'true',
				where: {
					uNo: $('#uNo').val(),
					uName: $('#uName').val()
				},
				height: 500,
				width: document.body.clientWidth,
				limit: 10,
				page: 'true',
				cols:[[
					{field: 'UUID',title: 'ID',width: '5%'},
 					{field: 'UNAME',title: '用户名',width: '15%'},
					{field: 'UNO',title: '账号',width: '15%'},
					{field: 'UPWD',title: '密码',width: '15%'},
					{field: 'SSDW_DM',title: '所属单位代码',width: '10%'},
					{field: 'SSDW_MC',title: '所属单位',width: '20%'},
					{field: 'YXBZ',title: '有效标志',width: '8%'},
					{field: 'right',title: '操作',width: '15%',fixed:'right',toolbar:'#bar'}
				]],
				done: function(res, curr, count) {
					layer.close(loadlayer);
					layer.msg('查询成功！',{
						time: 1000,
						offset: 'auto'
					})
				}				
			})
		})
		
	}

	
	//初始化Element
	layui.use([ 'element', 'layer' ], function() {
		var element = layui.element;
		var layer = layui.layer;

		//监听折叠
		element.on('collapse(test)', function(data) {
			layer.msg('展开状态：' + data.show);
		});
	});
	//初始化Element
	layui.use([ 'form' ], function() {
		var form = layui.form;

		form.on('submit(button)', function(data) {
			pageNo = 1; //当点击搜索的时候，应该回到第一页
			getData();
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		//监听指定开关
		  form.on('switch(yxbz)', function(data){
		    if(this.checked){
		    	$("#yxbz2").val("Y");
		    	}else{
			    	$("#yxbz2").val("N");		    		
		    	}
		  });
		//监听指定开关
		  form.on('switch(yxbz1)', function(data){
		    if(this.checked){
		    	$("#yxbz3").val("Y");
		    	}else{
			    	$("#yxbz3").val("N");		    		
		    	}
		  });
		form.on('submit(add)', function(data) {
			var index = layer.open({
				type : 1,
				title : '新增用户',
				area : [ '800px', '500px' ],
				shadeClose : false, //点击遮罩关闭
				content : $('#addmain'),
				btn : [ '确定', '取消' ],
				success : function() {
					$("#uno").val("");
					$("#uname").val("");
					$("#upwd").val("");
					$("#ssdw").val("");
					$("#yxbz2").val("");
					layui.form.render();
					return;
				},
				yes : function(index, layero) {
					var uname = $("#uname").val();
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						url : "user/add.do",
						data : $("#form3").serialize(),
						dataType : "json",
						success : function(obj) {
							if (obj.code == '000') {
								layer.msg('添加用户【' + uname + '】 成功！');
							} else {
								layer.alert('添加用户【' + uname + '】 失败！');
							}
							layer.close(index);
							$("#button").click();
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown) {
							// 状态码
							alert(XMLHttpRequest.status);
							// 状态
							alert(XMLHttpRequest.readyState);
							// 错误信息  
							alert(textStatus);
						}
					});
				},
				btn2 : function(index, layero) {
					return;
				},
				cancel : function() {
					return;
				},
				end : function() {
					//$('#addmain').css("display", "none");
				}
			});
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});

	});
	 layui.use('table', function() {
		var table = layui.table;
		//方法级渲染
		//监听工具条 		
		table.on('tool(user)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
			var data = obj.data; //获得当前行数据
			var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			if (layEvent === 'del') { //删除
				layer.confirm(''
				,{area:['30%','27%']
				,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">真的删除用户:【' + data.UNAME + '】么?</div>'}
				, function(index) {
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						url : "user/del.do",
						data : data,
						dataType : "json",
						success : function(obj) {
							if (obj.code == '000') {
								layer.msg('删除用户【' + data.uname + '】 成功！');
							} else {
								layer.alert('删除用户【' + data.uname + '】失败！');
							}
							layer.close(index);
							$("#button").click();
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown) {
							// 状态码
							alert(XMLHttpRequest.status);
							// 状态
							alert(XMLHttpRequest.readyState);
							// 错误信息  
							alert(textStatus);
						}
					});

				});
			} else if (layEvent === 'edit') { //编辑
				//do something

				var index = layer.open({
					type : 1,
					title : '修改用户信息',
					area : [ '800px', '500px' ],
					shadeClose : false, //点击遮罩关闭
					content : $('#editmain'),
					btn : [ '确定', '取消' ],
					success : function() {
						$("#uuid1").val(data.UUID);
						$("#uno1").val(data.UNO);
						$("#uname1").val(data.UNAME);
						$("#yxbz1").val(data.YXBZ);
						if (data.YXBZ == "Y") {
							$("#yxbz1").prop("checked",true);
						} else {
							//$("#yxbz2").prop("checked",true);
							$("#yxbz1").prop("checked",false);
						}
						$("#upwd1").val(data.UPWD);
						$("#ssdwedit").val(data.SSDW_DM);
						layui.use([ 'form' ], function() {
							var form = layui.form;
							form.render('checkbox');
							form.render('select');
						});
					},
					yes : function(index, layero) {//添加人员
						$.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							url : "user/update.do",
							data : $("#form4").serialize(),
							dataType : "json",
							success : function(obj) {
								if (obj.code == '000') {
									layer.msg('修改用户【' + data.uname + '】成功！');
								} else {
									layer.alert('修改用户【' + data.uname + '】失败！');
								}
								layer.close(index);
								$("#button").click();
							},
							error : function(XMLHttpRequest, textStatus,
									errorThrown) {
								// 状态码
								alert(XMLHttpRequest.status);
								// 状态
								alert(XMLHttpRequest.readyState);
								// 错误信息  
								alert(textStatus);
							}
						});
					},
					btn2 : function(index, layero) {
						return;
					},
					cancel : function() {
						return;
					},
					end : function() {
						//$('#addmain').css("display", "none");
					}
				});
				//同步更新缓存对应的值
				obj.update({
					TABLE_NAME : data.TABLE_NAME
				});
			}
		});
	
	}); 
	
</script>


</html>
