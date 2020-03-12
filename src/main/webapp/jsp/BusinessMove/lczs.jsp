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

<title>流程管理—企业迁出管理</title>

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
<input type="hidden" id="dwid" value="${dwid}" />
		<blockquote class="layui-elem-quote layui-text">本功能用于对系统的“企业迁出终审”进行操作管理！</blockquote>
		<form class="layui-form" id="form" action="">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<div class="layui-colla-content  layui-show">
					<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md4">
										<div class="layui-inline">
											<label class="layui-form-label">查询时间</label>
											<div class="layui-input-inline">
						  						<input type="text" class="layui-input" id="yearNmonth" name="yearNmonth" placeholder="请选择日期">
											</div>
								 		</div>
									</div>
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<label class="layui-form-label">街道</label>
											<div class="layui-input-block">
												<select name="ssjd" lay-filter="ssjd" id="ssjd">
													<option value="0">请选择</option>
													<option value="91">新街口</option>
													<option value="92">梅园</option>
													<option value="93">后宰门</option>						
													<option value="94">玄武湖</option>					
													<option value="95">玄武门</option>					
													<option value="96">锁金村</option>					
													<option value="97">孝陵卫</option>					
													<option value="98">红山</option>					
													<option value="99">徐庄</option>
												</select>
											</div>
										</div>
									</div>
								</div>

							</div>
						</div>

						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<label class="layui-form-label">纳税人名称</label>
											<div class="layui-input-block">
												<input style="width: 80%;" id="paramNsrmc" name="paramNsrmc" type="text" placeholder="请输入纳税人名称" class="layui-input">
											</div>
										</div>
									</div>
									
									<div class="layui-col-md4">
										<div class="layui-form-item">
											 <label class="layui-form-label" style="width: 20%;margin-left: -6%;">纳税人识别号</label>
				                			<div class="layui-input-inline">
				                 	 			<input style="width: 241%;" id="paramNsrsbh" name="paramNsrsbh" type="text" placeholder="请输入纳税人识别号" class="layui-input">
				                			</div>
										</div>
									</div>
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<div class="layui-row">
												<div class="layui-col-md6">
													<div style="text-align: center;">
														<div class="layui-btn-group">
															<button lay-submit class="layui-btn" id="search_btn" lay-filter="search_btn" >查询</button>
														</div>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
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

			<a class="layui-btn layui-btn-xs" lay-event="CK">查看</a>
 			{{# var dwid = $("#dwid").val();}}
		 {{# if(dwid == "01" && d.STATUS =="1"){ }}
  				  <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="ZS">终审</a>
 				  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="remove">终止</a>
          {{# } }}	
				</script>
			</fieldset>
		</form>
    <div id="editmain" style="display: none;">
		<form class="layui-form" id="form5" action="" style="margin-top: 50px;">	
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;">终审意见:</label>
				<div class="layui-input-inline" style="width: 50%;">
					<textarea rows="" cols="" id="ly" name="ly" class="layui-textarea" lay-verify="required"></textarea>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label" style="width: 150px;"></label>
				<div class="layui-input-inline" style="width: 50%;">
					<div style="text-align: right;">
						<button class="layui-btn layui-btn-normal" id="updateSave"
							type="button" lay-submit="" lay-filter="updateSave">确认</button>
					</div>
				</div>
			</div>
		</form>
	</div>
	</body>

	<script>
	  var loadlayer;
	  
	
    
    layui.use([ 'form', 'laydate','table' ], function() {
      
      var form = layui.form, layer = layui.layer, laydate = layui.laydate,table = layui.table;
      
	    $(function() {
	  		setTimeout("getData()", 600);
	  	});
      
      
    	//年月范围
		laydate.render({
			elem : '#yearNmonth',
			type : 'month',
			range : true
		});
      
		form.on('submit(search_btn)', function(data) {
			loadlayer = layer.load();
			getData();
			layer.close(loadlayer);
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
      
    });
    
    function getQyTable(data) {
      //执行渲染
      layui.use([ 'table' ], function() {
        layui.table.render({
          elem : '#table-qy',
          height : 500, //数据接口
          data : data,
          page : true,
          cols : [ [ //表头
            {
              field : 'NSRMC',
              title : '企业名称',
              width:'80%'
            }, 
            {
              field : '',
              title : '操作',
              toolbar : '#select-bar',
              fixed : 'right',
              width:'20%'
            }
          ] ]
        });

      });
 
    }
    

    
    
    function getData() {
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步 
			url : "qyqclczs/queryQyqc.do",
			data : $("#form").serialize(),
			dataType : "json",
			success : function(obj) {
				getTable(obj.data);//拼接表格
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
 
  
    function getTable(data) {
      layui.use('table', function(){
        var table = layui.table;
        
        //第一个实例
        table.render({
          elem: '#qrtable'
          ,data: data //数据
          ,page: true //开启分页
          ,width:document.body.clientWidth
          ,cols: [[ //表头
            {field: 'ID', title: 'ID', width:60, fixed: 'left',hide:true}
            ,{field: 'NSRSBH', title: '纳税人识别号', width:140}
            ,{field: 'NSRMC', title: '纳税人名称', width:300}
            ,{field: 'JDMC', title: '所属街道', width:150} 
            ,{field: 'SYSSQK', title: '上月税收情况', width:130,align:'right'} 
           	,{field: 'SYSSPM', title: '上月税收排名', width:130} 
            ,{field: 'SYSSGXBL', title: '上月税收贡献比例', width:150,align:'right',
            	templet: function (d) {
            		if(d.SYSSGXBL!='undefined'&&d.SYSSGXBL!=''&&d.SYSSGXBL!=undefined){
            			return d.SYSSGXBL+'%'
            		}else{
            			return''
            		}
            		
            		}} 
            ,{field: 'DNLJSK', title: '当年累计税款', width: 150,align:'right'}
            ,{field: 'QNLJSK', title: '去年累计税款', width: 150,align:'right'}
            ,{field: 'FBRQ', title: '启动日期', width: 180, templet: function(data) {
            	 var fbrq='';
           	  if(data.FBRQ!=undefined&data.FBRQ!="undefined"&data.FBRQ!=null){
           		  fbrq=data.FBRQ.substring(0, 10);
           	  }
            	return fbrq;
            }}
            ,{
				field : 'LY',
				title : '街道反馈意见',
				fixed : 'right',
				width:130
			},{
				field : 'SHYJ',
				title : '终审意见',
				fixed : 'right',
				width:130
			},{field: 'STATUS', title: '状态',fixed : 'right',  width: 150, templet: function(data) {
            	if (data.STATUS == '1'){
					return "待终审";
				} else if (data.STATUS == '4'){
					return "工商已终审";
				} else if (data.STATUS == '5'){
					return "工商已终止";
				}
            }}
             ,{field : '', title : '操作', toolbar : '#bar', fixed : 'right', width:250} 
          ]]
        });
        
        
        
        layui.use([ 'form', 'laydate', 'table' ],function() {
			var form = layui.form, layer = layui.layer, laydate = layui.laydate, table = layui.table;
			table.on('tool(qrtable)', function(obj) {
	        	
		       	  var data = obj.data; //获得当前行数据
		       	  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		       	  var tr = obj.tr;     
		          
		          if(layEvent === 'remove') {
		        	  editlayer = layer.open({
							type : 1,
							area : [ '40%', '40%' ],
							shadeClose : true, //点击遮罩关闭
							content : $('#editmain'),
							success : function(obj) {
								form.on('submit(updateSave)', function() {
									$.ajax({
										type : "post", //请求方式
										async : false, //是否异步
										url : "qyqclczs/removeQyqc.do",
										data : {
											"id" : data.ID,
											"ly" : $("#ly").val()
										},
										dataType : "json",
										success : function(obj) {
											if (obj.code == '000') {
												layer.msg('提交成功！');
											} else {
												layer.msg('提交失败！');
											}
											layer.close(editlayer);
											$("#search_btn").click();
										},
										cancel : function() {
											return;
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
									return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
								});
							},
							cancel : function() {
								return;
							},
							end : function() {

							}
						});          
		          } else if(layEvent === 'ZS') {
		        	  editlayer = layer.open({
							type : 1,
							area : [ '40%', '40%' ],
							shadeClose : true, //点击遮罩关闭
							content : $('#editmain'),
							success : function(obj) {
								form.on('submit(updateSave)', function() {
									$.ajax({
										type : "post", //请求方式
										async : false, //是否异步
										url : "qyqclczs/saveQyqczs.do",
										data : {
											"id" : data.ID,
											"ly" : $("#ly").val()
										},
										dataType : "json",
										success : function(obj) {
											if (obj.code == '000') {
												layer.msg('提交成功！');
											} else {
												layer.msg('提交失败！');
											}
											layer.close(editlayer);
											$("#search_btn").click();
										},
										cancel : function() {
											return;
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
									return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
								});
							},
							cancel : function() {
								return;
							},
							end : function() {

							}
						}); 
		          } else if (layEvent === 'CK') { //查看
		        	  $.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							url:"qyqclczs/doQueryByID.do",
							data: data,
							dataType : "json",
							success : function(obj) {
								var a = obj.data;
								if (obj.code == "000") {
									
									var html1='<ul class="layui-timeline"><br><br><br>';
									var html2 ='';
									for (var i = 0; i < a.length; i++) {
										var cv ="";
										var b = a[i].STATUS;
									      var c = "";
									      if(b==4){
									    	  c="同意"
									      }else{
									    	  c="不同意"
									      }
										if(i==0){
											cv="已推送至:";
											html2 +='<li class="layui-timeline-item">'+
										    '<i class="layui-icon layui-timeline-axis">&#xe63f;</i>'+
										    '<div class="layui-timeline-content layui-text">'+
										      '<h3 class="layui-timeline-title">'+a[i].FBRQ+'</h3>'+
										      '<p>'+a[i].NSRMC+'&nbsp &nbsp'+cv+a[i].NQRD+' </p>'+

										    '</div>'+
										  '</li>';
										}else if(i==1){
											cv="街道已反馈";
											html2 +='<li class="layui-timeline-item">'+
										    '<i class="layui-icon layui-timeline-axis">&#xe63f;</i>'+
										    '<div class="layui-timeline-content layui-text">'+
										      '<h3 class="layui-timeline-title">'+a[i].FBRQ+'</h3>'+
										      '<p>'+a[i].NQRD+'&nbsp &nbsp'+cv+' </p>'+

										    '</div>'+
										  '</li>';
										}else if(i==2){
											cv="工商已反馈：";
											html2 +='<li class="layui-timeline-item">'+
										    '<i class="layui-icon layui-timeline-axis">&#xe63f;</i>'+
										    '<div class="layui-timeline-content layui-text">'+
										      '<h3 class="layui-timeline-title">'+a[i].FBRQ+'</h3>'+
										      '<p>'+cv+'&nbsp &nbsp'+c+' </p>'+
										    '</div>'+
										  '</li>';
										}
										
									}
									html1 = html1+html2+'</ul>';
									layer.open({
									      type: 1,
									      title: "详细信息", //页面标题
										  shadeClose: true, //点击遮罩区域是否关闭页面
									      area: ['65%', '65%'], //宽高
									      content: html1
									    });
									/* if(a.length == 1){
										//页面层
									    layer.open({
									      type: 1,
									      title: "详细信息", //页面标题
										  shadeClose: true, //点击遮罩区域是否关闭页面
									      area: ['50%', '50%'], //宽高
									      content: 
									'<table style="margin: 7% auto;font-size: 24px;">'+
										'<tr>'+
											'<td>时间：</td>'+
											'<td>'+a[0].FBRQ+'</td>'+
											'<td>已推送至街道：</td>'+
											'<td>'+a[0].FBRQ+'</td>'+
										'</tr>'+
									'</table>'
									    });
									} else if(a.length == 2){
										//页面层
									    layer.open({
									      type: 1,
									      title: "详细信息", //页面标题
										  shadeClose: true, //点击遮罩区域是否关闭页面
									      area: ['50%', '55%'], //宽高
									      content: 
									
									'<ul class="layui-timeline">'+
									 '<br>'+
									 '<br>'+
									 '<br>'+
										  '<li class="layui-timeline-item">'+
										    '<i class="layui-icon layui-timeline-axis">&#xe63f;</i>'+
										    '<div class="layui-timeline-content layui-text">'+
										   
										      '<h3 class="layui-timeline-title">'+a[0].FBRQ+'</h3>'+
										      '<p>'+a[0].NSRMC+' </p>'+
										    '</div>'+
										  '</li>'+
										  '<li class="layui-timeline-item">'+
										    '<i class="layui-icon layui-timeline-axis">&#xe63f;</i>'+
										    '<div class="layui-timeline-content layui-text">'+
										      '<h3 class="layui-timeline-title">8月16日</h3>'+
										     ' <p>杜甫的思想核心是儒家的仁政思想，他有“<em>致君尧舜上，再使风俗淳</em>”的宏伟抱负。个人最爱的名篇有：</p>'+
										    '</div>'+
										  '</li>'+
										  '<li class="layui-timeline-item">'+
										    '<i class="layui-icon layui-timeline-axis">&#xe63f;</i>'+
										    '<div class="layui-timeline-content layui-text">'+
										      '<h3 class="layui-timeline-title">8月15日</h3>'+
										      '<p> 中国人民抗日战争胜利72周年</p>'+
										   '</div>'+
										  '</li>'+
										 ' <li class="layui-timeline-item">'+
										   ' <i class="layui-icon layui-timeline-axis">&#xe63f;</i>'+
										    '<div class="layui-timeline-content layui-text">'+
										     ' <div class="layui-timeline-title">过去</div>'+
										    '</div>'+
										  '</li>'+
										'</ul>'
									    });
									} else if(a.length == 3){
										//页面层
									    layer.open({
									      type: 1,
									      title: "详细信息", //页面标题
										  shadeClose: true, //点击遮罩区域是否关闭页面
									      area: ['30%', '30%'], //宽高
									      content: 
									'<table style="margin: 7% auto;font-size: 24px;">'+
										'<tr>'+
											'<td style="text-align:center">迁出成功</td>'+
										'</tr>'+
										'<tr>'+
											'<td>已推送至街道：</td>'+
											'<td>'+a[0].FBRQ+'</td>'+
										'</tr>'+
										'<tr>'+
											'<td>街道已反馈：</td>'+
											'<td>'+a[1].FBRQ+'</td>'+
										'</tr>'+
										'<tr>'+
											'<td>工商已反馈：</td>'+
											'<td>'+a[2].FBRQ+'</td>'+
										'</tr>'+
									'</table>'
									    });
									} */
								} 	
							}
						});
					}
		        });  
        });
     
            
      });
    }
    
  </script>
	

</html>
