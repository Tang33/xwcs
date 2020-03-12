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
<script type="text/html" id="jd">
	{{#  if(d.JD == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.JD.replace("街道", "").replace("玄武区", "") }}
    {{#  } }}
</script>
<script type="text/html" id="zhs">
	{{#  if(d.ZHS == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.ZHS }}
    {{#  } }}
</script>
<script type="text/html" id="jshs">
	{{#  if(d.JSHS == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.JSHS }}
    {{#  } }}
</script>
<script type="text/html" id="jshszb">
	{{#  if(d.JSHSZB == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.JSHSZB }}
    {{#  } }}
</script>
<script type="text/html" id="jnssje">
	{{#  if(d.JNSSJE == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.JNSSJE }}
    {{#  } }}
</script>
<script type="text/html" id="jnsszb">
	{{#  if(d.JNSSZB == undefined){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ d.JNSSZB }}
    {{#  } }}
</script>
</head>
<body>
	<button type="button" class="layui-btn " id="exportExcel" style="float: right;margin-right: 20px;margin-top: 20px;">
						导出
	</button>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
		<legend>按街道分析</legend>

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
          ,url:"${pageContext.request.contextPath}/syqkfx/JDDatamining.do"
          ,cols: [[ //表头
            {field: 'JD', title: '街道',align:'right',templet:'#jd'}
            ,{field: 'ZHS', title: '总户数', align:'right',templet:'#zhs'}
            ,{field: 'JSHS', title: '见税户数',  align:'right',templet:'#jshs'}
            ,{field: 'JSHSZB', title: '见税户数占比', align:'right',templet:'#jshszb'}
            ,{field: 'JNSSJE', title: '今年税收金额（元）', align:'right',templet:'#jnssje'}
            ,{field: 'JNSSZB', title: '今年税收金额占比', align:'right',templet:'#jnsszb'}          
          ]]
        });
        
      });
    }
    
    
    //导出
    $("#exportExcel").click(function() {
    	
    	window.location.href="../syqkfx/export.do?method=jd";
    });
    
</script>
</html>