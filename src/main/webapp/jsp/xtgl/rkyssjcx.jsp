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
<blockquote class="layui-elem-quote layui-text">本功能页面用于对入库数据（原始数据）的自定义条件查询并可导出查询结果！</blockquote>
		<input type="hidden" id="dwid" value="${dwid}" />
	<form class="layui-form" id="form1" action="">
		<div class="layui-form" lay-filter="test1">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					
					<div class="layui-colla-content  layui-show" style="line-height: 53px;margin: 0px auto;">
						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
								
										<div style="width: 22%;float: left;height: 38px;">
											<label class="layui-form-label" style="width: 40%; padding: 9px 0px 9px 0px"><span style="color:red;"> * </span> 税款所属年月：</label>
											<div class="layui-input-inline" style="width: 60%;">
								  				<input style="width: 100%;" type="text" class="layui-input" id="drrq" name="drrq" placeholder="请选择日期">
											</div>
										</div>
									
									
										<div style="width: 28%;float: left;height: 38px;margin-left: 2%">
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
										
										<div style="width: 28%;float: left;height: 38px;margin-left: 11%">
											<label class="layui-form-label" style="width: 20%; padding: 9px 0px 9px 0px">汇总条件：</label>
											<div class="layui-input-inline" style="width: 80%;">
												<select lay-filter="fz" id="fz"  style="height: 38px;width: 100%;" lay-search="">
													
												</select>	
											</div>
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
											<button class="layui-btn" id ="button11"
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
										<div id="selectname" style="width: 100%;">
										</div>
									
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
	</form>
	
</body>

