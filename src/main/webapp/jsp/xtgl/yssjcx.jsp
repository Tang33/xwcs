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
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<title>按企业查询</title>
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<style type="text/css">
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
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

.layui-form-label {
    float: left;
    display: block;
    padding: 9px 15px;
    width: 80px;
    font-weight: 400;
    line-height: 20px;
    text-align: right;
}

.anli{
 color: #808080;
}



</style>
</head>

<body style="overflow-x: hidden">
<blockquote class="layui-elem-quote layui-text">本功能页面用于对净入库数据（原始数据）的自定义条件查询并可导出查询结果！</blockquote>
		<input type="hidden" id="dwid" value="${dwid}" />
	<form class="layui-form" id="form1" action="">
		<div class="layui-form" lay-filter="test1">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					
					<div class="layui-colla-content  layui-show" style="line-height: 53px;margin: 0px auto;">
						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">

									<div style="width: 18%; float: left; height: 38px;">
										<label class="layui-form-label"
											style="width: 33%; padding: 9px 0px 9px 0px"> <span
											style="color: red;"> * </span>查询表名：
										</label>
										<div class="layui-input-inline" style="width: 60%;">
											<select class="tableName" lay-filter="tableName"
												id="tableName" style="height: 38px; width: 100%;"
												lay-search="">
												<option value="">请选择</option>
												<option value="xwcs_gsdr_yssjrk">入库原始数据表</option>
												<option value="xwcs_gsdr_yssjjrk">净入库原始数据表</option>
												<option value="xwcs_gsdr_yssjtk">退库原始数据表</option>
												<option value="xwcs_gsdr_yssjgs">个人所得税原始数据表</option>
											</select>
										</div>
									</div>

									<div style="width: 18%;float: left;height: 38px;">
											<label class="layui-form-label" style="width: 40%; padding: 9px 0px 9px 0px"><span style="color:red;"> * </span> 税款所属年月：</label>
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
										
										<div style="width: 24%;float: right;height: 38px;margin-right: 2.5%;">
											<label class="layui-form-label" style="width: 20%; padding: 9px 0px 9px 0px">汇总条件：</label>
											<div class="layui-input-inline" style="width: 80%;">
												<select lay-filter="fz" id="fz"  style="height: 38px;width: 100%;" lay-search="">
													
												</select>	
											</div>
										</div>
								
																		
									<div class="layui-row layui-col-space10" >
									
										<!-- 左边 -->
										<div
											style="width: 38%; border: 1px solid #e6e6e6; height: 200px; padding: 0; float: left; margin-left: 20px; margin-top: 20px; border-radius: 5px;">
											<table class="layui-table"
												style="margin: 0px; margin-left: 0.5px;">
												<thead>
													<tr>
														<th><strong>条件定义</strong></th>
													</tr>
												</thead>
											</table>
											<div style="width: 99%; height: 75px;">
												<div class="layui-input-block" style="margin-left: 62px;">

													<div id="left" class="layui-input-inline"
														style="margin-top: 10%; width: 75%;"></div>

												</div>
											</div>
										</div>


										<!-- 中间 -->
										<div
											style="width: 10%; height: 200px; padding: 0; margin-top: 20px; margin-left: 5%; margin-right: 5%; float: left; text-align: center;">
											<button class="layui-btn" id="button11"
												style="color: white; margin-top: 40%; right: 5.5%; border-radius: 5px 0px 0px 5px;"
												lay-filter="addcx2" lay-submit="addcx2">增加</button>
											<div style="text-align: center; margin-top: 25%;">
												<button type="button" class="layui-btn" id="chaxun">
													查 询</button>
												<button type="button" class="layui-btn" id="selectfz"
													style="margin-left: 0px;">导 出</button>
											</div>
										</div>


										<!-- 右边 -->
										<div
											style="width: 38%; border: 1px solid #e6e6e6; margin-top: 20px; height: 200px; padding: 0; float: left; border-radius: 5px;">
											<table class="layui-table"
												style="margin: 0px; margin-left: 0.5px;">
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
										<div id="selectname" style="width: 100%;">
										</div>
									
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
	
		<div style="height: 40px;margin-left: 26%;margin-top: 40px;">
			<div style="width: 10%;float: left;">
				<label style="width: 10%;line-height: 40px;">街道名称：</label>
			</div>
			<div style="height: 40px;width: 13%;float: left;">
    			<select id="jdxz" lay-verify="" lay-search style="height: 30px;width: 200px;margin-top: 5px;">
    			
    			</select>
    		</div>
		</div>
		<div style="width: 20%;height: 40px;margin: 0px auto;margin-top: 20px;">
		
			<button type="button" class="layui-btn " id="modify">
						修改
		    </button>
		</div>
		<div style="height: 40px;margin-left: 26%;margin-top: 20px;">
			<div style="width: 10%;float: left;">
				<label style="width: 10%;line-height: 40px;">模板名称:</label>
			</div>
			<div style="height: 40px;width: 30%;float: left;">
    			<input type="text" class="layui-input" id="mbmc" name="mbmc"
								placeholder="">
    		</div>
		</div>
		<div style="width: 20%;height: 40px;margin: 0px auto;margin-top: 20px;">
		
			<button type="button" class="layui-btn " id="addmb">
						保存模板
		    </button>
		</div>
	</div>
</body>

