<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

<head>
    <base href="<%=basePath%>">

    <title>数据导入</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <meta name="msapplication-TileColor" content="#3399cc"/>
    <!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
    <jsp:include page="../load.jsp"></jsp:include>

    <style type="text/css">

        .layui-form .layui-border-box .layui-table-view {
            max-width: 1700px;
        }

        i {
            display: inline-block;
            width: 18px;
            height: 18px;
            line-height: 18px;
            text-align: center
        }

        .label {
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
<blockquote class="layui-elem-quote layui-text">
    本功能页面用于对外部数据导入，请选择接口导入或文件导入两种方式进行导入数据文件，点击导入按钮进行数据的导入!
</blockquote>
<form class="layui-form" id="form" action="">
    <input id="cslx" name="cslx" type="hidden" value="0"/>
    <input id="zdyf" name="zdyf" type="hidden" value="0"/>
    <div class="layui-collapse" lay-filter="test">
        <div class="layui-colla-item">
            <div class="layui-colla-content  layui-show">

                <div class="layui-row">
                    <div class="layui-inline">
                      <%--  <input type="radio" id="JK" name="type" value="1" title="接口导入" checked=""  lay-filter="type">
                        <input type="radio" id="WJ" name="type" value="0" title="文件导入"
                               lay-filter="type" >--%>

                        <button type="button" class="layui-btn" id="jiekou"  lay-filter="jiekou">选择接口</button>
                        <button type="button" class="layui-btn" id="select"  lay-filter="select">选择文件</button>
                        <button type="button" class="layui-btn" id="doinput">
                            <i class="layui-icon" style="vertical-align:middle"></i>导入
                        </button>

                    </div>

                </div>
            </div>


            <div class="layui-row1" id="moset" style="margin-top: 5px;height: 30px;width: 50%;margin-left: 121px;">
                <!-- <span>123</span> -->
            </div>

        </div>
    </div>


    <fieldset class="layui-elem-field layui-field-title"
              style="margin-top: 20px;">
        <legend style="padding: 0px 15px;">查询显示</legend>

        <table class="layui-table" id="table" lay-filter="user">
            <thead>
            <tr>
                <th lay-data="{field:'wjm'}">文件名</th>
                <th lay-data="{field:'wjdrsl'}">文件导入数量</th>
                <th lay-data="{field:'qcsl'}">去除数量</th>
                <th lay-data="{field:'drsj'}">导入时间</th>
                <th lay-data="{field:'rkrq'}">入库日期</th>
                <th lay-data="{field:'bz'}">说明</th>
            </tr>
            </thead>
            <tbody>
            <tr data-index="0" class="">
                <td data-field="wjm">
                    <div class="layui-table-cell laytable-cell-3-wjm">Export_10070082(70001_75000).xls</div>
                </td>
                <td data-field="wjdrsl">
                    <div class="layui-table-cell laytable-cell-3-wjdrsl">5000</div>
                </td>
                <td data-field="qcsl">
                    <div class="layui-table-cell laytable-cell-3-qcsl">0</div>
                </td>
                <td data-field="drsj">
                    <div class="layui-table-cell laytable-cell-3-drsj">2019-09-15 17:12:11</div>
                </td>
                <td data-field="rkrq">
                    <div class="layui-table-cell laytable-cell-3-rkrq">201909</div>
                </td>
                <td data-field="bz">
                    <div class="layui-table-cell laytable-cell-3-bz">2019年9月上传文件</div>
                </td>
            </tr>
            <tr data-index="1" class="">
                <td data-field="wjm">
                    <div class="layui-table-cell laytable-cell-3-wjm">Export_10070082(65001_70000).xls</div>
                </td>
                <td data-field="wjdrsl">
                    <div class="layui-table-cell laytable-cell-3-wjdrsl">5000</div>
                </td>
                <td data-field="qcsl">
                    <div class="layui-table-cell laytable-cell-3-qcsl">0</div>
                </td>
                <td data-field="drsj">
                    <div class="layui-table-cell laytable-cell-3-drsj">2019-08-15 17:08:49</div>
                </td>
                <td data-field="rkrq">
                    <div class="layui-table-cell laytable-cell-3-rkrq">201908</div>
                </td>
                <td data-field="bz">
                    <div class="layui-table-cell laytable-cell-3-bz">2019年8月上传文件</div>
                </td>
            </tr>
            <tr data-index="2" class="">
                <td data-field="wjm">
                    <div class="layui-table-cell laytable-cell-3-wjm">Export_10070082(60001_65000).xls</div>
                </td>
                <td data-field="wjdrsl">
                    <div class="layui-table-cell laytable-cell-3-wjdrsl">5000</div>
                </td>
                <td data-field="qcsl">
                    <div class="layui-table-cell laytable-cell-3-qcsl">0</div>
                </td>
                <td data-field="drsj">
                    <div class="layui-table-cell laytable-cell-3-drsj">2019-08-15 17:05:29</div>
                </td>
                <td data-field="rkrq">
                    <div class="layui-table-cell laytable-cell-3-rkrq">201908</div>
                </td>
                <td data-field="bz">
                    <div class="layui-table-cell laytable-cell-3-bz">2019年8月上传文件</div>
                </td>
            </tr>
            <tr data-index="3" class="">
                <td data-field="wjm">
                    <div class="layui-table-cell laytable-cell-3-wjm">Export_10070082(55001_60000).xls</div>
                </td>
                <td data-field="wjdrsl">
                    <div class="layui-table-cell laytable-cell-3-wjdrsl">5000</div>
                </td>
                <td data-field="qcsl">
                    <div class="layui-table-cell laytable-cell-3-qcsl">0</div>
                </td>
                <td data-field="drsj">
                    <div class="layui-table-cell laytable-cell-3-drsj">2019-08-15 17:02:08</div>
                </td>
                <td data-field="rkrq">
                    <div class="layui-table-cell laytable-cell-3-rkrq">201908</div>
                </td>
                <td data-field="bz">
                    <div class="layui-table-cell laytable-cell-3-bz">2019年8月上传文件</div>
                </td>
            </tr>
            <tr data-index="4" class="">
                <td data-field="wjm">
                    <div class="layui-table-cell laytable-cell-3-wjm">Export_10070082(5001_10000).xls</div>
                </td>
                <td data-field="wjdrsl">
                    <div class="layui-table-cell laytable-cell-3-wjdrsl">5000</div>
                </td>
                <td data-field="qcsl">
                    <div class="layui-table-cell laytable-cell-3-qcsl">0</div>
                </td>
                <td data-field="drsj">
                    <div class="layui-table-cell laytable-cell-3-drsj">2019-07-05 18:58:46</div>
                </td>
                <td data-field="rkrq">
                    <div class="layui-table-cell laytable-cell-3-rkrq">201907</div>
                </td>
                <td data-field="bz">
                    <div class="layui-table-cell laytable-cell-3-bz">2019年7月上传文件</div>
                </td>
            </tr>
            <tr data-index="5" class="">
                <td data-field="wjm">
                    <div class="layui-table-cell laytable-cell-3-wjm">Export_10070082(50001_55000).xls</div>
                </td>
                <td data-field="wjdrsl">
                    <div class="layui-table-cell laytable-cell-3-wjdrsl">5000</div>
                </td>
                <td data-field="qcsl">
                    <div class="layui-table-cell laytable-cell-3-qcsl">0</div>
                </td>
                <td data-field="drsj">
                    <div class="layui-table-cell laytable-cell-3-drsj">2019-07-15 16:55:25</div>
                </td>
                <td data-field="rkrq">
                    <div class="layui-table-cell laytable-cell-3-rkrq">201907</div>
                </td>
                <td data-field="bz">
                    <div class="layui-table-cell laytable-cell-3-bz">2019年7月上传文件</div>
                </td>
            </tr>
            <tr data-index="6">
                <td data-field="wjm">
                    <div class="layui-table-cell laytable-cell-3-wjm">Export_10070082(45001_50000).xls</div>
                </td>
                <td data-field="wjdrsl">
                    <div class="layui-table-cell laytable-cell-3-wjdrsl">5000</div>
                </td>
                <td data-field="qcsl">
                    <div class="layui-table-cell laytable-cell-3-qcsl">0</div>
                </td>
                <td data-field="drsj">
                    <div class="layui-table-cell laytable-cell-3-drsj">2019-07-15 16:52:04</div>
                </td>
                <td data-field="rkrq">
                    <div class="layui-table-cell laytable-cell-3-rkrq">201907</div>
                </td>
                <td data-field="bz">
                    <div class="layui-table-cell laytable-cell-3-bz">2019年7月上传文件</div>
                </td>
            </tr>
            <tr data-index="7">
                <td data-field="wjm">
                    <div class="layui-table-cell laytable-cell-3-wjm">Export_10070082(40001_45000).xls</div>
                </td>
                <td data-field="wjdrsl">
                    <div class="layui-table-cell laytable-cell-3-wjdrsl">5000</div>
                </td>
                <td data-field="qcsl">
                    <div class="layui-table-cell laytable-cell-3-qcsl">0</div>
                </td>
                <td data-field="drsj">
                    <div class="layui-table-cell laytable-cell-3-drsj">2019-06-15 16:48:42</div>
                </td>
                <td data-field="rkrq">
                    <div class="layui-table-cell laytable-cell-3-rkrq">201906</div>
                </td>
                <td data-field="bz">
                    <div class="layui-table-cell laytable-cell-3-bz">2019年6月上传文件</div>
                </td>
            </tr>
            <tr data-index="8">
                <td data-field="wjm">
                    <div class="layui-table-cell laytable-cell-3-wjm">Export_10070082(35001_40000).xls</div>
                </td>
                <td data-field="wjdrsl">
                    <div class="layui-table-cell laytable-cell-3-wjdrsl">5000</div>
                </td>
                <td data-field="qcsl">
                    <div class="layui-table-cell laytable-cell-3-qcsl">0</div>
                </td>
                <td data-field="drsj">
                    <div class="layui-table-cell laytable-cell-3-drsj">2019-06-15 16:45:21</div>
                </td>
                <td data-field="rkrq">
                    <div class="layui-table-cell laytable-cell-3-rkrq">201906</div>
                </td>
                <td data-field="bz">
                    <div class="layui-table-cell laytable-cell-3-bz">2019年6月上传文件</div>
                </td>
            </tr>

            </tbody>
        </table>
        <div id="page1"></div>
    </fieldset>
</form>
</body>


<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
    var wait;
    var temp = "";
    var qxlayer;
    var files;
var  type1=document.getElementsByName("type")
    layui.use('upload', function (data) {
        var $ = layui.jquery, upload = layui.upload;
        var listwjm = new Array();
            uploadListIns = upload.render({
                elem: '#select',
                multiple: true,
                url: 'upload.do?lx=rksj&name='
                    + $("#rkrq").val(),
                before: function (obj) {
                    wait = layer.load();
                },
                accept: 'file' //普通文件
                ,
                auto: false,
                exts: 'xls|xlsx|zip|rar',
                bindAction: '#doinput',
                choose: function (obj) {
                    files = obj.pushFile();
                    obj.preview(function (index, file, result) {
                        console.log(file.name);
                        listwjm.push(file.name);
                        $("#moset").append('<a href="javascript:;" class="label"  id="' + index + '"><span>' + file.name + '</span><input type="hidden" name="' + file.name + '"/><i class="close">x</i></a>')

                        //删除
                        $(document).on("click", ".close", function () {
                            $(this).parent().remove();
                            var id = $(this).parent().attr('id');
                            delete files[id]; //删除对应的文件
                            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                        });
                    });
                },
                done: function (res) {
                    layer.close(wait);
                    var data = eval("(" + res.data + ")");
                    var filenames;
                    if (data.length === 1) {

                        filenames = data[0].bdsrc + "$_1$" + res.ybm;
                    } else {

                        $.each(data, function (i) {

                            if (i == 0) {

                                filenames = data[i].bdsrc;
                            } else {

                                filenames += "$_$" + data[i].bdsrc;
                            }
                        });
                    }
                    console.log(filenames);
                    if (res.code == "0") {
                        $.ajax({
                            type: "post", //请求方式
                            async: true, //是否异步
                            success: function (obj) {
                                console.log(obj)
                                layer.msg('导入成功');
                            },
                            error: function (
                                XMLHttpRequest,
                                textStatus,
                                errorThrown) {
                                // 状态码
                                alert(XMLHttpRequest.status);
                                // 状态
                                alert(XMLHttpRequest.readyState);
                                // 错误信息
                                alert(textStatus);
                            }
                        });
                    } else {
                        layer.msg("上传失败！");
                        layer.close(wait);
                    }
                }
            });



    });
    /*layui.use('upload', function (data) {
        var upload = layui.upload;
    upload.render({
        elem: '#jiekou'
        ,url: '/api/upload/' //必填项
        ,method: 'post'  //可选项。HTTP类型，默认post
        , //可选项。额外的参数，如：{id: 123, abc: 'xxx'}
    });
    });*/
    $("#jiekou").click(function(){
        layer.open({
        	area: ['40%', '35%'],
            title:"请输入接口地址",
            content: '<table style="margin: 7% 11%;font-size: 24px;">'+
				        '<tr>'+
				        '<td>接口地址:</td>'+
				        '<td><input type="text" id="put" class="layui-input" style="width: 140%;"></td>'+
				        '</tr>'+
				       '</table>',

        });
        var put = $('#put').val();
        $("#doinput").click(function(){
            layer.msg('导入成功');
        });
        });



    layui.use(['element', 'layer'], function () {
        var element = layui.element;
        var layer = layui.layer;

        //监听折叠
        element.on('collapse(test)', function (data) {
        });
    });





    //初始化Element
    layui.use(['form'], function () {
        var form = layui.form;

        form.on('submit(button)', function (data) {
            pageNo = 1; //当点击搜索的时候，应该回到第一页
            getData();
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });

    });


</script>


</html>