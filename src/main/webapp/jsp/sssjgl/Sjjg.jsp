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

<title>数据整理</title>

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
		.layui-form xm-select-parent{
		width: 49.5%;
		display:inline-block;
		}

#button11 {
 position: relative;
}

#button11:before, #button11:after {
 position: absolute;
 right: -38px;
 top: 0px;
 border: 19px solid transparent;
 border-left-color: #1F9FFF;
 content: ' ';
}
</style>
</head>

<body style="overflow-x: hidden">
<blockquote class="layui-elem-quote layui-text">本功能页面用于对数据的自定义条件查询和修改并存为规则！</blockquote>
	<form class="layui-form" id="form1" action="">
		<div class="layui-form" lay-filter="test1">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">

					<div class="layui-colla-content  layui-show" style="line-height: 53px;margin: 0px auto;">
						<div class="layui-row">
					   <div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
										
								<div style="width: 22%;float: left;height: 38px;">
									<label class="layui-form-label" style="width: 33%; padding: 9px 0px 9px 0px">
									<span style="color:red;"> *  </span>查询表名：</label>
									<div class="layui-input-inline" style="width: 60%;">
										<select class="tableName" lay-filter="tableName" id="tableName" style="height: 38px;width: 100%;" lay-search="">
										
											<option value="">请选择</option>
											<option value="XWCS_GSDR_YSSJRK">入库原始数据表</option>
											<option value="XWCS_GSDR_YSSJJRK">净入库原始数据表</option>
										</select>	
									</div>
								</div>
										
								<div style="width: 22%;float: left;height: 38px;">
									<label class="layui-form-label" style="width: 30%; padding: 9px 0px 9px 0px"><span style="color:red;"> * </span> 导入日期：</label>
									<div class="layui-input-inline" style="width: 60%;">
						  				<input style="width: 100%;" type="text" class="layui-input" id="drrq" name="drrq" placeholder="请选择日期">
									</div>
								</div>

								<div style="width: 28%;float: left;height: 38px;">
									<label class="layui-form-label" style="width: 20%; padding: 9px 0px 9px 0px">查询条件：</label>
									<div class="layui-input-inline" style="width: 80%;">
										<select style="width:100%;" lay-filter="drfs" lay-search="" id="drfs" style="height: 38px;">											
										</select>
									</div>
								</div>
									
									<div style="float: left;">
											<button type="button" class="layui-btn " id="addtr">
														下一步
										    </button>
										</div>
								
								
								
								
								<div class="layui-row layui-col-space10" >
									
										<!-- 左边 -->
										<div style="width: 38%; border: 1px solid #e6e6e6; height: 200px; padding: 0; float: left; margin-left: 20px;margin-top:20px; border-radius: 5px;">
											<table class="layui-table" style="margin: 0px;margin-left: 0.5px;">
												<thead>
													<tr>
														<th><strong>条件定义</strong></th>
													</tr>
												</thead>
											</table>
											<div  style="width: 99%; height: 75px;">
												<div  class="layui-input-block" style="margin-left:62px;">

										 			<div id="left"  class="layui-input-inline" style="margin-top: 10%;width: 75%;">
										
													</div>
									
												</div>
											</div>
										</div>	


										<!-- 中间 -->
										<div style="width: 10%; height: 200px; padding: 0; margin-top:20px;margin-left: 5%;margin-right: 5%;float: left; text-align: center;">
											<a class="layui-btn" id ="button11"
												style="color: white; margin-top: 40%; right: 5.5%; border-radius: 5px 0px 0px 5px;"
												lay-filter="addcx2" lay-submit="addcx2">增加</a>
											<div style="text-align: center; margin-top: 25%;">
												<button type="button" class="layui-btn " id="chaxun">
												      查 询
												</button>
												<button class="layui-btn layui-btn-normal" id="doUp" type="doUp" lay-submit="" lay-filter="doUp">
													修 改
												</button>
											</div>
										</div>


										<!-- 右边 -->
										<div style="width: 38%; border: 1px solid #e6e6e6;margin-top:20px; height: 200px; padding: 0; float: left; border-radius: 5px;">
											<table class="layui-table" style="margin: 0px; margin-left: 0.5px;">
												<thead>
													<tr>
														<th><strong>已添加条件</strong></th>
													</tr>
												</thead>
											</table>
											<div style="width: 100%; height: 175px; overflow: auto;">		
												<table class="layui-table" id="table1" lay-filter="user1"
													style="margin: 0px;">	
												</table>	
											</div>
										</div>


									</div>
								
								
								<div class="layui-row layui-col-space10">
									<div id="selectname" style="width: 100%;"></div>
								</div>
								
								</div>

							</div>
						</div>
					</div>
				</div>
			</div>

			<fieldset class="layui-elem-field layui-field-title"
				style="margin-top: 20px;">
				<legend >查询展示</legend>

				<table class="layui-table" id="cxzssj" lay-filter="cxzssj">
				
				</table>
			</fieldset>
		</div>
	</form>
	
	
	
	<div id="upload" style="display: none;">
	
		<div style="height: 40px;margin-left: 22%;margin-top: 80px;">
			<div style="width: 21%;float: left;">
				<label style="width: 10%;line-height: 40px;">街道名称：</label>
			</div>
			<div style="height: 40px;width: 13%;float: left;">
    			<select id="jdxz" lay-verify="" lay-search style="height: 40px;width: 360px;margin-top: 0px;">
    			
    			</select>
    		</div>
		</div>
		<div style="width: 11%;height: 40px;margin: 10px auto;margin-top: 50px;">
		
			<button type="button" class="layui-btn " id="modify" type="modify" lay-submit="" lay-filter="modify" style="font-size: 20px;">
						保存
		    </button>
		</div>
		
	</div>
	
	
	<div id="upload1" style="display: none;">
			<div style="height: 40px;margin-left: 15%;margin-top: 20px;">
				<div style="width: 21%;float: left;">
					<label style="width: 10%;line-height: 40px;">规则名称:</label>
				</div>
				<div style="height: 40px;width: 60%;float: left;">
	    			<input type="text" class="layui-input" id="mbmc" name="mbmc"
									placeholder=""> 
	    		</div>
			</div>
			<div style="height: 40px;margin-left: 15%;margin-top: 20px;">
	    		<div style="width: 21%;float: left;">
					<label style="width: 10%;line-height: 40px;">规则描述:</label>
				</div>
				<div style="height: 40px;width: 60%;float: left;">
					<textarea id="ms" name="ms" class="layui-textarea"></textarea> 
	    		</div>
			</div>
			<div style="width: 18%;height: 40px;margin: 0px auto;margin-top: 100px;">
				<button type="button" class="layui-btn " id="addmb" style="font-size: 20px;">
							保存规则
			    </button>
			</div>
		</div>
	
	
