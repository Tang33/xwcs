<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	java.util.Properties prop = new java.util.Properties();
	java.io.InputStream in;
	in = getClass().getResourceAsStream("/conf/jdbc.properties");
	prop.load(in);
	String value = (String) prop.get("ip");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>系统管理-后台页面</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta name="msapplication-TileColor" content="#3399cc" />
<!-- 公共开始 -->
<script src="./js/jquery.js" charset="utf-8"></script>
<script src="./static/js/easy.ajax.js" charset="utf-8"></script>
<!-- 公共结束 -->

<!-- layui开始 -->
<link rel="stylesheet" href="./static/layui/css/layui.css" media="all">
<script src="./static/layui/layui.js" charset="utf-8"></script>
<link rel="stylesheet" href="./static/css/index.css">
<link rel="stylesheet" href="./static/css/chinaz.css">
<link rel="stylesheet" href="./static/css/button.css">
<!-- layui结束 -->


	<script src="static/js/json.js"></script>
</head>
<body>

	<div
		style="position: absolute; z-index: 99999; right: 5%; top: 0px; font-size: 25px; cursor: pointer;">
	</div>
	<div class="foo"
		style="position: relative; top: -80px; cursor: pointer">
		<span class="letter" data-letter="A">A</span> <span class="letter"
			data-letter="P">P</span> <span class="letter" data-letter="I">I</span>
		<span class="letter" data-letter="文">文</span> <span class="letter"
			data-letter="档">档</span>
	</div>


	<div id="tb" class="layui-collapse" lay-accordion="">
		<div id="div" class="layui-colla-item">
			<h2 class="layui-colla-title">Login</h2>
			<div class="layui-colla-content  layui-show">
				<dl class="dl">
					<dt>
						<span
							style='background-color: #106DB8; color: white; padding: 5px; border: 1px solid red; border-radius: 5px;'>Post</span>初始化
					</dt>
					<dd>登录页面初始化方法</dd>
				</dl>
			</div>
		</div>
	</div>
	<input type="hidden" id="ip" value="<%=value%>" />


	<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->

	<script type="text/javascript">
		var count=0;
			$.ajax({
					url : "ajax.do?ctrl=Index_codeList",
					type : 'post',
					dataType : "json",
					success : function(obj) {
						var str = "";
						$("#tb").html(str);
						if (obj != null && obj.code == '000') {
							var data = obj.data;
							for (var i = 0; i < data.length; i++) {
								var claName = data[i].claName;
								var vString = data[i].value;
								var metList = data[i].metList;
								str += "<div class='layui-colla-item' style='clear:both;'><h2 class='layui-colla-title'>"
										+ claName
										+ "<span style='margin-left:200px;'>"
										+ vString + "</span></h2>";
								if(i==0){
									str += "<div class='layui-colla-content layui-show' >";
								}else{
									str += "<div class='layui-colla-content' >";
								}
								
								if (metList != null && metList.length > 0) {
									for (var j = 0; j < metList.length; j++) {
										var descript = metList[j].descript;
										var type = metList[j].type;
										var value = metList[j].value;
										var key = metList[j].key;
										if (type == 'Post') {
											str += "<dl class='dl' style='background-color:#E7F6EF;border-color: #DDE3E9;'><dt>";
										} else if (type == 'Get') {
											str += "<dl class='dl' style='background-color:#EAF0F7;border-color: #DDE3E9;'><dt>";
										}
										if (type == 'Post') {
											str += "<span style='background-color:#12A347;color:white;padding:5px;border:1px solid;border-radius:5px;margin-right:20px;'>"
													+ type
													+ "</span><span style='padding:0px 10px;float:right;color:#80BA96;'>"
													+ value + "</span></dt>";
											str += "<dd style='margin-top:10px;'>"
													+ descript
													+ "</dd><dd style='margin-top:10px;color:#80BA96;'>接口地址："
													+ key
													+ "<span style='float:right;'>"
													+ "<div class='svg-wrapper'><svg height='30' width='80' xmlns='http://www.w3.org/2000/svg'>"
													+ "	<rect id='shape' height='30' width='80' />"
													+ "	<div id='text'><a href='javascript:void(0);' onclick=\"javascript:test('"
													+ claName
													+ "','"
													+ vString
													+ "','"
													+ key
													+ "','"
													+ descript
													+ "','"
													+ type
													+ "');\"><span class='spot'></span>测 试</a></div>"
													+ "</svg></div>"
													+ "</span></dd></dl>";
										} else if (type == 'Get') {
											str += "<span style='background-color:#106DB8;color:white;padding:5px;border:1px solid;border-radius:5px;margin-right:20px;'>"
													+ type
													+ "</span><span style='padding:0px 10px;float:right;color:#678EB4;'>"
													+ value + "</span></dt>";
											str += "<dd style='margin-top:10px;'>"
													+ descript
													+ "</dd><dd style='margin-top:10px;color:#678EB4;'>接口地址："
													+ key
													+ "<span style='float:right;'>"
													+ "<div class='svg-wrapper'><svg height='30' width='80' xmlns='http://www.w3.org/2000/svg'>"
													+ "	<rect id='shape' height='30' width='80' />"
													+ "	<div id='text'><a href='javascript:void(0);' onclick=\"javascript:test('"
													+ claName
													+ "','"
													+ vString
													+ "','"
													+ key
													+ "','"
													+ descript
													+ "','"
													+ type
													+ "');\"><span class='spot'></span>测 试</a></div>"
													+ "</svg></div>"
													+ "</span></dd></dl>";
										}

									}
								}
								str += "</div></div>";
							}
						}
						$("#tb").html(str);
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						// 状态码
						alert(XMLHttpRequest.status);
						// 状态
						alert(XMLHttpRequest.readyState);
						// 错误信息  
						alert(textStatus);
					}
				});
		layui.use([ 'form', 'element', 'layer' ], function() {
			var element = layui.element;
			var layer = layui.layer;
			var form = layui.form;
			//监听折叠
			element.on('collapse(test)', function(data) {
				layer.msg('展开状态：' + data.show);
			});
		});
		function test(cname, vString, key, ms, type) {
			var count = $("#sel option").length;
			for (var i = 0; i < count; i++) {
				if ($("#sel ").get(0).options[i].value == type) {
					$("#sel ").get(0).options[i].selected = true;
					break;
				}
			}
			cname = cname.substring(cname.lastIndexOf(".") + 1);
			var ip = $("#ip").val();
			$(".input").val(ip + "/App/ajax.do?ctrl=" + cname + "_" + key);
			//$("[name='testname']").val("xxxxxxxxxxxxxxx");//向模态框中赋值
			layui.use([ 'layer' ], function() {
				var layer = layui.layer, $ = layui.$;
				layer.open({
					type : 1,//类型
					area : [ '80%', '80%' ],//定义宽和高
					title : '查看详细信息',//题目
					shadeClose : false,//点击遮罩层关闭
					content : $('#motaikunag'),
					cancel : function() {
						window.location.reload();
					}
				//打开的内容
				});
			})

		}
		function loadProperties() {
			jQuery.i18n.properties({// 加载properties文件  
				name : 'ISPindex', // properties文件名称  
				path : 'i18n/', // properties文件路径  
				mode : 'map', // 用 Map 的方式使用资源文件中的值  
				callback : function() {// 加载成功后设置显示内容  
					alert($.i18n.prop("ip"));//其中isp_index为properties文件中需要查找到的数据的key值  
				}
			});
		}
		function ins() {
			count+=1;
			var val = $("#val").val();
			var vv = parseInt(val) + 1;
			$("#val").val(vv);
			var val = $("#val").val();
			var str = "<tr id='tr"+val+"'><td><input type='text' id='kval"+val+"' autocomplete='off' class='layui-input'  style='margin-top:5px;' /></td>"
					+"<td><input type='text' id='vval"+val+"' autocomplete='off' class='layui-input'  style='margin-top:5px;margin-left:5px;' /></td><td>"
					+"<button style='height:32px;line-height:26px;background-color: #FF5722; color: white; padding:0px 10px;margin-top:5px; margin-left:10px;border: 1px solid #FF5722; border-radius: 2px;' onclick='del(\""
					+ val + "\");'> X </button></td></tr>";
			$("#tab").append(str);
		}
		function del(str) {
			count-=1;
			var tr = $("#tr" + str).remove();
		}

		function doLoad() {
			var data={};
			for(var i=0;i<count;i++){
				var key=$("#kval"+(i+1)).val();
				var value=$("#vval"+(i+1)).val();
				console.log("key:"+key+",value:"+value);
				eval("data."+key+"=" + value);
			}
			console.log(data);
			var turl = $(".input").val();
			var type = $("#sel").val();
			$.ajax({
				type : type,
				url : turl,
				data:data,
				dataType: "json",
				success : function(data) {
					console.log(data);
					var json = JSON.stringify(data);
					//(2)调用formatJson函数,将json格式进行格式化
					Process(json);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					// 状态码
					console.log(XMLHttpRequest.status);
					// 状态
					console.log(XMLHttpRequest.readyState);
					// 错误信息   
					console.log(textStatus);
				}
			});
		}
	</script>
	<div id="motaikunag" style="display: none;">
		<div id="div1">
			<select name="" id="sel">
				<option value="Post">Post</option>
				<option value="Get">Get</option>
				<option value="Delete">Delete</option>
				<option value="Put">Put</option>
			</select>
		</div>
		<input class="input" type="text" name="username"> <span
			style='float: right; position: relative; left: -145px; top: 5px;'>
			<div class='svg-wrapper'>
				<svg height='35' width='80' xmlns='http://www.w3.org/2000/svg'>
				<rect id='shape' height='35' width='80' />
				<div id='text'>
					<a href='javascript:void(0);' onclick="javascript:doLoad();"> <span
						class='spot'></span>跳 转
					</a>
				</div>
				</svg>
			</div>
		</span>
		<div class="wrap">
			<aside id="left">
			<fieldset>
				<legend>请求参数：</legend>
				<table
					style="width: 90%; overflow: auto; display: block; height: 90%;">
					<thead>
						<tr>
							<th><input type="text" value="键" autocomplete="off" class="layui-input" style="border: 0px solid;"></th>
							<th><input type="text" value="值"  autocomplete="off" class="layui-input" style="border: 0px solid;"></th>
							<th><button onclick='javascript:ins();' 
							style="height:28px;line-height:22px;background-color: #009FFD; color: white;padding:0px 10px; margin-left:10px;  border: 1px solid #009FFD; border-radius: 2px;"> + </button></th>
						</tr>
					</thead>
					<tbody id="tab">

					</tbody>
				</table>
				<input type="hidden" id="val" value="0" />
			</fieldset>
			</aside>
			<section id="right">
			<fieldset>
				<legend>返回值：</legend>
				<div id="Canvas"></div>
			</fieldset>
			</section>
		</div>


	</div>

</body>
</html>