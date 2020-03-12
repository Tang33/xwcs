<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	java.util.Properties prop = new java.util.Properties();
	java.io.InputStream in;
	in = getClass().getResourceAsStream("/conf/jdbc.properties");
	prop.load(in);
	String value = (String) prop.get("ip");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>主页</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />

<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<!-- 引入 ECharts 文件 -->
<script src="js/echarts.min.js"></script>
<!-- <script src="js/bj/jquery.js"></script> -->
<script src="js/bj/jquery.SuperSlide.2.1.1.js"></script>
<link href="js/bj/style.css" rel="stylesheet" type="text/css"/>
<link href="css/data_text.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
#bzt0 {
	padding:20px;
	float: left;
	width: 45%;
	height: 360px;
	margin-left: 0;
	margin-top: 10px;
	background-image: url("static/img/bj/bzt.png");
	background-repeat: no-repeat;
}

#bzt1 {
	padding:20px;
	float: left;
	width: 26%;
	height: 360px;
	margin-left: 1%;
	margin-top: 10px;
	background-image: url("static/img/bj/bzt.png");
	background-repeat: no-repeat;
}

#bzt2 {
	padding:20px;
	float: left;
	width: 26%;
	height: 360px;
	margin-left: 1%;
	margin-top: 10px;
	background-image: url("static/img/bj/bzt.png");
	background-repeat: no-repeat;
}

.set-content{

	width: 101%;
}
.dd {
    
       width: 0;
    height: 0;
    border: 15px solid transparent;
    border-bottom-color: #2A97E9;
    position: absolute;
    content: '';
    margin-left: 24px;
    margin-top: 12px;

}

</style>
</head>

<body style="background-color: #F5F5F5;overflow-x: hidden">
	<div style="margin-top: 40px;">
		<div style="width: 100%;">
			<div id="bzt0"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="main1" style="width: 95%; height:300px;"></div>
			</div>
			<div id="bzt1"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="main" style="width: 100%; height: 300px;"></div>
			</div>
			<div id="bzt2"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="main0" style="width: 100%; height: 300px;"></div>
			</div>

		</div>
	</div>
	
	

	<div style="width: 32%; margin-left: 0.5%;float:left;position: relative;top:-15px;background-image: url('static/img/bj/ggbg.png');background-repeat: no-repeat;margin-top: 30px;">
		<div class="set-content">
            <div style="height: 40px;"></div>
             
            <div class="set-title hd " >
            <div  class=" dd" >
            
            </div >
         
                <ul class="clearfix" style="width: 97%;">
                 
                    <li class="on" style="width: 90%;border-bottom:1px solid #2A97E9;font-size: 16px;margin-left: 5%;border-right: 0px;color:#000000;">  
                  		 一般公共预算收入前二十（万元）</li>
                </ul>
            </div>
            <div class="set-middle bd" style="height:300px;">
                <ul class="set-middle-item">
                    <li>
                       <table class="layui-table" id="table" lay-filter="user" style="width: 94%">
							<thead>
								<tr>
								
									<th lay-data="{field:'NSRMC'}">名称</th>
									<th lay-data="{field:'DNLJJE'}" style="text-align:center">当年累计</th>
									<!-- <th lay-data="{field:'QNTQLJJE'}">去年累计</th> -->
									<th lay-data="{field:'ZF'}" style="text-align:center">增幅</th>
									<!-- <th lay-data="{field:'ZZBL'}">增长比例</th> -->
								</tr>
							</thead>
							<tbody id="ttbody">
				
							</tbody>
						</table>
                    </li>
                </ul>
            </div>
        </div>
	</div>
	
	
	
	<div style="width: 32%; margin-left: 1.5%;float:left;position: relative;top:-15px;background-image: url('static/img/bj/ggbg.png');background-repeat: no-repeat;margin-top: 30px;">
		<div class="set-content">
            <div style="height: 40px;"></div>
            <div class="set-title hd">
             <div  class=" dd" >
            
            </div >
                <ul class="clearfix" style="width: 97%;">
                    <li class="on"  style="width: 90%;border-bottom:1px solid #2A97E9;font-size: 16px;margin-left: 5%;border-right: 0px;color:	#000000;">一般公共预算收入增幅前二十（万元）</li>
                </ul>
            </div>
            <div class="set-middle bd" style="height:300px;">
               
                <ul class="set-middle-item">
                    <li>
                        <table class="layui-table" id="table2" lay-filter="user2" style="width: 94%">
							<thead>
								<tr>

									<th lay-data="{field:'NSRMC'}">名称</th>
									<!-- <th lay-data="{field:'NSRSBH'}">纳税人识别号</th> -->
									<th lay-data="{field:'DNLJJE'}" style="text-align:center">当年累计</th>
									<!-- <th lay-data="{field:'QNTQLJJE'}">去年同期累计金额</th> -->
									<th lay-data="{field:'ZF'}" style="text-align:center">增幅</th>
									<!-- <th lay-data="{field:'ZZBL'}">增长比例</th> -->

								</tr>
							</thead>
							<tbody id="ttbody2">
				
							</tbody>
						</table>
                    </li>
                </ul>
                
            </div>
        </div>
	</div>
	
	
	
	<div style="width: 32%; margin-left: 1%;float:left;position: relative;top:-15px;background-image: url('static/img/bj/ggbg.png');background-repeat: no-repeat;margin-top: 30px;">
		<div class="set-content">
            <div style="height: 40px;"></div>
            <div class="set-title hd">
             <div  class=" dd" >
            
            </div >
                <ul class="clearfix" style="width: 97%;">
                    <li class="on"  style="width: 90%;border-bottom:1px solid #2A97E9;font-size: 16px;margin-left: 5%;border-right: 0px;color:	#000000;">一般公共预算收入减幅前二十（万元）</li>
                </ul>
            </div>
            <div class="set-middle bd" style="height:300px;">
                <ul class="set-middle-item">
                    <li>
                        <table class="layui-table" id="table3" lay-filter="user3" style="width: 94%">
							<thead>
								<tr>

									<th lay-data="{field:'NSRMC'}">名称</th>
									<!-- <th lay-data="{field:'NSRSBH'}">纳税人识别号</th> -->
									<th lay-data="{field:'DNLJJE'}" style="text-align:center">当年累计</th>
									<!-- <th lay-data="{field:'QNTQLJJE'}">去年同期累计金额</th> -->
									<th lay-data="{field:'ZF'}" style="text-align:center">增幅</th>
									<!-- <th lay-data="{field:'ZZBL'}">增长比例</th> -->

								</tr>
							</thead>
							<tbody id="ttbody3">
				
							</tbody>
						</table>
                    </li>
                </ul>
                
            </div>
        </div>
	</div>
