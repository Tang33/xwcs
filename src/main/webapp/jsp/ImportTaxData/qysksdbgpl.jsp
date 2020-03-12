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
<title>企业税款属地变更(自定义)</title>
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<style type="text/css">
/* .layui-table-cell .layui-form-checkbox[lay-skin="primary"] {
	top: 50%;
	transform: translateY(-50%);
} */
.layui-form-checkbox span {
  height: auto;
}
.label{
		    padding: 2px 5px;
		    background: #5FB878;
		    border-radius: 2px;
		    color: #fff;
		    display: block;
		    line-height: 20px;
		    height: 20px;
		    margin: 2px 5px 2px 0;
		    float: left;
		}
</style>
</head>

<body style="overflow-x: hidden">
	<blockquote class="layui-elem-quote layui-text">本功能页面用于对上传指定的纳税人批量变更属地！</blockquote>
	<div class="layui-form" lay-filter="test1">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
          <div class="layui-row">
						<div class="layui-col-md12">
						<div class="layui-col-md4" style="width: 6%;">
							<div class="layui-inline">
								<div class="layui-upload">
								  <button type="button" class="layui-btn layui-btn-normal" id="selectFile">选择文件</button>
								 <div id="moset" style="width:500px"></div>
								</div>
							</div>
						</div>
							<div class="layui-col-md4" style=" width: 35%;">
							<div class="layui-inline" style=" width: 100%;">
							<label class="layui-form-label" style=" width:18%;">查询入库日期：</label>
										<div class="layui-input-inline" >
											<input  style="width: 200px; " type="text" class="layui-input" id="drrq" name="drrq" placeholder="请选择日期">
										</div>
							
							</div>
							</div>
							<div class="layui-col-md4" style="text-align: center; width: 14%; float: right;">
								<div class="layui-inline">
									<button type="button" class="layui-btn" id="downmb">上传文件模板格式下载</button>
								</div>
							</div>
						</div>
					</div>
					<div class="layui-row" style="margin-top: 10px;">	
						<div class="layui-col-md12">
							<!-- <div class="layui-btn-group"> -->
							  	<button type="button" class="layui-btn" id="upload">开始上传</button>
								<button class="layui-btn layui-btn-normal" id="plgg" type="button" lay-submit="" lay-filter="plgg" style="margin-left: 8px">批量更改</button>
							<!-- </div> -->
						</div>
					</div>
					
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
			<legend style="font-size: 12px;">单位：万元</legend>

			<table class="layui-table" id="table" lay-filter="user">

			</table>
			<script type="text/html" id="bar">
				
