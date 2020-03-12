<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
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
<title>分国、地税查询</title>
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
						<div class="layui-colla-content  layui-show">
							<div class="layui-row">
								<div class="layui-col-md12">
									<div class="layui-row layui-col-space10">
										<div class="layui-col-md6">
											<div class="layui-inline">
												<label class="layui-form-label">年月范围:</label>
												<div class="layui-input-inline">
											        <input type="text" class="layui-input" id="yearNmonth" name placeholder="请选择日期">
											    </div>
											</div>
										</div>
										<div class="layui-col-md6">
											<div class="layui-inline">
												<div class="layui-btn-group">
													<button class="layui-btn layui-btn-normal" id="button" type="button" lay-submit="" lay-filter="button">查 询</button>
												</div>
												<div class="layui-btn-group">
													<button class="layui-btn layui-btn-normal" id="exportExcel" type="button" lay-submit="" lay-filter="exportExcel">导出Execl</button>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
	
				<!-- <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
					<legend style="font-size:12px;">单位：万元</legend>
	
					<table class="layui-table" id="table" lay-filter="user">
						<thead>
							<tr>
								<th style="text-align:center" lay-data="{field:'ZZS'}">增值税(国税)</th>
								<th style="text-align:center" lay-data="{field:'QYSDS_GS'}">企业所得税（国税）</th>
								<th style="text-align:center" lay-data="{field:'HJ_GS'}">合计（国税）</th>
								<th style="text-align:center" lay-data="{field:'YYS'}">营业税（地税）</th>
								<th style="text-align:center" lay-data="{field:'QYSDS_DS'}">企业所得税（地税）</th>
								<th style="text-align:center" lay-data="{field:'GRSDS'}">个人所得税（地税）</th>
								<th style="text-align:center" lay-data="{field:'FCS'}">房产税（地税）</th>
								<th style="text-align:center" lay-data="{field:'YHS'}">印花税（地税）</th>
								<th style="text-align:center" lay-data="{field:'CCS'}">车船税（地税）</th>
								<th style="text-align:center" lay-data="{field:'HJ_DS'}">合计（地税）</th>
								<th style="text-align:center" lay-data="{field:'HJ'}">合计（总）</th>
							</tr>
						</thead>
						<tbody id="ttbody">
						
						</tbody>
					</table>
					<div id="page"></div>
	
				</fieldset> -->
				
				<!-- 表和分页 -->
				<table id="demo" lay-filter="test"></table>
	
			</div>
		</form>
	</body>
	<script type="text/javascript">
		$(function() {
			$("#table").attr("lay-data","{width:"+document.body.clientWidth+"}");
		}); 
	
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
			 getData1();
			
			
			/* form.render(null, 'test1'); //更新全部
			laypage.render({
			    elem: 'page'
			    ,count: 100
			    ,theme: '#1E9FFF'
			}); */
			
			form.on('submit(button)', function(data) {
				pageNo = 1; //当点击搜索的时候，应该回到第一页
				getData1();
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});
			 
			form.on('submit(exportExcel)', function(data) {
				var date= $("#yearNmonth").val();
				window.location.href="export.do?ctrl=fgdscx_exportData&date="+date;
			});		
		});
		

		var pageNo = 1;
		var pageSize = 10;
		var count = 0;
		
		function getData1(){
			
			layui.use(['table'], function() {
				table = layui.table;
			
			var date= $("#yearNmonth").val();
			 table.render({
				    elem: '#demo'
				    ,height: 480
				    ,url: "ajax.do?ctrl=fgdscx_queryData&pageNo=" + pageNo+ "&pageSize=" + pageSize+"&date="+date+"&"+$("#form1").serialize() //数据接口
				    ,page: true //开启分页
				    ,parseData:function(res){
				    	console.log(res);
				    	return{
				    		"code":000,//数据状态
				    		"msg":"",
				    		"count":1000,//数据总数
				    		data:res.data
				    	};
				    }
				    ,cols: [[ //表头
				      {field: 'ZZS', title: '增值税(国税)', width:'10%'}
				      ,{field: 'QYSDS_GS', title: '企业所得税（国税）', width:'8%'}
				      ,{field: 'HJ_GS', title: '合计（国税）', width:'8%'}
				      ,{field: 'YYS', title: '营业税（地税）', width:'8%'} 
				      ,{field: 'QYSDS_DS', title: '企业所得税（地税）', width:'10%'}
				      ,{field: 'GRSDS', title: '个人所得税（地税）', width:'10%'}
				      ,{field: 'FCS', title: '房产税（地税）', width:'8%'}
				      ,{field: 'YHS', title: '印花税（地税）', width:'8%'}
				      ,{field: 'CCS', title: '车船税（地税）', width:'10%'}
				      ,{field: 'HJ_DS', title: '合计（地税）', width:'10%'}
				      ,{field: 'HJ', title: '合计（总）', width:'10%'}
				      
				    ]]
				    
				  });
			});
		}
		
		/* function getData() {
			var date=$("#yearNmonth").val();
			
			ajax({
				url : "ajax.do?ctrl=fgdscx_queryData&pageNo=" + pageNo+ "&pageSize=" + pageSize+"&date="+date,
				data : $("#form1").serialize(),
				type : 'post',
				dataType:"Json",
				success : function(obj) {
					if (obj != null && obj.data != null) {
						console.log(obj);
						getTbale(obj.data);//拼接表格
						count = obj.count;//数据总条数					
						queryPage();
						console.log(obj.data);
					}
				}
			});
		}

		//初始化表格
		function getTbale(data) {
			var s = "";
			$.each(data, function(v, o) {
				 [{QYSDS_GS=129617, HJ_GS=180263, ZZS=50646, YYS=0, CCS=0, HJ=180263, QYSDS_DS=0, YHS=0, HJ_DS=0, FCS=0, GRSDS=0}]
				
				s += '<tr>';
				s += '<td>' + o.ZZS + '</td>';
				s += '<td>' + o.QYSDS_GS + '</td>';
				s += '<td>' + o.HJ_GS + '</td>';
				s += '<td>' + o.YYS + '</td>';
				s += '<td>' + o.QYSDS_DS + '</td>';
				s += '<td>' + o.GRSDS + '</td>';
				s += '<td>' + o.FCS + '</td>';
				s += '<td>' + o.YHS + '</td>';
				s += '<td>' + o.CCS + '</td>';
				s += '<td>' + o.HJ_DS + '</td>';
				s += '<td>' + o.HJ + '</td>';
				s += '</tr>';
			});
			$("#ttbody").html(s);
			//执行渲染
			layui.use([ 'table' ], function() {
				layui.table.init('user', {
					height : 480,
					limit : pageSize
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

		function queryPage() {
			layui.use([ 'laypage' ], function() {
				laypage = layui.laypage;
				laypage.render({
					//注意，这里的page1 是 ID，不用加 # 号
					elem : 'page1',
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
		}*/
		
		
	</script>
</html>