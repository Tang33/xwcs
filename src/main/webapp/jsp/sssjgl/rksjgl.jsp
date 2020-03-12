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

<title>企业清册管理—不可分企业清册管理</title>

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
i{
	display: inline-block;
    width: 18px;
    height: 18px;
    line-height: 18px;
    text-align: center
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
		本功能用于对系统的“重点税源类别管理”进行操作管理！</blockquote>
	<form class="layui-form" id="form" action="">
		<input id="cslx" name="cslx" type="hidden" value="0" /> <input
			id="ids" name="ids" type="hidden" value="0" />
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md3">
							<div class="layui-inline ">
								<label class="layui-form-label">入库年月</label>
								<div class="layui-input-inline">
									<input id="rkrq" name="rkrq" type="text" placeholder="请输入入库年月"
										class="layui-input">
								</div>
								<button type="button" class="layui-btn" id="select">选择文件</button>
							</div>
						</div>

						<div class="layui-col-md3">
							<label class="layui-form-label">导入方式:</label>

							<div class="layui-inline">
								<select name="drfs" lay-filter="drfs" id="drfs">
									<option value="0">入库</option>
									<option value="1">退库</option>
									<option value="2">个人所得税</option>
								</select>
							</div>
						</div>
						<div class="layui-col-md6" style="text-align:center;">
							<div class="layui-inline">
								<input type="radio" name="type" id="qlsc" value="0" title="全量上传"
									checked="" lay-filter="type"> <input type="radio"
									name="type" id="ddxc" value="1" title="断点续传" lay-filter="type">
								<button type="button" class="layui-btn" id="doinput">
									<i class="layui-icon"></i>上传比对
								</button>
								<button type="button" class="layui-btn" id="dodr"
									onclick="javascript:dr();">
									<i class="layui-icon"></i>导入数据
								</button>
							</div>
						</div>

					</div>
					<div class="layui-row1" id="moset" style="margin-top: 5px;height: 30px;width: 50%;margin-left: 121px;">
						<!-- <span>123</span> -->				
					</div>
					
					<div class="layui-row" style="margin-top: 10px;">
						<div class="layui-col-md12">
							<div class="layui-col-md4">
								<div class="layui-form-item">
									<label class="layui-form-label">征收项目:</label>
									<div class="layui-input-block">
										<select name="zslist" id="zslist">
										</select>
									</div>
								</div>

							</div>
							<div class="layui-col-md4">
								<div class="layui-form-item">
											<label class="layui-form-label">行业:</label>
											<div class="layui-input-block">
												<select name="hylist" style="width: 40%;" id="hylist">

												</select>
											</div>
								</div>
							</div>
							<div class="layui-col-md4">
								<div class="layui-form-item">
											<label class="layui-form-label">街道:</label>
											<div class="layui-input-block">
												<select name="jdlist" style="width: 40%;" id="jdlist">

												</select>
											</div>
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md6">
							<div class="layui-form-item">
								<label class="layui-form-label">关键字查询:</label>
								<div class="layui-input-block">
									<input type="text" name="gjz" lay-verify="title" id="gjz"
										autocomplete="off"
										placeholder="登记序号，电子税票号码，应征凭证序号，纳税人名称，纳税人识别码"
										class="layui-input">
								</div>
							</div>
						</div>
						<div class="layui-col-md6" style="text-align: center;">
							<button class="layui-btn" id="bfxg" lay-submit=""
								lay-filter="bfxg">部分修改</button>
							<button class="layui-btn" id="qjxg" lay-submit=""
								lay-filter="qjxg">全局修改</button>
							<button class="layui-btn" id="button" type="button" lay-submit=""
								lay-filter="button">查询</button>
						</div>
					</div>
					
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>

			<table class="layui-table" id="table" lay-filter="user">
				<thead>
					<tr>
						<th lay-data="{field:'id',width:'100',checkbox:true}"></th>
						<th lay-data="{field:'xh',width:'100'}">序号</th>
						<th lay-data="{field:'nsrsbh',width:'200'}">纳税人识别码</th>
						<th lay-data="{field:'nsrmc',width:'250'}">纳税人名称</th>
						<th lay-data="{field:'zsxm',width:'150'}">征收项目</th>
						<th lay-data="{field:'jd',width:'150'}">街道</th>
						<th lay-data="{field:'qkj',width:'150'}">全口径</th>
						<th lay-data="{field:'dfkj',width:'150'}">地方口径</th>
						<th lay-data="{field:'yskm',width:'150'}">预算科目</th>
						<th lay-data="{field:'ysfpbl',width:'150'}">预算分配比例</th>
						<th lay-data="{field:'hy',width:'250'}">行业</th>
						<th lay-data="{field:'djxh',width:'200'}">登记序号</th>
						<th lay-data="{field:'dzsphm',width:'200'}">电子税票号码</th>
						<th lay-data="{field:'yzpzxh',width:'200'}">应征凭证序号</th>
						<th lay-data="{field:'hyml',width:'150'}">行业门类</th>
						<th lay-data="{field:'hydl',width:'150'}">行业大类</th>
						<th lay-data="{field:'hyzl',width:'150'}">行业中类</th>
						<th lay-data="{field:'yskmdm',width:'150'}">预算科目代码</th>
						<th lay-data="{field:'qxjbl',width:'150'}">区县级比例</th>
						<th lay-data="{field:'slsx',width:'150'}">税款属性</th>
					</tr>
				</thead>
				<tbody id="ttbody">

				</tbody>
			</table>
			<div id="page1"></div>
		</fieldset>
	</form>
	<div id="xg" style="display: none;">
		<form class="layui-form" id="form6" action=""
			style="margin-top: 20px;">
			<input type="hidden" name="tjfx" id="tjfx">
			<div class="layui-form-item">
				<label class="layui-form-label">街道名称</label>
				<div class="layui-input-block" style="width: 85%;">
					<select name="jd" id="jd">
						<option value=""></option>

					</select>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">纳税人名称</label>
				<div class="layui-input-block" style="width: 85%;">
					<input type="text" name="nsrmc" id="nsrmc" placeholder="请输入纳税人名称"
						autocomplete="off" class="layui-input">
				</div>
			</div>

			<div class="layui-form-item">
				<div class="layui-input-block">
					<button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
				</div>
			</div>
		</form>
	</div>
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
						debugger;
						//指定允许上传的文件类型    rkrq入库年月
						uploadListIns=upload.render({
									elem : '#select',
									multiple: true,
									url : 'upload.do?lx=rksj&name='
											+ $("#rkrq").val(),
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
										var rkrq = $("#rkrq").val();
										if (rkrq == "" || rkrq == undefined
												|| rkrq == null) {
											layer.close(wait);
											layer.msg("请选择入库日期");
											return;
										}
										console.log(res.data);
										var filenames;
										$.each(res.data, function(i) {
											
											if(i==0){
												
												filenames=res.data[i].bdsrc;
											}else{
												
												filenames +="、"+res.data[i].bdsrc;
											}
										});
										console.log(filenames);
										//导入方式
										if (res.code == "0") {
											var drfs = $("#drfs").val();
											$
													.ajax({
														type : "post", //请求方式
														async : true, //是否异步
														url : "ajax.do?ctrl=rksjgl_doInput",
														data : {
															filenames : filenames,
															drfs : drfs,
															rkrq : $(
															"#rkrq")
															.val()
															},
														dataType : "json",
														success : function(obj) {
															console.log(obj)
															if (obj.data == "success") {
																$
																		.ajax({
																			type : "post", //请求方式
																			async : true, //是否异步
																			url : "ajax.do?ctrl=rksjgl_doCheck",
																			data : {
																				rkrq : $(
																						"#rkrq")
																						.val()
																			},
																			dataType : "json",
																			success : function(
																					obj1) {
																				if (obj1.data == "1") {
																					$
																							.ajax({
																								type : "post", //请求方式
																								async : true, //是否异步
																								url : "ajax.do?ctrl=rksjgl_doLoadup",
																								data : {
																									rkrq : $(
																											"#rkrq")
																											.val(),
																									cslx : $(
																											"#cslx")
																											.val()
																								},
																								dataType : "json",
																								success : function(
																										obj2) {
																									layer
																											.close(wait);
																									if (obj2.code == "000") {
																										layer.msg("导入成功！");
																										
																									} else {
																										layer.msg("导入正式表失败！");
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
																					layer
																							.msg("比对失败！");
																					layer
																							.close(wait);
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
																layer
																		.msg("导入临时表失败！");
																layer
																		.close(wait);
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
	
	
	layui.use('laydate', function() {
		var laydate = layui.laydate;
		var date = new Date;
		var year = date.getFullYear();
		var month = date.getMonth();
		if (month == 0) {
			month = 12;
		}
		month = (month < 10 ? "0" + month : month);
		var mydate = (year.toString() + "" + month.toString());
		laydate.render({
			elem : '#rkrq',
			type : 'month',
			format : 'yyyyMM'
		});
		$("#rkrq").val(mydate);
		getData();
	});
	function initjd() {
		ajax({
			url : "ajax.do?ctrl=zdycx_queryInit",
			data : {},
			type : 'post',
			success : function(obj) {
				//	aui.alert(obj);
				if (obj != null && obj.data != null) {
					//拼接街道 下拉选框
					var str = "<option value='%'>请选择</option>";
					var jdlist = obj.data.jdlist;
					for (var i = 0; i < jdlist.length; i++) {
						str += "<option value='"+jdlist[i].JD_DM+"'>"
								+ jdlist[i].JD_MC + "</option>";
					}
					$("#jd").html(str);
					layui.form.render('select');

				}
			}
		});
	}
	$(function() {
		ajax({
			url : "ajax.do?ctrl=rksjgl_queryInit",
			data : {},
			type : 'post',
			success : function(obj) {
				if (obj != null && obj.data != null) {
					var str = "<option value='%'>请选择</option>";
					var jdlist = obj.data.jdlist;
					var hylist = obj.data.hylist;
					var zslist = obj.data.zslist;
					for (var i = 0; i < jdlist.length; i++) {
						str += "<option value='"+jdlist[i].JD_DM+"'>"
								+ jdlist[i].JD_MC + "</option>";
					}
					$("#jdlist").html(str);

					str = "<option value='%'>请选择</option>";
					for (var i = 0; i < hylist.length; i++) {
						str += "<option value='"+hylist[i].HYML_MC+"'>"
								+ hylist[i].HYML_MC + "</option>";
					}
					$("#hylist").html(str);

					str = "<option value='%'>请选择</option>";
					for (var i = 0; i < zslist.length; i++) {
						str += "<option value='"+zslist[i].ZSXM+"'>"
								+ zslist[i].ZSXM + "</option>";
					}
					$("#zslist").html(str);

					layui.form.render();
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
	function getData() {
		temp="";
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "ajax.do?ctrl=rksjgl_query&pageNo=" + pageNo + "&pageSize="
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
			s += '<tr><td></td><td>' + o.XH + '</td>';
			s += '<td>' + (o.NSRSBH == undefined ? "-" : o.NSRSBH) + '</td>';
			s += '<td>' + (o.NSRMC == undefined ? "-" : o.NSRMC) + '</td>';
			s += '<td>' + (o.ZSXM == undefined ? "-" : o.ZSXM) + '</td>';
			s += '<td>' + (o.JD_MC == undefined ? "-" : o.JD_MC) + '</td>';
			s += '<td>' + (o.ZSE == undefined ? "-" : o.ZSE) + '</td>';
			s += '<td>' + (o.SJSE == undefined ? "-" : o.SJSE) + '</td>';
			s += '<td>' + (o.YSKM_MC == undefined ? "-" : o.YSKM_MC) + '</td>';
			s += '<td>' + (o.YSFPBL_MC == undefined ? "-" : o.YSFPBL_MC)
					+ '</td>';
					s += '<td>' + (o.HY == undefined ? "-" : o.HY) + '</td>';
			s += '<td>' + (o.DJXHS == undefined ? "-" : o.DJXHS) + '</td>';
			s += '<td>' + (o.DZSPHM == undefined ? "-" : o.DZSPHM) + '</td>';
			s += '<td>' + (o.YZPZXH == undefined ? "-" : o.YZPZXH) + '</td>';
			s += '<td>' + (o.HY_DM == undefined ? "-" : o.HY_DM) + '</td>';
			s += '<td>' + (o.HYDL == undefined ? "-" : o.HYDL) + '</td>';
			s += '<td>' + (o.HYZL == undefined ? "-" : o.HYZL) + '</td>';
			s += '<td>' + (o.YSKMDM == undefined ? "-" : o.YSKMDM) + '</td>';
			s += '<td>' + (o.BL == undefined ? "-" : o.BL) + '</td>';
			s += '<td>' + (o.SKSX == undefined ? "-" : o.SKSX) + '</td></tr>';
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
	
	
	/* $(document).on("click", ".close", function () {
		
		$(this).parent().remove();
		
	}) */
</script>


</html>