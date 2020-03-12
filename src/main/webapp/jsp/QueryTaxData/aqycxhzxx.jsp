<%-- <%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> --%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

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
	<title>按企业查询汇总信息</title>
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
<input type="hidden" id="dwid" value="${dwid}" />
<form class="layui-form" id="form1" action="">
	<div class="layui-form" lay-filter="test1">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md12">
							<div class="layui-row layui-col-space10">
								<div class="layui-col-md8">
									<div class="layui-inline">
										<label class="layui-form-label">年月范围:</label>
										<div class="layui-input-inline">
											<input type="text" class="layui-input" id="yearNmonth" name="yearNmonth" placeholder="请选择日期">
										</div>
									</div>
								</div>
								<!-- <div class="layui-col-md4">
										<label class="layui-form-label">街道:</label>
										<div class="layui-input-block">
											<select name="jdlist" lay-filter="jdlist" id="jdlist">
												<option value="%">请选择</option>
											</select>
									</div>
								</div> -->
								<div class="layui-col-md4">
									<div class="layui-row">
										<div class="layui-col-md5">&nbsp;</div>
										<div class="layui-col-md7">
											<div class="layui-form-item">
												<div class="layui-input-block">
													<input type="radio" name="type" lay-filter="type" value="0"title="合计" checked="">
													<input id="cxls" name="cxls" type="hidden" value="0"/>
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
											<input type="text" name="nsName" lay-verify="title" id="nsName" autocomplete="off" placeholder="模糊查询" class="layui-input">
										</div>
									</div>
								</div>
								<div class="layui-col-md4">
									<div class="layui-form-item">
										<div class="layui-row">
											<div class="layui-col-md7">
												<label class="layui-form-label">排序:</label>
												<div class="layui-input-block">
													<select name="sortname" style="width: 40%;" id="sortname" >
														<option value="ZSE">合计</option>
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
														<option value="CZTDSYS">城镇土地使用税</option>
														<option value="HBS">环保税</option>
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
													<select name="qyxz" style="width: 30%;" id="qyxz" lay-filter="qyxz" >
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

												<input type="hidden" name="sfhh" id="sfhh"  value="0">
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
											<select name="tjkj" style="width: 30%;" id="tjkj" lay-filter="tjkj" >
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
														<button class="layui-btn layui-btn-normal" id="exportExcel" type="button" lay-submit="" lay-filter="exportExcel">导出</button>
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
					<th lay-data="{field:'NSRMC',width:'18%',fixed: 'left'}">纳税人名称</th>
					<th lay-data="{field:'JD_MC',width:'9%',fixed: 'left'}">所属街道</th>

					<th lay-data="{field:'ZZS',width:'9%',align:'right'}">增值税</th>
					<th lay-data="{field:'YGZZZS',width:'9%',align:'right'}">"营改增"增值税</th>
					<th lay-data="{field:'YYS',width:'9%',align:'right'}">营业税</th>
						<th lay-data="{field:'QYSDS_GS'}">企业所得税（国税）</th>
                        <th lay-data="{field:'QYSDS_DS'}">企业所得税（地税）</th>
					<th lay-data="{field:'QYSDS',width:'9%',align:'right'}">企业所得税（合计）</th>
					<th lay-data="{field:'GRSDS',width:'9%',align:'right'}">个人所得税</th>
					<th lay-data="{field:'FCS',width:'9%',align:'right'}">房产税</th>
					<th lay-data="{field:'YHS',width:'9%',align:'right'}">印花税</th>
					<th lay-data="{field:'CCS',width:'9%',align:'right'}">车船税</th>
					<th lay-data="{field:'DFJYFJ',width:'9%',align:'right'}">地方教育附加</th>
					<th lay-data="{field:'JYFJ',width:'9%',align:'right'}">教育附加</th>

					<th lay-data="{field:'CSWHJSS',width:'9%',align:'right'}">城市维护建设税</th>
					<th lay-data="{field:'CZTDSYS',width:'9%',align:'right'}">城镇土地使用税</th>
					<th lay-data="{field:'HBS',width:'4.5%',align:'right'}">环保税</th>
					<th lay-data="{field:'HJ',width:'13%',align:'right',fixed: 'right'}">合计</th>
					<th lay-data="{field:'RKRQ',style:'display:none;'}">入库日期</th>
					<th lay-data="{field:'NOFJ',width:'13%',align:'right',fixed: 'right'}">合计(不含附加税)</th>


				</tr>
				</thead>
				<tbody id="ttbody">

				</tbody> 
			</table>
		</fieldset>
	</div>
</form>
</body>

