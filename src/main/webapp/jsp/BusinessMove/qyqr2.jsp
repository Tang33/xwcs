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

<title>流程管理—企业迁出管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />


<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<script src="./css/layui/layui.js" charset="utf-8"></script>
<style type="text/css">
.sweet-alert{
	z-index:99999999;
    border: 1px solid #eee;
	box-shadow: 0 0 3px #c7b6b6;
}
</style>
</head>

<body style="overflow-x: hidden">
		<input type="hidden" id="uno" value="${uno}" />
		<blockquote class="layui-elem-quote layui-text">本功能用于对系统的“企业迁入”进行操作管理！</blockquote>
		<form class="layui-form" id="form" action="">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<div class="layui-colla-content  layui-show">
						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md4">
										<div class="layui-inline">
											<label class="layui-form-label">查询时间</label>
											<div class="layui-input-inline">
						  						<input type="text" class="layui-input" id="yearNmonth" name="yearNmonth" placeholder="请选择日期">
											</div>
								 		</div>
									</div>
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<label class="layui-form-label">街道</label>
											<div class="layui-input-block">
												<select name="ssjd" lay-filter="ssjd" id="ssjd">
													<option value="0">请选择</option>
													<option value="91">新街口</option>
													<option value="92">梅园</option>
													<option value="93">后宰门</option>						
													<option value="94">玄武湖</option>					
													<option value="95">玄武门</option>					
													<option value="96">锁金村</option>					
													<option value="97">孝陵卫</option>					
													<option value="98">红山</option>					
													<option value="99">徐庄</option>
												</select>
											</div>
										</div>
									</div>
								</div>

							</div>
						</div>

						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<label class="layui-form-label">纳税人名称</label>
											<div class="layui-input-block">
												<input style="width: 80%;" id="paramNsrmc" name="nsrmc" type="text" placeholder="请输入纳税人名称" class="layui-input">
											</div>
										</div>
									</div>
									
									<div class="layui-col-md4">
										<div class="layui-form-item">
											 <label class="layui-form-label" style="width: 20%;margin-left: -6%;">纳税人识别号</label>
				                			<div class="layui-input-inline">
				                 	 			<input style="width: 241%;" id="paramNsrsbh" name="nsrsbh" type="text" placeholder="请输入纳税人识别号" class="layui-input">
				                			</div>
										</div>
									</div>
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<div class="layui-row">
												<div class="layui-col-md6">
													<div style="text-align: center;">
														<div class="layui-btn-group">
															<button lay-submit class="layui-btn" id="search_btn" lay-filter="search_btn" >查询</button>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						
					</div>
				</div>
			</div>

			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend>查询显示</legend>

				<table class="layui-table" id="qrtable" lay-filter="qrtable">
				
				</table>
				<div id="page1"></div>
			</fieldset>
		</form>
		<div id="editmain" style="display: none;">
		<form class="layui-form" id="form5" action="" style="margin-top: 50px;">	
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">留言:</label>
				<div class="layui-input-inline" style="width: 50%;">
					<textarea rows="" cols="" id="ly" name="ly" class="layui-textarea" lay-verify="required"></textarea>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"></label>
				<div class="layui-input-inline" style="width: 50%;">
					<div style="text-align: right;">
						<button class="layui-btn layui-btn-normal" id="updateSave"
							type="button" lay-submit="" lay-filter="updateSave">确认</button>
					</div>
				</div>
			</div>
		</form>
	</div>
	</body>

	<script>
	var loadlayer;
	
	//查询数据库的最大月份
	getMaxData();
	function getMaxData() {
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url : "yskmcx/getNsDate.do",
			data :"",
			dataType : "json",
			success : function(obj) {
				if (obj.code == "000") {
					var r = obj.data[0];
					//日期条件默认值为数据库中数据的最大入库日期
					var year = r.RKRQ.substring(0,4);
					var month = r.RKRQ.substring(4,6);
					var rr = year + "/" + month;
					$("#yearNmonth").val(rr + " - " + rr);
				}
			}
		})
	}
	  
	layui.use('laydate', function() {
		var laydate = layui.laydate;

		//年月范围
		laydate.render({
			elem : '#yearNmonth',
			type : 'month',
			range : true
		});
	});
	
	$(function() {
		getMaxData();
		setTimeout("getData()", 600);
	}); 
	
	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 15;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
	function getData() {
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步 
			url : "qyqr/doQuery.do",
			data : $("#form").serialize(),
			dataType : "json",
			success : function(obj) {
				getTable(obj.data);//拼接表格
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
    

		function getTable(data) {
			layui.use('table', function() {
				var table = layui.table;

				//第一个实例
				table.render({
					elem : '#qrtable',
					data : data //数据
					,
					page : true //开启分页
					,
					width : document.body.clientWidth,
					cols : [ [ //表头
					{
						field : 'ID',
						title : 'ID',
						fixed : 'left',
						width : 60,
						style:'display:none;',
						hide:true
						
					}, {
						field : 'NSRSBH',
						title : '纳税人识别号'
					}, {
						field : 'NSRMC',
						title : '纳税人名称'
					}, {
						field : 'JDMC',
						title : '所属街道',
						width : 120
					}, {
						field : 'YQRD',
						title : '拟迁出地',
						width : 120
					},{
						field : 'QRRQ',
						title : '迁入日期'
					},  {
						field : 'STATUS',
						title : '状态',
						width : 100,
						templet : function(data) {
							if (data.STATUS == '4') {
								return "已审核";
							} else if (data.STATUS == '5'){
								return "已拒绝";
							} 
						}
					}] ]
				});
			});

		}

		layui.use([ 'form', 'laydate', 'table' ],function() {
			var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table;
			form.render('select');
			
			form.on('submit(search_btn)', function(data) {
				getData();
				loadlayer = layer.load();
				layer.close(loadlayer);
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});
			

		});
		
		
	</script>
	

</html>
