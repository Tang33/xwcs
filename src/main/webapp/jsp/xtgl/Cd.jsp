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
		本功能用于对系统的“功能菜单”进行操作管理！</blockquote>
	<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">

					<div class="layui-row">
						<div class="layui-col-md12">
							<div style="text-align: right;">
								<!-- <button class="layui-btn layui-btn-normal" id="button" type="button">查 询</button> -->
								<div class="layui-btn-group">
									<button class="layui-btn layui-btn-normal" id="button"
										type="button" lay-submit="" lay-filter="button">查 询</button>
									<button class="layui-btn layui-btn-primary" id="add"
										type="button" lay-submit="" lay-filter="addmain">新建主菜单</button>
									<button class="layui-btn layui-btn-primary" id="add"
										type="button" lay-submit="" lay-filter="add">新建子菜单</button>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>
		</div>
	</form>
	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend>查询显示</legend>
		 
		  <div class="layui-row">
		    <div class="layui-col-md2">
		    	<ul id="tree" style="margin-left:20%;"></ul>
		    </div>
		    <div class="layui-col-m10">
		    	<table class="layui-table" id="table" lay-filter="user">
					<thead>
						<tr>
							<th lay-data="{field:'num',width:'5%'}">序号</th>
							<th lay-data="{field:'uuid',width:'5%'}">序列</th>
							<th lay-data="{field:'gndm',width:'8%'}">功能代码</th>
							<th lay-data="{field:'gnmc',width:'12%',edit:'text'}">功能名称</th>
							<th lay-data="{field:'px',width:'8%'}">排序</th>
							<th lay-data="{field:'url',width:'18%'}">URL</th>
							<th lay-data="{field:'sjgndm',width:'8%'}">上级功能代码</th>
							<th lay-data="{field:'yxbz',width:'10%'}">有效标志</th>
							<th lay-data="{field:'tool',width:'12%',fixed:'right',toolbar:'#bar'}">操作</th>
						</tr>
					</thead>
					<tbody id="ttbody">
						
					</tbody>
				</table>
				<div id="page1"></div>
				<script type="text/html" id="bar">
					<a class="layui-btn layui-btn-xs" lay-event="test">启/禁用</a>
  					<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  					<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
				</script>
		    </div>
		  </div>

		
	</fieldset>
	<div id="editmain" style="display: none;">
		<form class="layui-form" id="form5" action=""
			style="margin-top: 20px;">
			<input type="hidden" id="uuid2" name="uuid2" />
			<div class="layui-form-item" style="margin-top:20px;">
				<label class="layui-form-label">功能代码</label>
				<div class="layui-input-inline">
					<input type="text" id="gndm2" name="gndm2" required
						lay-filter="gndm2" autocomplete="off" class="layui-input"
						readonly="readonly">
				</div>
			</div>
			<div class="layui-form-item" style="margin-top:20px;">
				<label class="layui-form-label">排序</label>
				<div class="layui-input-inline">
					<input type="text" id="px2" name="px2" required lay-filter="px2"
						autocomplete="off" class="layui-input">
				</div>
				<label class="layui-form-label">功能名称</label>
				<div class="layui-input-inline">
					<input type="text" id="gnmc2" name="gnmc2" required
						lay-filter="gnmc2" autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-form-item" style="margin-top:20px;">
				<label class="layui-form-label">url</label>
				<div class="layui-input-inline">
					<input type="text" id="url2" name="url2" required lay-filter="url2"
						autocomplete="off" class="layui-input">
				</div>
				<label class="layui-form-label">上级代码</label>
				<div class="layui-input-inline">
					<input type="text" id="sj_gndm2" name="sj_gndm2" required required
						lay-filter="sj_gndm2" autocomplete="off" class="layui-input">
				</div>
			</div>
		</form>
	</div>
	<div id="addold" style="display: none;">
		<form class="layui-form" id="form3" action=""
			style="margin-top: 20px;">
			<div class="layui-form-item" style="margin-top:20px;">
				<label class="layui-form-label">主菜单：</label>
				<div class="layui-input-inline">
					<select name="type3" id="type3" lay-search="" lay-filter="type3"></select>
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">是否启用</label>
				<div class="layui-input-block">
					<input type="checkbox" checked="" id="yxbz3" name="yxbz3"
						lay-skin="switch" lay-filter="yxbz3" title="是否启用" /> <input
						type="hidden" id="yxbz4" name="yxbz4" value="Y" />
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">功能代码</label>
				<div class="layui-input-inline">
					<input type="text" id="gndm3" name="gndm3" required
						autocomplete="off" class="layui-input">
				</div>
				<label class="layui-form-label">功能名称</label>
				<div class="layui-input-inline">
					<input type="text" id="gnmc3" name="gnmc3" required
						autocomplete="off" class="layui-input">
				</div>
			</div>
			<div class="layui-form-item">
				<label class="layui-form-label">排序</label>
				<div class="layui-input-inline">
					<input type="text" id="px3" name="px3" required autocomplete="off"
						class="layui-input">
				</div>
				<label class="layui-form-label">URL</label>
				<div class="layui-input-inline">
					<input type="text" id="url3" name="url3" required
						autocomplete="off" class="layui-input">
				</div>
			</div>
		</form>
	</div>
	<div id="addnew" style="display: none;margin-top: 20px;">
		<div class="layui-form-item">
			<label class="layui-form-label">主菜单名：</label>
			<div class="layui-input-inline">
				<input type="text" id="cdm" name="cdm" required autocomplete="off"
					class="layui-input">
			</div>
		</div>
	</div>
	<!-- 	</form> -->
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
	
	$(function() {
		tree();
		getData();
	});
	
	var zdm=0;
	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 10;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
	function tree(){
		
		$.ajax({
		    type: "post",
		    url: "ajax.do?ctrl=Index_load",
		    data:{
		    	gndm:0
		    },
		    dataType: 'json',
		    success: function (dta) {
		    	console.log(dta);
		        layui.use(['tree', 'layer','form'], function () {
		            layui.tree({
		                elem: '#tree',//绑定元素 
		                nodes: convert(dta),
		            });
		            var str="";
			        for(var i=0;i<dta.data.length;i++){
			        	console.log(dta.data);
			        	var data=dta.data[i];
			        	console.log(data);
			        	if(data.GNDM==zdm){
			        		str+='<option value="'+data.GNDM+'" selected="selected">'+data.GNMC+'</option>';	
			        	}else{
			        		str+='<option value="'+data.GNDM+'">'+data.GNMC+'</option>';
			        	}
			        }
			        $("#type3").html(str);
			        layui.form.render('select');
		            
		        });
		        
		    },
		    error:function(){
		    	alert("一場");
		    	
		    }
		});
                
	}
	function convert(rows){
	    var nodes = [];
	    for(var i=0;i<rows.data.length;i++){
	    	var data=rows.data[i];
	    	nodes.push({
	                id: data.GNDM,
	                name: data.GNMC,
	                href:'javascript:load('+data.GNDM+');'
	            });
	    }
	    
	    return nodes;//layui nodes对象
	}
	
	//ajax请求后台数据
    function load(i){
    	zdm=i;
        $.ajax({
			type:"post",    //请求方式
 			async:true,    //是否异步
            url: "ajax.do?ctrl=Cd_query&pageNo="+pageNo+"&pageSize="+pageSize,
            data:{
            	gndm:i
            },
            dataType: "json",
            success: function(obj){
                if(obj!=null&&obj.code=='000'){
                    getTbale(obj.data);
                    count  = obj.count;//数据总条数
	   				queryPage();
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
    //ajax请求后台数据
    function getData(){
        $.ajax({
			type:"post",    //请求方式
 			async:true,    //是否异步
            url: "ajax.do?ctrl=Cd_query&pageNo="+pageNo+"&pageSize="+pageSize,
            data: $("#form").serialize(),
            dataType: "json",
            success: function(obj){
                if(obj!=null&&obj.code=='000'){
                    getTbale(obj.data);
                    count  = obj.count;//数据总条数
	   				queryPage();
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
    
	//初始化表格
	function getTbale(data){
            var s = "";
            $.each(data,function(v,o){
                s+='<tr>';
                    s+='<td>'+(v+1)+'</td>';
                    s+='<td>'+o.UUID+'</td>';
                    s+='<td>'+o.GNDM+'</td>';
                    s+='<td>'+o.GNMC+'</td>';
                    s+='<td>'+o.PX+'</td>';
                    s+='<td>'+(o.URL==undefined?"-":o.URL)+'</td>';
                    s+='<td>'+o.SJGNDM+'</td>';
                    s+='<td>'+o.YXBZ+'</td>';
                    s+='</tr>';
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
		form.on('submit(button)',  function(data){
		  pageNo = 1; //当点击搜索的时候，应该回到第一页
		  getData();
		  return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		form.on('switch(yxbz3)', function(data){
			var falg=data.elem.checked;
			if(falg==false){
				$("#yxbz4").val('N');
			}else{
			    $("#yxbz4").val('Y');
			}
			console.log(data.elem.checked); //开关是否开启，true或者false
		}); 
		form.on('submit(addmain)',  function(data){
		  //do something
		  var index=layer.open({
	            type: 1,
	            title: '新增主菜单',
	            area: ['330px', '180px'],
	            shadeClose: false, //点击遮罩关闭
	            content: $('#addnew'),
	            btn: ['确定', '取消'],
	            success:function(){
		            
	            },
	            yes: function (index, layero) {//添加人员
	            	var cdm=$("#cdm").val();
	                $.ajax({
							type:"post",    //请求方式
			 			    async:true,    //是否异步
			                url: "ajax.do?ctrl=Cd_addmain",
			                data: {cdm:cdm},
			                dataType: "json",
			                success: function(data){
			                	if(data.code=='000'){   
			                		layer.msg('新增主菜单:'+cdm+'成功！');
			                		window.parent.location="index.do?ctrl=Index";
			                	}else{
			                		layer.alert('新增主菜单:'+cdm+'失败！');  
			                	}
			                	layer.close(index);
			                	getData();
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
		
		form.on('submit(add)',  function(data){
		  //do something
		  $("#type3").next().find(".layui-input").val(zdm);;
		  $("#type3").val(zdm);
		  form.render('select');
		  $("#gndm3").val("");
	      $("#gnmc3").val("");
	      $("#px3").val("");
	      $("#url3").val("");
	      $("#yxbz4").val("Y");
		  var index=layer.open({
	            type: 1,
	            title: '新增子菜单',
	            area: ['660px', '350px'],
	            shadeClose: false, //点击遮罩关闭
	            content: $('#addold'),
	            btn: ['确定', '取消'],
	            success:function(){
		            
	            },
	            yes: function (index, layero) {//添加人员
	                //做数据校验
	                var type3=$("#type3").val();
	                var gndm3=$("#gndm3").val();
			        var gnmc3=$("#gnmc3").val();
			        var px3=$("#px3").val();
			        var url3=$("#url3").val();
			        var yxbz3=$("#yxbz4").val();
			         
	                if(gnmc3==null||gnmc3==''||gnmc3=='undefied'){
	                	layer.msg('请输入功能名称！');
	                }else{
	                	$.ajax({
							type:"post",    //请求方式
			 			    async:true,    //是否异步
			                url: "ajax.do?ctrl=Cd_add",
			                data: {type3:type3,gndm3:gndm3,gnmc3:gnmc3,px3:px3,url3:url3,yxbz3:yxbz3},
			                dataType: "json",
			                success: function(data){
			                	if(data.code=='000'){   
			                		layer.msg('新增子菜单:'+gnmc3+'成功！');
			                	}else{
			                		layer.alert('新增子菜单:'+gnmc3+'失败！');  
			                	}
			                	var type=$("#type3").val();
							    $("#type").next().find(".layui-input").val(type);;
							    $("#type").val(type);
							    form.render('select');
			                	layer.close(index);
								$("#button").click();
								getData();
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
	                url: "ajax.do?ctrl=Cd_del",
	                data: data,
	                dataType: "json",
	                success: function(data){
	                	if(data.code=='000'){   
	                		layer.msg('删除功能菜单：'+data.data+'成功！');
	                	}else{
	                		layer.alert('删除功能菜单：'+data.data+'失败！');  
	                	}
	                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						layer.close(index);
						$("#button").click();
						getData();
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
	            	$("#uuid2").val(data.uuid);
		            $("#gndm2").val(data.gndm);
		            $("#gnmc2").val(data.gnmc);
		            $("#sj_gndm2").val(data.sjgndm);
		            $("#px2").val(data.px);
		            $("#url2").val(data.url);
	            },
	            yes: function (index, layero) {//添加人员
	                //做数据校验
	                var gnmc=$("#gnmc2").val();
	                if(gnmc==null||gnmc==''||gnmc=='undefied'){
	                	layer.msg('请输入功能名称！');
	                }else{
	                	$.ajax({
							type:"post",    //请求方式
			 			    async:true,    //是否异步
			                url: "ajax.do?ctrl=Cd_update",
			                data:$("#form5").serialize() ,
			                dataType: "json",
			                success: function(data){
			                	if(data.code=='000'){   
			                		layer.msg('修改菜单成功！');
			                	}else{
			                		layer.alert('修改菜单失败！');  
			                	}
			                	layer.close(index);
								$("#button").click();
								getData();
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
		  	$.ajax({
				type:"post",    //请求方式
			 	async:true,    //是否异步
			    url: "ajax.do?ctrl=Cd_test",
			    data: {gndm:data.gndm,yxbz:data.yxbz},
			    dataType: "json",
			    success: function(obj){
			       if(obj.code=='000'){   
			       	if(data.yxbz=="Y"){
			       		layer.msg('功能菜单：'+data.gnmc+' 禁用成功！');
			       	}else{
			       		layer.msg('功能菜单：'+data.gnmc+' 启用成功！');
			       	}
			       }else{
			           layer.alert('启/禁用失败！');  
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
		});
	
	});
	
	
</script>


</html>