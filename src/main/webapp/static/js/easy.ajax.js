/**
 * easy.ajax 1.0
 * User: 郏高阳 Jason
 * Date: 2017-5-20
 * 该框架依赖jquery,请先引入jquery1.5+版本。
 */
;
(function (window, $) {
    var EasyAjax = {};
    var _ajaxType = ['get', 'POST', 'put', 'delete', 'options', 'head', 'connect', 'trace'];
    var _ajaxDataType = ['json', 'xml', 'html', 'script'];
    var _ajaxContentType = ["application/json", "application/x-www-form-urlencoded"];
    /**
     * Ajax Get请求
     * @param config Ajax需要参数
     * @param callback ajax结束回掉函数
     */
    EasyAjax.get = function (config, callback) {
        config.type = _ajaxType[0];
        _ajax(config, callback);
    };
    /**
     * Ajax Post请求
     * @param config Ajax需要参数
     * @param callback ajax结束回掉函数
     */
    EasyAjax.post = function (config, callback) {
        config.type = _ajaxType[1];
        _ajax(config, callback);
    };
    /**
     * Ajax Get请求数据格式是JSON
     * @param config Ajax需要参数可只配URL
     * @param callback ajax结束回掉函数
     */
    EasyAjax.get_json = function (config, callback) {
        config.type = _ajaxType[0];
        config.dataType = _ajaxDataType[0];
        _ajax(config, callback);
    };
    /**
     * 文件上传Ajax
     * @param config ajax配置项
     * @param callback 回调函数
     */
    EasyAjax.upload_file = function (config, callback) {
        config.type = _ajaxType[1];
        config.dataType = _ajaxDataType[0];
        config.cache = false;
        config.contentType = false;
        config.processData = false;
        _ajax(config, callback);
    };
    /**
     * 根据dom元素上传文件
     * @param config
     * @param callback  回掉函数
     * @param elem 文件dom id or class
     */
    EasyAjax.Upload_File_Elem = function (config, callback) {
        var file = $(config.elem).get(0).files[0];
        var fileData = new FormData();
        fileData.append("file", file);
        config.type = _ajaxType[1];
        config.dataType = _ajaxDataType[0];
        config.cache = false;
        config.contentType = false;
        config.processData = false;
        config.data = fileData;
        _ajax(config, callback);
    };
    /**
     * Ajax post请求数据格式是JSON，接口使用@RequestParam时使用，使用form表单提交方式
     * @param config Ajax需要参数可只配URL
     * @param callback ajax结束回掉函数
     */
    EasyAjax.post_form_json = function (config, callback) {
        config.type = _ajaxType[1];
        config.dataType = _ajaxDataType[0];
        if (!config.contentType) {
            config.contentType = _ajaxContentType[1];
        }
        _ajax(config, callback);
    };

    /**
     * Ajax post请求数据格式是JSON
     * 接口使用@RequestBody时使用自动添加contentType以及格式化参数
     * @param config Ajax需要参数可只配URL
     * @param callback ajax结束回掉函数
     */
    EasyAjax.post_json = function (config, callback) {
        config.type = _ajaxType[1];
        config.dataType = _ajaxDataType[0];
        if (!config.contentType) {
            config.contentType = _ajaxContentType[0];
        }
        if (config.contentType == 'application/json') {
            config.data = JSON.stringify(config.data);
        }
        _ajax(config, callback);
    };

    function _ajax(config, callback) {
        $.ajax({
            url: config.url,
            type: config.type,
            data: config.data,
            dataType: config.dataType,
            contentType: config.contentType,
            timeout: config.timeout,
            async: config.async,
            cache: config.cache,
            processData: config.processData,
            beforeSend: function () {
                //todo 此处可以写加载中动画……
            },
            success: function (_resultData) {
                if (_resultData.success || config.mustCallback) {
                    (callback && typeof(callback) === "function") && callback(_resultData);
                } else {
                    alert(_resultData.message);
                    // todo 这里可以做登录超时，错误提示……等处理
                }
            },
            error: function (XMLHttpRequest) {
                _handleStatus(XMLHttpRequest.status);
            },
            complete: function () {
                // todo 关闭动画处理……
            }
        });
    }

    function _handleStatus(status) {
        switch (status) {
            case 404:
                alert('请求资源不存在：#' + status);
                break;
            case 400:
                alert('请求参数有误：#' + status);
                break;
            case 500:
                alert('服务器异常：#' + status);
                break;
            case 504:
                alert('请求超时：#' + status);
                break;
            default:
                alert('未知故障：#' + status);
                break;
        }
    }
    window.EasyAjax = EasyAjax;
})(window, jQuery);
// API
/*
    文件上传
    var file = $("#upImgFile").get(0).files[0];
    var fileData = new FormData();
    fileData.append("upImg", file);
*/