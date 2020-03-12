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

<title>模板管理</title>

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
.layui-form .layui-border-box .layui-table-view {
	max-width: 1700px;
}

i {
	display: inline-block;
	width: 18px;
	height: 18px;
	line-height: 18px;
	text-align: center
}

.label {
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

.layui-colla-content {
	padding: 20px 15px;
}

.layui-col-md4 {
	width: 20%;
}
</style>
<script type="text/html" id="zt">

	<input type="checkbox" name="ZT" value="{{d.ID}}" lay-skin="switch" lay-text="启用|不启用" lay-filter="ztDemo" {{ d.ZT == 1 ? 'checked' : '' }}>
	
</script>
<script type="text/html" id="barDemo">
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>

{{# if(d.MBLX=="定制查询模板"){}}
  <a class="layui-btn layui-btn-xs" lay-event="update">修改</a>
				{{# }　}}
{{# if(d.MBLX!="定制查询模板"){}}
  <a class="layui-btn layui-btn-xs" lay-event="download">下载</a>
				{{# }　}}
</script>
</head>
<body style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">本功能页面用于模板的管理！</blockquote>
	<form class="layui-form" id="form" action="">
		<input id="cslx" name="cslx" type="hidden" value="0" /> <input
			id="ids" name="ids" type="hidden" value="0" />
		<div class="layui-collapse" lay-filter="test"
			style="border-bottom: 0px; height: 69px;">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md12">
							<div class="layui-row layui-col-space10">
								<div style="width: 20%; float: left; height: 38px;">
									<label class="layui-form-label">日期：</label>
									<div class="layui-input-inline">
										<input id="rkrq" name="rkrq" type="text" placeholder="请输入日期"
											class="layui-input">
									</div>
								</div>
								<div style="float: left; display: inline-flex;">
									<div class="layui-inline " style="float: left;">
										<label class="layui-form-label">模板名称：</label>
										<div class="layui-input-inline">
											<input type="text" name="mbmc" id="mbmc"
												placeholder="请输入模板名称" autocomplete="off" class="layui-input">
										</div>
										<div class="layui-input-inline">
										</div>
										<div class="layui-input-inline">
										</div>
										<button onclick="return false;" data-type="reload"
											id="selectbyCondition" class="layui-btn">查询</button>
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
			<legend>查询显示</legend>

			<table class="layui-table" id="mb" lay-filter="mb">

			</table>
		</fieldset>
	</form>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
var editlayer;
	layui.use(['input'], function() {
		layer.msg("已开启");
	})
	layui.use([ 'form', 'laydate', 'laydate' ], function() {

		var form = layui.form, layer = layui.layer, laydate = layui.laydate;
		//年月范围
		laydate.render({
			elem : '#rkrq',
		});

		//监听
		form.on('switch(ztDemo)', function(obj) {
			//获取行内容,调用接口
			var status = '';
			if (obj.elem.checked == true) {

				status = 1;
			} else {

				status = 0;
			}
			var id = this.value;
			//修改接口
			ajax({
				url : "mbgl/modify.do",
				data : {
					"id" : id,
					"status" : status

				},
				type : 'post',
				success : function(obj) {

				}
			});

		});

	});

	layui.use('table', function() {
		var table = layui.table;

		//第一个实例
		table.render({
			elem : '#mb',
			height : 600,
			width:document.body.clientWidth-20,
			limit : 15,
			limits : [ 15, 30, 45, 60 ],
			page : true //开启分页
			,
			cols : [ [ //表头
			{
				type : 'numbers',
				title : '序号'
			}, {
				field : 'MBMC',
				title : '模板名称'
			}, {
				field : 'MBLX',
				title : '模板类型'
			}, {
				field : 'DRSJ',
				title : '创建时间'
			}, {
				field : 'CZY',
				title : '创建人'
			}, {
				field : 'MBMS',
				title : '模版描述'
			}, {
				field : 'ZT',
				title : '状态',
				templet : '#zt'
			}, {
				fixed : 'right',
				title : '操作',
				toolbar : '#barDemo',
				width : 150
			} ] ]
		}); 

		var $ = layui.$, active = {
			
			reload : function() {
				loadlayer = layer.load();
				table.reload('mb', {
					
					page : {
						curr : 1
					//重新从第 1 页开始
					},
					method : 'POST',
					url : 'mbgl/querymb.do' //数据接口
					,
					height : '600'
					//根据条件查询
					,
					where : {

						mbmc : document.getElementById('mbmc').value,
						rq : $("#rkrq").val()

					},
					done : function(res, curr, count) {
						layer.close(loadlayer);
					}
				}) 
			}
		};

		//点击搜索按钮根据用户名称查询
		$('#selectbyCondition').on('click',function() {

			var type = $(this).data('type');
			active[type] ? active[type].call(this) : '';
		});

		getdata();
		function getdata() {
			var type = 'reload';
			active[type] ? active[type].call(this) : '';
		}

		//监听行工具事件
		table.on('tool(mb)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				layer.confirm('', {btn: ['是','否']
				,area:['30%','27%']
	            ,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">是否确认删除？</div>'
                }, function(index){
                	$.ajax({
    					type : "post",
    					url : 'mbgl/delmb.do',
    					data : data,
    					xhrFields : {
    						withCredentials : true
    					},
    					crossDomain : true,
    					success : function(data) {

    						if (data.code == '000') {
    							layer.alert("删除成功", {
    								icon : 1
    							}, function() {
    								window.location.reload();
    							});
    						} else {
    							layer.alert("删除成功", {
    								icon : 1
    							}, function() {
    								window.location.reload();
    							});
    						}

    					}
    				});
					});	
			}else if (obj.event === 'update') {
				editlayer = layer.open({
					type : 2,
					title : '修改定制查询模板',
					area : [ '90%', '90%' ],
					shadeClose : false, //点击遮罩关闭
					content: ["jsp/sjfx/updatedzcxmb.jsp?id="+data.ID, 'yes'],
					success : function() {	
						return;
					},
					cancel : function() {
						return;
					},
					end : function() {

					}
				});
			
			}else if (obj.event === 'download') {
				window.location.href = data.WJLJ;
			}
		});

	});
</script>


</html>