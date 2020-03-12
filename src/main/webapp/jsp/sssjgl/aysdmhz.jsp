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
<script src="./css/layui/layui.js" charset="utf-8"></script>

<title>按预算科目代码汇总查询</title>
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<style type="text/css">
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
}

/* .layui-table-main td:nth-child(3){

	text-align: right!important;
} */
</style>
</head>

<body style="overflow-x: hidden">
<blockquote class="layui-elem-quote layui-text">本功能页面用于对导入数据按预算科目代码汇总查询！</blockquote>
	<form class="layui-form" id="form1" action="">
		<div class="layui-form" lay-filter="test1">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					
					<div class="layui-colla-content  layui-show" style="line-height: 53px;margin: 0px auto;">
						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									
										<div class="layui-inline">
											<label class="layui-form-label" style="width: 170px;">按预算科目代码汇总查询:</label>
											<label class="layui-form-label" style="width: 40px;padding: 9px 15px;">月份:</label>
											<div class="layui-input-inline">
												<input id="yearNmonth" name="yearNmonth" type="text" placeholder="请选择日期"
													class="layui-input">
											</div>
										</div>
										
										<div class="layui-inline">
										<div style="text-align: center;">
											
												<button class="layui-btn layui-btn-normal" id="button"
													type="button" lay-submit="" lay-filter="button">查
													询</button>
												
											
											<button class="layui-btn layui-btn-normal" id="exportExcel"
													type="button" lay-submit="" lay-filter="exportExcel">导出</button>
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

				<table class="layui-table" id="tablequery" lay-filter="tablequery">
			</table>
			<div id="page"></div>
			</fieldset>
		</div>
	</form>
</body>

<script>
	//查询数据库的最大月份
	$(function() {
			$("#tablequery").attr("lay-data",
					"{width:" + document.body.clientWidth + "}");
		});

	$(function() {
		getMaxData();
		setTimeout("getData()", 500);
	});
	function getMaxData() {
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			url : "jkdzd/getNsDate.do",
			dataType : "json",
			success : function(obj) {
				if (obj.code == "000") {
					var r = obj.data[0];
					//日期条件默认值为数据库中数据的最大入库日期
					$("#yearNmonth").val(r.RKRQ);
				}
			}
		})
	}
	function showLoad() {
		return layer.msg('拼命加载数据中...', {
			icon : 16,
			shade : [ 0.5, '#f5f5f5' ],
			scrollbar : false,
			offset : 'auto',
			time : 200000
		});
	}

	function closeLoad(index) {
		layer.close(index);
	}
	
	layui
			.use(
					[ 'form', 'layedit', 'laydate', 'laypage', 'table' ],
					function() {
						var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate, laypage = layui.laypage;
						table = layui.table;

						laydate.render({
							elem : '#yearNmonth',
							type : 'month',
							format : 'yyyyMM'
						});

						form.render(null, 'test1'); //更新全部
						
						form.on('submit(button)', function(data) {
							//pageNo = 1; //当点击搜索的时候，应该回到第一页
							//loadlayer = layer.load();
							getData();
							return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
						});

						form
								.on(
										'submit(exportExcel)',
										function(data) {
											window.location.href = "jkdzd/jkdzdDerive.do?"
													+ $("#yearNmonth")
															.serialize();
										});

					});

	
	function getData() {
		loadlayer = layer.load();
		 table.render({
	          elem: '#tablequery'
	           ,height: 500 
	           ,width:document.body.clientWidth 
	          ,method : 'post'
	          ,url:"jkdzd/jkdzdShow.do"
	          ,where: {
	        	  date : $("#yearNmonth").val()
		       }
	          ,limit:10
	          ,page: true //开启分页
	          ,cols: [[ //表头
	            {field: 'YSKMDM', title: '预算科目代码'}
	            ,{field: 'YSKMMC', title: '预算科目名称'}
	            ,{field: 'DFKJ', title: '地方口径'}
	          ]]
	        ,done : function(res, curr, count){
	        	layer.close(loadlayer);
     		 	layer.msg('查询成功！', {
     				time : 1000,
     				offset : 'auto'
     			});
       		}
	    });
	        
	}

	/*
	 * formatMoney(s,type)
	 * 功能：金额按千位逗号分割
	 * 参数：s，需要格式化的金额数值.
	 * 参数：type,判断格式化后的金额是否需要小数位.
	 * 返回：返回格式化后的数值字符串.
	 */
	function formatMoney(s, type) {
		if (/[^0-9\.]/.test(s))
			return "0";
		if (s == null || s == "")
			return "0";
		s = s.toString().replace(/^(\d*)$/, "$1.");
		s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
		s = s.replace(".", ",");
		var re = /(\d)(\d{3},)/;
		while (re.test(s))
			s = s.replace(re, "$1,$2");
		s = s.replace(/,(\d\d)$/, ".$1");
		if (type == 0) {// 不带小数位(默认是有小数位)
			var a = s.split(".");
			if (a[1] == "00") {
				s = a[0];
			}
		}
		return s;
	}

</script>

</html>