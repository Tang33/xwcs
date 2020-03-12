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
		本功能用于对街道进行操作管理！)</blockquote>
	<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">

					<div class="layui-row">
						<div class="layui-col-md3" style="width:18%">
							<div class="layui-inline">
								<label class="layui-form-label">关键字：</label>
								<div class="layui-input-inline">
									<input type="text" name="findText" id="findText"
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
									type="button" lay-submit="" lay-filter="add">新增街道</button>
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
				<thead>
					<tr>
						<th lay-data="{field:'jddm',width:'5%'}">街道代码</th>
						<th lay-data="{field:'jdmc',width:'15%'}">街道名称</th>
						<th lay-data="{field:'jdsm',width:'15%'}">街道说明</th>
						<th lay-data="{field:'glfw',width:'15%'}">管理范围</th>
						<th lay-data="{field:'sfqd',width:'10%'}">是否启用</th>
						<th
							lay-data="{field:'right',width:'15%',fixed:'right',toolbar:'#bar'}">操作</th>
				</thead>
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
				<label class="layui-form-label" style="width: 150px;">街道代码：</label>
				<div class="layui-input-inline">
					<input type="text" name="jddmedit" id="jddmedit" lay-verify="required"
						class="layui-input" /> <input type="hidden" name="idedit"
						id="idedit" lay-verify="required" class="layui-input" />
				</div>
				<label class="layui-form-label">街道名称：</label>
				<div class="layui-input-inline">
					<input type="text" name="jdmcedit" id="jdmcedit" lay-verify="required"
						class="layui-input" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">街道说明：</label>
				<div class="layui-input-inline">
					<input type="text" name="jdsmedit" id="jdsmedit" lay-verify="required"
						class="layui-input" />
				</div>
				<label class="layui-form-label">管理范围：</label>
				<div class="layui-input-inline">					
				<input type="text" name="glfwedit" id="glfwedit" lay-verify="required"
						class="layui-input" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label"  style="width: 150px;">是否启用：</label>
				<div class="layui-input-inline">
					<input type="checkbox"  id="yxbz1" name="yxbz1"
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
				<label class="layui-form-label" style="width: 150px;">街道代码：</label>
				<div class="layui-input-inline">
					<input type="text" name="jddm" id="jddm" lay-verify="required"
						class="layui-input" />
				</div>
				<label class="layui-form-label">街道名称：</label>
				<div class="layui-input-inline">
					<input type="text" name="jdmc" id="jdmc" lay-verify="required"
						class="layui-input" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">街道说明：</label>
				<div class="layui-input-inline">
					<input type="text" name="jdsm" id="jdsm" lay-verify="required"
						class="layui-input" />
				</div>
				<label class="layui-form-label">管理范围：</label>
				<div class="layui-input-inline">
				<input type="text" name="glfw" id="glfw" lay-verify="required"
						class="layui-input" />
					
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label"  style="width: 150px;">是否启用：</label>
				<div class="layui-input-inline">
					<input type="checkbox" checked="" id="yxbz" name="yxbz"
						lay-skin="switch" lay-filter="yxbz" title="是否启用" /> <input
						type="hidden" id="yxbz2" name="yxbz2" value="Y" />
				</div>
			</div>
		</form>
	</div>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	$(function() {
		$("#table").attr("lay-data","{width:" + document.body.clientWidth + "}");
		//initdw();
		getData();
	});

	

	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 10;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
	function getData() {
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "jdgl/query.do?pageNo=" + pageNo + "&pageSize="
					+ pageSize,
			data : $("#form").serialize(),
			dataType : "json",
			success : function(obj) {
				if (obj != null && obj.code == '000') {
					getTbale(obj.data);//拼接表格
					count = obj.count;//数据总条数
					queryPage();
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
	}

	function queryPage() {
		layui.use([ 'laypage' ], function() {
			laypage = layui.laypage;
			laypage.render({
				elem : 'page1' //注意，这里的page1 是 ID，不用加 # 号
				,
				count : count //数据总数，从服务端得到
				,
				limit : pageSize//每页显示条数
				,
				limits : [ 10, 20, 30, 50 ]//条数列表
				,
				layout : [ 'prev', 'page', 'next', 'skip', 'count', 'limit' ],
				curr : pageNo,
				jump : function(data, first) {
					//obj包含了当前分页的所有参数，比如：
					pageNo = data.curr;
					pageSize = data.limit;
					//首次不执行
					if (!first) {
						//do something
						getData();
					}
				}
			});
		});

	}
	//初始化表格
	function getTbale(data) {
		var s = "";
		$.each(data, function(v, o) {
			s += '<tr><td>' + o.JD_DM + '</td>';
			s += '<td>' + o.JD_MC + '</td>';
			s += '<td>' + (o.JD_SM==undefined?"-":o.JD_SM) + '</td>';
			s += '<td>' + (o.JD_GLFW==undefined?"-":o.JD_GLFW) + '</td>';
			s += '<td>' + (o.XYBZ == "1" ? "启用" : "未启用") + '</td></tr>';
		});
		$("#ttbody").html(s);
		//执行渲染
		layui.use([ 'table' ], function() {
			layui.table.init('user', {
				height : 480 //设置高度
				,
				limit : pageSize
			//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
			//支持所有基础参数
			});
		});

		if (data == null || data.length <= 0) {
			$("#page1").hide();
		}

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
		    	$("#yxbz2").val("1");
		    	}else{
			    	$("#yxbz2").val("0");		    		
		    	}
		  });
		//监听指定开关
		  form.on('switch(yxbz1)', function(data){
		    if(this.checked){
		    	$("#yxbz3").val("1");
		    	}else{
			    	$("#yxbz3").val("0");		    		
		    	}
		  });
		form.on('submit(add)', function(data) {
			var index = layer.open({
				type : 1,
				title : '新增街道',
				area : [ '800px', '500px' ],
				shadeClose : false, //点击遮罩关闭
				content : $('#addmain'),
				btn : [ '确定', '取消' ],
				success : function() {
					$("#jdmc").val("");
					$("#jddm").val("");
					$("#jdsm").val("");
					$("#glfw").val("");
					$("#yxbz2").val("1");
					layui.form.render();
					return;
				},
				yes : function(index, layero) {
					var jdmc = $("#jdmc").val();
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						url : "jdgl/add.do",
						data : $("#form3").serialize(),
						dataType : "json",
						success : function(obj) {
							if (obj.code == '000') {
								layer.msg('添加街道【' + jdmc + '】 成功！');
							} else {
								layer.alert('添加街道【' + jdmc + '】 失败！');
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
				,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">真的删除街道:【' + data.jdmc + '】么?</div>'}
				
				, function(index) {
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						url : "jdgl/del.do",
						data : data,
						dataType : "json",
						success : function(obj) {
							if (obj.code == '000') {
								layer.msg('删除街道【' + data.jdmc + '】 成功！');
							} else {
								layer.alert('删除街道【' + data.jdmc + '】失败！');
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
						$("#idedit").val(data.jddm);
						$("#jdmcedit").val(data.jdmc);
						$("#jddmedit").val(data.jddm);
						$("#jdsmedit").val(data.jdsm);
						$("#glfwedit").val(data.glfw);
						$("#yxbz3").val((data.sfqd=="启用"?"1":"0"));
						if(data.sfqd=='启用'){
							
							 $("#yxbz1").prop("checked", true);
							 
							
						}else{
							$("#yxbz1").prop("checked", false);
							
						} 
						
						layui.use([ 'form' ], function() {
							var form = layui.form;
							/* form.val("#form4", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
								  'open': $("#yxbz3").val==1?true:false
								  
								}); */
							form.render('checkbox');
							form.render('select');
						});
					},
					yes : function(index, layero) {//添加人员
						$.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							url : "jdgl/update.do",
							data : $("#form4").serialize(),
							dataType : "json",
							success : function(obj) {
								if (obj.code == '000') {
									layer.msg('修改街道【' + data.jdmc + '】成功！');
								} else {
									layer.alert('修改街道【' + data.jdmc + '】失败！');
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