<script>
var v = 0;
var wait;
var map = {};
var maplist = {};
var dwid=$("#dwid").val();
var fz="";
	var showlayer;
	var index;
	$(function() {
		$("#table").attr("lay-data","{width:" + document.body.clientWidth + "}");
		initfzzd();
	});
	
	function initfzzd() {
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url:"ajax.do?ctrl=rkyssjcx_queryfz",
			data: { 
		    },
			dataType : "json",
			success : function(obj) {
				if (obj!=null&&obj.code=="000") {
					var array = obj.data;
					var str="<option value=''>请选择</option>";
					for (var i = 0; i < array.length; i++) {
						str +='<option value="'+ array[i].ZDM+'">'+array[i].MBM+'</option>';						
					}
					$("#fz").html(str);
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
	
	//查询数据库的最大月份
	getMaxData();
	function getMaxData(){	
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url:"ajax.do?ctrl=Sjdr_getNsDate",
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
	
	//根据条件导出数据-------------
	$("#selectfz").click(function() {
		 var form = $("#form1").serializeArray();
	    	for(var i in form){
	      		var obj = form[i];
	      		if (obj.value == "") {
					layer.msg("请选择税款所属年月!")
					return;
				}
	      	}
	    	window.open(encodeURI("outSdbg.do?drrq="+$("#drrq").val()+"&fz="+$("#fz").val()+"&form="+JSON.stringify(form)));
		/* $.ajax({
			url:"outSdbg.do",
			type:"post",
			data:{
				 drrq:$("#drrq").val(),
	        	  fz:$("#fz").val(),
	        	form:JSON.stringify(form)
			},
             dataType: 'json'
		}); */
	});
	
	
	
	
	//根据条件查询数据-------------
	$("#chaxun").click(function() {
			var index = document.getElementById("fz").selectedIndex;
			var name = document.getElementById("fz").options[index].text
			var val = $("#fz").val();
			val = val.toUpperCase()//转换成大写
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
			          ,url:"ajax.do?ctrl=rkyssjcx_querySdbg"
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
	          ,url:"ajax.do?ctrl=rkyssjcx_querySdbg"
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

		function showSuccess() {
			 layer.msg('查询成功！',{time: 1000,offset: 'auto'});
		}
	});
	
	
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
	var formSelects;
	
	layui.config({base : 'static/js/'}).extend({formSelects : 'formSelects-v3'}).use([ 'form', 'formSelects' ],
			function() {
		formSelects = layui.formSelects;

	});
				
				

	//设置下拉框的东西
	layui.use(['form', 'layedit', 'laydate','laypage'], function() {
		var form = layui.form,
			layer = layui.layer,
			layedit = layui.layedit,
			laydate = layui.laydate,
			laypage=layui.laypage;
		laydate.render({
			elem: '#drrq',
			/* format: 'yyyyMM' */ //可任意组合
			type: 'month' 
		});
		
		
		
		//调用方法
		queryList();
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
			if(text=="街道乡镇" || text=="征收项目" ||text=="征收品目" ||text=="行业大类" ||text=="行业中类" ||text=="行业门类" ||text=="行业" ||text=="登记注册类型" ){
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
								$.ajax({
									type : "post", //请求方式
									async : true, //是否异步
									url:"ajax.do?ctrl=rkyssjcx_selectSZ",
									data: { 
								        "xz": xz
								    },
									dataType : "json",
									success : function(obj) {
										var array = obj.data;
										console.log("=------------");
										console.log(obj.data);
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
					 
					 
				}else if(text=="登记注册类型"||text=="预算分配比例"||text=="税款种类"||text=="总分机构类型"||text=="跨地区税收转移企业类型"||text=="应征凭证种类"||text=="汇总票证种类"||text=="调账类型"||text=="银行行别"||text=="票证种类" ||text=="征收代理方式"){
					pd = 2;
					m=m-1;
					var seleall='';
					var options="<option value=''>请选择</option>";
					console.log('---------------------------xz:'+xz)
					//var options1="<option value=''>请选择</option>";
					var str = "";
					//调用接口进行查询
					$.ajax({
						type : "post", //请求方式
						async : true, //是否异步
						url:"ajax.do?ctrl=rkyssjcx_queryzd",
						data: { 
					        "xz": xz
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
						var x = 1;
						$.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							url:"ajax.do?ctrl=rkyssjcx_selectislike",
							data: { 
						        "xz": xz
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
						$.ajax({
							type : "post", //请求方式
							async : true, //是否异步
							url:"ajax.do?ctrl=rkyssjcx_selectislike",
							data: { 
						        "xz": xz
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
	
	function queryList(){
		$.ajax({
			type : "post",
			async : true,
			url : "ajax.do?ctrl=rkyssjcx_selectList",
			data : "",
			dataType : "json",
			success : function(obj){
				var data = obj.data;
				var options="<option style='width:100px;' value=''>请选择</option>";
				
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

	/* var pageNo = 1;
	var pageSize = 10;
	var count = 0;
	function getData() {
		wait = layer.load();
		ajax({
			url : "ajax.do?ctrl=rkyssjcx_queryData&pageNo=" + pageNo+ "&pageSize=" + pageSize,
			data : {
				date : $("#yearNmonth").val(),
				nsName : $("#nsName").val(),
			},
			type : 'post',
			dataType : "Json",
			success : function(obj) {
				layer.close(wait);
				if(obj.code=="001"){
					layer.msg(obj.msg);
				}else if (obj != null && obj.data != null) {
					getTbale(obj.data);//拼接表格
					count = obj.count;//数据总条数					
					queryPage();
					console.log(obj.data);
					showSuccess();
				}
			}
		});
	} */

	//初始化表格
	function getTbale(data) {
		var s = "";
		$.each(data,function(v,o) {
			
		/* 	[{QYSDS_GS=154.86, CCS1=0, HJ=154.86, JYFJ1=0, CSWHJSS=0, JD_MC=新街口,
				CZTDSYS1=0, ZZS=0, DFJYFJ=0, ZZS1=0, GRSDS=0, HBS=0, HBS1=0, YGZZZS1=0, FCS1=0, 
				QYSDS_GS1=0, JD_DM=91, NSRSBH= , QYSDS_DS1=0, RK_RQ= , HY_MC=null, YHS1=0, 
				QYSDS=154.86, QYSDS_DS=0, YHS=0, GRSDS1=0, CSWHJSS1=0, DFJYFJ1=0, HJ1=0,
				NSRMC=鑫叶同创机电设备（北京）有限公司, JYFJ=0, YYS=0, CCS=0, QYSDS1=0, CZTDSYS=0, YGZZZS=0, FCS=0, YYS1=0, DSGLM= }, */

			
			s += '<tr>';
			
			s += '<td>' + o.NSRSBH + '</td>';
			s += '<td>' + o.NSRMC + '</td>';
			s += '<td>' + o.ZSXMMC + '</td>';
			s += '<td>' + o.ZSPM + '</td>';
			
			s += '<td>' + o.ZSPMDM + '</td>';
			s += '<td>' + o.SKSHQQ + '</td>';
			s += '<td>' + o.SKSHQZ + '</td>';
			s += '<td>' + o.DJZCLX + '</td>';
			
			
			s += '<td>' + (o.SSGLY == undefined ? "-" : o.SSGLY) + '</td>';
			s += '<td>' + o.HYML + '</td>';
			s += '<td>' + o.HYDL + '</td>';			
			s += '<td>' + o.HYZL + '</td>'
			s += '<td>' + o.HY + '</td>';
			s += '<td>' + o.ZSDLFS + '</td>';
			s += '<td>' + o.JSYJ + '</td>';
			s += '<td>' + (o.KSSL == undefined ? "-" : o.KSSL) + '</td>';
			s += '<td>' + o.SL + '</td>';
			s += '<td>' + o.SJJE + '</td>';
			s += '<td>' + o.SKSSSWJG + '</td>';
			s += '<td>' + o.ZGSWS + '</td>';
			s += '<td>' + o.YSKMDM + '</td>';
			s += '<td>' + o.YSKM + '</td>';
			s += '<td>' + o.YSFPBL + '</td>';
			s += '<td>' + o.ZYJBL + '</td>';
			s += '<td>' + o.SSJBL + '</td>';
			s += '<td>' + o.DSJBL + '</td>';
			s += '<td>' + o.QXJBL + '</td>';
			s += '<td>' + o.XZJBL + '</td>';
			s += '<td>' + o.ZYDFPBL + '</td>';
			s += '<td>' + o.DSDFPBL + '</td>';
			s += '<td>' + o.SJDFPBL + '</td>';
			s += '<td>' + o.SKGK + '</td>';
			s += '<td>' + o.KJRQ + '</td>';
			s += '<td>' + o.SJRQ + '</td>';
			s += '<td>' + o.SJXHRQ + '</td>';
			
			s += '<td>' + o.SJXHR + '</td>';
			s += '<td>' + o.RKRQ + '</td>';
			s += '<td>' + o.RKXHRQ + '</td>';
			s += '<td>' + o.RKXHR + '</td>';
			s += '<td>' + o.PZZL + '</td>';
			s += '<td>' + (o.PZZG == undefined ? "-" : o.PZZG) + '</td>';
			s += '<td>' + (o.PZHM == undefined ? "-" : o.PZHM) + '</td>';
			s += '<td>' + o.SKZL + '</td>';
			s += '<td>' + o.SKSX + '</td>';
			s += '<td>' + o.JDXZ + '</td>';
			s += '<td>' + o.YHHB + '</td>';
			s += '<td>' + o.YHYYWD + '</td>';
			s += '<td>' + o.YHZH + '</td>';
			
			
			s += '<td>' + o.TZLX + '</td>';
			s += '<td>' + o.JKQX + '</td>';
			s += '<td>' + o.ZSSWJG + '</td>';
			s += '<td>' + o.DZSPHM + '</td>';
			s += '<td>' + o.ZYJ + '</td>';
			s += '<td>' + o.SSJ + '</td>';
			s += '<td>' + o.DSJ + '</td>';
			s += '<td>' + o.QXJ + '</td>';
			s += '<td>' + o.XZJ + '</td>';
			s += '<td>' + o.ZYDFP + '</td>';
			
			s += '<td>' + o.SJDFP + '</td>';
			s += '<td>' + o.DSDFP + '</td>';
			s += '<td>' + o.DJXH + '</td>';
			s += '<td>' + (o.ZFJGLX == undefined ? "-": o.ZFJGLX) + '</td>';
			s += '<td>' + (o.KDQSSZYQYLX == undefined ? "-" : o.KDQSSZYQYLX) + '</td>';
		
			
			s += '</tr>';
		});
		$("#ttbody").html(s);
		//执行渲染
		layui.use([ 'table' ], function() {
			var table=layui.table;
			table.init('user', {
				height : 520,
				limit : pageSize+2
			//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
			//支持所有基础参数
			});
			 hidecolumn();
			 showcolumn();
			var layer = layui.layer;
			console.log(layer);
			/* table.on('tool(user)', function(obj) {
				console.log(obj);
				var data = obj.data;
				console.log(data);
				var layEvent = obj.event;
				console.log(layEvent);
				if (layEvent == 'qyName') {
					showlayer = layer.open({
						type : 1,
						title : '去年同期数据',
						area : [ '50%', '80%' ],
						shadeClose : false, //点击遮罩关闭
						content : $('#choose'),
						success : function() {
							$("#pzzs").text(data.ZZS1+"\t 元");
							$("#pygzzzs").text(data.YGZZZS1+"\t 元");
							$("#pyys").text( data.YYS1+"\t 元");
							$("#pqysds").text(  data.QYSDS1+"\t 元");
							$("#pfcs").text( data.FCS1+"\t 元");
							$("#pyhs").text(  data.YHS1+"\t 元");
							$("#pccs").text( data.CCS1+"\t 元");
							$("#pcswhjss").text( data.CSWHJSS1+"\t 元");
							$("#pdfjyfj").text(  data.DFJYFJ1+"\t 元");
							$("#pjyfj").text(  data.JYFJ1+"\t 元");
							$("#pcztdsys").text( data.CZTDSYS1+"\t 元");
							$("#pzzs").text(  data.ZZS1+"\t 元");
							$("#phbs").text( data.HBS1+"\t 元");
							$("#phj").text( data.HJ1+"\t 元");
							layui.form.render();

						},
						cancel : function() {
							return;
						},
						end : function() {

						}
					});
				}

			}); */
		});
		
		if (data == null || data.length <= 0) {
			$("#page").hide();
		} else {
			$("#page").show();
		}
	}

	function queryPage() {
		layui.use([ 'laypage' ], function() {
			laypage = layui.laypage;
			laypage.render({
				//注意，这里的page1 是 ID，不用加 # 号
				elem : 'page',
				//数据总数，从服务端得到
				count : count,
				//每页显示条数
				limit : pageSize,
				//条数列表
				limits : [ 10, 20, 30, 50 ],
				layout : [ 'prev', 'page', 'next', 'skip', 'count', 'limit' ],
				curr : pageNo,
				jump : function(data, first) {
					//obj包含了当前分页的所有参数，比如：
					pageNo = data.curr;
					pageSize = data.limit;
					//首次不执行
					if (!first) {
						//do something
						getData();
					}
				}
			});
		});
	}

</script>

</html>