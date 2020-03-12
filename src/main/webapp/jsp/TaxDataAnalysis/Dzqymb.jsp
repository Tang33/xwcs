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

<title>定制企业模板</title>

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
<!-- <link rel="stylesheet" href="./css/layui/css/layui.css" media="all"> -->
<script src="./css/layui/layui.js" charset="utf-8"></script>
<style type="text/css">
.layui-form .layui-border-box .layui-table-view {
	max-width: 1700px;
}

i {
	display: inline-block;
	width: 18px;
	height: 18px;
	line-height: 18px;
	text-align: center
}

.label {
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

.layui-colla-content {
	padding: 20px 15px;
}

.layui-col-md4 {
	width: 20%;
}

#mxtable  th,td{
		white-space: nowrap;
}

#mx {
	height: 300px; 
	width:document.body.clientWidth-20; 
	overflow:scroll;
}


</style>
</head>
<body style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">本功能页面用于定制企业模板！</blockquote>
	<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test" style="border-bottom: 0px; height: 99px;">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					
					
					
					<div class="layui-row">
						<div class="layui-col-md3" style="width: 6.2%; margin-left: 17px;">
							<button type="button" class="layui-btn" id="select">选择文件</button>
						</div>
						<div class="layui-col-md6" style="text-align: left; width: 25%;">
							<div class="layui-inline">
								<button type="button" class="layui-btn" id="doinput">
									生成模板</button>
							</div>
						</div>
						<div class="layui-col-md6"
							style="text-align: center; width: 14%; float: right;">
							<div class="layui-inline">
								<button type="button" class="layui-btn" id="downmb">
									上传文件模板格式下载</button>
							</div>
						</div>

					</div>
					<div class="layui-row1" id="moset" style="margin-top: 5px;height: 30px;width: 50%;margin-left: 17px;"></div>		
					
				</div>
			</div>
		</div>

		<div id="choose" value=""
			style="display: none; width: 70%; margin: 0px auto; margin-top: 30px;">
			<div style="padding-top: 30px;">
				<div style="width: 15%; float: left; line-height: 38px">模板名称:</div>
				<div style="width: 72%; float: left;">
					<input type="text" name="mbmc" id="mbmc" placeholder="请输入模板名称"
						autocomplete="off" class="layui-input">
				</div>
			</div>
			<div style="clear: both; padding-top: 30px;">
				<div style="width: 15%; float: left; line-height: 38px">模版描述：</div>
				<div style="width: 85%; float: left;">
					<textarea name="textfield3" class="textclob" cols="50" rows="5"></textarea>
				</div>
			</div>
			<div style="padding-top: 30px; margin-left: 76%; clear: both;">
				<button type="button" class="layui-btn" id="savezd">保存</button>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>

			<table class="layui-table" id="mbdy" lay-filter="mbdy">

			</table>
			
			<div id="mx" style=" height: 300px; width:1668px; "class="layui-hide" >
			</div>
			
		</fieldset>
	</form>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>


	var indexzf;
	$("#doinput").click(function() {
		//输入模版描述
		var a=$("#moset").html()
		/* console.log(a.length); */
		if(a.length<=0){
			layer.open({
				  title: ' '
				  ,area : [ '20%', '22%' ]
				  ,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 15%;">请选择文件！！</div>'
				}); 
			
			return false;
		}
		
		indexzf = layer.open({
			type : 1,
			title : '生成模版',
			area : [ '53%', '53%' ],
			content : $('#choose'),
			cancel : function() {
				return;
			}
		});

	})

	var wait;
	var temp = "";
	var qxlayer;
	var files;

	layui.use('upload', function() {
		var $ = layui.jquery, upload = layui.upload;
		var listwjm = new Array();
		//指定允许上传的文件类型

		uploadListIns = upload.render({
			elem : '#select',
			url : 'upload.do?lx=rksj&name=',
			before : function(obj) {
				wait = layer.load();
			},
			accept : 'file' //普通文件
			,
			auto : false,
			exts : 'xls|xlsx',
			bindAction : '#savezd',
			choose: function(obj){
				if(files != undefined){
					layer.alert("每次只能上传一个模板文件！", {
						icon : 2
					}, function() {
						layer.close(layer.index);
					});
				}else{
					files = obj.pushFile();
					 obj.preview(function(index, file, result){
						 /* console.log(file.name); */
						 listwjm.push(file.name);
						 $("#moset").append('<a href="javascript:;" class="label"  id="'+index+'"><span>'+file.name+'</span><input type="hidden" name="'+file.name+'"/><i class="close">x</i></a>')
						//删除
	                    $(document).on("click", ".close", function () {
							$(this).parent().remove();
							var id=$(this).parent().attr('id');
	                        delete files[id]; //删除对应的文件
	                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
	                        files=undefined;
	                    });
					 });
				}
			},
			done : function(res) {
				
				console.log($("#mbmc").val());
				var data=eval("("+res.data+")");
				var ofn = data[0].originalFilename;
				var filenames;
				filenames = data[0].bdsrc;
				var src = data[0].src;
				/* console.log(filenames); */
				var mbms = $(".textclob").val();
				if (res.code == "0") {
					var mbmc = $("#mbmc").val();
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						url : "Mbdy/impExcel.do",
						data : {
							path : filenames,
							mbmc : mbmc,
							mbms : mbms,
							wjlj : src
						},
						dataType : "json",
						success : function(obj) {
							layer.close(wait);
							if (obj.code == "001") {
								layer.alert("模板名称已经存在,请修改模板名称!!", {
									icon : 2
								}, function() {
									layer.close(layer.index);
								});
							} else if (obj.code == "002") {
								layer.alert("文件格式不正确!!", {
									icon : 2
								}, function() {
									layer.close(layer.index);
								});
							} else if (obj.code == "000") {
								layer.alert("保存成功", {
									icon : 1
								}, function() {
									layer.close(layer.index);
									layer.close(indexzf);
									window.location.reload();
								});
							}

						},
						error : function(XMLHttpRequest, textStatus,
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

	layui.use('table', function() {
		var table = layui.table;

		table.render({
			elem : '#mbdy',
			limit:10,
		    limits : [ 10, 15, 20,25,30 ],
			url : "Mbdy/querymbmx.do",
			cols : [ [ {
				field : 'MBMC',
				title : '模版名称',
				event : 'click'
			}, {
				field : 'DRSJ',
				title : '创建时间',
				event : 'click'
			}, {
				field : 'CZY',
				title : '创建人',
				event : 'click'
			}, {
				field : 'MBMS',
				title : '模版描述',
				templet:'<div><span title="{{# if(d.MBMS!=undefined){ }}{{d.MBMS}}{{#  }}}">{{# if(d.MBMS!=undefined){ }}{{d.MBMS}}{{#  }}}</span></div>'			
			}

			] ],
			page : true
			,done : function(){
		    	$(".layui-table-view")[0].style.width=document.body.clientWidth;
		    	
        	}
	  });
	  
	  table.on('tool(mbdy)', function(obj){
		  var data = obj.data;
		  if(obj.event === 'click'){
		  $('#mx').removeClass('layui-hide');
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url:"ajax.do?ctrl=Mbdy_readRecordsInputPath",
			data : {
				path : obj.data.PATH,
			},
			
			dataType : "json",
			success : function(ttt) {
				var aa = "";
				for (i=0;i<ttt.length;i++ ){ 
					if(i==0){
						var strs = ttt[i];
						aa += '<thead><tr>'
						for (j=0;j<strs.length;j++ ){ 
							aa += '<th>'+strs[j]+'</th>';
					    } 
						aa += '</tr></thead>'
						continue; 
					} else{
						var strs = ttt[i];
						aa += '<tbody><tr>'
						for (j=0;j<strs.length;j++ ){ 
							aa += '<td>'+strs[j]+'</td>';
					    } 
						aa += '</tr></tbody>'
						continue; 
					}
			    } 
				$('#mx').html("");
				$("#mx").append('<table id="mxtable" cellspacing="0" cellpadding="0" border="0" style="width:100%" class="layui-table">'+aa+'</table>');
				
				/* layer.open({
		            shadeClose: true,
		            offset: '20%',
		            area: ['70%', '50%'],
		            content: '<table cellspacing="0" cellpadding="0" border="0" class="layui-table">'+aa+'</table>'
		        }); */
			}
		});
	  }
	  });
	 
	});

	$("#downmb")
			.click(
					function() {

						window
								.open("/xwcs/downgd.do?filePath=数据分析自定义模板（必须以纳税人名称和纳税人识别号开头）.xls");
					});
</script>


</html>