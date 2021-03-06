<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<script type="text/javascript"></script>
<!-- 公共开始 -->
<script src="./js/jquery-2.2.4.min.js" charset="utf-8"></script>
<script src="./static/js/easy.ajax.js" charset="utf-8"></script>
<!-- 公共结束 -->

<!-- layui开始 -->
<link rel="stylesheet" href="./static/layui/css/layui.css" media="all">
<script src="./static/layui/layui.js" charset="utf-8"></script>
<script src="<%=basePath%>static/layui/layui-error.js" charset="utf-8"></script>
<link rel="stylesheet" href="./static/css/index.css">
<link rel="stylesheet" href="./static/css/chinaz.css">
<link rel="stylesheet" href="./static/css/button.css">
<!-- layui结束 -->
<script src="./js/aui.js" charset="utf-8"></script>
<!-- easyui开始 -->

<script type="text/javascript">
	function ajax(options) {
		options = options || {};
		options.type = (options.type || "GET").toUpperCase();
		options.dataType = options.dataType || "json";
		var paramss = formatparamss(options.data);
		//创建xhr对象 - 非IE6
		 
		if (window.XMLHttpRequest) {
			var xhr = new XMLHttpRequest();
			
		} else { //IE6及其以下版本浏览器
			var xhr = new ActiveXObject('Microsoft.XMLHTTP');
		}
		//GET POST 两种请求方式
		if (options.type == "GET") {
			xhr.open("GET", options.url + "?" + paramss, true);
			xhr.setRequestHeader("x-requested-with", "XMLHttpRequest");
			xhr.send(null);
		} else if (options.type == "POST") {
			xhr.open("POST", options.url, true);
			//设置表单提交时的内容类型
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			xhr.setRequestHeader("x-requested-with", "XMLHttpRequest");
			xhr.send(paramss);
		}
		//接收
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4) {
				var status = xhr.status;
				if (status == 200) {
					 
					//var sts=eval("("+xhr.responseText+")"); //强制转化成json对象
					var responseText = xhr.responseText;
					if(responseText == "loseSession"){//session失效
						/* layer.msg("登录状态已失效,3秒后跳转登录页!");
                		setTimeout(function(){ 
                			top.location.href='jsp/login.jsp';
                		},3000); */
                		countDown(5,"未登录");
					}
					var oobj = JSON.parse(responseText);
					options.success && options.success(oobj);
				} else {
					error(xhr);
				}
			}
		}
	}
	function error(xhr) {
		// 状态码
		 
		var str = "状态码：" + xhr.status + "\n状态：" + xhr.readyState + "\n错误信息：" + xhr.responseText;
		//log('ajax请求异常：',str);
		aui.error('ajax请求异常：', str);
	}

	//格式化参数
	function formatparamss(data) {
		var arr = [];
		for (var name in data) {
			arr.push(encodeURIComponent(name) + "=" + encodeURIComponent(data[name]));
		}
		arr.push(("v=" + Math.random()).replace(".", ""));
		return arr.join("&");
	}
	function countDown(second,content){
		parent.layer.msg(content, {
	        time : 5000,
	        shade: 0.6,
	        success: function(layero,index){//弹出成功的回调
	            //var msg = layero.text();
	            var i = second;
	            var timer = null;
	            var fn = function() {
	            layero.find(".layui-layer-content").text('登录状态已失效， '+i+' 跳转到登录页面!');
	            if(!i) {
	                layer.close(index);
	                clearInterval(timer);
	            } 
	            i--;
	            };
	            timer = setInterval(fn, 1000);
	            fn();
	        },
	        }, function() {//关闭的回调
	        	top.location.href = "jsp/login.jsp";
	    });
	}
</script>

<style>
.sweet-overlay {
	background-color: rgba(0, 0, 0, 0.4);
	position: fixed;
	left: 0;
	right: 0;
	top: 0;
	bottom: 0;
	display: none;
	z-index: 1000
}

