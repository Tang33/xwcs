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
<title>三大税种</title>
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
									<div class="layui-inline">
										<label class="layui-form-label">年月范围:</label>
										<div class="layui-input-inline">
											<input type="text" class="layui-input" id="yearNmonth"
												name="yearNmonth" placeholder="请选择日期">
										</div>
									</div>
									<div class="layui-inline">
									</div>
									<button class="layui-btn layui-btn-normal" id="button"
										type="button" lay-submit="" lay-filter="button">查 询</button>
									<button class="layui-btn layui-btn-normal" id="exportExcel"
										type="button" lay-submit="" lay-filter="exportExcel">导出</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend style="font-size: 12px;">单位：元 </legend>

				<table class="layui-table" id="table" lay-filter="user">
					
					<tbody id="ttbody">

					</tbody>
					
				</table>
				<!-- <div id="page" ></div> -->
			</fieldset>
		</div>
	</form>
</body>

<script>
		var wait;
		var width=document.body.clientWidth-20
		$(function() {
			$("#table").attr("lay-data",
					"{width:" + width + "}");
		});
		function showLoad() {
		    return layer.msg('拼命加载数据中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:100000});
		}
		
		function closeLoad(index) {
		    layer.close(index);
		}
		function showSuccess() {
		    layer.msg('查询成功！',{time: 1000,offset: 'auto'});
		}
		
		//查询数据库的最大月份
		getMaxData();
		function getMaxData() {
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				url : "yskmcx/getNsDates.do",
				data : "",
				dataType : "json",
				contentType : "application/json",  
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

			form.render(null, 'test1'); //更新全部
			
			form.on('submit(button)', function(data) {
				pageNo = 1; //当点击搜索的时候，应该回到第一页
				getData();
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});
			form.on('submit(exportExcel)', function(data) {
				wait = layer.load();
				window.location.href="Sdsz/export.do?"+$("#form1").serialize();
				
				layer.close(wait);
				
			});

		});

		
		
		var pageNo = 1;
		var pageSize = 10;
		var count=0;
	
		function getData() {
			layui.use('table', function(){
				loadlayer = layer.load();
				var table = layui.table;
				 table.render({
			          elem: '#table',
			          width:document.body.clientWidth,
			          height: 610,
			          method : 'post',
			          url:"Sdsz/findSdsz.do",
			          where: {
			        	  date : $("#yearNmonth").val()
				       },
			          /* limit:15,
			          limits : [15,30,45],
			          page: true,//开启分页 */
			          cols: [[ //表头
			            {field: 'SRQ', title: '月份', width: '5%', align: 'center'},
			            {field: 'JNZZS', title: '增值税当月收入', width: '10%', align: 'center'},
			            {field: 'SSZZS', title: '增值税省属税源', width: '10%', align: 'center'},
			            {field: 'QNZZS', title: '增值税去年同期', width: '10%', align: 'center'},
			            {field: 'STB', title: '增值税同比增幅%', width: '10%', align: 'center'},
			            {field: 'SZB', title: '增值税直比增幅%', width: '10%', align: 'center'},
			            {field: 'JNQYSDS', title: '企业所得税当月收入', width: '10%', align: 'center'},
			            {field: 'SSQYSDS', title: '企业所得税省属税源', width: '10%', align: 'center'},
			            {field: 'QNQYSDS', title: '企业所得税去年同期', width: '10%', align: 'center'},
			            {field: 'QTB', title: '企业所得税同比增幅%', width: '10%', align: 'center'},
			            {field: 'QZB', title: '企业所得税直比增幅%', width: '10%', align: 'center'},
			            {field: 'JNGRSDS', title: '个人所得税当月收入', width: '10%', align: 'center'},
			            {field: 'SSGRSDS', title: '个人所得税省属税源', width: '10%', align: 'center'},
			            {field: 'QNGRSDS', title: '个人所得税去年同期', width: '10%', align: 'center'},
			            {field: 'GTB', title: '个人所得税同比增幅%', width: '10%', align: 'center'},
			            {field: 'GZB', title: '个人所得税直比增幅%', width: '10%', align: 'center'}
			          ]],
			          done : function(res, curr, count){
			        	layer.close(loadlayer);
		     		 	layer.msg('查询成功！', {
		     				time : 1000,
		     				offset : 'auto'
		     			});
		       		}
			    });
			})		        
		}
	</script>
</html>