<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<base href="<%=basePath%>">

<title>系统管理-后台页面</title>

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
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
}
</style>
</head>

<body>
	<blockquote class="layui-elem-quote layui-text">
		本功能用于对查询数据库的“sql”进行操作管理！</blockquote>
	<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">
					
					<div class="layui-row">
						<div class="layui-col-md3" style="width:37%">
							<div class="layui-inline">
						      <label class="layui-form-label">类型：</label>
						      <div class="layui-input-inline">
						        <select name="type" id="type" lay-verify="required" lay-search="" lay-filter="type"></select>
						      </div> 
						    </div>
							<div class="layui-inline">
								<label class="layui-form-label">key：</label>
								<div class="layui-input-inline">
									<input type="text" name="key" id="key"  class="layui-input">
								</div>
							</div>
						</div>
						<div class="layui-col-md3">
							<div style="text-align: left;">
								<!-- <button class="layui-btn layui-btn-normal" id="button" type="button">查 询</button> -->
								<button class="layui-btn layui-btn-normal" id="button" type="button" lay-submit="" lay-filter="button">查 询</button>
								<button class="layui-btn layui-btn-normal" id="add" type="button" lay-submit="" lay-filter="add">新增sql</button>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend>查询显示</legend>

			<table class="layui-table" id="table" lay-filter="user">
				<thead>
					<tr>
						<th lay-data="{field:'uuid',width:'5%'}">序列</th>
						<th lay-data="{field:'key',width:'13%'}">KEY</th>
						<th lay-data="{field:'value',width:'50%',edit:'text'}">SQL语句</th>
						<th lay-data="{field:'lrrq',width:'12%'}">录入日期</th>
						<th lay-data="{field:'right',width:'20%',fixed:'right',toolbar:'#bar'}">操作</th>
					</tr>
				</thead>
				<tbody id="ttbody">
					
				</tbody>
			</table>
			<div id="page1"></div>
			<script type="text/html" id="bar">
				<a class="layui-btn layui-btn-xs" lay-event="test">调试</a>
  				<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  				<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
			</script>
		</fieldset>

	
	<div id="editmain" style="display: none;">
		<!-- <form class="layui-form" id="form2" action="" style="margin-top: 20px;"> -->
			<div class="layui-form-item" style="margin-top:20px;">
			    <label class="layui-form-label">key</label>
			    <div class="layui-input-inline" style="width: 800px;">
			      <input type="text" id="keys" name="keys" required  autocomplete="off" class="layui-input" readonly="readonly" >
			    </div>
			</div>
			<div class="layui-form-item">
			    <label class="layui-form-label">value</label>
			    <div class="layui-input-inline" style="width: 800px;">
			      <textarea id="value" name="value" placeholder="请输入sql" class="layui-textarea" style="height:180px;"></textarea>
			    </div>
			</div>
		<!-- </form> -->
	</div>
	<div id="addmain" style="display: none;">
		<!-- <form class="layui-form" id="form3" action="" style="margin-top: 20px;"> -->
			<div class="layui-form-item" style="margin-top:20px;">
				<label class="layui-form-label">类型：</label>
				<div class="layui-input-inline">
					<select name="type3" id="type3"  lay-search=""  lay-filter="type3"></select>
				</div> 
			</div>
			<div class="layui-form-item">
			    <label class="layui-form-label">key</label>
			    <div class="layui-input-inline" style="width: 600px;">
			      <input type="text" id="keys3" name="keys3" required   autocomplete="off" class="layui-input">
			    </div>
			</div>
			<div class="layui-form-item">
			    <label class="layui-form-label">value</label>
			    <div class="layui-input-inline" style="width: 600px;">
			      <textarea id="value3" name="value3" placeholder="请输入sql"  class="layui-textarea" style="height:180px;"></textarea>
			    </div>
			</div>
		<!-- </form> -->
	</div>
	<div id="testmain" style="display: none;">
			<div class="layui-form-item" id="testpar" style="margin-top:10px;">
			    
			</div>
			<div class="layui-form-item">
			    <label class="layui-form-label">调试sql</label>
			    <div class="layui-input-inline" style="width: 76%;">
			      <textarea id="value1" name="value1" placeholder="请输入sql" class="layui-textarea"></textarea>
			    </div>
			   <div class="layui-input-inline">
			    	<div style="text-align: right;">
			      		<button class="layui-btn layui-btn-normal" id="dosql" type="button" lay-submit="" lay-filter="dosql">执行sql</button>
			      	</div>
			    </div>
			</div>
			<div class="layui-form-item" style="width:96%;margin: 0px auto;" >
				<table class="layui-table" id="table2" lay-filter="user2">
					<thead id="tthead2">
						
					</thead>
					<tbody id="ttbody2">
						
					</tbody>
				</table>
				<div id="page2"></div>
			</div>
			
			<div class="layui-input-inline"  style="display: none;">
				<input type="text" id="parSize" name="parSize" required   autocomplete="off" class="layui-input">
			    <textarea id="value2" name="value2" placeholder="请输入sql" class="layui-textarea" style="height:180px;"></textarea>
			</div>
	</div>
	</form>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	
	$(function() {
		$("#table").attr("lay-data","{width:" + document.body.clientWidth + "}");
		init();
	});
	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 10;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize2 = 10;//每页显示数据条数
	var pageNo2 = 1;//当前页数
	var count2 = 0;//数据总条数
	function init(){
		ajax({ 
			type:"post",    //请求方式
 			async:true,    //是否异步
            url: "ajax.do?ctrl=Sql_csh",
            data: $("#form").serialize(),
            dataType: "json",
            success: function(obj){
                if(obj!=null&&obj.code=='000'){
                	var str="<option value='%'>全部</option>";
                	for (var i = 0; i < obj.data.length; i++) {
						str+="<option value='"+obj.data[i]+"'>"+obj.data[i]+"</option>";
					}
					$("#type").html(str);
					$("#type3").html(str);
					layui.use(['form'], function() {
						var form = layui.form;
						form.render('select');
					});
                    
                }
                getData();
            }
        });
	}
	
    //ajax请求后台数据
    function getData(){
    	var parkey=$("#key").val();
    	var partype=$("#type").val();
        ajax({
			type:"post",    //请求方式
 			async:true,    //是否异步
            url: "ajax.do?ctrl=Sql_query&pageNo="+pageNo+"&pageSize="+pageSize+"&type="+partype+"&key="+parkey,
            data: {},
            dataType: "json",
            success: function(obj){
            	console.log(obj);
            	//log(obj);
                if(obj!=null&&obj.code=='000'){
                    getTbale(obj.data);
                    count  = obj.count;//数据总条数
	   				queryPage();
                }
            }
        });
    }
	//初始化分页
	function queryPage(){
    	layui.use(['laypage'], function() {
    		laypage = layui.laypage;
    		laypage.render({
				elem: 'page1' //注意，这里的page1 是 ID，不用加 # 号
				,count: count //数据总数，从服务端得到
				,limit:pageSize//每页显示条数
				,limits:[10,20,30,50]//条数列表
				,layout:['prev', 'page', 'next','skip','count','limit']
				,curr:pageNo
				,jump: function(data, first){
					//obj包含了当前分页的所有参数，比如：
					pageNo = data.curr;
					pageSize = data.limit;
					//首次不执行
					if(!first){
						//do something
						getData();
					}
				}
			});
    	});
    	
    }
    //初始化分页
	function queryPage2(){
    	layui.use(['laypage'], function() {
    		laypage = layui.laypage;
    		laypage.render({
				elem: 'page2' //注意，这里的page2是 ID，不用加 # 号
				,count: count2 //数据总数，从服务端得到
				,limit:pageSize2//每页显示条数
				,limits:[10,20,30,50]//条数列表
				,layout:['prev', 'page', 'next','skip','count','limit']
				,curr:pageNo2
				,jump: function(data, first){
					//obj包含了当前分页的所有参数，比如：
					pageNo2 = data.curr;
					pageSize2 = data.limit;
					//首次不执行
					if(!first){
						//do something
						testQuery();
					}
				}
			});
    	});
    	
    }
    function testQuery(){
    	$.ajax({
				type:"post",    //请求方式
	 			async:false,    //是否异步
	            url: "ajax.do?ctrl=Sql_test&pageNo2="+pageNo2+"&pageSize2="+pageSize2,
	            data: $("#form").serialize(),
	            dataType: "json",
	            success: function(obj){
	                if(obj!=null&&obj.code=='000'){
	                	count2  = obj.count;//数据总条数
						if(obj.data!=null){
							if(obj.msg=='查询成功'){
								var head2="";
								var body2="";
								var hlist=obj.data.hlist;
								var blist=obj.data.blist;
								if(hlist!=null){
									head2+="<tr>";
									for (var i = 0; i < hlist.length; i++) {
										head2+="<th lay-data=\"{field:'"+hlist[i]+"'}\">"+hlist[i]+"</th>";
									}
									head2+="</tr>";
								}
								if(blist!=null){
									for (var i = 0; i < blist.length; i++) {
										body2+="<tr>";
										for (var j = 0; j < blist[i].length; j++) {
											body2+="<td>"+blist[i][j]+"</td>";
										}
										body2+="</tr>";
									}
								}
								$("#tthead2").html(head2);
								$("#ttbody2").html(body2);
								//执行渲染
								layui.use(['table'], function() {
									layui.table.init('user2', {
										height: 440 //设置高度
										,limit: pageSize2 //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
										//支持所有基础参数
									}); 
								});
								queryPage2();
							}else{
								alert(obj.msg);
							}
							
							
						}
						
	               		layer.msg('提示：执行操作成功！');
	                }else{
	                	layer.msg('提示：执行操作失败！');
	                }
	            },
	            error: function (XMLHttpRequest, textStatus, errorThrown) {
	                // 状态码
	                alert(XMLHttpRequest.status);
	                // 状态
	                alert(XMLHttpRequest.readyState);
	                // 错误信息  
	                alert(textStatus); 
	            }
	        });
    }
	//初始化表格
	function getTbale(data){
            var s = "";
            $.each(data,function(v,o){
                    s+='<tr><td>'+o.UUID+'</td>';
                    s+='<td>'+o.KEY+'</td>';
                    s+='<td>'+o.VALUE+'</td>';
                    s+='<td>'+o.LRRQ+'</td></tr>';
            });
			
            $("#ttbody").html(s);
			//执行渲染
			layui.use(['table'], function() {
				layui.table.init('user', {
					height: 470 //设置高度
					,limit: pageSize //注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
					//支持所有基础参数
				}); 
			});
    }
    //初始化Element
	layui.use([ 'element', 'layer' ], function() {
		var element = layui.element;
		var layer = layui.layer;
		//监听折叠
		element.on('collapse(test)', function(data) {
			layer.msg('展开状态：' + data.show);
		});
	});
	//初始化form
	layui.use([ 'form' ], function() {
		var form=layui.form;
		//监听
		form.on('select(type)', function(data){
		  pageNo = 1; //当点击搜索的时候，应该回到第一页
		  getData();
		});
		form.on('submit(button)',  function(data){
		  pageNo = 1; //当点击搜索的时候，应该回到第一页
		  getData();
		  return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		form.on('submit(dosql)',  function(data){
			$("#tthead2").html("");
			$("#ttbody2").html("");
			pageNo2=1;
		  	testQuery();
		  	return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		
		
		form.on('submit(add)',  function(data){
		  //do something
		  var type=$("#type").val();
		  $("#type3").next().find(".layui-input").val(type);;
		  $("#type3").val(type);
		  form.render('select');
		  $("#keys3").val("");
	      $("#value3").val("");
		  var index=layer.open({
	            type: 1,
	            title: 'sql新增',
	            area: ['800px', '420px'],
	            shadeClose: false, //点击遮罩关闭
	            content: $('#addmain'),
	            btn: ['确定', '取消'],
	            success:function(){
		            
	            },
	            yes: function (index, layero) {//添加人员
	                //做数据校验
	                var type=$("#type3").val();
	                var key=$("#keys3").val();
	                var value=$("#value3").val();
	                if(value==null||value==''||value=='undefied'){
	                	layer.msg('请输入sql！');
	                }else{
	                	$.ajax({
							type:"post",    //请求方式
			 			    async:true,    //是否异步
			                url: "ajax.do?ctrl=Sql_add",
			                data: {type:type,key:key,value:value},
			                dataType: "json",
			                success: function(data){
			                	if(data.code=='000'){   
			                		layer.msg('新增sql成功！');
			                	}else{
			                		layer.alert('新增sql失败！');  
			                	}
			                	var type=$("#type3").val();
							    $("#type").next().find(".layui-input").val(type);;
							    $("#type").val(type);
							    form.render('select');
			                	layer.close(index);
								$("#button").click();
			                },
			                error: function (XMLHttpRequest, textStatus, errorThrown) {
			                    // 状态码
			                    alert(XMLHttpRequest.status);
			                    // 状态
			                    alert(XMLHttpRequest.readyState);
			                    // 错误信息  
			                    alert(textStatus); 
			                }
			            });
	                	
	                	
	                }
	                
	            },
	            btn2: function (index, layero) {
	                return;
	            },
	            cancel: function () {
	                return;
	            },
	            end: function () {
	                //$('#addmain').css("display", "none");
	            }
	      });
		  return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
	});
	//初始化表格
	layui.use('table', function(){
	
		var table = layui.table;
		
		//监听工具条
		table.on('tool(user)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
		  var data = obj.data; //获得当前行数据
		  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		 
		  if(layEvent === 'del'){ //删除
		  	layer.confirm(''
  			,{area:['30%','27%']
  			,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">真的删除行么</div>'}
  			, function(index){
		    	$.ajax({
					type:"post",    //请求方式
	 			    async:true,    //是否异步
	                url: "ajax.do?ctrl=Sql_del",
	                data: data,
	                dataType: "json",
	                success: function(data){
	                	if(data.code=='000'){   
	                		layer.msg('删除sql成功！');
	                	}else{
	                		layer.alert('删除sql失败！');  
	                	}
	                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						layer.close(index);
						$("#button").click();
	                },
	                error: function (XMLHttpRequest, textStatus, errorThrown) {
	                    // 状态码
	                    alert(XMLHttpRequest.status);
	                    // 状态
	                    alert(XMLHttpRequest.readyState);
	                    // 错误信息  
	                    alert(textStatus); 
	                }
	            });
		      	
		    });
		  } else if(layEvent === 'edit'){ //编辑
		    //do something
		    var index=layer.open({
	            type: 1,
	            title: 'sql修改',
	            area: ['980px', '380px'],
	            shadeClose: false, //点击遮罩关闭
	            content: $('#editmain'),
	            btn: ['确定', '取消'],
	            success:function(){
		            $("#keys").val(data.key);
		            $("#value").val(data.value.trim());
	            },
	            yes: function (index, layero) {//添加人员
	                //做数据校验
	                var value=$("#value").val();
	                if(value==null||value==''||value=='undefied'){
	                	layer.msg('请输入sql！');
	                }else{
	                	$.ajax({
							type:"post",    //请求方式
			 			    async:true,    //是否异步
			                url: "ajax.do?ctrl=Sql_update",
			                data: {key:data.key,value:value},
			                dataType: "json",
			                success: function(data){
			                	if(data.code=='000'){   
			                		layer.msg('修改sql成功！');
			                	}else{
			                		layer.alert('修改sql失败！');  
			                	}
			                	layer.close(index);
								$("#button").click();
			                },
			                error: function (XMLHttpRequest, textStatus, errorThrown) {
			                    // 状态码
			                    alert(XMLHttpRequest.status);
			                    // 状态
			                    alert(XMLHttpRequest.readyState);
			                    // 错误信息  
			                    alert(textStatus); 
			                }
			            });
	                	
	                	
	                }
	                
	            },
	            btn2: function (index, layero) {
	                return;
	            },
	            cancel: function () {
	                return;
	            },
	            end: function () {
	                //$('#addmain').css("display", "none");
	            }
	        });
		    //同步更新缓存对应的值
		    obj.update({
		      TABLE_NAME: data.TABLE_NAME
		    });
		  }else if(layEvent === 'test'){ //调试
		  	var index=layer.open({
	            type: 1,
	            title: 'sql调试',
	            area: ['90%', '96%'],
	            shadeClose: false, //点击遮罩关闭
	            content: $('#testmain'),
	            success:function(){
	            	var value=data.value;
	            	var str='';
	            	if(value!=null){
	            		var arr=value.split("?");
	            		for (var i = 1; i < arr.length; i++) {
							str+='<label class="layui-form-label">参数'+i+'</label><div class="layui-input-inline">'
	            				+'<input type="text" id="par'+i+'" name="par'+i+'" required  autocomplete="off" class="layui-input"></div>';
						}
						$("#parSize").val(arr.length-1);
	            	}
	            	$("#testpar").html(str);
		            $("#value1").val(value);
		            $("#value2").val(value);
		            layui.use(['form'], function() {
						var form = layui.form;
						form.render();
					});
					$("#tthead2").html("");
					$("#ttbody2").html("");
				  	//执行渲染
					layui.use(['table'], function() {
						layui.table.init('user2', {
							height: 440 //设置高度
							//支持所有基础参数
						}); 
					});
					layui.use(['laypage'], function() {
		    		laypage = layui.laypage;
		    		laypage.render({
							elem: 'page2' //注意，这里的page2是 ID，不用加 # 号
							,limit:10//每页显示条数
							,limits:[10,20,30,50]//条数列表
							,layout:['prev', 'page', 'next','skip','count','limit']
							,curr:1
							,jump: function(data, first){
								//obj包含了当前分页的所有参数，比如：
								pageNo2 = data.curr;
								pageSize2 = data.limit;
								//首次不执行
								if(!first){
									//do something
									testQuery();
								}
							}
						});
			    	});
	            },
	            cancel: function () {
	                return;
	            },
	            end: function () {
	                //$('#addmain').css("display", "none");
	            }
	        });
		  }
		});
	
	});
	
	
</script>


</html>