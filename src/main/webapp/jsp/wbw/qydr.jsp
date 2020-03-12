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

<body>
		<form class="layui-form" id="form" action="">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<div class="layui-colla-content  layui-show">
						<div class="layui-row layui-col-space10">
							<div class="layui-col-md8">
								<div class="layui-inline layui-col-md6">
									<div class="layui-upload">
										<label class="layui-form-label" style="width: 130px;">企业名单导入：</label>
										<div class="layui-input-inline">
											<input class="layui-input" type="text" id="wjmc1" readonly="readonly">
										</div>
										<button type="button" class="layui-btn" id="xzwj1">选择文件</button>
									</div>
								</div>
								<div class="layui-inline layui-col-md6">
									<label class="layui-form-label">入库年月</label>
									<div class="layui-input-inline">
									<input id="rkrq1" type="text" placeholder="请输入入库年月"
										class="layui-input">
								</div>
								</div>
							</div>
							<div class="layui-col-md4">
								<div class="layui-inline">
									<button type="button" class="layui-btn" id="wjsc1">上传</button>
									<button type="button" class="layui-btn" id="dr1">导入</button>
								</div>
							</div>
						</div>
						<div class="layui-row layui-col-space10">
							<div class="layui-col-md8">
								<div class="layui-inline layui-col-md6">
									<div class="layui-upload">
										<label class="layui-form-label" style="width: 130px;">企业税率导入：</label>
										<div class="layui-input-inline">
											<input class="layui-input" type="text" id="wjmc2" readonly="readonly">
										</div>
										<button type="button" class="layui-btn" id="xzwj2">选择文件</button>
									</div>
								</div>
								<div class="layui-inline layui-col-md6">
									<label class="layui-form-label">入库年月</label>
									<div class="layui-input-inline">
									<input id="rkrq2" type="text" placeholder="请输入入库年月"
										class="layui-input">
								</div>
								</div>
							</div>
							<div class="layui-col-md4">
								<div class="layui-inline">
									<button type="button" class="layui-btn" id="wjsc2">上传</button>
									<button type="button" class="layui-btn" id="dr2">导入</button>
								</div>
							</div>
						</div>
						<div class="layui-row layui-col-space10">
							<div class="layui-col-md8">
								<div class="layui-inline layui-col-md6">
									<div class="layui-upload">
										<label class="layui-form-label" style="width: 130px;">企业销售额导入：</label>
										<div class="layui-input-inline">
											<input class="layui-input" type="text" id="wjmc3" readonly="readonly">
										</div>
										<button type="button" class="layui-btn" id="xzwj3">选择文件</button>
										
									</div>
								</div>
								<div class="layui-inline layui-col-md6">
									<label class="layui-form-label">入库年月</label>
									<div class="layui-input-inline">
									<input id="rkrq3" type="text" placeholder="请输入入库年月"
										class="layui-input">
								</div>
								</div>
							</div>
							<div class="layui-col-md4">
								<div class="layui-inline">
									<button type="button" class="layui-btn" id="wjsc3">上传</button>
									<button type="button" class="layui-btn" id="dr3" >导入</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend style="font-size:12px;">单位：元</legend>
				<table class="layui-table" id="table" lay-filter="user" >
					<thead>
						<tr>
							<th lay-data="{field:'nsrsbh',width:'10%'}">序号</th>
							<th lay-data="{field:'nsrmc',width:'15%'}">纳税人名称</th>
							<th lay-data="{field:'hy',width:'15%'}">税率</th>
							<th lay-data="{field:'zsxm',width:'15%'}">销售收入</th>
							<th lay-data="{field:'jd',width:'15%'}">销售额</th>
							<th lay-data="{field:'qkj',width:'15%'}">年度</th>
							<th lay-data="{field:'dfkj',width:'15%'}">月份</th>
						</tr>
					</thead>
					<tbody id="ttbody">
						
					</tbody>
				</table>
				<div id="demo2"></div>
			</fieldset>
	<script>
	var wait;
	var scTemp=0;//用于判断是否上传成功！0  未上传数据  1 上传成功
		layui.use(['form','layer', 'layedit', 'laydate','laypage','upload'], function() {
			var form = layui.form,
				layer = layui.layer,
				layedit = layui.layedit,
				laydate = layui.laydate,
				laypage=layui.laypage,
				upload = layui.upload,
				$ = layui.jquery;
			//年月范围
			laydate.render({
				elem: '#rkrq1',
				type: 'year',
				range: false
			});
			laydate.render({
				elem: '#rkrq2',
				type: 'year',
				range: false
			});
			laydate.render({
				elem: '#rkrq3',
				type: 'month',
				range: false
			});
			upload.render({
				  elem: '#xzwj1'
				  ,url: 'upload.do?lx=wbw&name=wjmc'
				  ,auto: false //选择文件后不自动上传
				  ,bindAction: '#wjsc1' //指向一个按钮触发上传id="doinput3"
				  ,accept: 'file' 
				  ,exts : 'xls|xlsx'
				  ,before : function(obj) {
						wait=layer.load();
				   }
				  ,choose: function(obj){
				    obj.preview(function(index, file, result){
				      $("#wjmc1").val(file.name);
				    });
				  }
				  ,done : function(res) {
					  var rkrq = $("#rkrq1").val();
						if (rkrq == "" || rkrq == undefined || rkrq == null) {
							layer.close(wait);
							layer.msg("请选择入库日期");
							return ;
						}
					   if(res.code == "0"){
						  $.ajax({
							  type : "post", //请求方式
								async : true, //是否异步
								url : "ajax.do?ctrl=qydr_doInput",
								data : {
									filename : res.data.bdsrc,
									rq : rkrq
								},
								dataType : "json",
								success:function(res1){
									if(res1 != null && res1.code == "000"){
										scTemp=1;
										layer.close(wait);
								  		layer.msg("数据上传成功！");
									}else if(res1.code == "006"){
										scTemp=0;
										layer.close(wait);
								  		layer.msg("数据上传失败！");
									}else{
										scTemp=0;
										layer.close(wait);
								  		layer.msg("数据上传失败！");
									}
								},
								error : function(XMLHttpRequest,textStatus, errorThrown) {
									// 状态码
									alert(XMLHttpRequest.status);
									// 状态
									alert(XMLHttpRequest.readyState);
									// 错误信息  
									alert(textStatus);
								}
						  })
					  	}else{
					  		scTemp=0;
					  		layer.close(wait);
					  		layer.msg("上传失败");
					  	}
					   getData();
					}
				}); 
			
			upload.render({
				  elem: '#xzwj2'
				  ,url: 'upload.do?lx=wbw&name=wjmc'
				  ,auto: false //选择文件后不自动上传
				  ,bindAction: '#wjsc2' //指向一个按钮触发上传id="doinput3"
				  ,accept: 'file' 
				  ,exts : 'xls|xlsx'
				  ,before : function(obj) {
						wait=layer.load();
				   }
				  ,choose: function(obj){
				    obj.preview(function(index, file, result){
				      $("#wjmc2").val(file.name);
				    });
				  }
				  ,done : function(res) {
					  var rkrq = $("#rkrq2").val();
						if (rkrq == "" || rkrq == undefined || rkrq == null) {
							layer.close(wait);
							layer.msg("请选择入库日期");
							return ;
						}
					   if(res.code == "0"){
						  $.ajax({
							  type : "post", //请求方式
								async : true, //是否异步
								url : "ajax.do?ctrl=qydr_doInputSL",
								data : {
									filename : res.data.bdsrc,
									rq : rkrq
								},
								dataType : "json",
								success:function(res1){
									if(res1 != null && res1.code == "000"){
										scTemp=1;
										layer.close(wait);
								  		layer.msg("数据上传成功！");
									}else if(res1.code == "006"){
										scTemp=0;
										layer.close(wait);
								  		layer.msg("数据上传失败！");
									}else{
										scTemp=0;
										layer.close(wait);
								  		layer.msg("数据上传失败！");
									}
								},
								error : function(XMLHttpRequest,textStatus, errorThrown) {
									// 状态码
									alert(XMLHttpRequest.status);
									// 状态
									alert(XMLHttpRequest.readyState);
									// 错误信息  
									alert(textStatus);
								}
						  })
					  	}else{
					  		scTemp=0;
					  		layer.close(wait);
					  		layer.msg("上传失败");
					  	}
					   getData();
					}
				}); 
			upload.render({
				  elem: '#xzwj3'
				  ,url: 'upload.do?lx=wbw&name=wjmc'
				  ,auto: false //选择文件后不自动上传
				  ,bindAction: '#wjsc3' //指向一个按钮触发上传id="doinput3"
				  ,accept: 'file' 
				  ,exts : 'xls|xlsx'
				  ,before : function(obj) {
						wait=layer.load();
				   }
				  ,choose: function(obj){
				    obj.preview(function(index, file, result){
				      $("#wjmc3").val(file.name);
				    });
				  }
				  ,done : function(res) {
					  var rkrq = $("#rkrq3").val();
						if (rkrq == "" || rkrq == undefined || rkrq == null) {
							layer.close(wait);
							layer.msg("请选择入库日期");
							return ;
						}
					   if(res.code == "0"){
						  $.ajax({
							  type : "post", //请求方式
								async : true, //是否异步
								url : "ajax.do?ctrl=qydr_doInputXSE",
								data : {
									filename : res.data.bdsrc,
									rq : rkrq
								},
								dataType : "json",
								success:function(res1){
									if(res1 != null && res1.code == "000"){
										scTemp=1;
										layer.close(wait);
								  		layer.msg("数据上传成功！");
									}else if(res1.code == "006"){
										scTemp=0;
										layer.close(wait);
								  		layer.msg("数据上传失败！");
									}else{
										scTemp=0;
										layer.close(wait);
								  		layer.msg("数据上传失败！");
									}
								},
								error : function(XMLHttpRequest,textStatus, errorThrown) {
									// 状态码
									alert(XMLHttpRequest.status);
									// 状态
									alert(XMLHttpRequest.readyState);
									// 错误信息  
									alert(textStatus);
								}
						  })
					  	}else{
					  		scTemp=0;
					  		layer.close(wait);
					  		layer.msg("上传失败");
					  	}
					   getData();
					}
				}); 
		});
		
		function exceA(){
			var rkrq = $("#rkrq1").val();
			if (rkrq == "" || rkrq == undefined || rkrq == null) {
				layer.msg("请选择入库日期");
				return ;
			}
			if(scTemp==0){
				layer.msg("请先上传数据！");
				return ;
			}
			wait=layer.load();
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				url : "ajax.do?ctrl=qydr_execA",
				data : {
					rq : rkrq
				},
				dataType : "json",
				success:function(res1){
					console.log("导入----------");
					console.log(res1);
					if(res1 != null && res1.code == "000"){
						layer.close(wait);
				  		layer.msg("数据上传成功！");
				  		scTemp=0;
					}else{
						layer.close(wait);
				  		layer.msg("数据上传失败！");
				  		scTemp=0;
					}
				},
				error : function(XMLHttpRequest,textStatus, errorThrown) {
					layer.close(wait);
					// 状态码
					alert(XMLHttpRequest.status);
					// 状态
					alert(XMLHttpRequest.readyState);
					// 错误信息  
					alert(textStatus);
				}
			});
			getData();
		}
		function exceB(){
			var rkrq = $("#rkrq2").val();
			if (rkrq == "" || rkrq == undefined || rkrq == null) {
				layer.msg("请选择入库日期");
				return ;
			}
			if(scTemp==0){
				layer.msg("请先上传数据！");
				return ;
			}
			wait=layer.load();
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				url : "ajax.do?ctrl=qydr_execB",
				data : {
					rq : rkrq
				},
				dataType : "json",
				success:function(res1){
					if(res1 != null && res1.code == "000"){
						layer.close(wait);
				  		layer.msg("数据上传成功！");
				  		scTemp=0;
					}else{
						layer.close(wait);
				  		layer.msg("数据上传失败！");
				  		scTemp=0;
					}
				},
				error : function(XMLHttpRequest,textStatus, errorThrown) {
					layer.close(wait);
					// 状态码
					alert(XMLHttpRequest.status);
					// 状态
					alert(XMLHttpRequest.readyState);
					// 错误信息  
					alert(textStatus);
				}
			});
			getData();
		}
		function exceD(){
			var rkrq = $("#rkrq3").val();
			if (rkrq == "" || rkrq == undefined || rkrq == null) {
				layer.msg("请选择入库日期");
				return ;
			}
			if(scTemp==0){
				layer.msg("请先上传数据！");
				return ;
			}
			wait=layer.load();
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				url : "ajax.do?ctrl=qydr_execD",
				data : {
					rq : rkrq
				},
				dataType : "json",
				success:function(res1){
					if(res1 != null && res1.code == "000"){
						layer.close(wait);
				  		layer.msg("数据上传成功！");
				  		scTemp=0;
					}else{
						layer.close(wait);
				  		layer.msg("数据上传失败！");
				  		scTemp=0;
					}
				},
				error : function(XMLHttpRequest,textStatus, errorThrown) {
					layer.close(wait);
					// 状态码
					alert(XMLHttpRequest.status);
					// 状态
					alert(XMLHttpRequest.readyState);
					// 错误信息  
					alert(textStatus);
				}
			});
			getData();
		}
		
		$(function() {
			//initdw();
			getData();
			$('#dr1').click(function(){
				exceA();
			});
			$('#dr2').click(function(){
				exceB();
			});
			$('#dr3').click(function(){
				exceD();
			});
		}); 

		//分页参数设置 这些全局变量关系到分页的功能
		var pageSize = 10;//每页显示数据条数
		var pageNo = 1;//当前页数
		var count = 0;//数据总条数
		function getData() {
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "ajax.do?ctrl=qydr_getallList&pageNo=" + pageNo + "&pageSize="
						+ pageSize,
				data : {},
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						getTbale(obj.data);//拼接表格
						count = obj.count;//数据总条数
						queryPage();
					}
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

		function queryPage() {
			layui.use([ 'laypage' ], function() {
				laypage = layui.laypage;
				laypage.render({
					elem : 'demo2' //注意，这里的page1 是 ID，不用加 # 号
					,
					count : count //数据总数，从服务端得到
					,
					limit : pageSize//每页显示条数
					,
					limits : [ 10, 20, 30, 50 ]//条数列表
					,
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
		//初始化表格
		function getTbale(data) {
			var s = "";
			$.each(data, function(v, o) {
				s += '<tr><td>' + (v+1) + '</td>';
				s += '<td>' + (o.NSRMC == undefined ? "-" : o.NSRMC) + '</td>';
				s += '<td>' + (o.SL == undefined ? "-" : o.SL) + '</td>';
				s += '<td>' + (o.XXSR == undefined ? "-" : o.XXSR) + '</td>';
				s += '<td>' + (o.XSE == undefined ? "-" : o.XSE) + '</td>';
				s += '<td>' + (o.ND == undefined ? "-" : o.ND) + '</td>';
				s += '<td>' + (o.RQ == undefined ? "-" : o.RQ) + '</td>';
				s += '<td>' + (o.HJE == undefined ? "-" : o.HJE) + '</td></tr>';
			});
			$("#ttbody").html(s);
			//执行渲染
			layui.use([ 'table' ], function() {
				layui.table.init('user', {
					height : 480 //设置高度
					,
					limit : pageSize
				//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
				//支持所有基础参数
				});
			});

			if (data == null || data.length <= 0) {
				$("#demo2").hide();
			} else {
				$("#demo2").show();
			}

		}
		
	</script>
</body>
</html>