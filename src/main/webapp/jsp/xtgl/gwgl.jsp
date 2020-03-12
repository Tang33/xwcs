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

<title>系统管理-单位管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />

<link rel="stylesheet" href="./static/layui/css/layui.css" media="all">
<script type="text/javascript" src="./static/js/jquery-2.2.4.min.js"></script>
<script src="./static/layui/layui.all.js" charset="utf-8"></script>

<style type="text/css">
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
}

.layui-form-checkbox span {
	height: auto !important;
}
</style>
</head>

<body>

	<blockquote class="layui-elem-quote layui-text">
		本功能用于对单位进行操作管理！</blockquote>
	<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">

					<div class="layui-row">
						<div class="layui-col-md3" style="width:19%">
							<div class="layui-inline">
								<label class="layui-form-label">单位名称：</label>
								<div class="layui-input-inline">
									<input type="text" name="findtext" id="findtext" autocomplete="off" class="layui-input">
								</div>
							</div>
							<!-- <div class="layui-inline">
									<label class="layui-form-label">单位名称：</label>
									<div class="layui-input-inline">
										<select name="gwsearch" id="gwsearch" lay-search=""
											lay-filter="gwsearch"></select>
									</div>
								</div> -->


						</div>
						<div class="layui-col-md3">
							<div style="text-align: left;">
								<button class="layui-btn layui-btn-normal" id="button"
									type="button" lay-submit="" lay-filter="button">查 询</button>
								<button class="layui-btn layui-btn-normal" id="add"
									type="button" lay-submit="" lay-filter="add">添加单位</button>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>

			<table class="layui-table" id="table" lay-filter="user">
				
				<tbody id="ttbody">

				</tbody>
			</table>
			<div id="page1"></div>
			<script type="text/html" id="bar">
  				<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  				<a class="layui-btn layui-btn-xs" lay-event="qx">设置权限</a>
  				<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>


			</script>
			<script type="text/html" id="zizeng">
              {{d.LAY_TABLE_INDEX+1}}
			</script>
			
		</fieldset>
		
	</form>

	<div id="editmain" style="display: none;">
		<form class="layui-form" id="form5" action=""
			style="margin-top: 20px;">
			<input type="hidden" id="idedit" name="idedit" lay-filter="idedit" />
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"></label>
				<div class="layui-input-inline" style="width: 760px;">
					<div style="text-align: right;">
						<button class="layui-btn layui-btn-normal" id="editsave"
							type="button" lay-submit="" lay-filter="editsave">保存</button>
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"><span
					style="color: red">*</span>单位名称：</label>
				<div class="layui-input-inline">
					<input type="text" id="gwmcedit" lay-verify="required"
						name="gwmcedit" class="layui-input">
				</div>
				<label class="layui-form-label" style="width: 150px;">上级单位：</label>
				<div class="layui-input-inline">
					<select name="sjdwedit" id="sjdwedit" 
						lay-search="" lay-filter="sjdwedit"></select>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"><span
					style="color: red">*</span>单位类型：</label>
				<div class="layui-input-inline">
					<select name="dwlxedit" id="dwlxedit" lay-verify="required"
						lay-search="" lay-filter="dwlxedit">
						<option>请选择单位类型</option>
						<option value="0">区</option>
						<option value="1">街道</option>
					</select>
				</div>
				<label class="layui-form-label" style="width: 150px;"><span
					style="color: red">*</span>单位代码：</label>
				<div class="layui-input-inline">
					<input type="text" id="dwdmedit" lay-verify="required"
						name="dwdmedit" class="layui-input">
				</div>
			</div>
		</form>
	</div>
	<div id="addmain" style="display: none;">
		<form class="layui-form" id="form3" action="" style="margin-top: 20px;">
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"></label>
				<div class="layui-input-inline" style="width: 760px;">
					<div style="text-align: right;">
						<button class="layui-btn layui-btn-normal" id="dosql"
							type="button" lay-submit="" lay-filter="dosql">保存</button>
					</div>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"><span
					style="color: red">*</span>单位名称：</label>
				<div class="layui-input-inline">
					<input type="text" id="gwmc" lay-verify="required" name="gwmc"
						class="layui-input">
				</div>
				<label class="layui-form-label" style="width: 150px;">上级单位：</label>
				<div class="layui-input-inline">
					<select name="sjdw" id="sjdw" lay-search="" lay-filter="sjdw"></select>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"><span
					style="color: red">*</span>单位类型：</label>
				<div class="layui-input-inline">
					<select name="dwlx" id="dwlx" lay-verify="required" lay-search="" lay-filter="dwlx">
						<option>请选择单位类型</option>
						<option value="0">区</option>
						<option value="1">街道</option>
					</select>
				</div>
					<label class="layui-form-label" style="width: 150px;"><span
					style="color: red">*</span>单位代码：</label>
				<div class="layui-input-inline">
					<input type="text" id="dwdm" lay-verify="required" name="dwdm" class="layui-input">
				</div>
			</div>
	</form>
	
	</div>
	<div id="qxmain" style="display: none;">
		<form class="layui-form" id="form6" action=""
			style="margin-top: 20px;"></form>
	</div>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
