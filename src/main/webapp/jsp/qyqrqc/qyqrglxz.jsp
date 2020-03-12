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

<style type="text/css">
.sweet-alert{
	z-index:99999999;
    border: 1px solid #eee;
	box-shadow: 0 0 3px #c7b6b6;
}

table{
    width: 50%;
    height: 80%;
    text-align: right;
}
</style>
</head>

<body style="overflow-x: hidden">
		<blockquote class="layui-elem-quote layui-text">本功能用于新增企业迁入信息！</blockquote>
		<form class="layui-form" id="form" action="">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<div class="layui-colla-content  layui-show">
					<table>
						<tr>
							<td>纳税人识别码</td>
							<td><input style="width: 100%;margin-left: 10%;" type="text" id="nsrsbh" name="nsrsbh" class="layui-input" lay-verify="required"></td>
						</tr>
						<tr>
							<td>纳税人名称</td>
							<td><input style="width: 100%;margin-left: 10%;" type="text" id="nsrmc" name="nsrmc" class="layui-input" lay-verify="required" ></td>
						</tr>
						<tr>
							<td>现所属街道</td>
							<td><div class="layui-input-block" style="width: 100%;margin-left: 10%;">
									<select id="xssjd" name="xssjd" lay-filter="xssjd"
										lay-verify="required">
										<option value="%">全部</option>
										<c:forEach items="${jdlist}" var="s">
											<option value="${s.JD_DM}">${s.JD_MC}</option>
										</c:forEach>
									</select>
								</div></td>
						</tr>
						<tr>
							<td>原迁入地</td>
							<td><input style="width: 100%;margin-left: 10%;" type="text" id="yqrd" name="yqrd" class="layui-input" ></td>
						</tr>
						<tr>
							<td>当年累计税款（全口径）/元</td>
							<td><input style="width: 100%;margin-left: 10%;" type="text" id="dnljsk" name="dnljsk" class="layui-input" ></td>
						</tr>
						<tr>
							<td>去年累计税款（全口径）/元</td>
							<td><input style="width: 100%;margin-left: 10%;" type="text" id="qnljsk" name="qnljsk" class="layui-input" ></td>
						</tr>
						<tr>
							<td>迁入日期</td>
							<td><input style="width: 100%;margin-left: 10%;" type="text" id="qrrq" name="qrrq" class="layui-input" ></td>
						</tr>
						<tr>
							<td>迁入原因</td>
							<td><textarea style="width: 100%;margin-left: 10%;" rows="" cols="" id="qryy" name="qryy" class="layui-textarea" ></textarea></td>
						</tr>
						<tr>
							<td colspan="2"><button style="margin-right: 25%;" lay-submit class="layui-btn" id="add_save_btn" lay-filter="add_save_btn" >保存</button></td>
						</tr>
					</table>

					<%-- <div id="addmain" class="open" style="width: 70%;text-align: left;">
									<div class="layui-form-item" >
										<label class="layui-form-label">纳税人识别码</label>
										<div class="layui-input-block">
											<input type="text" id="nsrsbh" name="nsrsbh" class="layui-input" lay-verify="required" >
										</div>
									</div>

									<div class="layui-form-item">
										<label class="layui-form-label" >纳税人名称</label>
										<div class="layui-input-block">
											<input type="text" id="nsrmc" name="nsrmc" class="layui-input" lay-verify="required" >
										</div>
									</div>

									<div class="layui-form-item">
										<label class="layui-form-label" >现所属街道</label>
										<div class="layui-input-block" >
											<select id="xssjd" name="xssjd" lay-filter="xssjd"  lay-verify="required" >
												<option value="%" >全部</option>
												<c:forEach items="${jdlist}" var="s">


													<option value="${s.JD_DM}">${s.JD_MC}</option>
												</c:forEach>
											</select>
										</div>
									</div>

									<div class="layui-form-item">
										<label class="layui-form-label" >原迁入地</label>
										<div class="layui-input-block">
											<input type="text" id="yqrd" name="yqrd" class="layui-input" >
										</div>
									</div>
									<div class="layui-form-item">
										<label class="layui-form-label" >当年累计税款（全口径）/元</label>
										<div class="layui-input-block">
											<input type="text" id="dnljsk" name="dnljsk" class="layui-input" >
										</div>
									</div>
									<div class="layui-form-item">
										<label class="layui-form-label" >去年累计税款（全口径）/元</label>
										<div class="layui-input-block">
											<input type="text" id="qnljsk" name="qnljsk" class="layui-input" >
										</div>
									</div>
									<div class="layui-form-item">
										<label class="layui-form-label" >迁入日期</label>
										<div class="layui-input-block">
											<input type="text" id="qrrq" name="qrrq" class="layui-input" >
										</div>
									</div>
									<div class="layui-form-item">
										<label class="layui-form-label" >迁入原因</label>
										<div class="layui-input-block">
											<textarea rows="" cols="" id="qryy" name="qryy" class="layui-textarea" ></textarea>
										</div>
									</div>
									<div class="layui-form-item" style="text-align: right" >
										<button lay-submit class="layui-btn" id="add_save_btn" lay-filter="add_save_btn" >保存</button>
									</div>
							</div> --%>


						</div>
					</div>
				</div>
			</div>
