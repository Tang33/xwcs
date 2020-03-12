<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> --%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<base href="<%=basePath%>">
<title>规则管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<jsp:include page="../load.jsp"></jsp:include>
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
{{#  if(d.BS === '1'){ }}
	{{ '加工规则' }}
{{#  } else { }}
	{{ '拆分时规则' }}
{{#  } }}
</script>
</head>
<body style="overflow-x: hidden;">

	<button type="button" class="layui-btn" id="right" style="position:absolute;z-index:2;height: 42px;cursor: pointer!important;margin-left: 45.5%;margin-top: 460px;">
													数据加工
	</button>


	<a href="javascript:void(0);" id="open_id" style="position:absolute;z-index:2;height: 42px;cursor: pointer!important;margin-left: 46.6%;margin-top:530px;">任务详情</a>
	<div style="float:left;margin-top:15px;margin-left:1.5%"><span>规则待选区：</span></div>	
    <div style="float:right;margin-top:15px;margin-right:39.5%"><span>需运行规则：</span></div>
	<div class="layui-container" style="margin-top: 35px;position:absolute;z-index:1;">
       <div class="jggz" style="width: 100%;">
        
        	<div id="main" class="demo-transfer">
        	</div>
        	
        </div>
        
    </div>
</body>
<div id="div_id" style="display:none">
	<table id="test3"  style="table-layout: fixed; width: 100%;"></table>
</div>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>

function showLoad() {
    return layer.msg('拼命执行规则中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:100000});
}
layui.config({
	  base: 'layui_exts/transfer/'
	}).use(['transfer', 'layer', 'util'], function(){
	
		var $ = layui.$
		  ,transfer = layui.transfer
		  ,layer = layui.layer
		  ,util = layui.util;
		
		//获取规则
	    var data1="";
	    
	    //获取拆分规则
	    getData();
	    
	    //获取右侧的内容
	    getRita();
	    function getData() {
			temp="";
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				//url : "ajax.do?ctrl=Sjjggz_getMb",
					url:"sjjg_getMb.do",
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
	    
	    data1 = data1;
	    var data2 = data2;
	    
	  	//先上方
		up(data1,data2,transfer);
	  
	    
	    
	    function getRita() {
			temp="";
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				//url : "ajax.do?ctrl=Sjjggz_getRule",
				url : "sjjg_getRule.do",
				data :{
					date: ""
				},
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						data2=obj.data;
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
	    
	    var tb1;
	    function up(data1,data2,transfer){
	  	    //表格列
		    var cols = [{type: 'checkbox', fixed: 'left'},{field: 'ID', title: '序号'},{field: 'MBMC', title: '模板名称'},{field: 'BS', title: '规则', sort: true, templet:'#gz'}]
		    //表格配置文件
		    var tabConfig = {'page':true,'limit':20,'height':700}
		    tb1 = transfer.render({
		        elem: "#main", //指定元素
		        cols: cols, //表格列  支持layui数据表格所有配置
		        data: [data1,data2], //[左表数据,右表数据[非必填]]
		        tabConfig: tabConfig //表格配置项 支持layui数据表格所有配置
		    })
		    
		    
	  	}
	    //数据加工验证
	    function sjjgyz(test2){
    		$.ajax({
	 	           url:"sjjg_ifTable.do",
	 	           dataType:'json',
	 	           type:'post',
	 	      		success:function(json){
		 	      		var code = json.data.code
		 	    		if(code=='0'){
		 	    			$('#right').attr("disabled",false);
		 				    $('#right').html("数据加工");
		 				 //清除Interval的定时器,传入变量名(创建Interval定时器时定义的变量名)
							clearInterval(test2);
		 	    		}else {
		 	    			$('#right').attr("disabled",true);
		 				    $('#right').html("加工中...");
		 				}
	 	      		}
		       		
		       	});
    	}
		
	    //点击数据加工
		$('#right').on('click',function () {
			layer.confirm("请确认需运行规则，加工后数据将无法修改，若可进行加工请点击确认！", {
				icon: 7, 
				title:'警告',
				offset:  ['30%', '38%'],
        	}, function(index) {
        		//showLoad();
        		var m = layer.msg('拼命执行规则中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto',time:false});
        		 var test2 = setInterval(function(){
					  sjjgyz(test2);
					  /* console.log("调用数据加工验证10秒一次") */
					},300000)
        		doClean(m);
    			
            });
			
			
            
		});
		
		
		function doClean(m){
			
			var data = transfer.get(tb1,'r');
			temp="";
    		$.ajax({
    			type : "post", //请求方式
    			async : true, //是否异步
    			//url : "ajax.do?ctrl=Sjjggz_doClean",
    				url:"sjjg_doClean.do",
    			data :{
					data: JSON.stringify(data)
				},
    			dataType : "json",
    			timeout: 1000*60*30,
    			success : function(obj) {
    				
    				if (obj != null && obj.code == '000') {
    					layer.alert("正在等待数据加工，可在下方任务详情查看。",{icon: 1},function () {
						    layer.close(layer.index);
						    layer.close(m);
						    $('#right').attr("disabled",true);
				    		$('#right').html("加工中...");
			            }); 
    				}else if (obj != null && obj.code == '008') {
    					layer.alert(obj.msg,{icon: 1},function () {
						    layer.close(layer.index);
						    layer.close(m);
    					 });    
    					
    				}else{
    					layer.alert(obj.msg,{icon: 2},function () {
						    layer.close(layer.index);
						    layer.close(m);
						    $('#right').attr("disabled",false);
						    $('#right').html("数据加工");
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
})
    layui.use(["layer","tree","table","element"],function(){
    	
    	var layer=layui.layer,tree=layui.tree,element = layui.element;
    	table=layui.table;
    	$(function () {
    		var code = '${JGZT_SJJG}'
    		if(code=='0'){
    			$('#right').attr("disabled",false);
			    $('#right').html("数据加工");
    		}else {
    			$('#right').attr("disabled",true);
			    $('#right').html("加工中...");
			}
    		initExcel()
		})
		
		$('#open_id').click(function(){
			
			initExcel()
			layer.open({
				  type: 1, 
				  area: ['750px', '470px'],
				  content: $('#div_id') //这里content是一个普通的String
				  ,end :function(){
					  $('#div_id').hide();
					  $.ajax({
			 	           url:"sjjg_ifTable.do",
			 	           dataType:'json',
			 	           type:'post',
			 	      		success:function(json){
				 	      		var code = json.data.code
				 	    		if(code=='0'){
				 	    			$('#right').attr("disabled",false);
				 				    $('#right').html("数据加工");
				 				 //清除Interval的定时器,传入变量名(创建Interval定时器时定义的变量名)
									clearInterval(test2);
				 	    		}else {
				 	    			$('#right').attr("disabled",true);
				 				    $('#right').html("加工中...");
				 				}
			 	      		}
				       		
				       	});
						 

						 
						
				  }
				});
		})
		
    	
    	
    	function initExcel(){
    		
    		$.ajax({
 	           url:"sjjg_getTable.do",
 	           
 	           dataType:'json',
 	           type:'post',
 	           success:function (json) {
 	        	   
 	        	   var da = json.data
 	               if(json.code!='000'){
 	               	layer.msg(json.msg, {icon: 2,time:3000,shade:0.2,offset:["300px"]});
 	               }else{    
 	            	   table.render({
 	       				id:"qddata"
 	       				,elem: '#test3'
 	       			    ,height:390
 	       			    ,width:730
 	       				,data: da
 	       			   // ,limit: da.length
 	       			    ,limits: [10,20,30]
 	       				,page: true //是否显示分页
 	       			    ,cols: [[
 	       			    	{field: 'ID', title: '编号',align:"center",width:80}
 	       			    	,{field: 'NAME', title: '任务名称',align:"center",width:180}
 	       			    	,{field: 'STARTTIME', title: '创建时间',align:"center",width:200}
 	       			    	,{field: 'ZT', title: '状态',align:"center",width:180,templet:function(d){
 	       			    			if(d.ZT=='0'){
 	       			    				return "已加工完成";
 	       			    			}else if(d.ZT=='1'){
 	       			    				return "正在加工中";
 	       			    			}else if(d.ZT=='2'){
 	       			    				return "加工错误";
 	       			    			}else {
 	       			    				return "已失效";
 	       			    			}
 	       			    		}
 	       			    	}
 	       			    	
 	       			    	,{field: 'ID', title: '操作',align:"center",width:80,templet: function(d){
 	       						
 	       				    	  return '<span class="task-del" task-id="'+d.ID+'">删除</span>'
 	       			      		}}
 	       					
 	       			    
 	       			        
 	       			    ]],done: function(res, page, count){
 	       			    
 	       				 
 	       			    // 删除任务
 	       				$('.task-del').click(function() {
 	       					var id = $(this).attr('task-id');
 	       					layer.confirm("确定删除吗?删除后不可恢复",{
 	       			       		offset:["300px"]
 	       			       		,time: 8000
 	       			       		,btn: ['删除', '取消']
 	       			       	},function(index){
 	       			       		
 	       			       	$.ajax({
 	       		 	           url:"sjjg_deleteTable.do",
 	       		 	           data:{
 	       		 	        	   id:id

 	       		 	           },
 	       		 	           dataType:'json',
 	       		 	           type:'post',
 	       		 	      		success:function(json){
 	       		 	      			
	 	       		 	      		var code = json.data.code
	 	       		 	      		
	 	       		 	      		console.log("加工状态码！"+code)
	 	       		 	    		if(code=='0'){
	 	       		 	    			$('#right').attr("disabled",false);
	 	       		 				    $('#right').html("数据加工");
	 	       		 	    		}else {
	 	       		 	    			$('#right').attr("disabled",true);
	 	       		 				    $('#right').html("加工中...");
	 	       		 				}
 	       		 	      			if(json.code!='000'){
 	       		 	      			layer.msg(json.msg, {icon: 2,time:3000,shade:0.2,offset:["300px"]});	
 	       		 	      			initExcel();
 	       		 	      			return;
 	       		 	      			}
 	       		 	      		layer.msg(json.msg, {icon: 1,time:1000,shade:0.2,offset:["300px"]});
 	       		 	      		initExcel();
 	       		 	      		}
 	       			       		
 	       			       	});
 	       			       	
 	       			       	layer.close(index);
 	       			       	
 	       			    	});//end	
 	       			    });
 	         	    }
 	            	   
 	            	   
 	            	   
 	            	   
 	       			  });	
 	       			  
 	       	}
 	       	
 	           }
 	       })
    };
	 
	 

   });

</script>


</html>