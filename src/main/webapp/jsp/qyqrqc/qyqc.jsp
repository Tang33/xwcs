<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
<base href="<%=basePath%>">

<title>流程管理—企业迁出管理</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
<jsp:include page="../load.jsp"></jsp:include>
<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->

<style type="text/css">
.sweet-alert{
	z-index:99999999;
    border: 1px solid #eee;
	box-shadow: 0 0 3px #c7b6b6;
}
</style>
</head>

<body style="overflow-x: hidden">
		<blockquote class="layui-elem-quote layui-text">本功能用于对系统的“企业迁出”进行操作管理！</blockquote>
		<form class="layui-form" id="form" action="">
		<div class="layui-collapse" lay-filter="test">
			<div class="layui-colla-item">
				<div class="layui-colla-content  layui-show">
					<div class="layui-row">
						<div class="layui-col-md12">
							<div class="layui-row layui-col-space10">
								<div class="layui-col-md3">
									<label class="layui-form-label">纳税人名称</label>
									<div class="layui-input-block">
										<input id="paramNsrmc" name="paramNsrmc" type="text"
											placeholder="请输入纳税人名称" class="layui-input">
									</div>
								</div>
								<div class="layui-col-md4">
									<label class="layui-form-label" style="width: 100px">纳税人识别号</label>
									<div class="layui-input-inline">
										<input id="paramNsrsbh" name="paramNsrsbh" type="text"
											placeholder="请输入纳税人识别号" class="layui-input">
									</div>
									<div class="layui-input-inline"></div>
									<div class="layui-input-inline"></div>
									<button lay-submit class="layui-btn" id="search_btn"
										lay-filter="search_btn">查询</button>
									<button lay-submit class="layui-btn" id="add_btn"
										lay-filter="add_btn">发起任务</button>
								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
				<legend>查询显示</legend>

				<table class="layui-table" id="qrtable" lay-filter="qrtable">
				
				</table>
				<div id="page1"></div>
				<script type="text/html" id="bar">
          {{# if(d.FILEURL){ }}
  				  <a href="{{d.FILEURL}}" class="layui-btn layui-btn-normal layui-btn-sm" lay-event="downloadFile">附件下载</a>
          {{# } }}
          {{# if(d.STATUS == "1"){ }}
  				  <a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="checkDetail">审核详情</a>
          {{# } }}
          {{# if(${sessionScope.dwid == '00'} ){ }}
  				  <a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="remove">删除</a>
          {{# } }}
          {{# if(${sessionScope.dwid != '00'} && d.STATUS == "0"){ }}
  				  <a class="layui-btn layui-btn-normal layui-btn-sm" lay-event="check">审核</a>
          {{# } }}
				</script>
			</fieldset>
		</form>
		
		<div id="table-qy-div" class="layui-hide" style="padding: 10px;">
		  <form class="layui-form" id="qy_form" action="">
			  <div class="layui-row">
	        <div class="layui-col-md8">
	          <label class="layui-form-label">企业名称:</label>
	          <div class="layui-input-block">
	            <input type="text" class="layui-input" id="qymc" name="qymc"
	              placeholder="">
	          </div>
	        </div>
	        <div class="layui-col-md3" style="margin-left: 20px;">
	          <button lay-submit class="layui-btn" id="search_qy_btn" lay-filter="search_qy_btn">查询</button>
	        </div>
	      </div>
      </form>
      <table class="layui-table" id="table-qy" lay-filter="table-qy">

      </table>
    </div>
    <script type="text/html" id="select-bar">
      <a class="layui-btn layui-btn-xs" lay-event="select-qymc">选择</a>
    </script>
		
		<div id="addmain" class="layui-hide" style="padding: 10px;">
		  <form class="layui-form" id="add_form">
				<div class="layui-form-item">
          <label class="layui-form-label">纳税人名称</label>
          <div class="layui-input-block">
            <input type="text" id="nsrmc" name="nsrmc" class="layui-input" lay-verify="required">
          </div>
        </div>
				<div class="layui-form-item">
					<label class="layui-form-label">纳税人识别码</label>
					<div class="layui-input-block">
						<input type="text" id="nsrsbh" name="nsrsbh" class="layui-input" lay-verify="required">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label">所属街道</label>
					<div class="layui-input-block">
						<select id="ssjd" name="ssjd" lay-filter="ssjd" lay-verify="required">
						 <c:forEach items="${jdlist}" var="s">
						 	<option value="${s.JD_DM}">${s.JD_MC}</option>
				     </c:forEach>
						</select>
					</div>
				</div>
				<div class="layui-form-item">
          <label class="layui-form-label">注册资本</label>
          <div class="layui-input-block">
            <input type="text" id="zczb" name="zczb" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">现注册地址</label>
          <div class="layui-input-block">
            <input type="text" id="xzcdz" name="xzcdz" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">当年累计税款（全口径）</label>
          <div class="layui-input-block">
            <input type="text" id="dnljsk" name="dnljsk" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">去年累计税款（全口径）</label>
          <div class="layui-input-block">
            <input type="text" id="qnljsk" name="qnljsk" class="layui-input">
          </div>
        </div>
				<div class="layui-form-item">
          <label class="layui-form-label">拟迁入地</label>
          <div class="layui-input-block">
            <input type="text" id="yqrd" name="yqrd" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">迁出原因</label>
          <div class="layui-input-block">
            <input type="text" id="qryy" name="qryy" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">附件</label>
					<div class="layui-upload">
			      <button type="button" class="layui-btn layui-btn-normal" id="selectFile">选择文件</button>
			      <span id="originalFilename" style="display:none;"></span>
			      <a id="removeFile" class="" href="javascript:;" style="display:none;"><i class="layui-icon layui-icon-close"></i></a>
			    </div>
			    <input type="hidden" id="filename" name="filename">
			    <input type="hidden" id="filesrc" name="filesrc">
        </div>
        <div class="layui-form-item" style="text-align: right;">
          <button lay-submit class="layui-btn" id="add_save_btn" lay-filter="add_save_btn">保存</button>
        </div>
			</form>
    </div>
    
    <div id="checkmain" class="layui-hide" style="padding: 10px;">
      <form class="layui-form" id="check_form">
        <input type="hidden" name="id" id="id"/>
	      <div class="layui-form-item">
	        <label class="layui-form-label">审核意见</label>
	        <div class="layui-input-block">
	          <select id="shyj" name="shyj" lay-filter="shyj" lay-verify="required">
		          <option value="同意">同意</option>这
		          <option value="不同意">不同意</option>
	          </select>
	        </div>
	      </div>
	      <div class="layui-form-item">
	        <label class="layui-form-label">原因</label>
	        <div class="layui-input-block">
	          <textarea rows="" cols="" class="layui-textarea" id="shly" name="shly" placeholder="">
	          </textarea>
	        </div>
	      </div>
	      <div class="layui-form-item" style="text-align: right;">
	        <button lay-submit class="layui-btn" id="check_save_btn" lay-filter="check_save_btn">保存</button>
	      </div>
      </form>
    </div>
    
    <div id="checkdetailmain" class="layui-hide" style="padding: 10px;">
      <form class="layui-form" id="check_form">
        <div class="layui-form-item">
          <label class="layui-form-label">审核意见</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="shyj-span">同意</span>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">原因</label>
          <div class="layui-input-block" style="line-height: 36px; width: 70%">
            <span id="shyy-span" style="word-wrap: anywhere;">111111111111111111111111111111111111111111111111111111111111111111111111111</span>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">审核人</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="fkr-span">同意</span>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">审核日期</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="fkrq-span">同意</span>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">发布人</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="fbr-span">同意</span>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label">发布日期</label>
          <div class="layui-input-block"style="line-height: 36px;">
            <span id="fbrq-span">同意</span>
          </div>
        </div>
      </form>
    </div>
    
	</body>

	<script>
	  var loadlayer;
	  
	  layui.use('upload', function(){
      var $ = layui.jquery,upload = layui.upload;
      
      //选完文件后不自动上传
      upload.render({
        elem: '#selectFile'
        ,url: 'qysksdbg/upload.do?lx=qyqc'
        //,auto: false
        //,multiple: true
        ,exts: 'xlsx|xls'
        ,bindAction: '#upload'
        ,done: function(res) {
          console.log(res);
          var filename = res.data[0].bdsrc;
          var filesrc = res.data[0].src;
          var ofn = res.data[0].originalFilename;
          console.log(filename);
          $('#filename').val(filename);
          $('#filesrc').val(filesrc);
          $('#originalFilename').text(ofn);
          $('#originalFilename').show();
          $('#removeFile').show();
        }
      });
      
      $('#removeFile').click(function() {
    	  $('#filename').val('');
        $('#filesrc').val('');
        $('#originalFilename').text('');
        $('#originalFilename').hide();
        $('#removeFile').hide();
      });
    });
    
    layui.use([ 'form', 'laydate' ], function() {
      
      var form = layui.form, layer = layui.layer, laydate = layui.laydate;
      
      laydate.render({
        elem: '#qrrq' //指定元素
      });
      
      ajax({
        url : "ajax.do?ctrl=qyqc_queryInit",
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
            $("#ssjd").html(str);
            //getData();
            form.render('select');
          }
        }
      });
      form.render('select');
      
      form.on('submit(search_btn)', function() {
    	  loadlayer = layer.load();
        ajax({
          url : "ajax.do?ctrl=qyqc_queryQyqc",
          data : {
            "nsrsbh" : $('input[name="paramNsrsbh"]').val(),
            "nsrmc" : $('input[name="paramNsrmc"]').val()
          },
          type : 'post',
          success : function(obj) {
            console.log(obj);
            getTable(obj.data);
            layer.close(loadlayer);
            layer.msg('查询成功！');
          }
        });
        return false;
      });
      
      form.on('submit(add_btn)', function() {
        console.log("add-------------");
        $('#table-qy-div').removeClass('layui-hide');
        //form.render();
        var selectQyLayer = layer.open({
          type : 1,
          title : '新增企业迁出',
          area : [ '700px', '80%' ],
          content : $('#table-qy-div'),
          success : function() {
              //initjd();
            },
            cancel : function() {
              $('#table-qy-div').hide();

            },
            end : function() {
              $('#table-qy-div').hide();
            }
        });
        return false;
      });
      
      form.on('submit(search_qy_btn)', function() {
    	  loadlayer = layer.load();
    	  console.log("search_qy_btn------------");
    	  ajax({
          url : "ajax.do?ctrl=qyqc_queryQymc",
          data : {
            "qymc" : $('input[name="qymc"]').val(),
          },
          type : 'post',
          success : function(obj) {
            console.log(obj);
            if (obj != null && obj.data != null) {
              //$("#table-qy-div").show();
              var sjList = obj.data;
              console.log(sjList);
              getQyTable(sjList);
              layer.close(loadlayer);
            }
          }
        });
        return false;
      });
      
      form.on('submit(add_save_btn)', function(obj) {
        ajax({
          url : "ajax.do?ctrl=qyqc_addQyqc",
          data : obj.field,
          type : 'post',
          success : function(obj) {
            console.log(obj);
            if(obj.code == '000') {
              layer.closeAll();
              layer.msg('新增成功！');
              getData();
            } else if(obj.code == '500') {
              layer.msg('新增失败！已有该条迁出记录');
            } else {
              layer.msg('新增失败！');
            }
          }
        });
        return false;
      });
      
      form.on('submit(check_save_btn)', function(obj) {
        ajax({
          url : "ajax.do?ctrl=qyqc_checkQyqc",
          data : obj.field,
          type : 'post',
          success : function(obj) {
            console.log(obj);
            if(obj.code == '000') {
              layer.closeAll();
              layer.msg('保存成功！');
              getData();
            } else {
              layer.msg('保存失败！');
            }
          }
        });
        return false;
      });
      
    });
  
    $(function() {
      getData();
    });
    
    function getQyTable(data) {
      //执行渲染
      layui.use([ 'table' ], function() {
        layui.table.render({
          elem : '#table-qy',
          height : 500, //数据接口
          data : data,
          page : true,
          cols : [ [ //表头
            {
              field : 'NSRMC',
              title : '企业名称',
              width:'80%'
            }, 
            {
              field : '',
              title : '操作',
              toolbar : '#select-bar',
              fixed : 'right',
              width:'20%'
            }
          ] ]
        });
        
        //方法级渲染
        //监听工具条
        layui.table.on('tool(table-qy)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
          console.log('listen-table-qy----------------');
          
          var data = obj.data; //获得当前行数据

          var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
          if (layEvent === 'select-qymc') { //选择企业
            /* nsrmc = data.NSRMC;
            getData(data.NSRMC);
            $("#table-qy-div").hide(); */
        	  $('#addmain').removeClass('layui-hide');
             //form.render();
            layer.closeAll();
            layer.open({
              type : 1,
              title : '新增企业迁出',
              area : [ '700px', '80%' ],
              content : $('#addmain'),
              success : function() {
                //initjd();
              },
              cancel : function() {
                $('#addmain').hide();
  
              },
              end : function() {
                $('#addmain').hide();
              }
            });
            $("#nsrmc").val(data.NSRMC);
            return false;
          }
        });
        
      });
    }
  
    function getData() {
      ajax({
        url : "ajax.do?ctrl=qyqc_queryQyqc",
        data : {
          "nsrmc" : $("#paramNsrmc").val(),
          "nsrsbh" : $("#paramNsrsbh").val(),
        },
        type : 'post',
        success : function(obj) {
          console.log(obj);
          if (obj != null && obj.data != null) {
            var sjList = obj.data;
            console.log(sjList);
            getTable(sjList);
            //layer.close(loadlayer);
          }
        }
      });
    }
  
    function getTable(data) {
      layui.use('table', function(){
        var table = layui.table;
        
        //第一个实例
        table.render({
          elem: '#qrtable'
          ,height: 500
          ,width:document.body.clientWidth
          ,data: data //数据
          ,page: true //开启分页
          ,cols: [[ //表头
            {field: 'ID', title: 'ID', width:40, fixed: 'left'}
            ,{field: 'NSRSBH', title: '纳税人识别号', width:220}
            ,{field: 'NSRMC', title: '纳税人名称', width:280}
            ,{field: 'JDMC', title: '所属街道', width:100} 
            ,{field: 'ZCZB', title: '注册资本', width:100} 
            ,{field: 'ZCDZ', title: '现注册地址', width:100} 
            ,{field: 'NQRD', title: '拟迁入地', width: 100}
            ,{field: 'DNLJSK', title: '当年累计税款', width: 120}
            ,{field: 'QNLJSK', title: '去年累计税款', width: 120}
            ,{field: 'STATUS', title: '状态', width: 80, templet: function(data) {
            	console.log(data.STATUS);
            	if(data.STATUS == '0') {
            		return "待审核";
            	} else {
            		return "已审核";
            	}
            }}
            ,{field : '', title : '操作', toolbar : '#bar', fixed : 'right', width:330}
          ]]
        });
        
        layui.table.on('tool(qrtable)', function(obj) {
       	  var data = obj.data; //获得当前行数据
       	  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
       	  var tr = obj.tr; 
          console.log("remove----------");
          if(layEvent === 'remove') {
	          layer.confirm('', {icon: 2, title:'删除'
	          ,area:['30%','27%']
	          ,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">确定删除该条企业迁出记录?</div>'
	          }, function(index){
	        	  //do something
	        	  ajax({
		            url : "ajax.do?ctrl=qyqc_removeQyqc",
		            data : {
		              "id" : obj.data.ID
		            },
		            type : 'post',
		            success : function(obj) {
		              console.log(obj);
		              if(obj.code == '000') {
		            	  getData();
		            	  layer.msg('删除成功！');
		              } else {
		            	  layer.msg('删除失败！');
		              }
			        	  layer.close(index);
		            }
		          });
	        	});  
          } else if(layEvent === 'downloadFile') {
        	  
          } else if(layEvent === 'check') {
        	  $('#checkmain').removeClass('layui-hide');
            //form.render();
            $('#check_form #id').val(obj.data.ID);
            layer.open({
              type : 1,
              title : '企业迁出审核',
              area : [ '500px', '300px' ],
              content : $('#checkmain'),
              success : function() {
                //initjd();
              },
              cancel : function() {
                $('#checkmain').hide();
  
              },
              end : function() {
                $('#checkmain').hide();
              }
            });
            return false;
          } else if(layEvent === 'checkDetail') {
        	  $('#checkdetailmain').removeClass('layui-hide');
        	  console.log(obj.data);
        	  $('#shyj-span').text(obj.data.SHYJ);
        	  $('#shyy-span').text(obj.data.LY);
        	  $('#fkr-span').text(obj.data.FKZ);
        	  $('#fkrq-span').text(obj.data.FKRQ.substring(0, 10));
        	  $('#fbr-span').text(obj.data.FBZ);
        	  $('#fbrq-span').text(obj.data.FBRQ.substring(0, 10));
        	  layer.open({
              type : 1,
              title : '企业迁出审核详情',
              area : [ '500px', '400px' ],
              content : $('#checkdetailmain'),
              success : function() {
                //initjd();
              },
              cancel : function() {
                $('#checkdetailmain').hide();
  
              },
              end : function() {
                $('#checkdetailmain').hide();
              }
            });
          }
        });
      });
    }
    
  </script>
	

</html>
