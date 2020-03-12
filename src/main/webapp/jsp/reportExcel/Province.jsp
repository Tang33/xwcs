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
<title>前150户企业明细</title>
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<style type="text/css">
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
}
.label{
		    padding: 2px 5px;
		    background: #5FB878;
		    border-radius: 2px;
		    color: #fff;
		    display: block;
		    line-height: 20px;
		    height: 20px;
		    margin: 2px 5px 2px 0;
		    float: left;
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
									<div class="layui-col-md4" style="width: 18%;">
										<label class="layui-form-label">选择月份:</label>
										<div class="layui-input-inline">
											<input type="text" class="layui-input" id="chooseMonth"
												name="chooseMonth" placeholder="请选择日期">
										</div>
									</div>
									<div class="layui-col-md4" style="width: 8%;">
										<button class="layui-btn layui-btn-normal" id="addData"
											type="button" lay-submit="" lay-filter="addData">生成当月数据</button>
									</div>
									<div class="layui-col-md4" style="width: 20%;">
										<button class="layui-btn layui-btn-normal" id="clear"
											type="button" lay-submit="" lay-filter="clear">清除当月数据</button>
									</div>
									<div class="layui-col-md4" style="width: 18%;">
										<label class="layui-form-label">年月范围:</label>
										<div class="layui-input-inline">
											<input type="text" class="layui-input" id="yearNmonth"
												name="yearNmonth" placeholder="请选择日期">
										</div>
									</div>
									<div class="layui-col-md4">
										<button class="layui-btn layui-btn-normal" id="query"
											type="button" lay-submit="" lay-filter="query">查询</button>
										<button class="layui-btn layui-btn-normal" id="exportExcel"
											type="button" lay-submit="" lay-filter="exportExcel">导出</button>
									</div>
									
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
					<thead>
						<tr>
							<th lay-data="{field:'ID',width:'5%',align:'right'}">序号</th>
						    <th lay-data="{field:'NSRMC',width:'20%',align:'right'}">纳税人名称</th>
						    <th lay-data="{field:'DFZSE',align:'right'}">地方口径</th>
						    <th lay-data="{field:'HJ',align:'right'}">合计</th>
						    <th lay-data="{field:'ZZS',align:'right'}">增值税</th>
							<th lay-data="{field:'YYS',align:'right'}">营业税</th>
							<th lay-data="{field:'QYSDS',align:'right'}">企业所得税</th>
							<th lay-data="{field:'GRSDS',align:'right'}">个人所得税</th>
							<th lay-data="{field:'CSWHJSS',align:'right'}">城市维护建设税</th>
							<th lay-data="{field:'CZTDSYS',align:'right'}">城镇土地使用税</th>	
							<th lay-data="{field:'YHS',align:'right'}">印花税</th>	
							<th lay-data="{field:'FCS',align:'right'}">房产税</th>	
							<th lay-data="{field:'CCS',align:'right'}">车船税</th>	
							<th lay-data="{field:'HBS',align:'right'}">环保税</th>
						</tr>
					</thead>
					<tbody id="ttbody">

					</tbody>	
				</table>
				<div id="page" ></div>
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
						$("#chooseMonth").val(rr);
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
			//年月范围
			laydate.render({
				elem: '#chooseMonth',
				type: 'month'
			});
			form.render(null, 'test1'); //更新全部
			
			form.on('submit(query)', function(data) {
				pageNo = 1; //当点击搜索的时候，应该回到第一页
				getData();
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});
			form.on('submit(exportExcel)', function(data) {
				wait = layer.load();
				window.location.href="Province/export.do?"+$("#form1").serialize();
				layer.close(wait);
				
			});
			
			form.on('submit(addData)', function(data) {
				addData();
			});
			form.on('submit(clear)', function(data) {
				clear();
			});
					
		});
	
		
		var pageNo = 1;
		var pageSize = 15;
		var count=276;
	
		function getData() {	
			wait = layer.load();
			ajax({
				url : "Province/queryData.do?pageNo=" + pageNo + "&pageSize="+ pageSize,
				page:true,
				data :{			
					date: $("#yearNmonth").val(),
				},
				type : 'post',
				dataType:"Json",
				contentType : "application/json",  
				success : function(obj) {
					layer.close(wait);
					if (obj != null && obj.data != null) {
						getTbale(obj.data);//拼接表格				
						queryPage();
						showSuccess();
					}
				}
			});
		}
		
		function clear() {
			layer.confirm('', {btn: ['是','否']
				,content: '确认清除该月数据？'
           }, function(index){ 
        	   ajax({
	   				url : "Province/deleteData.do",
	   				page:true,
	   				data :{			
	   					date: $("#chooseMonth").val(),
	   				},
	   				type : 'post',
	   				dataType:"Json",
	   				contentType : "application/json",  
	   				success : function(obj) {
	   					layer.close(wait);
	   					console.log(obj);
	   					if (obj.code == "001") {
	   						layer.alert("数据清除完成！");	
	   					} else if (obj.code == "002") {
	   						layer.alert("数据清除失败！");	
	   					} else {
	   						layer.alert("不存在该月份数据！");	
	   					}
	   				}
   				});
           });
		}
		
		function addData() {	
			wait = layer.load();
			ajax({
				url : "Province/addData.do",
				page:true,
				data :{			
					date: $("#chooseMonth").val(),
				},
				type : 'post',
				dataType:"Json",
				contentType : "application/json",  
				success : function(obj) {
					layer.close(wait);
					console.log(obj);
					if (obj.code == "000") {
						layer.alert("数据生成完成！");	
					} else if (obj.code == "001") {
						layer.alert("该月份数据未加工！");	
					} else if (obj.code == "007") {
						layer.alert("已存在该月份数据！");	
					} else {
						layer.alert("查询异常！");	
					}
				}
			});
		}
		
		//初始化表格
		function getTbale(data) {
			var s = "";
			$.each(data, function(v, o) {
				s += '<tr>';
				s += '<td>' + o.ID + '</td>';
				s += '<td>' + o.NSRMC + '</td>';
				s += '<td>' + o.DFZSE + '</td>';
				s += '<td>' + o.HJ + '</td>';
				s += '<td>' + o.ZZS + '</td>';
				s += '<td>' + o.YYS + '</td>';
				s += '<td>' + o.QYSDS + '</td>';
				s += '<td>' + o.GRSDS + '</td>';
				s += '<td>' + o.CSWHJSS + '</td>';
				s += '<td>' + o.CZTDSYS + '</td>';
				s += '<td>' + o.YHS + '</td>';
				s += '<td>' + o.FCS + '</td>';
				s += '<td>' + o.CCS + '</td>';
				s += '<td>' + o.HBS + '</td>';
				s += '</tr>';
			});
			$("#ttbody").html(s);
			//执行渲染
			layui.use([ 'table' ], function() {
				layui.table.init('user', {
					cellMinWidth: 120, 
					limit : pageSize+2
				//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
				//支持所有基础参数
				});
			});
			/*  if (data == null || data.length <= 0) {
				$("#page").hide();
			} else {
				$("#page").show();
			}    */
			
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
					limit : pageSize,
					//条数列表
					
					limits : [15,30,45],
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