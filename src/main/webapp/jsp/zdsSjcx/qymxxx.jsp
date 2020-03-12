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
			<blockquote class="layui-elem-quote layui-text">
				本功能用于对系统的“功能菜单”进行操作管理！
			</blockquote>
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
											<label class="layui-form-label">国、地税:</label>
											<div class="layui-input-block">
												<select name="gds" id="gds" style="width: 50%;">
													<option value="1">地税</option>
													<option value="0">国税</option>
												</select>
											</div>
										</div>
									</div>
									<div class="layui-col-md4">
										&nbsp;
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
											<div class="layui-input-inline">
												<input type="text" id="nsrmc" placeholder="请输入纳税人名称" class="layui-input">
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
										<div class="layui-inline">
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

			<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend style="font-size:12px;">单位：万元</legend>

				<table class="layui-table" id="table" lay-filter="user">
					<thead>
						<tr>
							<th style="text-align:center" lay-data="{field:'ROWNUM'}">企业名称</th>
							<th style="text-align:center" lay-data="{field:'GNDM'}">街道</th>
							<th style="text-align:center" lay-data="{field:'GNMC'}">行业</th>
							<th style="text-align:center" lay-data="{field:'PX'}">入库月份</th>
							<th style="text-align:center" lay-data="{field:'URL'}">注册地址</th>
							<th style="text-align:center" lay-data="{field:'LRRQ'}">生产经营地址</th>
						</tr>
					</thead>
					<tbody id="ttbody">
						
					</tbody>
				</table>
				<!-- <div id="demo2"></div> -->

			</fieldset>

		</div>
</body>

<script>
		var wait;
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
				url:"ajax.do?ctrl=qymxxx_queryInit",
				data:{},
				type:'post',
				success:function(obj){
					if(obj!=null&&obj.data!=null){
						var str="<option value='%'>请选择</option>";
						var jdlist=obj.data.jdlist;
						for (var i = 0; i < jdlist.length; i++) {
							str+="<option value='"+jdlist[i].JD_DM+"'>"+jdlist[i].JD_MC+"</option>";
						}
						$("#jdlist").html(str);
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
				var nsrmc=$("#nsrmc").val();
				var gds=$("#gds").val();
				window.location.href="export.do?ctrl=qymxxx_export&cxrq="
						+cxrq+"&jd_dm="+jd_dm+"&nsrmc="+nsrmc+"&gds="+gds;
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
			wait = layer.load();
			var cxrq=$("#test8").val();
			var jd_dm=$("#jdlist").val();
			var nsrmc=$("#nsrmc").val();
			var gds=$("#gds").val();
			showLoad();
			ajax({
				url:"ajax.do?ctrl=qymxxx_queryQymx",
				data:{
					"pageNo":pageNo,
					"pageSize":pageSize,
					"cxrq":cxrq,
					"jd_dm":jd_dm,
					"nsrmc":nsrmc,
					"gds":gds
				},
				type:'post',
				success:function(obj){
					layer.close(wait);
					console.log(obj);
					if(obj!=null&&obj.data!=null){
						var sjList=obj.data;
						getTbale(sjList);
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
	           s += '<tr><td>' + (v+1) + '</td>';
	           s += '<td>' + o.ZSXM_MC + '</td>';
	           s += '<td>' + o.SSBNYLJ + '</td>';
	           s += '<td>' + o.SNSSTQS + '</td>';
	           s += '<td>' + o.SSTBZJE + '</td>';
	           s += '<td>' + o.SSTBZJL + '</td>';
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

</html>