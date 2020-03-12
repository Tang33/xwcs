<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
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
<title>分行业查询</title>
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
	<form class="layui-form" id="form1" action="">
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
												<input type="text" class="layui-input" id="yearNmonth" name="yearNmonth" placeholder="请选择日期">
											</div>
										</div>
									</div>
									<div class="layui-col-md4">
										&nbsp;
									</div>
									<div class="layui-col-md4">
										<div class="layui-row">
											<div class="layui-col-md5">
												<div class="layui-form-item">
													<label class="layui-form-label">街道:</label>
													<div class="layui-input-block">
														<select name="jdlist" lay-filter="jdlist" id="jdlist">
															<option value="%">请选择</option>
														</select>
													</div>
												</div>
											</div>
											<div class="layui-col-md7">
												<div class="layui-form-item">
													<div class="layui-input-block">
														<input type="radio" name="type" lay-filter="type"  value="0" title="合計" checked="">
														<input type="radio" name="type" lay-filter="type"  value="1" title="月明細">
														<input type="hidden" name="cxlx" id="cxlx" value="0">
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
												<input id="nsName" type="text" name="nsName" lay-verify="nsName" autocomplete="off" placeholder="模糊查询" class="layui-input">
											</div>
										</div>
									</div>
									<div class="layui-col-md4">
										<div class="layui-form-item">
											<div class="layui-row">
												<div class="layui-col-md7">
													<label class="layui-form-label">排序:</label>
													<div class="layui-input-block">
														<select name="sortname" style="width: 40%;" id="sortname">
															<option value="NSRMC">行业名称</option>
															
														</select>
													</div>
												</div>
												<div class="layui-col-md5">
													<div class="layui-input-block">
														<select name="px" lay-filter="px" id="px">
															<option value="DESC">倒序</option>
															<option value="ASC">正序</option>
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
														<select name="qyxz" style="width: 30%;" id="qyxz">
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
													<input type="radio" name="type1" lay-filter="type1" value="0" title="是" checked=""> 
													<input type="radio" name="type1" lay-filter="type1" value="1" title="否">
													<input type="hidden" name="sfhh" id="sfhh" value="0">
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
												<select name="tjkj" lay-filter="tjkj" id="tjkj">
													<option value="">全口径</option>
													<option value="0">地方口径</option>
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
														<select name="hylist" lay-filter="hylist" id="hylist">
															<option value="%">请选择</option>
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
														<select name="zdsyh" id="zdsyh" lay-filter="zdsyh">
															<option value="%">请选择</option>
															<option value="01">金鹰</option>
															<option value="02">烟厂</option>
															<option value="03">德基</option>
															<option value="04">苏宁</option>
															<option value="05">宏图三胞</option>
															<option value="06">华威</option>
															<option value="07">五星</option>
															<option value="08">现代快报</option>
															<option value="09">永和</option>
															<option value="10">途牛</option>
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
														</div>
														<div class="layui-btn-group">
															<button class="layui-btn layui-btn-normal" id="exportExcel" type="button" lay-submit="" lay-filter="exportExcel">导出Execl</button>
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

			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend style="font-size:12px;">单位：元</legend>

				<table class="layui-table" id="table" lay-filter="user">
					<thead>
						<tr>
							<th lay-data="{field:'HYDM',style:'display:none;'}">行业代码名称</th>
							<th lay-data="{field:'HYMC'}">行业名称</th>							
							<th lay-data="{field:'ZZS'}">增值税</th>
							<th lay-data="{field:'YGZZZS'}">"营改增"增值税</th>
							<th lay-data="{field:'YYS'}">营业税</th>							
							<th lay-data="{field:'QYSDSGS'}">企业所得税(国税)</th>
							<th lay-data="{field:'QYSDSDS'}">企业所得税(地税)</th>
							<th lay-data="{field:'QYSDSH'}">企业所得税(合计)</th>
							<th lay-data="{field:'GRSDS'}">个人所得税</th>
							<th lay-data="{field:'FCS'}">房产税</th>							
							<th lay-data="{field:'YHS'}">印花税</th>
							<th lay-data="{field:'CCS'}">车船税</th>
							<th lay-data="{field:'XJ'}">小计</th>
							<th lay-data="{field:'CXWHJSS'}">城市维护建设税</th>							
							<th lay-data="{field:'DFJYFJ'}">地方教育附加</th>
							<th lay-data="{field:'JYFJ'}">教育附加</th>
							<th lay-data="{field:'HJ'}">合计</th>
						</tr>
					</thead>
					<tbody id="ttbody">
					
					</tbody>
				</table>
				<div id="page"></div>

			</fieldset>

		</div>
		</form>
	</body>

	<script>
	var wait;
		function showLoad() {
		    return layer.msg('拼命加载数据中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:100000});
		}
		
		function closeLoad(index) {
		    layer.close(index);
		}
		function showSuccess() {
		    layer.msg('查询成功！',{time: 1000,offset: 'auto'});
		}
	
		layui.use(['form', 'layedit', 'laydate','laypage'], function() {
			var form = layui.form,
				layer = layui.layer,
				layedit = layui.layedit,
				laydate = layui.laydate,
				laypage=layui.laypage;
			//年月范围
			laydate.render({
				elem: '#yearNmonth',
				type: 'month',
				range: true
			});
			 var date=new Date;
			 var year=date.getFullYear(); 
			 var month=date.getMonth()+1;
			 month =(month<10 ? "0"+month:month); 
			 var mydate = (year.toString()+"-"+month.toString())+" - "+(year.toString()+"-"+month.toString());
			 
			 $("#yearNmonth").val(mydate);
			 showLoad();
			 getData();
			
			form.render(null, 'test1'); //更新全部
			laypage.render({
			    elem: 'page'
			    ,count: 100
			    ,theme: '#1E9FFF'
			});
			
			form.on('submit(button)', function(data) {
				pageNo = 1; //当点击搜索的时候，应该回到第一页
				getData();
				return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
			});
			 
			form.on('submit(exportExcel)', function(data) {
				window.location.href="export.do?ctrl=fhycx_exportData&"+$("#form1").serialize();
				
			});	
			
			form.on('radio(type)', function (data) {        
			    console.log(data);
			    $("#cxlx").val(data.value);
		    });
			form.on('radio(type1)', function (data) {        
			    console.log(data);
			    $("#sfhh").val(data.value);
		    });
			
			
			ajax({
				url : "ajax.do?ctrl=aqycx_queryInit",
				data : {},
				type : 'post',
				success : function(obj) {
					//	aui.alert(obj);
					if (obj != null && obj.data != null) {
						//拼接街道 下拉选框
						var str = "<option value='%'>请选择</option>";
						var jdlist = obj.data.jdlist;
						var hylist = obj.data.hylist;
						var zdsylist = obj.data.zdsylist;
						for (var i = 0; i < jdlist.length; i++) {
							str += "<option value='"+jdlist[i].JD_DM+"'>"
									+ jdlist[i].JD_MC + "</option>";
						}
						$("#jdlist").html(str);
						form.render('select');

						//拼接行业下拉选框 
						str = "<option value='%'>请选择</option>";
						for (var i = 0; i < hylist.length; i++) {
							str += "<option value='"+hylist[i].HYML_DM+"'>"
									+ hylist[i].HYML_MC
									+ "</option>";
						}
						$("#hylist").html(str);
						form.render('select');
					
					}
				}
			});
		});

		var pageNo = 1;
		var pageSize = 10;	
		var count = 0;
		
		function getData() {
			wait = layer.load();
			ajax({
				url : "ajax.do?ctrl=fhycx_queryData&pageNo=" + pageNo+ "&pageSize=" + pageSize,
				data : {
					date : $("#yearNmonth").val(),
					jd : $("#jdlist").val(),
					nsName : $("#nsName").val(),
					zdsyh : $("#zdsyh").val(),
					tjkj : $("#tjkj").val(),
					sortname : $("#sortname").val(),
					px : $("#px").val(),
					qyxz : $("#qyxz").val(),
					hylist : $("#hylist").val(),
					type : $("#cxlx").val(),
					islp : $("#sfhh").val(),
				},
				type : 'post',
				dataType : "Json",
				success : function(obj) {
					layer.close(wait);
					if (obj != null && obj.data != null) {
						getTbale(obj.data);//拼接表格
						count = obj.count;//数据总条数					
						queryPage();
						console.log(obj.data);
						showSuccess();
					}
				}
			});
		}

		//初始化表格
		function getTbale(data) {
			var s = "";
			$.each(data, function(v, o) {
				/* 
					HYDM=12,HYMC=租赁和商务服务业，ZZS=804768.7,  YGZZZS=38123781.83,YYS=0,
					QYSDSG=42864728.28,QYSDSD=0, QYSDSH=42864728.28,GRSDS=0， 
					FCS=0,YHS=0, CCS=0,XJ=81793278.81,CSWHJSS=0, DFJYFJ=0, JYFJ=0,  HJ=81793278.81,
					*/
				s += '<tr>';
				s += '<td>' + o.HYDM + '</td>';
				s += '<td>' + (o.HYMC==null?" - ":o.HYMC) + '</td>';			
				s += '<td>' + o.ZZS + '</td>';
				s += '<td>' + o.YGZZZS + '</td>';					
				s += '<td>' + o.YYS + '</td>';
				s += '<td>' + o.QYSDSG + '</td>';
				s += '<td>' + o.QYSDSD + '</td>';
				s += '<td>' + o.QYSDSH + '</td>';				
				s += '<td>' + o.GRSDS + '</td>';				
				s += '<td>' + o.FCS + '</td>';
				s += '<td>' + o.YHS + '</td>'; 
				s += '<td>' + o.CCS + '</td>';
				s += '<td>' + o.XJ + '</td>';
				s += '<td>' + o.CSWHJSS + '</td>';				
				s += '<td>' + o.DFJYFJ + '</td>';
				s += '<td>' + o.JYFJ + '</td>';
				s += '<td>' + o.HJ + '</td>';
				s += '</tr>';
			});
			$("#ttbody").html(s);
			//执行渲染
			layui.use([ 'table' ], function() {
				layui.table.init('user', {
					height : 480,
					limit : pageSize
				//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
				//支持所有基础参数
				});
				$('.layui-table:eq(1) thead tr th:eq(0)').addClass('layui-hide');
			});
			if (data == null || data.length <= 0) {
				$("#page").hide();
			} else {
				$("#page").show();
			}
		}
		//分页方法
		function queryPage() {
			layui.use([ 'laypage' ], function() {
				laypage = layui.laypage;
				laypage.render({
					//注意，这里的page1 是 ID，不用加 # 号
					elem : 'page',
					//数据总数，从服务端得到
					count : count,
					//每页显示条数
					limit : 20,
					//条数列表
					limits : [ 20, 30, 50 ],
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