function queryinit(e){
	layui.use('table', function(){
        var table = layui.table;
        //表单序列化数组
    	var index = layer.load(2);
        //第一个实例
        table.render({
          elem: '#table'
          ,height: 500
          ,width:document.body.clientWidth//设置宽度为body宽度
          ,method : 'post'
          /* url : "ajax.do?ctrl=aqycxhzxx_queryData&pageNo=" + pageNo+ "&pageSize=" + pageSize, */
          ,url:"gwgl/queryry.do"
          ,cellMinWidth: 120
          ,where: {
        	  'findtext':$("#findtext").val()
	       }
          ,limit:10
          ,page: true //开启分页
          ,cols: [[ //表头
        	{field:'ID',title: '主键 ',style:'display:none;'}, 
			{field:'zizeng',title: '序号 ',width:'11%',fixed: 'right',templet:"#zizeng"} ,
			{field:'SSDW_DM',title: '单位代码',width:'14%',align:'right'}  ,
			{field:'SSDW_MC',title: '单位名称',width:'14%',align:'right'} ,
			{field:'SJDW_DM',title: '上级单位代码 ',width:'14%',align:'right'} ,
			{field:'SJDWMC1',title: '上级单位',width:'14%',align:'right'}  ,
            {field:'SSDW_LX',title: '单位类型',width:'14%',align:'right'}  ,
            {title: '操作', width: 148, align: 'center', fixed: 'right',toolbar:'#bar' }
          
          ]]
        ,done : function(res, curr, count){
        	$('.layui-table:eq(1) thead tr th:eq(0)').addClass('layui-hide');
        	debugger;
  		 	layer.close(index);
    	}
        });
        
      });
	
}

	var editlayer;
	var addlayer;
	var gwid = "";
	var level = 0;
	var qxlayer;
	var ids = "";
	var idlist = [];
	$(function() {
		$("#table").attr("lay-data","{width:" + document.body.clientWidth + "}");

		initdw();
		//getData();
		queryinit();
	});
	function initids(id) {
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "gwgl/queryids.do",
			data : {
				id: id
			},
			dataType : "json",
			success : function(obj) {
				if (obj != null && obj.code == '000') {
					idlist = obj.data;
					initcd();
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
	function Checkid(id) {
		for (var i = 0; i < idlist.length; i++) {
			if (idlist[i].CDID == id) {
				ids += idlist[i].CDID + ",";
				return true;
			}
		}
		return false;
	}
	function initcd() {
		$.ajax({
					type : "post", //请求方式
					async : false, //是否异步
					url : "gwgl/querycd.do",
					data : {},
					dataType : "json",
					success : function(obj) {
						if (obj != null && obj.code == '000') {
							$("#form6").html('');
							var str = '<input type="hidden" id="ids" name="ids" /><input type="hidden" id="gwidqx" name="gwidqx" /><div class="layui-form-item">'
									+ '<label class="layui-form-label" style="width: 150px;"></label>'
									+ '<div class="layui-input-inline" style="width: 760px;">'
									+ '<div style="text-align: right;">'
									+ '<button class="layui-btn layui-btn-normal" id="qxsave"' +
							'	type="button" lay-submit="" lay-filter="qxsave">保存</button>'
									+ '</div>' + '</div>' + '</div>';
							for (var i = 0; i < obj.data.length; i++) {
								if (Checkid(obj.data[i].UUID)) {
									str += '<div class="layui-form-item"><div class="layui-input-block" style="margin-left:15px;"><input  lay-filter="gncd_all" class="' + obj.data[i].UUID + '" id="' + obj.data[i].UUID + '" type="checkbox" checked="" name="like[read]" title="'
									+ obj.data[i].GNMC
									+ '"  value="' + obj.data[i].UUID + '"></div></div><div class="layui-form-item"><div class="layui-input-block">';
								} else {
									str += '<div class="layui-form-item"><div class="layui-input-block" style="margin-left:15px;"><input  lay-filter="gncd_all" class="' + obj.data[i].UUID + '" id="' + obj.data[i].UUID + '" type="checkbox" name="like[read]" title="'
									+ obj.data[i].GNMC
									+ '"  value="' + obj.data[i].UUID + '"></div></div><div class="layui-form-item"><div class="layui-input-block">';
								}

								for (var j = 0; j < obj.data[i].gnlist.length; j++) {
									if (Checkid(obj.data[i].gnlist[j].UUID)) {
										str += '<input id="' + obj.data[i].UUID + '_' + obj.data[i].gnlist[j].UUID + '" type="checkbox"  class="' + obj.data[i].UUID + '" name="like1[write]" lay-filter="gncd_one" checked="" lay-skin="primary" title="'
										+ obj.data[i].gnlist[j].GNMC
										+ '" value="' + obj.data[i].gnlist[j].UUID + '" >';
									} else {
										str += '<input id="' + obj.data[i].UUID + '_' + obj.data[i].gnlist[j].UUID + '" type="checkbox"  class="' + obj.data[i].UUID + '" name="like1[write]" lay-filter="gncd_one" lay-skin="primary" title="'
										+ obj.data[i].gnlist[j].GNMC
										+ '" value="' + obj.data[i].gnlist[j].UUID + '" >';
									}
								}
								str += '</div></div>';
							}
							$("#form6").html(str);
							layui.form.render();
							//初始化Element
							layui.use([ 'form' ], function() {
								var form = layui.form;
								form.on('checkbox(gncd_all)', function(data) {
									console.log(data.elem); //得到checkbox原始DOM对象
									console.log(data.elem.checked); //是否被选中，true或者false
									console.log(data.value); //复选框value值，也可以通过data.elem.value得到
									console.log(data.othis); //得到美化后的DOM对象
									var a = data.elem.checked;
									var selector = $("." + data.value);
									if (a == true) {
										selector.each(function() {
											ids += this.value + ",";
											console.log(this.value);
										});

										$("." + data.value).prop("checked",true);
										form.render('checkbox');
									} else {
										selector.each(function() {
											ids = ids.replace(this.value + ",","");
											console.log(this.value);
										});

										$("." + data.value).prop("checked",false);
										form.render('checkbox');
									}
								});
								form.on('checkbox(gncd_one)',
										function(data) {
											console.log(data.elem); //得到checkbox原始DOM对象
											console.log(data.elem.checked); //是否被选中，true或者false
											console.log(data.value); //复选框value值，也可以通过data.elem.value得到
											console.log(data.othis); //得到美化后的DOM对象
											var a = data.elem.checked;
											var id = data.elem.id.substring(0,data.elem.id.lastIndexOf("_"));
											var pid = $("#" + id).val();
											if (a == true) {
												ids += data.value + ",";
												ids += pid + ",";
												$("#" + id).prop("checked",true);
												form.render('checkbox');
											} else {
												ids = ids.replace(data.value + ",", "");
											}
										});
							});
							console.log(str);
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

	function initdw() {
		
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "gwgl/sjdw.do",
			data : {},
			dataType : "json",
			success : function(obj) {
				
				if (obj != null && obj.code == '000') {
					
					var str = "<option value=''>请选择上级单位</option>";
					for (var i = 0; i < obj.data.length; i++) {
						str += "<option value='" + obj.data[i].SSDW_DM + "'>"
								+ obj.data[i].SSDW_MC + "</option>";
					}
					$("#sjdw").html(str);
					$("#sjdwedit").html(str);
					layui.use([ 'form' ], function() {
						var form = layui.form;
						form.render('select');
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
	}

	//初始化Element
	layui.use([ 'form' ], function() {
		var form = layui.form;

		form.on('submit(button)', function(data) {
			pageNo = 1; //当点击搜索的时候，应该回到第一页
			//getData();
			queryinit();
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		form.on('submit(dosql)', function(data) {
			var gwmc = $("#gwmc").val();
			if (gwmc == "" || gwmc == undefined) {
				layer.msg('提示：请输入单位名称！');
			} else {

				$.ajax({
					type : "post", //请求方式
					async : false, //是否异步
					url : "gwgl/add.do",
					data : $("#form3").serialize(),
					dataType : "json",
					success : function(obj) {
						if (obj != null && obj.code == '000') {
							
							layer.close(addlayer);
							$("#button").click();
							//initdw();
							$("#gw").html("");
							layer.msg('提示：执行操作成功！');
							
						} else {
							layer.msg('提示：执行操作失败！');
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

			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		form.on('submit(qxsave)', function(data) {
			$("#ids").val(ids);
			var gwid = $("#gwidqx").val();
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "gwgl/updateqx.do",
				data : {
					ids : ids,
					gwid : gwid
				},
				dataType : "json",
				success : function(obj) {
					$('#qxmain').css('display', 'none');

					if (obj != null && obj.code == '000') {
						layer.msg('提示：执行操作成功！');
						layer.close(qxlayer);
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

			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。			
		});
		form.on('submit(editsave)', function(data) {
			var sjgw = $("#sjdwedit").val();
			var id = $("#idedit").val();
			if (id == sjgw) {
				layer.msg('提示：上级单位不得是当前单位！');
				return;
			}
			var gwmc = $("#gwmcedit").val();
			if (gwmc == "" || gwmc == undefined) {
				layer.msg('提示：请输入单位名称！');
			} else {
				$.ajax({
					type : "post", //请求方式
					async : false, //是否异步
					url : "gwgl/update.do",
					data : $("#form5").serialize(),
					dataType : "json",
					success : function(obj) {
						if (obj != null && obj.code == '000') {
							layer.msg('提示：执行操作成功！');
							layer.close(editlayer);
							$("#button").click();
							$("#gw").html("");
							$("#editsave").hide();
							initdw();
						} else {
							layer.msg('提示：执行操作失败！');
							$("#editsave").hide();
						}
					},
					cancel : function() {
						$("#editsave").hide();
						return;
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						// 状态码
						alert(XMLHttpRequest.status);
						// 状态
						alert(XMLHttpRequest.readyState);
						// 错误信息  
						alert(textStatus);
						$("#editsave").hide();
					}
				});

			}

			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});

		form.on('submit(add)', function(data) {
			addlayer = layer.open({
				type : 1,
				title : '新增单位',
				area : [ '53%', '53%' ],
				shadeClose : false, //点击遮罩关闭
				content : $('#addmain'),
				success : function() {
					$("#gwmc").val("");
					$("#sjdw").val("");
					$("#dwlx").val("");
					layui.form.render();
					return;
				},
				cancel : function() {
					$('#addmain').hide();
					return;
				},
				end : function() {
					$('#addmain').hide();
					//$('#addmain').css("display", "none");
				}
			});

			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});

	});

	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 10; //每页显示数据条数
	var pageNo = 1; //当前页数
	var count = 0; //数据总条数
	function getData() {
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "gwgl/queryry.do?pageNo=" + pageNo + "&pageSize="
					+ pageSize,
			data : $("#form").serialize(),
			dataType : "json",
			success : function(obj) {
				getTbale(obj.data); //拼接表格
				count = obj.count; //数据总条数
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
				elem : 'page1', //注意，这里的page1 是 ID，不用加 # 号
				count : count, //数据总数，从服务端得到
				limit : pageSize, //每页显示条数
				limits : [ 10, 20, 30, 50 ], //条数列表
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
			s += '<tr><td>' + o.SSDW_DM + '</td>';
			s += '<td>' + (v + 1) + '</td>';
			s += '<td>' + (o.SSDW_DM == undefined ? "-" : o.SSDW_DM) + '</td>';
			s += '<td>' + (o.SSDW_MC == undefined ? "-" : o.SSDW_MC) + '</td>';
			s += '<td>' + (o.SJDW_DM == undefined || o.SJDW_DM == "null" ? "-" : o.SJDW_DM) + '</td>';
			s += '<td>' + (o.SJDWMC1 == undefined ? "-" : o.SJDWMC1) + '</td>';
			s += '<td>' + (o.SSDW_LX == 0 ? "区" : "街道") + '</td>';
			s += '</tr>';
		});
		$("#ttbody").html(s);
		//执行渲染
		layui.use([ 'table' ], function() {
			layui.table.init('user', {
				height : 480, //设置高度
				limit : pageSize
			//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
			//支持所有基础参数
			});
			$('.layui-table:eq(1) thead tr th:eq(0)').addClass('layui-hide');
		});

		if (data == null || data.length <= 0) {
			$("#page1").hide();
		}

	}
	layui.use('table',
					function() {
						var table = layui.table;
						//方法级渲染
						//监听工具条
table.on('tool(user)',
		function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
				var data = obj.data; //获得当前行数据
				var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
				if (layEvent === 'del') { //删除
					layer.confirm('',{area : ['30%','27%' ],content : '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">真的删除单位'
					+ data.SSDW_MC+ '么? </div>'
						},
		function(index) {
				$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "gwgl/checkzgw.do",
				data : data,
				dataType : "json",
				success : function(res) {
				if (res != null&& res.code == '000') {
					$.ajax({
					type : "post", //请求方式
					async : false, //是否异步
					url : "gwgl/checkgwry.do",
					data : data,
					dataType : "json",
					success : function(res) {
				if (res != null&& res.code == '000') {
					$.ajax({
					type : "post", //请求方式
					async : true, //是否异步
					url : "gwgl/del.do",
					data : data,
					dataType : "json",
		success : function(obj) {
				if (obj.code == '000') {
				layer.msg('删除单位'+ data.SSDW_MC+ ' 成功！');
				} else {
					layer.alert('删除单位'+ data.SSDW_MC+ '失败！');
					}
				layer.close(index);
				$("#button").click();
				initdw();
				},
				error : function(XMLHttpRequest,textStatus,errorThrown) {
				// 状态码
					alert(XMLHttpRequest.status);
				// 状态
					alert(XMLHttpRequest.readyState);
				// 错误信息  
					alert(textStatus);
				}
				});
	            } else {
					layer.msg('提示：请先删除当前单位的所有人员！');
						}
				},
				cancel : function() {
				return;
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
					layer.msg('提示：请先删除当前单位的所有子单位！');
					}
			    },
				cancel : function() {
					return;
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
				});
				} else if (layEvent === 'edit') { //编辑
				//do something
					editlayer = layer.open({
									  type : 1,
									  title : '单位修改',
										area : [ '1000px','600px' ],
										shadeClose : false, //点击遮罩关闭
										content : $('#editmain'),
										success : function() {
													$("#idedit").val(data.SSDW_DM);
													$("#gwmcedit").val(data.SSDW_MC);
													$("#sjdwedit").val(data.SJDW_DM);
													$("#dwlxedit").val((data.DWLX == "区" ? "0": "1"));
													$("#dwdmedit").val((data.SSDW_DM));
													layui.form.render();
															},
										cancel : function() {
															$('#editmain').hide();
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
											} else if (layEvent === 'qx') { //编辑
												//do something

												qxlayer = layer.open({
															type : 1,
															title : '设置权限',
															area : [ '1000px','600px' ],
															shadeClose : false, //点击遮罩关闭
															content : $('#qxmain'),
															success : function() {
																ids = "";
																initids(data.SSDW_DM);
																$("#gwidqx").val(data.SSDW_DM);

															},
															cancel : function() {
																$('#qxmain').hide();
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
										});

					});
</script>


</html>