.sweet-alert {
	background-color: white;
	font-family: 'Microsoft Yahei';
	width: 478px;
	padding: 17px;
	border-radius: 5px;
	text-align: center;
	position: fixed;
	left: 50%;
	top: 50%;
	margin-left: -256px;
	margin-top: -200px;
	overflow: hidden;
	display: none;
	z-index: 2000
}

@media all and (max-width:540px) {
	.sweet-alert {
		width: auto;
		margin-left: 0;
		margin-right: 0;
		left: 15px;
		right: 15px
	}
}

.sweet-alert h2 {
	color: #575757;
	font-size: 30px;
	text-align: center;
	font-weight: 600;
	text-transform: none;
	position: relative
}

.sweet-alert p {
	color: #797979;
	font-size: 16px;
	text-align: center;
	font-weight: 300;
	position: relative;
	margin: 0;
	line-height: normal
}

.sweet-alert button {
	font-family: Arial, Helvetica, sans-serif;
	background-color: #aedef4;
	color: white;
	border: 0;
	box-shadow: none;
	font-size: 17px;
	font-weight: 500;
	border-radius: 5px;
	padding: 10px 32px;
	margin: 26px 5px 0 5px;
	cursor: pointer
}

.sweet-alert button:focus {
	outline: 0;
	box-shadow: 0 0 2px rgba(128, 179, 235, 0.5), inset 0 0 0 1px
		rgba(0, 0, 0, 0.05)
}

.sweet-alert button:hover {
	background-color: #a1d9f2
}

.sweet-alert button:active {
	background-color: #81ccee
}

.sweet-alert button.cancel {
	background-color: #d0d0d0
}

.sweet-alert button.cancel:hover {
	background-color: #c8c8c8
}

.sweet-alert button.cancel:active {
	background-color: #b6b6b6
}

.sweet-alert button.cancel:focus {
	box-shadow: rgba(197, 205, 211, 0.8) 0 0 2px, rgba(0, 0, 0, 0.0470588) 0
		0 0 1px inset !important
}

.sweet-alert[data-has-cancel-button=false] button {
	box-shadow: none !important
}

.sweet-alert .icon {
	width: 80px;
	height: 80px;
	border: 4px solid gray;
	border-radius: 50%;
	margin: 20px auto;
	position: relative;
	box-sizing: content-box
}

.sweet-alert .icon.error {
	border-color: #f27474
}

.sweet-alert .icon.error .x-mark {
	position: relative;
	display: block
}

.sweet-alert .icon.error .line {
	position: absolute;
	height: 5px;
	width: 47px;
	background-color: #f27474;
	display: block;
	top: 37px;
	border-radius: 2px
}

.sweet-alert .icon.error .line.left {
	-webkit-transform: rotate(45deg);
	transform: rotate(45deg);
	left: 17px
}

.sweet-alert .icon.error .line.right {
	-webkit-transform: rotate(-45deg);
	transform: rotate(-45deg);
	right: 16px
}

.sweet-alert .icon.warning {
	border-color: #f8bb86
}

.sweet-alert .icon.warning .body {
	position: absolute;
	width: 5px;
	height: 47px;
	left: 50%;
	top: 10px;
	border-radius: 2px;
	margin-left: -2px;
	background-color: #f8bb86
}

.sweet-alert .icon.warning .dot {
	position: absolute;
	width: 7px;
	height: 7px;
	border-radius: 50%;
	margin-left: -3px;
	left: 50%;
	bottom: 10px;
	background-color: #f8bb86
}

.sweet-alert .icon.info {
	border-color: #c9dae1
}

.sweet-alert .icon.info::before {
	content: "";
	position: absolute;
	width: 5px;
	height: 29px;
	left: 50%;
	bottom: 17px;
	border-radius: 2px;
	margin-left: -2px;
	background-color: #c9dae1
}

.sweet-alert .icon.info::after {
	content: "";
	position: absolute;
	width: 7px;
	height: 7px;
	border-radius: 50%;
	margin-left: -3px;
	top: 19px;
	background-color: #c9dae1
}

.sweet-alert .icon.success {
	border-color: #a5dc86
}

.sweet-alert .icon.success::before, .sweet-alert .icon.success::after {
	content: '';
	border-radius: 50%;
	position: absolute;
	width: 60px;
	height: 120px;
	background: white;
	transform: rotate(45deg)
}

