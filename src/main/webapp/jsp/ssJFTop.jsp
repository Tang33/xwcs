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
		<legend>按行业占比情况（单位：万元）</legend>

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
          /* ,data: data //数据 */
          ,url:"${pageContext.request.contextPath}/show/hyzbAll.do"
          ,cols: [[ //表头
            {field: 'HYMC', title: '行业名称',align:'right'}
            ,{field: 'DNLJJE', title: '当年累计金额', align:'right', templet:'#jelj'}
            ,{field: 'TQLJJE', title: '同期累计金额',  align:'right',templet:'#jetq'}
            ,{field: 'ZF', title: '增幅', templet:'#zf',align:'right'}
            ,{field: 'ZZBL', title: '增长比例', templet:'#zzlx',align:'right'}
            ,{field: 'ZB', title: '占比', templet:'#zb',align:'right'}
            
            
          ]]
        });
        
      });
    }
    
    
  //导出
    $("#exportExcel").click(function() {
    	
    	window.location.href="../expecel/export.do?no=3";
    });
</script>
</html>