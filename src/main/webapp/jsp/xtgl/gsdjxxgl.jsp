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

<title>工商登记信息管理</title>

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

.layui-form .layui-border-box .layui-table-view{
	max-width:1700px;
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
<body  style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">
		本功能页面用于对外部数据导入，请选择入库年月、导入方式、全量/断点、选择上传的数据文件，点击上传比对进行数据的导入!</blockquote>
	<form class="layui-form" id="form" action="">
		<input id="cslx" name="cslx" type="hidden" value="0" /> 
		<input id="zdyf" name="zdyf" type="hidden" value="0" />
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md6" >

							<div class="layui-inline">
								<!-- <input type="radio"
									name="type" value="1" title="断点续传" checked=""  lay-filter="type">
								<input type="radio" name="type" value="0" title="全量上传"
									lay-filter="type"> -->
								<button type="button" class="layui-btn" id="select">选择文件</button>
								<button type="button" class="layui-btn" id="doinput">
								上传
								</button>
							</div>
						</div>

					</div>
					<div class="layui-row1" id="moset" style="margin-top: 5px;height: 30px;width: 50%;margin-left: 121px;">
						<!-- <span>123</span> -->				
					</div>
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend style="padding: 0px 15px;">查询显示</legend>

			<table class="layui-table" id="table" lay-filter="user">
				<thead>
					<tr>
						<th lay-data="{field:'wjm'}">文件名</th>
						<th lay-data="{field:'xzsl'}">新增纳税人</th>
						<th lay-data="{field:'qcsl'}">去除数量</th>
						<th lay-data="{field:'drsj'}">导入时间</th>
					</tr>
				</thead>
				<tbody id="ttbody">

				</tbody>
			</table>
			<div id="page1"></div>
		</fieldset>
	</form>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	var wait;
	var temp = "";
	var qxlayer;
	var files;

	layui.use('upload',function() {
						var $ = layui.jquery, upload = layui.upload;
						var listwjm=new Array();
						//指定允许上传的文件类型
						uploadListIns=upload.render({
									elem : '#select',
									multiple: true,
									url : 'upload.do?lx=sy',
									before : function(obj) {
										wait = layer.load();
									},
									accept : 'file' //普通文件
									,
									auto : false,
									exts : 'xls|xlsx|zip|rar',
									bindAction : '#doinput',
									choose: function(obj){
										
										files = obj.pushFile();
										 obj.preview(function(index, file, result){
											 console.log(file.name);
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
										console.log(res.data);
										var data=eval("("+res.data+")");
										var filenames;
										if(data.length===1){
											
											filenames=data[0].bdsrc+"$_1$"+res.ybm;
											
										}else{
											
											$.each(data, function(i) {
												
												if(i==0){
													
													filenames=data[i].bdsrc;
												}else{
													
													filenames +="$_$"+data[i].bdsrc;
												}
											});
										}
										
										
										console.log(filenames);
										if (res.code == "0") {
											$.ajax({
														type : "post", //请求方式
														async : true, //是否异步
														url : "ajax.do?ctrl=swdjxxgl_doInputnew",
														data : {
															filenames : filenames
															},
														dataType : "json",
														timeout: 1000*60*30,
														success : function(obj) {
															console.log(obj)
															if (obj.code == "000") {
																layer.alert("导入成功",{icon: 1},function () {
																    layer.close(layer.index);
																    window.location.reload(); 
													            }); 
															} else {
																
																layer.msg("导入失败！");
																layer.close(wait);
															}

														},
														error : function(
																XMLHttpRequest,
																textStatus,
																errorThrown) {
															// 状态码
															alert(XMLHttpRequest.status);
															// 状态
															alert(XMLHttpRequest.readyState);
															// 错误信息  
															alert(textStatus);
														}
													});
										} else {
											layer.msg("上传失败！");
											layer.close(wait);
										}
									}
								});
					});
	
	
	function dr() {
		aui.confirm({
			title : "导入确认",
			text : "请确认所有的数据都已修改完毕，导入后将无法继续修改数据",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : '#DD6B55',
			confirmButtonText : '确 认导入',
			success : function() {
				wait = layer.load();
				ajax({
					url : "ajax.do?ctrl=rksjgl_dr",
					type : "post", //请求方式
					async : true, //是否异步
					data : {
						rkrq : $("#rkrq").val()
					},
					dataType : "json",
					success : function(obj) {
						layer.close(wait);
						aui.alert("导入成功！");
					}
				});
			}
		});

	}
	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 10;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
	getData();
	function getData() {
		temp="";
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "ajax.do?ctrl=swdjxxgl_query&pageNo=" + pageNo + "&pageSize="
					+ pageSize,
			data : $("#form").serialize(),
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
				elem : 'page1' //注意，这里的page1 是 ID，不用加 # 号
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
			s += '<tr><td>' + (o.wjm == undefined ? "-" : o.wjm) + '</td>';
			s += '<td>' + (o.xzsl == undefined ? "-" : o.xzsl) + '</td>';
			s += '<td>' + (o.qcsl == undefined ? "-" : o.qcsl) + '</td>';
			s += '<td>' + (o.drsj == undefined ? "-" : o.drsj) + '</td>';
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

			$(".layui-table-view")[0].style.width=document.body.clientWidth-20;
			layui.table.on('checkbox(user)', function(obj) {
				console.log(obj.data);
				if (obj.type=="all") {
					var arr= layui.table.cache.table;
					for (var i = 0; i < arr.length; i++) {
						if (obj.checked) {
							var xh = arr[i].xh;
							temp += xh + ","
						}else{
							temp = temp.replace(arr[i].xh + ",", "");
						}
					}
				}else{
					if (obj.checked) {
						var xh = obj.data.xh;
						temp += xh + ","
					} else {
						temp = temp.replace(obj.data.xh + ",", "");
					}
				}
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
		element.on('collapse(test)', function(data) {
		});
	});
	//初始化Element
	layui.use([ 'form' ], function() {
		var form = layui.form;

		form.on('submit(button)', function(data) {
			pageNo = 1; //当点击搜索的时候，应该回到第一页
			getData();
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		
		 form.on('radio(type)', function (data) {        
		     console.log(data);
		     $("#cslx").val(data.value);
	         });
			//监听指定开关
			form.on('switch(yxbz)', function(data) {
				if (this.checked) {
					$("#yxbz2").val("1");
				} else {
					$("#yxbz2").val("0");
				}
			});
			//监听指定开关
			form.on('switch(yxbz1)', function(data) {
				if (this.checked) {
					$("#yxbz3").val("1");}});
		form.on('submit(formDemo)', function(data) {
			$("#ids").val(temp);
			var nsrmc = $("#nsrmc").val();
			var jdmc = $("#jd").find("option:selected").text();
			var jd = $("#jd").val();
			var tjfx = $("#tjfx").val();
			if (tjfx == "0") {
				$.ajax({
					type : "post", //请求方式
					async : true, //是否异步
					url : "ajax.do?ctrl=rksjgl_Update&nsrmc=" + nsrmc
							+ "&jddm=" + jd + "&jdmc=" + jdmc,
					data : $("#form").serialize(),
					dataType : "json",
					success : function(obj) {
						if (obj.code == '000') {
							layer.msg('修改成功！');
						} else {
							layer.msg('修改失败！');
						}
						layer.close(qxlayer);
						$("#button").click();
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
			} else {
				$.ajax({
					type : "post", //请求方式
					async : true, //是否异步
					url : "ajax.do?ctrl=rksjgl_UpdateAll&nsrmc=" + nsrmc
							+ "&jddm=" + jd + "&jdmc=" + jdmc,
					data : $("#form").serialize(),
					dataType : "json",
					success : function(obj) {
						if (obj.code == '000') {
							layer.msg('修改成功！');
						} else {
							layer.msg('修改失败！');
						}
						layer.close(qxlayer);
						$("#button").click();
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

			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		form.on('submit(bfxg)', function(data) {
			initjd();
			qxlayer = layer.open({
				type : 1,
				title : '部分修改',
				area : [ '1000px', '600px' ],
				shadeClose : false, //点击遮罩关闭
				content : $('#xg'),
				success : function() {

					$("#tjfx").val("0");
				},
				cancel : function() {
					$('#xg').hide();

				},
				end : function() {
					$('#xg').hide();
				}
			});

			return false;

		});
		form.on('submit(qjxg)', function(data) {
			initjd();
			qxlayer = layer.open({
				type : 1,
				title : '全局修改',
				area : [ '1000px', '600px' ],
				shadeClose : false, //点击遮罩关闭
				content : $('#xg'),
				success : function() {

					$("#tjfx").val("1");
				},
				cancel : function() {
					$('#xg').hide();

				},
				end : function() {
					$('#xg').hide();
				}
			});

			return false;

		});
		
	});
</script>


</html>