</body>

<script>
//查询数据库的最大月份
getMaxData();
function getMaxData(){	
	$.ajax({
		type : "post", //请求方式
		async : true, //是否异步
		url:"sssjgl/getNsDate.do",
		data : "",
		dataType : "json",
		success:function(obj) {
			if(obj.code == "000"){
				var r = obj.data[0];
				//日期条件默认值为数据库中数据的最大入库日期
				$("#drrq").val(r.RKRQ);
			}
		}
	})
}



		var fz="";
		function showLoad() {
		    return layer.msg('拼命加载数据中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:100000});
		}
		
		
		//点击删除对应的查询条件 
		function deletes(obj,xh) {
			console.log(obj);
			obj.parentNode.parentNode.parentNode.removeChild(obj.parentNode.parentNode);
			//$("input[name=radio1]").size()>0
			
			//判断是否有tr 没有v 设置为0
			var a=document.getElementById("table1");
			var tr=a.getElementsByTagName("tr");
			console.log('-------------html:'+tr.length);
			if(tr.length==0){
				v = 0;
			}
			console.log('-------------v:'+v)
		}
		
		
		function closeLoad(index) {
		    layer.close(index);
		}
		function showSuccess() {
		    layer.msg('查询成功！',{time: 1000,offset: 'auto'});
		}
		//查询
		
		 function queryList(tableName){
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				url:"sssjgl/selectList.do",
				data: {
					tableName:tableName
				},
				dataType : "json",
				success : function(obj) {
					var data = obj.data;
					var options="<option value=''>请选择</option>";
				
					 for(var i in data){
						var zdm = data[i].ZDM;
						var mbm = data[i].MBM;
						options = options+"<option value="+zdm+">"+mbm+"</option>";
					} 
					 $("#drfs").append(options);
					layui.form.render();
				}
			})
				
		}
		 var formSelects;
			
			layui.config({base : 'static/js/'}).extend({formSelects : 'formSelects-v3'}).use([ 'form', 'formSelects' ],
					function() {
				formSelects = layui.formSelects;

			});
		
		
		
		var map = {};
		var maplist = {};
		layui.use(['form', 'layedit', 'laydate','laypage'], function() {
			var form = layui.form,
				layer = layui.layer,
				layedit = layui.layedit,
				laydate = layui.laydate,
				laypage=layui.laypage;
			form.on('select(tableName)', function(data){   
				  var val=data.value;
				 /*  if (val !="") { */
					  $("#drfs").html("");
					  $("#left").html("");
					  $("#table1").html("");
					queryList(val);
					
				/* } */
		 });
			laydate.render({
				elem: '#drrq',
				type: 'month',
				format: 'yyyyMM' //可任意组合
			});
			//添加展示
			var m=0;
			var m1=0;
			var k = 0;
			//判断进了那个条件定义
		    var pd = 0;
		    var v = 0;
		    //要拼接显示的值
			var val2 = "";
		    //拼接的sql 
		    var sql = "";
		    
			var seleid1 = "";
			var seleid11 = "";
			
			var input = "";
			var input2 = "";
			var strs2 ="";
			
			var text2 = "";
			var xz2 ="";
			 
			var pdval2="";
			$("#addtr").click(function() {
				var xz=document.getElementById("drfs").value;
				var xz=document.getElementById("drfs").value;
				xz2 = xz;
				$("#left").html("");
				 if(xz == ""){
					 return;
				 }else {
					
				if(m==0){
					$("#selectname").append("<div id='tr0'></div>");
				}
				$("#left").append("<div id='td"+m+"' style='width: 245%;padding-left: 1%;height: 40px;float: left;'></div>");
				//获取选中值的text文本
				var index=document.getElementById("drfs").selectedIndex;
				var text=document.getElementById("drfs").options[index].text
				text2 = text;
				//获取查询状态文本
				/* var queryindex=document.getElementById("queryList").selectedIndex;
				var queryList = document.getElementById("queryList").options[queryindex].text;
				 */
						if(text=="街道乡镇"||text=="征收项目"||text=="征收品目"||text=="行业大类"||text=="行业中类"||text=="行业门类"||text=="行业"||text=="登记注册类型"){
							pd = 1;
							document.getElementById("td"+m).style.width="245%";
							document.getElementById("td"+m).style.height="70px";
							document.getElementById("td"+m).style.marginTop="3px";
						var str = "<div style='float: left;'><label style='line-height: 40px;'>"+text+"：</label></div><input type='hidden' id='"+xz+""+k+"' value='hid' >";
						 str += "<select xm-select='select"+k+"' id='"+xz+"_"+k+"'  xm-select-type='1' style='width: 150%;' lay-filter='columns"+k+"'></select></div>";
							
						layui.config({base : 'static/js/'}).extend({formSelects : 'formSelects-v3'}).use([ 'form', 'formSelects' ],
								function() {
									var form = layui.form, formSelects = layui.formSelects;

									formSelects.value('select'); //获取选中的值
									formSelects.value('select', 'val'); //获取选中的val数组
									formSelects.value('select', 'name'); //获取选中的name数组
									
									var str1='<option value=""></option>';
									//调用接口进行查询
									$.ajax({
										type : "post", //请求方式
										async : true, //是否异步
										url:"sssjgl/selectSZ.do",
										data: { 
									        "xz": xz,
									        "tableName":$("#tableName").val(),
									        'rkrq':$("#drrq").val(),
									        
									    },
										dataType : "json",
										success : function(obj) {
											var array = obj.data;
											for (var i = 0; i < array.length; i++) {
												var zsxmmc = array[i].ZSXMMC;
												str1 +='<option value="'+zsxmmc+'">'+zsxmmc+'</option>';
												
											}
											
											$("#"+xz+"_"+k).html(str1);
											formSelects.render({
												name : 'select'+k,
												on : function(data, arr) {
													console.log(data);
													console.log(arr);
													var id =data.elem.id.replace("_","");
													//ltxx = formSelects.value('select'+k, 'val').join(",");
													var strs="";
													for(var i in arr){
													strs += arr[i].val+",";
														
													}
													if(strs.length>0){
														strs=strs.substring(0,strs.length-1);													
													}
													console.log(strs);
													document.getElementById(id).value=strs;
													
													strs2 = strs;
													//id='"+xz+"_"+k+"'
													seleid11 = xz+"_"+k;
													
												}
											});
											
											formSelects.value('select'+k, array); //动态赋值
											//修改根据classname获取修改所有样式
											var xlks =document.getElementsByClassName("xm-select-parent");
											for (var i = 0; i < xlks.length; i++) {
												xlks[i].style.display="inline-block";
												xlks[i].style.width="30%";
											}
											k++;
										}
									})
									
								});
						
						$("#td"+m).append(str);
						 m=m+1;
						
					}else if(text=="税收管理员"||text=="税率"||text=="主管税务所"||text=="预算科目"||text=="预算分配比例"||text=="上解销号人"||text=="入库销号人"||text=="总分机构类型"||text=="跨地区税收转移企业类型"||text=="应征凭证种类"||text=="开票人"||text=="调账类型"||text=="汇总票证种类"||text=="银行行别"||text=="票证种类"||text=="征收代理方式"){
						pd = 2;
						m=m-1;
						var seleall='';
						var options="<option value=''>请选择</option>";
						var options1="<option value=''>请选择</option>";
						var str = "";
						//调用接口进行查询
						$.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							url:"sssjgl/queryzd.do",
							data: { 
						        "xz": xz,
						        "tableName":$("#tableName").val(),
						    },
							dataType : "json",
							success : function(obj) {
								//将查询的结果添加进对应的select选项
									//选择框时
									map[xz] = "select";
								
									var s=xz;
									$.each(obj.data, function(i) {
					                      
					                      options=options+"<option value="+obj.data[i][s]+">"+obj.data[i][s]+"</option>";
					                      
					                     })

					                    
					                 seleall="<select id="+xz+"  lay-filter="+xz+" style='display: block;height: 40px;width: 65%;margin-left:10px;border: 1px solid #e6e6e6;' lay-search=''>"+options+"</select>";
				                    var strs = "<div style='float: left;width: 80%;'><div style='float: left;'><label style='line-height: 40px;'>"+text+"：</label></div><div style='float: left;width: 56%;'>"+seleall+"</div></div>";
				                    
				                    seleid1 =xz; 
				                    //把值存进条件展示
				        
				                    
				                    $("#td"+m).append(strs);  
				                    m=m+1;
							
							}
					});
						
						
					}else{
						if(text.indexOf("期")!=-1){
							pd = 3;
							m=m-1;
							var xz=document.getElementById("drfs").value;
							var options1="<option value=''>请选择</option>";
							var x = 1;
							$.ajax({
								type : "post", //请求方式
								async : true, //是否异步
								url:"sssjgl/selectislike.do",
								data: { 
							        "xz": xz,
							        "tableName":$("#tableName").val(),
							    },
								dataType : "json",
								success : function(obj) {
									var data = obj.data;
									for(var i in data){
										var czf = data[i].CZF;
										 options1 = options1 +"<option value='"+czf+"_"+ data[i].YMSRK+"' name='"+x+"'>"+czf+"</option>";
									 x++;
										
									}
									str = "<div style='float: left;width: 80%;'><div style='float: left;'><label style='line-height: 40px;'>"+text+":</label></div>";
									 str += "<select id='query1'   style='display: inline;height: 40px;margin-left: 10px;width: 11%;border: 1px solid #e6e6e6;'>"+options1+"</select><input type='text' class='layui-input'  id='"+xz+"_"+m1+"'  placeholder='' style='display:inline;width:32%;margin-left:10px'></div>"; 
									//str += "<select id='query1' name='select"+m1+"'  style='display: inline;height: 40px;width:28%;margin-left: 10px;'>"+options1+"</select><div style='float: right;width: 7%;text-align:center;'><a style='line-height: 40px;color: blue;cursor: pointer;' id='delete' class='"+xz+"'>删除</a></div></div>";
									 
									 //把值存进条件展示    id='"+xz+"_"+m1+"'
									 input = xz+"_"+m1; 
									 
									 $("#td"+m).append(str);
									  m=m+1;
									//年月范围
									laydate.render({
										elem: '#'+xz+"_"+m1,
										type: 'month',
										format: 'yyyyMM' //可任意组合
										
									});
									 map[xz] = "rq";
								
								}
						});
							
							
						}else{
							pd = 4;
							m=m-1;
							var xz=document.getElementById("drfs").value;
							var options1="<option value=''>请选择</option>";
							var y = 6;
							$.ajax({
								type : "post", //请求方式
								async : true, //是否异步
								url:"sssjgl/selectislike.do",
								data: { 
							        "xz": xz,
							        "tableName":$("#tableName").val(),
							    },
								dataType : "json",
								success : function(obj) {
									var data = obj.data;
									for(var i in data){
										var czf = data[i].CZF;
										 options1 = options1 +"<option value='"+czf+"_"+ data[i].YMSRK+"' name='"+y+"'>"+czf+"</option>";
									 y++;
										
									}
									str = "<div style='float: left;width: 80%;'><div style='float: left;'><label style='line-height: 40px;'>"+text+"：</label></div>";
									str += "<select id='query2' lay-filter="+xz+" style='display: inline;height: 40px;margin-left: 10px;width: 11%;border: 1px solid #e6e6e6;'>"+options1+"</select><input id='"+xz+"'  type='text' placeholder='请输入' class='layui-input' style='display:inline;width:32%;margin-left:10px'></div>";
									 
									$("#td"+m).append(str);
									m=m+1;
									map[xz] = "input";
									
									//把值存进条件展示
									input2 =xz; 
								}
						});
						}
						console.log(map);
						m1++;
					}
					m++;
				}
				
			});
			
			
			

			
			
			 //点击增加
			form.on('submit(addcx2)', function(data) {
				//判断为空时不给添加
				if(text2==''||$("#"+seleid1).val()==''||$("#query1").val()==''||$("#"+input).val()==''||$("#query2").val()==''||$("#"+input2).val()==''){
					layer.msg('条件定义选项不可为空！');
					return false;
				}
				//先判断属于那个条件类型  每种页面显示的不一样
				if(pd==1){
					if(strs2==''){
						layer.msg('条件定义选项不可为空！');
						return false;
					}
					//把值取出来 页面展示
					val2 = text2+"  in  ( "+strs2+" )";
					
					//sql 拼接
					var arr2 = strs2.split(",");
					var strs3 ="";
					for(var i = 0 ;i<arr2.length;i++){
						if(i==arr2.length-1){
							strs3 += "'"+arr2[i]+"'";
						}else{
							strs3 += "'"+arr2[i]+"',";
						}
						
					}
					sql = " and  a."+xz2+" in  ("+strs3+")";
					console.log("拼接：---------------"+sql);
					
			
				}else if(pd==2){
					val2 = text2  + "  =  "+$("#"+seleid1).val();
					//sql 拼接
				 	sql = " and  "+xz2+"  = '"+$("#"+seleid1).val()+"'" ;
					console.log("拼接：---------------"+sql); 
					$("#"+seleid1).val("");
					
				}else if(pd==3){
					
					var a = $("#query1").val();
					var date = $("#"+input).val();
					var strArr=a.split("_");
					val2 = text2+"  " + strArr[0]+"  " +date  ;
					$("#query1").val("");
					$("#"+input).val("");
					//sql 拼接
					if(xz2.endsWith("rq")){
						sql = " and  to_date(a."+xz2+",'yyyyMM') "+strArr[0]+ "to_date('"+date+"','yyyyMM')" ;
					}else {
						sql = " and  a."+xz2+strArr[0]+"'"+date+"'" ;
						console.log("拼接：---------------"+sql); 
					}
				 	
					
				}else if(pd==4){
					
					var a = $("#query2").val();
					var date = $("#"+input2).val();
					var strArr=a.split("_");
					val2 = text2+"  " + strArr[0]+"  " +date ;
					$("#query2").val("");
					$("#"+input2).val("");
					//sql 拼接
					if(strArr[0]=='='||strArr[0]=='!='){
						
						sql = " and  a."+xz2+" "+strArr[0]+ "'"+date+"'" ;
					}else{
						
						sql = " and  a."+xz2+" "+strArr[0]+" '%"+date+"%'" ;
					}
					
					console.log("拼接：---------------"+sql); 
					
					
				}
				//把值拼接到table
				v = v+1;
				var s = '<tr> <td>'+v+'</td>  <td>'+val2+'</td> <td><span style="color:red;cursor:pointer;"  onclick="deletes(this,'+v+')" >删除</span><input type="hidden" name="name_'+v+'" value="'+sql+'" /></td></tr>';	
				
				//前台显示
				$("#table1").append(s);
				
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});


			
			
			
/* 			//删除操作(将标签从页面上一出，同时将map中的key移除)
			$(document).on("click", "#delete", function () {
				
				//获取父节点的id,然后在页面上删除
				var fid=$(this).parent().parent().attr("id");
				if (fid == undefined) {
					fid = $(this).parent().parent().parent().attr("id");
				}
				var element=document.getElementById(fid);
				element.parentNode.removeChild(element);
				
				//获取点击元素的class,根据class从map的key中移除
				var claname=$(this).attr("class");
				delete map[claname];
				delete maplist[claname];
				
			});
 */			
			
			//查询
			$("#chaxun").click(function() {
				
				getData();
				return false;
			});
			
			
			//增加模板  var map = {};
			$("#addmb").click(function() {
				
				for(var prop in map){
				    if(map.hasOwnProperty(prop)){
				    	
				    	if(map[prop]=="select"){
				    		
				    		maplist[prop]=document.getElementById(prop).value;
				    	}else{
				    		
				    		maplist[prop]=$("#"+prop).val();
				    	}
				    }
				}
				
				var index = layer.load(2);
				ajax({
					url : "sssjgl/addmb.do",
					data : {
						"maplist" : JSON.stringify(maplist),
						"jdxz" : document.getElementById("jdxz").value,
						"mbmc" : $("#mbmc").val(),
						"ms" : $("#ms").val(),
						"tableName":$("#tableName").val()
					},
					type : 'post',
					success : function(obj) {
						
						layer.close(index);
						if (obj != null && obj.code == "000") {
							layer.close(uploadlayer1);
							layer.msg("模板保存成功！");		
						}
					}
				});
				
			});
			
			
			//修改页面
			form.on('submit(doUp)', function(data) {
				
				var input=$('#drfs option:selected') .val();
				console.log(input)
				if(input==''){
					layer.msg("请添加查询条件!!")
					return false;
				}
				
				 var form = $("#form1").serializeArray();
			    	for(var i in form){
			      		var obj = form[i];
			      		if (obj.value == "" &&obj.name!='drrq'&&obj.name!='XWCS_GSDR_YSSJRK'&&obj.name!='XWCS_GSDR_YSSJJRK') {
							layer.msg("请输入查询条件!!")
							return false;
						}
			      	}
			
				getxgnumber();
				jdall();	 
			

				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});
			
			
			//保存规则页面
			
			$("#modify").click(function() {
				layer.confirm('', 	{
						area : ['30%','27%' ],	
							 content : '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">'+'保存后不可撤销，是否修改？'+'</div>'
							,btn: ['是','否']
	                }, function(index){
                	var form = $("#form1").serializeArray();
                	layer.close(uploadlayer);
    				var index = layer.load(2);
    				for(var prop in map){
    				    if(map.hasOwnProperty(prop)){
    				    	
    				    	if(map[prop]=="select"||map[prop]=="select"){
    				    		
    				    		maplist[prop]=$("#"+prop).val();
    				    	}else{
    				    		
    				    		maplist[prop]=$("#"+prop).val();
    				    	}
    				    }
    				}
    				
    				var jdxz = document.getElementById("jdxz").value;
    				
   				 ajax({
   					url : "sssjgl/modify.do",
   					data : {
   						tableName:$("#tableName").val(),
   		        	  	drrq:$("#drrq").val(),
   						"jdxz" : jdxz,
   						"maplist" : JSON.stringify(maplist),
   						"form" : JSON.stringify(form)
   					},
   					type : 'post',
   					success : function(obj) {
   						layer.close(index);
   						getData();
   						console.log(obj);
   						if (obj != null && obj.code === "000") {
   							
   							layer.confirm(''
   									,{
   									area : ['30%','27%' ],	
   									 content : '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">'+obj.msg+'条数据修改完成，是否将此次操作存为规则？'+'</div>'
   									,btn: ['是','否']
   			                }, function(index){
   								uploadlayer1 = layer.open({
   									skin: 'demo-class',
   									type : 1,
   									title : '定义规则',
   									area : [ '50%', '50%' ],
   									shadeClose : false, //点击遮罩关闭
   									content : $('#upload1'),
   									success : function() {
   										return;
   									},
   									cancel : function() {
   										return;
   									},
   									end : function() {
   									}
   								});
   								});	
   						}
   					}
   					
   				}); 
    				
					});	
			
			 
			});
			
	
			
			
			
			function  getxgnumber(){
				var form = $("#form1").serializeArray();
				 
				
				ajax({
					url : "sssjgl/querySdbg1.do",
					data : {
						tableName:$("#tableName").val(),
		        	  	drrq:$("#drrq").val(),
		        	    form:JSON.stringify(form),
		        	    fz:fz
					},
					type : 'post',
					success : function(obj) {
						var data = obj.data;
						
						if (obj != null && obj.code === "000") {
							
							layer.confirm('', 	{
								area : ['30%','27%' ],	
								 content : '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">'+data[0].ALLCOUNT+'条数据将修改,'+'涉及'+data[0].NSRNO+'个纳税人,'+'涉及纳税额'+data[0].DFZSE+'（元）'+'是否修改？'+'</div>'
								,btn: ['是','否']
		                }, function(index){
			               	 uploadlayer = layer.open({
			 					skin: 'demo-class',
			 					type : 1,
			 					title : '属地变更',
			 					area : [ '50%', '50%' ],
			 					shadeClose : false, //点击遮罩关闭
			 					content : $('#upload'),
			 					success : function() {
			 						
			 						return;
			 					},
			 					cancel : function() {
			 						return;
			 					},
			 					end : function() {
			 						//$('#addmain').css("display", "none");
			 					}
			 				}); 
			               	layer.close(index);
								});	
						} 
					}
					
				}); 
			}
			
			
			function getData() {
				layui.use('table', function(){
			        var table = layui.table;
			        var input=$('#drfs option:selected') .val();
			        var inputTableName=$('#tableName option:selected') .val();
					console.log(input)
					/* if(inputTableName==''|| inputTableName == undefined){
						layer.msg("请选择查询表名!")
						return false;
					}else if (input==''|| input == undefined) {
						layer.msg("请添加查询条件!!")
						return false;
					}
			       
			    	for(var i in form){
			      		var obj = form[i];
			      		if (obj.value == "" &&obj.name!='drrq'&&obj.name!='XWCS_GSDR_YSSJRK'&&obj.name!='XWCS_GSDR_YSSJJRK') {
							layer.msg("请输入查询条件!!")
							return;
						}
			      	} */
			      	 var form = $("#form1").serializeArray();
					var index = layer.load(2);
			        //第一个实例
			        table.render({
			          elem: '#cxzssj'
			          ,height: 500
			          /* ,data: data //数据 */
			          ,width:document.body.clientWidth-20
			          ,method : 'post'
			          ,url:"sssjgl/querySdbg.do"
			          ,where: {
			        	  	tableName:$("#tableName").val(),
			        	  	drrq:$("#drrq").val(),
			        	    form:JSON.stringify(form),
			        	    fz:fz
				       }
			          ,limit:10
			          ,page: true //开启分页
			          ,cols: [[ //表头
			            {field: 'NSRMC', title: '纳税人名称', fixed: 'left'}
			            ,{field: 'NSRSBH', title: '纳税人识别号'}
			            ,{field: 'ZSXM_MC', title: '征收项目'}
			            ,{field: 'JD_MC', title: '街道乡镇', templet:'#zt'}
			            ,{field: 'DFKJ', title: '地方口径（元）',align:'right'}
			            ,{field: 'QKJ', title: '全口径（元）',align:'right'}
			          ]]
			        ,done : function(res, curr, count){
		      		 	layer.close(index);
		        	}
			        });
			        
			      });
			}
			
		  //查询街道  var map = {};
		  function jdall() { 
			  for(var prop in map){
				    if(map.hasOwnProperty(prop)){	
				    	if(map[prop]=="select"){	    		
				    		maplist[prop]=document.getElementById(prop).value;
				    	}else{
				    		maplist[prop]=$("#"+prop).val();
				    	}
				    }
				}
				
				ajax({
					url : "sssjgl/getjd.do",
					data : {},
					type : 'post',
					success : function(obj) {
						console.log(obj);
						var row="";
						$("#jdxz").find("option").remove();
						if (obj != null && obj.data != null) {
							
							
							document.getElementById("jdxz").options.length = 0; 
							
							/* 将街道名称添加入select  */
							$.each(obj.data, function(i) {
								
								if(i==0){
									
									row="<option value=''>请选择</option><option value='"+obj.data[i].JD_MC+"'>"+obj.data[i].JD_MC+"</option>";
								}else{
									
									row="<option value='"+obj.data[i].JD_MC+"'>"+obj.data[i].JD_MC+"</option>";
								}
								$("#jdxz").append(row);
								
							});
							
						}
					}
				});
				
			}
			
			
			function showSuccess() {
			    layer.msg('查询成功！',{time: 1000,offset: 'auto'});
			}
			
			
			
	
			
			
			
		});
		
</script>
<style type="text/css">
       body .demo-class{
           font-size: 25px;
       }
</style> 
</html>
