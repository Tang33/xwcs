<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<title>企业清册模板管理</title>
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<style type="text/css">
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
}
</style>
</head>

<body style="overflow-x: hidden">
	<input type="hidden" id="dwid" value="${dwid}" />
	<form class="layui-form" id="form1" action="">
		<div class="layui-form" lay-filter="test1">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<h2 class="layui-colla-title">查询条件：</h2>
					<div class="layui-colla-content  layui-show">
						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md6">
										<div class="layui-form-item">
											<label class="layui-form-label">模板名称:</label>
											<div class="layui-input-block">
												<input type="text" name="nsName" lay-verify="title"
													id="nsName" autocomplete="off" placeholder="模糊查询"
													class="layui-input">
											</div>
										</div>
									</div>

									<div class="layui-col-md6">
											<div style="text-align: right;">

												<div class="layui-btn-group">
													<button class="layui-btn layui-btn-normal" id="button"
														type="button" lay-submit="" lay-filter="button">查
														询</button>
												</div>
												<div class="layui-btn-group">
													<button class="layui-btn " id="add" type="button"
														lay-submit="" lay-filter="add">新增</button>
												</div>
											</div>
									</div>

								</div>

							</div>
						</div>





					</div>
				</div>
			</div>

			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend style="font-size: 12px;"></legend>
				<table class="layui-table" id="table" lay-filter="user">
					<thead>
						<tr>
							<th
								lay-data="{field:'xh',width:'15%'}">序号</th>
							<th lay-data="{field:'mbmc',width:'20%'}">模板名称</th>
							<th lay-data="{field:'dch',width:'15%'}">起始行</th>
							<th lay-data="{field:'dcl',width:'15%'}">起始列</th>
						<th
							lay-data="{field:'',width:'20%',toolbar:'#bar'}">操作</th>
						</tr>
					</thead>
					<tbody id="ttbody">


					</tbody>
				</table>
				<div id="page"></div>
				<script type="text/html" id="bar">
  				<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  				<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
			</script>
			</fieldset>
		</div>
	</form>


	<div id="addmain" style="display: none;">
		<form class="layui-form" id="form3" action=""
			style="margin-top: 20px;">
<input type="hidden" id="mblj" name="mblj" lay-filter="mblj" />
<input type="hidden" id="mbbdlj" name="mbbdlj" lay-filter="mbbdlj" />
			
			<div class="layui-form-item">

				<label class="layui-form-label" style="width: 150px;">模板名称:</label>
				<div class="layui-input-inline">
				<input type="text" name="mbmc" id="mbmc" lay-verify="required"
						class="layui-input" /> 
				</div>
					<div class="layui-input-inline" style="width: 40%;">
					<div style="text-align: center;">
						<button class="layui-btn layui-btn-normal" id="select"
							type="button"  lay-filter="select">选择文件</button>
					</div>
				</div>
			</div>
			<div class="layui-form-item">

				<label class="layui-form-label" style="width: 150px;">导出首行:</label>
				<div class="layui-input-inline">
				<input type="text" name="dch" id="dch" lay-verify="required"
						class="layui-input" /> 
				</div>
				<label class="layui-form-label" style="width: 150px;">导出列:</label>
				<div class="layui-input-inline">
				<input type="text" name="dcl" id="dcl" lay-verify="required"
						class="layui-input" /> 
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"></label>
				<div class="layui-input-inline" style="width: 100%;">
					<div style="text-align: center;">
						<button class="layui-btn layui-btn-normal" id="dosave"
							type="button"  lay-filter="dosave">上传并保存</button>
					</div>
				</div>
			</div>

		</form>
	</div>
	
	<div id="editmain" style="display: none;">
		<form class="layui-form" id="form4" action=""
			style="margin-top: 20px;">