<%--
			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend>新增记录</legend>

				<table class="layui-table" id="qrtable" lay-filter="qrtable">
				</table>
				<div id="page1"></div>
				<script type="text/html" id="bar">
  				<a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="remove">删除</a>
				</script>
			</fieldset>--%>
		</form>

	</body>

	<script>
	  var loadlayer;
    
    layui.use([ 'form', 'laydate' ], function() {
      
      var form = layui.form, layer = layui.layer, laydate = layui.laydate;
      
      laydate.render({
        elem: '#qrrq' //指定元素
      });
      
      ajax({
        url : "ajax.do?ctrl=qyqrglxz_queryInit",
        data : {},
        type : 'post',
        success : function(obj) {
          if (obj != null && obj.data != null) {
            var str = "<option value=''>请选择</option>";
            jdlist = obj.data.jdlist;
            var hylist = obj.data.hylist;
            for (var i = 0; i < jdlist.length; i++) {
                if(jdlist[i].JD_DM==20||jdlist[i].JD_DM==00||jdlist[i].JD_DM==01||jdlist[i].JD_DM==25){
                    continue;
                }
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
          url : "ajax.do?ctrl=qyqrglxz_queryQyqr",
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

      form.on('submit(add_save_btn)', function(obj) {
        ajax({
          url : "ajax.do?ctrl=qyqrglxz_addQyqr",
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
        url : "ajax.do?ctrl=qyqrglxz_queryQyqr",
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
          ,page: true //开启分页
          ,cols: [[ //表头
            {field: 'ID', title: 'ID', width:80, fixed: 'left'}
            ,{field: 'NSRSBH', title: '纳税人识别号', width:200}
            ,{field: 'NSRMC', title: '纳税人名称', width:200}
            ,{field: 'JDMC', title: '现所属街道', width:100} 
            ,{field: 'YQRD', title: '原迁入地', width: 100}
            ,{field: 'DNLJSK', title: '当年累计税款', width: 200}
            ,{field: 'QNLJSK', title: '去年累计税款', width: 200}
            ,{field: 'QRRQ', title: '迁入日期', width: 150}
            ,{field : '', title : '操作', toolbar : '#bar', fixed : 'right', width:80}
          ]]
        });
        
        layui.table.on('tool(qrtable)', function(obj) {
          console.log("remove----------");
          layer.confirm('', {icon: 2, title:'删除'
          ,area:['30%','27%']
          ,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">确定删除该条企业迁入记录?</div>'
          }, function(index){
        	  //do something
        	  ajax({
	            url : "ajax.do?ctrl=qyqrglxz_removeQyqr",
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
          
        });
        
      });
    }
    
  </script>
	

</html>
