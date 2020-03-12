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
<title>按行业查询</title>
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
										<label class="layui-form-label">行业:</label>
										<div class="layui-input-block">
											<select name="hylist" lay-filter="hylist" id="hylist">
												<option value="%">请选择</option>
											</select>
										</div>
									</div>



									<div class="layui-inline">
										<label class="layui-form-label">统计口径:</label>
										<div class="layui-input-block">
											<select name="tjkj" style="width: 30%;" id="tjkj"
												lay-filter="tjkj">
												<option value="0">全口径</option>
												<option value="1">归属地方</option>
											</select>
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
					<thead>
						<tr>
							<!-- 	<th lay-data="{field:'HY_MC'}">行业名称</th> -->
							<th lay-data="{field:'NSRMC',width:'18%',fixed: 'left'}">企业名称</th>
							<th lay-data="{field:'ZZS',width:'9%',align:'right'}">增值税</th>
							<th lay-data="{field:'YGZZZS',width:'9%',align:'right'}">营改增"增值税</th>
							<th lay-data="{field:'YYS',width:'9%',align:'right'}">营业税</th>
							<th lay-data="{field:'QYSDS_GS',width:'9%',align:'right'}">企业所得税</th>
							<!-- 	<th lay-data="{field:'QYSDS'}">企业所得税</th> -->
							<th lay-data="{field:'GRSDS',width:'9%',align:'right'}">个人所得税</th>
							<th lay-data="{field:'FCS',width:'9%',align:'right'}">房产税</th>
							<th lay-data="{field:'YHS',width:'9%',align:'right'}">印花税</th>
							<th lay-data="{field:'CCS',width:'9%',align:'right'}">车船税</th>
							<th lay-data="{field:'DFJYFJ',width:'9%',align:'right'}">地方教育附加</th>
							<th lay-data="{field:'JYFJ',width:'9%',align:'right'}">教育费附加</th>
							<th lay-data="{field:'CSWHJSS',width:'9%',align:'right'}">城市维护建设税</th>
							<th lay-data="{field:'CZTDSYS',width:'9%',align:'right'}">城镇土地使用税</th>
							<th lay-data="{field:'HBS',width:'9%',align:'right'}">环保税</th>
							<th lay-data="{field:'HJ',width:'9%',align:'right',fixed: 'right'}">合计</th>
							<th lay-data="{field:'QNTQHJS',width:'9%',align:'right',fixed: 'right'}">去年同期合计数</th>
							<th lay-data="{field:'HJTB',width:'6%',align:'right',fixed: 'right'}">合计同比</th>
							<th lay-data="{field:'ZJE',width:'9%',align:'right',fixed: 'right'}">增减额</th>
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
			
			/* laypage.render({
			    elem: 'page'
			    ,count: count
			    ,theme: '#1E9FFF'
			}); */
			
			form.on('submit(button)', function(data) {
				pageNo = 1; //当点击搜索的时候，应该回到第一页
				getData();
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});
			form.on('submit(exportExcel)', function(data) {
				wait = layer.load();
				window.location.href="ahycx/export.do?"+$("#form1").serialize();
				layer.close(wait);
				
			});
			
			ajax({
				url:"ahycx/queryInit.do",
				data:{},
				type:'post',
				contentType : "application/json",  
				success:function(obj){
				//	aui.alert(obj);
					if(obj!=null&&obj.data!=null){
						var str="<option value='%'>请选择</option>";						
						var hylist=obj.data.hylist;
						//拼接行业下拉选框 
						str="<option value='%'>请选择</option>";
						for (var i = 0; i < hylist.length; i++) {
							str+="<option value='"+hylist[i].HYML_DM+"'>"+hylist[i].HYML_MC+"</option>";
						}
						$("#hylist").html(str);
						form.render('select');
						
					}
				}
			});			
		});

		
		
		var pageNo = 1;
		var pageSize = 10;
		var count=0;
	
		function getData() {	
			wait = layer.load();
			ajax({
				url : "ahycx/queryData.do?pageNo=" + pageNo+ "&pageSize=" + pageSize,
				page:true,
				data :{
					
					date: $("#yearNmonth").val(),
					hylist: $("#hylist").val(),
					tjkj: $("#tjkj").val(),
					countTotal:$("#myCount").val(),
				},
				type : 'post',
				dataType:"Json",
				contentType : "application/json",  
				success : function(obj) {
					layer.close(wait);
					if (obj != null && obj.data != null) {
						${sessionScope.user.JD_DM }
						getTbale(obj.data);//拼接表格
						count = obj.count;//数据总条数					
						queryPage();
						console.log(obj.data);
						showSuccess();
					}
				}
			});
		}

		//初始化表格
		function getTbale(data) {
			var s = "";
			$.each(data, function(v, o) {
			/* 	
				[{QYSDS_GS=154.86, CCS1=0, HJ=154.86, CSWHJSS=0, CZTDSYS1=0, ZZS=0, 
					DFJYFJ=0, ZZS1=0, GRSDS=0, HBS=0, HBS1=0, FCS1=0, QYSDS_GS1=0, NSRSBH= , 
					QYSDS_DS1=0, RK_RQ= , HY_MC=null, YHS1=0, QYSDS=154.86, QYSDS_DS=0,
					YHS=0, GRSDS1=0, HJ1=0, NSRMC=鑫叶同创机电设备（北京）有限公司, JYFJ=0, YYS=0,
					CCS=0, QYSDS1=0, CZTDSYS=0, YGZZZS=0, FCS=0, YYS1=0, DSGLM= },  */
				s += '<tr>';
				s += '<td>' + o.NSRMC + '</td>';
				s += '<td>' + o.ZZS + '</td>';
				s += '<td>' + o.YGZZZS + '</td>';
				s += '<td>' + o.YYS + '</td>';
			 	s += '<td>' + o.QYSDS + '</td>';
				/* s += '<td>' + o.QYSDS + '</td>'; */
				s += '<td>' + o.GRSDS + '</td>';
				s += '<td>' + o.FCS + '</td>';
				s += '<td>' + o.YHS + '</td>';
				s += '<td>' + o.CCS + '</td>';
				s += '<td>' + o.DFJYFJ + '</td>';
				s += '<td>' + o.JYFJ + '</td>';
				s += '<td>' + o.CSWHJSS + '</td>';
				s += '<td>' + o.CZTDSYS + '</td>';
				s += '<td>' + o.HBS + '</td>';
				s += '<td>' + o.HJ + '</td>';
				s += '<td>' + o.HJ1 + '</td>';//去年合计数
				s += '<td>' + (((o.HJ/o.HJ1)*100 == -Infinity || Infinity  ? "-" :((o.HJ/o.HJ1)*100)))+ '</td>';
				s += '<td>' + (o.HJ-o.HJ1) + '</td>';
				s += '</tr>';
			});
			$("#ttbody").html(s);
			//执行渲染
			layui.use([ 'table' ], function() {
				layui.table.init('user', {
					cellMinWidth: 120, 
					
					height : 580,
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