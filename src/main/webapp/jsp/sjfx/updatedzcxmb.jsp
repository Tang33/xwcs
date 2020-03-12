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
<title>定制查询模板</title>
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<style type="text/css">
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
}

.layui-container {
	width: 100%;
}

.layui-col-md2 {
	width: 13%;
}

.layui-form-checkbox[lay-skin="primary"] {
	margin-right: 0px;
	padding-right: 0px;
	height: 25px !important;
	line-height: normal !important;
	background: 0px 0px;
	border-width: initial !important;
	border-style: none !important;
	border-color: initial !important;
	border-image: initial !important;
}

.button {
	display: inline-block;
	position: relative;
	cursor: pointer;
	height: 36px;
	line-height: 36px;
	padding: 0 15px;
	border: 1px solid #2A97E9;
	border-right: none;
	background: #2A97E9;
}

.button:before, .button:after {
	position: absolute;
	right: -37px;
	top: -2px;
	border: 19px solid transparent;
	border-left-color: #2A97E9;
	content: ' ';
}

.button:before {
	right: -38px;
	border-left-color: #2A97E9;
}

.anli{
	color: #808080;
}
</style>


</head>

<body style="overflow-x: hidden">
	<div class="layui-container" style="margin-top: 15px;height: 1080px;">
		<div style="width: 43%; height: 95%; float: left;">
			<div style="width: 100%; border: 1px solid #e6e6e6; height: 55%;">
				<form class="layui-form" id="form" action="">
					<table class="layui-table" style="margin: 0px;">
						<thead>
							<tr>
								<th><strong>定义查询项</strong></th>
							</tr>
						</thead>
					</table>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<div class="layui-block ">
								<label class="layui-form-label" style="width: 80px;">年份</label>
								<div class="layui-input-block" style="width: 100px;">
									<select name="year" id="year">
										<option value="">请选择</option>
										<option value="2019">2019</option>
									</select>
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<div class="layui-block ">
								<label class="layui-form-label" style="width: 80px;">月份起止</label>
								<div class="layui-input-inline layui-col-md3" style="width: 100px;margin-right: 3px;">
									<select name="monthstart" id="monthstart">
										<option value="">请选择</option>
										<option value="01">01</option>
										<option value="02">02</option>
										<option value="03">03</option>
										<option value="04">04</option>
										<option value="05">05</option>
										<option value="06">06</option>
										<option value="07">07</option>
										<option value="08">08</option>
										<option value="09">09</option>
										<option value="10">10</option>
										<option value="11">11</option>
										<option value="12">12</option>
									</select>
								</div>

								<label class="layui-form-label" style="width: 50px;padding-left: 0px;margin-left: 0px;">月起</label>
								<div class="layui-input-inline layui-col-md3" style="width: 100px;margin-left: 25px;margin-right: 3px;">
									<select name="monthend" id="monthend">
										<option value="">请选择</option>
										<option value="01">01</option>
										<option value="02">02</option>
										<option value="03">03</option>
										<option value="04">04</option>
										<option value="05">05</option>
										<option value="06">06</option>
										<option value="07">07</option>
										<option value="08">08</option>
										<option value="09">09</option>
										<option value="10">10</option>
										<option value="11">11</option>
										<option value="12">12</option>
									</select>
								</div>
								<label class="layui-form-label" style="width: 50px;padding-left: 0px;margin-left: 0px;">月止</label>
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<div class="layui-block ">
								<label class="layui-form-label" style="width: 80px;">税种</label>
								<div class="layui-input-block" style="padding-top: 10px;">
									<input type="checkbox" name="checkbox" lay-skin="primary"
										value="全选" lay-filter="qx" title="全选/反选">&nbsp;&nbsp;&emsp;<input
										type="checkbox" name="checkbox" lay-skin="primary"
										value="多税种是否小计" lay-filter="sz_one" title="多税种是否小计"> <br />
									<input type="checkbox" name="checkbox" lay-skin="primary"
										value="增值税" lay-filter="sz_one" title="增值税">&emsp;&emsp;&emsp;<input
										type="checkbox" name="checkbox" lay-skin="primary" value="车船税"
										lay-filter="sz_one" title="车船税" style="padding-left: 36px">&emsp;&emsp;&emsp;<input
										type="checkbox" name="checkbox" lay-skin="primary"
										value="地方教育附加" lay-filter="sz_one" title="地方教育附加"><br />
									<input type="checkbox" name="checkbox" lay-skin="primary"
										value="营改增增值税" lay-filter="sz_one" title="营改增增值税"><input
										type="checkbox" name="checkbox" lay-skin="primary" value="房产税"
										lay-filter="sz_one" title="房产税">&emsp;&emsp;&emsp;<input
										type="checkbox" name="checkbox" lay-skin="primary"
										value="城市维护建设税" lay-filter="sz_one" title="城市维护建设税"><br />
									<input type="checkbox" name="checkbox" lay-skin="primary"
										value="营业税" lay-filter="sz_one" title="营业税">&emsp;&emsp;&emsp;<input
										type="checkbox" name="checkbox" lay-skin="primary" value="印花税"
										lay-filter="sz_one" title="印花税">&emsp;&emsp;&emsp;<input
										type="checkbox" name="checkbox" lay-skin="primary"
										value="城镇土地使用税" lay-filter="sz_one" title="城镇土地使用税"><br />
									<input type="checkbox" name="checkbox" lay-skin="primary"
										value="企业所得税" lay-filter="sz_one" title="企业所得税">&emsp;<input
										type="checkbox" name="checkbox" lay-skin="primary" value="环保税"
										lay-filter="sz_one" title="环保税"> <br />
									<input type="checkbox" name="checkbox" lay-skin="primary"
										value="个人所得税" lay-filter="sz_one" title="个人所得税">&emsp;<input
										type="checkbox" name="checkbox" lay-skin="primary"
										value="教育附加" lay-filter="sz_one" title="教育附加">
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<div class="layui-block ">
								<label class="layui-form-label" style="width: 80px;">口径</label>
								<div class="layui-input-block" style="padding-top: 10px;">
									<input type="checkbox" name="checkbox" lay-filter="kj"
										lay-skin="primary" value="地方口径" title="地方口径" checked="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input 
										type="checkbox" name="checkbox" lay-filter="kj" lay-skin="primary" value="全口径" title="全口径">
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<label class="layui-form-label" style="width: 80px;">其他</label>
							<div class="layui-input-block" style="padding-top: 10px;">
								<input type="checkbox" name="checkbox" lay-skin="primary"
									title="同期" value="同期" lay-filter="qt">
							</div>
							<div class="layui-input-block">
								<input type="checkbox" name="checkbox" lay-skin="primary"
									value="所选年份地方口径" lay-filter="qt" title="所选年份地方口径">
							</div>
							<div class="layui-input-block">
								<input type="checkbox" name="checkbox" lay-skin="primary"
									value="街道" lay-filter="qt" title="街道">
							</div>
						</div>
					</div>
				</form>
			</div>
			<div
				style="width: 100%; border: 1px solid #e6e6e6; height: 42.3%; margin-top: 2.5%;">
				<table class="layui-table" style="margin: 0px;">
					<thead>
						<tr>
							<th><strong>定义公式</strong></th>
						</tr>
					</thead>
				</table>
				<div style="width: 100%; padding-left: 5%; margin-top: 3%;">
					<p class="anli">根据查询项的序号来生成公式，如下样例：</p>
					<p class="anli">公式：列1/列2-1</p>
					<p class="anli">名称：2018年01-06月增值税地方口径增长比例</p>
					<br />
					<p>请参考上方的样例在下方输入公式及公式名称：</p>
				</div>
				<form class="layui-form" id="form1" action="">
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<label class="layui-form-label">公式：</label>
							<div class="layui-input-block">
								<input type="text" name="gs" id="gs" autocomplete="off"
									class="layui-input">
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<label class="layui-form-label">名称：</label>
							<div class="layui-input-block">
								<input type="text" name="mc" id="mc" autocomplete="off"
									class="layui-input">
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>

		<div
			style="width: 10%; border: 0px solid #e6e6e6; height: 95%; float: left; margin-left: 10px; text-align: center;">

			<form class="layui-form" action="">
				<div
					style="width: 100%; height: 50%; float: left; text-align: center;">
					<button class="button"
						style="color: white;  top: 54%; right:5.5%; border-radius: 5px 0px 0px 5px;"
						lay-filter="addcx" lay-submit="">添加</button>
				</div>
				<div
					style="width: 100%; height: 45%; float: left; text-align: center;">
					<button class="button"
						style="color: white; top: 54%; right:5.5%; border-radius: 5px 0px 0px 5px;"
						lay-filter="addgs" lay-submit="">添加</button>
				</div>
				<div
					style="width: 100%; height: 10%; float: left; text-align: center;">
					<button class="layui-btn layui-btn-normal" style="top: 45%;"
						lay-filter="scmb" lay-submit="">保存模板</button>
				</div>
			</form>
		</div>
		<div
			style="width: 43%; border: 1px solid #e6e6e6; height: 95%; overflow: auto; float: left; margin-left: 10px;">

			<form class="layui-form" id="form2" action="">
				<table class="layui-table" id="table" lay-filter="user"
					style="margin: 0px;">
					<thead>
						<tr>
							<th style="width: 55px;"><strong>序号</strong></th>
							<th><strong>查询项名称</strong></th>
							<th style="width: 55px;"><strong>操作</strong></th>
						</tr>
					</thead>
					<tbody id="ttbody">

					</tbody>
				</table>
			</form>

		</div>
		<div id="scmain" style="display: none;">
			<form class="layui-form" id="form3" action=""
				style="margin-top: 20px;">
				<input type="hidden" id="data" name="data" />
				<input type="hidden" id="id" name="id" />

				<div class="layui-form-item">
					<label class="layui-form-label">模板名称：</label>
					<div class="layui-input-inline" style="width: 70%;">
						<input type="text" id="mbmc" lay-verify="required" name="mbmc"
							class="layui-input">
					</div>

				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">模板描述：</label>
					<div class="layui-input-inline" style="width: 70%;">
						<textarea placeholder="请输入内容" class="layui-textarea" id="mbms"
							name="mbms"></textarea>
					</div>

				</div>
				<div class="layui-form-item">
					<div class="layui-input-block" style="margin-left: 0px;">
						<div style="text-align: center;">
							<button class="layui-btn layui-btn-normal" id="mbsave"
								type="button" lay-submit="" lay-filter="mbsave">保存</button>
						</div>
					</div>
				</div>

			</form>
		</div>
