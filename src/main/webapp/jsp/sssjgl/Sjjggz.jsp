-<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
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
<title>加工规则</title>
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
.layui-col-md2{

	width: 13%;
}
</style>
<script type="text/html" id="gz">
{{#  if(d.BS === '2'){ }}
	{{ '加工规则' }}
{{#  } else { }}
	{{ '拆分规则' }}
{{#  } }}
</script>


</head>

<body style="overflow-x: hidden">
	<div class="layui-container" style="margin-top: 15px;">
       <div class="jggz" style="width: 100%;">
        
        	<div class="layui-btn-group">
            	<button class="layui-btn right" style="margin-left: 18px!important;">数据加工</button>
        	</div>
        	<div id="main" class="demo-transfer">	
        	</div>
        	
        </div>
        
    </div>
</body>

<script>

layui.config({
	  base: 'layui_exts/transfer/'
	}).use(['transfer', 'layer', 'util'], function(){
		var $ = layui.$
		  ,transfer = layui.transfer
		  ,layer = layui.layer
		  ,util = layui.util;
	    //数据源
	    
	    //获取规则
	    var data1="";
	    
	    //获取拆分规则
	    getData();
	    
	    function getData() {
			temp="";
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "ajax.do?ctrl=Sjjggz_getMb",
				data :{
					date: ""
				},
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						data1=obj.data;
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
		
	    
	    console.log(data1);
	    data1 = data1;
	    var data2 = [];
	    
	  	//先上方
		up(data1,data2,transfer);
	  
	    
	    //transfer.get(参数1:初始化返回值,参数2:获取数据[all,left,right,l,r],参数:指定数据字段)
	    
	  	
	  	function up(data1,data2,transfer){
	  	    //表格列
		    var cols = [{type: 'checkbox', fixed: 'left'},{field: 'ID', title: '序号'},{field: 'MBMC', title: '模板名称'},{field: 'BS', title: '规则', sort: true, templet:'#gz'}]
		    //表格配置文件
		    var tabConfig = {'page':true,'limit':20,'height':600}
		    var tb1 = transfer.render({
		        elem: "#main", //指定元素
		        cols: cols, //表格列  支持layui数据表格所有配置
		        data: [data1,data2], //[左表数据,右表数据[非必填]]
		        tabConfig: tabConfig //表格配置项 支持layui数据表格所有配置
		    })
		    
		    //获取数据
		    $('.right').on('click',function () {
		        var data = transfer.get(tb1,'r');
		        console.log(JSON.stringify(data));
		        
		        //获取id,用","分割
		        doGz();
		        function doGz() {
		    		temp="";
		    		$.ajax({
		    			type : "post", //请求方式
		    			async : false, //是否异步
		    			url : "ajax.do?ctrl=Sjjggz_doClean",
		    			data :{
							data: JSON.stringify(data)
						},
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
		        
		    });
	  	}
	  	
	})
		
	</script>

</html>