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

<title>税源情况分析</title>

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
<script src="js/echarts.min.js"></script>
<style type="text/css">

#bzt0 {
 padding:20px;
 width: 45%;
 height: 380px;
 margin-left: 0px;
 margin-top: 10px;
 background-image: url("static/img/bj/bzt.png");
 background-repeat: no-repeat;
 float:left;
}

#bzt1 {
 padding:20px;
 width: 45%;
 height: 380px;
 margin-left: 0px;
 margin-top: 10px;
 background-image: url("static/img/bj/bzt.png");
 background-repeat: no-repeat;
 float:left;
}

#bzt2 {
 padding:20px;
 width: 45%;
 height: 380px;
 margin-left: 0px;
 margin-top: 10px;
 background-image: url("static/img/bj/bzt.png");
 background-repeat: no-repeat;
 float:left;
}
#bzt3 {
 padding:20px;
 width: 45%;
 height: 380px;
 margin-left: 0px;
 margin-top: 10px;
 background-image: url("static/img/bj/bzt.png");
 background-repeat: no-repeat;
 float:left;
}

#bzt4 {
 padding:20px;
 width: 93%;
 height: 380px;
 margin-left: 0px;
 margin-top: 10px;
 background-image: url("static/img/bj/bzt.png");
 background-repeat: no-repeat;
 float:left;
}
</style>
</head>
<body style="background-color: #F5F5F5;overflow-x: hidden">
	
	<div style="margin-top: 40px;">
		<div style="width: 100%;">
			<!-- 税务总体情况 -->
			<div id="bzt0"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="swztqk" style="width: 95%; height:300px;"></div>
			</div>
			<!--按登记注册类型分析  -->
			<div id="bzt1"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="adjzclx" style="width: 100%; height: 300px;"></div>
			</div>
			<!-- 按行业类型分析 -->
			<div id="bzt2"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="ahylx" style="width: 100%; height: 300px;"></div>
			</div>

			<!-- 按街道类型分析 -->
			<div id="bzt3"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="ajdfx" style="width: 100%; height: 300px;"></div>
			</div>
			
			<!-- 按登记日期分析 -->
			<div id="bzt4"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="adjrqfx" style="width: 100%; height: 300px;"></div>
			</div>
			
		</div>
	</div>
