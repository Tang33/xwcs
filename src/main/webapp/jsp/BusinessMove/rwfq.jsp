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

<title>流程管理—企业迁出(任务发起)</title>

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
		
		<div id="addmain"  style="padding: 10px;">
		  <form class="layui-form" id="add_form" lay-filter="example" > <!-- margin:0 65% 0 0 -->
			<table style="width: 50%;height: 80%;text-align: right;">
				<tr>
					<td>纳税人名称</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="nsrmc" name="nsrmc" class="layui-input" disabled="disabled"></td>
				</tr>
				<tr>
					<td>纳税人识别码</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="nsrsbh" name="nsrsbh" class="layui-input" disabled="disabled"></td>
				</tr>
				<tr>
					<td>现所属街道</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="ssjd" name="ssjd" class="layui-input" disabled="disabled">
					</td>
				</tr>
				<tr>
					<td>注册资本</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="zczb" name="zczb" class="layui-input"></td>
				</tr>
				<tr>
					<td>当年累计税款（全口径）/元</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="dnljsk" name="dnljsk" class="layui-input" disabled="disabled"></td>
				</tr>
				<tr>
					<td>去年累计税款（全口径）/元</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="qnljsk" name="qnljsk" class="layui-input" disabled="disabled"></td>
				</tr>
				<tr>
					<td>上月税收情况（地方口径）/元</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="sylj" name="sylj" class="layui-input" disabled="disabled"></td>
				</tr>
				<tr>
					<td>上月税收排名</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="pm" name="pm" class="layui-input" disabled="disabled"></td>
				</tr>
				<tr>
					<td>上月税收贡献比例</td>
					<td><input style="width: 100%; margin-left: 10%;" type="text" id="gxbl" name="gxbl" class="layui-input" disabled="disabled"></td>
				</tr>
				<tr>
					<td>拟迁出地</td>
					<td><div class="layui-input-block" style="width: 100%; margin-left: 10%;">
							<select name="jdlist" lay-filter="jdlist" id="jdlist">
								<option value="0">请选择</option>
								<option value="91">新街口</option>
								<option value="92">梅园</option>
								<option value="93">后宰门</option>						
								<option value="94">玄武湖</option>					
								<option value="95">玄武门</option>					
								<option value="96">锁金村</option>					
								<option value="97">孝陵卫</option>					
								<option value="98">红山</option>					
								<option value="99">徐庄</option>
							</select>
						</div></td>
				</tr>
				<tr>
					<td>迁出原因</td>
					<td><textarea style="width: 100%;margin-left: 10%;" rows="" cols="" id="qryy" name="qryy" class="layui-textarea" lay-verify="required"></textarea></td>
				</tr>
				<tr>
					<td colspan="2"><button style="margin-right: 25%;" lay-submit class="layui-btn" id="add_save_btn" lay-filter="add_save_btn">发起任务</button></td>
				</tr>
			</table>
			</form>
    </div>
    
    
    
	</body>

	<script>
	  var loadlayer;
	  
	  
    
    layui.use([ 'form', 'laydate' ], function() {
      
      var form = layui.form, layer = layui.layer, laydate = layui.laydate;
      
      form.on('submit(search_qy_btn)', function() {
    	 
    	  var aa = $('input[name="qymc"]').val();
    	  if(aa!=null&&aa!=''){
    		  loadlayer = layer.load();
    		  queryinit();
    		 /*  ajax({
                  url : "rwfq/queryQymc.do",
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
                              document.getElementById("add_form").reset();
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
                }); */
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
      
      form.on('submit(add_save_btn)', function(obj) {
    	  layer.confirm('', {btn: ['是','否']
				,area:['30%','27%']
				,content: '<div style="font-size: 24px;padding-top: 5%;padding-left: 5%;">是否确认迁移到'+$('#jdlist').find("option:selected").text()+'？</div>'
          }, function(index){ 
        	 ajax({
                 url : "rwfq/addQyqc.do",
                 data : obj.field,
                 type : 'post',
                 success : function(obj) {
                   if(obj.code == '000') {
                     layer.closeAll();
                     layer.msg('新增成功！');
                     getData();
                   } else if(obj.code == '500') {
                     layer.msg('新增失败！已有该条迁出记录');
                   } else if(obj.code == '501') {
                     layer.msg('请选择迁入地！');
                   } else {
                     layer.msg('新增失败！');
                   }
                 }
               });
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
              width:'35%'
            }, 
            {
                field : 'JD_MC',
                title : '所属街道',
                width:'32.5%'
              }, 
            {
              field : '',
              title : '操作',
              toolbar : '#select-bar',
              width:'32.5%'
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
                var data = obj.data; //获得当前行数据
                var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                if (layEvent === 'select-qymc') { //选择企业
              	  $('#addmain').removeClass('layui-hide');
              
                    layer.closeAll();
                	loadlayer = layer.load();
                    ajax({
                        url : "rwfq/queryAll.do",
                        data : {
                          "nsrmc" : data.NSRMC,
                          "jd_dm" : data.JD_DM
                        },
                        type : 'post',
                        success : function(zz) {   
                          layer.close(loadlayer);
                      	  var sj = zz.data;
                      	  if(sj.length <= 0){
                      		form.val('example', {
                      		    "nsrmc": data.NSRMC //纳税人名称
                      		  	//,"nsrsbh": data.NSRSBH //纳税人识别号
                      		    ,"ssjd": data.JD_MC
                      		    ,"dnljsk": ""
                      		    ,"qnljsk": ""
                      		    ,"sylj":""
                      		    ,"pm":""
                      		    ,"gxbl":""
                      		  })  
                        }else{
                      		form.val('example', {
                      		    "nsrmc": data.NSRMC //纳税人名称
                      		  	//,"nsrsbh": data.NSRSBH //纳税人识别号
                      		    ,"ssjd": data.JD_MC
                      		    ,"dnljsk": sj[0].DNLJJE==undefined?"":sj[0].DNLJJE
                      		    ,"qnljsk": sj[0].QNLJJE==undefined?"":sj[0].QNLJJE
                      		    ,"sylj":sj[0].SYLJ==undefined?"":sj[0].SYLJ
                      		    ,"pm":sj[0].PM==undefined?"":sj[0].PM
                      		    ,"gxbl":sj[0].GXBL==undefined?"":sj[0].GXBL+'%'
                      		  })  
                        }
                      	 }
                      	  
                      });
                }
              }); 
        	  
        	  
        	  
        	  
        	  
        });
        
        
        
        
        
      });
    }
  
    function queryinit(e) {
        //执行渲染
        layui.use([ 'table' ], function() {
          layui.table.render({
            elem : '#table-qy',
            //height : 543, //数据接口
            //data : data,
            url : "rwfq/queryQymc.do"
	        ,where: {
	        	"qymc" : $('input[name="qymc"]').val()
		    }
	        ,limit:10
	        ,page: true ,//开启分页
            cols : [ [ //表头
              {
                field : 'NSRMC',
                title : '企业名称',
                width:'20%'
              }, 
              {
                  field : 'JD_MC',
                  title : '所属街道',
                  width:'10%'
                }, 
              {
                field : '',
                title : '操作',
                toolbar : '#select-bar',
                width:'10%'
              }
            ] ]
	        ,done: function(res, curr, count){
	        	$('#table-qy-div').removeClass('layui-hide'); 
                var selectQyLayer = layer.open({	
                  type : 1,
                  title : '新增企业迁出',
                  area : [ '700px', '80%' ],
                  content : $('#table-qy-div'),
                  id:'ccnklop',
                  success : function() {            	 
                        layer.close(loadlayer);
                      
                    },
                    cancel : function() {
                      $('#table-qy-div').hide();

                    },
                    end : function() {
                      $('#table-qy-div').hide();
                    }
                })
	         }
          });
          
         
          layui.use(['form', 'layedit', 'laydate'], function(){
          	  var form = layui.form
          	  ,layer = layui.layer
          	  ,layedit = layui.layedit
          	  ,laydate = layui.laydate;

          	  //方法级渲染
                //监听工具条
                layui.table.on('tool(table-qy)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"   
                  var data = obj.data; //获得当前行数据
                  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
                  if (layEvent === 'select-qymc') { //选择企业
                	  $('#addmain').removeClass('layui-hide');
                
                      layer.closeAll();
                  	loadlayer = layer.load();
                      ajax({
                          url : "rwfq/queryAll.do",
                          data : {
                            "nsrmc" : data.NSRMC,
                            "jd_dm" : data.JD_DM
                          },
                          type : 'post',
                          success : function(zz) {   
                            layer.close(loadlayer);
                        	  var sj = zz.data;
                        	  if(sj.length <= 0){
                        		form.val('example', {
                        		    "nsrmc": data.NSRMC //纳税人名称
                        		  	//,"nsrsbh": data.NSRSBH //纳税人识别号
                        		    ,"ssjd": data.JD_MC
                        		    ,"dnljsk": ""
                        		    ,"qnljsk": ""
                        		    ,"sylj":""
                        		    ,"pm":""
                        		    ,"gxbl":""
                        		  })  
                          }else{
                        		form.val('example', {
                        		    "nsrmc": data.NSRMC //纳税人名称
                        		  	//,"nsrsbh": data.NSRSBH //纳税人识别号
                        		    ,"ssjd": data.JD_MC
                        		    ,"dnljsk": sj[0].DNLJJE==undefined?"":sj[0].DNLJJE
                        		    ,"qnljsk": sj[0].QNLJJE==undefined?"":sj[0].QNLJJE
                        		    ,"sylj":sj[0].SYLJ==undefined?"":sj[0].SYLJ
                        		    ,"pm":sj[0].PM==undefined?"":sj[0].PM
                        		    ,"gxbl":sj[0].GXBL==undefined?"":sj[0].GXBL+'%'
                        		  })  
                          }
                        	 }
                        	  
                        });
                  }
                }); 
          	  
          	  
          	  
          	  
          	  
          });
          
          
          
          
          
        });
      }

    
  </script>
	

</html>
