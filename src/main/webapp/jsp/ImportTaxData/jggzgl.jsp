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

<title>加工规则管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->

<style type="text/css">
.sweet-alert{
	z-index:99999999;
    border: 1px solid #eee;
	box-shadow: 0 0 3px #c7b6b6;
}
</style>

<script type="text/html" id="zt">

	<input type="checkbox" name="ZT" value="{{d.ID}}" lay-skin="switch" lay-text="启用|不启用" lay-filter="ztDemo" {{ d.ZT == 1 ? 'checked' : '' }}>
</script>
<script type="text/html" id="barDemo">
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
</head>

<body style="overflow-x: hidden">
<blockquote class="layui-elem-quote layui-text">本功能页面用于对加工规则的管理！</blockquote>
			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend>规则管理</legend>

				<table class="layui-table" id="mb" lay-filter="mb">
				
				</table>
			</fieldset>
	</body>

	<script>
    
    layui.use([ 'form', 'laydate' ], function() {
      
      var form = layui.form, layer = layui.layer, laydate = layui.laydate;
      
      
      //监听
      form.on('switch(ztDemo)', function(obj){
    	   //获取行内容,调用接口
    	  /* layer.tips(this.value + ' ' + this.name + '：'+ obj.elem.checked, obj.othis); */
    	
    	  var status='';
    	  if(obj.elem.checked==true){
    		  
    		  status=1;
    	  }else{
    		  
    		  status=0;
    	  }
    	  debugger;
    	   var id=this.value;
    	  //修改接口
    	  ajax({
				//url : "ajax.do?ctrl=Gzzs_modify",//updatejggzStatus.do
				url : "jggzgl/updatejggzStatus.do",//updatejggzStatus.do
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
	      table.on('tool(mb)', function(obj) { 
	    	  var data = obj.data;
	       	  if (obj.event === 'del') {
	   				layer.confirm('', {btn: ['是','否']
	   				,area:['30%','27%']
	   				,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">是否确认删除？</div>'
	               }, function(index){ 
	            	   $.ajax({
	    					type : "post",
	    					url : 'jggzgl/deleteJggz.do',//
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
	     	 	}
	         });
    });
  
    $(function() {
    	getTable();
    });
  
    function getTable(data) {
      layui.use('table', function(){
        var table = layui.table;
        
        //第一个实例
        table.render({
          elem: '#mb'
          ,height: 680
          ,width:document.body.clientWidth-20
          ,url:"jggzgl/jggzquery.do"
          ,limit:15
          ,limits : [ 15, 30, 45, 60 ]

          ,page: true //开启分页
          ,cols: [[ //表头
            {field: 'ID', title: 'ID', fixed: 'left',width:'160'}
            ,{field: 'MBMC', title: '模板名称',width:'290'}
            ,{field: 'MS', title: '规则描述',width:'490'}
            ,{field: 'CJSJ', title: '创建时间',width:'210'}
            ,{field: 'ZT', title: '状态', templet:'#zt',width:'250'}
            ,{fixed : 'right',title : '操作',toolbar : '#barDemo',width :'255'}
          ]]
        });
        
      });
    }
    
  </script>
  
</html>
