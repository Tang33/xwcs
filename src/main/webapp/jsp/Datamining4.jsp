<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<link rel="stylesheet" href="../static/layui/css/layui.css" media="all">
<script type="text/javascript" src="../static/layuicms/layui/layui.js"></script>
<script type="text/javascript" src="../static/js/jquery.min.js"></script>
<script type="text/html" id="DJRQLX">
	{{#  if(d.DJRQLX == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.DJRQLX }}
    {{#  } }}
</script>
<script type="text/html" id="ZHS">
	{{#  if(d.ZHS == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.ZHS }}
    {{#  } }}
</script>
<script type="text/html" id="JSHS">
	{{#  if(d.JSHS == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.JSHS }}
    {{#  } }}
</script>
<script type="text/html" id="SEVENJSHS">
	{{#  if(d.SEVENJSHS == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.SEVENJSHS }}
    {{#  } }}
</script>
<script type="text/html" id="SEVENJSHSZB">
	{{#  if(d.SEVENJSHSZB == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.SEVENJSHSZB }}
    {{#  } }}
</script>
<script type="text/html" id="EIGHTJSHS">
	{{#  if(d.EIGHTJSHS == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.EIGHTJSHS }}
    {{#  } }}
</script>
<script type="text/html" id="EIGHTJSHSZB">
	{{#  if(d.EIGHTJSHSZB == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.EIGHTJSHSZB }}
    {{#  } }}
</script>
<script type="text/html" id="NINEJSHS">
	{{#  if(d.NINEJSHS == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.NINEJSHS }}
    {{#  } }}
</script>
<script type="text/html" id="NINEJSHSZB">
	{{#  if(d.NINEJSHSZB == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.NINEJSHSZB }}
    {{#  } }}
</script>
<script type="text/html" id="SEVENSSJE">
	{{#  if(d.SEVENSSJE == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.SEVENSSJE }}
    {{#  } }}
</script>
<script type="text/html" id="SEVENSSJEZB">
	{{#  if(d.SEVENSSJEZB == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.SEVENSSJEZB }}
    {{#  } }}
</script>
<script type="text/html" id="EIGHTSSJE">
	{{#  if(d.EIGHTSSJE == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.EIGHTSSJE }}
    {{#  } }}
</script>
<script type="text/html" id="EIGHTSSJEZB">
	{{#  if(d.EIGHTSSJEZB == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.EIGHTSSJEZB }}
    {{#  } }}
</script>
<script type="text/html" id="NINETSSJE">
	{{#  if(d.NINETSSJE == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.NINETSSJE }}
    {{#  } }}
</script>
<script type="text/html" id="NINETSSJEZB">
	{{#  if(d.NINETSSJEZB == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.NINETSSJEZB }}
    {{#  } }}
</script>

</head>
<body style="overflow-x: scroll;">
	<button type="button" class="layui-btn " id="exportExcel" style="float: right;margin-right: 20px;margin-top: 20px;">
						导出
	</button>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
		<legend>按年份分析</legend>

		<table class="layui-table" id="mb" lay-filter="mb">
		
		</table>
	</fieldset>	
</body>
<script>
    
    layui.use([ 'form', 'laydate' ], function() {
      
      var form = layui.form, layer = layui.layer, laydate = layui.laydate;
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
          ,height: 'auto'
          ,url:"${pageContext.request.contextPath}/syqkfx/YEARDatamining.do"
          ,cols: [[ //表头
            {field: 'DJRQLX', title: '登录日期类型',align:'right',templet:'#DJRQLX',width:150}
            ,{field: 'ZHS', title: '总户数', align:'right',templet:'#ZHS',width:150}
            ,{field: 'JSHS', title: '见税户数',  align:'right',templet:'#JSHS',width:150}
            ,{field: 'SEVENJSHS', title: '2017见税户数', align:'right',templet:'#SEVENJSHS',width:150}
            ,{field: 'SEVENJSHSZB', title: '2017见税户数占比', align:'right',templet:'#SEVENJSHSZB',width:150}
            ,{field: 'EIGHTJSHS', title: '2018见税户数', align:'right',templet:'#EIGHTJSHS',width:150}  
            ,{field: 'EIGHTJSHSZB', title: '2018见税户数占比', align:'right',templet:'#EIGHTJSHSZB',width:150}   
            ,{field: 'NINEJSHS', title: '2019见税户数', align:'right',templet:'#NINEJSHS',width:150}   
            ,{field: 'NINEJSHSZB', title: '2019见税户数占比', align:'right',templet:'#NINEJSHSZB',width:150}   
            ,{field: 'SEVENSSJE', title: '2017税收金额（元）', align:'right',templet:'#SEVENSSJE',width:170}   
            ,{field: 'SEVENSSJEZB', title: '2017税收金额占比', align:'right',templet:'#SEVENSSJEZB',width:150}   
            ,{field: 'EIGHTSSJE', title: '2018税收金额（元）', align:'right',templet:'#EIGHTSSJE',width:170}   
            ,{field: 'EIGHTSSJEZB', title: '2018税收金额占比', align:'right',templet:'#EIGHTSSJEZB',width:150} 
            ,{field: 'NINETSSJE', title: '2019税收金额（元）', align:'right',templet:'#NINETSSJE',width:170} 
            ,{field: 'NINETSSJEZB', title: '2019税收占比', align:'right',templet:'#NINETSSJEZB',width:150} 
          ]]
        });
        
      });
    }
    
    
    //导出
    $("#exportExcel").click(function() {
    	window.location.href="../syqkfx/export.do?method=year";
    });
    
</script>
</html>