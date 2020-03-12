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

<title>流程管理—企业迁入管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<script src="./css/layui/layui.js" charset="utf-8"></script>
<style type="text/css">
.sweet-alert{
	z-index:99999999;
    border: 1px solid #eee;
	box-shadow: 0 0 3px #c7b6b6;
}
</style>
</head>

<body style="overflow-x: hidden">
		<blockquote class="layui-elem-quote layui-text">本功能用于系统查询企业迁入信息！</blockquote>

		<form class="layui-form" id="form" action="">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<div class="layui-colla-content  layui-show">
						<div class="layui-row">
							<div class="layui-col-md4">
								<label class="layui-form-label">纳税人名称</label>
								<div class="layui-input-block">
									<input id="paramNsrmc" name="paramNsrmc" type="text" placeholder="请输入纳税人名称" class="layui-input">
								</div>
							</div>
							<div class="layui-col-md4">
                <label class="layui-form-label" style="width: 100px;">纳税人识别号</label>
                <div class="layui-input-inline">
                  <input id="paramNsrsbh" name="paramNsrsbh" type="text" placeholder="请输入纳税人识别号" class="layui-input">
                </div>
                <div class="layui-input-inline">
                 </div>
                 <div class="layui-input-inline">
                 </div>
                <a lay-submit class="layui-btn" id="search_btn" lay-filter="search_btn">查询</a>
              </div>
							
								
								<%--<button lay-submit class="layui-btn" id="add_btn" lay-filter="add_btn">新增</button>--%>
							
						</div>
					</div>
				</div>
			</div>

			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend>查询显示</legend>

				<table class="layui-table" id="qrtable" lay-filter="qrtable">

				</table>
				<div id="page1"></div>
				<script type="text/html" id="bar">
  				<a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="remove">删除</a>
				</script>
			</fieldset>
		</form>
		
		
	<div id="checkdetailmain" class="layui-hide" style="padding: 10px;">
      <form class="layui-form" id="check_form">
      	
      	<div class="layui-form-item">
          <label class="layui-form-label">纳税人识别号</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="NSRSBH-span"></span>
          </div>
        </div>
        
        <div class="layui-form-item">
          <label class="layui-form-label">纳税人名称</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="NSRMC-span"></span>
          </div>
        </div>
        
        <div class="layui-form-item">
          <label class="layui-form-label">现所属街道</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="XSSJD-span"></span>
          </div>
        </div>
         
        <div class="layui-form-item">
          <label class="layui-form-label">原迁入地</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="YQRD-span"></span>
          </div>
        </div>
        
      	 <div class="layui-form-item">
          <label class="layui-form-label">当年累计税款</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="DNLJSK-span"></span>
          </div>
        </div>
        
         <div class="layui-form-item">
          <label class="layui-form-label">去年累计税款</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="QNLJSK-span"></span>
          </div>
        </div>
       
       <div class="layui-form-item">
          <label class="layui-form-label">迁入日期</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="QRRQ-span"></span>
          </div>
        </div>
        
        <div class="layui-form-item">
          <label class="layui-form-label">迁入原因</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="QRYY-span"></span>
          </div>
        </div>
        
       
        <div class="layui-form-item">
          <label class="layui-form-label">创建人</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="CJR-span"></span>
          </div>
        </div>
        
       
        <div class="layui-form-item">
          <label class="layui-form-label">创建日期</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="CJRQ-span"></span>
          </div>
        </div>

      </form>
    </div>

    
	</body>

	<script>
	  var loadlayer;
    
    layui.use([ 'form', 'laydate' ], function() {
      
      var form = layui.form, layer = layui.layer, laydate = layui.laydate;
      
      laydate.render({
        elem: '#qrrq' //指定元素
      });
      
      ajax({
        url : "ajax.do?ctrl=qyqrglcx_queryInit",
        data : {},
        type : 'post',
        success : function(obj) {
          if (obj != null && obj.data != null) {
            var str = "<option value=''>请选择</option>";
            jdlist = obj.data.jdlist;
            var hylist = obj.data.hylist;
            for (var i = 0; i < jdlist.length; i++) {
              str += "<option value='"+jdlist[i].JD_DM+"'>"
                  + jdlist[i].JD_MC
                  + "</option>";
            }
              $("#xssjd").html(str);
            //getData();
            form.render('select');
          }
        }
      });
      form.render('select');
      
      form.on('submit(search_btn)', function() {
    	  loadlayer = layer.load();
        ajax({
          url : "ajax.do?ctrl=qyqrglcx_queryQyqr",
          data : {
            "nsrsbh" : $('input[name="paramNsrsbh"]').val(),
            "nsrmc" : $('input[name="paramNsrmc"]').val()
          },
          type : 'post',
          success : function(obj) {
            console.log(obj);
            getTable(obj.data);
            layer.close(loadlayer);
            layer.msg('查询成功！');
          }
        });
        return false;
      });
      
    /*  form.on('submit(add_btn)', function() {
        console.log("add-------------");
        $('#addmain').removeClass('layui-hide');
        //form.render();
        layer.open({
          type : 1,
          title : '新增企业迁入',
          area : [ '700px', '80%' ],
          content : $('#addmain')
        });
        return false;
      });*/
      
      form.on('submit(add_save_btn)', function(obj) {
        ajax({
          url : "ajax.do?ctrl=qyqrglcx_addQyqr",
          data : obj.field,
          type : 'post',
          success : function(obj) {
            console.log(obj);
            if(obj.code == '000') {
              layer.closeAll();
              layer.msg('新增成功！');
              getData();
            } else if(obj.code == '500') {
            	layer.msg('新增失败！已有该条迁入记录');
            } else {
              layer.msg('新增失败！');
            }
          }
        });
        return false;
      });
      
    });
  
    $(function() {
      getData();
    });
  
    function getData() {

      ajax({
        url : "ajax.do?ctrl=qyqrglcx_queryQyqr",
        data : {
          "nsrmc" : $("#paramNsrmc").val(),
          "nsrsbh" : $("#paramNsrsbh").val(),
        },
        type : 'post',
        success : function(obj) {
          console.log(obj);
          if (obj != null && obj.data != null) {
            var sjList = obj.data;
            console.log(sjList);
            getTable(sjList);
          }
        }
      });
    }
  
    function getTable(data) {
      layui.use('table', function(){
        var table = layui.table;
        
        //第一个实例
        table.render({
          elem: '#qrtable'
          ,height: 500
          ,data: data //数据
          ,width:document.body.clientWidth
          ,page: true //开启分页
          ,cols: [[ //表头
            {field: 'ID', title: 'ID', width:80, fixed: 'left'}
            ,{field: 'NSRSBH', title: '纳税人识别号', width:250}
            ,{field: 'NSRMC', title: '纳税人名称', width:400}
            ,{field: 'JDMC', title: '现所属街道', width:150} 
            ,{field: 'YQRD', title: '原迁入地', width: 150}
            ,{field: 'DNLJSK', title: '当年累计税款', width: 150}
            ,{field: 'QNLJSK', title: '去年累计税款', width: 150}
            ,{field: 'QRRQ', title: '迁入日期', width: 150}
            ,{field : '', title : '操作', toolbar : '#bar', fixed : 'right', width:100}
          ]]
        });
        
        
        var num = 0;
        layui.table.on('row(qrtable)', function(obj) {
       	  if(num == 1){
       		  num = 0;
       		  return false;
       	  }
        	  var data = obj.data; 
       	  $('#checkdetailmain').removeClass('layui-hide');
       	  console.log(obj.data);
       	  var qrrq='';
       	  if(obj.data.QRRQ!=undefined&obj.data.QRRQ!="undefined"&obj.data.QRRQ!=null){
       		qrrq=obj.data.QRRQ.substring(0, 10);
       	  }
       	  var cjrq='';
       	  if(obj.data.CJRQ!=undefined&obj.data.CJRQ!="undefined"&obj.data.CJRQ!=null){
       		cjrq=obj.data.CJRQ.substring(0, 10);
       	  }
       	  
       	  $('#NSRSBH-span').text(obj.data.NSRSBH);
       	  $('#NSRMC-span').text(obj.data.NSRMC);
       	  $('#XSSJD-span').text(obj.data.JDMC);
       	  $('#YQRD-span').text(obj.data.YQRD);
       	  $('#DNLJSK-span').text(obj.data.DNLJSK);
     	  $('#QNLJSK-span').text(obj.data.QNLJSK);
     	  $('#QRRQ-span').text(qrrq);
       	  $('#QRYY-span').text(obj.data.QRYY);
       	  
       	  $('#CJR-span').text(obj.data.CJR);
       	  
       	  $('#CJRQ-span').text(cjrq);
       
       	  
       /* 	  layer.open({
             type : 1,
             title : '详情',
             area : [ '600px', '600px' ],
             content : $('#checkdetailmain'),
             success : function() {
               //initjd();
             },
             cancel : function() {
               $('#checkdetailmain').hide();
 
             },
             end : function() {
               $('#checkdetailmain').hide();
             }
           }); */
        	  
       	  
        	  }) 
        
        
        
        
        
        layui.table.on('tool(qrtable)', function(obj) {
        	var data = obj.data; //获得当前行数据
         	var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
         	var tr = obj.tr; 
        	if(layEvent === 'remove'){
        	    console.log("remove----------");
                layer.confirm('', {icon: 2, title:'删除'
                ,area:['30%','27%']
                ,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">确定删除该条企业迁入记录?</div>'
                }, function(index){
              	  //do something
              	  ajax({
      	            url : "ajax.do?ctrl=qyqrglcx_removeQyqr",
      	            data : {
      	              "id" : obj.data.ID
      	            },
      	            type : 'post',
      	            success : function(obj) {
      	              console.log(obj);
      	              if(obj.code == '000') {
      	            	  getData();
      	            	  layer.msg('删除成功！');
      	              } else {
      	            	  layer.msg('删除失败！');
      	              }
      		        	  layer.close(index);
      	            }
      	          });
              	}); 
        	}
       			num=1
          
          
          
        });
        
      });
    }
    
  </script>
	

</html>
