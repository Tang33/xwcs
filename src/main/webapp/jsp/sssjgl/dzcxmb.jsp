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
</style>


</head>

<body style="overflow-x: hidden">
	<div class="layui-container" style="margin-top: 15px;">
		<div style="width: 43%; height: 95%; float: left;">
			<div style="width: 100%; border: 1px solid #e6e6e6; height: 55%;">
				<form class="layui-form" id="form" action="">
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<div class="layui-block ">
								<label class="layui-form-label">年份</label>
								<div class="layui-input-block">
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
								<label class="layui-form-label">月份起止</label>
								<div class="layui-input-inline layui-col-md3">
									<select name="zdsyh" id="zdsyh">
										<option value="">请选择</option>
										<option value="1">01</option>
										<option value="2">02</option>
										<option value="3">03</option>
										<option value="4">04</option>
										<option value="5">05</option>
										<option value="6">06</option>
										<option value="7">07</option>
										<option value="8">08</option>
										<option value="9">09</option>
										<option value="10">10</option>
										<option value="11">11</option>
										<option value="12">12</option>
									</select>
								</div>

								<label class="layui-form-label" style="width: 30px;">月起</label>
								<div class="layui-input-inline layui-col-md3">
									<select name="zdsyh" id="zdsyh">
										<option value="">请选择</option>
										<option value="1">01</option>
										<option value="2">02</option>
										<option value="3">03</option>
										<option value="4">04</option>
										<option value="5">05</option>
										<option value="6">06</option>
										<option value="7">07</option>
										<option value="8">08</option>
										<option value="9">09</option>
										<option value="10">10</option>
										<option value="11">11</option>
										<option value="12">12</option>
									</select>
								</div>
								<label class="layui-form-label" style="width: 30px;">月止</label>
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<div class="layui-block ">
								<label class="layui-form-label">税种</label>
								<div class="layui-input-block" style="padding-top: 10px;">
									<input type="checkbox" name="qx" lay-skin="primary" value="全选"
										lay-filter="qx" title="全选/反选"> <input type="checkbox"
										name="sfxj" lay-skin="primary" value="多税种是否小计"
										lay-filter="sz_one" title="多税种是否小计"> <input
										type="checkbox" name="zzs" lay-skin="primary" value="增值税"
										lay-filter="sz_one" title="增值税"> <input
										type="checkbox" name="ccz" lay-skin="primary" value="车船税"
										lay-filter="sz_one" title="车船税"> <input
										type="checkbox" name="dfjyfj" lay-skin="primary"
										value="地方教育附加" lay-filter="sz_one" title="地方教育附加"> <input
										type="checkbox" name="ygzzzs" lay-skin="primary"
										value="营改增增值税" lay-filter="sz_one" title="营改增增值税"> <input
										type="checkbox" name="fcs" lay-skin="primary" value="房产税"
										lay-filter="sz_one" title="房产税"> <input
										type="checkbox" name="cswhjss" lay-skin="primary"
										value="城市维护建设税" lay-filter="sz_one" title="城市维护建设税">
									<input type="checkbox" name="yys" lay-skin="primary"
										value="营业税" lay-filter="sz_one" title="营业税"> <input
										type="checkbox" name="yhs" lay-skin="primary" value="印花税"
										lay-filter="sz_one" title="印花税"> <input
										type="checkbox" name="cztdsys" lay-skin="primary"
										value="城镇土地使用税" lay-filter="sz_one" title="城镇土地使用税">
									<input type="checkbox" name="qysds" lay-skin="primary"
										value="企业所得税" lay-filter="sz_one" title="企业所得税"> <input
										type="checkbox" name="hbs" lay-skin="primary" value="环保税"
										lay-filter="sz_one" title="环保税"> <input
										type="checkbox" name="grsds" lay-skin="primary" value="个人所得税"
										lay-filter="sz_one" title="个人所得税"> <input
										type="checkbox" name="jyfj" lay-skin="primary" value="教育附加"
										lay-filter="sz_one" title="教育附加">
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<div class="layui-block ">
								<label class="layui-form-label">口径</label>
								<div class="layui-input-block" style="padding-top: 10px;">
									<input type="checkbox" name="dfkj" lay-filter="kj"
										lay-skin="primary" value="地方口径" title="地方口径" checked="">
									<input type="checkbox" name="qkj" lay-filter="kj"
										lay-skin="primary" value="全口径" title="全口径">
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<label class="layui-form-label">其他</label>
							<div class="layui-input-block" style="padding-top: 10px;">
								<input type="checkbox" name="tq" lay-skin="primary" title="同期"
									value="同期" lay-filter="qt">
							</div>
							<div class="layui-input-block">
								<input type="checkbox" name="sxnfljdfkj" lay-skin="primary"
									value="所选年份累计地方口径" lay-filter="qt" title="所选年份累计地方口径">
							</div>
						</div>
					</div>
				</form>
			</div>
			<div
				style="width: 100%; border: 1px solid #e6e6e6; height: 42.3%; margin-top: 2.5%;">
				<div style="width: 100%; padding-left: 10%; margin-top: 10%;">
					<p>根据查询项的序号来生成公式,如下样例:</p>
					<p>公式:列1/列2-1</p>
					<p>名称:2018年1-6月增值税地方口径增长比例</p>
					<br />
					<p>请参考上方的样例在下方输入公式及公式名称：</p>
				</div>
				<form class="layui-form" id="form1" action="">
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<label class="layui-form-label">公式:</label>
							<div class="layui-input-block">
								<input type="text" name="gs" id="gs" autocomplete="off"
									class="layui-input">
							</div>
						</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md10" style="margin-top: 10px;">
							<label class="layui-form-label">名称:</label>
							<div class="layui-input-block">
								<input type="text" name="mc" id="mc" autocomplete="off"
									class="layui-input">
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>

		<div style="width: 10%; height: 95%; float: left; margin-left: 10px; text-align: center;">
			<form class="layui-form" action="">
				<div
					style="width: 100%; height: 50%; float: left; text-align: center;">
					<button class="button"
						style="color: white; top: 25%; border-radius: 5px 0px 0px 5px;"
						lay-filter="addcx" lay-submit="">添加</button>
				</div>
				<div
					style="width: 100%; height: 45%; float: left; text-align: center;">
					<button class="button"
						style="color: white; top: 45%; border-radius: 5px 0px 0px 5px;"
						lay-filter="addgs" lay-submit="">添加</button>
				</div>
				<div
					style="width: 100%; height: 10%; float: left; text-align: center;">
					<button class="layui-btn layui-btn-normal" style="top: 45%;"
						lay-filter="scmb" lay-submit="">生成模板</button>
				</div>
			</form>
		</div>
		<div
			style="width: 43%; border: 1px solid #e6e6e6; height: 95%; overflow: auto; float: left; margin-left: 10px;">
			<table class="layui-table" id="table" lay-filter="user" style="margin: 0px">
				<thead>
					<tr>
						<th style="width: 50px;">序号</th>
						<th >查询项名称</th>
						<th style="width: 50px;">操作</th>
					</tr>
				</thead>
				<tbody id="ttbody">

				</tbody>
			</table>


		</div>
