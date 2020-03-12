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
<script src="js/echarts-gl.min.js"></script>
<style type="text/css">
#bzt0 {
	padding: 20px;
	width: 45%;
	height: 450px;
	margin-top: 10px;
	background-image: url("static/img/bj/bzt.png");
	background-repeat: no-repeat;
	margin: 0;
	float: left;
}

#bzt1 {
	padding: 20px;
	width: 93%;
	height: 550px;
	margin-top: 10px;
	background-image: url("static/img/bj/bzt.png");
	background-repeat: no-repeat;
	margin: 0;
	float: left;
}

#bzt2 {
	padding: 20px;
	width: 45%;
	height: 450px;
	margin-top: 10px;
	background-image: url("static/img/bj/bzt.png");
	background-repeat: no-repeat;
	margin: 0;
	float: left;
}

#bzt3 {
	padding: 20px;
	width: 45%;
	height: 42%;
	margin-top: 10px;
	background-image: url("static/img/bj/bzt.png");
	background-repeat: no-repeat;
	margin-left: 10px;
	float: left;
}

#bzt4 {
	padding: 20px;
	width: 45%;
	height: 42%;
	margin-left: 0px;
	margin-top: 10px;
	background-image: url("static/img/bj/bzt.png");
	background-repeat: no-repeat;
	float: left;
}
</style>
</head>
<body style="background-color: #F5F5F5; overflow-x: hidden">

	<div style="margin-top: 40px;">
		<div style="width: 100%;">
			<!-- 税务总体情况 -->
			<div id="bzt0"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="swztqk" style="width: 95%; height: 95%;"></div>
			</div>
			<!-- 按行业类型分析 -->
			<div id="bzt2"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="ahylx" style="width: 100%; height: 450px;"></div>
			</div>

			<!--按登记注册类型分析  -->
			<div id="bzt1"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="adjzclx" style="width: 100%; height: 500px;"></div>

			</div>

			<!-- 按街道类型分析 -->
			<div id="bzt3"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="ajdfx" style="width: 100%; height: 400px;">
					<div class="set-title hd ">
						<ul class="clearfix" style="width: 97%;">
							<li class="on"
								style="text-align: center; font-size: 24px; font-weight: bolder; color: #333; color: '#333';">
								税源结构-按街道类型</li>
						</ul>
					</div>
					<table class="layui-table" id="table" lay-filter="user"
						style="width: 100%">
						<thead>
							<tr>
								<th lay-data="{field:'JD' }" style="text-align: center">街道</th>
								<th lay-data="{field:'ZHS'}" style="text-align: center">总户数</th>
								<th lay-data="{field:'JSHS'}" style="text-align: center">见税户数</th>
								<th lay-data="{field:'JNSSJE'}" style="text-align: center">今年税收金额（元）</th>
							</tr>
						</thead>
						<tbody id="ttbody3">
							
						</tbody>
					</table>
				</div>
			</div>

			<!-- 按登记日期分析 -->
			<div id="bzt4"
				style="-moz-background-size: 100% 100%; background-size: 100% 100%;">
				<div id="adjrqfx" style="width: 100%; height: 400px;">
					<div class="set-title hd ">
						<ul class="clearfix" style="width: 97%;">
							<li class="on"
								style="text-align: center; font-size: 24px; font-weight: bolder; color: #333; color: '#333';">
								税源结构-按登记日期类型</li>
						</ul>
					</div>
					<table class="layui-table" id="table" lay-filter="user"
						style="width: 100%">
						<thead>
							<tr>
								<th lay-data="{field:'YEAR'}" style="text-align: center">年份</th>
								<th lay-data="{field:'ZHS'}" style="text-align: center">总户数</th>
								<th lay-data="{field:'JSHS'}" style="text-align: center">见税户数</th>
							</tr>
						</thead>
						<tbody id="ttbody4">

						</tbody>
					</table>
				</div>
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
			url : "syqkfx/querySwztqk.do",
			type : "post",
			data : {},
			dataType:'json',
			success : function(obj) {
				debugger;
				if (obj != null && obj.code == "000") {
					var data = obj.data;

					makechart(data);
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

	function makechart(list) {
		debugger;
		var yarr = new Array();//数量
		for (var i = 0; i < list.length; i++) {
			yarr.push(list[i].NAME);
		}
		var colors = [ '#5793f3', '#d14a61', '#675bba' ];
		var arrlist = list;
		var myChart = echarts.init(document.getElementById("swztqk"),
				'macarons');
		var option = {
			title : {
				text : '税源结构',
				subtext : '总体情况',
				x : 'center',
				textStyle : {
					fontSize : 24,
					fontWeight : 'bolder',
					color : '#333' // 主标题文字颜色
				},
				subtextStyle : {
					color : '#aaa', // 副标题文字颜色
					fontSize : 18,
				}
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)",
				textStyle : {
					color : '#fff',
					fontSize : 18,
				}
			},
			legend : {
				orient : 'horizontal',
				x : 'center',
				// y 设置垂直安放位置，默认全图顶端，可选值：'top' ¦ 'bottom' ¦ 'center' ¦ {number}（y坐标，单位px）
				y : 350,
				itemWidth : 24, // 设置图例图形的宽
				itemHeight : 18, // 设置图例图形的高
				textStyle : {
					color : '#666', // 图例文字颜色
					fontSize : 20,
				},
				data : arrlist
			},
			series : [ {
				name : '访问来源',
				type : 'pie',
				radius : '55%',
				center : [ '50%', '50%' ],
				data : arrlist,
				itemStyle : {
					emphasis : {
						shadowBlur : 10,
						shadowOffsetX : 0,
						shadowColor : 'rgba(0, 0, 0, 0.5)'
					}
				}
			} ]
		};
		myChart.setOption(option);
	}

	$(document).on("click", "#swztqk", function() {

		//添加一个新的页面、
		layer.open({
			type : 2,
			shadeClose : true,
			shade : 0.8,
			maxmin : true,
			area : [ '70%', '70%' ],
			content : '${pageContext.request.contextPath}/jsp/swztqk.jsp',
			success : function(layero, index) {
				var body = layer.getChildFrame('body', index);
				var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
				var inputList = body.find("input");
				for (var i = 0; i < inputList.length; i++) {
					$(inputList[i]).val(editList[i]);
				}
			}
		});

	})

	//按登记注册类型情况
	function swztqk1() {
		$.ajax({
			url : "syqkfx/queryDjzclx.do",
			type : "post",
			data : {},
			dataType:'json',
			success : function(obj) {
				if (obj != null && obj.code == "000") {
					var data = obj.data;
					makechart1(data);
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

	function makechart1(list) {
		var lxArr = new Array();//类型
		var arr = new Array();
		for (var i = 0; i < list.length; i++) {
			lxArr.push(list[i].DJZCLX);
		}

		var hours = lxArr;//X
		var days = [ '总户数', '建税户数' ];//Y

		var data = [ [ 0, 0, list[0].ZHS ], /*  [Y,X,Z], */
		[ 1, 0, list[0].JSHS ], [ 0, 1, list[1].ZHS ], [ 1, 1, list[1].JSHS ],
				[ 0, 2, list[2].ZHS ], [ 1, 2, list[2].JSHS ],
				[ 0, 3, list[3].ZHS ], [ 1, 3, list[3].JSHS ],
				[ 0, 4, list[4].ZHS ], [ 1, 4, list[4].JSHS ],
				[ 0, 5, list[4].ZHS ], [ 1, 5, list[4].JSHS ],
				[ 0, 6, list[4].ZHS ], [ 1, 6, list[4].JSHS ],
		/* [0,7,list[4].ZHS],
		[1,7,list[4].JSHS],
		[0,8,list[4].ZHS],
		[1,8,list[4].JSHS],
		[0,9,list[4].ZHS],
		[1,9,list[4].JSHS], */
		];

		var arrlist = list;
		var myChart = echarts.init(document.getElementById("adjzclx"),
				'macarons');
		var option = {
			title : {
				text : '税源结构-按登记注册类型分析',
				x : 'center',
				textStyle : {
					fontSize : 24,
					fontWeight : 'bolder',
					color : '#333' // 主标题文字颜色
				},
			},
			tooltip : {
				trigger : 'item',
				formatter : function(params) {
					return params.name;
				}
			},
			visualMap : {
				x : '100',
				max : 10000,
				inRange : {
					color : [ '#313695', '#4575b4', '#74add1', '#abd9e9',
							'#e0f3f8', '#ffffbf', '#fee090', '#fdae61',
							'#f46d43', '#d73027', '#a50026' ]
				}
			},
			xAxis3D : {
				name : '登记注册类型',
				type : 'category',
				data : hours
			},
			yAxis3D : {
				name : '户数类型',
				type : 'category',
				data : days
			},
			zAxis3D : {
				name : '户数数量',
				type : 'value'
			},
			grid3D : {
				boxWidth : 300,
				boxDepth : 80,
				viewControl : {
				///projection: 'orthographic'
				},
				light : {
					main : {
						intensity : 1.2,
						shadow : true
					},
					ambient : {
						intensity : 0.3
					}
				}
			},
			series : [ {
				type : 'bar3D',
				data : data.map(function(item) {
					return {
						value : [ item[1], item[0], item[2] ],
					}
				}),
				shading : 'lambert',

				label : {
					textStyle : {
						fontSize : 20,
						borderWidth : 1
					}
				},

				emphasis : {
					label : {
						textStyle : {
							fontSize : 20,
							color : '#900'
						}
					},
					itemStyle : {
						color : '#900'
					}
				}
			} ]
		};
		myChart.setOption(option);
	}

	$(document).on("click", "#bzt1", function() {

		//添加一个新的页面、
		layer.open({
			type : 2,
			shadeClose : true,
			shade : 0.8,
			maxmin : true,
			area : [ '70%', '70%' ],
			content : '${pageContext.request.contextPath}/jsp/djzcqk.jsp',
			success : function(layero, index) {
				var body = layer.getChildFrame('body', index);
				var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
				var inputList = body.find("input");
				for (var i = 0; i < inputList.length; i++) {
					$(inputList[i]).val(editList[i]);
				}
			}
		});

	})

	//按行业类型分析
	function swztqk2() {
		$.ajax({
			url : "syqkfx/queryHY.do",
			type : "post",
			data : {},
			dataType:'json',
			success : function(obj) {
				if (obj != null && obj.code == "000") {
					var data = obj.data;
					makechart2(data);
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

	function makechart2(list) {

		var arrlist = list;
		var myChart = echarts
				.init(document.getElementById("ahylx"), 'macarons');
		var option = {
			title : {
				text : '税源结构-按行业类型分析',
				x : 'center',
				textStyle : {
					fontSize : 24,
					fontWeight : 'bolder',
					color : '#333' // 主标题文字颜色
				},
			},

			color : [ '#37a2da', '#32c5e9', '#9fe6b8', '#ffdb5c', '#ff9f7f',
					'#fb7293', '#e7bcf3', '#8378ea' ],
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b} : {c} ({d}%)"
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
				name : '增值电信业务统计表',
				type : 'pie',
				radius : [ 30, 180 ],

				roseType : 'area',
				data : arrlist
			} ]
		};
		myChart.setOption(option);

	}

	$(document).on("click", "#bzt2", function() {

		//添加一个新的页面、
		layer.open({
			type : 2,
			shadeClose : true,
			shade : 0.8,
			maxmin : true,
			area : [ '70%', '70%' ],
			content : '${pageContext.request.contextPath}/jsp/hylx.jsp',
			success : function(layero, index) {
				var body = layer.getChildFrame('body', index);
				var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
				var inputList = body.find("input");
				for (var i = 0; i < inputList.length; i++) {
					$(inputList[i]).val(editList[i]);
				}
			}
		});

	})

	//按街道类型分析
	function swztqk3() {
		$.ajax({
			url : "syqkfx/queryJDLX.do",
			type : "post",
			data : {},
			dataType:'json',
			success : function(obj) {
				if (obj != null && obj.code == "000") {
					var data = obj.data;
					makechart3(data);
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

	function makechart3(list) {
		var s = "";
		$
				.each(
						list,
						function(v, o) {
							if (v < 5) {
								s += '<tr>';
								s += '<td style="text-align:center">'
										+ (o.jd == undefined ? "-" : o.jd
												.replace("街道", "").replace(
														"玄武区", "")) + '</td>';
								s += '<td style="text-align:center">'
										+ (o.zhs == undefined ? "-" : o.zhs)
										+ '</td>';
								s += '<td style="text-align:center">'
										+ (o.jshs == undefined ? "-" : o.jshs)
										+ '</td>';
								s += '<td style="text-align:center">'
										+ (o.jnssje == undefined ? "-"
												: o.jnssje) + '</td></tr>';
								//14.000000000000002%
							}
							if (v == 4) {
								s += '<tr><td id="topmore3" colspan="7" style="text-align: center; cursor: pointer;">更多</td></tr>'
							}
						});
		$("#ttbody3").html(s);
	}

	$(document).on("click", "#topmore3", function() {
		//添加一个新的页面、
		layer.open({
			type : 2,
			shadeClose : true,
			shade : 0.8,
			maxmin : true,
			area : [ '70%', '70%' ],
			content : '${pageContext.request.contextPath}/jsp/Datamining3.jsp',
			success : function(layero, index) {
				var body = layer.getChildFrame('body', index);
				var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
				var inputList = body.find("input");
				for (var i = 0; i < inputList.length; i++) {
					$(inputList[i]).val(editList[i]);
				}
			}
		});

	})

	//按登记日期分析
	function swztqk4() {
		$.ajax({
			url : "syqkfx/queryDJRQLX.do",
			type : "post",
			data : {},
			dataType:'json',
			success : function(obj) {
				if (obj != null && obj.code == "000") {
					var data = obj.data;
					makechart4(data);
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

	function makechart4(list) {
		console.log(list)
		var s = "";
		$
				.each(
						list,
						function(v, o) {
							if (v < 5) {
								s += '<tr>';
								s += '<td style="text-align:center">'
										+ (o.year == undefined ? "-" : o.year)
										+ '</td>';
								s += '<td style="text-align:center">'
										+ (o.zhs == undefined ? "-" : o.zhs)
										+ '</td>';
								s += '<td style="text-align:center">'
										+ (o.jshs == undefined ? "-" : o.jshs)
										+ '</td><tr>';
							}
							if (v == 4) {
								s += '<tr><td id="topmore4" colspan="7" style="text-align: center; cursor: pointer;">更多</td></tr>'
							}
						});
		$("#ttbody4").html(s);
	}

	$(document).on("click", "#topmore4", function() {
		//添加一个新的页面、
		layer.open({
			type : 2,
			shadeClose : true,
			shade : 0.8,
			maxmin : true,
			area : [ '70%', '70%' ],
			content : '${pageContext.request.contextPath}/jsp/Datamining4.jsp',
			success : function(layero, index) {
				var body = layer.getChildFrame('body', index);
				var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();  
				var inputList = body.find("input");
				for (var i = 0; i < inputList.length; i++) {
					$(inputList[i]).val(editList[i]);
				}
			}
		});

	})
</script>
</html>