</body>

<script>
	var sz = "";
	var kj = "地方口径,";
	var qt = "";
	var sclayer;
	var v = 0;
	//初始化Element
	layui.use([ 'element', 'layer' ], function() {
		var element = layui.element;
		var layer = layui.layer;

		//监听折叠
		element.on('collapse(test)', function(data) {
		});
	});
	 function getUrlParam (name) {
	     var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if (r!= null) {
	        return decodeURI(r[2]);
	     }else{
	        return null;
	     }
	}

		var id=getUrlParam("id");
	$(function() {
		var date = new Date();
		var jn = date.getFullYear();
		var str = "";
		for (var i = 0; i < 10; i++) {
			str += "<option value='" + (jn - i) + "'>" + (jn - i) + "</option>";
		}
		$("#year").html(str);
		$.ajax({

			type : "post",
			url : 'mbgl/selectCXX.do',
			data : {id:id},
			xhrFields : {
				withCredentials : true
			},
			crossDomain : true,
			success : function(data) {
				//alert(data);
				console.log(data);
				var el = $.parseJSON(data);	
				if (el.code == '000') {
					var s = "";
					
					for (var i = 0; i < el.data.length; i++) {
						
						v=parseInt(el.data[i].XH.substring(el.data[i].XH.lastIndexOf("序")+2))-1;
						var gs=el.data[i].GS;
						if (el.data[i].GS=="undefined"||el.data[i].GS==undefined||el.data[i].GS==null) {
							gs="";
						}
						s += '<tr title="'+gs +'"><td><input type="hidden" name="xh_'
								+ v
								+ '" value="列'
								+ (v + 1)
								+ '">列'
								+ (v + 1) + '</td>';
						s += '<td><input type="hidden" name="cxxmc_'+v+'" value="' + el.data[i].CXXMC +'"><input type="hidden" name="gs_'+v+'" value="' + el.data[i].GS +'">'
								+ el.data[i].CXXMC
								+ '</td>';
						s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this,\'列' +  (v + 1)+'\')">删除</span></td>';
						s += '</tr>';
					}
					v++;
					$("#ttbody").html(s);
				}

			}
		});
		initmc();
	});
	function initmc() {
		$.ajax({

			type : "post",
			url : 'mbgl/selectMCMS.do',
			data : {id:id},
			xhrFields : {
				withCredentials : true
			},
			crossDomain : true,
			success : function(data) {
				console.log(data);
				
				var el = $.parseJSON(data);	
				if (el.code == '000') {
				$("#mbmc").val(el.data.MBMC);
				$("#mbms").val(el.data.MBMS);
				$("#id").val(id);
				}

			}
		});
	}
	//初始化Element
	layui.use(
					[ 'form' ],
					function() {
						var form = layui.form;

						form.on('checkbox(qx)', function(data) {
							console.log(data.elem); //得到checkbox原始DOM对象
							console.log(data.elem.checked); //是否被选中，true或者false
							console.log(data.value); //复选框value值，也可以通过data.elem.value得到
							var zx = data.elem.parentElement.childNodes;
							for (var i = 0; i < zx.length; i++) {
								if (zx[i].tagName == "INPUT") {
									if (zx[i].checked
											&& zx[i].value != "多税种是否小计") {
										sz = sz.replace(zx[i].value + ",", "");
										$(zx[i]).prop("checked", false);

									} else if (zx[i].value != "多税种是否小计") {
										if (zx[i].value != "全选") {
											sz += zx[i].value + ",";
										}
										$(zx[i]).prop("checked", true);

									}
								}
							}

							form.render('checkbox');
						});
						form
								.on(
										'submit(addcx)',
										function(data) {
											var year = $("#year").val();
											if (year == "" || year == undefined
													|| year == null) {
												layer.msg("请选择年份！");
												return false;
											}
											var monthstart = $("#monthstart")
													.val();
											var monthend = $("#monthend").val();
											if ((monthstart == ""
													|| monthstart == undefined || monthstart == null)
													&& (monthend != ""
															&& monthend != undefined && monthend != null)) {
												layer.msg("请选择起始月！");
												return false;
											}
											if ((monthstart != ""
													&& monthstart != undefined && monthstart != null)
													&& (monthend == ""
															|| monthend == undefined || monthend == null)) {
												layer.msg("请选择截止月！");
												return false;
											}
											if ((monthstart != ""
													&& monthstart != undefined && monthstart != null)
													&& (monthend != ""
															&& monthend != undefined && monthend != null)
													&& parseInt(monthstart) > parseInt(monthend)) {
												layer.msg("起始月不得小于截止月！");
												return false;
											}
											sz = sz.substring(0, sz.length - 1)
											kj = kj.substring(0, kj.length - 1)
											qt = qt.substring(0, qt.length - 1)
											var szarr = sz.split(",");
											var kjarr = kj.split(",");
											var qtarr = qt.split(",");
											var cxmbm = year + "年";
											var qntq = "";
											var cxmbm1 = year + "年";
											var qntq1 = "";
											for (var i = 0; i < qtarr.length; i++) {
												if (qtarr[i] == "同期") {
													qntq += (parseInt(year) - 1)
															+ "年";
													qntq1 += (parseInt(year) - 1)
															+ "年";
												}
											}
											for (var i = 0; i < qtarr.length; i++) {
												if (qtarr[i] == "街道") {

													var s = "";
													s += '<tr><td><input type="hidden" name="xh_'
															+ v
															+ '" value="列'
															+ (v + 1)
															+ '">列'
															+ (v + 1) + '</td>';
													s += '<td><input type="hidden" name="cxxmc_'+v+'" value="' + "所属街道"
												+ '">'
															+ "所属街道"
															+ '</td>';
													s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this,\'列' +  (v + 1)+'\')">删除</span></td>';
													s += '</tr>';
													v++;
													$("#ttbody").append(s);	
												}
											}
											for (var i = 0; i < qtarr.length; i++) {
												if (qtarr[i] == "所选年份地方口径") {

													var s = "";
													s += '<tr><td><input type="hidden" name="xh_'
															+ v
															+ '" value="列'
															+ (v + 1)
															+ '">列'
															+ (v + 1) + '</td>';
													s += '<td><input type="hidden" name="cxxmc_'+v+'" value="' + cxmbm + "地方口径"
												+ '">'
															+ cxmbm
															+ "地方口径"
															+ '</td>';
													s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this,\'列' +  (v + 1)
													+'\')">删除</span></td>';
													s += '</tr>';
													v++;
													$("#ttbody").append(s);
													s = "";
													if (qntq != "") {
														s = "";
														s += '<tr><td><input type="hidden" name="xh_'
																+ v
																+ '" value="列'
																+ (v + 1)
																+ '">列'
																+ (v + 1)
																+ '</td>';
														s += '<td><input type="hidden" name="cxxmc_'+v+'" value="' + qntq+"同期" + "地方口径"
													+ '">'
																+ qntq+"同期" 
																+ "地方口径"
																+ '</td>';
														s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this,\'列' +  (v + 1)
														+'\')">删除</span></td>';
														s += '</tr>';
														v++;
														$("#ttbody").append(s);

														s = "";
													}
												}
											}
											if (monthstart != ""
													&& monthstart != undefined
													&& monthstart != null) {
												if (qntq != "") {
													if (monthstart == monthend) {
														qntq += monthstart
																
																+ "月";
														qntq1 += monthstart
														
														+ "月";
													} else {
														qntq += monthstart
																+ "-"
																+ monthend
																		
																+ "月";
														qntq1 += monthstart
														+ "-"
														+ monthend
																
														+ "月";
													}

												}
												if (monthstart == monthend) {
													cxmbm += monthstart + "月";
													cxmbm1 += monthstart + "月";
												} else {
													cxmbm += monthstart + "-"
															+ monthend + "月";
													cxmbm1 += monthstart + "-"
													+ monthend + "月";
												}

											}else{
												if (qntq != "") {
													qntq+="同期";
													qntq1+="同期";
													}
											}
											for (var i = 0; i < szarr.length; i++) {
												if (szarr[i] != "多税种是否小计") {
													cxmbm1 += szarr[i] + "、";
													if (qntq != "") {
														qntq1 += szarr[i] + "、";
													}

												}

											}

											for (var i = 0; i < kjarr.length; i++) {
												var s = "";

												s = "";
												for (var j = 0; j < szarr.length; j++) {
													if (szarr[j] == "多税种是否小计") {
														if (qntq != "") {
															s += '<tr><td><input type="hidden" name="xh_'
																	+ v
																	+ '" value="列'
																	+ (v + 1)
																	+ '">列'
																	+ (v + 1)
																	+ '</td>';
															s += '<td><input type="hidden" name="cxxmc_'
																	+ v
																	+ '" value="'
																	+ qntq1
																			.substring(
																					0,
																					qntq1.length - 1)
																	+ kjarr[i]
																	+ '小计">'
																	+ qntq1
																			.substring(
																					0,
																					qntq1.length - 1)
																	+ kjarr[i]
																	+ '小计</td>';
															s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this,\'列' +  (v + 1)+'\')">删除</span></td>';
															s += '</tr>';
															v++;
															$("#ttbody")
																	.append(s);

															s = "";
														}
														s = "";
														s += '<tr><td><input type="hidden" name="xh_'
																+ v
																+ '" value="列'
																+ (v + 1)
																+ '">列'
																+ (v + 1)
																+ '</td>';
														s += '<td><input type="hidden" name="cxxmc_'
																+ v
																+ '" value="'
																+ cxmbm1
																		.substring(
																				0,
																				cxmbm1.length - 1)
																+ kjarr[i]
																+ '小计">'
																+ cxmbm1
																		.substring(
																				0,
																				cxmbm1.length - 1)
																+ kjarr[i]
																+ '小计</td>';
														s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this,\'列' +  (v + 1)+'\')">删除</span></td>';
														s += '</tr>';
														v++;
														$("#ttbody").append(s);

														s = "";
													} else {
														s = "";
														if (qntq != "") {
															s += '<tr><td><input type="hidden" name="xh_'
																	+ v
																	+ '" value="列'
																	+ (v + 1)
																	+ '">列'
																	+ (v + 1)
																	+ '</td>';
															s += '<td><input type="hidden" name="cxxmc_'
																	+ v
																	+ '" value="'
																	+ qntq+szarr[j]
																	+ kjarr[i]
																	+ '">'
																	+ qntq
																	+ szarr[j]
																	+ kjarr[i]
																	+ '</td>';
															s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this,\'列' +  (v + 1)+'\')">删除</span></td>';
															s += '</tr>';
															v++;
															$("#ttbody")
																	.append(s);

															s = "";
														}
														s = "";
														s += '<tr><td><input type="hidden" name="xh_'
																+ v
																+ '" value="列'
																+ (v + 1)
																+ '">列'
																+ (v + 1)
																+ '</td>';
														s += '<td><input type="hidden" name="cxxmc_'
															+ v
															+ '" value="'
															+ cxmbm+szarr[j]
															+ kjarr[i]
															+ '">'
																+ cxmbm
																+ szarr[j]
																+ kjarr[i]
																+ '</td>';
														s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this,\'列' +  (v + 1)+'\')">删除</span></td>';
														s += '</tr>';
														v++;
														$("#ttbody").append(s);

														s = "";
													}

												}

											}
											var checks = $("input[name='checkbox']");
											for (var i = 0; i < checks.length; i++) {
												if (checks[i].value == "地方口径") {
													$(checks[i]).prop(
															"checked", true);
												} else {
													$(checks[i]).prop(
															"checked", false);
												}
											}
											form.render('checkbox');
											sz = "";
											kj = "地方口径,";
											qt = "";
											$("#monthstart").val('');
											$("#monthend").val('');
											var date = new Date();
											var jn = date.getFullYear();
											$("#year").val(jn);
											form.render();
											return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
										});
						form.on('submit(scmb)', function(data) {
							var list = $("#form2").serializeArray();
							if (list.length == 0) {
								layer.msg("请先添加查询项！");
								layer.close(sclayer);
								return false;
							}
							sclayer = layer.open({
								type : 1,
								title : '生成模板',
								area : [ '500px', '300px' ],
								shadeClose : false, //点击遮罩关闭
								content : $('#scmain'),
								success : function() {

									var data = [];
									for (var i = 0; i < list.length; i++) {
										var name = list[i].name;
										var index = name.substring(name
												.lastIndexOf("_") + 1);
										name = name.substring(0, name
												.lastIndexOf("_"));
										var json = {};
										if (name == "xh") {
											json.xh = list[i].value;
										}
										for (var j = 0; j < list.length; j++) {
											var name1 = list[j].name;
											var index1 = name1.substring(name1
													.lastIndexOf("_") + 1);
											name1 = name1.substring(0, name1
													.lastIndexOf("_"));
											if (index == index1
													&& name1 != name) {
												if (name1 == "cxxmc") {
													json.cxxmc = list[j].value;
												} else if (name1 == "gs") {
													json.gs = list[j].value;
												}
											}
										}
										if (json.xh != undefined) {
											data.push(json);
										}
									}
									console.log(JSON.stringify(data));
									$("#data").val(JSON.stringify(data));
								},
								cancel : function() {
									$('#scmain').hide();
									return;
								},
								end : function() {
									$('#scmain').hide();
								}
							});
							return false;
						});
						form.on('submit(addgs)',function(data) {
											var gs = $("#gs").val();
											if (gs == "" || gs == undefined
													|| gs == null) {
												layer.msg("请输入公式！");
												return false;
											}
											var mc = $("#mc").val();
											if (mc == "" || mc == undefined
													|| mc == null) {
												layer.msg("请输入名称！");
												return false;
											}
											var newregexp = new RegExp(
													"^[^\\+\\-\\*\\/][0-9列\\+\\-\\*/\\(\\)]{3,100}$");
											if (!newregexp.test(gs)) {
												layer.msg("公式格式错误！");
												return false;
											}
											var s = "";
											s += '<tr title="'+gs+'"><td><input type="hidden" name="xh_'
													+ v
													+ '" value="列'
													+ (v + 1)
													+ '">列'
													+ (v + 1)
													+ '</td>';
											s += '<td><input type="hidden" name="cxxmc_'+v+'" value="' + mc+ '"><input type="hidden" name="gs_'+v+'" value="' + gs+ '">'
													+ mc + '</td>';
											s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this,\'列' +  (v + 1)+'\')">删除</span></td>';
											s += '</tr>';
											v++;
											$("#ttbody").append(s);
											$("#mc").val('');
											$("#gs").val('');
											return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
										});

						form.on('submit(mbsave)', function(data) {
							loadlayer = layer.load();
							var mc = $("#mbmc").val();
							if (mc == "" || mc == undefined || mc == null) {
								layer.close(loadlayer);
								layer.msg("请输入名称！");
								return false;
							}
							var ms = $("#mbms").val();
							if (ms == "" || ms == undefined || ms == null) {
								layer.close(loadlayer);
								layer.msg("请输入描述！");
								return false;
							}
							
							$.ajax({
								type : "post", //请求方式
								async : false, //是否异步
								url : "mbgl/updateCXMB.do",
								data : $("#form3").serialize(),
								dataType : "json",
								success : function(obj) {
									layer.close(loadlayer);
									if (obj != null && obj.code == '000') {
										layer.msg("修改成功！");
										layer.close(sclayer);
										setTimeout(function() {
											parent.location.reload();
										}, 3000);
									} else {
										layer.msg("生成失败！");
									}
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
							return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
						});
						form.on('checkbox(sz_one)', function(data) {
							console.log(data.elem); //得到checkbox原始DOM对象
							console.log(data.elem.checked); //是否被选中，true或者false
							console.log(data.value); //复选框value值，也可以通过data.elem.value得到
							console.log(data.othis); //得到美化后的DOM对象
							var a = data.elem.checked;
							if (a == true) {
								sz += data.value + ",";
							} else {
								sz = sz.replace(data.value + ",", "");
							}
						});

						form.on('checkbox(kj)', function(data) {
							console.log(data.elem); //得到checkbox原始DOM对象
							console.log(data.elem.checked); //是否被选中，true或者false
							console.log(data.value); //复选框value值，也可以通过data.elem.value得到
							console.log(data.othis); //得到美化后的DOM对象
							var a = data.elem.checked;
							if (a == true) {
								kj += data.value + ",";
							} else {
								kj = kj.replace(data.value + ",", "");
							}
						});
						form.on('checkbox(qt)', function(data) {
							console.log(data.elem); //得到checkbox原始DOM对象
							console.log(data.elem.checked); //是否被选中，true或者false
							console.log(data.value); //复选框value值，也可以通过data.elem.value得到
							console.log(data.othis); //得到美化后的DOM对象
							var a = data.elem.checked;
							if (a == true) {
								qt += data.value + ",";
							} else {
								qt = qt.replace(data.value + ",", "");
							}
						});

					});

	function deletes(obj,xh) {
		console.log(obj);
		obj.parentNode.parentNode.parentNode
				.removeChild(obj.parentNode.parentNode);
	}
</script>

</html>