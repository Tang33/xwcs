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

<title>分月明细查询</title>

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
<script src="./js/ySelect.js" charset="utf-8"></script>
<link rel="stylesheet" href="./css/ySelect.css" media="all">
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
.layui-colla-content{


	padding: 20px 15px;
}

.layui-col-md4{

	width: 20%;
}

#szys .layui-form-select{

	display: none;
}
.layui-table-body{

	overflow: auto;
	
}
</style>
</head>
<body style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">本功能页面用于通过选择定制企业模板及添加条件查询出对应的数据！</blockquote>
	<form class="layui-form" id="form" action="">
		<input id="cslx" name="cslx" type="hidden" value="0" /> <input
			id="ids" name="ids" type="hidden" value="0" />
		<div class="layui-collapse" lay-filter="test" style="border-bottom: 0px;height: 110px;">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md4">
							<label class="layui-form-label">模板选择:</label>

							<div class="layui-inline">
								<select name="mbmccx" lay-filter="mbmc" id="mbmccx">
								</select>
							</div>
						</div>
						<div class="layui-col-md3">
							<div class="layui-inline">
								<input type="radio" lay-filter="radiolx"  name="radiolx" value="分月" title="分月">
								<input type="radio" lay-filter="radiolx" name="radiolx" value="分税种" title="分税种">
								<input type="radio" lay-filter="radiolx" name="radiolx" value="分月分税种" title="分月分税种">
							</div>
						</div>
					</div>
					<div class="layui-row" style="margin-top: 20px;">
						
							<label class="layui-form-label">年份:</label>
							<div class="layui-inline">
								<select name="yeard" lay-filter="yeard" id="yeard">
									<option value="">请选择</option>
									<option value="2019">2019</option>
									<option value="2018">2018</option>
									<option value="2017">2017</option>
									<option value="2017">2016</option>
									<option value="2017">2015</option>
									<option value="2017">2014</option>
									<option value="2017">2013</option>
									<option value="2017">2012</option>
									<option value="2017">2011</option>
									<option value="2017">2010</option>
								</select>
							</div>
						
						<div class="layui-inline" id="szys" style="display: none;">
							<label class="layui-form-label">税种:</label>
							<div class="layui-inline" style="width: 300px;border: 1px solid #e6e6e6;">
								<select name="sz" lay-filter="sz" id="sz" multiple="multiple">
									<option value="zzs">增值税</option>
									<option value="yys">营业税</option>
									<option value="grsds">个人所得税</option>
									<option value="fcs">房产税</option>
									<option value="yhs">印花税</option>
									<option value="ccs">车船税</option>
									<option value="qysds">企业所得税</option>
									<option value="ygzzzs">营改增增值税</option>
									<option value="cswhjss">城市维护建设税</option>
									<option value="dfjyfj">地方教育附加</option>
									<option value="jyfj">教育费附加</option>
									<option value="cztdsys">城镇土地使用税</option>
									<option value="hbs">环保税</option>
								</select>
							</div>
						</div>
						<div class="layui-inline">
						 </div>
					       <div class="layui-inline">
					        <button class="layui-btn layui-btn-normal" onclick="return false;"
					         data-type="reload" id="selectbyCondition">查询</button>
					         <button class="layui-btn layui-btn-normal" id="exportExcel"
							type="button" lay-submit="" lay-filter="exportExcel">导出</button>	
					       </div>
					    
					</div>
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>

			<table class="layui-hide" lay-skin="nob" id="syzs" lay-filter="syzs"></table>
				
		</fieldset>
	</form>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>

	$('#sz').ySelect();

	var wait;
	var temp = "";
	var qxlayer;
	var files;
	var radiolx='';
	layui.use([ 'form', 'laydate', 'laypage' ],function() {
		var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate, laypage = layui.laypage;
		
		 form.on('radio(radiolx)', function (data) {
	            radiolx=$(this).val();
	            style="display: none;";
	            
	            //如果不是分月查询，则下方的税种展示
	            if(radiolx=='分月'){
	            	
	        		document.getElementById("szys").style.display="none";
	            	
	            }else{
	            	
	        		document.getElementById("szys").style.display="";
	            }
	            
	     });
		 
		//导出
		form.on('submit(exportExcel)', function(data) {
		var	mbmc=document.getElementById("mbmccx").value;

			if(mbmc=='' || mbmc==undefined ){
				layer.msg('请选择模板');
				return false;
			}

			window.location.href="export.do?ctrl=zhcxfymx_export&"+"sz="+$("#sz").ySelectedValues(",")+"&szzw="+$("#sz").ySelectedTexts(",")+"&radiolx="+radiolx+"&mbmc="+mbmc+"&nf="+document.getElementById("yeard").value;
		});
	});
	
	
	
	
	//获取模板的相关信息
	getmb();
	var row='';
	function getmb(){
		layui.use('form', function(){
			var form = layui.form;
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "fymxcx/Zhcxfymx_querymbmc.do",
				data : {},
				dataType : "json",
				success : function(obj) {
					
					$.each(obj.data, function(i) {
						
						if(i==0){
							
							row='<option value="">请选择</option><option value="'+obj.data[i].MBMC+'">'+obj.data[i].MBMC+'</option>';
						}else{
							
							row+='<option value="'+obj.data[i].MBMC+'">'+obj.data[i].MBMC+'</option>';
						}
								
					});
					$("#mbmccx").append(row);
					form.render('select');
					
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
			
		})
		
	}
	
	
	var colsys=[];
	var colsys1=[];
	function getTitle(){
		
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "fymxcx/Zhcxfymx_getTitle.do",
			data : {
				
				sz: $("#sz").ySelectedValues(","),
				szzw: $("#sz").ySelectedTexts(","),
				mbmc: document.getElementById("mbmccx").value,
				radiolx: radiolx
			},
			dataType : "json",
			success : function(obj) {
				
				colsys=[];
				colsys=obj.data;
				
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
	function getTitle1(){
		
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "fymxcx/Zhcxfymx_getTitle1.do",		
			data : {
				sz: $("#sz").ySelectedValues(","),
				szzw: $("#sz").ySelectedTexts(","),
				mbmc: document.getElementById("mbmccx").value,
				radiolx: radiolx
				
			},
			dataType : "json",
			success : function(obj) {
				colsys1=[];
				colsys1=obj.data;
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
	
	
	var cols=[];
	layui.use(['table'], function(){
		
		var table = layui.table
		var i=0;
		//查询
		 $('#selectbyCondition').on('click',
			 function(){
			 debugger;
				 var mbmc = $("#mbmccx").val();
				 var yeard = $("#yeard").val();
				 if(mbmc=='' || mbmc==undefined || yeard=='' || yeard==undefined){
					 layer.msg('请选择查询模板与查询年份！');
					 return false;
				 }
				 debugger
			 	 loadlayer = layer.load();
			     if(radiolx=='分月分税种'){
			    	 colsys1=[];
			    	 colsys=[];
			    	 getTitle();
				     getTitle1();
				     cols=[colsys,colsys1]
			     }else{
			    	 colsys1=[];
			    	 colsys=[];
			    	 getTitle();
			    	 cols=[colsys]
			     }
			     
			     //执行一个 table 实例
				  table.render({
				    elem: '#syzs'
				    ,height: 570
				    ,method: 'post'
				    ,url:'fymxcx/Zhcxfymx_queryYmx.do'		
			    	,where: {
			    		
			    		mbmc: document.getElementById("mbmccx").value,
			    		nf: document.getElementById("yeard").value,
			    		cxlx: radiolx,
			    		sz: $("#sz").ySelectedValues(",")
			        	
			        }
				    ,limit:10
				    ,cols: cols
				    ,page: true //开启分页
				    ,id:'syzs'
				    ,done : function(){
				    	$(".layui-table-view")[0].style.width=document.body.clientWidth-20;
				    	layer.close(loadlayer);
		        	}
				    
				});
	     });
	});
	
</script>
</html>