.sweet-alert .icon.success::before {
	border-radius: 120px 0 0 120px;
	top: -7px;
	left: -33px;
	-webkit-transform: rotate(-45deg);
	transform: rotate(-45deg);
	-webkit-transform-origin: 60px 60px;
	transform-origin: 60px 60px
}

.sweet-alert .icon.success::after {
	border-radius: 0 120px 120px 0;
	top: -11px;
	left: 30px;
	-webkit-transform: rotate(-45deg);
	transform: rotate(-45deg);
	-webkit-transform-origin: 0 60px;
	transform-origin: 0 60px
}

.sweet-alert .icon.success .placeholder {
	width: 80px;
	height: 80px;
	border: 4px solid rgba(165, 220, 134, 0.2);
	border-radius: 50%;
	box-sizing: content-box;
	position: absolute;
	left: -4px;
	top: -4px;
	z-index: 2
}

.sweet-alert .icon.success .fix {
	width: 5px;
	height: 90px;
	background-color: white;
	position: absolute;
	left: 28px;
	top: 8px;
	z-index: 1;
	-webkit-transform: rotate(-45deg);
	transform: rotate(-45deg)
}

.sweet-alert .icon.success .line {
	height: 5px;
	background-color: #a5dc86;
	display: block;
	border-radius: 2px;
	position: absolute;
	z-index: 2
}

.sweet-alert .icon.success .line.tip {
	width: 25px;
	left: 14px;
	top: 46px;
	-webkit-transform: rotate(45deg);
	transform: rotate(45deg)
}

.sweet-alert .icon.success .line.long {
	width: 47px;
	right: 8px;
	top: 38px;
	-webkit-transform: rotate(-45deg);
	transform: rotate(-45deg)
}

.sweet-alert .icon.custom {
	background-size: contain;
	border-radius: 0;
	border: 0;
	background-position: center center;
	background-repeat: no-repeat
}

@
-webkit-keyframes showSweetAlert { 0%{
	transform: scale(0.7);
	-webkit-transform: scale(0.7)
}

45%{
transform
:scale
(1
.05
);-webkit-transform
:scale
(1
.05
)
}
80%{
transform
:scale
(0
.95
);-webkit-tranform
:scale
(0
.95
)
}
100%{
transform
:scale(1)
;-webkit-transform
:scale(1)
}
}
@
-moz-keyframes showSweetAlert { 0%{
	transform: scale(0.7);
	-webkit-transform: scale(0.7)
}

45%{
transform
:scale
(1
.05
);-webkit-transform
:scale
(1
.05
)
}
80%{
transform
:scale
(0
.95
);-webkit-tranform
:scale
(0
.95
)
}
100%{
transform
:scale(1)
;-webkit-transform
:scale(1)
}
}
@
keyframes showSweetAlert { 0%{
	transform: scale(0.7);
	-webkit-transform: scale(0.7)
}

45%{
transform
:scale
(1
.05
);-webkit-transform
:scale
(1
.05
)
}
80%{
transform
:scale
(0
.95
);-webkit-tranform
:scale
(0
.95
)
}
100%{
transform
:scale(1)
;-webkit-transform
:scale(1)
}
}
@
-webkit-keyframes hideSweetAlert { 0%{
	transform: scale(1);
	-webkit-transform: scale(1)
}

100%{
transform
:scale
(0
.5
);-webkit-transform
:scale
(0
.5
)
}
}
@
-moz-keyframes hideSweetAlert { 0%{
	transform: scale(1);
	-webkit-transform: scale(1)
}

100%{
transform
:scale
(0
.5
);-webkit-transform
:scale
(0
.5
)
}
}
@
keyframes hideSweetAlert { 0%{
	transform: scale(1);
	-webkit-transform: scale(1)
}

100%{
transform
:scale
(0
.5
);-webkit-transform
:scale
(0
.5
)
}
}
.showSweetAlert {
	-webkit-animation: showSweetAlert .3s;
	-moz-animation: showSweetAlert .3s;
	animation: showSweetAlert .3s
}