<input type="hidden" id="xhedit" name="xhedit" lay-filter="xhedit" />
			<div class="layui-form-item">
				<label class="layui-form-label" >模板名称:</label>
				<div class="layui-input-block" style="width:80%;">
				<input type="text" name="mbmcedit" id="mbmcedit" lay-verify="required"
						class="layui-input" /> 
				</div>					
			</div>
			<div class="layui-form-item">

				<label class="layui-form-label" style="width: 150px;">导出首行:</label>
				<div class="layui-input-inline">
				<input type="text" name="dchedit" id="dchedit" lay-verify="required"
						class="layui-input" /> 
				</div>
				<label class="layui-form-label" style="width: 150px;">导出列:</label>
				<div class="layui-input-inline">
				<input type="text" name="dcledit" id="dcledit" lay-verify="required"
						class="layui-input" /> 
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"></label>
				<div class="layui-input-inline" style="width: 100%;">
					<div style="text-align: center;">
						<button class="layui-btn layui-btn-normal" id="editsave"
							type="button" lay-submit=""  lay-filter="editsave">保存</button>
					</div>
				</div>
			</div>

		</form>
	</div>
</body>

<script>
	var dwid = $("#dwid").val();
	var showlayer;
	var index;
	var wait;
	layui.use('upload',function() {
		var $ = layui.jquery, upload = layui.upload;
		//指定允许上传的文件类型
		upload.render({
					elem : '#select',
					url : 'upload.do?lx=mbdc&name=qcmb',
					before : function(obj) {
						wait = layer.load();
					},
					accept : 'file' //普通文件
					,
					auto : false,
					exts : 'xls|xlsx',
					bindAction : '#dosave',
					done : function(res) {
						console.log(res)
						if (res.code == "0") {
							$("#mbbdlj").val(res.data.bdsrc);
							$("#mblj").val(res.data.src);
							$.ajax({
										type : "post", //请求方式
										async : true, //是否异步
										url : "ajax.do?ctrl=qyqcmbgl_add",
										data : $("#form3").serialize(),
										dataType : "json",
										success : function(obj) {
											console.log(obj)
											if (obj.code == "000") {
												$.ajax({
															type : "post", //请求方式
															async : true, //是否异步
															url : "ajax.do?ctrl=qyqcmbgl_doinput",
															data : {
																mbbdlj:res.data.bdsrc,
																mbid:obj.data.UUID
															},
															dataType : "json",
															success : function(
																	obj1) {
																if (obj1.code == "000") {
																	layer.msg("操作成功！");
																	getData();
																	layer.close(index);
																	layer.close(wait);
																} else {
																	layer.msg("操作失败！");
																	layer.close(wait);
																}

															},
															error : function(
																	XMLHttpRequest,
																	textStatus,
																	errorThrown) {
																// 状态码
																alert(XMLHttpRequest.status);
																// 状态
																alert(XMLHttpRequest.readyState);
																// 错误信息  
																alert(textStatus);
															}
														});
											} else {
												layer.msg("操作失败！");
												layer.close(wait);
											}

										},
										error : function(
												XMLHttpRequest,
												textStatus,
												errorThrown) {
											// 状态码
											alert(XMLHttpRequest.status);
											// 状态
											alert(XMLHttpRequest.readyState);
											// 错误信息  
											alert(textStatus);
										}
									});
						} else {
							layer.msg("上传失败！");
							layer.close(wait);
						}
					}
				});
	});
	$(function() {
		 getData();
		$("#table").attr("lay-data",
				"{width:" + document.body.clientWidth + "}");
	});

	function showLoad() {
		return layer.msg('拼命加载数据中...', {
			icon : 16,
			shade : [ 0.5, '#f5f5f5' ],
			scrollbar : false,
			offset : 'auto',
			time : 100000
		});
	}

	function closeLoad(index) {
		layer.close(index);
	}


	layui.use([ 'form', 'laydate', 'laypage' ],
					function() {
						var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate, laypage = layui.laypage;

						//点击查询

						form.render(null, 'test1'); //更新全部
						laypage.render({
							elem : 'page',
							count : count,
							theme : '#1E9FFF'
						});

						form.on('submit(editsave)', function(data) {
							$.ajax({
								type : "post", //请求方式
								async : true, //是否异步
								url : "ajax.do?ctrl=qyqcmbgl_update",
								data : $("#form4").serialize(),
								dataType : "json",
								success : function(
										obj1) {
									if (obj1.code == "000") {
										getData();
										layer.msg("操作成功！");
										layer.close(showlayer);
									} else {
										layer.msg("操作失败！");
									}

								},
								error : function(
										XMLHttpRequest,
										textStatus,
										errorThrown) {
									// 状态码
									alert(XMLHttpRequest.status);
									// 状态
									alert(XMLHttpRequest.readyState);
									// 错误信息  
									alert(textStatus);
								}
							});
							return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
						});
						form.on('submit(button)', function(data) {
							pageNo = 1; //当点击搜索的时候，应该回到第一页
							getData();
							return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
						});

						form.on('submit(add)', function(data) {
							var index = layer.open({
								type : 1,
								title : '新增模板',
								area : [ '80%', '80%' ],
								shadeClose : false, //点击遮罩关闭
								content : $('#addmain'),
								btn : [ '确定', '取消' ],
								success : function() {
									$("#mblj").val("");
									$("#mbbdlj").val("");
									$("#mbmc").val("");
									$("#dch").val("");
									$("#dcl").val("");
									layui.form.render();
									return;
								},
								cancel : function() {
									return;
								},
								end : function() {
									$('#addmain').css("display", "none");
								}
							});
							return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
						});
					});

	var pageNo = 1;
	var pageSize = 10;
	var count = 0;
	function getData() {

		ajax({
			url : "ajax.do?ctrl=qyqcmbgl_queryData&pageNo=" + pageNo
					+ "&pageSize=" + pageSize,
			data : {
				nsName : $("#nsName").val()
			},
			type : 'post',
			dataType : "Json",
			success : function(obj) {
				if (obj.code == "001") {
					layer.msg(obj.msg);
				} else if (obj != null && obj.data != null) {
					getTbale(obj.data);//拼接表格
					count = obj.count;//数据总条数					
					queryPage();
				}
			}
		});
	}

	//初始化表格
	function getTbale(data) {
		var s = "";
		$.each(data, function(v, o) {

			s += '<tr>';
			s += '<td>' + o.UUID + '</td>';
			s += '<td>' + o.MBMC + '</td>';
			s += '<td>' + o.SJQSH + '</td>';
			s += '<td>' + o.SJQSL + '</td>';

			s += '</tr>';
		});
		$("#ttbody").html(s);
		//执行渲染
		layui.use([ 'table' ], function() {
			var table = layui.table;
			table.init('user', {
				height : 520,
				limit : pageSize
			//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
			//支持所有基础参数
			});
			var layer = layui.layer;
			console.log(layer);
			table.on('tool(user)', function(obj) {
				console.log(obj);
				var data = obj.data;
				console.log(data);
				var layEvent = obj.event;
				console.log(layEvent);
				if (layEvent == 'edit') {
					showlayer = layer.open({
						type : 1,
						title : '修改',
						area : [ '80%', '80%' ],
						shadeClose : false, //点击遮罩关闭
						content : $('#editmain'),
						success : function() {
							$("#mbmcedit").val(data.mbmc);
							$("#dchedit").val(data.dch);
							$("#dcledit").val(data.dcl);
							$("#xhedit").val(data.xh);

						},
						cancel : function() {
							return;
						},
						end : function() {

						}
					});
				}else if(layEvent=='del'){
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						url : "ajax.do?ctrl=qyqcmbgl_update",
						data : data,
						dataType : "json",
						success : function(
								obj1) {
							if (obj1.code == "000") {
								layer.msg("操作成功！");
							} else {
								layer.msg("操作失败！");
							}

						},
						error : function(
								XMLHttpRequest,
								textStatus,
								errorThrown) {
							// 状态码
							alert(XMLHttpRequest.status);
							// 状态
							alert(XMLHttpRequest.readyState);
							// 错误信息  
							alert(textStatus);
						}
					});
				}

			});
		});

		if (data == null || data.length <= 0) {
			$("#page").hide();
		} else {
			$("#page").show();
		}
	}

	function queryPage() {
		layui.use([ 'laypage' ], function() {
			laypage = layui.laypage;
			laypage.render({
				//注意，这里的page1 是 ID，不用加 # 号
				elem : 'page',
				//数据总数，从服务端得到
				count : count,
				//每页显示条数
				limit : pageSize,
				//条数列表
				limits : [ 10, 20, 30, 50 ],
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
</script>

</html>