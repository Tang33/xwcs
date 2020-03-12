<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<script src="../js/jquery.js" charset="utf-8"></script>

<link rel="stylesheet" href="../static/layui/css/layui.css" media="all">
<script src="../static/layui/layui.js" charset="utf-8"></script>



<!--begin code mirror -->
<!--下面两个是使用Code Mirror必须引入的-->
<link rel="stylesheet" href="../codemirror-5.45.0/lib/codemirror.css" />
<!--引入css文件，用以支持主题-->
<!-- <link rel="stylesheet" type="text/css" href="../codemirror-5.45.0/theme/dracula.css"/> -->
<!-- <link rel="stylesheet" type="text/css" href="../codemirror-5.45.0/theme/monokai.css"> -->



<link type="text/css" rel="stylesheet"
	href="../codemirror-5.45.0/theme/blackboard.css" />
<link type="text/css" rel="stylesheet"
	href="../codemirror-5.45.0/theme/eclipse.css" />


<!--支持代码折叠-->
<link rel="stylesheet"
	href="../codemirror-5.45.0/addon/fold/foldgutter.css" />
<!--全屏支持样式文件-->
<link href="../codemirror-5.45.0/addon/display/fullscreen.css"
	rel="stylesheet" type="text/css">
<link type="text/css" rel="stylesheet"
	href="../codemirror-5.45.0/addon/hint/show-hint.css" />

<!-- 引入CodeMirror核心文件 -->
<script src="../codemirror-5.45.0/lib/codemirror.js"></script>
<!--Java代码高亮必须引入-->
<script src="../codemirror-5.45.0/mode/clike/clike.js"></script>


<!-- CodeMirror支持不同语言，根据需要引入JS文件 -->
<!-- 因为HTML混合语言依赖Javascript、XML、CSS语言支持，所以都要引入 -->
<script type="text/javascript"
	src="../codemirror-5.45.0/mode/javascript/javascript.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/mode/xml/xml.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/mode/css/css.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/mode/htmlmixed/htmlmixed.js"></script>

<script src="../codemirror-5.45.0/addon/fold/foldcode.js"></script>
<script src="../codemirror-5.45.0/addon/fold/foldgutter.js"></script>
<script src="../codemirror-5.45.0/addon/fold/brace-fold.js"></script>
<script src="../codemirror-5.45.0/addon/fold/comment-fold.js"></script>
<!-- 下面分别为显示行数、括号匹配和全屏插件  -->
<script type="text/javascript"
	src="../codemirror-5.45.0/addon/selection/active-line.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/addon/edit/matchbrackets.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/addon/display/fullscreen.js"></script>

<!-- 自动代码补全  -->
<script type="text/javascript"
	src="../codemirror-5.45.0/mode/velocity/velocity.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/addon/hint/show-hint.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/addon/hint/html-hint.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/addon/hint/css-hint.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/addon/hint/javascript-hint.js"></script>
<script type="text/javascript"
	src="../codemirror-5.45.0/addon/hint/anyword-hint.js"></script>
<!--end Code Mirror -->



<link rel="stylesheet" type="text/css"
	href="../static/easyui/themes/material-teal/easyui.css">
<link rel="stylesheet" type="text/css"
	href="../static/easyui/themes/icon.css">
<script type="text/javascript"
	src="../static/easyui/jquery.easyui.all.js"></script>

<head>
<meta charset="utf-8" />
<title>代码框</title>
<style type="text/css">
.CodeMirror {
	border: 0px solid;
	font-size: 15px;
	width: 100%;
	height: 100%;
}

#tt {
	padding: 0px;
	margin: 0px auto;
}

.left {
	float: left;
	width: 60%;
	height: 100%;
}

.right {
	float: right;
	width: 40%;
	height: 100%;
}

.right .up {
	border-bottom: 1px solid #DDDDDD;
	width: 100%;
	height: 100%;
}

