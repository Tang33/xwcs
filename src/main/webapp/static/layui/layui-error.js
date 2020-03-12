/**
 * 重写layui中ajax请求
 */
	//重写layui的Ajax请求
	if (!(typeof layui == "undefined")) {
	    layui.use(['layer', 'jquery'], function () {
	        var layer = layui.layer,
	            $ = layui.jquery;
	        //首先备份下jquery的ajax方法
	        var _ajax = $.ajax;
	        //重写jquery的ajax方法
	        var flashLoad;
	        $.ajax = function (opt) {
	            //备份opt中error和success方法
	            var fn = {
	                error: function (XMLHttpRequest, textStatus, errorThrown) {
	                },
	                success: function (data, textStatus) {
	                }
	            }
	            if (opt.error) {
	                fn.error = opt.error;
	            }
	            if (opt.success) {
	                fn.success = opt.success;
	            }
	            //扩展增强处理
	            var _opt = $.extend(opt, {
	                error: function (XMLHttpRequest, textStatus, errorThrown) {
	                    //错误方法增强处理
	                    if ('TIMEOUT' == XMLHttpRequest.getResponseHeader('SESSIONS_TATUS')) {
	                        parent.window.parent.window.location.href = XMLHttpRequest.getResponseHeader('content_path');
	                    }
	                    fn.error(XMLHttpRequest, textStatus, errorThrown);
	                },
	                success: function (data, textStatus) {
	                    //成功回调方法增强处理
	                    if (-1 == data.status || '-1' == data.status || 0 == data.status || '0' == data.status) {
	                        return layer.msg(data.tip);
	                    }
	                    fn.success(data, textStatus);
	                },
	                beforeSend: function (XHR, response) {
	                    /**
	                     * 修复layui分页bug，pageNum属性-1适应后端查询
	                     */
	                   /* var urlParams = util.url.getUrlAllParams(response.url);
	                    if (urlParams && urlParams.pageNum) {
	                        var urlIndex = response.url.substring(0, response.url.indexOf('?') + 1);
	                        urlParams.pageNum = urlParams.pageNum-1;
	                        for (var item in urlParams) {
	                            urlIndex += (item + '=' + urlParams[item]) + '&';
	                        }
	                        response.url = urlIndex.substring(0, urlIndex.length-1);
	                    }
	                    //提交前回调方法
	                    flashLoad = layer.load(0, {shade: [0.7, '#393D49']}, {shadeClose: true}); //0代表加载的风格，支持0-2
*/	                },
	                complete: function (XHR, TS) {
	                	 
	                    //请求完成后回调函数 (请求成功或失败之后均调用)。
	                	if(TS == 'parsererror'){
//	                		layer.msg("登录状态已失效,3秒后跳转登录页!");
//	                		setTimeout(function(){ 
//	                			top.location.href='jsp/login.jsp';
//	                		},3000);
	                		countDown(5,"未登录")
	                		
	                	}
	                    //layer.close(flashLoad);
	                }
	            });
	            return _ajax(_opt);
	        }
	    });
	};
	
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

