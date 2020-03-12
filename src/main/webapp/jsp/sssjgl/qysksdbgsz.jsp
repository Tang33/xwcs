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
<title>Insert title here</title>
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
	<div class="layui-form" lay-filter="test1">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md12">
							<div class="layui-row layui-col-space10">
								<div class="layui-col-md2">
									<label class="layui-form-label">企业名称:</label>
									<div class="layui-input-block">
										<input type="text" class="layui-input" id="qymc" name="qymc"
											placeholder="">
									</div>
								</div>
								<div class="layui-col-md2">
									<label class="layui-form-label">月份:</label>
									<div class="layui-input-block">
										<input type="text" class="layui-input" name="cxyf" id="test8"
											placeholder="">
									</div>
								</div>
								<div class="layui-col-md2">
									<div class="layui-form-item">
										<label class="layui-form-label">税种:</label>
										<div class="layui-input-block">
											<select name="zsxm_dm" id="zsxm_dm" style="width: 30%;">
												<option value="">全部</option>
												<option value="增值税">增值税</option>
												<option value="营改增增值税">营改增增值税</option>
												<option value="营业税">营业税</option>
												<option value="企业所得税">企业所得税</option>
												<option value="个人所得税">个人所得税</option>
												<option value="车船税">车船税</option>
												<option value="房产税">房产税</option>
												<option value="印花税">印花税</option>
												<option value="城市维建设税">城市维建设税</option>
												<option value="地方教育附加">地方教育附加</option>
												<option value="教育附加">教育附加</option>
												<option value="环保税">环保税</option>
											</select>
										</div>
									</div>
								</div>
								<div class="layui-col-md2">
									<div class="layui-form-item">
										<label class="layui-form-label">街道:</label>
										<div class="layui-input-block">
											<select name="jdlist" lay-filter="aihao" id="jdlist">
											</select>
										</div>
									</div>
								</div>
								<div class="layui-col-md1" style="text-align: center;">
									<div class="layui-inline">
										<div class="layui-btn-group">
											<button class="layui-btn layui-btn-normal" id="button"
												type="button" lay-submit="" lay-filter="button">查 询</button>
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
			<legend style="font-size: 12px;">单位：万元</legend>

			<table class="layui-table" id="table" lay-filter="user">

			</table>
			<script type="text/html" id="bar">
{{# if(d.QXJ=="1"){ }}
  				<a class="layui-btn layui-btn-xs" lay-event="hf">恢复原始记录</a>
				{{# } }}
{{# if(d.QXJ!="1"){ }}
  				<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="cf">选择拆分</a>
				{{# } }}
			</script>
			<script type="text/html" id="bg">
{{# if(d.QXJ=="1"){ }}
  				已变更
				{{# } }}
{{# if(d.QXJ!="1"){ }}
  				未变更
				{{# } }}
			</script>

			<table class="layui-table" id="selecttable" hidden="hidden" lay-filter="selecttable">
			<thead>
						<tr>
							<th lay-data="{field:'qy',width:320}">企业名称</th>
							<th lay-data="{field:'sz',width:120}">税种</th>
							<th lay-data="{field:'jd',width:120}">街道</th>
							<th lay-data="{field:'ze',width:120}">总税源</th>
						</tr>
					</thead>
					<tbody id="ttbody">
						
					</tbody>
			</table>
			<form class="layui-form" id="form" action="">
			<table class="layui-table" id="inserttable" hidden="hidden" lay-filter="inserttable">
			<thead>
						<tr>
							<th style="text-align:center" lay-data="{field:'qy'}">税种名称</th>
							<th style="text-align:center" lay-data="{field:'jd'}">拆分金额</th>
							<th style="text-align:center" lay-data="{field:'ze'}">操作</th>
						</tr>
					</thead>
					<tbody id="insertbody">
						
					</tbody>
			</table>
			</form>
		</fieldset>

	</div>
	<script>
	var jdlist;
	var zsee=0;
	var jd="";
	var sz="";
	var nsrmc;
	
	//查询数据库的最大月份
	getMaxData();
	function getMaxData() {
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url : "ajax.do?ctrl=Sjdr_getNsDate",
			data : "",
			dataType : "json",
			success : function(obj) {
				if (obj.code == "000") {
					var r = obj.data[0];
					//日期条件默认值为数据库中数据的最大入库日期
					var year = r.RKRQ.substring(0,4);
					var month = r.RKRQ.substring(4,6);
					var rr = year + "-" + month;
					$("#test8").val(rr);
				}
			}
		})
	}
	
		layui.use([ 'form', 'layedit', 'laydate' ],
						function() {

							var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate
							//年月范围
							laydate.render({
								elem : '#test8',
								type : 'month',
								format : 'yyyyMM'
							});
							
							form.render(null, 'test1'); //更新全部
							ajax({
								url : "ajax.do?ctrl=qysksdbgsz_queryInit",
								data : {},
								type : 'post',
								success : function(obj) {
									if (obj != null && obj.data != null) {
										var str = "<option value=''>请选择</option>";
										jdlist = obj.data.jdlist;
										var hylist = obj.data.hylist;
										for (var i = 0; i < jdlist.length; i++) {
											str += "<option value='"+jdlist[i].JD_DM+"'>"
													+ jdlist[i].JD_MC
													+ "</option>";
										}
										$("#jdlist").html(str);
										//getData();
										form.render('select');
									}
								}
							});

							form.on('submit(button)', function() {
								getData();
								return false;
							});
							form.on('submit(add)', function() {
								var s="";
								 s += '<tr>';
						         
						           var str = "<option value=''>请选择</option>";									
									str+='<option value="">全部</option>';
									str+='<option value="增值税">增值税</option>';
									str+='<option value="营改增增值税">营改增增值税</option>';
									str+='<option value="营业税">营业税</option>';
									str+='<option value="企业所得税">企业所得税</option>';
									str+='<option value="个人所得税">个人所得税</option>';
									str+='<option value="车船税">车船税</option>';
									str+='<option value="房产税">房产税</option>';
									str+='<option value="印花税">印花税</option>';
									str+='<option value="城市维建设税">城市维建设税</option>';
									str+='<option value="地方教育附加">地方教育附加</option>';
									str+='<option value="教育附加">教育附加</option>';
									str+='<option value="环保税">环保税</option>';
									
						           s += '<td><select name="szdm" id="szdm">'+str+'</select></td>';
						           s += '<td><input type="text"  class="layui-input" id="zseadd" name="zseadd" placeholder="请输入总税额"  ></td>';
						           s += '<td><button class="layui-btn layui-btn-danger layui-btn-xs" id="del" type="button" lay-submit="" lay-filter="del">删除</button></td>';
						           s += '</tr>';
						           $("#insertbody").append(s);
						           form.render();
								return false;
							});
							form.on('submit(del)', function(obj) {
								console.log(obj);
								$(obj.elem.parentNode.parentNode).remove();
								return false;
							});
							form.on('submit(save)', function() {
								console.log($("#form").serializeArray());
								var result=$("#form").serializeArray();
								var arraystr="[";
								var sum=0;
								for (var i = 0; i < result.length; i++) {
								
								if (result[i].name=="szdm") {
									arraystr+="{szdm:\""+result[i].value+"\",";
								}
								if (result[i].name=="zseadd") {
									arraystr+="zse:\""+result[i].value+"\"},";
									sum+=parseFloat(result[i].value)
								}	
								}
								sum=Math.floor(sum * 100) / 100;
								if (zsee!=sum) {
									layer.msg("拆分的总税额有误！");
									return;
								}
								arraystr=arraystr.substring(0,arraystr.length-1);
								arraystr+="]";
								var cxrq = $("#test8").val();
								ajax({
									url : "ajax.do?ctrl=qysksdbgsz_CF",
									data : {
									str:arraystr,
									jd:jd,
									sz:sz,
									nsrmc:nsrmc,
									cxrq:cxrq
									},
									type : 'post',
									success : function(obj) {
										console.log(obj);
										if (obj != null && obj.data != null) {
											var sjList = obj.data;
											getTbale(sjList);
										}
										if(obj != null && obj.code == "000"){
											getData();
										}
									}
								});
								console.log(arraystr);
								return false;
							});
						});

		
		// 恢复
		function gethf(sz,narmc,jd){
			console.log("-----------------");
			var cxrq = $("#test8").val();
			ajax({
				url : "ajax.do?ctrl=qysksdbgsz_HF",
				data : {
				sz:sz,
				jd:jd,
				nsrmc:nsrmc,
				cxrq:cxrq
				},
				type : 'post',
				success : function(obj) {
					console.log(obj);
					if (obj != null && obj.data != null) {
						var sjList = obj.data;
						console.log("拆分后刷新页面");
						getTbale(sjList);
					}
					if(obj != null && obj.code == "000"){
						getData();
					}
				}
			});
		}
		
		var wait;
		//分页参数设置 这些全局变量关系到分页的功能
		var pageSize = 10;//每页显示数据条数
		var pageNo = 1;//当前页数
		var count = 0;//数据总条数
		function getData() {

			wait = layer.load();

			var cxrq = $("#test8").val();
			var jd_dm = $("#jdlist").val();
			var qymc = $("#qymc").val();
			var zsxm_dm = $("#zsxm_dm").val();
			if (cxrq == undefined || cxrq == "") {
				layer.msg("请选择日期！");
				layer.close(wait);
				return;
			}
			if (qymc == undefined || qymc == "") {
				layer.msg("请输入企业名称！");
				layer.close(wait);
				return;
			}
			ajax({
				url : "ajax.do?ctrl=qysksdbgsz_querySdbg",
				data : {
					"cxrq" : cxrq,
					"jd_dm" : jd_dm,
					"zsxm_dm" : zsxm_dm,
					"qymc" : qymc
				},
				type : 'post',
				success : function(obj) {
					console.log(obj);
					if (obj != null && obj.data != null) {
						var sjList = obj.data;
						console.log(sjList);
						getTbale(sjList);
					}
				}
			});
		}

		//初始化表格
		function getTbale(data) {
			//执行渲染
			layui.use([ 'table' ], function() {
				//第一个实例
				layui.table.render({
					elem : '#table',
					height : 150 //数据接口
					,
					data : data,
					cols : [ [ //表头
					{
						field : 'SFBGMC',
						templet : '#bg',
						title : '是否变更'
					}, {
						field : 'RK_RQ',
						title : '税款月份'
					},{
						field : 'NSRMC',
						title : '企业名称'
					}, {
						field : 'ZSXM',
						title : '税种'
					}, {
						field : 'JD_MC',
						title : '街道'
					}, {
						field : 'ZSE',
						title : '总税额'
					},  {
						field : 'QXJ',
						title : '',
						hide : true
					},{
						field : '',
						title : '操作',
						toolbar : '#bar',
						fixed : 'right'
					} ] ]
				});
				var ss = $(".laytable-cell-1-QXJ");
				for (var i = 0; i < ss.length; i++) {
					$(ss[i].parentNode).hide();
				}

		           $("#selecttable").hide();
		           $("#inserttable").hide();
				//方法级渲染
				//监听工具条
				layui.table.on('tool(user)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
					var data = obj.data; //获得当前行数据
					console.log(">>><<<");
					console.log(data);
					var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
					if (layEvent === 'cf') { //删除
						var s="";
					zsee=parseFloat(data.ZSE);
					jd=data.JD_MC;
					sz=data.ZSXM;
					nsrmc=data.NSRMC;
						 s += '<tr>';
				           s += '<td>' + data.NSRMC + '</td>';
				           s += '<td>' + data.ZSXM + '</td>';
				           s += '<td>' + data.JD_MC + '</td>';
				           s += '<td>' + data.ZSE + '</td>';
				           s += '</tr>';
				           s += '<tr>';
				           s += '<td colspan="2" style="text-align:center;">拆分后税款信息</td>';
				           s += '<td><button class="layui-btn layui-btn-xs" id="add" type="button" lay-submit="" lay-filter="add">添加</button></td>';
				           s += '<td><button class="layui-btn layui-btn-xs" id="save" type="button" lay-submit="" lay-filter="save">保存</button></td>';
				           s += '</tr>';
				           $("#ttbody").html(s);

				           $("#selecttable").show();
				           $("#inserttable").show();
					} else if (layEvent === 'hf') { //编辑
						sz=data.ZSXM;
						nsrmc=data.NSRMC;
						jd=data.JD_MC;
						gethf(sz,nsrmc,jd);
					}
				});
			});
			layer.close(wait);
		} /* getTbale结尾 */
	</script>
</html>