.right, down {
	width: 40%;
	height: 50%;
}
</style>
</head>
<body style="overflow:hidden">

	<div id="tt" class="easyui-tabs" style="width:100%;height:100%;">
		<div title="html" style="padding:0px;display:none;overflow:hidden">
			<div class="left">
				<textarea id="html_code" class="CodeMirror" name="html_code"></textarea>
			</div>
			<div class="right">
				<div class="up">
					<textarea id="js_code" class="CodeMirror" name="js_code"></textarea>
				</div>
				<div class="down">
					<textarea id="css_code" class="CodeMirror" name="css_code"></textarea>
				</div>
			</div>

		</div>
		<div title="预览" style="overflow:auto;padding:0px;display:none;">
			<iframe id="yl" src=""
				style="width:100%;height: 100%;border: 0px solid;"></iframe>
		</div>
		<div title="java" id="java" style="padding:0px;display:none;">
			<textarea id="java_code" class="CodeMirror" name="java_code"
				style="margin-left: 42px;"></textarea>
		</div>
	</div>

	<script>
		//editor.getOption("theme","seti");
		var idx = 0;
		var tds = -1;
		var editor_jsp;
		var editor_js;
		var editor_css;
		var editor;
		var str = "";
		var str_jsp = "";
		var str_js = "";
		var str_css = "";
	
		var ref = window.location.href;
		var url = ref.substring(ref.indexOf("file_name") + 10, ref.indexOf("&"));
		var sms = ref.substring(ref.indexOf("&") + 4);
	
		init();
		function addTab(title, url) {
			if ($('#tt').tabs('exists', title)) {
				$('#tt').tabs('select', title);
			} else {
				var content1 = '<iframe scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:100%;"></iframe>';
				$('#tt').tabs('add', {
					title : title,
					content : content1,
					closable : true
				});
			}
		}
		function init() {
			$('#tt').tabs({
				fit : true,
				border : true,
				tabPosition : "bottom",
				onSelect : function(title, index) {
					tds = index;
					if (index == 0) {
						//html初始化----------------------------------------------------------------------------
						editor_jsp = CodeMirror.fromTextArea(document.getElementById("html_code"), {
							lineNumbers : true, // 显示行数
							indentUnit : 2, // 缩进单位为2
							styleActiveLine : true, // 当前行背景高亮
							matchBrackets : true, // 括号匹配
							mode : "text/html",
	
							theme : 'eclipse', // 使用monokai模版
							smartIndent : true, // 自动缩进
							autofocus : true, // 是否在初始化时自动获取焦点
							showCursorWhenSelecting : true, // 在选择时是否显示光标，默认为false 
							//keyMap:"vim",		   // 绑定Vim
							// 代码折叠
							lineWrapping : true, // 自动换行
							foldGutter : true,
							gutters : [ "CodeMirror-linenumbers", "CodeMirror-foldgutter" ],
							//全屏模式
							//fullScreen : true,
							cursorHeight : 0.85,
						});
						editor_jsp.setOption("extraKeys", {
							// Tab键换成4个空格
							Tab : function(cm) {
								var spaces = Array(cm.getOption("indentUnit") + 1).join(" ");
								cm.replaceSelection(spaces);
							},
							"Alt-/" : "autocomplete",
						});
	
						//js初始化----------------------------------------------------------------------------
						editor_js = CodeMirror.fromTextArea(document.getElementById("js_code"), {
							lineNumbers : true, // 显示行数
							indentUnit : 2, // 缩进单位为2
							styleActiveLine : true, // 当前行背景高亮
							matchBrackets : true, // 括号匹配
							mode : "text/javascript",
	
							theme : 'eclipse', // 使用monokai模版
							smartIndent : true, // 自动缩进
							autofocus : true, // 是否在初始化时自动获取焦点
							showCursorWhenSelecting : true, // 在选择时是否显示光标，默认为false 
							//keyMap:"vim",		   // 绑定Vim
							// 代码折叠
							lineWrapping : true, // 自动换行
							foldGutter : true,
							gutters : [ "CodeMirror-linenumbers", "CodeMirror-foldgutter" ],
							//全屏模式
							//fullScreen : true,
							cursorHeight : 0.85,
						});
						editor_js.setOption("extraKeys", {
							// Tab键换成4个空格
							Tab : function(cm) {
								var spaces = Array(cm.getOption("indentUnit") + 1).join(" ");
								cm.replaceSelection(spaces);
							},
							"Alt-/" : "autocomplete",
						});
	
	
						//css初始化----------------------------------------------------------------------------
						editor_css = CodeMirror.fromTextArea(document.getElementById("css_code"), {
							lineNumbers : true, // 显示行数
							indentUnit : 2, // 缩进单位为2
							styleActiveLine : true, // 当前行背景高亮
							matchBrackets : true, // 括号匹配
							mode : "text/css",
	
							theme : 'eclipse', // 使用monokai模版
							smartIndent : true, // 自动缩进
							autofocus : true, // 是否在初始化时自动获取焦点
							showCursorWhenSelecting : true, // 在选择时是否显示光标，默认为false 
							//keyMap:"vim",		   // 绑定Vim
							// 代码折叠
							lineWrapping : true, // 自动换行
							foldGutter : true,
							gutters : [ "CodeMirror-linenumbers", "CodeMirror-foldgutter" ],
							//全屏模式
							//fullScreen : true,
							cursorHeight : 0.85,
						});
						editor_css.setOption("extraKeys", {
							// Tab键换成4个空格
							Tab : function(cm) {
								var spaces = Array(cm.getOption("indentUnit") + 1).join(" ");
								cm.replaceSelection(spaces);
							},
							"Alt-/" : "autocomplete",
						});
	
						var hh = '';
						$.ajax({
							type : "post", //请求方式
							async : false, //是否异步
							url : "../ajax.do?ctrl=Index_getPage",
							data : {
								url : url,
								type : "jsp"
							},
							dataType : "json",
							success : function(obj) {
								if (obj != null && obj.code == '000') {
									hh = obj.data;
								} else {
									hh = '&lt;%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>\n' +
										'&lt;%\n' +
										'String path = request.getContextPath();\n' +
										'String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";\n' +
										'%>\n' +
										'\n' +
										'<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">\n' +
										'<html>\n' +
										'<head>\n' +
										'<base href="&lt;%=basePath%>">\n' +
										'\n' +
										'<title></title>\n' +
										'\n' +
										'<meta http-equiv="pragma" content="no-cache">\n' +
										'<meta http-equiv="cache-control" content="no-cache">\n' +
										'<meta http-equiv="expires" content="0">\n' +
										'<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">\n' +
										'<meta http-equiv="description" content="This is my page">\n' +
										'<!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->\n'+
										'<!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->\n' +
										'</head>\n' +
										'<body>\n' +
										'  \n' +
										'</body>\n' +
										'</html>\n\n\n\n\n\n\n\n';
									hh = hh.replace("&lt;", "<");
									hh = hh.replace("&lt;", "<");
									hh = hh.replace("&lt;", "<");
								}
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
						editor_jsp.setValue(hh);
						$.ajax({
							type : "post", //请求方式
							async : false, //是否异步
							url : "../ajax.do?ctrl=Index_getPage",
							data : {
								url : url,
								type : "js"
							},
							dataType : "json",
							success : function(obj) {
								if (obj != null && obj.code == '000') {
									hh = obj.data;
								} else {
									hh = "//js注释\n";
								}
	
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
						editor_js.setValue(hh);
						$.ajax({
							type : "post", //请求方式
							async : false, //是否异步
							url : "../ajax.do?ctrl=Index_getPage",
							data : {
								url : url,
								type : "css"
							},
							dataType : "json",
							success : function(obj) {
								if (obj != null && obj.code == '000') {
									hh = obj.data;
								} else {
									hh = "/* css注释 */\n"
								}
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
						editor_css.setValue(hh);
					} else if (index == 1) {
						$("#yl").attr("src", "../index.do?ctrl=" + url);
					} else if (index == 2) { //java
						if (idx == 0) {
							editor = CodeMirror.fromTextArea(document.getElementById("java_code"), {
								lineNumbers : true, // 显示行数
								indentUnit : 4, // 缩进单位为2
								styleActiveLine : true, // 当前行背景高亮
								matchBrackets : true, // 括号匹配
								mode : "text/x-java",
								theme : 'blackboard', // 使用monokai模版
								smartIndent : true, // 自动缩进
								showCursorWhenSelecting : true, // 在选择时是否显示光标，默认为false 
								//keyMap:"vim",		   // 绑定Vim
								// 代码折叠
								//lineWrapping : true, // 自动换行
								foldGutter : true,
								gutters : [ "CodeMirror-linenumbers", "CodeMirror-foldgutter" ],
								//全屏模式
								//fullScreen : true,
								cursorHeight : 0.85,
							});
							editor.setOption("extraKeys", {
								// Tab键换成4个空格
								Tab : function(cm) {
									var spaces = Array(cm.getOption("indentUnit") + 1).join(" ");
									cm.replaceSelection(spaces);
								},
								"Alt-/" : "autocomplete",
							});
							var hh = '';
							$.ajax({
								type : "post", //请求方式
								async : false, //是否异步
								url : "../ajax.do?ctrl=Index_getPage",
								data : {
									url : url,
									type : "java"
								},
								dataType : "json",
								success : function(obj) {
									if (obj != null && obj.code == '000') {
										hh = obj.data;
									} else {
										hh = 'package fast.app;\n' +
											'\n' +
											'import java.util.Map;\n' +
											'import fast.main.conf.ApiBody;\n' +
											'import fast.main.conf.ApiHead;\n' +
											'import fast.main.util.Super;\n' +
											'' +
											'@ApiHead(value="' + sms + '")\n' +
											'public class ' + url + ' extends Super{\n' +
											'	@ApiBody(name = "初始化", context = "方法说明(在首页API文档详情中显示)", type = "Post")\n' +
											'	public String init(Map<String, Object> rmap) {\n' +
											'		try {\n' +
											'			//初始化         bs（数据库访问）、request（请求）,response（响应）,par（layui,table自定义参数）\n' +
											'			initMap(rmap);\n' +
											'			return "xtgl/' + url + '";\n' +
											'		} catch (Exception e) {\n' +
											'			e.printStackTrace();\n' +
											'			return "xtgl/' + url + '";\n' +
											'		}\n' +
											'	}\n\n\n\n' +
											'}\n\n\n\n';
									}
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
							editor.setValue(hh);
						}
						idx += 1;
					}
				}
			});
	
	
	
	
			$(document).keydown(function(e) {
				if (e.ctrlKey && e.which == 83) {
					e.preventDefault();
					//如果支持window.event(IE肯定是支持的)
					if (window.event) {
						window.event.keyCode = false;
					}
					if (tds == 0) {
						str_jsp = editor_jsp.getValue(); //获取代码框的值
						str_js = editor_js.getValue(); //获取代码框的值
						str_css = editor_css.getValue(); //获取代码框的值
						$.ajax({
							type : "post", //请求方式
							async : false, //是否异步
							url : "../ajax.do?ctrl=Index_save",
							data : {
								url : url,
								type : "jsp",
								str_jsp : str_jsp,
								str_js : str_js,
								str_css : str_css
							},
							dataType : "json",
							success : function(obj) {
								if (obj != null && obj.code == '000') {
									hh = obj.data;
									var f=$(window.parent.document).find('.tree-item');
									for(var i=0;i<f.length;i++){
										var ff=$(f[i]).text();
										if(ff==url+"*"){
											console.log(url);
											$(f[i]).find("a").text(url);
											$(f[i]).find("a").css('color','black');
											
										}
									}
								} 
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
					} else if (tds == 1) {
						alert("当前为网页，无法保存！");
					} else if (tds == 2) {
						str = editor.getValue(); //获取代码框的值
						$.ajax({
							type : "post", //请求方式
							async : false, //是否异步
							url : "../ajax.do?ctrl=Index_save",
							data : {
								url : url,
								type : "java",
								str : str
							},
							dataType : "json",
							success : function(obj) {
								if (obj != null && obj.code == '000') {
									hh = obj.data;
									var f=$(window.parent.document).find('.tree-item');
									for(var i=0;i<f.length;i++){
										var ff=$(f[i]).text();
										if(ff==url+"*"){
											$(f[i]).find("a").text(url);
											$(f[i]).find("a").css('color','black');
										}
									}
								} 
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
					}
	
				}else{
					var f=$(window.parent.document).find('.tree-item');
					for(var i=0;i<f.length;i++){
						var ff=$(f[i]).text();
						if(ff==url){
							$(f[i]).find("a").css('color','blue');
							$(f[i]).find("a").text(ff+"*");
						}
					}
				}
	
			});
			bc();
			function bc() {
				str_jsp = editor_jsp.getValue(); //获取代码框的值
				str_js = editor_js.getValue(); //获取代码框的值
				str_css = editor_css.getValue(); //获取代码框的值
				$.ajax({
					type : "post", //请求方式
					async : false, //是否异步
					url : "../ajax.do?ctrl=Index_save",
					data : {
						url : url,
						type : "jsp",
						str_jsp : str_jsp,
						str_js : str_js,
						str_css : str_css
					},
					dataType : "json",
					success : function(obj) {
						if (obj != null && obj.code == '000') {
							hh = obj.data;
						} else {
	
						}
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
				addTab("预览", "../index.do?ctrl=" + url);
				addTab("java", url);
				str = editor.getValue(); //获取代码框的值
	
				$.ajax({
					type : "post", //请求方式
					async : false, //是否异步
					url : "../ajax.do?ctrl=Index_save",
					data : {
						url : url,
						type : "java",
						str : str
					},
					dataType : "json",
					success : function(obj) {
						if (obj != null && obj.code == '000') {
							hh = obj.data;
						} else {
	
						}
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
				addTab("html", url);
			}
	
		}
	</script>
</body>
</html>