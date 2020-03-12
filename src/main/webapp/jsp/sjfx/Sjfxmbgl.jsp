<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<base href="<%=basePath%>">

<title>数据分析模板管理</title>

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

	<input type="checkbox" name="zt" value="{{d.id}}" lay-skin="switch" lay-text="启用|不启用" lay-filter="ztDemo" {{ d.zt == 1 ? 'checked' : '' }}>
</script>
</head>

<body style="overflow-x: hidden">

			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend>数据分析模板管理</legend>

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
    	  console.log(obj.elem.checked);
    	  
    	  var status='';
    	  if(obj.elem.checked==true){
    		  
    		  status=1;
    	  }else{
    		  
    		  status=0;
    	  }
    	   var id=this.value;
    	  //修改接口
    	  ajax({
				url : "ajax.do?ctrl=Sjfxmbgl_modify",
				data : {
					"id" : id,
					"status" : status
					
				},
				type : 'post',
				success : function(obj) {
					/* console.log(obj); */
				}
		 });
    	   
    	   
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
          ,height: 500
          /* ,data: data //数据 */
          ,url:"ajax.do?ctrl=Sjfxmbgl_querymb"
          ,limit:10
          ,page: true //开启分页
          ,cols: [[ //表头
            {field: 'id', title: 'ID', fixed: 'left'}
            ,{field: 'mbmc', title: '模板名称'}
            ,{field: 'cjsj', title: '创建时间'}
            ,{field: 'zt', title: '状态', templet:'#zt'}
          ]]
        });
        
      });
    }
    
  </script>
	

</html>
