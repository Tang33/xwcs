<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<base href="<%=basePath%>">

<title>企业清册管理—国税大企业清册管理</title>

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

</head>
<body style="overflow-x: hidden">
	<input type="hidden" id="dwid" value="${dwid}" />
	<blockquote class="layui-elem-quote layui-text">
		本功能用于上传每月各街道报表！</blockquote>
	<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div style="width: 20%; float: left; height: 38px;">
										<label class="layui-form-label">报表月份:</label>
										<div class="layui-input-inline">
											<input type="text" class="layui-input" id="yf" name="yf"
												 lay-filter="yf">
										</div>
									</div>
									<div style="float: left; display: inline-flex;">
										<div class="layui-btn-group">
											<button class="layui-btn layui-btn-normal" id="button"
												type="button" lay-submit="" lay-filter="button">查 询</button>
										</div>
										<div class="layui-btn-group">
											<button class="layui-btn layui-btn-normal" id="doAdd"
												type="doAdd" lay-submit="" lay-filter="doAdd">新增报表</button>
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

			<table class="layui-table" id="table" lay-filter="sj">
				<thead>
					<tr>
						<th lay-data="{field:'JD_DM',style:'display:none;'}">街道代码</th>
						<th lay-data="{field:'SSYF',style:'display:none;'}">所属月份</th>
						<th lay-data="{field:'SCBBMC',style:'display:none;'}">上传报表名称</th>
						<th lay-data="{field:'JD_MC'}">街道名称</th>
						<th lay-data="{field:'LRRY_MC'}">上传人</th>
						<th lay-data="{field:'SCSJ'}">上传日期</th>
						<th lay-data="{field:'BBMC'}">报表名称</th>
						<th lay-data="{field:'PX',toolbar:'#bar1'}">浏览</th>
						<th lay-data="{field:'dfkj',width:'15%',toolbar:'#bar2'}">操作</th>
					</tr>
				</thead>
				<tbody id="ttbody">

				</tbody>
			</table>
			<div id="page1"></div>
			<script type="text/html" id="bar1">

				{{# if(d.SSYF!="" && d.SSYF!="undefined" && d.SCBBMC!="-1"){ }}
  				<a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="downLoad"><i class="layui-icon"></i> 下载</a>
				{{# } }}

				</script>
			<script type="text/html" id="bar2">
  					{{# if(d.SSYF!="" && d.SSYF!="undefined"){ }}
		<a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="deleteInfo"><i class="layui-icon"></i> 删除</a>
			{{# } }} 
				</script>
		</fieldset>
	</form>

	<div id="upload" style="display: none;">
		<form class="layui-form" id="form7" action=""
			style="margin-top: 20px;">
			<div class="layui-form-item" id="uploadsendPic">
				<label class="layui-form-label"><span style="color: red">*</span>街道代码:</label>
				<div class="layui-input-block" style="width: 85%;">
					<select name="upload_JD_DM" id="upload_JD_DM" lay-verify="required"
						lay-search="" lay-filter="upload_JD_DM">
						<!-- <option value="0">请选择</option>
						<option value="91">新街口</option>
						<option value="92">梅园</option>
						<option value="93">后宰门</option>						
						<option value="94">玄武湖</option>					
						<option value="95">玄武门</option>					
						<option value="96">锁金村</option>					
						<option value="97">孝陵卫</option>					
						<option value="98">红山</option>					
						<option value="99">徐庄</option> -->
					</select>
				</div>
			</div>
			<div class="layui-form-item" id="uploadsendPic">
				<div class="layui-input-block"
					style="text-align: center; margin-left: 0;">
					<button type="button" class="layui-btn " id="selectBB" class="selectBB"
						style="margin-top: 0px;" lay-filter="selectBB">
						选择文件
					</button>
					<button type="button" class="layui-btn layui-btn-normal"
						id="addSave" lay-submit="" lay-filter="addSave"
						style="margin-top: 0px; margin-left: 20%;">确定上传</button>
				</div>
				<div class="layui-row1" id="moset" style="margin-left: 30%;font-size: 20px;">
				</div>
			</div>
		</form>
	</div>
</body>
<!-- <script src="../static/layui/layui.js" charset="utf-8"></script> -->
<script>
	var editlayer;
	var addlayer;
	var uploadlayer;
	

	
	
	function getMaxData() {
		layui.use(['laydate','layer'], function() {
			var laydate = layui.laydate;
			 var layer = layui.layer;
		var m = layer.msg('拼命加载中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time:false});
		
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url : "ajax.do?ctrl=Sjdr_getNsDate",
			data : "",
			dataType : "json",
			success : function(obj) {
				var rq = "";
				if (obj.code == "000") {
					 r = obj.data[0];
					//日期条件默认值为数据库中数据的最大入库日期
					$("#yf").val(r.RKRQ);
					rq = r.RKRQ
					getData(m)
					
				}
				
					//常规用法
					laydate.render({
						elem : '#yf',
						type : 'month',
						format : 'yyyyMM',
						max : rq
					});
					
				
			}
		})
	
		$.ajax({
		type : "post", //请求方式
		async : true, //是否异步
		//url : "ajax.do?ctrl=Sjdr_getNsDate",
		url : "JdSswcqkLrAction_jdInit.do",
		data : "",
		dataType : "json",
		success : function(obj) {
			var data = obj.data
			var html = "";
			debugger;
			for (var i = 0; i < data.length; i++) {
				html+='<option value="'+data[i].JD_DM+'">'+data[i].JD_MC+'</option>'
			}
			$('#upload_JD_DM').append(html);
			layui.use('form', function(){
			  var form = layui.form;
			  form.render('select');
			  //各种基于事件的操作，下面会有进一步介绍
			});
			
		}
	})
		});
	}

	$(function() {
		var dwid = $("#dwid").val();
		if (dwid != "00") {
			$("#doAdd").hide();
		}
		$("#table").attr("lay-data",
				"{width:" + document.body.clientWidth + "}");

		var value = "${jdlist}";
		
		getMaxData();
		//setTimeout("getData()", 500);
	}); 
	
	
	
	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 15;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
	function getData(m) {
		
		
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			//url : "ajax.do?ctrl=JdSswcqkLrAction_doQuery&pageNo=" + pageNo+ "&pageSize=" + pageSize,
			url : "JdSswcqkLrAction_doQuery.do?pageNo=" + pageNo+ "&pageSize=" + pageSize,
			data : $("#form").serialize(),
			dataType : "json",
			success : function(obj) {
				getTbale(obj.data);//拼接表格
				count = obj.count;//数据总条数
				queryPage();
				layer.close(m);
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
		layui.use([ 'laypage','layer'], function() {
			laypage = layui.laypage;
			var layer = layui.layer;
			laypage.render({
				//注意，这里的page1 是 ID，不用加 # 号
				elem : 'page1',
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
						var m = layer.msg('拼命加载中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time:false});
						getData(m);
					}
				}
			});
		});

	}

	//初始化表格
	function getTbale(data) {
		var s = "";
		$.each(data, function(v, o) {
			s += '<tr>';
			s += '<td>' + o.JD_DM + '</td>';
			s += '<td>' + o.SSYF + '</td>';
			s += '<td>' + o.SCBBMC + '</td>';
			s += '<td>' + (o.JD_MC == undefined ? "-" : o.JD_MC) + '</td>';
			s += '<td>' + (o.LRRY_MC == undefined ? "-" : o.LRRY_MC) + '</td>';
			s += '<td>' + (o.SCSJ == undefined ? "-" : o.SCSJ) + '</td>';
			//s += '<td>' + (o.SSYF == undefined ? "-" : o.SSYF) + '</td>';
			s += '<td>' + (o.BBMC == undefined ? "-" : o.BBMC) + '</td>';
			s += '</tr>';
		});
		$("#ttbody").html(s);
		//执行渲染
		layui.use([ 'table' ], function() {
			layui.table.init('sj', {
				height : 545 //设置高度
				,
				limit : pageSize
			//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
			//支持所有基础参数
			});
			$('.layui-table:eq(1) thead tr th:eq(0)').addClass('layui-hide');
			$('.layui-table:eq(1) thead tr th:eq(1)').addClass('layui-hide');
			$('.layui-table:eq(1) thead tr th:eq(2)').addClass('layui-hide');
			//街道单位隐藏删除操作列
			var dwid = $("#dwid").val();
			if (dwid != "00") {
				$("[data-field='dfkj']").css('display','none');
			}
		});
		if (data == null || data.length <= 0) {
			$("#page1").hide();
		} else {
			$("#page1").show();
		}
	}


	layui.use([ 'form','layer' ], function() {
		var form = layui.form;
		var layer = layui.layer;
		form.on('submit(button)', function(data) {
			pageNo = 1; //当点击搜索的时候，应该回到第一页
			var m = layer.msg('拼命加载中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time:false});
			getData(m);
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});

		form.on('submit(doAdd)', function(data) {
			debugger
			var yy2 = $("#yf").val()
			if(yy2==""||yy2=="undefiend"){
				layer.msg('请选择报表月份!', {icon: 2});
				return false;
			}
			
			uploadlayer = layer.open({
				type : 1,
				title : '添加记录',
				area : [ '60%', '60%' ],
				shadeClose : false, //点击遮罩关闭
				content : $('#upload'),
				success : function() {

					//$("#upload_YF").val(getYF());
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
	
	layui.use('upload',function() {
		var $ = layui.jquery, upload = layui.upload;
		var listwjm=new Array();
		//指定允许上传的文件类型
		uploadListIns=upload.render({
					elem : '#selectBB',
					url : 'uploadBB.do',
					before : function(obj) {
						wait = layer.load();
					},
					accept : 'file' //普通文件
					,
					auto : false,
					exts : 'xls|xlsx',
					bindAction : '#addSave',
					choose: function(obj){
						files = obj.pushFile();
						 obj.preview(function(index, file, result){
							 listwjm.push(file.name);
							 $("#moset").append('<a href="javascript:;" class="label"  id="'+index+'"><span>'+file.name+'</span><input type="hidden" name="'+file.name+'"/><i class="close">x</i></a>')	 	
							//删除
	                        $(document).on("click", ".close", function () {
								$(this).parent().remove();
								var id=$(this).parent().attr('id');
	                            delete files[id]; //删除对应的文件
	                            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
	                        });
						 
						 });
					},
					done : function(res) {
						layer.close(wait);
						if(res.mesg=='上传成功'){	
							$.ajax({
								type : "post", //请求方式
								async : true, //是否异步
								//url:"ajax.do?ctrl=JdSswcqkLrAction_addBB",
								url:"JdSswcqkLrAction_addBB.do",
								data: { 
									jd_dm : $("#upload_JD_DM").val(),
									yf : $("#yf").val(),
									scbbmc : res.src,
									bbmc : listwjm[0]
							    },
								dataType : "json",
								success : function(obj) {
									if (obj.code == "000") {
										layer.alert("上传成功",{icon: 1},function () {
										    layer.close(layer.index);
										    window.location.reload(); 
										    table.render();
							            });
									} else if (obj.code == "001"){
										layer.msg("请选择街道！");
									} else if (obj.code == "002"){
										layer.msg("该报表已存在！");
									}
									
								}
							});
						}
					}
				});
	});
	

	layui.use(['table','layer'],function() {
		var table = layui.table;
		var layer = layui.layer;
		//方法级渲染
		//监听工具条
		table.on('tool(sj)',function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
			var data = obj.data; //获得当前行数据
			var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			if (layEvent === 'deleteInfo') { //删除
				layer.confirm('',{
					area : ['30%','27%' ],
					content : '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">真的删除'+ data.BBMC+ '么?</div>'
					},function(index) {
						$.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							//url : "ajax.do?ctrl=JdSswcqkLrAction_doDel",
							url : "JdSswcqkLrAction_doDel.do",
							data : data,
							dataType : "json",
							success : function(
									obj) {
								if (obj.code == '000') {
									layer.msg('删除'+ data.BBMC+ ' 成功！');
								} else {
									layer.alert('删除'+ data.BBMC+ '失败！');
								}
								layer.close(index);
								var m = layer.msg('拼命加载中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time:false});
								getData(m);
							}
						});
				});
			} else if (layEvent === 'downLoad') {
				debugger
				var data = obj.data; //获得当前行数据
				var url =  "downBB.do?filePath="+data.SCBBMC+"&bbmc="+data.BBMC;
				window.location.href = encodeURI(url) 
				
			}
			});
		});
</script>

</html>