<script>

	var wait;
	var dwid=$("#dwid").val();
	$(function() {
		$("#table").attr("lay-data",
				"{width:" + document.body.clientWidth + "}");
	});

	function showLoad() {
		return layer.msg('拼命加载数据中...', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: 'auto', time:100000});
	}

	function closeLoad(index) {
		layer.close(index);
	}
	function showSuccess() {
		layer.msg('查询成功！',{time: 1000,offset: 'auto'});
	}

	//查询数据库的最大月份
	getMaxData();
	function getMaxData() {
		$.ajax({
			type : "post", //请求方式
			async : true, //是否异步
			 url : "aqycxhzxx/queryZdny.do",
			data : "",
			dataType : "json",
			success : function(obj) {
				if (obj.code == "000") {
					var r = obj.data[0];
					//日期条件默认值为数据库中数据的最大入库日期
					var year = r.RKRQ.substring(0,4);
					var month = r.RKRQ.substring(4,6);
					var rr = year + "-" + month;
					$("#yearNmonth").val(rr + " - " + rr);
				}
			}
		})
	}

	layui.use(['form', 'layedit', 'laydate', 'laypage'], function() {
		var form = layui.form,
				layer = layui.layer,
				layedit = layui.layedit,
				laypage = layui.laypage,
				laydate = layui.laydate;
		//年月范围
		laydate.render({
			elem: '#yearNmonth',
			type: 'month',
			range: true
		});

		laypage.render({
			elem: 'page',
			count: 100,
			theme: '#1E9FFF'
		});

		form.on('submit(button)', function(data) {
			pageNo = 1; //当点击搜索的时候，应该回到第一页
			queryinit();			
			return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
		});
		
		function queryinit(e){
			layui.use('table', function(){
		        var table = layui.table;
		        //表单序列化数组
		    	var index = layer.load(2);
		        //第一个实例
		        table.render({
		          elem: '#table'
		          ,height: 500
		          ,width:document.body.clientWidth//设置宽度为body宽度
		          ,method : 'post'
		          ,url:"aqycxhzxx/queryAqyhzxxhz.do"
		          ,cellMinWidth: 120
		          ,where: {
		        	  	date: $("#yearNmonth").val(),//时间
						//jd: $("#jdlist").val(),//街道代码
						nsName: $("#nsName").val(),//纳税人名称
						zdsyh: $("#zdsyh").val(),			//重点税源户
						tjkj: $("#tjkj").val(),//统计口径
						sortname: $("#sortname").val(),//纳税名字
						px: $("#px").val(),//排序
						qyxz: $("#qyxz").val(),//企业性质
						hylist: $("#hylist").val(),//行业代码
						type:$("#cxls").val(),//是否按月统计
						islp:$("#sfhh").val(),//是否合伙
			       }
		          ,limit:10
		          ,page: true //开启分页
		          ,cols: [[ //表头
		        	{field:'NSRMC',title: '纳税人名称 ',width:'18%',fixed: 'left'}, 
					{field:'JD_MC',title: '所属街道 ',width:'9%',fixed: 'left'} ,
					{field:'ZZS',title: '增值税',width:'9%',align:'right'}  ,
					{field:'YGZZZS',title: '"营改增"增值税 ',width:'9%',align:'right'} ,
					{field:'YYS',title: '营业税 ',width:'9%',align:'right'} ,
					{field:'QYSDS_GS',title: '企业所得税（国税）'}  ,
	                {field:'QYSDS_DS',title: '企业所得税（地税）'}  ,
					{field:'QYSDS',title: '企业所得税（合计）',width:'9%',align:'right'}  ,
					{field:'GRSDS',title: '个人所得税',width:'9%',align:'right'}  ,
					{field:'FCS',title: '房产税 ',width:'9%',align:'right'} ,
					{field:'YHS',title: '印花税',width:'9%',align:'right'} , 
					{field:'CCS',title: '车船税',width:'9%',align:'right'}  ,
					 {field:'DFJYFJ',title: '地方教育附加',width:'9%',align:'right'}  ,
					 {field:'JYFJ',title: ' 教育附加',width:'9%',align:'right'} ,
					 {field:'CSWHJSS',title: '城市维护建设税',width:'9%',align:'right'}  ,
					 {field:'CZTDSYS',title: '城镇土地使用税',width:'9%',align:'right'} , 
					 {field:'HBS',title: '环保税',width:'4.5%',align:'right'} , 
					 {field:'HJ',title: '合计',width:'13%',align:'right',fixed: 'right'} , 
					 {field:'RKRQ',title: '入库日期 ',style:'display:none;'},
					 {field:'NOFJ',title: '合计(不含附加税)',width:'9%',align:'right',fixed: 'right'} 
		          ]]
		        ,done : function(res, curr, count){
	      		 	layer.close(index);
	        	}
		        });
		        
		      });
			
		}

		form.on('submit(exportExcel)', function(data) {
			debugger
			if(count<1){
				layer.msg("无数据")
				return;
			}
			window.location.href="export.do?ctrl=aqycxhzxx_exportData&"+$("#form1").serialize();
		});

		form.on('radio(type)', function (data) {
			console.log(data);
			$("#cxls").val(data.value);
		});
		form.on('radio(type1)', function (data) {
			console.log(data);
			$("#sfhh").val(data.value);
		});

		form.render(null, 'test1'); //更新全部

		ajax({
			url:"aqycxhzxx/queryAqyInit.do",
			data:{},
			type:'post',
			success:function(obj){
				if(obj!=null&&obj.data!=null){
					var str="<option value='%'>请选择</option>";
					var jdlist=obj.data.jdlist;
					var hylist=obj.data.hylist;
					var zdsylist=obj.data.zdsylist;
					if (dwid == "00") {
						for (var i = 0; i < jdlist.length; i++) {
							str += "<option value='" + jdlist[i].JD_DM + "'>" + jdlist[i].JD_MC + "</option>";
						}
					}else {
						for (var i = 0; i < jdlist.length; i++) {
							str= "<option value='"+jdlist[i].JD_DM+"'>" + jdlist[i].JD_MC + "</option>";
						}
					}
					$("#jdlist").html(str);
					form.render('select');

					//拼接行业下拉选框
					str="<option value='%'>请选择</option>";
					for (var i = 0; i < hylist.length; i++) {
						str+="<option value='"+hylist[i].HYML_DM+"'>"+hylist[i].HYML_MC+"</option>";
					}
					$("#hylist").html(str);
					form.render('select');

				}
			}
		});
	});





	var pageNo = 1;
	var pageSize = 10;
	var count=1;
	function getData() {
		wait = layer.load();
		ajax({
			url : "aqycxhzxx/queryAqyhzxxhz.do?page=" + pageNo+ "&limit=" + pageSize,
			data :{
				date: $("#yearNmonth").val(),//时间
				jd: $("#jdlist").val(),//街道代码
				nsName: $("#nsName").val(),//纳税人名称
				zdsyh: $("#zdsyh").val(),			//重点税源户
				tjkj: $("#tjkj").val(),//统计口径
				sortname: $("#sortname").val(),//纳税名字
				px: $("#px").val(),//排序
				qyxz: $("#qyxz").val(),//企业性质
				hylist: $("#hylist").val(),//行业代码
				type:$("#cxls").val(),//是否按月统计
				islp:$("#sfhh").val(),//是否合伙

			},
			type : 'post',
			dataType:"Json",
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
			s += '<tr>';
			s += '<td>' + o.NSRMC + '</td>';
			s += '<td>' + o.JD_MC + '</td>';

			s += '<td>' + o.ZZS + '</td>';
			s += '<td>' + o.YGZZZS + '</td>';
			s += '<td>' + o.YYS + '</td>';
			s += '<td>' + o.QYSDS + '</td>';
			s += '<td>' + o.GRSDS + '</td>';
			s += '<td>' + o.FCS + '</td>';
			s += '<td>' + o.YHS + '</td>';
			s += '<td>' + o.CCS + '</td>';
			s += '<td>' + o.DFJYFJ + '</td>';
			s += '<td>' + o.JYFJ + '</td>';
			s += '<td>' + o.CSWHJSS + '</td>';
			s += '<td>' + o.CZTDSYS + '</td>';
			s += '<td>' + o.HBS + '</td>';
			s += '<td>' + o.HJ + '</td>';
			s += '<td>' + o.RK_RQ + '</td>';
			s += '<td>' + o.NOFJ + '</td>';
			s += '</tr>';
		});
		$("#ttbody").html(s);
		//执行渲染
		layui.use([ 'table' ], function() {
			layui.table.init('user', {
				height : 480,
				limit : pageSize+2
				//注意：请务必确保 limit 参数（默认：10）是与你服务端限定的数据条数一致
				//支持所有基础参数
			});
			/* if ($("#cxls").val()==1) {
				$('.layui-table:eq(1) thead tr th:eq(15)').show();
				$('.layui-table:eq(2) tbody tr').find('td:eq(15)').show();
			}else{
				$('.layui-table:eq(1) thead tr th:eq(15)').hide();
				$('.layui-table:eq(2) tbody tr').find('td:eq(15)').hide();
			} */
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
				limit : pageSize,
				//条数列表
				limits : [ 10, 20, 30, 50 ],
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