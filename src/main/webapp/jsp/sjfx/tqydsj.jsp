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
											</div>
											<div class="layui-col-md4">
												<div class="layui-form-item">
													<label class="layui-form-label">纳税人名称:</label>
													<div class="layui-input-block">
														<input type="text" name="title" lay-verify="title"
															autocomplete="off" class="layui-input" id="nsr" placeholder="请输入纳税人名称">
													</div>
												</div>
											</div>
											<div class="layui-col-md2">
												<div style="text-align: center;">
													<!-- <button class="layui-btn layui-btn-normal" id="button" type="button">查 询</button> -->
													<div class="layui-btn-group">
														<button class="layui-btn layui-btn-normal" id="button" type="button" lay-submit="" lay-filter="button">查询</button>
														<button class="layui-btn layui-btn-normal" id="export" type="button" lay-submit="" lay-filter="export">导出Excel</button>
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
			</div>
		</div>
	</form>

	<div>
	<fieldset class="layui-elem-field layui-field-title"
		style="margin-top: 20px;">
		<legend style="font-size: 12px;">单位：元</legend>

		<table class="layui-table" id="table" lay-filter="user">
			<thead>
				<tr>
					<th lay-data="{field:'ROWNUM'}">纳税人名称</th>
					<th lay-data="{field:'GNDM'}">年份</th>
					<th lay-data="{field:'LRRQ1'}">1月</th>
					<th lay-data="{field:'LRRQ2'}">2月</th>
					<th lay-data="{field:'LRRQ3'}">3月</th>
					<th lay-data="{field:'LRRQ4'}">4月</th>
					<th lay-data="{field:'LRRQ5'}">5月</th>
					<th lay-data="{field:'LRRQ6'}">6月</th>
					<th lay-data="{field:'LRRQ7'}">7月</th>
					<th lay-data="{field:'LRRQ8'}">8月</th>
					<th lay-data="{field:'LRRQ9'}">9月</th>
					<th lay-data="{field:'LRRQ0'}">10月</th>
					<th lay-data="{field:'LRRQ11'}">11月</th>
					<th lay-data="{field:'LRRQ12'}">12月</th>
					<th lay-data="{field:'HJ'}">截止本月合計</th>
					<th lay-data="{field:'NHJ'}">全年合計</th>
				</tr>
			</thead>
			<tbody id="ttbody">
				

			</tbody>
		</table>
		<div id="demo2"></div>
	</fieldset>

	</div>
</body>

<script>
$(function() {
    $("#table").attr("lay-data",
        "{width:" + document.body.clientWidth + "}");
  }); 
  
	//查询数据库的最大月份
	getMaxData();
	function getMaxData() {
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url : "ajax.do?ctrl=Sjdr_getNsDate",
			data : "",
			dataType : "json",
			success : function(obj) {
				if (obj.code == "000") {
					var r = obj.data[0];
					//日期条件默认值为数据库中数据的最大入库日期
					var year = r.RKRQ.substring(0,4);
					var month = r.RKRQ.substring(4,6);
					var rr = year + "-" + month;
					$("#test8").val(rr);
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
		    ,count: 2
		    ,theme: '#1E9FFF'
		}); */
		
		// 查询
		form.on('submit(button)', function(data) {
			pageNo = 1; //当点击搜索的时候，应该回到第一页
			getData();
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		// 导出
		form.on('submit(export)',function(){
			showLoad1();
			var jzTime = $("#test8").val();//页面选择的截止时间
			var nsr = $("#nsr").val();
			window.location.href="export.do?ctrl=tqydsj_export&jzTime="+jzTime+"&nsr="+nsr;
		});
		
	});
	
	// 根据条件查询内容方法
	//分页参数设置 这些全局变量关系到分页的功能
	var pageSize = 10;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
	function getData() {
		console.log("显示条数"+pageSize+",当前页数"+pageNo+",数据总条数"+count);
		var jzTime = $("#test8").val();//页面选择的截止时间
		var nsr = $("#nsr").val();
		console.log("截止时间:"+jzTime+",纳税人:"+nsr);
		 showLoad();
		ajax({
			url : "ajax.do?ctrl=tqydsj_dataQuery",
			data : {
				"jzTime": jzTime,
				"nsr": nsr,
				"pageNo": pageNo,
				"counts": count,
				"pageSize": pageSize
			},
			type : 'post',
			success : function(obj) {
				getTable(obj.data);
				count = obj.count;//数据总条数
				queryPage();
				showSuccess();
			}
		});
	}

	function queryPage() {
		layui.use([ 'laypage' ], function() {
			laypage = layui.laypage;
			laypage.render({
				elem : 'demo2' //注意，这里的demo2 是 ID，不用加 # 号
				,
				count : count //数据总数，从服务端得到
				,
				limit : pageSize//每页显示条数
				,
				limits : [ 10, 20, 30, 50 ]//条数列表
				,
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

	function getTable(data) {
		var s = "";
		$.each(data, function(v, o) {
			s +='<tr>';
			s += '<td>' + o.NSRMC + '</td>';
			s += '<td>' + o.NF + '</td>';
			s += '<td>' + (o.M01==null? 0 : o.M01) + '</td>';
			s += '<td>' + (o.M02==null? 0 : o.M02) + '</td>';
			s += '<td>' + (o.M03==null? 0 : o.M03) + '</td>';
			s += '<td>' + (o.M04==null? 0 : o.M04) + '</td>';
			s += '<td>' + (o.M05==null? 0 : o.M05) + '</td>';
			s += '<td>' + (o.M06==null? 0 : o.M06) + '</td>';
			s += '<td>' + (o.M07==null? 0 : o.M07) + '</td>';
			s += '<td>' + (o.M08==null? 0 : o.M08) + '</td>';
			s += '<td>' + (o.M09==null? 0 : o.M09) + '</td>';
			s += '<td>' + (o.M10==null? 0 : o.M10) + '</td>';
			s += '<td>' + (o.M11==null? 0 : o.M11) + '</td>';
			s += '<td>' + (o.M12==null? 0 : o.M12) + '</td>';
			s += '<td>' + (o.HJ==null? 0 : o.HJ) + '</td>';
			s += '<td>' + (o.NHJ==null? 0 : o.NHJ) + '</td>';
			s +='</tr>';
		});
		$("#ttbody").html(s);
		//执行渲染
		layui.use([ 'table' ], function() {
			layui.table.init('user', {
				height : 480 //设置高度
				,
				limit : pageSize 
			//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
			//支持所有基础参数
			});
			table=layui.table;
			
		});
	
		if (data == null || data.length <= 0) {
			$("#demo2").hide();
		}
	
	}
	
	// 以下3个方法提示作用
	function showLoad() {
	    return layer.msg('拼命加载数据中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:100000});
	}
	
	function closeLoad(index) {
	    layer.close(index);
	}
	function showSuccess() {
	    layer.msg('查询成功！',{time: 1000,offset: 'auto'});
	}

	// 导出时提示
	function showLoad1() {
	    return layer.msg('数据导出中请稍等！，请勿其他操作,等待下载页面！', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:3000});
	}

</script>

</html>