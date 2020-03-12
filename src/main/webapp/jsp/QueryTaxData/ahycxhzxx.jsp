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
<title>按行业汇总查询</title>
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
									
										<div class="layui-inline">
											<label class="layui-form-label">年月范围:</label>
											<div class="layui-input-inline">
												<input type="text" class="layui-input" id="yearNmonth" name="yearNmonth" placeholder="请选择日期">
											</div>
										</div>


										<div class="layui-inline">
											<label class="layui-form-label">统计口径:</label>
											<div class="layui-input-block">
												<select name="tjkj" style="width: 30%;" id="tjkj">
													<option value="0">全口径</option>
													<option value="1">归属地方</option>
												</select>
											</div>
										</div>
										
										
										<div class="layui-inline">
											<label class="layui-form-label">街道:</label>
											<div class="layui-input-block">
												<select name="jdlist" lay-filter="jdlist" id="jdlist">
													<option value="%">请选择</option>
												</select>
											</div>
										</div>
										
										<div class="layui-inline">
										</div>
																					
											
												<button class="layui-btn layui-btn-normal" id="button"type="button" lay-submit="" lay-filter="button">查
													询</button>
												<button class="layui-btn layui-btn-normal" id="exportExcel"type="button" lay-submit="" lay-filter="exportExcel">
												导出</button>
										
										
																								
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 10px; margin-bottom: 0px;">
				<legend style="font-size: 12px;">单位：元</legend>

				<table class="layui-table" id="table" lay-filter="user" style="margin: 0px">
					
				</table>
				
			</fieldset>
		</div>
	</form>
		
	
</body>

<script>
		var wait;
		var dwid=$("#dwid").val();
		$(function() {
			//$("#table").attr("lay-data","{width:" + document.body.clientWidth + "}");
			getMaxData();
			setTimeout("getData()", 2000);
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
		
		function getMaxData() {
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				url : "ahycxhzxx/getNsDate.do",
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
		
		layui.use(['form',  'laydate','laypage'], function() {
			
			var form = layui.form,
				layer = layui.layer,
				laydate = layui.laydate,
				laypage=layui.laypage;
			//年月范围
			laydate.render({
				elem: '#yearNmonth',
				type: 'month',
				range: true
			});

			form.render(null, 'test1'); //更新全部
			laypage.render({
			    elem: 'page'
			    ,count: count
			    ,theme: '#1E9FFF'
			});
			
			form.on('submit(button)', function(data) {
				pageNo = 1; //当点击搜索的时候，应该回到第一页
				getData();
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});
			
			form.on('submit(exportExcel)', function(data) {
				window.location.href="ahycxhzxx/export.do?"+$("#form1").serialize();
			});
			
			ajax({
				url : "aqycx/aqycx_queryInit.do",
				data : {},
				type : 'post',
				success : function(obj) {
					if (obj != null && obj.data != null) {
						//拼接街道 下拉选框
						var str = "<option value='%'>请选择</option>";
						var jdlist = obj.data.jdlist;
						if (dwid == "00") {
							for (var i = 0; i < jdlist.length; i++) {
								str += "<option value='"+jdlist[i].JD_DM+"'>" + jdlist[i].JD_MC + "</option>";
							}
						} else {
							for (var i = 0; i < jdlist.length; i++) {
								str = "<option value='"+jdlist[i].JD_DM+"'>" + jdlist[i].JD_MC + "</option>";
							}
						}
						
						$("#jdlist").html(str);
						form.render('select');
					}
				}
			});

		})
		
		var pageNo = 1;
		var pageSize = 10;	
		var count=0;
		function getData() {	
			loadlayer = layer.load();
			layui.use(['table'], function() {
				layui.table.render({
					elem: '#table',
					height: 640,
					width: document.body.clientWidth,
					method: 'post',
					url: "ahycxhzxx/queryData.do",
					where: {
						date: $("#yearNmonth").val(),
						jd : $("#jdlist").val(),
						tjkj: $("#tjkj").val()
					},
					cols: [[
						{field: 'HY_MC',title: '行业名称',width: '15%',fixed: 'left'},
						{field: 'ZZS',title: '增值税',width: '9%',align: 'right'},
						{field: 'YGZZZS',title: '"营改增"增值税',width: '9%9%',align: 'right'},
						{field: 'YYS',title: '营业税',width: '9%',align: 'right'},
						{field: 'QYSDS_GS',title: '企业所得税',width: '9%',align: 'right'},
						{field: 'GRSDS',title: '个人所得税',width: '9%',align: 'right'},
						{field: 'FCS',title: '房产税',width: '9%',align: 'right'},
						{field: 'YHS',title: '印花税',width: '9%',align: 'right'},
						{field: 'CCS',title: '车船税',width: '9%',align: 'right'},
						{field: 'CSWHJSS',title: '城市维护建设税',width: '9%',align: 'right'},
						{field: 'DFJYFJ',title: '地方教育附加',width: '9%',align: 'right'},
						{field: 'JYFJ',title: '教育附加',width: '9%',align: 'right'},
						{field: 'HJ',title: '合计',width: '9%',align: 'right'},
						{field: 'QN_ZZS',title: '增值税(上年)',width: '15%',align: 'right'},
						{field: 'QN_YGZZZS',title: '"营改增"增值税(上年)',width: '15%',align: 'right'},
						{field: 'QN_YYS',title: '营业税(上年)',width: '15%',align: 'right'},
						{field: 'QN_QYSDS_GS',title: '企业所得税(上年)',width: '15%',align: 'right'},
						{field: 'QN_GRSDS',title: '个人所得税(上年)',width: '15%',align: 'right'},
						{field: 'QN_FCS',title: '房产税(上年)',width: '15%',align: 'right'},
						{field: 'QN_YHS',title: '印花税(上年)',width: '15%',align: 'right'},
						{field: 'QN_CCS',title: '车船税(上年)',width: '15%',align: 'right'},
						{field: 'QN_CSWHJSS',title: '城市维护建设税(上年)',width: '15%',align: 'right'},
						{field: 'QN_DFJYFJ',title: '地方教育附加(上年)',width: '15%',align: 'right'},
						{field: 'QN_JYFJ',title: '教育费附加(上年)',width: '15%',align: 'right'},
						{field: 'QN_HJ',title: '合计(上年)',width: '15%',align: 'right'},
						{field: 'TB',title: '合计同比',width: '15%',align: 'right'},
						{field: 'ZJE',title: '增减额',width: '15%',align: 'right'}
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
	</script>

</html>