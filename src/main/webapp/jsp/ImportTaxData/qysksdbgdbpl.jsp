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
<title>企业税款属地变更(单户批量)</title>
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<style type="text/css">
.layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
}

  .layui-form-label{
            width: 100px;
        }
  .layui-input-block {
            margin-left: 130px;
            min-height: 36px
        }
</style>
</head>

<body style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">本功能页面用于单户的属地变更与金额拆分！</blockquote>
	<div class="layui-form" lay-filter="test1">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<h2 class="layui-colla-title">查询条件：</h2>
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md3">
							<label class="layui-form-label">企业名称:</label>
							<div class="layui-input-block">
								<input type="text" class="layui-input" id="qymc" name="qymc"
									placeholder="">
							</div>
						</div>
						<div class="layui-col-md3" style="margin-left: 123px;">
              <label class="layui-form-label">纳税人识别号:</label>
              <div class="layui-input-block">
                <input type="text" class="layui-input" id="nsrsbh" name="nsrsbh"
                  placeholder="">
              </div>
            </div>
            <div class="layui-col-md3" style="margin-left: 110px;">
              <label class="layui-form-label">月份:</label>
              <div class="layui-input-block">
                <input type="text" class="layui-input" name="cxyf" id="test8"
                  placeholder="">
              </div>
            </div>
          </div>
          <div class="layui-row" style="margin-top: 20px">
						
						<div class="layui-col-md3">
							<div class="layui-form-item">
								<label class="layui-form-label">税种:</label>
								<div class="layui-input-block">
									<select name="zsxm_dm" id="zsxm_dm" style="width: 30%;">
										<option value="">全部</option>
										<option value="增值税">增值税</option>
										<option value="营改增增值税">营改增增值税</option>
										<option value="营业税">营业税</option>
										<option value="企业所得税">企业所得税</option>
										<option value="个人所得税">个人所得税</option>
										<option value="车船税">车船税</option>
										<option value="房产税">房产税</option>
										<option value="印花税">印花税</option>
										<option value="城市维护建设税">城市维护建设税</option>
										<option value="地方教育附加">地方教育附加</option>
										<option value="教育附加">教育附加</option>
										<option value="环保税">环保税</option>
									</select>
								</div>
							</div>
						</div>
						<div class="layui-col-md3" style="margin-left: 123px;">
							<div class="layui-form-item">
								<label class="layui-form-label">街道:</label>
								<div class="layui-input-block">
									<select name="jdlist" lay-filter="aihao" id="jdlist">
									</select>
								</div>
							</div>
						</div>
						<div class="layui-col-md3" style="text-align: right; margin-left: 110px">
							<div class="layui-inline">
								<div class="layui-btn-group">
									<button class="layui-btn layui-btn-normal" id="button"
										type="button" lay-submit="" lay-filter="button">查 询</button>
								</div>
								
                				<div class="layui-btn-group">
	                  				<button class="layui-btn layui-btn-normal" id="qbcf" type="button" lay-submit="" lay-filter="qbcf">批量拆分</button>
                				</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
			<legend style="font-size: 12px;">单位：万元</legend>

			<table class="layui-table" id="table" lay-filter="user" >

			</table>
			
			<script type="text/html" id="bar">
				
				{{# if((d.QXJ == 1 || d.QXJ == 2) && d.HFID != "" && d.HFID != null && d.HFID != undefined ){ }}
          		<a class="layui-btn layui-btn-xs" lay-event="hf">恢复原始记录</a>
        		{{# } }}
				
			</script>
			<script type="text/html" id="bg">
				{{# if(d.QXJ!=""&&d.QXJ!=null&&d.QXJ!=undefined){ }}
  				已变更
				{{# } }}
				{{# if(d.QXJ==""||d.QXJ==null||d.QXJ==undefined){ }}
  				未变更
				{{# } }}
			</script>
			<div id="table-qy-div">
				<table class="layui-table" id="table-qy" lay-filter="table-qy">
	
	      </table>
      </div>
      <script type="text/html" id="select-bar">
          <a class="layui-btn layui-btn-xs" lay-event="select-qymc">选择</a>
      </script>
      <div id="cf-area" style="margin:10px;">
				<table class="layui-table" id="selecttable" hidden="hidden" lay-filter="selecttable">
	        <thead>
						<tr>
							<th lay-data="{field:'qy',width:320}">企业名称</th>
							<th lay-data="{field:'sz',width:120}">税种</th>
							<th lay-data="{field:'jd',width:120}">街道</th>
							<th lay-data="{field:'ze',width:120}">总税额</th>
						</tr>
					</thead>
					<tbody id="ttbody">
						
					</tbody>
				</table>
				<form class="layui-form" id="form" action="">
					<table class="layui-table" id="inserttable" hidden="hidden" lay-filter="inserttable">
		        <thead>
							<tr>
								<th style="text-align:center" lay-data="{field:'qy'}">街道名称</th>
								<th style="text-align:center" lay-data="{field:'jd'}">拆分金额</th>
								<th style="text-align:center" lay-data="{field:'ze'}">操作</th>
							</tr>
						</thead>
						<tbody id="insertbody">
							
						</tbody>
					</table>
				</form>
			</div>
		</fieldset>

	</div>
	<div id="xg" style="display: none;">
		<form class="layui-form" id="form6" action=""
			style="margin-top: 20px;">
			<div class="layui-form-item">
				<label class="layui-form-label">街道名称</label>
				<div class="layui-input-block" style="width: 65%;">
					<select name="jd" id="jd">
						<option value=""></option>

					</select>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-input-block" style="margin-left: 0px;text-align:center;">
					<button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
				</div>
			</div>
		</form>
	</div>
	
	<div id="qb" style="display: none;">
		<form class="layui-form" id="form7" action=""
			style="margin-top: 20px;">
		<div class="layui-form-item" style="margin-bottom: 142px;">
        	<label class="layui-form-label">税种</label>
        	<div class="layui-input-block" style="width: 65%;height: 1%;">
          		<input type="checkbox" lay-skin="primary"  id="c_all" lay-filter="c_all" title="全选/全不选">
          		<input type="checkbox" name="sz" title="增值税" lay-skin="primary" value="增值税" lay-filter="c_one" class="sz">
	          	<input type="checkbox" name="sz" title="营改增增值税" lay-skin="primary" value="营改增增值税" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="营业税" lay-skin="primary" value="营业税" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="企业所得税" lay-skin="primary" value="企业所得税" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="个人所得税" lay-skin="primary" value="个人所得税" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="车船税" lay-skin="primary" value="车船税" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="房产税" lay-skin="primary" value="房产税" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="印花税" lay-skin="primary" value="印花税" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="城市维护建设税" lay-skin="primary" value="城市维护建设税" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="地方教育附加" lay-skin="primary" value="地方教育附加" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="教育附加" lay-skin="primary" value="教育附加" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="城镇土地使用税" lay-skin="primary" value="城镇土地使用税" lay-filter="c_one" class="sz"> 
	          	<input type="checkbox" name="sz" title="环保税" lay-skin="primary" value="环保税" lay-filter="c_one" class="sz"> 
        	</div>
      	</div>
			
			<div class="layui-form-item" style="margin-bottom: 142px;">
				<label class="layui-form-label">街道名称</label>
				<div class="layui-input-block" style="width: 65%;">
					<select name="cfjd" id="cfjd">
						<option value=""></option>

					</select>
					<button class="layui-btn" lay-submit lay-filter="formQB" style="margin-left: 35%;
    margin-top: 4%;">立即提交</button>
				</div>
			</div>
			
		</form>
	</div>
	
	<script>
	
	 	function inputNum(obj){
		    // 清除"数字"和"."以外的字符
		    obj.value = obj.value.replace(/[^\d.]/g,"");
		    // 验证第一个字符是数字
		    obj.value = obj.value.replace(/^\./g,"");
		    // 只保留第一个, 清除多余的
		    obj.value = obj.value.replace(/\.{2,}/g,".");
		    obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		    // 只能输入两个小数
		    obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');
		  }
		
		var jdlist;
		var zsee=0;
		var sz="";
		var nsrmc;
		var temp="";
		var xhs=[];
		var pllayer;
		var loadlayer;
		
		layui.use([ 'form', 'layedit', 'laydate' ], function() {

			var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate
			
			form.on('checkbox(c_all)', function (data) {
	            var a = data.elem.checked;
	            if (a == true) {
	                $(".sz").prop("checked", true);
	                form.render('checkbox');
	            } else {
	                $(".sz").prop("checked", false);
	                form.render('checkbox');
	            }
	        });
			
			//年月范围
			laydate.render({
				elem : '#test8',
				type : 'month',
				format : 'yyyyMM',
				range:'-'
			});
			
			//var date = new Date;
			//var year = date.getFullYear();
			//var month = date.getMonth() + 1;
			//month = (month < 10 ? "0" + month : month);
			//var mydate = (year.toString() + "" + month.toString())+" - "+ (year.toString() + "" + month.toString());

			ajax({
				url : "dh/queryInit.do",
				data : {id:'1'},
				type : 'post',
				success : function(obj) {
					if (obj != null && obj.data != null) {
						var str = "<option value=''>请选择</option>";
						jdlist = obj.data.jdlist;
						var hylist = obj.data.hylist;
						for (var i = 0; i < jdlist.length; i++) {
							str += "<option value='"+jdlist[i].JD_DM+"'>"
									+ jdlist[i].JD_MC
									+ "</option>";
						}
						$("#jdlist").html(str);
						$("#test8").val(obj.data.maxRkrq + ' - ' + obj.data.maxRkrq);
						//getData();
						//form.render('select');
			      form.render(null, 'test1'); //更新全部
					}
				}
			});
			
			form.on('submit(qbcf)', function(obj) {
				
				plgglayer = layer.open({
					type : 1,
					title : '批量修改',
					area : [ '700px', '80%' ],
					shadeClose : false, //点击遮罩关闭
					content : $('#qb'),
					success : function() {
						initjd();
					},
					cancel : function() {
						$('#qb').hide();

					},
					end : function() {
						$('#qb').hide();
					}
				});
				return false;
			});

			form.on('submit(button)', function() {
				
				loadlayer = layer.load();
      				layui.use('table', function(){
      					  var table = layui.table;
      					
      					
    					 table.render({
    						elem: '#table-qy',
    						height: 480,
    						method: 'post',
    						url: "dh/queryQymc.do",
    						where: {
    							"qymc" : $('input[name="qymc"]').val(),
    		          			"nsrsbh" : $('input[name="nsrsbh"]').val(),
    		          			"cxrq" : $("#test8").val()
    						},
    						page: true,
    						
    						cols:[[
    							{field : 'NSRMC',title : '企业名称',width:'450'}, 
    				        	{field : '',title : '操作',toolbar : '#select-bar',fixed : 'right',width:'100'}
    						]],
    						
    						limit: 10,
    						done : function(res, curr, count){
    							$(".layui-table-box").css("height", '437px');
    							$('.layui-laypage-default').show();
    			     		 	layer.msg('查询成功！', {
    			     				time : 1000,
    			     				offset : 'auto'
    			     			});
    			     		 	$("#table-qy-div").show();
    			     		 	layer.close(loadlayer);
    			     		 	selectQyLayer=layer.open({
    		        				type : 1,
    		        				title : '选择企业',
    		        				area : [ '610px', '92%' ],
    		        				id : 'qymcdh',
    		        				area: ['560','560'],
    		        				shadeClose : false, //点击遮罩关闭
    		        				content : $('#table-qy-div'),
    		        				success : function() {
    		      	  
    		        				},
    		        				cancel : function() {
    		          					$('#table-qy-div').hide();
    		          					layer.close('#table-qy-div');
    		        				},
    		        				end : function() {
    		          					$('#table-qy-div').hide();
    		          					layer.close('#table-qy-div');
    		        				}	
    		      				})
    			       		}
    					})
    					//方法级渲染
    	        		//监听工具条
    	        		table.on('tool(table-qy)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
    	          				var data = obj.data; //获得当前行数据

    	          				var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    	          				if (layEvent === 'select-qymc') { //选择企业
    	        	  				nsrmc = data.NSRMC;
    	            				getData(nsrmc);
    	            				//layer.close(selectQyLayer);
    	            				layer.closeAll('page'); 
    	            			$("#table-qy-div").hide();
    	          			}
    	        		})
      				
				return false; 
			}) 
		})
			
			
			form.on('submit(add)', function() {
				var s="";
			  	s += '<tr>';
	         
        		var str = "<option value=''>请选择</option>";
         
				for (var i = 0; i < jdlist.length; i++) {
					str += "<option value='"+jdlist[i].JD_DM+"'>"
							+ jdlist[i].JD_MC
							+ "</option>";
				}
				
		        s += '<td><select name="jdadd" id="jdadd">'+str+'</select></td>';
		        s += '<td><input type="text" onkeyup="inputNum(this)" class="layui-input" id="zseadd" name="zseadd" placeholder="请输入总税额"  ></td>';
		        s += '<td><button class="layui-btn layui-btn-danger layui-btn-xs" id="del" type="button" lay-submit="" lay-filter="del">删除</button></td>';
		        s += '</tr>';
		        $("#insertbody").append(s);
		        form.render();
        
				return false;
			});
			
			form.on('submit(del)', function(obj) {
				$(obj.elem.parentNode.parentNode).remove();
				return false;
			});
			
			form.on('submit(formDemo)', function(obj) {
				temp=temp.substring(0,temp.length-1);
				var temparr=temp.split(",");
				var resultlist=[];
				for (var i = 0; i < temparr.length; i++) {
					for (var j = 0; j < xhs.length; j++) {
						if (xhs[j].xh==temparr[i]) {
							resultlist.push(xhs[j]);
						}
					}
				}
				var jdmc = $("#jd").find("option:selected").text();
				var jd = $("#jd").val();
				ajax({
					url : "dh/plxg.do",
					data : {
						xh:JSON.stringify(resultlist),
						jdmc:jdmc,
						jddm:jd
					},
					type : 'post',
					success : function(obj) {
						if (obj != null && obj.code == "000") {
							layer.msg("修改成功！");
						}else{
							layer.msg("修改失败！");
						}
						getData(nsrmc);
						layer.close(wait);
					}
				});
				return false;
			});
			
			form.on('submit(formQB)', function(obj) {
				
				loadlayer = layer.load();
				var sz = new Array();
				$("input[name='sz']:checked").each(function(i){
					sz[i] = $(this).val();
            	});
				var jdmc = $("#cfjd").find("option:selected").text();
				var jd = $("#cfjd").val();
				ajax({
					url : "dh/qbcf.do",
					data : {
						nsrmc: nsrmc,
						jdmc:jdmc,
						jddm:jd,
						rkrq:$("#test8").val(),
						sz: sz.join(",")
					},
					type : 'post',
					success : function(obj) {
						if (obj != null && obj.code == "000") {
							layer.msg("修改成功！");
						}else{
							layer.msg("修改失败！");
						}
						layer.closeAll('page');
						getData(nsrmc);	
						layer.close(wait);
					}
				});
				return false;
			});
			
			form.on('submit(plgg)', function(obj) {
				if (temp == "") {
					layer.msg("请先勾选信息");
					return;
				}
				pllayer = layer.open({
					type : 1,
					title : '部分修改',
					area : [ '700px', '80%' ],
					shadeClose : false, //点击遮罩关闭
					content : $('#xg'),
					success : function() {
						initjd();
					},
					cancel : function() {
						$('#xg').hide();
					},
					end : function() {
						$('#xg').hide();
					}
				});
				return false;
			}); 
			
			form.on('submit(plcf)', function(obj) {
        		if (temp == "") {
          			layer.msg("请先勾选信息");
          			return;
        		}
        		
		        var xh_temp = "";
		        var nsrmc_temp = xhs[0].nsrmc;
		        var sz_temp = xhs[0].sz;
		        var jd_temp = xhs[0].jd;
		        var zse_temp = 0;
		        for(x in xhs) {
		        	if(xhs[x].nsrmc != nsrmc_temp) {
		            	layer.msg("请选择同一企业的数据");
		            	return;
			        }
		        	if(xhs[x].sz != sz_temp) {
		        		layer.msg("请选择同一税种的数据");
		            	return;
		        	}
		        	if(xhs[x].jd != jd_temp) {
		            	layer.msg("请选择同一街道的数据");
		            	return;
		          	}
		        	if(xhs[x].qxj) {
		        		layer.msg("请选择未变更的数据");
		            	return;
		        	}
	        		xh_temp += xhs[x].xh + ",";
	        		zse_temp += parseFloat(xhs[x].zse);
        		} 
        		
        		xh = xh_temp.substring(0, xh_temp.length - 1);
		        zsee = zse_temp.toFixed(2);
		        var s="";
		        s += '<tr>';
		        s += '<td>' + nsrmc_temp + '</td>';
		        s += '<td>' + sz_temp + '</td>';
		        s += '<td>' + jd_temp + '</td>';
		        s += '<td>' + zse_temp.toFixed(2) + '</td>';
		        s += '</tr>';
		        s += '<tr>';
		        s += '<td colspan="2" style="text-align:center;">拆分后税款信息</td>';
		        s += '<td><button class="layui-btn layui-btn-xs" id="add" type="button" lay-submit="" lay-filter="add">添加</button></td>';
		        s += '<td><button class="layui-btn layui-btn-xs" id="save" type="button" lay-submit="" lay-filter="plsave">保存</button></td>';
		        s += '</tr>';
		        $("#ttbody").html(s);
        
		        $("#selecttable").show();
		        $("#inserttable").show();
		        $("#insertbody").html('');
		        plcflayer = layer.open({
		          	type : 1,
		          	title : '拆分金额',
		          	area : [ '700px', '80%' ],
			        shadeClose : false, //点击遮罩关闭
			        content : $('#cf-area'),
			        success : function() {
			          	//initjd();
			        },
		         	cancel : function() {
			            $('#cf-area').hide();
			
			         },
	          		 end : function() {
	            		$('#cf-area').hide();
	          		 }
        		});
        		return false;
     		});
			
			form.on('submit(plsave)', function() {
		        var result = $("#form").serializeArray();
		        var arraystr = "[";
		        var sum = 0;
		        var xh_str = "";
		        for(x in xhs) {
		        	xh_str += xhs[x].xh + ",";
		        }
		        for (var i = 0; i < result.length; i++) {
		         	if (result[i].name=="jdadd") {
		         		if (result[i].value == "") {
							layer.msg("请选择街道！");
							return;
						}else{
							arraystr+="{jddm:\""+result[i].value+"\",";
						}
		           		
		        	}
		         	if (result[i].name=="zseadd") {
		         		if (result[i].value == "" && result[i].value == "0") {
		         			layer.msg("请输入金额！");
		         			return;
						} else {
							arraystr+="zse:\""+result[i].value+"\"},";
					        sum+=parseFloat(result[i].value);
						}
			         }
		        }
        		sum=parseFloat(sum.toFixed(2));
		        if (zsee < sum) {
		          layer.msg("拆分的总税额不能大于原总税额！");
		          return;
		        }
		        
		        arraystr=arraystr.substring(0,arraystr.length-1);
		        arraystr+="]";
		        var cxrq = $("#test8").val();
		        loadlayer = layer.load();
		        ajax({
		        	url : "dh/CF.do",
		          	data : {
								str : arraystr,
								sz : xhs[0].sz,
								nsrmc : nsrmc,
								cxrq : cxrq,
								xh : xh_str.substring(0, xh_str.length-1),
								total: sum
		          	},
		            type : 'post',
		            success : function(obj) {
			            layer.close(plcflayer);
			            layer.close(loadlayer);
			            xhs = [];
			            if (obj != null && obj.data != null) {
			              	var sjList = obj.data;
			              	getTbale(sjList);
			            }
			            if(obj != null && obj.code == "000"){
			            	layer.msg("拆分成功！");
			              getData(nsrmc);
			            }else{	
			            	layer.msg("拆分失败！");
			            }
          			}
        		})
        		return false;
      		})
			
			form.on('submit(save)', function() {
				var result=$("#form").serializeArray();
				var arraystr="[";
				var sum=0;
				for (var i = 0; i < result.length; i++) {
					if (result[i].name=="jdadd") {
						arraystr += "{jddm:\"" + result[i].value+"\",";
					}
					if (result[i].name == "zseadd") {
						arraystr += "zse:\"" + result[i].value + "\"},";
						sum += parseFloat(result[i].value);
					}
				}
				sum = parseFloat(sum.toFixed(2));
				if (zsee < sum) {
					layer.msg("拆分的总税额不能大于原总税额！");
					return;
				}
				
				arraystr=arraystr.substring(0,arraystr.length-1);
				arraystr += "]";
				var cxrq = $("#test8").val();
				loadlayer = layer.load();
				ajax({
					url : "dh/CF.do",
					data : {
						str: arraystr,
						sz: sz,
						nsrmc: nsrmc,
						cxrq: cxrq,
						xh: xh
					},
					type : 'post',
					success : function(obj) {
						layer.close(cflayer);
						layer.close(loadlayer);
						xhs = [];
						if (obj != null && obj.data != null) {
							var sjList = obj.data;						
							getTbale(sjList);
						}
						if(obj != null && obj.code == "000"){
							
							layer.msg("拆分成功！");
							getData(nsrmc);
						} else {
							
							layer.msg("拆分失败！");
						}
					}
				})
				return false;
			})
			
			
		})
		
		function initjd() {
			ajax({
				url : "dh/queryInits.do",
				data : {},
				type : 'post',
				dataType : "json",
				success : function(obj) {
					if (obj != null && obj.data != null) {
						//拼接街道 下拉选框
						var str = "";
						var jdlist = obj.data.jdlist;
						for (var i = 0; i < jdlist.length; i++) {
							str += "<option value='"+jdlist[i].JD_DM+"'>"
									+ jdlist[i].JD_MC + "</option>";
						}
						$("#jd").html(str);
						$("#cfjd").html(str);
						layui.form.render('select');

					}
				}
			})
		}

		// 恢复
		function gethf(nsrmc,rkrq,sz){
			loadlayer = layer.load();
			ajax({
				url : "dhpl/HF.do",
				data : {
					nsrmc:nsrmc,
					rkrq:rkrq,
					sz:sz
				},
				type : 'post',
				success : function(obj) {
					layer.close(loadlayer);
					if (obj != null && obj.data != null) {
						var sjList = obj.data;
						getTbale(sjList);
					}
					if(obj != null && obj.code == "000"){
						getData(nsrmc);
					}
				}
			});
		}
		
		var wait;
		//分页参数设置 这些全局变量关系到分页的功能
		var pageSize = 10;//每页显示数据条数
		var pageNo = 1;//当前页数
		var count = 0;//数据总条数
		function getData(nsrmc) {

			loadlayer = layer.load();
			wait = layer.load();

			var cxrq = $("#test8").val();
			
			var jd_dm = $("#jdlist").val();
			
			var qymc = $("#qymc").val();
			
			var zsxm_dm = $("#zsxm_dm").val();
			if (cxrq == undefined || cxrq == "") {
				layer.msg("请选择日期！");
				layer.close(wait);
				return;
			}
			 ajax({
				url : "dhpl/querySdbg.do",
				data : {
					"cxrq" : cxrq,
					"jd_dm" : jd_dm,
					"zsxm_dm" : zsxm_dm,
					"qymc" : nsrmc,
					"nsrsbh" : $('#nsrsbh').val()
				},
				type : 'post',
				dataType: "json",
				success : function(obj) {
					if (obj != null && obj.data != null) {
						var sjList = obj.data;
						getTbale(sjList);
					}
				}
				,error:function(data){   
					debugger;
	                if(data.responseText=='loseSession'){
	                    //在这个地方进行跳转 
	                	top.location.href='<%=basePath%>jsp/login.jsp';
	                }
	            }
			}) 
			
		}
		
		//初始化表格
		function getTbale(data) {
			//xhs.splice(0, xhs.length);//清空数组 
			//temp = "";
			//执行渲染
			layui.use([ 'table' ], function() {
				//第一个实例
				layui.table.render({
					elem : '#table',
					height : 500, //数据接口
					data : data,
					limit : 10,
					page : true,
					/* initSort: {
			              field: 'SFBGMC' //排序字段，对应 cols 设定的各字段名
			              ,type: 'desc' //排序方式  asc: 升序、desc: 降序、null: 默认排序
			        }, */
					cols : [ [ //表头
						{
							field : 'SFBGMC',
							templet : '#bg',
							title : '是否变更',
							//width:'150'
						}, {
							field : 'RK_RQ',
							title : '税款月份',
							//width:'150'
						}, {
							field : 'NSRMC',
							title : '企业名称',
							//width:'250'
						}, {
							field : 'ZSXM',
							title : '税种',
							//width:'150'
						}, {
							field : 'JD_MC',
							title : '街道',
							//width:'150'
						}, {
							field : 'ZSE',
							title : '总税额（全口径）',
							//width:'150'
						},{
							field : 'DFZSE',
							title : '总税额（地方口径）',
							//width:'150'
						}, {
							field : '',
							title : '操作',
							toolbar : '#bar',
							fixed : 'right',
							//width:'150'
						} 
					] ]
				}); 
				
				layui.table.on('checkbox(user)', function(obj) {
					
					if (obj.type=="all") {
						var arr= layui.table.cache.table;
						for (var i = 0; i < arr.length; i++) {
							var xh={};
							if (obj.checked) {
								if (temp.lastIndexOf(arr[i].XH)<0) {
									temp += arr[i].XH + ","
								}
								xh.xh = arr[i].XH;	
								xh.nsrmc = arr[i].NSRMC;
								xh.sz = arr[i].ZSXM;
								xh.jd = arr[i].JD_MC;
								xh.zse = arr[i].ZSE;
								xh.qxj = arr[i].QXJ;
								xhs.push(xh);
							}else{
								temp = temp.replace(arr[i].XH + ",", "");
								for(x in xhs) {
									if(xhs[x].xh == arr[i].XH) {
										xhs.splice(x, 1);
									}
								}
							}
						}
					}else{
						if (obj.checked) {	
							if (temp.lastIndexOf(obj.data.XH)<0) {
								temp += obj.data.XH + ","
							}
							var xh={};
							xh.xh = obj.data.XH;
							xh.nsrmc = obj.data.NSRMC;
							xh.sz = obj.data.ZSXM;
							xh.jd = obj.data.JD_MC;
              				xh.zse = obj.data.ZSE;
              				xh.qxj = obj.data.QXJ;
							xhs.push(xh);
						} else {
							temp = temp.replace(obj.data.XH + ",", "");
							for(x in xhs) {
                				if(xhs[x].xh == obj.data.XH) {
                  					xhs.splice(x, 1);
                				}
              				}
						}
					}
				}); 

        $("#selecttable").hide();
        $("#inserttable").hide();
          
				//方法级渲染
				//监听工具条
				layui.table.on('tool(user)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
					var data = obj.data; //获得当前行数据

					var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
					if (layEvent === 'cf') { //删除
						var s="";
						xh=data.XH;
						zsee=parseFloat(data.ZSE);
						sz=data.ZSXM;
						nsrmc=data.NSRMC;
						s += '<tr>';
						s += '<td>' + data.NSRMC + '</td>';
						s += '<td>' + data.ZSXM + '</td>';
						s += '<td>' + data.JD_MC + '</td>';
						s += '<td>' + data.ZSE + '</td>';
						s += '</tr>';
						s += '<tr>';
						s += '<td colspan="2" style="text-align:center;">拆分后税款信息</td>';
						s += '<td><button class="layui-btn layui-btn-xs" id="add" type="button" lay-submit="" lay-filter="add">添加</button></td>';
						s += '<td><button class="layui-btn layui-btn-xs" id="save" type="button" lay-submit="" lay-filter="save">保存</button></td>';
						s += '</tr>';
						$("#ttbody").html(s);
						
						$("#selecttable").show();
						$("#inserttable").show();
						$("#insertbody").html('');
						cflayer = layer.open({
	            			type : 1,
	            			title : '拆分金额',
	            			area : [ '700px', '80%' ],
	            			shadeClose : false, //点击遮罩关闭
	            			content : $('#cf-area'),
	            			success : function() {
	              				//initjd();
	            			},
	            			cancel : function() {
	              				$('#cf-area').hide();
	
	            			},
	            			end : function() {
	              				$('#cf-area').hide();
	            			}
	          			})
					} else if (layEvent === 'hf') { //编辑
						
						nsrmc = data.NSRMC;
						rkrq = data.RK_RQ;
						sz = data.ZSXM;
						
						gethf(nsrmc,rkrq,sz);
					} else if (layEvent === 'plhf') {
						//plhf();
					}
				});
				
				var ss = $(".laytable-cell-1-QXJ");
				for (var i = 0; i < ss.length; i++) {
					$(ss[i].parentNode).hide();
				}
			});
			layer.close(wait);
		} 
		
	</script>
</html>