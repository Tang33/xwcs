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
									<div class="layui-col-md4">
										<div class="layui-inline">
											<label class="layui-form-label">年月范围:</label>
											<div class="layui-input-inline">
										        <input type="text" class="layui-input" id="test8" placeholder="">
										    </div>
										</div>
									</div>
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<label class="layui-form-label">街道:</label>
											<div class="layui-input-block">
												<select name="interest" lay-filter="aihao" id="jdlist">
												</select>
											</div>
										</div>
									</div>
									<div class="layui-col-md4">
										<div class="layui-row">
											<div class="layui-col-md5">
												&nbsp;
											</div>
											<div class="layui-col-md7">
												<div class="layui-form-item">
													<div class="layui-input-block">
														<input type="radio" id="yhj" name="yhj" value="0" title="合計" checked="checked">
														<input type="radio" id="yhj" name="yhj" value="1" title="月明細">
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<label class="layui-form-label">纳税人名称:</label>
											<div class="layui-input-block">
												<input type="text" name="title" id="nsrmc" lay-verify="title" autocomplete="off" placeholder="模糊查询" class="layui-input">
											</div>
										</div>
									</div>
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<div class="layui-row">
												<div class="layui-col-md7">
													<label class="layui-form-label">排序:</label>
													<div class="layui-input-block">
														<select name="sortname" id="sortname" style="width: 40%;">
															<option value="NSRMC">纳税人名称</option>

															<option value="ZZS">增值税</option>
															<option value="YGZZZS">营改增增值税</option>
															<option value="YYS">营业税</option>
															<option value="QYSDS">企业所得税</option>
															<option value="GRSDS">个人所得税</option>
															<option value="CCS">车船税</option>
															<option value="FCS">房产税</option>
															<option value="YHS">印花税</option>
															<option value="CSWHJSS">城市维护建设税</option>
															<option value="DFJYFJ">地方教育附加</option>
															<option value="JYFJ">教育附加</option>
															<option value="ZSE">合计</option>
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
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<div class="layui-row">
												<div class="layui-col-md6">
													<label class="layui-form-label">企业性质:</label>
													<div class="layui-input-block">
														<select name="qyxz" id="qyxz" style="width: 30%;">
															<option value="%">全部</option>
															<option value="J">一般企业</option>
															<option value="F">房地产</option>
															<option value="B">区本级</option>
															<option value="">其他</option>
														</select>
													</div>
												</div>
												<div class="layui-col-md6">
													<label class="layui-form-label">合伙:</label>
													<input type="radio" id="hhqy" name="sex" value="0" title="是">
													<input type="radio" id="hhqy" name="sex" value="1" title="否" checked="">
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>

						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md4">
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
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<div class="layui-row">
												<div class="layui-col-md12">
													<label class="layui-form-label">行业:</label>
													<div class="layui-input-block">
														<select name="interest" lay-filter="aihao" id="hylist">
														</select>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<div class="layui-row">
												<div class="layui-col-md6">
													<label class="layui-form-label">重点税源:</label>
													<div class="layui-input-block">
														<select name="zdsyh" id="zdsyh" >
															<option selected="selected" value="%">请选择</option>
															<option value="01">金鹰</option>
															<option value="02">烟厂</option>
															<option value="04">苏宁</option>
															<option value="05">宏图三胞</option>
															<option value="06">华威</option>
															<option value="07">五星</option>
															<option value="08">现代快报</option>
															<option value="09">永和</option>
															<option value="11">海尔</option>
															<option value="12">长发</option>
															<option value="13">南汽</option>
															<option value="14">福中</option>
															<option value="15">交通集团</option>
															<option value="16">东南大学</option>
															<option value="17">熊猫</option>
															<option value="18">广电集团</option>
															<option value="19">南京商厦</option>
															<option value="20">好享购</option>
															<option value="21">地铁集团</option>
															<option value="22">国资商贸</option>
															<option value="23">火车站</option>
															<option value="24">新工集团</option>
															<option value="25">兴源电力</option>
															<option value="26">商贸旅游</option>
															<option value="27">华东医药</option>
															<option value="28">城建集团</option>
														</select>
													</div>
												</div>
												<div class="layui-col-md6">
													<div style="text-align: center;">
														<!-- <button class="layui-btn layui-btn-normal" id="button" type="button">查 询</button> -->
														<div class="layui-btn-group">
															<button class="layui-btn layui-btn-normal" id="button" type="button" lay-submit="" lay-filter="button">查 询</button>
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

						<div class="layui-row">
							<div class="layui-col-md12">
								<div class="layui-row layui-col-space10">
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<label class="layui-form-label">风险类型:</label>
											<div class="layui-input-block">
												<select name="fxlx" id="fxlx" style="width: 50%;">
													<option value="-1">--请选择--</option>
													<option value="0">营业收入和税收增减反向预警</option>
													<option value="1">税收下降10%以上</option>
													<option value="2">税收下降20%以上</option>
													<option value="3">税收下降30%以上</option>
													<option value="4">税收下降40%以上</option>
													<option value="5">税收下降50%以上</option>
												</select>
											</div>
										</div>
									</div>
									<div class="layui-col-md4">
									</div>
									<div class="layui-col-md4">
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
							<th lay-data="{field:'JD_MC'}">所属街道</th>
							<th lay-data="{field:'NSRMC'}">纳税人名称</th>
							<th lay-data="{field:'SS'}">税收</th>
							<th lay-data="{field:'SN_SS'}">上年同期</th>
							<th lay-data="{field:'YYS1'}">营业收入</th>
							<th lay-data="{field:'SN_SS'}">上年同期</th>
							<th lay-data="{field:'ZZS'}">增值税</th>
							<th lay-data="{field:'YGZZZS'}">"营改增"增值税</th>
							<th lay-data="{field:'YYS'}">营业税</th>
							<th lay-data="{field:'QYSDS_GS'}">企业所得税（国税）</th>
							<th lay-data="{field:'QYSDS_DS'}">企业所得税（地税）</th>
							<th lay-data="{field:'QYSDS'}">企业所得税（合计）</th>
							<th lay-data="{field:'GRSDS'}">个人所得税</th>
							<th lay-data="{field:'FCS'}">房产税</th>
							<th lay-data="{field:'YHS'}">印花税</th>
							<th lay-data="{field:'CCS'}">车船税</th>
							<th lay-data="{field:'HJ'}">合计</th>
						</tr>
					</thead>
					<tbody id="ttbody">
						
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
				format:'yyyyMM',
				range: true
			});
			form.render(null, 'test1'); //更新全部
			laypage.render({
			    elem: 'demo2'
			    ,count: 100
			    ,theme: '#1E9FFF'
			});
			
			

			ajax({
				url:"ajax.do?ctrl=qymxcx_queryInit",
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
						form.render('select');
						
						getData();
						
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
				var hy_dm=$("#hylist").val();
				var zdsyh=$("#zdsyh").val();
				var qyxz=$("#qyxz").val();
				var nsrmc=$("#nsrmc").val();
				var yhj=$("#yhj").val();
				var hhqy=$("#hhqy").val();
				var fxlx=$("#fxlx").val();
				window.location.href="export.do?ctrl=qymxcx_export&cxrq="
						+cxrq+"&jd_dm="+jd_dm+"&sortname="+sortname+"&sorttype="+sorttype
						+"&tjkj="+tjkj+"&hy_dm="+hy_dm+"&zdsyh="+zdsyh
						+"&qyxz="+qyxz+"&nsrmc="+nsrmc+"&yhj="+yhj+"&hhqy="+hhqy+"&fxlx="+fxlx;
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
			var hy_dm=$("#hylist").val();
			var zdsyh=$("#zdsyh").val();
			var qyxz=$("#qyxz").val();
			var nsrmc=$("#nsrmc").val();
			var yhj=$("#yhj").val();
			var hhqy=$("#hhqy").val();
			var fxlx=$("#fxlx").val();
			showLoad();
			ajax({
				url:"ajax.do?ctrl=qymxcx_queryMxcx",
				data:{
					"pageNo":pageNo,
					"pageSize":pageSize,
					"cxrq":cxrq,
					"jd_dm":jd_dm,
					"hy_dm":hy_dm,
					"sortname":sortname,
					"sorttype":sorttype,
					"tjkj":tjkj,
					"zdsyh":zdsyh,
					"qyxz":qyxz,
					"nsrmc":nsrmc,
					"yhj":yhj,
					"hhqy":hhqy,
					"fxlx":fxlx,
					"count":count
				},
				type:'post',
				success:function(obj){
					console.log(obj);
					if(obj!=null&&obj.data!=null){
						var sjList=obj.data;
						getTbale(sjList);
						count = obj.count;//数据总条数
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
		
		
		//初始化表格
	    function getTbale(data) {
	       var s = "";
	       $.each(data, function(v, o) {
	           s += '<tr><td>' + o.JD_MC + '</td>';
	           s += '<td>' + o.NSRMC + '</td>';
	           s += '<td>' + o.SS + '</td>';
	           s += '<td>' + o.ZZS + '</td>';
	           s += '<td>' + o.ZZS + '</td>';
	           s += '<td>' + o.ZZS + '</td>';
	           s += '<td>' + o.ZZS + '</td>';
	           s += '<td>' + o.YGZZZS + '</td>';
	           s += '<td>' + o.YYS + '</td>';
	           s += '<td>' + o.QYSDS_GS + '</td>';
	           s += '<td>' + o.QYSDS_DS + '</td>';
	           s += '<td>' + o.QYSDS + '</td>';
	           s += '<td>' + o.GRSDS + '</td>';
	           s += '<td>' + o.FCS + '</td>';
	           s += '<td>' + o.YHS + '</td>';
	           s += '<td>' + o.CCS + '</td>';
	           s += '<td>' + o.HJ + '</td>';
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