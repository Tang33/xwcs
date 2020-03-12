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

<title>迁入迁出统计查询</title>

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
		<blockquote class="layui-elem-quote layui-text">本功能用于对系统的“迁入迁出统计查询”进行操作管理！</blockquote>
		<form class="layui-form" id="form" action="">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<div class="layui-colla-content  layui-show">
						<div class="layui-row">
							<div class="layui-col-md4">
								<label class="layui-form-label">查询时间</label>
								<div class="layui-input-inline">
			  						<input type="text" class="layui-input" id="yearNmonth" name="yearNmonth" placeholder="请选择日期">
								</div>
								<button style="margin-left: 5%;" lay-submit class="layui-btn" id="search_btn" lay-filter="search_btn" >查询</button>
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
	</body>

	<script>
	var loadlayer;
	
	//查询数据库的最大月份
	getMaxData();
	function getMaxData() {
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url : "qrqctjcx/getNsDate.do",
			data : "",
			dataType : "json",
			success : function(obj) {
				if (obj.code == "000") {
					var r = obj.data[0];
					//日期条件默认值为数据库中数据的最大入库日期
					var year = r.RKRQ.substring(0,4);
					var month = r.RKRQ.substring(4,6);
					var rr = year + "-" + month;
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
		/* $.ajax({
			type : "post", //请求方式
			async : false, //是否异步 
			url : "ajax.do?ctrl=tjcx_doQuery",
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
		});  */
		
		layui.use('table', function() {
			var table = layui.table;
			//第一个实例
			
			table.render({
				elem : '#qrtable',
				method: 'post',
				url : "qrqctjcx/doQuery.do",
				where: {
					yearNmonth : $("#yearNmonth").val()
				},
				page : true, //开启分页
				limit: 10,
				width : document.body.clientWidth,
				cols : [ [ //表头
				{field : 'JD_MC',title : '街道',fixed : 'left'},
				{field : 'HS',title : '迁出户数'},
				{field : 'HSZB',title : '迁出户数占比'},
				{field : 'SK',title : '迁出税款'},
				{field : 'SKZB',title : '迁出税款占比'} 
				] ],
				done: function (res, curr, count) {
					
				}
			})
		})
		
	}
    

		/* function getTable(data) {
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
						field : 'JD_MC',
						title : '街道',
						fixed : 'left'
					},{
						field : 'HS',
						title : '迁出户数'
					}, {
						field : 'HSZB',
						title : '迁出户数占比'
					}, {
						field : 'SK',
						title : '迁出税款'
					}, {
						field : 'SKZB',
						title : '迁出税款占比'
					} ] ]
				});
			});

		} */

		layui.use([ 'form', 'laydate', 'table' ],function() {
			var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table;
			form.render('select');
			
			form.on('submit(search_btn)', function(data) {
				loadlayer = layer.load();
				getData();
				layer.close(loadlayer);
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});

		});
		
		
	</script>
	

</html>
