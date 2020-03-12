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

<title>定制企业查询</title>

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
.layui-colla-content{


	padding: 20px 15px;
}

.layui-col-md4{

	width: 20%;
}

</style>
<script type="text/html" id="dyjelj">
{{#  if(d.DYLJJE == null){ }}
	 {{ "" }}
{{#  } else { }}
	 {{ (parseFloat(d.DYLJJE) / 10000).toFixed(2) }}
{{#  } }}
</script>
<script type="text/html" id="jetq">
	{{#  if(d.TQLJJE == null){ }}
		 {{ "" }}
	{{#  } else { }}
		 {{ (parseFloat(d.TQLJJE) / 10000).toFixed(2) }}
    {{#  } }}
</script>
<script type="text/html" id="zf">
	{{#  if(d.ZF == null){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ (parseFloat(d.ZF) / 10000).toFixed(2) }}
    {{#  } }}
</script>
<script type="text/html" id="zzlx">
	{{#  if(d.ZZBL == null){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ (parseFloat(d.ZZBL) * 100).toFixed(2)+"%" }}
    {{#  } }}
</script>
<script type="text/html" id="zb">
	{{#  if(d.ZB == null){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ (parseFloat(d.ZB) * 100).toFixed(2)+"%" }}
    {{#  } }}
</script>
</head>
<body  style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">本功能页面用于对当年税收基本情况分析的查询！</blockquote>
	<form class="layui-form" id="form" action="">
		<input id="cslx" name="cslx" type="hidden" value="0" /> <input
			id="ids" name="ids" type="hidden" value="0" />
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">

						<div class="layui-col-md3" >
							<label class="layui-form-label" style="padding-right: 0px">税收视角：</label>

							<div class="layui-inline" style="width:70%">
								<select name="mbmccx" lay-filter="mbmc" id="mbmccx">
									<option value="">请选择</option>
									<option value="FAST_YLJ">一般公共预算收入分月情况</option>
									<option value="FAST_NSZZB">一般公共预算按税种占比情况</option>
									<option value="FAST_NHYZB">一般公共预算按行业占比情况</option>
									<option value="FAST_YBGGYS_0">一般公共预算收入前二十</option>
									<option value="FAST_YBGGYS_1">一般公共预算增幅前二十</option>
									<option value="FAST_YBGGYS_2">一般公共预算减幅前二十</option>
									
								</select>
							</div>
						</div>
						<div class="layui-col-md6" style="text-align:left;">
							<div class="layui-inline">
								<button class="layui-btn layui-btn-normal" onclick="return false;"
									data-type="reload" id="selectbyCondition">查询</button>
								<button type="button" class="layui-btn " id="exportExcel" >
									导出
								</button>	
							</div>
	
						</div>
					</div>
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>
			<div id="myTable">
				<table class="layui-table" id="syzs" lay-filter="syzs">
				
				</table>
			</div>
			
		</fieldset>
	</form>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	var wait;
	var temp = "";
	var qxlayer;
	var files;

	var cols=[];
	function getTitle(){
		cols=[];
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "Symbcx/Symbcx_getheader.do",
			data : {
				
				mbmc: document.getElementById("mbmccx").value
			},
			dataType : "json",
			success : function(obj) {
				
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
	
	//查询展示的内容
	layui.use(['table'], function(){
		
		var table = layui.table
		
		//查询
		 $('#selectbyCondition').on('click',
			 function(){
			 
			 	$("#myTable").empty();
	            $("#myTable").append('<table class="layui-table" id="syzs" lay-filter="syzs"></table>');
			     getTitle();
			     
			    //执行一个 table 实例
				  table.render({
				    elem: '#syzs'
				    ,height: 590
				    ,width:document.body.clientWidth-20
					,url:"Symbcx/Symbcx_getdata.do"
			    	,where: {
			        	mbmc: document.getElementById("mbmccx").value

			        }
				    ,limit:15
				    ,limits : [ 15, 30, 45, 60 ]
				    ,cols: [cols]
				    ,page: true //开启分页
				    ,id:'syzs'
				    
				  });
	     });
		
		  //导出
	    $("#exportExcel").click(function() {
	    	var selectIndex = document.getElementById("mbmccx").selectedIndex;//获得是第几个被选中了
	    	var text = document.getElementById("mbmccx").options[selectIndex].text;//获得被选中的项目的文本
	    	console.log(text)
	    	if("一般公共预算收入分月情况" ==text ){
	    		window.location.href="export.do?ctrl=expecel_ssTop";
	    	}else if("一般公共预算按税种占比情况" == text){
	    		window.location.href="export.do?ctrl=expecel_szzbAll";
	    	}else if("一般公共预算按行业占比情况" == text){
	    		window.location.href="export.do?ctrl=expecel_hyzbAll";
	    	}else if("一般公共预算收入前二十" == text){
	    		window.location.href="export.do?ctrl=expecel_exportop1";
	    	}else if("一般公共预算增幅前二十" == text){
	    		window.location.href="export.do?ctrl=expecel_ssZFTop";
	    	}else if("一般公共预算减幅前二十" == text){
	    		window.location.href="export.do?ctrl=expecel_ssJFTop";
	    	}
	    	
	    });
	});
	
</script>


</html>