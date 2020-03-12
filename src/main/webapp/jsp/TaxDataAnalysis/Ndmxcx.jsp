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

<title>数据自定义</title>

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

</style>
</head>
<body style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">
		本功能页面用于通过选择定制企业模板与定制查询模板查询出对应数据！</blockquote>
	<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">

						<div class="layui-form-item">
							<label class="layui-form-label" style="width: 100px">定制企业模板:</label>

							<div class="layui-inline" style="width: 300px">
								<select name="qymbmc" lay-filter="qymbmc" id="qymbmc"
									lay-search="">

								</select>
							</div>
						</div>

						<div class="layui-form-item"></div>
						<div class="layui-form-item">
							<label class="layui-form-label" style="width: 100px">定制查询模板:</label>
							<div class="layui-inline" style="width: 300px">
								<select name="cxmbmc" lay-filter="cxmbmc" id="cxmbmc"
									lay-search="">

								</select>
							</div>
							<div class="layui-inline"></div>

							<div class="layui-inline" style="margin-left: 70px">
								<button class="layui-btn layui-btn-normal" data-type="reload"
									id="selectbyCondition" onclick="return false;">查询</button>
								<button class="layui-btn layui-btn-normal" id="exportExcel"
									type="button" lay-submit="" lay-filter="exportExcel">导出</button>
							</div>

						</div>


					</div>
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>

			<table class="layui-table" id="syzs" lay-filter="syzs">

			</table>
		</fieldset>
	</form>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	var wait;
	var temp = "";
	var qxlayer;
	var files;
	
		queryDzqymb();
		queryDzcxmb();
	
	function queryDzqymb(){
		layui.use('form', function(){
			var form = layui.form; 
			$.ajax({
				url : "ndmxcx/queryDzqymb.do",
				type : "post", //请求方式
				async : true, //是否异步
				data : {},
				dataType : "json",
				success : function(obj) {
					if(obj.code=='000'){
						row='<option value="请选择">请选择</option>';
						for(var i in obj.data){
							row+='<option value='+obj.data[i].U_ID+'>'+obj.data[i].MBMC+'</option>';
						}
						$("#qymbmc").append(row);
						form.render('select');
					}
				}
			})
		})
	}
	
	function queryDzcxmb(){
		layui.use('form', function(){
			var form = layui.form; 
		$.ajax({
			url : "ndmxcx/queryDzcxmb.do",
			type : "post", //请求方式
			async : true, //是否异步
			data : {},
			dataType : "json",
			success : function(obj) {
				console.log(obj)
				if(obj.code=='000'){
					row='<option value="请选择">请选择</option>';
					for(var i in obj.data){
						row+='<option value='+obj.data[i].U_ID+'>'+obj.data[i].MBMC+'</option>';
					}
					$("#cxmbmc").append(row);
					form.render('select');
				}
			}
		});
	})
	}

	
	
	layui.use([ 'form', 'laydate', 'laypage' ],function() {
		var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate, laypage = layui.laypage;
		//年月范围
		laydate.render({
			elem : '#rkrq',
			type : 'month',
			range : true
		});
		var date=new Date;
		 var year=date.getFullYear(); 
		 var month=date.getMonth()+1;
		 month =(month<10 ? "0"+month:month); 
		 var mydate = (year.toString()+"-"+month.toString())+" - "+(year.toString()+"-"+month.toString());
		 
		$("#rkrq").val(mydate);
		
		//导出
		form.on('submit(exportExcel)', function(data) {
			var dzqymb= document.getElementById("qymbmc").value;
			var dzcxmb=document.getElementById("cxmbmc").value ;
			console.log(dzqymb);
			console.log(dzcxmb);

			if(dzqymb=='请选择'|| dzqymb==undefined){
				layer.msg('请选择企业模板');
				return false;
			}
			window.open("ndmxcx/exportExcel.do?"+"dzqymb_id="+dzqymb+"&dzcxmb_id="+dzcxmb );
		});
	});
	
	   var cols=[];
		function getTitle(){
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "ndmxcx/getTitle.do",
				data : {
					dzqymb_id: document.getElementById("qymbmc").value,
					dzcxmb_id: document.getElementById("cxmbmc").value 
				},
				dataType : "json",
				success : function(obj) {
					console.log(obj.data)
					cols=obj.data;
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
		layui.use(['table'], function(){
			var table = layui.table
			//查询
			 $('#selectbyCondition').on('click',
				 function(){
				
				 var qymbmc =$("#qymbmc").val(); 
				  if(qymbmc=="请选择"){
						layer.confirm('未选择定制企业模版；请选择'
									, {btn: ['确认']
					 				,offset: ['300px', '450px']
					 				,icon: 7
					 				}
					 				, function(index) {
					                    layer.close(index)
					                    });
					 return false;
				 }else{ 
					 loadlayer = layer.load();
					 getTitle();
				     //执行一个 table 实例
					   table.render({
					    elem: '#syzs'
					    ,url:"ndmxcx/queryDataByMB.do"
				    	,where: {
				    		dzqymb_id: document.getElementById("qymbmc").value,
				    		dzcxmb_id: document.getElementById("cxmbmc").value 
				        }
					    
					    ,limit:10
					    ,width:document.body.clientWidth
					    ,page: true //开启分页
					    ,cols: [cols]
					    ,id:'syzs'
					    ,loading:false
				    	,done: function (res, curr, count) {
				            // 添加分页加载动画的函数    
				            layer.closeAll('loading');
				            $('.layui-laypage > a').each(function(){
				                $(this).attr('onclick',"pageLoading(this)");
				                layer.closeAll('loading'); // 渲染完成后关闭动画
				            })
				        }
					  })
				 }
		     });
		});
	   
		function pageLoading(that){
			loadlayer = layer.load();
			var disa = $(that).hasClass('layui-disabled');
			if(disa){ // 如果是最后一页或者是只有一页,那就不可点击
				return false;
			}
		}
	
	
</script>


</html>