.hideSweetAlert {
	-webkit-animation: hideSweetAlert .2s;
	-moz-animation: hideSweetAlert .2s;
	animation: hideSweetAlert .2s
}

@
-webkit-keyframes animateSuccessTip { 0%{
	width: 0;
	left: 1px;
	top: 19px
}

54%{
width
:
0;left
:
1px;top
:
19px
}
70%{
width
:
50px;left
:
-8px;top
:
37px
}
84%{
width
:
17px;left
:
21px;top
:
48px
}
100%{
width
:
25px;left
:
14px;top
:
45px
}
}
@
-moz-keyframes animateSuccessTip { 0%{
	width: 0;
	left: 1px;
	top: 19px
}

54%{
width
:
0;left
:
1px;top
:
19px
}
70%{
width
:
50px;left
:
-8px;top
:
37px
}
84%{
width
:
17px;left
:
21px;top
:
48px
}
100%{
width
:
25px;left
:
14px;top
:
45px
}
}
@
keyframes animateSuccessTip { 0%{
	width: 0;
	left: 1px;
	top: 19px
}

54%{
width
:
0;left
:
1px;top
:
19px
}
70%{
width
:
50px;left
:
-8px;top
:
37px
}
84%{
width
:
17px;left
:
21px;top
:
48px
}
100%{
width
:
25px;left
:
14px;top
:
45px
}
}
@
-webkit-keyframes animateSuccessLong { 0%{
	width: 0;
	right: 46px;
	top: 54px
}

65%{
width
:
0;right
:
46px;top
:
54px
}
84%{
width
:
55px;right
:
0;top
:
35px
}
100%{
width
:
47px;right
:
8px;top
:
38px
}
}
@
-moz-keyframes animateSuccessLong { 0%{
	width: 0;
	right: 46px;
	top: 54px
}

65%{
width
:
0;right
:
46px;top
:
54px
}
84%{
width
:
55px;right
:
0;top
:
35px
}
100%{
width
:
47px;right
:
8px;top
:
38px
}
}
@
keyframes animateSuccessLong { 0%{
	width: 0;
	right: 46px;
	top: 54px
}

65%{
width
:
0;right
:
46px;top
:
54px
}
84%{
width
:
55px;right
:
0;top
:
35px
}
100%{
width
:
47px;right
:
8px;top
:
38px
}
}
@
-webkit-keyframes rotatePlaceholder { 0%{
	transform: rotate(-45deg);
	-webkit-transform: rotate(-45deg)
}

5%{
transform
:rotate(-45deg)
;-webkit-transform
:rotate(-45deg)
}
12%{
transform
:rotate(-405deg)
;-webkit-transform
:rotate(-405deg)
}
100%{
transform
:rotate(-405deg)
;-webkit-transform
:rotate(-405deg)
}
}
@
-moz-keyframes rotatePlaceholder { 0%{
	transform: rotate(-45deg);
	-webkit-transform: rotate(-45deg)
}

5%{
transform
:rotate(-45deg)
;-webkit-transform
:rotate(-45deg)
}
12%{
transform
:rotate(-405deg)
;-webkit-transform
:rotate(-405deg)
}
100%{
transform
:rotate(-405deg)
;-webkit-transform
:rotate(-405deg)
}
}
@
keyframes rotatePlaceholder { 0%{
	transform: rotate(-45deg);
	-webkit-transform: rotate(-45deg)
}

5%{
transform
:rotate(-45deg)
;-webkit-transform
:rotate(-45deg)
}
12%{
transform
:rotate(-405deg)
;-webkit-transform
:rotate(-405deg)
}
100%{
transform
:rotate(-405deg)
;-webkit-transform
:rotate(-405deg)
}
}
.animateSuccessTip {
	-webkit-animation: animateSuccessTip .75s;
	-moz-animation: animateSuccessTip .75s;
	animation: animateSuccessTip .75s
}

.animateSuccessLong {
	-webkit-animation: animateSuccessLong .75s;
	-moz-animation: animateSuccessLong .75s;
	animation: animateSuccessLong .75s
}

