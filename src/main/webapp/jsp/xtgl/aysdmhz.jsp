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
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<title>按预算科目代码汇总查询</title>
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
	<form class="layui-form" id="form1" action="">
		<div class="layui-form" lay-filter="test1">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<h2 class="layui-colla-title">查询条件：</h2>
					<div class="layui-colla-content  layui-show" style="line-height: 53px;margin: 0px auto;">
						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md5" style="width: 45%;">
										<div class="layui-inline">
											<label class="layui-form-label" style="width: 40px;padding: 9px 15px;">月份:</label>
											<div class="layui-input-inline">
												<input type="text" class="layui-input" id="yearNmonth"
													name="yearNmonth" placeholder="请选择日期">
											</div>
										</div>
									</div>
									<div class="layui-col-md5">
										<div style="text-align: center;">
											<div class="layui-btn-group">
												<button class="layui-btn layui-btn-normal" id="button"
													type="button" lay-submit="" lay-filter="button">查
													询</button>
												<button class="layui-btn layui-btn-normal" id="exportExcel"
													type="button" lay-submit="" lay-filter="exportExcel">导出</button>
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
				<legend style="font-size: 12px;">查询展示</legend>

				<table class="layui-table" id="table" lay-filter="user">
					<thead>
						<tr>
							<th lay-data="{field:'yskmdm'}">预算科目代码</th>
							<th lay-data="{field:'yskmmc'}">预算科目名称</th>
							<th lay-data="{field:'qkj'}">全口径</th>
							<th lay-data="{field:'dfkj'}">地方口径</th>
							<th lay-data="{field:'sj'}">时间</th>
						</tr>
					</thead>
					<tbody id="ttbody">

					</tbody>
			</table>
			<div id="page"></div>
			</fieldset>
		</div>
	</form>
</body>

<script>
		function showLoad() {
		    return layer.msg('拼命加载数据中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:100000});
		}
		
		function closeLoad(index) {
		    layer.close(index);
		}
		function showSuccess() {
		    layer.msg('查询成功！',{time: 1000,offset: 'auto'});
		}
		layui.use(['form', 'layedit', 'laydate','laypage'], function() {
			var form = layui.form,
				layer = layui.layer,
				layedit = layui.layedit,
				laydate = layui.laydate,
				laypage=layui.laypage;
			//年月范围
			laydate.render({
				elem: '#yearNmonth',
				type: 'month',
				range: true
			});
			
			 var date=new Date;
			 var year=date.getFullYear(); 
			 var month=date.getMonth()+1;
			 month =(month<10 ? "0"+month:month); 
			 var mydate = (year.toString()+"-"+month.toString())+" - "+(year.toString()+"-"+month.toString());
			 
			 $("#yearNmonth").val(mydate);
			 //showLoad();
			 //getData();
			
			
			form.render(null, 'test1'); //更新全部
			laypage.render({
			    elem: 'page'
			    ,count: count
			    ,theme: '#1E9FFF'
			});
			
			form.on('submit(button)', function(data) {
				pageNo = 1; //当点击搜索的时候，应该回到第一页
				showLoad();
				getData();
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});
			
			form.on('submit(exportExcel)', function(data) {
				window.location.href="export.do?ctrl=ahycx_exportData&"+$("#form1").serialize();
			});
			
		});
		
		
		var pageNo = 1;
		var pageSize = 10;
		var count=0;
		
		function getData() {	
			
			ajax({
				url : "ajax.do?ctrl=rksjgl_yskm&pageNo=" + pageNo+ "&pageSize=" + pageSize,
				data :{
					date: $("#yearNmonth").val()
				},
				type : 'post',
				dataType:"Json",
				success : function(obj) {
					if (obj != null && obj.data != null) {
						getTbale(obj.data);//拼接表格
						count = obj.count;//数据总条数					
						queryPage();
						console.log(obj.data);
						if(pageNo===1){
							
							showSuccess();
						}
						
					}
				}
			});
		}
		
		//初始化表格
		function getTbale(data) {
			var s = "";
			$.each(data, function(v, o) {
				s += '<tr>';
				s += '<td>' + (o.yskmdm == undefined ? "-" : o.yskmdm) + '</td>';
				s += '<td>' + (o.yskmmc == undefined ? "-" : o.yskmmc) + '</td>';
				s += '<td>' + (o.qkj == undefined ? "-" : o.qkj) + '</td>';
				s += '<td>' + (o.dfkj == undefined ? "-" : o.dfkj) + '</td>';
				s += '<td>' + (o.sj == undefined ? "-" : o.sj) + '</td></tr>';
			});
			$("#ttbody").html(s);
			//执行渲染
			layui.use([ 'table' ], function() {
				layui.table.init('user', {
					height : 480,
					limit : pageSize+2
				//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
				//支持所有基础参数
				});
			});
			if (data == null || data.length <= 0) {
				$("#page").hide();
			} else {
				$("#page").show();
			}
		}
		//分页方法
		function queryPage() {
			layui.use([ 'laypage' ], function() {
				laypage = layui.laypage;
				laypage.render({
					//注意，这里的page1 是 ID，不用加 # 号
					elem : 'page',
					//数据总数，从服务端得到
					count : count,
					//每页显示条数
					limit : 10,
					//条数列表
					limits : [10,20,30],
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