<!-- <script>
            jQuery(".set-content").slide({autoPlay:false,trigger:"click",delayTime:700,pnLoop:false});
</script> -->
	<script type="text/javascript">
	
		jQuery(".set-content").slide({autoPlay:false,trigger:"click",delayTime:700,pnLoop:false});
	
		layui.use([ 'form', 'layedit', 'laydate', 'laypage' ], function() {
			var form = layui.form;
			//获取纳税总户数的信息
// 			nszhs();
// 			xznsr();
			tbsj();
			tbsj1();
// 			tbsj1();
			hyzbbt();
	
		});
	
		$(function() {
			//initdw();
			getData();
			getData2();
			getData3();
		});
	
	
		function nszhs() {
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "show/nszhsnum.do",
				data : "",
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
	
						$("#nszhs").append("<tr><td>" + obj.data[0].YEAR + "年纳税总户数</td><td>" + obj.data[1].YEAR + "年纳税总户数</td></tr>");
						$("#nszhs").append("<tr><td>" + obj.data[0].YEAR + "年</td><td>" + obj.data[0].NUM + "</td></tr>");
						$("#nszhs").append("<tr><td>" + obj.data[1].YEAR + "年</td><td>" + obj.data[1].NUM + "</td></tr>");
	
					}
	
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
	
	
	
		function getData() {
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "show/ssTop.do",
				data : "",
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						getTbale(obj.data); //拼接表格
	
					}
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
		function getData2() {
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "show/ssZFTop.do",
				data : "",
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						getTbale2(obj.data); //拼接表格
	
					}
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
		function getData3() {
			$.ajax({
				type : "post", //请求方式
				async : false, //是否异步
				url : "show/ssJFTop.do",
				data : "",
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.code == '000') {
						getTbale3(obj.data); //拼接表格
	
					}
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
	
		function getTbale(data) {
			var s = "";
			$.each(data, function(v, o) {
				console.log(v+1)
				 

				if (v< 3) {
					s += '<tr>';
					s += '<td>' + (o.NSRMC == undefined ? "-" : o.NSRMC)
						+ '</td>';
					/* s += '<td>' + (o.NSRSBH == undefined ? "-" : o.NSRSBH) + '</td>'; */
					s += '<td style="text-align:right">' + (o.DNLJJE == undefined ? "-" : (o.DNLJJE/10000).toFixed(2)) + '</td>';
					/* s += '<td>' + (o.QNTQLJJE == undefined ? "-" : o.QNTQLJJE) + '</td>'; */

					s += '<td style="text-align:right">' + (o.ZF == undefined ? "-" : (o.ZF/10000).toFixed(2)) + '</td></tr>';
				}
	
				if (v== 2) {
	

					s += '<tr id="motrfist1"><td id="topmore1" colspan="7" style="text-align: center; cursor: pointer;">更多</td></tr>';

				}

	
	
			});
			$("#ttbody").html(s);
			//执行渲染
			/*  layui.use([ 'table' ], function() {
				layui.table.init('user', {
					limit : 20
				//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
				//支持所有基础参数
				});
			});  */
	
		}
	
		function getTbale2(data) {
			var s = "";
			$.each(data, function(v, o) {
	
				if (v < 3) {
	
					s += '<tr>';
					s += '<td>' + (o.NSRMC == undefined ? "-" : o.NSRMC)
						+ '</td>';
					s += '<td style="text-align:right">' + (o.DNLJJE == undefined ? "-" : (o.DNLJJE/10000).toFixed(2)) + '</td>';
					s += '<td style="text-align:right">' + (o.ZF == undefined ? "-" : (o.ZF/10000).toFixed(2)) + '</td></tr>';
				}
	
				if (v == 2) {
					s += '<tr id="motrfist2"><td id="topmore2" colspan="7" style="text-align: center; cursor: pointer;">更多</td></tr>';
				}
	
	
			});
			$("#ttbody2").html(s);
			/* //执行渲染
			layui.use([ 'table' ], function() {
				layui.table.init('user2', {
					height : auto //设置高度
					,
					limit : 20
				//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
				//支持所有基础参数
				});
			}); */
	
		}
	
		function getTbale3(data) {
			var s = "";
			$.each(data, function(v, o) {
	
				if (v < 3) {
	
					s += '<tr>';
					s += '<td>' + (o.NSRMC == undefined ? "-" : o.NSRMC)
						+ '</td>';
					s += '<td style="text-align:right">' + (o.DNLJJE == undefined ? "-" : (o.DNLJJE/10000).toFixed(2)) + '</td>';
					s += '<td style="text-align:right">' + (o.ZF == undefined ? "-" : (o.ZF/10000).toFixed(2)) + '</td></tr>';
				}
	
				if (v == 2) {

					s += '<tr id="motrfist3"><td id="topmore3" colspan="7" style="text-align: center; cursor: pointer;">更多</td></tr>'

				}
			});
			$("#ttbody3").html(s);
			//执行渲染
			/* layui.use([ 'table' ], function() {
				layui.table.init('user3', {
					//height : 480 //设置高度
					height : auto //设置高度
					,
					limit : 20
				//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
				//支持所有基础参数
				});
			}); */
	
		}
	
		// 纳税人
		function xznsr() {
			ajax({
				url : "show/xznsrQuery.do",
				type : 'post',
				success : function(obj) {
					$("#xznsr").html("新增纳税人-" + obj.data[0].RKRQ);
					$("#nsrrs").html(obj.data[0].SL);
				}
			});
		}
	
		// 图表
		function tbsj() {
			ajax({
				url : "show/szzb.do",
				type : 'post',
				success : function(obj) {
					tbsc(obj.data);
				}
			});
		}
	
	
		// 图表
		function hyzbbt() {
			ajax({
				url : "show/hyzb.do",
				type : 'post',
				success : function(obj) {
					hytbsc(obj.data);
				}
			});
		}
	
		function tbsj1() {
			ajax({
				url : "show/monthTotal.do",
				type : 'post',
				success : function(obj) {
					var data = obj.data;
					var list = [];
					var result = {};
					var arr = [];
					var year;
					var yearx;
					for (var i = 0; i < data.length; i++) {
						year = data[0].year;
						if (year != data[i].year) {
							yearx = data[i].year;
						}
					}
	
					result.name = year;
					for (var i = 0; i < data.length; i++) {
						if (year == data[i].year) {
							arr.push(data[i].se);
						}
					}
					result.type = "line";
					result.stack = '总量', result.data = arr;
					list.push(result);
					var arr1 = [];
					var result1 = {};
					result1.name = yearx;
					for (var i = 0; i < data.length; i++) {
						if (yearx == data[i].year) {
							arr1.push(data[i].se);
						}
					}
					result1.type = "line";
					result1.stack = '总量', result1.data = arr1;
					list.push(result1);
					console.log(list);
					tbsc1(list);
				}
			});
		}
	
		function tbsc1(optb) {
			var myChart = echarts.init(document.getElementById('main1'));
			var arrlist = optb;
			console.log("---------");
			console.log(optb);
			console.log(arrlist);
			var option = {
				title : {
				     text : '一般公共预算收入分月情况(单位：万元)',
				     textStyle: {
		                         	fontSize: 16
				                 }
			    },
				tooltip : {
					trigger : 'axis'
				},
				color : ["#2A97E9", "rgb(194,53,49)"],
				legend : {
					data : [optb[1].name,optb[0].name]
				},
				grid : {
					left : '3%',
					right : '4%',
					bottom : '3%',
					containLabel : true
				},
				toolbox : {
					feature : {
						saveAsImage : {}
					}
				},
				xAxis : {
					type : 'category',
					boundaryGap : false,
					data : [ '1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月',
						'9月', '10月', '11月', '12月' ]
				},
				yAxis : {
					type : 'value'
				},
				series : [
					{
						 name: optb[1].name,
	                     type: 'line',
	                     data: optb[1].data
					},
					{
						 name: optb[0].name,
	                     type: 'line',
	                     data: optb[0].data
					}	
				]
			};
	
			myChart.setOption(option);
		}
	
		function tbsc(optb) {
			var myChart = echarts.init(document.getElementById('main'));
			var arrlist = optb;
	
			var option = {
				title : {
					text : '一般公共预算按税种占比情况(单位：万元)',
					subtext : '玄武区财政',
					x : 'left',
					textStyle: {
                     	fontSize: 16
	                 }
				},
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b} : {c} ({d}%)"
				},
				legend : {
					x : 'center',
					y : 'bottom',
					data : arrlist
				},
				toolbox : {
					show : true,
					feature : {
						mark : {
							show : true
						},
						dataView : {
							show : true,
							readOnly : false
						},
						magicType : {
							show : true,
							/* type : [ 'pie', 'funnel' ] */
							type : [ 'pie', 'funnel' ]
						},
						restore : {
							show : true
						},
						saveAsImage : {
							show : true
						}
					}
				},
				calculable : true,
				series : [ {
					name : '面积模式',
					type : 'pie',
					/* radius : [ 30, 110 ],
					center : [ '30%', '45%' ],
					roseType : 'area',*/
					radius : '55%',
					center : [ '40%', '45%' ],
					label: {
	                    formatter: '{b}: ({d}%)'
	                },
	
					data : arrlist
				} ]
			};
	
			myChart.setOption(option);
		}
	
	
		function hytbsc(optb) {
			var myChart = echarts.init(document.getElementById('main0'));
			var arrlist = optb;
	
			var option = {
				title : {
					text : '一般公共预算按行业占比情况(单位：万元)',
					subtext : '玄武区财政',
					x : 'left',
					textStyle: {
                     	fontSize: 16
	                 }
				},
				tooltip : {
					trigger : 'item',
					formatter : "{a} <br/>{b} : {c} ({d}%)"
				},
				legend : {
					x : 'center',
					y : 'bottom',
					data : arrlist
				},
				/* color:['red', 'green','yellow','blueviolet'], */
				toolbox : {
					show : true,
					feature : {
						mark : {
							show : true
						},
						dataView : {
							show : true,
							readOnly : false
						},
						magicType : {
							show : true,
							type : [ 'pie' ]
						},
						restore : {
							show : true
						},
						saveAsImage : {
							show : true
						}
					}
				},
				calculable : true,
				series : [ {
					name : '面积模式',
					type : 'pie',
					radius : '55%',
					center : [ '40%', '45%' ],
					/* radius : [ 30, 110 ],
					center : [ '30%', '45%' ], */
					/* roseType : 'area', */
					label: {
	                    formatter: '{b}: ({d}%)'
	                },
					data : arrlist
				} ]
			};
	
			myChart.setOption(option);
		}
	
		//点击更多，展示不同的内容
		$(document).on("click", "#topmore1", function() {

			
			//添加一个新的页面、
			layer.open({
		           type: 2,
		           shadeClose: true,
		           shade: 0.8,
		           maxmin: true,
		           area: ['70%', '70%'],
		           content: '${pageContext.request.contextPath}/jsp/topmore1.jsp',
		           success: function(layero, index){  
		              var body = layer.getChildFrame('body', index);  
		               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
		               var inputList = body.find("input"); 
		               for (var i = 0; i < inputList.length; i++ ) {  
		            		   $(inputList[i]).val(editList[i]);  
		               }    
		           }   
		 });

	
	
		})

		
		
		$(document).on("click", "#bzt0", function() {
	
			//添加一个新的页面、
			layer.open({
		           type: 2,
		           shadeClose: true,
		           shade: 0.8,
		           maxmin: true,
		           area: ['70%', '70%'],
		           content: '${pageContext.request.contextPath}/jsp/ssTop.jsp',
		           success: function(layero, index){  
		              var body = layer.getChildFrame('body', index);  
		               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
		               var inputList = body.find("input"); 
		               for (var i = 0; i < inputList.length; i++ ) {  
		            		   $(inputList[i]).val(editList[i]);  
		               }    
		           }   
		 }); 
	
	
		})
		
		$(document).on("click", "#bzt1", function() {
	
			//添加一个新的页面、
			layer.open({
		           type: 2,
		           shadeClose: true,
		           shade: 0.8,
		           maxmin: true,
		           area: ['70%', '70%'],
		           content: '${pageContext.request.contextPath}/jsp/ssZFTop.jsp',
		           success: function(layero, index){  
		              var body = layer.getChildFrame('body', index);  
		               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
		               var inputList = body.find("input"); 
		               for (var i = 0; i < inputList.length; i++ ) {  
		            		   $(inputList[i]).val(editList[i]);  
		               }    
		           }   
		 }); 

	
	
		})

		
		
		$(document).on("click", "#bzt2", function() {
	
			//添加一个新的页面、
			layer.open({
		           type: 2,
		           shadeClose: true,
		           shade: 0.8,
		           maxmin: true,
		           area: ['70%', '70%'],
		           content: '${pageContext.request.contextPath}/jsp/ssJFTop.jsp',
		           success: function(layero, index){  
		              var body = layer.getChildFrame('body', index);  
		               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
		               var inputList = body.find("input"); 
		               for (var i = 0; i < inputList.length; i++ ) {  
		            		   $(inputList[i]).val(editList[i]);  
		               }    
		           }   
		 }); 
	
	

		})

		
		
		$(document).on("click", "#topmore2", function() {
	
			
			//添加一个新的页面、
			layer.open({
		           type: 2,
		           shadeClose: true,
		           shade: 0.8,
		           maxmin: true,
		           area: ['70%', '70%'],
		           content: '${pageContext.request.contextPath}/jsp/topmore2.jsp',
		           success: function(layero, index){  
		              var body = layer.getChildFrame('body', index);  
		               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
		               var inputList = body.find("input"); 
		               for (var i = 0; i < inputList.length; i++ ) {  
		            		   $(inputList[i]).val(editList[i]);  
		               }    
		           }   
		 });

	
		})

	
	
		$(document).on("click", "#topmore3", function() {
	
			
			//添加一个新的页面、
			layer.open({
		           type: 2,
		           shadeClose: true,
		           shade: 0.8,
		           maxmin: true,
		           area: ['70%', '70%'],
		           content: '${pageContext.request.contextPath}/jsp/topmore3.jsp',
		           success: function(layero, index){  
		              var body = layer.getChildFrame('body', index);  
		               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
		               var inputList = body.find("input"); 
		               for (var i = 0; i < inputList.length; i++ ) {  
		            		   $(inputList[i]).val(editList[i]);  
		               }    
		           }   
		 });
			
		})
	
	

	</script>


</body>
</html>