.icon.success.animate::after {
	-webkit-animation: rotatePlaceholder 4.25s ease-in;
	-moz-animation: rotatePlaceholder 4.25s ease-in;
	animation: rotatePlaceholder 4.25s ease-in
}

@
-webkit-keyframes animateErrorIcon { 0%{
	transform: rotateX(100deg);
	-webkit-transform: rotateX(100deg);
	opacity: 0
}

100%{
transform
:rotateX(0deg)
;-webkit-transform
:rotateX(0deg)
;opacity
:
1
}
}
@
-moz-keyframes animateErrorIcon { 0%{
	transform: rotateX(100deg);
	-webkit-transform: rotateX(100deg);
	opacity: 0
}

100%{
transform
:rotateX(0deg)
;-webkit-transform
:rotateX(0deg)
;opacity
:
1
}
}
@
keyframes animateErrorIcon { 0%{
	transform: rotateX(100deg);
	-webkit-transform: rotateX(100deg);
	opacity: 0
}

100%{
transform
:rotateX(0deg)
;-webkit-transform
:rotateX(0deg)
;opacity
:
1
}
}
.animateErrorIcon {
	-webkit-animation: animateErrorIcon .5s;
	-moz-animation: animateErrorIcon .5s;
	animation: animateErrorIcon .5s
}

@
-webkit-keyframes animateXMark { 0%{
	transform: scale(0.4);
	-webkit-transform: scale(0.4);
	margin-top: 26px;
	opacity: 0
}

50%{
transform
:scale
(0
.4
);-webkit-transform
:scale
(0
.4
);margin-top
:
26px;opacity
:
0
}
80%{
transform
:scale
(1
.15
);-webkit-transform
:scale
(1
.15
);margin-top
:
-6px
}
100%{
transform
:scale(1)
;-webkit-transform
:scale(1)
;margin-top
:
0;opacity
:
1
}
}
@
-moz-keyframes animateXMark { 0%{
	transform: scale(0.4);
	-webkit-transform: scale(0.4);
	margin-top: 26px;
	opacity: 0
}

50%{
transform
:scale
(0
.4
);-webkit-transform
:scale
(0
.4
);margin-top
:
26px;opacity
:
0
}
80%{
transform
:scale
(1
.15
);-webkit-transform
:scale
(1
.15
);margin-top
:
-6px
}
100%{
transform
:scale(1)
;-webkit-transform
:scale(1)
;margin-top
:
0;opacity
:
1
}
}
@
keyframes animateXMark { 0%{
	transform: scale(0.4);
	-webkit-transform: scale(0.4);
	margin-top: 26px;
	opacity: 0
}

50%{
transform
:scale
(0
.4
);-webkit-transform
:scale
(0
.4
);margin-top
:
26px;opacity
:
0
}
80%{
transform
:scale
(1
.15
);-webkit-transform
:scale
(1
.15
);margin-top
:
-6px
}
100%{
transform
:scale(1)
;-webkit-transform
:scale(1)
;margin-top
:
0;opacity
:
1
}
}
.animateXMark {
	-webkit-animation: animateXMark .5s;
	-moz-animation: animateXMark .5s;
	animation: animateXMark .5s
}

@
-webkit-keyframes pulseWarning { 0%{
	border-color: #f8d486
}

100%{
border-color
:
#f8bb86
}
}
@
-moz-keyframes pulseWarning { 0%{
	border-color: #f8d486
}

100%{
border-color
:
#f8bb86
}
}
@
keyframes pulseWarning { 0%{
	border-color: #f8d486
}

100%{
border-color
:
#f8bb86
}
}
.pulseWarning {
	-webkit-animation: pulseWarning .75s infinite alternate;
	-moz-animation: pulseWarning .75s infinite alternate;
	animation: pulseWarning .75s infinite alternate
}