</body>

	<script>
		layui.use([ 'form', 'layedit', 'laydate', 'laypage' ], function() {
			var form = layui.form;

			swztqk();
			swztqk1();
			swztqk2()
			swztqk3()
			swztqk4()
		});
	
	
		
		//按税务总体情况
		function swztqk() {
			$.ajax({
				url:"ajax.do?ctrl=syqkfx_querySwztqk",
				type:"post",
				data:{},
	            success: function(obj){
	            	if(obj != null && obj.code == "000"){
	            		var data = obj.data;
	            		makechart(data);
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
		}
		
	
		function makechart(list){
			var yarr = new Array();//数量
			for (var i = 0; i < list.length; i++) {
				yarr.push(list[i].NAME);
			}
			var arrlist = list;
			var myChart = echarts.init(document.getElementById("swztqk"), 'macarons');
			var option = {
					title : {
						text : '税务总体情况',
						x : 'center',
						textStyle: {
				              fontSize: 26,
				              fontWeight: 'bolder',
				              color: '#000080'
						},
					},
					 tooltip: {
					        trigger: 'item',
					        formatter: "{a} <br/>{b}: {c} ({d}%)"
					    },
					    legend: {
					        orient: 'vertical',
					        x: '50',
					        itemWidth: 30,   // 设置图例图形的宽
				            itemHeight: 22,  // 设置图例图形的高
					        data:arrlist
					    },
					    series: [
					        {
					            name:'税务总体情况',
					            type:'pie',
					            radius: '80%',
					            radius: ['50%', '70%'],
					            center: ['50%', '60%'],  // 设置饼状图位置，第一个百分数调水平位置，第二个百分数调垂直位置
					            avoidLabelOverlap: false,
					            label: {
					                normal: {
					                    show: false,
					                    position: 'center'
					                },
					                emphasis: {
					                    show: true,
					                    textStyle: {
					                        fontSize: '22',
					                        fontWeight: 'bold'
					                    }
					                }
					            },
					            labelLine: {
					                normal: {
					                    show: false
					                }
					            },
					            data: arrlist
					        }
					    ]
				};
			myChart.setOption(option);
	}
		
			 $(document).on("click", "#bzt0", function() {
					
					//添加一个新的页面、
					layer.open({
				           type: 2,
				           shadeClose: true,
				           shade: 0.8,
				           maxmin: true,
				           area: ['70%', '70%'],
				           content: '${pageContext.request.contextPath}/jsp/swztqk.jsp',
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
		
	
		//按登记注册类型情况
		function swztqk1() {
			$.ajax({
				url:"ajax.do?ctrl=syqkfx_queryDJZCLX",
				type:"post",
				data:{},
	            success: function(obj){
	            	if(obj != null && obj.code == "000"){
	            		var data = obj.data;
	            		makechart1(data);
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
		}
		
	
		function makechart1(list){
			var yarr = new Array();//数量
			for (var i = 0; i < list.length; i++) {
				yarr.push(list[i].NAME);
			}
			var arrlist = list;
			var myChart = echarts.init(document.getElementById("adjzclx"), 'macarons');
			var option = {
					title : {
						text : '登记注册类型情况',
						x : 'center',
						textStyle: {
				              fontSize: 26,
				              fontWeight: 'bolder',
				              color: '#000080'
						},
					},
					color: ['#3398DB'],
				    tooltip : {
				        trigger: 'axis',
				        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
				            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				        }
				    },
				    grid: {
				        left: '3%',
				        right: '4%',
				        bottom: '3%',
				        containLabel: true
				    },
				    xAxis : [
				        {
				            type : 'category',
				            data : ['私营有限责任公司', '内资个体', '内资个人', '其他有限责任公司', '事业单位'],
				            axisTick: {
				                alignWithLabel: true
				            }
				        }
				    ],
				    yAxis : [
				        {
				            type : 'value'
				        }
				    ],
				    series : [
				        {
				            name:'直接访问',
				            type:'bar',
				            barWidth: '60%',
				            data:[6657, 6156, 1223, 1090, 152]
				        }
				    ]
				};
			myChart.setOption(option);
	}
		
			/* $(document).on("click", "#bzt0", function() {
					
					//添加一个新的页面、
					layer.open({
				           type: 2,
				           shadeClose: true,
				           shade: 0.8,
				           maxmin: true,
				           area: ['70%', '70%'],
				           content: '${pageContext.request.contextPath}/jsp/swztqk.jsp',
				           success: function(layero, index){  
				              var body = layer.getChildFrame('body', index);  
				               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
				               var inputList = body.find("input"); 
				               for (var i = 0; i < inputList.length; i++ ) {  
				            		   $(inputList[i]).val(editList[i]);  
				               }    
				           }   
				 }); 
			
			
	}) */
	
	
		//按行业类型分析
		function swztqk2() {
			$.ajax({
				url:"ajax.do?ctrl=syqkfx_queryDJZCLX",
				type:"post",
				data:{},
	            success: function(obj){
	            	if(obj != null && obj.code == "000"){
	            		var data = obj.data;
	            		makechart2(data);
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
		}
		
	
		function makechart2(list){
			var yarr = new Array();//数量
			for (var i = 0; i < list.length; i++) {
				yarr.push(list[i].NAME);
			}
			var arrlist = list;
			var myChart = echarts.init(document.getElementById("ahylx"), 'macarons');
			var option = {
					title : {
						text : '行业类型分析',
						x : 'center',
						textStyle: {
				              fontSize: 26,
				              fontWeight: 'bolder',
				              color: '#000080'
						},
					},
					 tooltip : {
					        trigger: 'axis',
					        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
					        }
					    },
					    grid: {
					        left: '3%',
					        right: '4%',
					        bottom: '3%',
					        containLabel: true
					    },
					xAxis: {
				        type: 'category',
				        data: ['其他电子产品零售', '计算机、软件及辅助设备零售', '其他居民服务业', '计算机、软件及辅助设备批发', '专业设计服务'],
				        axisTick: {
			                alignWithLabel: true
			            }
				       /*  axisLabel: {
							interval: 0,    //强制文字产生间隔
						    rotate: 45,     //文字逆时针旋转45°
							textStyle: {    //文字样式
							            color: "black",
							            fontSize: 11,
							            fontFamily: 'Microsoft YaHei'
							            }
						   } */
				    },
				    yAxis: {
				        type: 'value'
				    },
				   
				    series: [{
				    	 name:'直接访问',
				            type:'bar',
				            barWidth: '60%',
				        data: [1214, 860, 677, 613, 479],
				        type: 'bar'
				    }]
				};
			myChart.setOption(option);
	}
		
			/* $(document).on("click", "#bzt0", function() {
					
					//添加一个新的页面、
					layer.open({
				           type: 2,
				           shadeClose: true,
				           shade: 0.8,
				           maxmin: true,
				           area: ['70%', '70%'],
				           content: '${pageContext.request.contextPath}/jsp/swztqk.jsp',
				           success: function(layero, index){  
				              var body = layer.getChildFrame('body', index);  
				               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
				               var inputList = body.find("input"); 
				               for (var i = 0; i < inputList.length; i++ ) {  
				            		   $(inputList[i]).val(editList[i]);  
				               }    
				           }   
				 }); 
			
			
	}) */
	
	
		//按街道类型分析
		function swztqk3() {
			$.ajax({
				url:"ajax.do?ctrl=syqkfx_queryDJZCLX",
				type:"post",
				data:{},
	            success: function(obj){
	            	if(obj != null && obj.code == "000"){
	            		var data = obj.data;
	            		makechart3(data);
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
		}
		
	
		function makechart3(list){
			var yarr = new Array();//数量
			for (var i = 0; i < list.length; i++) {
				yarr.push(list[i].NAME);
			}
			var colors = ['#5793f3', '#d14a61', '#675bba'];
			var arrlist = list;
			var myChart = echarts.init(document.getElementById("ajdfx"), 'macarons');
			var option = {
					title: {
				        //text: '街道分析'
				    },
				    tooltip: {
				        trigger: 'axis'
				    },
				    legend: {
				        data:['玄武区新街口街道','玄武区梅园街道','玄武区玄武湖街道','玄武区孝陵卫街道','玄武区玄武门街道']
				    },
				    grid: {
				        left: '3%',
				        right: '4%',
				        bottom: '3%',
				        containLabel: true
				    },
				    toolbox: {
				        feature: {
				            saveAsImage: {}
				        }
				    },
				    xAxis: {
				        type: 'category',
				        boundaryGap: false,
				        data: ['周一','周二','周三','周四','周五','周六','周日']
				    },
				    yAxis: {
				        type: 'value'
				    },
				    series: [
				        {
				            name:'玄武区新街口街道',
				            type:'line',
				            stack: '总量',
				            data:[120, 132, 101, 134, 90, 230, 210]
				        },
				        {
				            name:'玄武区梅园街道',
				            type:'line',
				            stack: '总量',
				            data:[220, 182, 191, 234, 290, 330, 310]
				        },
				        {
				            name:'玄武区玄武湖街道',
				            type:'line',
				            stack: '总量',
				            data:[150, 232, 201, 154, 190, 330, 410]
				        },
				        {
				            name:'玄武区孝陵卫街道',
				            type:'line',
				            stack: '总量',
				            data:[320, 332, 301, 334, 390, 330, 320]
				        },
				        {
				            name:'玄武区玄武门街道',
				            type:'line',
				            stack: '总量',
				            data:[820, 932, 901, 934, 1290, 1330, 1320]
				        }
				    ]
				};
			myChart.setOption(option);
	}
		
			/* $(document).on("click", "#bzt0", function() {
					
					//添加一个新的页面、
					layer.open({
				           type: 2,
				           shadeClose: true,
				           shade: 0.8,
				           maxmin: true,
				           area: ['70%', '70%'],
				           content: '${pageContext.request.contextPath}/jsp/swztqk.jsp',
				           success: function(layero, index){  
				              var body = layer.getChildFrame('body', index);  
				               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
				               var inputList = body.find("input"); 
				               for (var i = 0; i < inputList.length; i++ ) {  
				            		   $(inputList[i]).val(editList[i]);  
				               }    
				           }   
				 }); 
			
			
	}) */
	
	
	
		//按登记日期分析
		function swztqk4() {
			$.ajax({
				url:"ajax.do?ctrl=syqkfx_queryDJZCLX",
				type:"post",
				data:{},
	            success: function(obj){
	            	if(obj != null && obj.code == "000"){
	            		var data = obj.data;
	            		makechart4(data);
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
		}
		
	
		function makechart4(list){
			var yarr = new Array();//数量
			for (var i = 0; i < list.length; i++) {
				yarr.push(list[i].NAME);
			}
			var colors = ['#5793f3', '#d14a61', '#675bba'];
			var arrlist = list;
			var myChart = echarts.init(document.getElementById("adjrqfx"), 'macarons');
			var option = {
					title : {
						text : '登记日期分析',
						x : 'center',
						textStyle: {
				              fontSize: 26,
				              fontWeight: 'bolder',
				              color: '#000080'
						},
					},
					xAxis: {
				        type: 'category',
				        data: ['14年之前', '15年', '16年', '17年', '18年', '19年']
				    },
				    yAxis: {
				        type: 'value'
				    },
				    series: [{
				        data: [120, 200, 150, 80, 70, 110],
				        type: 'line',
				        symbol: 'triangle',
				        symbolSize: 20,
				        lineStyle: {
				            normal: {
				                color: 'green',
				                width: 4,
				                type: 'dashed'
				            }
				        },
				        itemStyle: {
				            normal: {
				                borderWidth: 3,
				                borderColor: 'yellow',
				                color: 'blue'
				            }
				        }
				    }]
				};
			myChart.setOption(option);
	}
		
			/* $(document).on("click", "#bzt0", function() {
					
					//添加一个新的页面、
					layer.open({
				           type: 2,
				           shadeClose: true,
				           shade: 0.8,
				           maxmin: true,
				           area: ['70%', '70%'],
				           content: '${pageContext.request.contextPath}/jsp/swztqk.jsp',
				           success: function(layero, index){  
				              var body = layer.getChildFrame('body', index);  
				               var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
				               var inputList = body.find("input"); 
				               for (var i = 0; i < inputList.length; i++ ) {  
				            		   $(inputList[i]).val(editList[i]);  
				               }    
				           }   
				 }); 
			
			
	}) */
	</script>
</html>