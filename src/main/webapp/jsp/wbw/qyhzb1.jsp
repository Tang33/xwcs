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
												<input type="text" class="layui-input" id="test8" placeholder=" - ">
											</div>
										</div>
									</div>
									<div class="layui-col-md6">
										<div class="layui-form-item">
											<div class="layui-row">
												<div class="layui-col-md7">
													<label class="layui-form-label">排序:</label>
													<div class="layui-input-block">
														<select name="sortname" id="sortname" style="width: 40%;">
															<option value="NSRMC">纳税人名称</option>
															<option value="SSBNYLJ">本年至本月累计数（税收）</option>
															<option value="SNSSTQS">上年同期数（税收）</option>
															<option value="SSSNQNHJ">上年全年合计（税收）</option>
															<option value="YSSRBYLJ">本年至本月累计数（营业收入）</option>
															<option value="YSSRSNTQS">上年同期数（营业收入）</option>
															<option value="YYSRSNQNHJ">上年全年合计（营业收入）</option>
															<option value="HY_MC">行业名称</option>
															<option value="JD_MC">街道名称</option>
														</select>
													</div>
												</div>
												<div class="layui-col-md5">
													<div class="layui-input-block">
														<select name="sorttype" id="sorttype" style="width: 30%;">
															<option value="DESC">降序</option>
															<option value="ASC">升序</option>
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
								<div class="layui-form-item">
									<label class="layui-form-label">统计口径:</label>
									<div class="layui-input-block">
										<select name="tjkj" id="tjkj" style="width: 30%;">
											<option value="0">全口径</option>
											<option value="1">归属地方</option>
										</select>
									</div>
								</div>
							</div>
						</div>
						<div class="layui-row">
							<div class="layui-col-md6">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md6">
										<div class="layui-form-item">
											<label class="layui-form-label">行业:</label>
											<div class="layui-input-block">
												<select name="interest" lay-filter="aihao" id="hylist">
												</select>
											</div>
										</div>
									</div>
									<div class="layui-col-md6">
										<div class="layui-form-item">
											<label class="layui-form-label">统计单位:</label>
											<div class="layui-input-block">
												<select name="tjdw" id="tjdw" style="width: 30%;">
													<option value="1">元</option>
													<option value="10000">万元</option>
													<option value="1000000">百万元</option>
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
											<label class="layui-form-label">税种:</label>
											<div class="layui-input-block">
												<select name="zsxm_dm" id="zsxm_dm" style="width: 30%;">
													<option value="%">全部</option>
													<option value="01">增值税</option>
													<option value="999">营改增税</option>
													<option value="02">营业税</option>
													<option value="51">企业所得税</option>
													<option value="05">个人所得税</option>
													<option value="22">车船税</option>
													<option value="12">房产税</option>
													<option value="16">印花税</option>
													<option value="10">城市维建设税</option>
													<option value="63">地方教育附加</option>
													<option value="61">教育附加</option>
												</select>
											</div>
										</div>
										<div class="layui-col-md6">
											<div style="text-align: center;">
												<!-- <button class="layui-btn layui-btn-normal" id="button" type="button">查 询</button> -->
												<div class="layui-btn-group">
													<button class="layui-btn layui-btn-normal" id="button" type="button" lay-submit="" lay-filter="button">查 询</button>
												</div>
												<div class="layui-btn-group">
												<button class="layui-btn layui-btn-normal" id="export" type="button" lay-submit="" lay-filter="export">导出Execl</button>
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

			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend style="font-size:12px;">单位：元</legend>

				<table class="layui-table" id="table" lay-filter="user">
					<thead>
						<tr>
							<th rowspan="3" lay-data="{field:'ROWNUM'}">序号</th>
							<th rowspan="3" lay-data="{field:'GNMC'}">纳税人名称</th>
							<th colspan="27" lay-data="{field:'PX'}">营业收入情况</th>
							<th colspan="27" lay-data="{field:'URL'}">税收情况</th>
						</tr>
						<tr>
							<th colspan="2" lay-data="{field:'URL1'}">1月</th>
							<th colspan="2" lay-data="{field:'URL2'}">2月</th>
							<th colspan="2" lay-data="{field:'URL3'}">3月</th>
							<th colspan="2" lay-data="{field:'URL4'}">4月</th>
							<th colspan="2" lay-data="{field:'URL5'}">5月</th>
							<th colspan="2" lay-data="{field:'URL6'}">6月</th>
							<th colspan="2" lay-data="{field:'URL7'}">7月</th>
							<th colspan="2" lay-data="{field:'URL8'}">8月</th>
							<th colspan="2" lay-data="{field:'URL9'}">9月</th>
							<th colspan="2" lay-data="{field:'URL10'}">10月</th>
							<th colspan="2" lay-data="{field:'URL11'}">11月</th>
							<th colspan="2" lay-data="{field:'URL12'}">12月</th>
							<th rowspan="2" lay-data="{field:'URL13'}">本年累计</th>
							<th rowspan="2" lay-data="{field:'URL14'}">上年同期</th>
							<th rowspan="2" lay-data="{field:'URL15'}">上年累计</th>
							<th colspan="2" lay-data="{field:'URL16'}">1月</th>
							<th colspan="2" lay-data="{field:'URL17'}">2月</th>
							<th colspan="2" lay-data="{field:'URL18'}">3月</th>
							<th colspan="2" lay-data="{field:'URL19'}">4月</th>
							<th colspan="2" lay-data="{field:'URL20'}">5月</th>
							<th colspan="2" lay-data="{field:'URL21'}">6月</th>
							<th colspan="2" lay-data="{field:'URL22'}">7月</th>
							<th colspan="2" lay-data="{field:'URL23'}">8月</th>
							<th colspan="2" lay-data="{field:'URL24'}">9月</th>
							<th colspan="2" lay-data="{field:'URL25'}">10月</th>
							<th colspan="2" lay-data="{field:'URL26'}">11月</th>
							<th colspan="2" lay-data="{field:'URL27'}">12月</th>
							<th rowspan="2" lay-data="{field:'URL28'}">本年累计</th>
							<th rowspan="2" lay-data="{field:'URL29'}">上年同期</th>
							<th rowspan="2" lay-data="{field:'URL30'}">上年累计</th>
						</tr>
						<tr>
							<th lay-data="{field:'URLB1'}">本年</th>
							<th lay-data="{field:'URLB2'}">上年</th>
							<th lay-data="{field:'URLB3'}">本年</th>
							<th lay-data="{field:'URLB4'}">上年</th>
							<th lay-data="{field:'URLB5'}">本年</th>
							<th lay-data="{field:'URLB6'}">上年</th>
							<th lay-data="{field:'URLB7'}">本年</th>
							<th lay-data="{field:'URLB8'}">上年</th>
							<th lay-data="{field:'URLB9'}">本年</th>
							<th lay-data="{field:'URLB10'}">上年</th>
							<th lay-data="{field:'URLC1'}">本年</th>
							<th lay-data="{field:'URLC2'}">上年</th>
							<th lay-data="{field:'URLC3'}">本年</th>
							<th lay-data="{field:'URLC4'}">上年</th>
							<th lay-data="{field:'URLC5'}">本年</th>
							<th lay-data="{field:'URLC6'}">上年</th>
							<th lay-data="{field:'URLC7'}">本年</th>
							<th lay-data="{field:'URLC8'}">上年</th>
							<th lay-data="{field:'URLC9'}">本年</th>
							<th lay-data="{field:'URLC10'}">上年</th>
							<th lay-data="{field:'URLD1'}">本年</th>
							<th lay-data="{field:'URLD2'}">上年</th>
							<th lay-data="{field:'URLD3'}">本年</th>
							<th lay-data="{field:'URLD4'}">上年</th>
							<th lay-data="{field:'URLD5'}">本年</th>
							<th lay-data="{field:'URLD6'}">上年</th>
							<th lay-data="{field:'URLD7'}">本年</th>
							<th lay-data="{field:'URLD8'}">上年</th>
							<th lay-data="{field:'URLD9'}">本年</th>
							<th lay-data="{field:'URLD10'}">上年</th>
							<th lay-data="{field:'URLE1'}">本年</th>
							<th lay-data="{field:'URLE2'}">上年</th>
							<th lay-data="{field:'URLE3'}">本年</th>
							<th lay-data="{field:'URLE4'}">上年</th>
							<th lay-data="{field:'URLE5'}">本年</th>
							<th lay-data="{field:'URLE6'}">上年</th>
							<th lay-data="{field:'URLE7'}">本年</th>
							<th lay-data="{field:'URLE8'}">上年</th>
							<th lay-data="{field:'URLE9'}">本年</th>
							<th lay-data="{field:'URLE10'}">上年</th>
							<th lay-data="{field:'URLF1'}">本年</th>
							<th lay-data="{field:'URLF2'}">上年</th>
							<th lay-data="{field:'URLF3'}">本年</th>
							<th lay-data="{field:'URLF4'}">上年</th>
							<th lay-data="{field:'URLF5'}">本年</th>
							<th lay-data="{field:'URLF6'}">上年</th>
							<th lay-data="{field:'URLF7'}">本年</th>
							<th lay-data="{field:'URLF8'}">上年</th>
						</tr>
					</thead>
					<tbody id="ttbody">
						
					</tbody>
				</table>
				<div id="demo2"></div>
			</fieldset>

		</div>
	<script>
	var pageSize = 10;//每页显示数据条数
	var pageNo = 1;//当前页数
	var count = 0;//数据总条数
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
			var pageSize = 10;//每页显示数据条数
			var pageNo = 1;//当前页数
			var count = 0;//数据总条数

			ajax({
				url:"ajax.do?ctrl=qyhzb1_queryInit&pageNo=" + pageNo + "&pageSize="
					+ pageSize,
				data:{},
				type:'post',
				success:function(obj){
					if(obj!=null&&obj.data!=null){
						var str="<option value='%'>请选择</option>";
						var jdlist=obj.data.jdlist;
						var hylist=obj.data.hylist;
						for (var i = 0; i < jdlist.length; i++) {
							str+="<option value='"+jdlist[i].JD_DM+"'>"+jdlist[i].JD_MC+"</option>";
						}
						$("#jdlist").html(str);
						form.render('select');
						str="<option value='%'>请选择</option>";
						for (var i = 0; i < hylist.length; i++) {
							str+="<option value='"+hylist[i].HYML_DM+"'>"+hylist[i].HYML_MC+"</option>";
						}
						$("#hylist").html(str);
						getData();
						
						form.render('select');
					}
				}
			});
			
			form.on('submit(button)',function(){
				getData();
			});
			
			
			form.on('submit(export)',function(){
				var cxrq=$("#test8").val();
				var jd_dm=$("#jdlist").val();
				var sortname=$("#sortname").val();
				var sorttype=$("#sorttype").val();
				var tjkj=$("#tjkj").val();
				var tjdw=$("#tjdw").val();
				var hy_dm=$("#hylist").val();
				console.log(">>>执行导出<<<");
				window.location.href="export.do?ctrl=qyhzb1_export&cxrq="+cxrq
						+"&tjkj="+tjkj+"&jd_dm="+jd_dm+"&sortname="+sortname
						+"&sorttype="+sorttype+"&tjdw="+tjdw+"&hy_dm="+hy_dm;
			});
			
		});
		
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

		//分页参数设置 这些全局变量关系到分页的功能
		var pageSize = 10;//每页显示数据条数
		var pageNo = 1;//当前页数
		var count = 0;//数据总条数
		function getData() {			
			var cxrq=$("#test8").val();
			var jd_dm=$("#jdlist").val();
			var sortname=$("#sortname").val();
			var sorttype=$("#sorttype").val();
			var tjkj=$("#tjkj").val();
			var tjdw=$("#tjdw").val();
			var hy_dm=$("#hylist").val();
			showLoad();
			ajax({
				url:"ajax.do?ctrl=qyhzb1_queryHzb1",
				data:{
					"pageNo":pageNo,
					"pageSize":pageSize,
					"cxrq":cxrq,
					"jd_dm":jd_dm,
					"hy_dm":hy_dm,
					"sortname":sortname,
					"sorttype":sorttype,
					"tjkj":tjkj,
					"tjdw":tjdw,
					"count": count
				},
				type:'post',
				success:function(obj){
					console.log(obj);
					if(obj!=null&&obj.data!=null){
						var sjList=obj.data;
						getTbale(sjList);
						count = obj.count;
						queryPage();
						showSuccess();
					}
				}
			});
		}

		function queryPage() {
			layui.use([ 'laypage' ], function() {
				laypage = layui.laypage;
				laypage.render({
					elem : 'demo2' //注意，这里的page1 是 ID，不用加 # 号
					,
					count : count //数据总数，从服务端得到
					,
					limit : 1000//每页显示条数
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
		
		
		//初始化表格
	    function getTbale(data) {
	       var s = "";
	       $.each(data, function(v, o) {
	           s += '<tr><td>' + (v+1) + '</td>';
	           s += '<td>' + o.NSRMC + '</td>';
	           s += '<td>' + o.BNYYSSYLJ1 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ1 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ2 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ2 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ3 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ3 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ4 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ4 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ5 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ5 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ6 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ6 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ7 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ7 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ8 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ8 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ9 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ9 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ10 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ10 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ11 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ11 + '</td>';
	           s += '<td>' + o.BNYYSSYLJ12 + '</td>';
	           s += '<td>' + o.SNYYSSYLJ12 + '</td>';
	           s += '<td>' + o.YSSRBYLJ + '</td>';
	           s += '<td>' + o.YSSRSNTQS + '</td>';
	           s += '<td>' + o.YYSRSNQNHJ + '</td>';
	           s += '<td>' + o.BNSSYLJ1 + '</td>';
	           s += '<td>' + o.SNSSYLJ1 + '</td>';
	           s += '<td>' + o.BNSSYLJ2 + '</td>';
	           s += '<td>' + o.SNSSYLJ2 + '</td>';
	           s += '<td>' + o.BNSSYLJ3 + '</td>';
	           s += '<td>' + o.SNSSYLJ3 + '</td>';
	           s += '<td>' + o.BNSSYLJ4 + '</td>';
	           s += '<td>' + o.SNSSYLJ4 + '</td>';
	           s += '<td>' + o.BNSSYLJ5 + '</td>';
	           s += '<td>' + o.SNSSYLJ5 + '</td>';
	           s += '<td>' + o.BNSSYLJ6 + '</td>';
	           s += '<td>' + o.SNSSYLJ6 + '</td>';
	           s += '<td>' + o.BNSSYLJ7 + '</td>';
	           s += '<td>' + o.SNSSYLJ7 + '</td>';
	           s += '<td>' + o.BNSSYLJ8 + '</td>';
	           s += '<td>' + o.SNSSYLJ8 + '</td>';
	           s += '<td>' + o.BNSSYLJ9 + '</td>';
	           s += '<td>' + o.SNSSYLJ9 + '</td>';
	           s += '<td>' + o.BNSSYLJ10 + '</td>';
	           s += '<td>' + o.SNSSYLJ10 + '</td>';
	           s += '<td>' + o.BNSSYLJ11 + '</td>';
	           s += '<td>' + o.SNSSYLJ11 + '</td>';
	           s += '<td>' + o.BNSSYLJ12 + '</td>';
	           s += '<td>' + o.SNSSYLJ12 + '</td>';
	           s += '<td>' + o.SSBNYLJ + '</td>';
	           s += '<td>' + o.SNSSTQS + '</td>';
	           s += '<td>' + o.SSSNQNHJ + '</td>';
	           s += '</tr>';
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
	           
	       });
	       if (data == null || data.length <= 0) {
				$("#demo2").hide();
			}else {
				$("#demo2").show();
			}
	  	 } /* getTbale结尾 */
	
		
		
	</script>


</body>

</html>