<script>
var wait;
var dwid=$("#dwid").val();
var fz="";
	var showlayer;
	var index;
	$(function() {
		$("#table").attr("lay-data",
				"{width:" + document.body.clientWidth + "}");
		
	});
	
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
					var year = r.RKRQ.substring(0,4);
					var month = r.RKRQ.substring(4,6);
					var rr = year + "-" + month;
					$("#drrq").val(rr);
				}
			}
		})
	}
	
	function initfzzd(tableName) {
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url:"grsdsyssjcx/queryfz.do",
			data: { 
				tableName:tableName
		    },
			dataType : "json",
			success : function(obj) {
				if (obj!=null&&obj.code=="000") {
					var array = obj.data;
					var str="<option value=''>请选择</option>";
					for (var i = 0; i < array.length; i++) {
						str +='<option value="'+ array[i].ZDM+'">'+array[i].MBM+'</option>';						
					}
					$("#fz").append(str);
					layui.form.render();
				}
				
	
			}
	});
	}

	
	function showLoad() {
	    return layer.msg('拼命加载数据中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:100000});
	}
	
	function closeLoad(index) {
	    layer.close(index);
	}
	function showSuccess() {
	    layer.msg('查询成功！',{time: 1000,offset: 'auto'});
	}
	function hidecolumn() {
			array = [  2, 3, 4, 5, 6, 7, 8, 9, 10,
				11, 12, 13, 14,16,17,18,19,20,21,22,23,24,25,26,27,28,29 ];
		
		for (var i = 0; i < array.length; i++) {
			$('.layui-table:eq(0) thead tr th:eq(' + array[i] + ')').hide();
			$('.layui-table:eq(1) thead tr th:eq(' + array[i] + ')').hide();
			$('.layui-table:eq(2) tbody tr').find('td:eq(' + array[i] + ')')
					.hide();
		}
	}
	function showcolumn() {
		var array = [  2, 3, 4, 5, 6, 7, 8, 9, 10,
			11 ];
		if (dwid == "00") {
			array = [  2, 3, 4, 5, 6, 7, 8, 9, 10,
				11, 12, 13, 14 ];
		}
		for (var i = 0; i < array.length; i++) {
			$('.layui-table:eq(0) thead tr th:eq(' + array[i] + ')').show();
			$('.layui-table:eq(1) thead tr th:eq(' + array[i] + ')').show();
			$('.layui-table:eq(2) tbody tr').find('td:eq(' + array[i] + ')').show();
		}

	}
	//查询页面加载下拉框中的数据
	 function selectList(tableName){
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url:"grsdsyssjcx/selectList.do",
			data: { 
				tableName:tableName,
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
	var v = 0;
	//设置下拉框的东西
	layui.use(['form', 'layedit', 'laydate','laypage'], function() {
		var form = layui.form,
			layer = layui.layer,
			layedit = layui.layedit,
			laydate = layui.laydate,
			laypage=layui.laypage;
		
		form.on('select(tableName)', function(data){   
			  var val=data.value;
			  if (val !="") {
				  $("#drfs").html("");
				  $("#left").html("");
				  $("#table1").html("");
				  selectList(val);
				  $("#fz").html("");
				  initfzzd(val);
			}
	 });
		
		laydate.render({
			elem: '#drrq',
			type: 'month' 
			//format: 'yyyyMM' 
		});

		//添加展示
		var m=0;
		var m1=0;
	    var k = 0;
	   
	    //判断进了那个条件定义
	    var pd = 0;
	    
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
			xz2 = xz;
			$("#left").html("");
			 if(xz == ""){
				 
				 return;
			 }else {
				 
				 //定义一个取值的变量
				
				 
				 //清空之前存在的选择条件
			if(m==0){
				$("#selectname").append("<div id='tr0' ></div>");
			}
			$("#left").append("<div id='td"+m+"' style='width: 245%;padding-left: 1%;height: 40px;float: left;'></div>");
			//获取选中值的text文本
			var index=document.getElementById("drfs").selectedIndex;
			var text=document.getElementById("drfs").options[index].text;
			console.log('--------------------text2:'+text2)
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
						
								formSelects.value('select'); //获取选中的值
								formSelects.value('select', 'val'); //获取选中的val数组
								formSelects.value('select', 'name'); //获取选中的name数组 
								console.log("----------------------xz:"+xz)
								var str1='<option value=""></option>';
								//调用接口进行查询
								var tableName = $('#tableName').val();
								$.ajax({
									type : "post", //请求方式
									async : true, //是否异步
									url:"grsdsyssjcx/selectSZ.do",
									data: { 
										
								        "xz": xz,
								        "tableName":tableName
								    },
									dataType : "json",
									success : function(obj) {
										var array = obj.data;
										for (var i = 0; i < array.length; i++) {
											var selname = array[i].SELNAME;
											str1 +='<option value="'+selname+'">'+selname+'</option>';
											
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
												
												/* var input = document.getElementsByClassName("xm-select");
												for (var i = 0; i < input.length; i++) {
													input[i].style.width="100%";
												} */
											}
										});
										
										formSelects.value('select'+k, array); //动态赋值
										
										//修改根据classname获取修改所有样式
										var xlks =document.getElementsByClassName("xm-select-parent");
										for (var i = 0; i < xlks.length; i++) {
											xlks[i].style.display="inline-block";
											xlks[i].style.width="30%";
											/* xlks[i].style.width="1300px"; */
										}
										k++;
										
										
										
										
									}
							});
					
							
							
							$("#td"+m).append(str);
							 m=m+1;
					 
			 }else if(text=="登记注册类型"||text=="预算分配比例"||text=="税款种类"||text=="总分机构类型"||text=="跨地区税收转移企业类型"||text=="应征凭证种类"||text=="汇总票证种类"||text=="调账类型"||text=="银行行别"||text=="票证种类"){
				
					pd = 2;
					m=m-1;
					var seleall='';
					var options="<option value=''>请选择</option>";
					console.log('---------------------------xz:'+xz)
					//var options1="<option value=''>请选择</option>";
					var str = "";
					var tableName = $('#tableName').val();
					//调用接口进行查询
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						url:"grsdsyssjcx/queryzd.do",
						data: { 
					        "xz": xz,
					        "tableName":tableName
					    },
						dataType : "json",
						success : function(obj) {
							//将查询的结果添加进对应的select选项
								//选择框时
								map[xz] = "select";
							
								for (var i = 0; i < obj.data.length; i++) {
									options=options+"<option value="+obj.data[i].xmmc+">"+obj.data[i].xmmc+"</option>";
				                      
								}
								
				                     
				                    seleall="<select id="+xz+"  lay-filter="+xz+" style='display: block;height: 40px;width: 65%;margin-left:10px;border: 1px solid #e6e6e6;' lay-search=''>"+options+"</select>";
				                    var strs = "<div style='float: left;width: 80%;'><div style='float: left;'><label style='line-height: 40px;'>"+text+"：</label></div><div style='float: left;width: 56%;'>"+seleall+"</div></div>";
				                    
				                    seleid1 =xz; 
				                    //把值存进条件展示
				        
				                    
				                    $("#td"+m).append(strs);
				                    //<div style='float: right;width: 7%;text-align:center;'><a style='line-height: 40px;color: blue;cursor: pointer;' id='delete' class='"+xz+"'>删除</a></div>
				                   m=m+1;
						
						}
					});
					
					
				}else{
					//如果选择是日期的
					if(text.indexOf("期")!=-1){
						pd = 3;
						m=m-1;
						var xz=document.getElementById("drfs").value;
						var options1="<option value=''>请选择</option>";
						var tableName = $('#tableName').val();
						var x = 1;
						$.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							url:"grsdsyssjcx/selectislike.do",
							data: { 
						        "xz": xz,
						        "tableName":tableName
						    },
							dataType : "json",
							success : function(obj) {
								var data = obj.data;
								for(var i in data){
									var czf = data[i].CZF;
									 options1 = options1 +"<option value='"+czf+"_"+ data[i].YMSRK+"' >"+czf+"</option>";
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
								});
								 map[xz] = "rq";
							
							}
						});
						
						
					}else{
						//如果是文本框
						pd = 4;
						m=m-1;
						var xz=document.getElementById("drfs").value;
						var options1="<option value=''>请选择</option>";
						var y = 6;
						var tableName = $('#tableName').val();
						$.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							url:"grsdsyssjcx/selectislike.do",
							data: { 
						        "xz": xz,
						        "tableName":tableName
						    },
							dataType : "json",
							success : function(obj) {
								var data = obj.data;
								for(var i in data){
									var czf = data[i].CZF;
									 options1 = options1 +"<option value='"+czf+"_"+ data[i].YMSRK+"' >"+czf+"</option>";
									 y++;
									
								}
								str = "<div style='float: left;width: 80%;'><div style='float: left;'><label style='line-height: 40px;'>"+text+"：</label></div>";
								str += "<select id='query2' lay-filter="+xz+" style='display: inline;height: 40px;margin-left: 10px;width: 11%;border: 1px solid #e6e6e6;'>"+options1+"</select><input id='"+xz+"'  type='text' placeholder='请输入' class='layui-input' style='display:inline;width:32%;margin-left:10px'></div>";
								 
								$("#td"+m).append(str);
								m=m+1;
								map[xz] = "input";
								
								//把值存进条件展示
								input2 =xz; 
								
			                    //val2 = text+"  in  ( "+strs+" )";
								
							}
						});
					}
					console.log('------------选择的值')
					console.log(map);
					m1++;
				}
				m++;
			}
			
		})
		
		
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
				sql = " and  "+xz2+" in  ("+strs3+")";
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
			 	sql = " and  "+xz2+strArr[0]+"'"+date+"'" ;
				console.log("拼接：---------------"+sql); 
				
			}else if(pd==4){
				
				var a = $("#query2").val();
				var date = $("#"+input2).val();
				var strArr=a.split("_");
				val2 = text2+"  " + strArr[0]+"  " +date ;
				$("#query2").val("");
				$("#"+input2).val("");
				//sql 拼接
				if(strArr[0]=='='||strArr[0]=='!='){
					sql = " and  "+xz2+" "+strArr[0]+ "'"+date+"'" ;
				}else{
					sql = " and  "+xz2+" "+strArr[0]+" '%"+date+"%'" ;
				}
				console.log("拼接：---------------"+sql); 
				
				
			}
			console.log('-------------v11111:'+v)
			//把值拼接到table
			v = v+1;
			/* var s = "<tr> <td>"+v+"</td>  <td>"+val2+"</td> <td><span style='color:red;cursor:pointer;' onclick='deletes(this,"+v+")' >删除</span><input type='hidden' name='name_"+v+"'  value='"+sql2+"' /></td> </tr>"; */ 
			var s = '<tr> <td>'+v+'</td>  <td>'+val2+'</td> <td><span style="color:red;cursor:pointer;"  onclick="deletes(this,'+v+')" >删除</span><input type="hidden" name="name_'+v+'" value="'+sql+'" /></td></tr>';	
			
			//前台显示
			$("#table1").append(s);
			
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		
	});
	
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
	

		
	//删除操作(将标签从页面上一出，同时将map中的key移除)
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
		
		
	//根据条件导出数据-------------
	$("#selectfz").click(function() {
		tableName
		debugger
		 var form = $("#form1").serializeArray();
		var tableName = $("#tableName").val();
		
		if(tableName==''){
  			layer.msg("请选择税查询表明!")
			return;
  			
  		}
	    	for(var i in form){
	      		var obj = form[i];
	      		
	      		
	      		if (obj.value == "") {
					layer.msg("请选择税款所属年月!")
					return;

				}
	      	}
	    	window.location.href = encodeURI("outSdbg3.do?drrq="+$("#drrq").val()+"&fz="+$("#fz").val()+"&form="+JSON.stringify(form)+"&tableName="+$("#tableName").val())
	    	//window.open(encodeURI("outSdbg3.do?drrq="+$("#drrq").val()+"&fz="+$("#fz").val()+"&form="+JSON.stringify(form)+"&tableName="+$("#tableName").val()));
	});
	
		//查询
		$("#chaxun").click(function() {
			var index = document.getElementById("fz").selectedIndex;
			var name = document.getElementById("fz").options[index].text
			var val = $("#fz").val();
			val = val.toUpperCase()//转换成大写
			var tableName = $("#tableName").val();
			console.log(tableName);
			if(tableName == "xwcs_gsdr_yssjtk"){
				if ($("#fz").val()!="") {
					layui.use('table', function(){
				        var table = layui.table;
				        var form = $("#form1").serializeArray();
				    	for(var i in form){
				      		var obj = form[i];
				      		if (obj.value == "") {
								layer.msg("请选择税款所属年月!")
								return;
							}
				      	}
				    	var index = layer.load(2);
				        //第一个实例
				        table.render({
				          elem: '#cxzssj'
				          ,height: 500
				          ,width : document.body.clientWidth
				          /* ,data: data //数据 */
				          ,method : 'post'
				          ,url:"tk/tkcx.do"
				          ,cellMinWidth : 150
				          ,where: {
				        	  drrq:$("#drrq").val(),
				        	  fz:$("#fz").val(),
				        	    form:JSON.stringify(form)
					       }
				          ,limit:10
				          ,page: true //开启分页
				          ,cols: [[ //表头
							
							{field:val,title:name}
							,{field:'SE',title:'税额',fixed:'right'}
							,{field:'DFBL',title:'地方比例',fixed:'right'}
							,{field:'QXJ',title:'区县级金额',fixed:'right'}
				          ]]
				        ,done : function(res, curr, count){
			      		 	layer.close(index);
			        	}
				      });
				        
				     });
				}else {
				
				layui.use('table', function(){
			        var table = layui.table;
			        var form = $("#form1").serializeArray();
			    	for(var i in form){
			      		var obj = form[i];
			      		if (obj.value == "") {
			      			layer.msg("请选择税款所属年月!")
							return;
						}
			      	}
			    	var index = layer.load(2);
			        //第一个实例
			        table.render({
			          elem: '#cxzssj'
			          ,height: 500
			          ,width : document.body.clientWidth
			          /* ,data: data //数据 */
			          ,method : 'post'
			          ,url:"tk/tkcx.do"
			          ,cellMinWidth : 150
			          ,where: {
			        	  drrq:$("#drrq").val(),
			        	 	 fz:$("#fz").val(),
			        	    form:JSON.stringify(form)
				       }
			          ,limit:10
			          ,page: true //开启分页
			          ,cols: [[ //表头
						{field:'NSRSBH',title:'纳税人识别号'}
						,{field:'NSRMC',title:'纳税人姓名'}
						,{field:'PZZL',title:'票证种类'}
						,{field:'PZZG',title:'票证字轨'}
						
						,{field:'PZHM',title:'票证号码'}
						,{field:'ZSXM',title:'征收项目'}
						,{field:'ZSPM',title:'征收品目'}
						,{field:'SKSSQQ',title:'税款所属期起'}
						
						,{field:'XMMC',title:'细目名称'}
						,{field:'SKSSQZ',title:'税款所属期止'}
						,{field:'SKZL',title:'税款种类'}
						
						,{field:'SKSX',title:'税款属性'}
						,{field:'DJZCLX',title:'登记注册类型'}
						,{field:'HYML',title:'行业门类'}
						,{field:'HYDL',title:'行业大类'}
						,{field:'HYZL',title:'行业中类'}
						,{field:'HY',title:'行业'}
						,{field:'SE',title:'税额',fixed:'right'}
						,{field:'YSKM',title:'预算科目'}
						,{field:'YSKMMC',title:'预算科目名称'}

						,{field:'YSFPBL',title:'预算分配比例'}
						,{field:'SKGK',title:'收款国库'}
						,{field:'TTSJLX',title:'提退税金类型'}
						,{field:'TDSFYJWSZH',title:'退抵税（费）依据文书字号'}

						,{field:'YHYYWD',title:'银行营业网点'}
						,{field:'ZHMC',title:'账户名称'}
						,{field:'YHZH',title:'银行账号'}
						,{field:'KPRQ',title:'开票日期'}

						,{field:'THRQ',title:'退还日期'}
						,{field:'XHRQ',title:'销号日期'}
						,{field:'XHR',title:'销号人'}
						,{field:'JDXZ',title:'街道乡镇'}
						,{field:'TTJG',title:'提退机构'}
						,{field:'SSGLY',title:'税收管理员'}
						,{field:'SKSSSWJG',title:'税款所属税务机关'}
						
						,{field:'ZGSWS',title:'主管税务所（科、分局）'}
						,{field:'SWJG',title:'税务机关'}
						,{field:'TDSFYYLX',title:'退抵税费原因类型'}
						,{field:'DZSPHM',title:'电子税票号码'}
						,{field:'TZLX',title:'调账类型'}
						,{field:'CZLX',title:'操作类型'}
						,{field:'SSJMXZDL',title:'税收减免性质大类'}
						
						,{field:'SSJMXZXL',title:'税收减免性质小类'}
						,{field:'FHJG',title:'复核结果'}
						,{field:'LRR',title:'录入人'}
						,{field:'KPR',title:'开票人'}
						,{field:'LRRQ',title:'录入日期'}
						,{field:'RK_RQ',title:'导入日期'}
						
						,{field:'SSWJ',title:'所属文件'}
						,{field:'ID',title:'ID'}
						,{field:'DRFS',title:'导入方式'}
						
						,{field:'SJZW',title:'数据指纹'}
						,{field:'DFBL',title:'地方比例',fixed:'right'}
						,{field:'QXJ',title:'区县级金额',fixed:'right'}

			          ]]
			        ,done : function(res, curr, count){
		      		 	layer.close(index);
		        	}
			      });
			        
			     });
				}
			} else if(tableName == "xwcs_gsdr_yssjrk") {
				if ($("#fz").val()!="") {
					layui.use('table', function(){
				        var table = layui.table;
				        var form = $("#form1").serializeArray();
				    	for(var i in form){
				      		var obj = form[i];
				      		if (obj.value == "") {
								layer.msg("请选择税款所属年月!")
								return;
							}
				      	}
				    	var index = layer.load(2);
				        //第一个实例
				        table.render({
				          elem: '#cxzssj'
				          ,height: 500
				          ,width : document.body.clientWidth
				          /* ,data: data //数据 */
				          ,method : 'post'
				          ,url:"rkyssjcx/querySdbg.do"
				          ,cellMinWidth : 150
				          ,where: {
				        	  drrq:$("#drrq").val(),
				        	  fz:$("#fz").val(),
				        	    form:JSON.stringify(form)
					       }
				          ,limit:10
				          ,page: true //开启分页
				          ,cols: [[ //表头
							
							{field:val,title:name}
							,{field:'SJJE',title:'实缴金额',fixed:'right'}
							,{field:'ZYJ',title:'中央级',fixed:'right'}
							,{field:'SSJ',title:'省市级',fixed:'right'}
							,{field:'DSJ',title:'地市级',fixed:'right'}
							,{field:'QXJ',title:'区县级',fixed:'right'}
							,{field:'XZJ',title:'乡镇级',fixed:'right'}

				          ]]
				        ,done : function(res, curr, count){
			      		 	layer.close(index);
			        	}
				      });
				        
				     });
				}else {
					
				layui.use('table', function(){
		        var table = layui.table;
		        var form = $("#form1").serializeArray();
		    	for(var i in form){
		      		var obj = form[i];
		      		if (obj.value == "") {
						layer.msg("请选择税款所属年月!")
						return;
					}
		      	}
		    	var index = layer.load(2);
		        //第一个实例
		        table.render({
		          elem: '#cxzssj'
		          ,height: 500
		          ,width : document.body.clientWidth
		          /* ,data: data //数据 */
		          ,method : 'post'
		          ,url:"rkyssjcx/querySdbg.do"
		          ,cellMinWidth : 150
		          ,where: {
		        	  drrq:$("#drrq").val(),
		        	  fz:$("#fz").val(),
		        	    form:JSON.stringify(form)
			       }
		          ,limit:10
		          ,page: true //开启分页
		          ,cols: [[ //表头
					{field:'NSRSBH',title:'纳税人识别号'}
					,{field:'NSRMC',title:'纳税人名称'}
					,{field:'ZSXMMC',title:'征收项目'}
					,{field:'ZSPM',title:'征收品目'}
					
					,{field:'ZSPMDM',title:'征收品目代码'}
					,{field:'SKSHQQ',title:'税款所属期起'}
					,{field:'SKSHQZ',title:'税款所属期止'}
					,{field:'DJZCLX',title:'登记注册类型'}
					
					,{field:'SSGLY',title:'税收管理员'}
					,{field:'HYML',title:'行业门类'}
					,{field:'HYDL',title:'行业大类'}
					
					,{field:'HYZL',title:'行业中类'}
					,{field:'HY',title:'行业'}
					,{field:'ZSDLFS',title:'征收代理方式'}
					,{field:'JSYJ',title:'计税依据'}
					,{field:'KSSL',title:'课税数量'}
					,{field:'SL',title:'税率'}
					,{field:'SJJE',title:'实缴金额',fixed:'right'}
					,{field:'SKSSSWJG',title:'税款所属税务机关'}
					,{field:'ZGSWS',title:'主管税务所（科、分局）'}

					,{field:'YSKMDM',title:'预算科目代码'}
					,{field:'YSKM',title:'预算科目'}
					,{field:'YSFPBL',title:'预算分配比例'}
					,{field:'ZYJBL',title:'中央级比例'}

					,{field:'SSJBL',title:'省市级比例'}
					,{field:'DSJBL',title:'地市级比例'}
					,{field:'QXJBL',title:'区县级比例'}
					,{field:'XZJBL',title:'乡镇级比例'}

					,{field:'ZYDFPBL',title:'中央待分配比例'}
					,{field:'DSDFPBL',title:'地市待分配比例'}
					,{field:'SJDFPBL',title:'省局待分配比例'}
					,{field:'SKGK',title:'收款国库'}
					,{field:'KJRQ',title:'开具日期'}
					,{field:'SJRQ',title:'上解日期'}
					,{field:'SJXHRQ',title:'上解销号日期'}
					
					,{field:'SJXHR',title:'上解销号人'}
					,{field:'RKRQ',title:'入库日期'}
					,{field:'RKXHRQ',title:'入库销号日期'}
					,{field:'RKXHR',title:'入库销号人'}
					,{field:'PZZL',title:'票证种类'}
					,{field:'PZZG',title:'票证字轨'}
					,{field:'PZHM',title:'票证号码'}
					
					,{field:'SKZL',title:'税款种类'}
					,{field:'SKSX',title:'税款属性'}
					,{field:'JDXZ',title:'街道乡镇'}
					,{field:'YHHB',title:'银行行别'}
					,{field:'YHYYWD',title:'银行营业网点'}
					,{field:'YHZH',title:'银行账号'}
					
					,{field:'TZLX',title:'调账类型'}
					,{field:'JKQX',title:'缴款期限'}
					,{field:'ZSSWJG',title:'征收税务机关'}
					
					,{field:'DZSPHM',title:'电子税票号码'}
					,{field:'ZYJ',title:'中央级',fixed:'right'}
					,{field:'SSJ',title:'省市级',fixed:'right'}
					,{field:'DSJ',title:'地市级',fixed:'right'}
					,{field:'QXJ',title:'区县级',fixed:'right'}
					,{field:'XZJ',title:'乡镇级',fixed:'right'}
					,{field:'ZYDFP',title:'中央待分配'}
					 
					 
					,{field:'SJDFP',title:'省级待分配'}
					,{field:'DSDFP',title:'地市待分配'}
					,{field:'DJXH',title:'登记序号'}
					,{field:'ZFJGLX',title:'总分机构类型'}
					,{field:'KDQSSZYQYLX',title:'跨地区税收转移企业类型'}

		          ]]
		        ,done : function(res, curr, count){
	      		 	layer.close(index);
	        	}
		      });
		        
		     });
		} 
			} else if(tableName == "xwcs_gsdr_yssjjrk") {
				if ($("#fz").val()!="") {
					layui.use('table', function(){
				        var table = layui.table;
				        var form = $("#form1").serializeArray();
				    	for(var i in form){
				      		var obj = form[i];
				      		if (obj.value == "") {
								layer.msg("请选择税款所属年月!")
								return;
							}
				      	}
				    	var index = layer.load(2);
				        //第一个实例
				        table.render({
				          elem: '#cxzssj'
				          ,height: 500
				          ,width : document.body.clientWidth
				          /* ,data: data //数据 */
				          ,method : 'post'
				          ,url:"jrkyssjcyController/jrkyssjquery.do"
				          ,cellMinWidth : 150
				          ,where: {
				        	  drrq:$("#drrq").val(),
				        	  fz:$("#fz").val(),
				        	    form:JSON.stringify(form)
					       }
				          ,limit:10
				          ,page: true //开启分页
				          ,cols: [[ //表头
							
							{field:val,title:name}
							,{field:'SJJE',title:'实缴金额',fixed:'right'}
							,{field:'ZYJ',title:'中央级',fixed:'right'}
							,{field:'SSJ',title:'省市级',fixed:'right'}
							,{field:'DSJ',title:'地市级',fixed:'right'}
							,{field:'QXJ',title:'区县级',fixed:'right'}
							,{field:'XZJ',title:'乡镇级',fixed:'right'}

				          ]]
				        ,done : function(res, curr, count){
			      		 	layer.close(index);
			        	}
				      });
				        
				     });
				}else{
					
				layui.use('table', function(){
			        var table = layui.table;
			        //表单序列化数组
			        var form = $("#form1").serializeArray();
			      	for(var i in form){
			      		var obj = form[i];
			      		if (obj.value == "") {
			      			layer.msg("请选择税款所属年月!")
							return;
						}
			      	}
			    	var index = layer.load(2);
			        //第一个实例
			        table.render({
			          elem: '#cxzssj'
			          ,height: 500
			          ,width:document.body.clientWidth//设置宽度为body宽度
			          /* ,data: data //数据 */
			          ,method : 'post'
			          ,url:"jrkyssjcyController/jrkyssjquery.do"
			          ,cellMinWidth: 120
			          ,where: {
			        	  drrq:$("#drrq").val(),
	        	 			fz:$("#fz").val(),
			        	    form:JSON.stringify(form)
				       }
			          ,limit:10
			          ,page: true //开启分页
			          ,cols: [[ //表头
			        	{field: 'NSRSBH', title: '纳税人识别号',fixed: 'left',width:120}
			            ,{field: 'NSRMC', title: '纳税人名称', fixed: 'left',width:120}
			            ,{field: 'ZSXMMC', title: '征收项目',width:120}
			            ,{field: 'ZSPM', title: '征收品目',width:120}
			            ,{field: 'ZSPMDM', title: '征收品目代码',width:120}
			            ,{field: 'SKSHQQ', title: '税款所属期起',width:120}
			            ,{field: 'SKSHQZ', title: '税款所属期止',width:120}
			            ,{field: 'DJZCLX', title: '登记注册类型',width:120}
			            ,{field: 'SSGLY', title: '税收管理员',width:120}
			            ,{field: 'HYML', title: '行业门类',width:120}
			            ,{field: 'HYDL', title: '行业大类',width:120}
			            ,{field: 'HYZL', title: '行业中类',width:120}
			            ,{field: 'HY', title: '行业',width:120}
			            ,{field: 'ZSDLFS', title: '征收代理方式',width:120}
			            ,{field: 'JSYJ', title: '计税依据',width:120}
			            ,{field: 'KSSL', title: '课税数量',width:120}
			            ,{field: 'SL', title: '税率',width:120}
			            ,{field: 'SJJE', title: '实缴金额',width:120}
			            ,{field: 'SKSSSWJG', title: '税款所属税务机关',width:120}
			            ,{field: 'ZGSWS', title: '主管税务所',width:120}
			            ,{field: 'YSKMDM', title: '预算科目代码',width:120}
			            ,{field: 'YSKM', title: '预算科目',width:120}
			            ,{field: 'YSFPBL', title: '预算分配比例',width:120}
			            ,{field: 'ZYJBL', title: '中央级比例',width:120}
			            ,{field: 'SSJBL', title: '省市级比例',width:120}
			            ,{field: 'DSJBL', title: '地市级比例',width:120}
			            ,{field: 'QXJBL', title: '区县级比例',width:120}
			            ,{field: 'XZJBL', title: '乡镇级比例',width:120}
			            ,{field: 'ZYDFPBL', title: '中央待分配比例',width:120}
			            ,{field: 'DSDFPBL', title: '地市待分配比例',width:120}
			            ,{field: 'SJDFPBL', title: '省局待分配比例',width:120}
			            ,{field: 'SKGK', title: '收款国库',width:120}
			            ,{field: 'KJRQ', title: '开具日期',width:120}
			            ,{field: 'SJRQ', title: '上解日期',width:120}
			            ,{field: 'SJXHRQ', title: '上解销号日期',width:120}
			            ,{field: 'SJXHR', title: '上解销号人',width:120}
			            ,{field: 'RKXHRQ', title: '入库销号日期',width:120}
			            ,{field: 'RKXHR', title: '入库销号人',width:120}
			            ,{field: 'PZZL', title: '票证种类',width:120}
			            ,{field: 'PZZG', title: '票证字轨',width:120}
			            ,{field: 'PZHM', title: '票证号码',width:120}
			            ,{field: 'SKZL', title: '税款种类',width:120}
			            ,{field: 'SKSX', title: '税款属性',width:120}
			            ,{field: 'JDXZ', title: '街道乡镇',width:120}
			            ,{field: 'YHHB', title: '银行行别',width:120}
			            ,{field: 'YHYYWD', title: '银行营业网点',width:120}
			            ,{field: 'YHZH', title: '银行账号',width:120}
			            ,{field: 'HZRQ', title: '汇总日期',width:120}
			            ,{field: 'HZR', title: '汇总人',width:120}
			            ,{field: 'HZPZZL', title: '汇总票证种类',width:120}
			            ,{field: 'HZPZZG', title: '汇总票证字轨',width:120}
			            ,{field: 'HZPZHM', title: '汇总票证号码',width:120}
			            ,{field: 'TZLX', title: '调账类型',width:120}
			            ,{field: 'JKQX', title: '缴款期限',width:120}
			            ,{field: 'ZSSWJG', title: '征收税务机关',width:120}
			            ,{field: 'DZSPHM', title: '电子税票号码',width:120}
			            ,{field: 'ZYJ', title: '中央级',width:120}
			            ,{field: 'SSJ', title: '省市级',width:120}
			            ,{field: 'DSJ', title: '地市级',width:120}
			            ,{field: 'QXJ', title: '区县级',width:120}
			            ,{field: 'XZJ', title: '乡镇级',width:120}
			            ,{field: 'ZYDFP', title: '中央待分配',width:120}
			            ,{field: 'SJDFP', title: '省级待分配',width:120}
			            ,{field: 'DSDFP', title: '地市待分配',width:120}
			            ,{field: 'DJXH', title: '登记序号',width:120}
			            ,{field: 'RK_RQ', title: '导入日期',width:120}
			          
			          ]]
			        ,done : function(res, curr, count){
		      		 	layer.close(index);
		        	}
			        });
			        
			      });
				}
			} else if(tableName == "xwcs_gsdr_yssjgs") {
				if ($("#fz").val()!="") {
					layui.use('table', function(){
				        var table = layui.table;
				        var form = $("#form1").serializeArray();
				    	for(var i in form){
				      		var obj = form[i];
				      		if (obj.value == "") {
								layer.msg("请选择税款所属年月!")
								return;
							}
				      	}
				    	var index = layer.load(2);
				        //第一个实例
				        table.render({
				          elem: '#cxzssj'
				          ,height: 500
				          ,width : document.body.clientWidth
				          /* ,data: data //数据 */
				          ,method : 'post'
				          ,url:"grsdsyssjcx/querySdbg.do"
				          ,cellMinWidth : 150
				          ,where: {
				        	  drrq:$("#drrq").val(),
				        	  fz:$("#fz").val(),
				        	    form:JSON.stringify(form)
					       }
				          ,limit:10
				          ,page: true //开启分页
				          ,cols: [[ //表头
							
							{field:val,title:name}
							,{field: 'yzsje', title: '应征税金额',fixed:'right'}
				          ]]
				        ,done : function(res, curr, count){
			      		 	layer.close(index);
			        	}
				      });
				        
				     });
					
				}else {
				
				layui.use('table', function(){
			     
			        var table = layui.table;
			        var form = $("#form1").serializeArray();
			    	for(var i in form){
			      		var obj = form[i];
			      		if (obj.value == "") {
							layer.msg("请选择税款所属年月!")
							return;
						}
			      	}
			     	
			     	
			    	var index = layer.load(2);
			        //第一个实例
			        table.render({
			          elem: '#cxzssj'
			          ,height: 500
			          ,width:document.body.clientWidth//设置宽度为body宽度
			          /* ,data: data //数据 */
			          ,method : 'post'
			          ,url:"grsdsyssjcx/querySdbg.do"
			          ,cellMinWidth: 120
			          ,where: {
			        	  drrq:$("#drrq").val(),
			        	  fz:$("#fz").val(),
			        	    form:JSON.stringify(form)
				       }
			          ,limit:10
			          ,page: true //开启分页
			          ,cols: [[ //表头
			        	{field: 'NSRSBH', title: '纳税人识别号',fixed: 'left',width:120}
			            ,{field: 'NSRMC', title: '纳税人姓名', fixed: 'left',width:120}
			            ,{field: 'SFZJLX', title: '身份证件类型',width:120}
			            ,{field: 'SFZJHM', title: '身份证件号码',width:120}
			            ,{field: 'DJZCLX', title: '登记注册类型',width:120}
			            ,{field: 'ZSXM', title: '征收项目',width:120}
			            ,{field: 'ZXPM', title: '征收品目',width:120}
			            ,{field: 'YZSJE', title: '应征税金额',fixed:'right',width:120}
			            ,{field: 'SKZT', title: '税款状态',width:120}
			            ,{field: 'SBRQ', title: '申报日期',width:120}
			            ,{field: 'KJRQ', title: '开具日期',width:120}
			            ,{field: 'SJRQ', title: '上解日期',width:120}
			            ,{field: 'RKRQ', title: '入库日期',width:120}
			            ,{field: 'SKSSQQ', title: '税款所属期起',width:120}
			            ,{field: 'SKSSQZ', title: '税款所属期止',width:120}
			            ,{field: 'SKSX', title: '税款属性',width:120}
			            ,{field: 'SKJNFS', title: '税款缴纳方式',width:120}
			            ,{field: 'YSKE', title: '预算科目',width:120}
			            ,{field: 'YSFPBL', title: '预算分配比例',width:120}
			            ,{field: 'SKGK', title: '收款国库',width:120}
			            ,{field: 'PZZL', title: '票证种类',width:120}
			            ,{field: 'KPR', title: '开票人',width:120}
			            ,{field: 'ZSJG', title: '征收机关',width:120}
			            ,{field: 'ZGSWS', title: '主管税务所',width:120}
			            ,{field: 'JDXZ', title: '街道乡镇',width:120}
			            ,{field: 'SKSSSWJG', title: '税款所属税务机关',width:120}
			            ,{field: 'YZPZZL', title: '应征凭证种类',width:120}
			            ,{field: 'XJHZPZHM', title: '现金汇总凭证号码',width:120}
			            ,{field: 'RK_RQ', title: '导入日期',width:120}
			          ]]
			        ,done : function(res, curr, count){
		      		 	layer.close(index);
		        	}
			        });
			        
			      });
				}
			}
			
			function showSuccess() {
				 layer.msg('查询成功！',{time: 1000,offset: 'auto'});
			}
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
				url : "ajax.do?ctrl=jrkyssjcx_addmb",
				data : {
					"maplist" : JSON.stringify(maplist),
					"jdxz" : document.getElementById("jdxz").value,
					"mbmc" : $("#mbmc").val()
				},
				type : 'post',
				success : function(obj) {
					
					layer.close(index);
					if (obj != null && obj.code == "000") {
						layer.msg("模板保存成功！");		
					}
				}
			});
			
		});
				
		
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
				url : "ajax.do?ctrl=jrkyssjcx_getjd",
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
		


</script>

</html>