{{# if(d.QXJ == 1 || d.QXJ == 2 ){ }}
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
      <div id="cf-area" style="margin:10px;">
				<table class="layui-table" id="selecttable" hidden="hidden" lay-filter="selecttable">
	        <thead>
						<tr>
							<th lay-data="{field:'qy',width:320}">企业名称</th>
							<th lay-data="{field:'sz',width:120}">税种</th>
							<th lay-data="{field:'jd',width:120}">街道</th>
							<th lay-data="{field:'ze',width:120}">总税源</th>
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
        <label class="layui-form-label">税种</label>
        <div class="layui-input-block" style="width: 65%;">
          <input type="checkbox" lay-skin="primary"  id="c_all" lay-filter="c_all" title="全选/全不选">
          <!-- <input type="checkbox" name="sz" id="boxid" lay-skin="primary" title="全选/全不选" value="全选/全不选" onclick="setAllNo()"/> -->
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
			<div class="layui-form-item">
        <label class="layui-form-label">街道名称</label>
        <div class="layui-input-block" style="width: 65%;">
          <select name="jd" id="jd">
            <option value=""></option>

          </select>
        </div>
      </div>
			<div class="layui-form-item">
				<div class="layui-input-block" style="width:65%; text-align:right;">
					<button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
				</div>
			</div>
		</form>
	</div>
	
	<div id="bcmbDiv" style="display: none;">
    <form class="layui-form" id="form6" action="" style="margin-top: 20px;">
      <input type="hidden" name="mbParams" id="mbParams">
      <input type="hidden" name="mbSz" id="mbSz">
      <input type="hidden" name="mbJd" id="mbJd">
      <div class="layui-form-item" style="text-align: center">
        <label class="layui-form-label">规则名称</label>
        <div class="layui-input-block">
          <input type="text" class="layui-input" id="mbmc" name="mbmc" placeholder="" style="width: 70%;">
        </div>
      </div>
      <div class="layui-form-item" style="text-align: center">
        <label class="layui-form-label">规则描述</label>
        <div class="layui-input-block">
          <textarea rows="" cols="" class="layui-textarea" id="mbms" name="mbms" placeholder="" style="width: 70%; height: 50%">
          </textarea>
        </div>
      </div>
      <div class="layui-form-item">
        <div class="layui-input-block" style="width:65%; text-align:right;">
          <button class="layui-btn" lay-submit lay-filter="mbSavebtn">立即提交</button>
        </div>
      </div>
    </form>
  </div>
	<script>
		var jdlist;
		var zsee = 0;
		var sz = "";
		var nsrmc;
		var temp = "";
		var xhs = [];
		var pllayer;
		var filename_p = "";
		var loadlayer;
		var nsrmcs;
		var rkrq = $('#drrq').val();


		getMaxData();
		function getMaxData(){	
			$.ajax({
				type : "post", //请求方式
				async : true, //是否异步
				url:"sssjgl/getNsDate.do",
				dataType : "json",
				success:function(obj) {
					if(obj.code == "000"){
						var r = obj.data[0];
						//日期条件默认值为数据库中数据的最大入库日期
						$("#drrq").val(r.RKRQ);
						
					}
				}
			})
		}
		
		
		layui.use('upload', function(){
		  var $ = layui.jquery
		  ,upload = layui.upload;
		  var listwjm=new Array();
		  //选完文件后不自动上传
		 uploadListIns= upload.render({
		    elem: '#selectFile'
		    ,url: 'qysksdbg/upload.do?lx=qysksdbd'
		    ,auto: false
		    //,multiple: true
		    ,exts: 'xlsx|xls'
		    ,bindAction: '#upload'
		    ,choose: function(obj){
				
				files = obj.pushFile();
				 obj.preview(function(index, file, result){
					 listwjm.push(file.name);
					 $("#moset").append('<a href="javascript:;" class="label"  id="'+index+'"><span>'+file.name+'</span><input type="hidden" name="'+file.name+'"/><i class="close">x</i></a>')
				 
				 	
					//删除
                    $(document).on("click", ".close", function () {
						$(this).parent().remove();
						var id=$(this).parent().attr('id');
                        delete files[id]; //删除对应的文件
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    });
				 
				 });
				 
				 
				 
				
			}
		    ,done: function(res) {
		    	loadlayer = layer.load();
		    	
		      var obj2=eval("("+res.data+")");
		      var filename=obj2[0].bdsrc
		      filename_p = filename;
		      if (res.code == "0") {
						$.ajax({
						  type : "post", //请求方式
						  async : true, //是否异步
						  url : "qysksdbgpl/doInput.do",
						  data : {
							  rkrq:$('#drrq').val(),
						    filename : filename
						  },
						  dataType : "json",
						  success : function(obj) {
						    if (obj.code == "000") {
						    	if(obj.data.length == 0) {
						    		layer.msg("上传企业无最大入库日期当月数据！");
						    	} else {
							    	temp = obj.data.arr;
							    	getTbale(obj.data.arr);
							    	nsrmcs = obj.data.nsrmc;
							    	
						    	}
						    	layer.close(loadlayer);
						    }else if(obj.code == "009"){
						    	layer.msg("查询失败");
						    	layer.close(loadlayer);
						    }
						    	
						  }
						});
		      }
	      }
		  });
		});
		
		layui.use([ 'form', 'layedit', 'laydate' ], function() {
			//全选	            
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
					elem : '#drrq',
					type : 'month',
					format : 'yyyyMM',
					
					
				});
				var date = new Date;
				var year = date.getFullYear();
				var month = date.getMonth() + 1;
	
				month = (month < 10 ? "0" + month : month);
				var mydate = (year.toString() + "" + month.toString()) + " - " + (year.toString() + "" + month.toString());
				$("#test8").val(mydate);
				form.render(null, 'test1'); //更新全部
				ajax({
					url : "qysksdbgpl/queryInit.do",
					data : {},
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
							//getData();
							form.render('select');
						}
					}
				});
	
				form.on('submit(button)', function() {
					getData();
					return false;
				});
				
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
			           s += '<td><input type="text"  class="layui-input" id="zseadd" name="zseadd" placeholder="请输入总税额"  ></td>';
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
				
				form.on('submit(mbSavebtn)', function(obj) {
					ajax({
            url : "qysksdbgpl/bcmb.do",
            data : {
            	mbmc: $('#mbmc').val(),
              params: $('#mbParams').val(),
              nsrmcs:nsrmcs,
              mbms:$('#mbms').val(),
              sz: $('#mbSz').val(),
              jd: $('#mbJd').val()
            },
            type : 'post',
            success : function(obj) {
              if(obj != null && obj.code == "000"){
                layer.msg("保存成功！");
                layer.close(bcmblayer);
                //getData();
              } else {
            	  layer.msg("保存失败！");
              }
            }
          });
          return false;
        });
				
				form.on('submit(formDemo)', function(obj) {
					loadlayer = layer.load();
					var jd = $("#jd").val();
					var sz = new Array();
					$("input[name='sz']:checked").each(function(i){
						sz[i] = $(this).val();
	            });
					ajax({
						url : "qysksdbgpl/PLXG.do",
						data : {
							rkrq:$('#drrq').val(),
							xh:JSON.stringify(temp),
							nsrmcs:nsrmcs,
							jddm: jd,
							sz: sz.join(",")
						
						},
						type : 'post',
						success : function(obj) {
							
	        	  			getData();
							if (obj != null && obj.code == "000") {
								
								$('#mbParams').val(obj.data.params);
								$('#mbSz').val(obj.data.sz);
								$('#mbJd').val(obj.data.jd);
								layer.confirm('修改成功！涉及纳税人 ' + obj.data.companyCount + ' 户, 涉及金额￥' + obj.data.totalAmount + '元！是否将本次操作存为规则？'
									, {icon: 3, title:'提示',area:['40%','40%'], btn: ['保存规则', '下次再说']}, function(index){
										bcmblayer = layer.open({
					            type : 1,
					            title : '保存规则',
					            area : [ '50%', '50%' ],
					            shadeClose : false, //点击遮罩关闭
					            content : $('#bcmbDiv')
					          }, function(index) {
					          });
									  layer.close();
									});  
								//layer.msg("修改成功！涉及纳税人 " + obj.data.companyCount + " 户, 涉及金额￥" + obj.data.totalAmount + "元！");
							}else{
								layer.msg("修改失败！");
							}
						
							//getData();
							layer.close(plgglayer);
							layer.close(loadlayer);
							//layer.closeAll()
						}
					});
					return false;
				});
				
				form.on('submit(plgg)', function(obj) {
					if(temp.length == 0) {
            layer.msg("没有可以修改的数据！");
            return false;
          }
					plgglayer = layer.open({
						type : 1,
						title : '批量修改',
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
				
				form.on('submit(save)', function() {
					var result=$("#form").serializeArray();
					var arraystr="[";
					var sum=0;
					for (var i = 0; i < result.length; i++) {
					
					if (result[i].name=="jdadd") {
						arraystr+="{jddm:\""+result[i].value+"\",xh:\""+xh+"\",";
					}
					if (result[i].name=="zseadd") {
						arraystr+="zse:\""+result[i].value+"\"},";
						sum+=parseFloat(result[i].value)
					}	
					}
					sum=parseFloat(sum.toFixed(2));
					if (zsee!=sum) {
						layer.msg("拆分的总税额有误！");
						return;
					}
					arraystr=arraystr.substring(0,arraystr.length-1);
					arraystr+="]";
					var cxrq = $("#test8").val();
					ajax({
						url : "qysksdbgpl/CF.do",
						data : {
						rkrq:$('#drrq').val(),
						str:arraystr,
						sz:sz,
						nsrmc:nsrmc,
						cxrq:cxrq
						},
						type : 'post',
						success : function(obj) {
							if (obj != null && obj.data != null) {
								var sjList = obj.data;
								getTbale(sjList);
							}
							if(obj != null && obj.code == "000"){
								getData();
							}
						}
					});
					return false;
				});
				
				
			});
		
		function initjd() {
			ajax({
				url : "qysksdbgpl/queryInit.do",
				data : {},
				type : 'post',
				success : function(obj) {
					//	aui.alert(obj);
					if (obj != null && obj.data != null) {
						//拼接街道 下拉选框
						var str = "";
						var jdlist = obj.data.jdlist;
						for (var i = 0; i < jdlist.length; i++) {
							str += "<option value='"+jdlist[i].JD_DM+"'>"
									+ jdlist[i].JD_MC + "</option>";
						}
						$("#jd").html(str);
						layui.form.render('select');

					}
				}
			});
		}

		// 恢复
		function gethf(xh){
			loadlayer = layer.load();
			ajax({
				url : "qysksdbgpl/HF.do",
				data : {
						rkrq:$('#drrq').val(),
					  nsrmcs:nsrmcs
				},
				type : 'post',
				success : function(obj) {
					if (obj != null && obj.data != null) {
						var sjList = obj.data;
						getTbale(sjList);
					}
					if(obj != null && obj.code == "000"){
						getData();
					}
				}
			});
		}
		
		
		var wait;
		//分页参数设置 这些全局变量关系到分页的功能
		var pageSize = 10;//每页显示数据条数
		var pageNo = 1;//当前页数
		var count = 0;//数据总条数
		function getData() {
			loadlayer = layer.load();
			$.ajax({
        type : "post", //请求方式
        async : true, //是否异步
        url : "qysksdbgpl/doInput.do",
        data : {
        	rkrq:$('#drrq').val(),
          filename : filename_p
        },
        dataType : "json",
        success : function(obj) {
          if (obj.code == "000") {
            temp = obj.data.arr;
            getTbale(obj.data.arr);
          }
          layer.close(loadlayer);
        }
      });
		}

		//初始化表格
		function getTbale(data) {
			wait = layer.load();
			//执行渲染
			layui.use([ 'table' ], function() {
				//第一个实例
				layui.table.render({
					elem : '#table',
					height :500, //数据接口
					limit : 80,
					data : data,
					page:true,
					cols : [ [ //表头
						{
							field : 'XH',
							title : '序号',
							//width:'100'
						}, {
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
							templet : '#nsrmc',
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
							field : 'ZSEDF',
							title : '总税额(地方)',
							//width:'150'
						}, {
							field : 'ZSE',
							title : '总税额(全口径)',
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
								xh.sz=arr[i].ZSXM;
								xhs.push(xh);
							}else{
								temp = temp.replace(arr[i].XH + ",", "");
							}
						}
					}else{
						if (obj.checked) {	
							if (temp.lastIndexOf(obj.data.XH)<0) {
							temp += obj.data.XH + ","
						}
							var xh={};
							xh.xh = obj.data.XH;
							xh.sz=obj.data.ZSXM;
							xhs.push(xh);
						} else {
							temp = temp.replace(obj.data.XH + ",", "");
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
						pllayer = layer.open({
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
					} else if (layEvent === 'hf') { //编辑
						xh=data.XH;
						gethf(xh);
					}
				});
				
				var ss = $(".laytable-cell-1-QXJ");
				for (var i = 0; i < ss.length; i++) {
					$(ss[i].parentNode).hide();
				}
			});
			layer.close(wait);
		} /* getTbale结尾 */
		
		$("#downmb")
		.click(
				function() {

					window
							.open("/xwcs/downgd.do?filePath=数据分析自定义模板（必须以纳税人名称和纳税人识别号开头）.xls");
				});
	</script>
</html>