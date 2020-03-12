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
<script type="text/html" id="zf">
	{{#  if(d.ZF == null){ }}
		 {{ "-" }}
	{{#  } else { }}
		 {{ (parseFloat(d.ZF) / 10000).toFixed(2) }}
    {{#  } }}
</script>
<script type="text/html" id="jelj">
	{{#  if(d.DNLJJE == null){ }}
		 {{ "" }}
	{{#  } else { }}
		 {{ (parseFloat(d.DNLJJE) / 10000).toFixed(2) }}
    {{#  } }}
</script>
<script type="text/html" id="jetq">
	{{#  if(d.TQLJJE == null){ }}
		 {{ "" }}
	{{#  } else { }}
		 {{ (parseFloat(d.TQLJJE) / 10000).toFixed(2) }}
    {{#  } }}
</script>
</head>
<body>
	<button type="button" class="layui-btn " id="exportExcel" style="float: right;margin-right: 20px;margin-top: 20px;">
						导出
	</button>
	<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
		<legend>税务总体情况</legend>

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
          ,url:"${pageContext.request.contextPath}/syqkfx/querySwztqkDetail.do"
          ,cols: [[ //表头
            {field: 'KZZTLX', title: '课征主体类型',align:'center',templet:'#KZZTLX'}
            ,{field: 'ZHS', title: '总户数',  align:'center',templet:'#ZHS'}
            ,{field: 'JSHS', title: '见税户数',  align:'right',templet:'#JSHS'}
            ,{field: 'JSHSZB', title: '见税户数占比', templet:'#JSHSZB',align:'center'}
            ,{field: 'JNSSJE', title: '今年税收金额', templet:'#JNSSJE',align:'center'}
            ,{field: 'JNSSZB', title: '今年税收占比', templet:'#JNSSZB',align:'center',
            	templet: function (d) {
            		if(d.JNSSZB!='undefined'){
            			return (parseFloat(d.JNSSZB) / 1).toFixed(2)+'%'
            		}else{
            			return''
            		}
            	}
     		}
          ]]
        })
      })
    }
    
    
    //导出
    $("#exportExcel").click(function() {
    	
    	window.location.href="../syqkfx/export.do?method=sw";
    });
</script>
</html>