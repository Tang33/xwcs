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

<title>文件归档</title>

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
</style>
</head>
<body  style="overflow-x: hidden">
	<input type="hidden" id="dwid" value="${dwid}" />
	<blockquote class="layui-elem-quote layui-text">本功能页面用于外部文件的上传归档及查阅！</blockquote>
	<form class="layui-form" id="form" action="">
		<input id="cslx" name="cslx" type="hidden" value="0" /> <input
			id="ids" name="ids" type="hidden" value="0" />
		<div class="layui-collapse" lay-filter="test" style="border-bottom: 0px;">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row" style="margin-left: 17px;">
						
							<div class="layui-inline ">
								<button type="button" class="layui-btn" id="select">选择文件</button>
								&nbsp;
								<button type="button" class="layui-btn" id="doinput">上传</button>
							</div>
						
						<div class="layui-inline">
							<label class="layui-form-label" style="width: 150px; padding-right:0px;">归档文件查询：</label>
						</div>
						<div class="layui-inline " >
							<select lay-filter="drfs" id="drfs"
								style="height: 38px; width: 100%;" lay-search="">
							</select>
						</div>
						<div class="layui-inline " >
							<label class="layui-form-label">上传日期：</label>
							<div class="layui-input-inline">
								<input type="text" class="layui-input" id="drrq" name="drrq"
									placeholder="请选择日期">
							</div>
						</div>
						<div class="layui-inline ">
						</div>
						<div class="layui-inline ">
						</div>
						<div class="layui-inline ">
							<button type="button" class="layui-btn " id="chaxun">查 询
							</button>

						</div>
					</div>
					<div class="layui-row1" id="moset" style="margin-top: 5px;width: 50%;margin-left: 17px;">
						<!-- <span>123</span> -->				
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
<script type="text/html" id="barDemo">
  <a class="layui-btn layui-btn-xs" lay-event="ck">查看</a>
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	var wait;
	var temp = "";
	var qxlayer;
	var files;
	var dwid = $("#dwid").val();
	
	layui.use(['form', 'layedit', 'laydate','laypage'], function() {
		var form = layui.form,
			layer = layui.layer,
			layedit = layui.layedit,
			laydate = layui.laydate,
			laypage=layui.laypage;
		//调用方法
		queryList();
		
		laydate.render({
			elem: '#drrq',
			format: 'yyyyMM',//可任意组合
			type: 'month'
		});
	});
	
	
	
	//查询
	$("#chaxun").click(function() {
		getData();
		return false;
	});
	
	function getData() {
		var wjid = $('#drfs').val();
		//console.log(wjid);
		layui.use('table', function(){
	        var table = layui.table;
	        //第一个实例
	        table.render({
	          elem: '#syzs'
	          ,height: 600
	          ,width:document.body.clientWidth
	          ,url:"wjdg/crwjzs.do?wjid="+wjid+"&scrq="+$("#drrq").val()
	          ,limit:15
	          ,limits : [ 15, 30, 45, 60 ]
	          ,page: true //开启分页
	          ,cols: [[ //表头
	                    {field: 'WJMC', title: '文件名称'}
	      		      ,{field: 'SCRQ', title: '上传日期'}     
	      		      ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:150}
	          ]]
	        ,id:'syzs'
	        });
	       
	      }); 
	} 
	
	//查询下拉框
	 function queryList(){
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url:"wjdg/selectList.do",
			data: "",
			dataType : "json",
			success : function(obj) {
				var data = obj.data;
				var options="<option value='请选择'>请选择</option>";
			
				 for(var i in data){
					var wjmc = data[i].WJMC;
					var wjid = data[i].WJID;
					options = options+"<option value="+wjid+">"+wjmc+"</option>";
				} 
				 $("#drfs").append(options);
				layui.form.render();
			}
		})
			
	}
	
	layui.use('upload',function() {
		var $ = layui.jquery, upload = layui.upload;
		var listwjm=new Array();
		//指定允许上传的文件类型
		uploadListIns=upload.render({
									elem : '#select',
									url : 'uploadgd.do?dwid='+dwid,
									before : function(obj) {
										wait = layer.load();
									},
									accept : 'file', //普通文件
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
					                        })
										 })
									},
									done : function(res) {
										layer.close(wait);
										console.log(res.mesg);
										if(res.mesg=='上传成功'){
											
											layer.alert("上传成功",{icon: 1},function () {
											    layer.close(layer.index);
											    window.location.reload(); 
											    table.render();

								            }); 
										}
									}
								});
					});
	

	      //do something
		  //查询展示的内容
		  layui.use('table', function(){
		        var table = layui.table;
		        
		        //第一个实例
		        
		        table.render(getData());
		        
		        
		        
		        table.on('tool(syzs)', function(obj){
		            var data = obj.data;
		            //console.log(obj)
		            if(obj.event === 'ck'){
		            	console.log(data.WJQC)
		            	//将文件下载到本地
		            	window.location.href = "downgd.do?filePath="+data.WJQC;
		            }
		            if (obj.event === 'del') {
		   				layer.confirm('', {btn: ['是','否']
		   				,area:['30%','27%']
		   				,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">是否确认删除？</div>'
		               }, function(index){ 
		            	  $.ajax({
		    					type : "post",
		    					url : 'wjdg/delwjdg.do',
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
							})
		               })
		     	 	}
		        })
		    })
	</script>
</html>