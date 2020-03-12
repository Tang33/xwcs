<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

<title>流程管理—企业迁出管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<script src="./css/layui/layui.js" charset="utf-8"></script>
<style type="text/css">
.sweet-alert {
	z-index: 99999999;
	border: 1px solid #eee;
	box-shadow: 0 0 3px #c7b6b6;
}
</style>
</head>

<body style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">本功能用于对系统的“企业迁出审核”进行操作管理！</blockquote>
	<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md4">
							<label class="layui-form-label">纳税人名称</label>
							<div class="layui-input-block">
								<input id="paramNsrmc" name="paramNsrmc" type="text"
									placeholder="请输入纳税人名称" class="layui-input">
							</div>
						</div>
						<div class="layui-col-md4">
							<label class="layui-form-label" style="width: 100px;">纳税人识别号</label>
							<div class="layui-input-inline">
								<input id="paramNsrsbh" name="paramNsrsbh" type="text"
									placeholder="请输入纳税人识别号" class="layui-input">
								
									
							</div>
							<div class="layui-inline" style="padding-left: 20px;">
								<a lay-submit class="layui-btn" id="search_btn"
									lay-filter="search_btn">查询</a>

							</div>

							

						</div>

					</div>
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>

			<table class="layui-table" id="qrtable" lay-filter="qrtable">

			</table>
			<div id="page1"></div>
			<script type="text/html" id="bar">
          {{# if(${sessionScope.dwid != '00'} && d.STATUS == "0"){ }}
  				  <a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="check">审核</a>
          {{# } }}
         
			
          {{# if(${sessionScope.dwid == '00'} ){ }}
  				  <a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="remove">删除</a>
          {{# } }}
         
		   {{# if(d.FILEURL){ }}
  				  <a href="{{d.FILEURL}}" class="layui-btn layui-btn-normal layui-btn-sm" lay-event="downloadFile">附件下载</a>
          {{# } }}	
				</script>
		</fieldset>
	</form>

	<div id="table-qy-div" class="layui-hide" style="padding: 10px;">
		<form class="layui-form" id="qy_form" action="">
			<div class="layui-row">
				<div class="layui-col-md8">
					<label class="layui-form-label">企业名称:</label>
					<div class="layui-input-block">
						<input type="text" class="layui-input" id="qymc" name="qymc"
							placeholder="">
					</div>
				</div>
				<div class="layui-col-md3" style="margin-left: 20px;">
					<button lay-submit class="layui-btn" id="search_qy_btn"
						lay-filter="search_qy_btn">查询</button>
				</div>
			</div>
		</form>
		<table class="layui-table" id="table-qy" lay-filter="table-qy" >

		</table>
	</div>




	<div id="checkmain" class="layui-hide" style="padding: 10px">
		<form class="layui-form" id="check_form">
			<input type="hidden" name="id" id="id" />
			<div class="layui-form-item">
				<label class="layui-form-label">审核意见</label>
				<div class="layui-input-block">
					<input type="radio" name="shyj" value="同意" title="同意" checked>
					<input type="radio" name="shyj" value="不同意" title="不同意">
				</div>
			</div>
			<div class="layui-form-item" style="padding-left: 0px; width: 400px">
				<label class="layui-form-label">备注</label>
				<div class="layui-input-block">
					<textarea rows="" cols="" class="layui-textarea" id="shly"
						name="shly">
	          </textarea>
				</div>
			</div>
			<div class="layui-form-item" style="padding-left: 340px">
				<button lay-submit class="layui-btn" id="check_save_btn"
					lay-filter="check_save_btn">保存</button>
			</div>
		</form>
	</div>

	<div id="checkdetailmain" class="layui-hide" style="padding: 10px;">
		<form class="layui-form" id="check_form">

			<div class="layui-form-item">
				<label class="layui-form-label">纳税人识别号</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="NSRSBH-span"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">纳税人名称</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="NSRMC-span"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">所属街道</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="SSJD-span"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">迁出原因</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="QCYY-span"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">拟迁入地</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="NQRD-span"></span>
				</div>
			</div>


			<div class="layui-form-item">
				<label class="layui-form-label">审核意见</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="shyj-span"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">备注</label>
				<div class="layui-input-block" style="line-height: 36px; width: 70%">
					<span id="shyy-span" style="word-wrap: anywhere;"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">审核人</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="fkr-span"></span>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">审核日期</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="fkrq-span"></span>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">发布人</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="fbr-span"></span>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">发布日期</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="fbrq-span"></span>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">上月税收情况资本</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="SYSSQK"></span>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">上月税收排名</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="SYSSPM"></span>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">上月税收贡献比例</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="SYSSGXBL"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">注册资本</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="ZCZB-span"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">注册地址</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="ZCDZ-span"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">当年累计税款</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="DNLJSK-span"></span>
				</div>
			</div>

			<div class="layui-form-item">
				<label class="layui-form-label">去年累计税款</label>
				<div class="layui-input-block" style="line-height: 36px;">
					<span id="QNLJSK-span"></span>
				</div>
			</div>


		</form>
	</div>

</body>

<script>
	var loadlayer;
	 


	layui.use([ 'form', 'laydate' ], function() {

		var form = layui.form, layer = layui.layer, laydate = layui.laydate;

		laydate.render({
			elem : '#qrrq' //指定元素
		});

		form.render('select');

		form.on('submit(search_btn)', function() {
			loadlayer = layer.load();
			ajax({
				url : "ajax.do?ctrl=qyqccl_queryQyqc",
				data : {
					"nsrsbh" : $('input[name="paramNsrsbh"]').val(),
					"nsrmc" : $('input[name="paramNsrmc"]').val()
				},
				type : 'post',
				success : function(obj) {
					console.log(obj);
					getTable(obj.data);
					layer.close(loadlayer);
					layer.msg('查询成功！');
				}
			});
			return false;
		});

		form.on('submit(search_qy_btn)', function() {
			loadlayer = layer.load();
			console.log("search_qy_btn------------");
			ajax({
				url : "ajax.do?ctrl=qyqccl_queryQymc",
				data : {
					"qymc" : $('input[name="qymc"]').val(),
				},
				type : 'post',
				success : function(obj) {
					console.log(obj);
					if (obj != null && obj.data != null) {
						//$("#table-qy-div").show();
						var sjList = obj.data;
						console.log(sjList);
						getQyTable(sjList);
						layer.close(loadlayer);
					}
				}
			});
			return false;
		});

		form.on('submit(check_save_btn)', function(obj) {
			console.log(obj.field);
			ajax({
				url : "ajax.do?ctrl=qyqccl_checkQyqc",
				data : obj.field,
				type : 'post',
				success : function(obj) {
					console.log(obj);
					if (obj.code == '000') {
						layer.closeAll();
						layer.msg('保存成功！');
						getData();
					} else {
						layer.msg('保存失败！');
					}
				}
			});
			return false;
		});

	});

	$(function() {
		getData();
	});

	function getQyTable(data) {
		//执行渲染
		layui.use([ 'table' ], function() {
			layui.table.render({
				elem : '#table-qy',
				height : 500, //数据接口
				data : data,
				page : true,
				cols : [ [ //表头
				{
					field : 'NSRMC',
					title : '企业名称',
					width : '80%'
				}, {
					field : '',
					title : '操作',
					toolbar : '#select-bar',
					fixed : 'right',
					width : '20%'
				} ] ]
			});

			//方法级渲染
			//监听工具条
			layui.table.on('tool(table-qy)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
				console.log('listen-table-qy----------------');

				var data = obj.data; //获得当前行数据

				var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）

			});

		});
	}

	function getData() {
		ajax({
			url : "ajax.do?ctrl=qyqccl_queryQyqc",
			data : {
				"nsrmc" : $("#paramNsrmc").val(),
				"nsrsbh" : $("#paramNsrsbh").val(),
			},
			type : 'post',
			success : function(obj) {
				console.log(obj);
				if (obj != null && obj.data != null) {
					var sjList = obj.data;
					console.log(sjList);
					getTable(sjList);
					//layer.close(loadlayer);
				}
			}
		});
	}

	function getTable(data) {
		layui
				.use(
						'table',
						function() {
							var table = layui.table;

							//第一个实例
							table
									.render({
										elem : '#qrtable',
										height : 500,
										
										data : data //数据
										,
										page : true //开启分页
										,
										width: document.body.clientWidth,
										cols : [ [ //表头
												{
													field : 'ID',
													title : 'ID',
													width : 40,
													fixed : 'left'
												},
												{
													field : 'NSRSBH',
													title : '纳税人识别号',
													width : 120
												},
												{
													field : 'NSRMC',
													title : '纳税人名称',
													width : 250
												},
												{
													field : 'JDMC',
													title : '所属街道',
													width : 100
												},
												{field: 'SYSSQK', title: '上月税收情况', width:130,align:'right'} ,
									           	{field: 'SYSSPM', title: '上月税收排名', width:130} ,
									            {field: 'SYSSGXBL', title: '上月税收贡献比例', width:150,align:'right',
									           		templet: function (d) {
									           			if(d.SYSSGXBL!='undefined'&&d.SYSSGXBL!=''&&d.SYSSGXBL!=undefined){
									            			return d.SYSSGXBL+'%'
									            		}else{
									            			return''
									            		}
								            		
								            		}}, 
												{
													field : 'ZCZB',
													title : '注册资本',
													width : 120
													,align:'right'
												},
												{
													field : 'ZCDZ',
													title : '现注册地址',
													width : 130
												},
												{
													field : 'NQRD',
													title : '拟迁入地',
													width : 130
												},
												{
													field : 'DNLJSK',
													title : '当年累计税款',
													width : 120
													,align:'right'
												},
												{
													field : 'QNLJSK',
													title : '去年累计税款',
													width : 120
													,align:'right'
												},
												{
													field : 'FBRQ',
													title : '发布日期',
													width : 150,
													templet : function(data) {
														var fbrq = '';
														if (data.FBRQ != undefined
																& data.FBRQ != "undefined"
																& data.FBRQ != null) {
															fbrq = data.FBRQ
																	.substring(
																			0,
																			10);
														}
														return fbrq;
													}
												},
												{
													field : 'STATUS',
													title : '状态',
													width : 80,
													templet : function(data) {
														console
																.log(data.STATUS);
														if (data.STATUS == '0'
																&& data.ZSZT == 'N') {
															return "待审核";
														} else if (data.STATUS == '1'
																&& data.ZSZT == 'N') {
															return "已审核";
														} else if (data.STATUS == '1'
																&& data.ZSZT == 'Y') {
															return "已终审";
														}
													}
												}, {
													field : '',
													title : '操作',
													toolbar : '#bar',
													fixed : 'right',
													width : 230
												} ] ]
									});

							var num = 0;
							layui.table
									.on(
											'row(qrtable)',
											function(obj) {
												if (num == 1) {
													num = 0;
													return false;
												}
												var data = obj.data;
												$('#checkdetailmain')
														.removeClass(
																'layui-hide');
												console.log(obj.data);
												var fkrq = '';
												if (obj.data.FKRQ != undefined
														& obj.data.FKRQ != "undefined"
														& obj.data.FKRQ != null) {
													fkrq = obj.data.FKRQ
															.substring(0, 10);
												}
												var fbrq = '';
												if (obj.data.FBRQ != undefined
														& obj.data.FBRQ != "undefined"
														& obj.data.FBRQ != null) {
													fbrq = obj.data.FBRQ
															.substring(0, 10);
												}

												$('#NSRSBH-span').text(
														obj.data.NSRSBH);
												$('#NSRMC-span').text(
														obj.data.NSRMC);
												$('#SSJD-span').text(
														obj.data.JDMC);
												$('#QCYY-span').text(
														obj.data.QCYY);
												$('#NQRD-span').text(
														obj.data.NQRD);

												$('#shyj-span').text(
														obj.data.SHYJ);
												$('#shyy-span').text(
														obj.data.LY);
												$('#fkr-span').text(
														obj.data.FKZ);
												$('#fkrq-span').text(fkrq);
												$('#fbr-span').text(
														obj.data.FBZ);
												$('#fbrq-span').text(fbrq);
												$('#SYSSQK').text(obj.data.SYSSQK);
									        	  $('#SYSSPM').text(obj.data.SYSSPM);
									        	  if(obj.data.SYSSGXBL!=undefined&&obj.data.SYSSGXBL!=''&&obj.data.SYSSGXBL!='undefined'){
									        		  $('#SYSSGXBL').text(obj.data.SYSSGXBL+"%");
								            		}
									        	  
												$('#ZCZB-span').text(
														obj.data.ZCZB);
												$('#ZCDZ-span').text(
														obj.data.ZCDZ);
												$('#DNLJSK-span').text(
														obj.data.DNLJSK);
												$('#QNLJSK-span').text(
														obj.data.QNLJSK);
												/*   layer.open({
												  type : 1,
												  title : '任务详情',
												  area : [ '600px', '600px' ],
												  content : $('#checkdetailmain'),
												  success : function() {
												    //initjd();
												  },
												  cancel : function() {
												    $('#checkdetailmain').hide();
												
												  },
												  end : function() {
												    $('#checkdetailmain').hide();
												  }
												}); */

											})

							layui.table
									.on(
											'tool(qrtable)',
											function(obj) {
												var data = obj.data; //获得当前行数据
												var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
												var tr = obj.tr;
												console.log("remove----------");
												if (layEvent === 'remove') {
													layer
															.confirm(
																	'',
																	{
																		icon : 2,
																		title : '删除',
																		area : [
																				'30%',
																				'27%' ],
																		content : '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">确定删除该条企业迁出记录?</div>'
																	},
																	function(
																			index) {
																		//do something
																		ajax({
																			url : "ajax.do?ctrl=qyqccl_removeQyqc",
																			data : {
																				"id" : obj.data.ID
																			},
																			type : 'post',
																			success : function(
																					obj) {
																				console
																						.log(obj);
																				if (obj.code == '000') {
																					getData();
																					layer
																							.msg('删除成功！');
																				} else {
																					layer
																							.msg('删除失败！');
																				}
																				layer
																						.close(index);
																			}
																		});
																	});
												} else if (layEvent === 'downloadFile') {

												} else if (layEvent === 'check') {
													$('#checkmain')
															.removeClass(
																	'layui-hide');
													//form.render();
													$('#check_form #id').val(
															obj.data.ID);
													layer
															.open({
																type : 1,
																title : '任务审核',
																area : [
																		'500px',
																		'300px' ],
																content : $('#checkmain'),
																success : function() {
																	//initjd();
																},
																cancel : function() {
																	$(
																			'#checkmain')
																			.hide();

																},
																end : function() {
																	$(
																			'#checkmain')
																			.hide();
																}
															});

												}
												num = 1;
											});
						});
	}
</script>


</html>