</body>

<script>
	var sz = "";
	var kj = "地方口径,";
	var qt = "";
	//初始化Element
	layui.use([ 'element', 'layer' ], function() {
		var element = layui.element;
		var layer = layui.layer;

		//监听折叠
		element.on('collapse(test)', function(data) {
		});
	});
	//初始化Element
	layui.use([ 'form' ],function() {
						var form = layui.form;

						form.on('checkbox(qx)', function(data) {
							console.log(data.elem); //得到checkbox原始DOM对象
							console.log(data.elem.checked); //是否被选中，true或者false
							console.log(data.value); //复选框value值，也可以通过data.elem.value得到
							var zx = data.elem.parentElement.childNodes;
							for (var i = 0; i < zx.length; i++) {
								if (zx[i].tagName == "INPUT") {
									if (zx[i].checked) {
										sz = sz.replace(zx[i].value + ",", "");
										$(zx[i]).prop("checked", false);

									} else {
										if (zx[i].value != "全选") {
											sz += zx[i].value + ",";
										}
										$(zx[i]).prop("checked", true);

									}
								}
							}

							form.render('checkbox');
						});
						var v = 0;
						form.on('submit(addcx)',function(data) {
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
														&&  monthend != undefined && monthend != null)) {
												layer.msg("请选择截止月！");
												return false;
											}
											if ((monthstart != ""
												&& monthstart != undefined && monthstart != null)
													&& (monthend == ""
															|| monthend == undefined || monthend == null)) {
												layer.msg("请选择起始月！");
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
											for (var i = 0; i < qtarr.length; i++) {
												if (qtarr[i] == "同期") {
													qntq += (parseInt(year) - 1)
															+ "年";
												} 
											}
											for (var i = 0; i < qtarr.length; i++) {
												if (qtarr[i] == "所选年份累计地方口径") {
													
												var s = "";
												s += '<tr><td>列' + (v + 1)
														+ '</td>';
												s += '<td>' + cxmbm + "累计地方口径"
														+ '</td>';
														s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this)">删除</span></td>';
												s += '</tr>';
												v++;
												$("#ttbody").append(s);
												if (qntq!="") {
													var s = "";
													s += '<tr><td>列' + (v + 1)
															+ '</td>';
													s += '<td>' + qntq + "累计地方口径"
															+ '</td>';
															s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this)">删除</span></td>';
													s += '</tr>';
													v++;
													$("#ttbody").append(s);
												}
												}
											}
											if (monthstart != ""
													&& monthstart != undefined
													&& monthstart != null) {
												cxmbm += monthstart.replace(
														"0", "")
														+ "-"
														+ monthend.replace("0",
																"") + "月";
											}
											for (var i = 0; i < szarr.length; i++) {

												cxmbm += szarr[i] + "、";
												if (qntq != "") {
													qntq += szarr[i] + "、";
												}

											}

											for (var i = 0; i < kjarr.length; i++) {
												var s = "";
												if (qntq != "") {
													s += '<tr><td>列' + (v + 1)
															+ '</td>';
													s += '<td>' + qntq.substring(0,qntq.length-1)
															+ kjarr[i]
															+ '</td>';
															s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this)">删除</span></td>';
													s += '</tr>';
													v++;
													$("#ttbody").append(s);
												} 
												s = "";
												s += '<tr><td>列' + (v + 1)
												+ '</td>';
										s += '<td>' + cxmbm.substring(0,cxmbm.length-1)
												+ kjarr[i]
												+ '</td>';
												s += '<td><span style="color:red;cursor:pointer;" onclick="deletes(this)">删除</span></td>';
										s += '</tr>';
												v++;
												$("#ttbody").append(s);
											}
											
											return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
										});
						form.on('submit(addgs)', function(data) {
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
	
	function deletes(obj) {
		console.log(obj);
		obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
	}
</script>

</html>