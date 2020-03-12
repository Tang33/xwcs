<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<base href="<%=basePath%>">

<title>定制企业查询</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->

<!-- <link rel="stylesheet" href="./static/layui/css/layui.css" media="all">
<script src="./static/layui/layui.js" charset="utf-8"></script>
<script src="./static/assets/js/jquery-1.8.3.min.js" charset="utf-8"></script>
<script src="./static/js/easy.ajax.js" charset="utf-8"></script> -->
<style type="text/css">

.layui-form .layui-border-box .layui-table-view{
	max-width:1700px;
}
i{
	display: inline-block;
    width: 18px;
    height: 18px;
    line-height: 18px;
    text-align: center
}
.label{
		    padding: 2px 5px;
		    background: #5FB878;
		    border-radius: 2px;
		    color: #fff;
		    display: block;
		    line-height: 20px;
		    height: 20px;
		    margin: 2px 5px 2px 0;
		    float: left;
}
.layui-colla-content{


	padding: 20px 15px;
}

.layui-col-md4{

	width: 20%;
}

</style>
</head>
<body  style="overflow-x: hidden">
	<form class="layui-form" id="form" action="">
		<input id="cslx" name="cslx" type="hidden" value="0" /> <input
			id="ids" name="ids" type="hidden" value="0" />
		<div class="layui-collapse" lay-filter="test" style="border-bottom: 0px;height: 99px;">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md3">
							<label class="layui-form-label">模板名称:</label>
							<div class="layui-inline">
							
								<input type="text" name="mbmc" id="mbmc" placeholder="请输入模板名称"
										autocomplete="off" class="layui-input">
								
							</div>
							<button type="button" class="layui-btn" id="select">选择文件</button>
						</div>
						<div class="layui-col-md6" style="text-align:left;width: 25%;">
							<div class="layui-inline">
								<button type="button" class="layui-btn" id="doinput">
									保存
								</button>
							</div>
						</div>

					</div>
					<div class="layui-row1" id="moset" style="margin-top: 5px;height: 30px;width: 50%;margin-left: 121px;">
						<!-- <span>123</span> -->				
					</div>
				</div>
			</div>
		</div>
		
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">

						<div class="layui-col-md3">
							<label class="layui-form-label">模板:</label>

							<div class="layui-inline">
								<select name="mbmccx" lay-filter="mbmc" id="mbmccx">
								</select>
							</div>
						</div>
						<div class="layui-col-md4">
							<div class="layui-inline">
								<label class="layui-form-label">截止月份:</label>
								<div class="layui-input-inline">
									<input type="text" class="layui-input" id="test8"
										placeholder="">
								</div>
							</div>
						</div>
						<div class="layui-col-md6" style="text-align:left;">
							<div class="layui-inline">
								<button class="layui-btn layui-btn-normal" 
									data-type="reload" id="selectbyCondition">查询</button>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>

			<table class="layui-table" id="syzs" lay-filter="syzs">
				
			</table>
		</fieldset>
	</form>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	var wait;
	var temp = "";
	var qxlayer;
	var files;
	
	
	layui.use('upload',function() {
						var $ = layui.jquery, upload = layui.upload;
						var listwjm=new Array();
						//指定允许上传的文件类型
						
						console.log($("#mbmc").val());
						uploadListIns=upload.render({
									elem : '#select',
									url : 'upload.do?lx=rksj&name='
										+ $("#rkrq").val(),
									before : function(obj) {
										wait = layer.load();
									},
									accept : 'file' //普通文件
									,
									auto : false,
									exts : 'xls|xlsx',
									bindAction : '#doinput',
									choose: function(obj){
										
										files = obj.pushFile();
										 obj.preview(function(index, file, result){
											 console.log(file.name);
											 listwjm.push(file.name);
											 $("#moset").append('<a href="javascript:;" class="label"  id="'+index+'"><span>'+file.name+'</span><input type="hidden" name="'+file.name+'"/><i class="close">x</i></a>')
										 
										 	
											//删除
					                        $(document).on("click", ".close", function () {
												$(this).parent().remove();
												var id=$(this).parent().attr('id');
					                            delete files[id]; //删除对应的文件
					                            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
					                        });
										 
										 });
										 
										 
										 
										
									},
									done : function(res) {
										console.log(res);
										
										
										console.log($("#mbmc").val());
										var filenames;
										$.each(res.data, function(i) {
											
											if(i==0){
												
												filenames=res.data[i].bdsrc;
											}else{
												
												filenames +="$_1$"+res.data[i].bdsrc;
											}
										});
										
										console.log(filenames);
										if (res.code == "0") {
											var mbmc = $("#mbmc").val();
											$
													.ajax({
														type : "post", //请求方式
														async : true, //是否异步
														url : "ajax.do?ctrl=Zdydr_impExcel",
														data : {
															filenames : filenames,
															mbmc : mbmc
															
														},
														dataType : "json",
														success : function(obj) {
															console.log(obj)
															if (obj.code == "001") {
																layer.alert("模板名称已经存在,请修改模板名称!!",{icon: 1},function () {
																    layer.close(layer.index);
													            });
															}else if (obj.code == "002") {
																layer.alert("文件格式不正确!!",{icon: 1},function () {
																    layer.close(layer.index);
													            });
															}else if (obj.code == "000") {
																layer.alert("保存成功",{icon: 1},function () {
																    layer.close(layer.index);
																    window.location.reload(); 
													            }); 
															}

														},
														error : function(
																XMLHttpRequest,
																textStatus,
																errorThrown) {
															// 状态码
															alert(XMLHttpRequest.status);
															// 状态
															alert(XMLHttpRequest.readyState);
															// 错误信息  
															alert(textStatus);
														}
													});
										} else {
											layer.msg("上传失败！");
											layer.close(wait);
										}
									}
								});
					});
	
	
	layui.use([ 'form', 'laydate', 'laypage' ],function() {
		var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate, laypage = layui.laypage;
		//年月范围
		laydate.render({
			elem: '#test8',
			type: 'month',
			format:'yyyy-MM'
		});
		var date=new Date;
		 var year=date.getFullYear(); 
		 var month=date.getMonth()+1;
		 month =(month<10 ? "0"+month:month); 
		 var mydate = (year.toString()+"-"+month.toString());
		 $("#test8").val(mydate);
	});
	
	
	
	//获取模板的相关信息
	getmb();
	var row='';
	function getmb(){
		layui.use('form', function(){
			var form = layui.form;
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "ajax.do?ctrl=Zdydr_querymbmc",
				data : {},
				dataType : "json",
				success : function(obj) {
					
					$.each(obj.data, function(i) {
						
						if(i==0){
							
							row='<option value="">请选择</option><option value="'+obj.data[i].U_ID+'">'+obj.data[i].MBMC+'</option>';
						}else{
							
							row+='<option value="'+obj.data[i].U_ID+'">'+obj.data[i].MBMC+'</option>';
						}
								
					});
					$("#mbmccx").append(row);
					form.render('select');
					
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
			
		})
		
	}
	
	
	var cols=[];
	function getTitle(){
		
		$.ajax({
			type : "post", //请求方式
			async : false, //是否异步
			url : "ajax.do?ctrl=Zdydr_getTitle",
			data : {
				uid: document.getElementById("mbmccx").value
			},
			dataType : "json",
			success : function(obj) {
				
				cols=obj.data;
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
	
	layui.use(['table'], function(){
		
		var table = layui.table
		//查询
		 $('#selectbyCondition').on('click',
			 function(){
			     getTitle();
			     //执行一个 table 实例
				  table.render({
				    elem: '#syzs'
				    ,height: 435
				    ,url:"ajax.do?ctrl=Zdydr_getMbData"
			    	,where: {
			        	uid: document.getElementById("mbmccx").value,
			        	date: $("#test8").val()
			        	
			        }
				    ,limit:10
				    ,cols: [cols]
				    ,page: true //开启分页
				    ,id:'syzs'
				    
				  });
	     });
	});
</script>


</html>