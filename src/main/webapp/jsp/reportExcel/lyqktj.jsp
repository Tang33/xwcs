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
<title>楼宇情况统计</title>
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
										<label class="layui-form-label">选择楼宇:</label>
										<div class="layui-input-inline">
											<select name="sortname" id="sortname" style="width: 40%;"
											id="sortname">
												<option value="NJYH">南京银行</option>
												<option value="DJGC">德基广场</option>
												<option value="ZXDS">中信大厦</option>
												<option value="KRJC">凯润金城</option>
												<option value="ZDGC">置地广场</option>
												<option value="XSJZX">新世界中心</option>
												<option value="ZJYH">珠江一号</option>
												<option value="DJDS">德基大厦</option>
												<option value="YZDS">邮储大厦</option>
												<option value="SRBDS">省人保大厦</option>
												<option value="MSYHDS">民生银行大厦</option>
												<option value="HSFDSWL">华山饭店商务楼</option>
												<option value="HTSBDS">宏图三胞大厦</option>
												<option value="HPDS">和平大厦</option>
												<option value="HRDS">华融大厦</option>
												<option value="SNDS">苏宁总部大楼</option>
											</select>
										</div>	
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
				window.location.href="lyqktj/export.do?"+$("#form1").serialize();
				
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
			          method : 'post',
			          url:"lyqktj/find.do",
			          where: {
			        	  date : $("#yearNmonth").val(),
			        	  ly : $("#sortname").val()
				       },
			          limit:10,
			          limits : [10,15,30,45],
			          page: true,//开启分页
			          cols: [[ //表头
			            {field: 'NSRMC', title: '纳税人名称',width:'15%'},
			            {field: 'ZCZB', title: '注册资本'},
			            {field: 'ZCDZ', title: '注册地址'},
			            {field: 'JYDZ', title: '经营地址'},
			            {field: 'KYSLRQ', title: '成立日期'},
			            {field: 'FDDBRXM', title: '法人代表'},
			            {field: 'FDDBRYDDH', title: '法人联系电话'},
			            {field: 'CWFZRXM', title: '财务负责人'},
			            {field: 'CWFZRYDDH', title: '财务负责人电话'},
			            {field: 'HY', title: '行业'},
			            {field: 'ZSE', title: '全口径税收'},
			            {field: 'DFZSE', title: '地方口径税收'}
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