@
-webkit-keyframes pulseWarningIns { 0%{
	background-color: #f8d486
}

100%{
background-color
:
#f8bb86
}
}
@
-moz-keyframes pulseWarningIns { 0%{
	background-color: #f8d486
}

100%{
background-color
:
#f8bb86
}
}
@
keyframes pulseWarningIns { 0%{
	background-color: #f8d486
}

100%{
background-color
:
#f8bb86
}
}
.pulseWarningIns {
	-webkit-animation: pulseWarningIns .75s infinite alternate;
	-moz-animation: pulseWarningIns .75s infinite alternate;
	animation: pulseWarningIns .75s infinite alternate
}
</style>
<script type="text/javascript">
!function(e,t){function n(){var e='<div class="sweet-overlay" tabIndex="-1"></div><div class="sweet-alert" tabIndex="-1"><div class="icon error"><span class="x-mark"><span class="line left"></span><span class="line right"></span></span></div><div class="icon warning"> <span class="body"></span> <span class="dot"></span> </div> <div class="icon info"></div> <div class="icon success"> <span class="line tip"></span> <span class="line long"></span> <div class="placeholder"></div> <div class="fix"></div> </div> <div class="icon custom"></div> <h2>Title</h2><p>Text</p><button class="cancel" tabIndex="2">Cancel</button><button class="confirm" tabIndex="1">确定</button></div>',n=t.createElement("div");n.innerHTML=e,t.body.appendChild(n)}function o(t){var n=y(),o=n.querySelector("h2"),r=n.querySelector("p"),a=n.querySelector("button.cancel"),c=n.querySelector("button.confirm");if(o.innerHTML=b(t.title).split("\n").join("<br>"),r.innerHTML=b(t.text||"").split("\n").join("<br>"),t.text&&w(r),x(n.querySelectorAll(".icon")),t.type){for(var l=!1,s=0;s<f.length;s++){if(t.type===f[s]){l=!0;break}}if(!l){return e.console.error("Unknown alert type: "+t.type),!1}var u=n.querySelector(".icon."+t.type);switch(w(u),t.type){case"success":g(u,"animate"),g(u.querySelector(".tip"),"animateSuccessTip"),g(u.querySelector(".long"),"animateSuccessLong");break;case"error":g(u,"animateErrorIcon"),g(u.querySelector(".x-mark"),"animateXMark");break;case"warning":g(u,"pulseWarning"),g(u.querySelector(".body"),"pulseWarningIns"),g(u.querySelector(".dot"),"pulseWarningIns")}}if(t.imageUrl){var d=n.querySelector(".icon.custom");d.style.backgroundImage="url("+t.imageUrl+")",w(d);var p=80,m=80;if(t.imageSize){var v=t.imageSize.split("x")[0],h=t.imageSize.split("x")[1];v&&h?(p=v,m=h,d.css({width:v+"px",height:h+"px"})):e.console.error("Parameter imageSize expects value with format WIDTHxHEIGHT, got "+t.imageSize)}d.setAttribute("style",d.getAttribute("style")+"width:"+p+"px; height:"+m+"px")}n.setAttribute("data-has-cancel-button",t.showCancelButton),t.showCancelButton?a.style.display="inline-block":x(a),t.cancelButtonText&&(a.innerHTML=b(t.cancelButtonText)),t.confirmButtonText&&(c.innerHTML=b(t.confirmButtonText)),c.style.backgroundColor=t.confirmButtonColor,i(c,t.confirmButtonColor),n.setAttribute("data-allow-ouside-click",t.allowOutsideClick);var S=t.doneFunction?!0:!1;n.setAttribute("data-has-done-function",S)}function r(e,t){e=String(e).replace(/[^0-9a-f]/gi,""),e.length<6&&(e=e[0]+e[0]+e[1]+e[1]+e[2]+e[2]),t=t||0;var n="#",o,r;for(r=0;3>r;r++){o=parseInt(e.substr(2*r,2),16),o=Math.round(Math.min(Math.max(0,o+o*t),255)).toString(16),n+=("00"+o).substr(o.length)}return n}function a(e){var t=/^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(e);return t?parseInt(t[1],16)+", "+parseInt(t[2],16)+", "+parseInt(t[3],16):null}function i(e,t){var n=a(t);e.style.boxShadow="0 0 2px rgba("+n+", 0.8), inset 0 0 0 1px rgba(0, 0, 0, 0.05)"}function c(){var e=y();B(p(),10),w(e),g(e,"showSweetAlert"),v(e,"hideSweetAlert"),I=t.activeElement;var n=e.querySelector("button.confirm");n.focus(),setTimeout(function(){g(e,"visible")},500)}function l(){var n=y();T(p(),5),T(n,5),v(n,"showSweetAlert"),g(n,"hideSweetAlert"),v(n,"visible");var o=n.querySelector(".icon.success");v(o,"animate"),v(o.querySelector(".tip"),"animateSuccessTip"),v(o.querySelector(".long"),"animateSuccessLong");var r=n.querySelector(".icon.error");v(r,"animateErrorIcon"),v(r.querySelector(".x-mark"),"animateXMark");var a=n.querySelector(".icon.warning");v(a,"pulseWarning"),v(a.querySelector(".body"),"pulseWarningIns"),v(a.querySelector(".dot"),"pulseWarningIns"),e.onkeydown=M,t.onclick=A,I&&I.focus(),L=void 0}function s(){var e=y();e.style.marginTop=C(y())}var u=".sweet-alert",d=".sweet-overlay",f=["error","warning","info","success"],y=function(){return t.querySelector(u)},p=function(){return t.querySelector(d)},m=function(e,t){return new RegExp(" "+t+" ").test(" "+e.className+" ")},g=function(e,t){m(e,t)||(e.className+=" "+t)},v=function(e,t){var n=" "+e.className.replace(/[\t\r\n]/g," ")+" ";if(m(e,t)){for(;n.indexOf(" "+t+" ")>=0;){n=n.replace(" "+t+" "," ")}e.className=n.replace(/^\s+|\s+$/g,"")}},b=function(e){var n=t.createElement("div");return n.appendChild(t.createTextNode(e)),n.innerHTML},h=function(e){e.style.opacity="",e.style.display="block"},w=function(e){if(e&&!e.length){return h(e)}for(var t=0;t<e.length;++t){h(e[t])}},S=function(e){e.style.opacity="",e.style.display="none"},x=function(e){if(e&&!e.length){return S(e)}for(var t=0;t<e.length;++t){S(e[t])}},k=function(e,t){for(var n=t.parentNode;null!==n;){if(n===e){return !0}n=n.parentNode}return !1},C=function(e){e.style.left="-9999px",e.style.display="block";var t=e.clientHeight,n=parseInt(getComputedStyle(e).getPropertyValue("padding"),10);return e.style.left="",e.style.display="none","-"+parseInt(t/2+n)+"px"},B=function(e,t){t=t||16,e.style.opacity=0,e.style.display="block";var n=+new Date,o=function(){e.style.opacity=+e.style.opacity+(new Date-n)/100,n=+new Date,+e.style.opacity<1&&setTimeout(o,t)
};o()},T=function(e,t){t=t||16,e.style.opacity=1;var n=+new Date,o=function(){e.style.opacity=+e.style.opacity-(new Date-n)/100,n=+new Date,+e.style.opacity>0?setTimeout(o,t):e.style.display="none"};o()},E=function(n){if(MouseEvent){var o=new MouseEvent("click",{view:e,bubbles:!1,cancelable:!0});n.dispatchEvent(o)}else{if(t.createEvent){var r=t.createEvent("MouseEvents");r.initEvent("click",!1,!1),n.dispatchEvent(r)}else{t.createEventObject?n.fireEvent("onclick"):"function"==typeof n.onclick&&n.onclick()}}},q=function(t){"function"==typeof t.stopPropagation?(t.stopPropagation(),t.preventDefault()):e.event&&e.event.hasOwnProperty("cancelBubble")&&(e.event.cancelBubble=!0)},I,A,M,L;e.sweetAlert=e.swal=function(){function n(e){var t=e.keyCode||e.which;if(-1!==[9,13,32,27].indexOf(t)){for(var n=e.target||e.srcElement,o=-1,r=0;r<h.length;r++){if(n===h[r]){o=r;break}}9===t?(n=-1===o?v:o===h.length-1?h[0]:h[o+1],q(e),n.focus(),i(n,u.confirmButtonColor)):(n=13===t||32===t?-1===o?v:void 0:27!==t||b.hidden||"none"===b.style.display?void 0:b,void 0!==n&&E(n,e))}}function a(e){var t=e.target||e.srcElement,n=e.relatedTarget,o=m(d,"visible");if(o){var r=-1;if(null!==n){for(var a=0;a<h.length;a++){if(n===h[a]){r=a;break}}-1===r&&t.focus()}else{L=t}}}var u={title:"",text:"",type:null,allowOutsideClick:!1,showCancelButton:!1,confirmButtonText:"确定",confirmButtonColor:"#AEDEF4",cancelButtonText:"Cancel",imageUrl:null,imageSize:null};if(void 0===arguments[0]){return e.console.error("sweetAlert expects at least 1 attribute!"),!1}switch(typeof arguments[0]){case"string":u.title=arguments[0],u.text=arguments[1]||"",u.type=arguments[2]||"";break;case"object":if(void 0===arguments[0].title){return e.console.error('Missing "title" argument!'),!1}u.title=arguments[0].title,u.text=arguments[0].text||u.text,u.type=arguments[0].type||u.type,u.allowOutsideClick=arguments[0].allowOutsideClick||u.allowOutsideClick,u.showCancelButton=arguments[0].showCancelButton||u.showCancelButton,u.confirmButtonText=u.showCancelButton?"Confirm":u.confirmButtonText,u.confirmButtonText=arguments[0].confirmButtonText||u.confirmButtonText,u.confirmButtonColor=arguments[0].confirmButtonColor||u.confirmButtonColor,u.cancelButtonText=arguments[0].cancelButtonText||u.cancelButtonText,u.imageUrl=arguments[0].imageUrl||u.imageUrl,u.imageSize=arguments[0].imageSize||u.imageSize,u.doneFunction=arguments[1]||null;break;default:return e.console.error('Unexpected type of argument! Expected "string" or "object", got '+typeof arguments[0]),!1}o(u),s(),c();for(var d=y(),f=function(e){var t=e.target||e.srcElement,n="confirm"===t.className,o=m(d,"visible"),a=u.doneFunction&&"true"===d.getAttribute("data-has-done-function");switch(e.type){case"mouseover":n&&(e.target.style.backgroundColor=r(u.confirmButtonColor,-0.04));break;case"mouseout":n&&(e.target.style.backgroundColor=u.confirmButtonColor);break;case"mousedown":n&&(e.target.style.backgroundColor=r(u.confirmButtonColor,-0.14));break;case"mouseup":n&&(e.target.style.backgroundColor=r(u.confirmButtonColor,-0.04));break;case"focus":var i=d.querySelector("button.confirm"),c=d.querySelector("button.cancel");n?c.style.boxShadow="none":i.style.boxShadow="none";break;case"click":n&&a&&o&&u.doneFunction(),l()}},p=d.querySelectorAll("button"),g=0;g<p.length;g++){p[g].onclick=f,p[g].onmouseover=f,p[g].onmouseout=f,p[g].onmousedown=f,p[g].onfocus=f}A=t.onclick,t.onclick=function(e){var t=e.target||e.srcElement,n=d===t,o=k(d,e.target),r=m(d,"visible"),a="true"===d.getAttribute("data-allow-ouside-click");!n&&!o&&r&&a&&l()};var v=d.querySelector("button.confirm"),b=d.querySelector("button.cancel"),h=d.querySelectorAll("button:not([type=hidden])");M=e.onkeydown,e.onkeydown=n,v.onblur=a,b.onblur=a,e.onfocus=function(){e.setTimeout(function(){void 0!==L&&(L.focus(),L=void 0)},0)}},function(){"complete"===t.readyState||"interactive"===t.readyState?n():t.addEventListener?t.addEventListener("DOMContentLoaded",function e(){t.removeEventListener("DOMContentLoaded",arguments.callee,!1),n()},!1):t.attachEvent&&t.attachEvent("onreadystatechange",function(){"complete"===t.readyState&&(t.detachEvent("onreadystatechange",arguments.callee),n())})}()}(window,document);
</script>
