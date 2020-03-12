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

<title>企业属地管理—不可分企业清册管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->

<!-- <link rel="stylesheet" href="./static/layui/css/layui.css" media="all">
<script src="./static/layui/layui.js" charset="utf-8"></script>
<script src="./static/assets/js/jquery-1.8.3.min.js" charset="utf-8"></script>
<script src="./static/js/easy.ajax.js" charset="utf-8"></script> -->

<style type="text/css">
.sweet-alert{
	z-index:99999999;
    border: 1px solid #eee;
	box-shadow: 0 0 3px #c7b6b6;
}
</style>
</head>
<body style="overflow-x: hidden">
		<blockquote class="layui-elem-quote layui-text">
			本功能用于对系统的“重点税源类别管理”进行操作管理！</blockquote>
		<form class="layui-form" id="form" action="">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<div class="layui-colla-content  layui-show">
						<div class="layui-row">
						<div class="layui-col-md12">
							<div class="layui-row layui-col-space10">
								<div style="width: 20%; float: left; height: 38px;">
									<label class="layui-form-label">纳税人名称</label>
									<div class="layui-input-inline" style="width: 60%;">
										<input id="paramNsrmc" name="paramNsrmc" type="text" placeholder="请输入纳税人名称" class="layui-input">
									</div>
								</div>
								<div style="float: left; display: inline-flex;">
									<button class="layui-btn layui-btn-normal" id="button"
									type="button" lay-submit="" lay-filter="button">查 询</button>
								<button class="layui-btn layui-btn-normal" id="doExcel"
									type="doExcel" lay-submit="" lay-filter="doExcel">导出Excel</button>
								<button class="layui-btn layui-btn-normal" id="doAdd"
									type="doAdd" lay-submit="" lay-filter="doAdd">新增记录</button>
								</div>
							</div>


					</div>
					</div>
				</div>
			</div>

			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend>查询显示</legend>

				<table class="layui-table" id="table" lay-filter="sj">
					<thead>
						<tr>
							<th lay-data="{field:'SWGLM'}">税务管理码</th>
							<th lay-data="{field:'NSRSBH'}">纳税人识别码</th>
							<th lay-data="{field:'NSRMC',width:'30%'}">纳税人名称</th>
							<th lay-data="{field:'SCJYDZ'}">生产经营地址</th>
							<th lay-data="{field:'RQ'}">最后变更日期</th>
							<th lay-data="{field:'dfkj',toolbar:'#bar'}">操作</th>
						</tr>
					</thead>
					<tbody id="ttbody">
						
					</tbody>
				</table>
				<div id="page1"></div>
			<script type="text/html" id="bar">
  				<a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="updateInfo">修改</a>
  				<a class="layui-btn layui-btn-danger layui-btn-sm" lay-event="deleteInfo">删除</a>
			</script>
			</fieldset>
		</form>
		
		<div id="editmain" style="display: none;">
		<form class="layui-form" id="form5" action=""
			style="margin-top: 20px;">
				<input type="hidden" id="cb_id" name="cb_id"/>
				
				
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">纳税人识别码:</label>
				<div class="layui-input-inline">
					<input type="text" id="update_NSRSBH"  readonly="readonly"  style="width: 250px;" name="update_NSRSBH" 
					class="layui-input">
				</div>
				<label class="layui-form-label" style="width: 150px;">纳税人名称:</label>
				<div class="layui-input-inline">
					<input type="text" id="update_NSRMC" style="width: 250px;" name="update_NSRMC" 
					class="layui-input">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">税务管理码:</label>
				<div class="layui-input-inline">
					<input type="text" id="update_SWGLM" readonly="readonly"  style="width: 250px;" name="update_SWGLM" 
						class="layui-input">
				</div>
				<label class="layui-form-label" style="width: 150px;">生产经营地址:</label>
				<div class="layui-input-inline">
					<input type="text" id="update_SCJYDZ" style="width: 250px;" name="update_SCJYDZ" 
					class="layui-input">
				</div>
			</div>
			
			<div class="layui-form-item">
					<label class="layui-form-label" style="width: 150px;"></label>
					<div class="layui-input-inline" style="width: 64%;">
						<div style="text-align: right;">
							<button class="layui-btn layui-btn-normal" id="updateSave"
								type="button" lay-submit="" lay-filter="updateSave">确认修改</button>
						</div>
					</div>
				</div>
			
			<div class="layui-form-item">
				
			</div>
			<input type="hidden" name="update_YNSRMC" id="update_YNSRMC" value="">
		</form>
	</div>
	<div id="addmain" style="display: none;">
	
		<form class="layui-form" id="form3" action=""
			style="margin-top: 20px;">
			
			
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"><span style="color:red">*</span>纳税人识别码:</label>
				<div class="layui-input-inline">
					<input type="text" id="add_NSRSBH" name="add_NSRSBH"  lay-verify="required" 
					placeholder="请输入纳税人识别码:" class="layui-input">
				</div>
				<label class="layui-form-label" style="width: 150px;"><span style="color:red">*</span>纳税人名称:</label>
				<div class="layui-input-inline">
					<input type="text" id="add_NSRMC" name="add_NSRMC"  lay-verify="required"
					 placeholder="请输入纳税人名称" class="layui-input">
				</div>
				
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"><span style="color:red">*</span>税务管理码:</label>
				<div class="layui-input-inline">
					<input type="text" id="add_SWGLM" name="add_SWGLM"  lay-verify="required"
					 placeholder="请输入税务管理码" class="layui-input">
				</div>
				<label class="layui-form-label" style="width: 150px;"><span style="color:red">*</span>生产经营地址:</label>
				<div class="layui-input-inline">
					<input type="text" id="add_SCJYDZ" name="add_SCJYDZ"  lay-verify="required"
						 placeholder="请输入生产经营地址" class="layui-input">
				</div>
			</div>
			
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"></label>
				<div class="layui-input-inline" style="width: 57%;">
					<div style="text-align: right;">
						<button class="layui-btn layui-btn-normal" id="addSave"
							type="button" lay-submit="" lay-filter="addSave">确定添加</button>
					</div>
				</div>
			</div>
		</form>
	</div>
	</body>

	<script>
	var editlayer;
	var addlayer;
	
	$(function() {
		$("#table").attr("lay-data","{width:"+document.body.clientWidth+"}");
	
		var value = "${jdlist}";
		getData();
	});
	layui.use('laydate', function() {
		var laydate = layui.laydate;

		//常规用法
		laydate.render({
			elem : '#lrrq'
		});
		laydate.render({
			elem : '#xgrqedit'
		});
		//常规用法
		laydate.render({
			elem : '#rzrq'
		});
		laydate.render({
			elem : '#rzrqedit'
		});
	});

	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 15;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
	function getData() {
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "bkfqyqcgl/doQuery.do?pageNo=" + pageNo + "&pageSize="
					+ pageSize,
			data : $("#form").serialize(),
			dataType : "json",
			success : function(obj) {
					getTbale(obj.data);//拼接表格
					count = obj.count;//数据总条数
					queryPage();
			
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
				//注意，这里的page1 是 ID，不用加 # 号
				elem : 'page1' ,
				 //数据总数，从服务端得到
				count : count,
				//每页显示条数
				limit : pageSize,
				//条数列表
				limits : [ 15, 30, 45, 60 ],
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
	
	//将时间戳转 （yy-MM-dd hh:mm:ss）
/* 	function formatDateTime(inputTime) {  
		    var date = new Date(inputTime);
		    var y = date.getFullYear();  
		    var m = date.getMonth() + 1;  
		    m = m < 10 ? ('0' + m) : m;  
		    var d = date.getDate();  
		    d = d < 10 ? ('0' + d) : d;  
		    var h = date.getHours();
		    h = h < 10 ? ('0' + h) : h;
		    var minute = date.getMinutes();
		    var second = date.getSeconds();
		    minute = minute < 10 ? ('0' + minute) : minute;  
		    second = second < 10 ? ('0' + second) : second; 
		     return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;  
	};*/
	function getLocalTime(nS) {
		return new Date(parseInt(nS) * 1000).Format("yyyy-MM-dd");
	}
		Date.prototype.Format = function(fmt) {//author: meizz
			var o = {
				"M+" : this.getMonth() + 1, //月份
				"d+" : this.getDate(), //日
				"h+" : this.getHours(), //小时
				"m+" : this.getMinutes(), //分 				"s+" : this.getSeconds(), //秒
				"q+" : Math.floor((this.getMonth() + 3)/3), 
				"S" : this.getMilliseconds()
			//毫秒
			};
			if (/(y+)/.test(fmt))
				fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
						.substr(4 - RegExp.$1.length));
			for ( var k in o)
				if (new RegExp("(" + k + ")").test(fmt))
					fmt = fmt.replace(RegExp.$1,
							(RegExp.$1.length == 1) ? (o[k])
									: (("00" + o[k]).substr(("" + o[k]).length)));
			return fmt;
		}
	
	//初始化表格
	function getTbale(data) {
		var s = "";
		$.each(data, function(v, o) {
			
			s += '<tr>';
			s += '<td>' + (o.SWGLM == undefined ? "-" : o.SWGLM)+ '</td>';
			s += '<td>' + (o.NSRSBH == undefined ? "-" : o.NSRSBH)+ '</td>';
			s += '<td>' + (o.NSRMC == undefined ? "-" : o.NSRMC) + '</td>';
			s += '<td>' + (o.SCJYDZ == undefined ? "-" : o.SCJYDZ) + '</td>';
			s += '<td>' + o.RQ + '</td>';
			s += '</tr>';
		});
		$("#ttbody").html(s);
		//执行渲染
		layui.use([ 'table' ], function() {
			layui.table.init('sj', {
				height : 560 //设置高度
				,
				limit : pageSize
			//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
			//支持所有基础参数
			});
		});
		if (data == null || data.length <= 0) {
			$("#page1").hide();
		} else {
			$("#page1").show();
		}
	}

	//初始化Element
	layui.use([ 'element', 'layer' ], function() {
		var element = layui.element;
		var layer = layui.layer;

		//监听折叠
		
	});
	//初始化Element
	layui.use([ 'form' ], function() {
		var form = layui.form;
		form.on('submit(button)', function(data) {
			pageNo = 1; //当点击搜索的时候，应该回到第一页
			getData();
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		form.on('submit(doExcel)', function(data) {
			var paramNsrmc=$("#paramNsrmc").val();
			window.location.href="bkfqyqcgl/export.do?paramNsrmc="+paramNsrmc;
			pageNo = 1; //当点击搜索的时候，应该回到第一页
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		form.on('select(findship)', function(data) {
			getData();
		});
		
		form.on('submit(addSave)', function(data) {
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "bkfqyqcgl/doQueryCount.do",
				data : $("#form3").serialize(),
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						addAjax();
					} else if(obj != null && obj.code == '001'){
						layer.confirm('纳税人识别码以及纳税人名称已存在，是否覆盖？',{title:'温馨提示'},function(e){
							deleteAndInsertAjax()
							layer.close(e);
						});
					} else if(obj != null && obj.code == '002'){
						layer.confirm('纳税人识别码已存在，是否覆盖？',{title:'温馨提示'},function(e){
							deleteAndInsertAjax()
							layer.close(e);
						});
					} else if(obj != null && obj.code == '003'){
						layer.confirm('税人名称已存在，是否覆盖？',{title:'温馨提示'},function(e){
							deleteAndInsertAjax()
							layer.close(e);
						});
					}else if(obj != null && obj.code == '004'){
						layer.confirm('税务管理码已存在，是否覆盖？',{title:'温馨提示'},function(e){
							deleteAndInsertAjax()
							layer.close(e);
						});
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
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		function addAjax(){
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "bkfqyqcgl/doAdd.do",
				data : $("#form3").serialize(),
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						//未做用户名、邮箱、电话重复的判断及提示
						layer.msg('成功添加记录！');
						layer.close(addlayer);
						
						$("#add_NSRMC").val("");
						$("#add_NSRSBH").val("");
						$("#add_SCJYDZ").val("");
						$("#add_SWGLM").val("");
						layui.form.render();
						$("#button").click();
					} else {
						layer.msg('添加记录失败!');
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
		
		function deleteAndInsertAjax(){
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "bkfqyqcgl/doDeleteAndInsert.do",
				data : $("#form3").serialize(),
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						//未做用户名、邮箱、电话重复的判断及提示
						layer.close(addlayer);
						layer.msg('覆盖成功！');
						$("#add_NSRMC").val("");
						$("#add_NSRSBH").val("");
						$("#add_SCJYDZ").val("");
						$("#add_SWGLM").val("");
						layui.form.render();
						$("#button").click();
					} else {
						layer.msg('覆盖失败!');
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

		form.on('submit(updateSave)', function(data) {
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "bkfqyqcgl/doQueryCount2.do",
				data : $("#form5").serialize(),
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						updateAjax();
					} else if(obj != null && obj.code == '003'){
						aui.alert('纳税人名称已存在');
					}
				},
				cancel : function() {
					return;
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
			
			
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		function updateAjax(){
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "bkfqyqcgl/doUpdate.do",
				data : $("#form5").serialize(),
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						//未做用户名、密码、邮箱重复的判断及提示
						layer.msg('提示：执行操作成功！');
						layer.close(editlayer);
						//获取当前的用户名字
						$("#update_SWGLM").val("")
						$("#update_NSRMC").val("")
						$("#update_YNSRMC").val("")
						$("#update_SCJYDZ").val("")
						$("#update_NSRSBH").val("")
						layui.form.render();
						$("#button").click();
					} else {
						layer.msg('提示：执行操作失败！');
					}
				},
				cancel : function() {
					return;
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

		form.on('submit(doAdd)', function(data) {
			addlayer = layer.open({
				type : 1,
				title : '添加记录',
				area : [ '60%', '40%' ],
				shadeClose : false, //点击遮罩关闭
				content : $('#addmain'),
				success : function() {
					return;
				},
				cancel : function() {
					return;
				},
				end : function() {
					//$('#addmain').css("display", "none");
				}
			});

			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});

	});

	layui.use('table', function() {
		var table = layui.table;
		//方法级渲染
		//监听工具条
		table.on('tool(sj)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
			var data = obj.data; //获得当前行数据
			var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			if (layEvent === 'deleteInfo') { //删除
				layer.confirm(''
				,{area:['30%','27%']
				,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">真的删除-->' + data.NSRMC + '么?</div>'}
				, function(index) {
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						url : "bkfqyqcgl/doDel.do",
						data : data,
						dataType : "json",
						success : function(obj) {
							if (obj.code == '000') {
								layer.msg('删除' + data.NSRMC + ' 成功！');
							} else {
								layer.alert('删除' + data.NSRMC + '失败！');
							}
							layer.close(index);
							$("#button").click();
						},
						error : function(XMLHttpRequest, textStatus,
								errorThrown) {
							// 状态码
							alert(XMLHttpRequest.status);
							// 状态
							alert(XMLHttpRequest.readyState);
							// 错误信息  
							alert(textStatus);
						}
					});

				});
			} else if (layEvent === 'updateInfo') { //编辑
				//do something
				editlayer = layer.open({
					type : 1,
					title : '修改资料',
					area : [ '60%', '40%' ],
					shadeClose : false, //点击遮罩关闭
					content : $('#editmain'),
					success : function() {
						
						$("#update_SWGLM").val(data.SWGLM)
						$("#update_NSRMC").val(HTMLDecode(data.NSRMC))
						$("#update_YNSRMC").val(HTMLDecode(data.NSRMC))
						$("#update_SCJYDZ").val(data.SCJYDZ)
						$("#update_NSRSBH").val(data.NSRSBH)
						layui.form.render();
					},
					cancel : function() {
						return;
					},
					end : function() {
						//$('#addmain').css("display", "none");
					}
				});
				//同步更新缓存对应的值
				obj.update({
					name : data.name
				});
			}
			//else if (layEvent === 'location') { //编辑
			//window.location.href="index.do?ctrl=map&method=location"; 
			//}

		});

	});
	//HTML反转义
	function HTMLDecode(text) { 
	var temp = document.createElement("div"); 
	temp.innerHTML = text; 
	var output = temp.innerText || temp.textContent; 
	temp = null; 
	return output; 
	} 
</script>


</html>