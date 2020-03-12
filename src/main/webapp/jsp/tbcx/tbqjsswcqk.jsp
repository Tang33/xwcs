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
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<title>Insert title here</title>
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<!-- 引入 ECharts 文件 -->
    <script src="js/echarts.min.js"></script>
<style type="text/css">
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
}
</style>
</head>

<body style="overflow-x: hidden">
	<form class="layui-form" id="form" action="">
	<div class="layui-form" lay-filter="test1">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show" style="height: 50px;">
					<div class="layui-col-md12">
						<div class="layui-row layui-col-space10">
							<div class="layui-row">
								<div class="layui-col-md12">
									<div class="layui-row layui-col-space10">
										<div class="layui-col-md4">
											<div class="layui-inline">
												<label class="layui-form-label">截止月份:</label>
												<div class="layui-input-inline">
													<input type="text" class="layui-input" id="test8"
														placeholder="">
												</div>
											</div>
											<div class="layui-inline">
											</div>
											<div class="layui-inline">
											</div>
											<button class="layui-btn layui-btn-normal" id="button"
														type="button" lay-submit="" lay-filter="button">查
														询</button>
										</div>
										
									
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>

	<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 1000px;height:400px;"></div>

</body>


<script>
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
				var d1= r.RKRQ.substring(0,4);
				var d2= r.RKRQ.substring(4);
				var d=d1+'-'+d2;
				console.log(d)
				$("#test8").val(d);
			}
		}
	})
}

	layui.use(['form', 'layedit', 'laydate','laypage'], function() {
		
		var form = layui.form,
			layer = layui.layer,
			layedit = layui.layedit,
			laydate = layui.laydate,
			laypage=layui.laypage;
		//年月范围
		laydate.render({
			elem: '#test8',
			type: 'month',
			format:'yyyy-MM'
		});
		
		setTimeout(function(){ getData(); }, 500);
		/* form.render(null, 'test1'); //更新全部
		laypage.render({
		    elem: 'demo2'
		    ,count: 100
		    ,theme: '#1E9FFF'
		}); */
		
		form.on('submit(button)', function(data) {
			pageNo = 1; //当点击搜索的时候，应该回到第一页
			getData();
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
	});
	
	
	function getData() {
		var jzTime = $("#test8").val();//页面选择的截止时间
		console.log("截止时间:"+jzTime);
		ajax({
			url : "ajax.do?ctrl=tbqjsswcqk_dataQuery",
			data : {
				"jzTime": jzTime
			},
			type : 'post',
			success : function(obj) {
				console.log(obj.data);
				if(obj.code=="006"){
					aui.alert("意外彩蛋!请联系管理员");
				}else{
					getTb(obj.data);
				}
			}
		});
	}

	function getTb(optb) {
		// 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
        // 指定图表的配置项和数据
        var option = {
        	    title : {
        	        text: '财政总收入完成情况比对',
        	        subtext: '单位(元)'
        	    },
        	    tooltip : {
        	        trigger: 'axis'
        	    },
        	    legend: {
        	        data:[jzTime,sTime]
        	    },
        	    toolbox: {
        	        show : true,
        	        feature : {
        	            dataView : {show: true, readOnly: false},
        	            magicType : {show: true, type: ['line', 'bar']},
        	            restore : {show: true},
        	            saveAsImage : {show: true}
        	        }
        	    },
        	    calculable : true,
        	    xAxis : [
        	        {
        	            type : 'category',
        	            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        	        }
        	    ],
        	    yAxis : [
        	        {
        	            type : 'value'
        	        }
        	    ],
        	    series : [
        	        {
        	            name:'2019',
        	            type:'bar',
        	            data:[optb[0].BNJE, optb[1].BNJE, optb[2].BNJE, optb[3].BNJE, optb[4].BNJE, optb[5].BNJE, optb[6].BNJE, optb[7].BNJE, optb[8].BNJE, optb[9].BNJE, optb[10].BNJE, optb[11].BNJE],
        	        },
        	        {
        	            name:'2018',
        	            type:'bar',
        	            data:[optb[0].SNJE, optb[1].SNJE, optb[2].SNJE, optb[3].SNJE, optb[4].SNJE, optb[5].SNJE, optb[6].SNJE, optb[7].SNJE, optb[8].SNJE, optb[9].SNJE, optb[10].SNJE, optb[11].SNJE],
        	        }
        	    ]
        	    
        	};
		
		
		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	}
	
	// 以下3个方法提示作用
	function showLoad() {
	    return layer.msg('加载数据并生成图表中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:100000});
	}
	
	function closeLoad(index) {
	    layer.close(index);
	}
	function showSuccess() {
	    layer.msg('加载成功！',{time: 1000,offset: 'auto'});
	}
    </script>

</html>