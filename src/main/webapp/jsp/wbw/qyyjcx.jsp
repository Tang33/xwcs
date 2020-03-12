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
	<div class="layui-form" lay-filter="test1">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md12">
							<div class="layui-row layui-col-space10">
								<div class="layui-col-md6">
									<div class="layui-inline">
										<label class="layui-form-label">查询月份:</label>
										<div class="layui-input-inline">
											<input type="text" class="layui-input" id="test8"
												placeholder=" - ">
										</div>
									</div>
								</div>
								<div class="layui-col-md6">
									<div class="layui-form-item">
										<div class="layui-row">
											<div class="layui-col-md7">
												<label class="layui-form-label">排序:</label>
												<div class="layui-input-block">
													<select name="interest" lay-filter="aihao">
														<option value="">本年至本月累计数</option>
														<option value="0">去年至本年累计数</option>
													</select>
												</div>
											</div>
											<div class="layui-col-md5">
												<div class="layui-input-block">
													<select name="interest" lay-filter="aihao">
														<option value="">倒序</option>
														<option value="0">正序</option>
													</select>
												</div>
											</div>
										</div>
									</div>
								</div>

							</div>

						</div>
					</div>

					<div class="layui-row">
						<div class="layui-col-md6">
							<div class="layui-row layui-col-space10">
								<div class="layui-col-md6">
									<div class="layui-form-item">
										<label class="layui-form-label">街道:</label>
										<div class="layui-input-block">
											<select name="interest" lay-filter="aihao">
												<option value="">全部</option>
												<option value="0">新街口</option>
												<option value="1">梅园</option>
												<option value="2">后宰门</option>
												<option value="3">玄武门</option>
												<option value="4">玄武湖</option>
												<option value="4">玄徐庄</option>
											</select>
										</div>
									</div>
								</div>
								<div class="layui-col-md6">
									<div class="layui-form-item">
										<label class="layui-form-label">统计单位:</label>
										<div class="layui-input-block">
											<select name="interest" lay-filter="aihao">
												<option value="0">元</option>
												<option value="1">万元</option>
											</select>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="layui-col-md6">
							<div class="layui-form-item">
								<div class="layui-row">
									<div class="layui-col-md6">
										<label class="layui-form-label">统计口径:</label>
										<div class="layui-input-block">
											<select name="interest" lay-filter="aihao">
												<option value="">全口径</option>
												<option value="0">地方口径</option>
											</select>
										</div>
									</div>
									<div class="layui-col-md6">
										<div style="text-align: center;">
											<!-- <button class="layui-btn layui-btn-normal" id="button" type="button">查 询</button> -->
											<div class="layui-btn-group">
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

		<fieldset class="layui-elem-field layui-field-title"
			style="margin-top: 20px;">
			<legend style="font-size: 12px;">单位：元</legend>

			<table class="layui-table" id="table" lay-filter="user">
				<thead>
					<tr>
						<th colspan="5" lay-data="{field:'LRRQ'}">税收情况</th>
						<th colspan="5" lay-data="{field:'LRRQ'}">营业收入情况</th>
						<th rowspan="2" lay-data="{field:'LRRQ'}">收入增减/税收增减</th>
					</tr>
					<tr>
						<th lay-data="{field:'ROWNUM'}">本年累计</th>
						<th lay-data="{field:'GNDM'}">上年同期</th>
						<th lay-data="{field:'GNMC'}">增减额</th>
						<th lay-data="{field:'PX'}">增减率</th>
						<th lay-data="{field:'URL'}">上年合计</th>
						<th lay-data="{field:'YXBZ'}">本年累计</th>
						<th lay-data="{field:'SJ_GNDM'}">上年同期</th>
						<th lay-data="{field:'GNMC'}">增减额</th>
						<th lay-data="{field:'PX'}">增减率</th>
						<th lay-data="{field:'URL'}">上年合计</th>
					</tr>
				</thead>
				<tbody id="ttbody">
					<tr>
						<td>100000</td>
						<td>90000</td>
						<td>10000</td>
						<td>10%</td>
						<td>90000</td>
						<td>100000</td>
						<td>90000</td>
						<td>10000</td>
						<td>10%</td>
						<td>90000</td>
						<td>20%</td>
					</tr>
					<tr>
						<td>100000</td>
						<td>90000</td>
						<td>10000</td>
						<td>10%</td>
						<td>90000</td>
						<td>100000</td>
						<td>90000</td>
						<td>10000</td>
						<td>10%</td>
						<td>90000</td>
						<td>20%</td>
					</tr>
					
				</tbody>
			</table>
			<div id="demo2"></div>
		</fieldset>

	</div>

	<script>
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
				range: false
			});
			form.render(null, 'test1'); //更新全部
			laypage.render({
			    elem: 'demo2'
			    ,count: 100
			    ,theme: '#1E9FFF'
			});
		});
	</script>


</body>

</html>