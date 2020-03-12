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
		<blockquote class="layui-elem-quote layui-text">本功能用于发布企业迁出任务！</blockquote>
		<form class="layui-form" id="form" action="">
			<div class="layui-collapse" lay-filter="test">
				<div class="layui-colla-item">
					<div class="layui-colla-content  layui-show">
						<div class="layui-row">
						<form class="layui-form" id="qy_form" action="">
							<div class="layui-row">
								<div class="layui-col-md4">
									<label class="layui-form-label">企业名称:</label>
									<div class="layui-input-block">
										<input type="text" class="layui-input" id="qymc" name="qymc"
											placeholder="">
									</div>
								</div>
								<div class="layui-col-md4" style="margin-left: 20px;">
									<button lay-submit class="layui-btn" id="search_qy_btn"
										lay-filter="search_qy_btn">查询</button>
								</div>
							</div>
						</form>
					</div>
					</div>
				</div>
			</div>

			
		</form>
		
		<div id="table-qy-div" class="layui-hide" style="padding: 10px;">
		  
      <table class="layui-table" id="table-qy" lay-filter="table-qy">

      </table>
    </div>
    <script type="text/html" id="select-bar">
      <a class="layui-btn layui-btn-xs" lay-event="select-qymc">选择</a>
    </script>
		
		<div id="addmain" class="layui-hide" style="padding: 10px;">
		  <form class="layui-form" id="add_form" lay-filter="example" > <!-- margin:0 65% 0 0 -->
			<table style="width: 50%;height: 80%;text-align: right;">
				<tr>
					<td>纳税人名称</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="nsrmc" name="nsrmc" class="layui-input" lay-verify="required"></td>
				</tr>
				<tr>
					<td>纳税人识别码</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="nsrsbh" name="nsrsbh" class="layui-input" lay-verify="required"></td>
				</tr>
				<tr>
					<td>现所属街道</td>
					<td>
						<div class="layui-input-inline" style="width: 100%;margin-left: 10%;">
						<select id="ssjd" name="ssjd" lay-filter="ssjd" lay-verify="required">
							1111
						</select>
					</div>
					</td>
				</tr>
				<tr>
					<td>注册资本</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="zczb" name="zczb" class="layui-input"></td>
				</tr>
				<tr>
					<td>当年累计税款（全口径）/元</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="dnljsk" name="dnljsk" class="layui-input"></td>
				</tr>
				<tr>
					<td>去年累计税款（全口径）/元</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="qnljsk" name="qnljsk" class="layui-input"></td>
				</tr>
				<tr>
					<td>上月税收情况（地方口径）/元</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="sylj" name="sylj" class="layui-input"></td>
				</tr>
				<tr>
					<td>上月税收排名</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="pm" name="pm" class="layui-input"></td>
				</tr>
				<tr>
					<td>上月税收贡献比例</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="gxbl" name="gxbl" class="layui-input"></td>
				</tr>
				<tr>
					<td>拟迁入地</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="yqrd" name="yqrd" class="layui-input"></td>
				</tr>
				<tr>
					<td>迁出原因</td>
					<td><textarea style="width: 100%;margin-left: 10%;" rows="" cols="" id="qryy" name="qryy" class="layui-textarea" lay-verify="required"></textarea></td>
				</tr>
				<tr>
					<td>上传附件</td>
					<td>
						<div class="layui-upload">
			      <button style="margin-left: 87%;" type="button" class="layui-btn layui-btn-normal" id="selectFile">选择文件</button>
			      <span id="originalFilename" style="display:none;"></span>
			      <a id="removeFile" class="" href="javascript:;" style="display:none;"><i class="layui-icon layui-icon-close"></i></a>
			    </div>
			    <input type="hidden" id="filename" name="filename">
			    <input type="hidden" id="filesrc" name="filesrc">
					</td>
				</tr>
				<tr>
					<td colspan="2"><button style="margin-right: 25%;" lay-submit class="layui-btn" id="add_save_btn" lay-filter="add_save_btn">发起任务</button></td>
				</tr>
			</table>
			<!-- 	<div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">纳税人名称</label>
          <div class="layui-input-inline" style="width: 60%">
            <input type="text" id="nsrmc" name="nsrmc" class="layui-input" lay-verify="required">
          </div>
        </div>
				<div class="layui-form-item">
					<label class="layui-form-label" style=" text-align: left;">纳税人识别码</label>
					<div class="layui-input-inline" style="width: 60%">
						<input type="text" id="nsrsbh" name="nsrsbh" class="layui-input" lay-verify="required">
					</div>
				</div>
				<div class="layui-form-item">
					<label class="layui-form-label" style=" text-align: left;">所属街道</label>
					<div class="layui-input-inline" style="width: 60%">
						<select id="ssjd" name="ssjd" lay-filter="ssjd" lay-verify="required">
							1111
						</select>
					</div>
				</div>
				<div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">注册资本</label>
          <div class="layui-input-inline" style="width: 60%">
            <input type="text" id="zczb" name="zczb" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">现注册地址</label>
          <div class="layui-input-inline" style="width: 60%">
            <input type="text" id="xzcdz" name="xzcdz" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">当年累计税款（全口径）/元</label>
          <div class="layui-input-inline" style="width: 60%">
            <input type="text" id="dnljsk" name="dnljsk" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">去年累计税款（全口径）/元</label>
          <div class="layui-input-inline" style="width: 60%">
            <input type="text" id="qnljsk" name="qnljsk" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">上月税收情况（地方口径）/元</label>
          <div class="layui-input-inline" style="width: 60%">
            <input type="text" id="sylj" name="sylj" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">上月税收排名</label>
          <div class="layui-input-inline" style="width: 60%">
            <input type="text" id="pm" name="pm" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">上月税收贡献比例</label>
          <div class="layui-input-inline" style="width: 60%">
            <input type="text" id="gxbl" name="gxbl" class="layui-input">
          </div>
        </div>
        
				<div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">拟迁入地</label>
          <div class="layui-input-inline" style="width: 60%">
            <input type="text" id="yqrd" name="yqrd" class="layui-input">
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;" >迁出原因</label>
          <div class="layui-input-inline" style="width: 60%">
            <textarea rows="" cols="" id="qryy" name="qryy" class="layui-textarea" lay-verify="required"></textarea>
          </div>
        </div>
        <div class="layui-form-item">
          <label class="layui-form-label" style="text-align: left;">上传附件</label>
					<div class="layui-upload">
			      <button type="button" class="layui-btn layui-btn-normal" id="selectFile">选择文件</button>
			      <span id="originalFilename" style="display:none;"></span>
			      <a id="removeFile" class="" href="javascript:;" style="display:none;"><i class="layui-icon layui-icon-close"></i></a>
			    </div>
			    <input type="hidden" id="filename" name="filename">
			    <input type="hidden" id="filesrc" name="filesrc">
        </div>
        <div class="layui-form-item" style="text-align: left; padding: 9px 15px">
          <button lay-submit class="layui-btn" id="add_save_btn" lay-filter="add_save_btn">发起任务</button>
        </div> -->
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
            	if(jdlist[i].JD_DM==20||jdlist[i].JD_DM==00||jdlist[i].JD_DM==01||jdlist[i].JD_DM==25){
            		continue;
                 } 
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
      
      form.on('submit(search_qy_btn)', function() {
    	 
    	  var aa = $('input[name="qymc"]').val();
    	  console.log("aa"+aa)
    	  if(aa!=null&&aa!=''){
    		  loadlayer = layer.load();
    		  ajax({
                  url : "ajax.do?ctrl=Rwfq_queryQymc",
                  data : {
                    "qymc" : $('input[name="qymc"]').val(),
                  },
                  type : 'post',
                  success : function(obj) {
                	  $('#table-qy-div').removeClass('layui-hide'); 
                      var selectQyLayer = layer.open({	
                        type : 1,
                        title : '新增企业迁出',
                        area : [ '700px', '80%' ],
                        content : $('#table-qy-div'),
                        success : function() {            	 
                            if (obj != null && obj.data != null) {
                              var sjList = obj.data;
                              console.log(sjList);
                              getQyTable(sjList);
                              layer.close(loadlayer);
                            }
                          },
                          cancel : function() {
                            $('#table-qy-div').hide();

                          },
                          end : function() {
                            $('#table-qy-div').hide();
                          }
                      });
                  }
                });
    	  }else{
    		 // layer.msg("请输入迁出企业名称")
    		  layer.confirm('', {btn: ['确认']
    		  ,area:['20%','35%']
    		  ,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">请输入迁出企业名称</div>'
 				
             }, function(index){ 
          		layer.close(index)	
             });
    	  }
    	 

          
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
      
      form.on('submit(add_save_btn)', function(obj) {
        ajax({
          url : "ajax.do?ctrl=Rwfq_addQyqc",
          data : obj.field,
          type : 'post',
          success : function(obj) {
            console.log(obj);
            if(obj.code == '000') {
              layer.closeAll();
              layer.msg('新增成功！');
              document.getElementById("add_form").reset();
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
              width:'60%'
            }, 
            {
                field : 'NSRSBH',
                title : '纳税人识别号',
                width:'20%'
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
        
       
        layui.use(['form', 'layedit', 'laydate'], function(){
        	  var form = layui.form
        	  ,layer = layui.layer
        	  ,layedit = layui.layedit
        	  ,laydate = layui.laydate;

        	  //方法级渲染
              //监听工具条
              layui.table.on('tool(table-qy)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
                console.log('listen-table-qy----------------');
                
                var data = obj.data; //获得当前行数据

                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                if (layEvent === 'select-qymc') { //选择企业
              	  $('#addmain').removeClass('layui-hide');
                    layer.closeAll();
                    ajax({
                        url : "ajax.do?ctrl=Rwfq_queryAll",
                        data : {
                          "nsrmc" : data.NSRMC
                        },
                        type : 'post',
                        success : function(zz) {         	
                      	  var sj = zz.data;
                      	  console.log("-----------");
                      	  console.log(data.NSRMC);
                      	  console.log(sj);
                      	  form.val('example', {
                      		    "nsrmc": data.NSRMC //纳税人名称
                      		  	,"nsrsbh": data.NSRSBH //纳税人识别号
                      		    ,"ssjd": sj[0].JDDM
                      		    ,"dnljsk": sj[0].DNLJJE
                      		    ,"qnljsk": sj[0].QNLJJE
                      		    ,"sylj":sj[0].SYLJ
                      		    ,"pm":sj[0].PM
                      		    ,"gxbl":sj[0].GXBL+'%'
                      		  })  
                        }
                      });
                }
              }); 
        	  
        	  
        	  
        	  
        	  
        });
        
        
        
        
        
      });
    }
  


    
  </script>
	

</html>
