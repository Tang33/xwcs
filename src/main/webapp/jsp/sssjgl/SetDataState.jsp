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

<title>修改数据状态</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->

<!-- <link rel="stylesheet" href="./static/layui/css/layui.css" media="all">
<script src="./static/layui/layui.js" charset="utf-8"></script>
<script src="./static/assets/js/jquery-1.8.3.min.js" charset="utf-8"></script>
<script src="./static/js/easy.ajax.js" charset="utf-8"></script> -->

<style type="text/css">
</style>
</head>
<body style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">
		本功能用于对系统的“重点税源类别管理”进行操作管理！</blockquote>
	<form class="layui-form" id="form" action="">
		<input id="cslx" name="cslx" type="hidden" value="0" />
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md3" style="width:18%">
							<div class="layui-inline">
								<label class="layui-form-label">入库年月</label>
								<div class="layui-input-inline">
									<input id="rkrq" name="rkrq" type="text" placeholder="请输入入库年月"
										class="layui-input">
								</div>
							</div>
						</div>

						<div class="layui-col-md3">
							<button class="layui-btn" id="button" type="button" lay-submit=""
								lay-filter="button">查询</button>
								<button class="layui-btn" id="add" type="button" lay-submit=""
								lay-filter="add">添加</button>
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
						<th lay-data="{field:'rkrq',width:'200'}">入库日期</th>
						<th lay-data="{field:'kczt',width:'200'}">可查状态</th>
						<th lay-data="{field:'dfkj',toolbar:'#bar1'}">操作</th>					
					</tr>
				</thead>
				<tbody id="ttbody">

				</tbody>
			</table>
			<div id="page1"></div>
			<script type="text/html" id="bar1">

				{{# if(d.kczt=="可查"){ }}
  				<a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="bkc">不可查</a>
				{{# } }}
				{{# if(d.kczt=="不可查"){ }}
  				<a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="kc">可查</a>
				{{# } }}
				</script>
		</fieldset>
	</form>
<div id="addmain" style="display: none;">
		<form class="layui-form" id="form3" action=""
			style="margin-top: 20px;">
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">入库日期：</label>
				<div class="layui-input-inline">
					<input type="text" name="rkrqadd" id="rkrqadd" lay-verify="required"
						class="layui-input" />
				</div>				
			</div>
			
		</form>
	</div>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	layui.use('laydate', function() {
		var laydate = layui.laydate;
		laydate.render({
			elem : '#rkrq',
			type : 'month',
			format:'yyyyMM'
		});
		laydate.render({
			elem : '#rkrqadd',
			type : 'month',
			format:'yyyyMM'
		});
	});
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
			//url : "ajax.do?ctrl=SetDataState_query&pageNo=" + pageNo + "&pageSize="+ pageSize,
			url : "SetDataState_query.do?pageNo=" + pageNo + "&pageSize="+ pageSize,
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
			s += '<tr><td>' + (o.RKRQ == undefined ? "-" : o.RKRQ)+ '</td>';
			s += '<td>' + (o.STATE == "0" ? "不可查" : "可查") + '</td></tr>';
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
		} else {
			$("#page1").show();
		}

	}

	//初始化Element
	layui.use([ 'element', 'layer' ], function() {
		var element = layui.element;
		var layer = layui.layer;

		//监听折叠
		element.on('collapse(test)', function(data) {
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
		form.on('submit(add)', function(data) {
			var index = layer.open({
				type : 1,
				title : '新增',
				area : [ '500px', '200px' ],
				shadeClose : false, //点击遮罩关闭
				content : $('#addmain'),
				btn : [ '确定', '取消' ],
				success : function() {
					$("#rkrqadd").val($("#rkrq").val());
					return;
				},
				yes : function(index, layero) {
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						//url : "ajax.do?ctrl=SetDataState_add",
						url : "SetDataState_add.do",
						data : $("#form3").serialize(),
						dataType : "json",
						success : function(obj) {
							if (obj.code == '000') {
								layer.msg('添加成功！');
							} else {
								layer.alert('添加 失败！');
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
		form.on('submit(kc)', function(data) {
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				//url : "ajax.do?ctrl=rksjgl_doState&State=1",
				url : "rksjgl_doState.do?State=1",
				data : $("#form").serialize(),
				dataType : "json",
				success : function(obj) {
					if (obj.code == '000') {
						layer.msg('操作成功！');
					} else {
						layer.alert('操作 失败！');
					}
					$("#button").click();
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
			return false;
		});
		form.on('submit(bkc)', function(data) {
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				//url : "ajax.do?ctrl=rksjgl_doState&State=0",
				url : "rksjgl_doState.do?State=0",
				data : $("#form").serialize(),
				dataType : "json",
				success : function(obj) {
					if (obj.code == '000') {
						layer.msg('操作成功！');
					} else {
						layer.alert('操作 失败！');
					}
					$("#button").click();
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
			return false;
		});
	

	});
	
	layui.use('table', function() {
		var table = layui.table;
		//方法级渲染
		//监听工具条
		table.on('tool(user)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
			var data = obj.data; //获得当前行数据
			var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			if (layEvent === 'kc') { //删除
				layer.confirm('真的设置' + data.rkrq + '的数据可查么?', function(index) {
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						//url : "ajax.do?ctrl=SetDataState_doState&State=1",
						url : "SetDataState_doState.do?State=1",
						data : data,
						dataType : "json",
						success : function(obj) {
							if (obj.code == '000') {
								layer.msg('操作成功！');
							} else {
								layer.alert('操作 失败！');
							}
							$("#button").click();
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

				});
			} else if(layEvent === 'bkc'){
					var data = obj.data; //获得当前行数据
					layer.confirm('真的设置' + data.rkrq + '的数据不可查么?', function(index) {
						$.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							//url : "ajax.do?ctrl=SetDataState_doState&State=0",\
							url : "SetDataState_doState.do?State=0",
							data : data,
							dataType : "json",
							success : function(obj) {
								if (obj.code == '000') {
									layer.msg('操作成功！');
								} else {
									layer.alert('操作 失败！');
								}
								$("#button").click();
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

					});
			}

		});

	});
</script>


</html>