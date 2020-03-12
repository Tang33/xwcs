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

    <title>数据自定义</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <meta name="msapplication-TileColor" content="#3399cc"/>
    <!-- 开始......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
    <jsp:include page="../load.jsp"></jsp:include>
    <!-- 结束......初始化jquery、layui、easyui、easy-ajax 等js、css .......-->
    <script src="./css/layui/layui.js" charset="utf-8"></script>
    <!-- <link rel="stylesheet" href="./static/layui/css/layui.css" media="all">
    <script src="./static/layui/layui.js" charset="utf-8"></script>
    <script src="./static/assets/js/jquery-1.8.3.min.js" charset="utf-8"></script>
    <script src="./static/js/easy.ajax.js" charset="utf-8"></script> -->
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
    本功能页面用于通过选择定制企业模板与定制查询模板查询出对应数据！
</blockquote>
<form class="layui-form" id="form" action="">
    <div class="layui-collapse" lay-filter="test">
        <div class="layui-colla-item">
            <div class="layui-colla-content  layui-show">
                <div class="layui-row">

                    <div class="layui-form-item"></div>
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 100px">定制模板:</label>
                        <div class="layui-inline" style="width: 300px">
                            <select name="cxmbmc" lay-filter="cxmbmc" id="cxmbmc" lay-search="">

                                <option value="请选择">请选择</option>
                                <option value="1">定制模板1</option>
                                <option value="2">定制模板2</option>
                                <option value="3">定制模板3</option>
                                <option value="4">定制模板4</option>
                                <option value="5">定制模板5</option>
                                <option value="6">定制模板6</option>
                                <option value="7">定制模板7</option>
                            </select>
                            <div class="layui-form-select">
                                <div class="layui-select-title"><input type="text" placeholder="请选择" value="请选择"
                                                                       class="layui-input"><i class="layui-edge"></i>
                                </div>
                                <dl class="layui-anim layui-anim-upbit" style="">
                                    <dd lay-value="请选择" class="">请选择</dd>
                                    <dd lay-value="1" class="layui-this">定制模板1</dd>
                                    <dd lay-value="2" class="">定制模板2</dd>
                                    <dd lay-value="3" class="">定制模板3</dd>
                                    <dd lay-value="4" class="">定制模板4</dd>
                                    <dd lay-value="5" class="">定制模板5</dd>
                                    <dd lay-value="6" class="">定制模板6</dd>
                                    <dd lay-value="7" class="">定制模板7</dd>

                                </dl>
                            </div>
                        </div>
                        <div class="layui-inline"></div>

                        <div class="layui-inline" style="margin-left: 70px">
                            <button class="layui-btn layui-btn-normal" type="button"
                                    id="XZ" lay-submit="" lay-filter="XZ">新增
                            </button>
                            <button class="layui-btn layui-btn-normal" data-type="reload"
                                    id="selectbyCondition" onclick="return false;">查询
                            </button>
                            <button class="layui-btn layui-btn-normal" id="exportExcel"
                                    type="button" lay-submit="" lay-filter="exportExcel">导出
                            </button>
                        </div>

                    </div>


                </div>
            </div>
        </div>
    </div>

    <fieldset class="layui-elem-field layui-field-title"
              style="margin-top: 20px;">
        <legend>查询显示</legend>

        <table class="layui-table" id="syzs" lay-filter="syzs">

        </table>
        <div class="layui-form layui-border-box layui-table-view" lay-filter="LAY-table-1"
             style="height: 530px; width: 1613px;">
            <div class="layui-hide" id="table">
                <div class="layui-table-header">
                    <table cellspacing="0" cellpadding="0" border="0" class="layui-table">
                        <thead>
                        <tr>
                            <th data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0"><span>纳税人名称</span></div>
                            </th>
                            <th data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"><span>纳税人识别号</span></div>
                            </th>
                            <th data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2"><span>地址</span></div>
                            </th>
                            <th data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3"><span>联系方式</span></div>
                            </th>
                            <th data-field="2019年车船税地方口径" data-key="1-0-4" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4" align="right">
                                    <span>2019年车船税地方口径</span></div>
                            </th>
                            <th data-field="2019年印花税地方口径" data-key="1-0-5" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5" align="right">
                                    <span>2019年印花税地方口径</span></div>
                            </th>
                            <th data-field="2019年房产税地方口径" data-key="1-0-6" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6" align="right">
                                    <span>2019年房产税地方口径</span></div>
                            </th>
                            <th data-field="2019年车船税全口径" data-key="1-0-7" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7" align="right"><span>2019年车船税全口径</span>
                                </div>
                            </th>
                            <th data-field="2019年印花税全口径" data-key="1-0-8" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8" align="right"><span>2019年印花税全口径</span>
                                </div>
                            </th>
                            <th data-field="2019年房产税全口径" data-key="1-0-9" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9" align="right"><span>2019年房产税全口径</span>
                                </div>
                            </th>


                        </tr>
                        </thead>
                    </table>
                </div>
                <div class="layui-table-body layui-table-main" style="height: 448px;">
                    <table cellspacing="0" cellpadding="0" border="0" class="layui-table">
                        <tbody>
                        <tr data-index="0" class="">
                            <td data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0">南京长日汽配供应中心(普通合伙)</div>
                            </td>
                            <td data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"></div>
                            </td>
                            <td data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2">孝陵卫</div>
                            </td>
                            <td data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3">1324434.2</div>
                            </td>
                            <td data-field="2019年车船税地方口径" data-key="1-0-4" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4">267914</div>
                            </td>
                            <td data-field="2019年印花税地方口径" data-key="1-0-5" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5">23259.6</div>
                            </td>
                            <td data-field="2019年房产税地方口径" data-key="1-0-6" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6">41367</div>
                            </td>
                            <td data-field="2019年车船税全口径" data-key="1-0-7" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7">0</div>
                            </td>
                            <td data-field="2019年印花税全口径" data-key="1-0-8" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8">239.6</div>
                            </td>
                            <td data-field="2019年房产税全口径" data-key="1-0-9" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9">0</div>
                            </td>
                        </tr>
                        <tr data-index="0" class="">
                            <td data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0">南京长日汽配供应中心</div>
                            </td>
                            <td data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"></div>
                            </td>
                            <td data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2">孝陵卫</div>
                            </td>
                            <td data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3">562732.7</div>
                            </td>
                            <td data-field="2019年车船税地方口径" data-key="1-0-4" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4">342546.9</div>
                            </td>
                            <td data-field="2019年印花税地方口径" data-key="1-0-5" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5">239453.6</div>
                            </td>
                            <td data-field="2019年房产税地方口径" data-key="1-0-6" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6">4353653.4</div>
                            </td>
                            <td data-field="2019年车船税全口径" data-key="1-0-7" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7">0</div>
                            </td>
                            <td data-field="2019年印花税全口径" data-key="1-0-8" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8">239.6</div>
                            </td>
                            <td data-field="2019年房产税全口径" data-key="1-0-9" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9">0</div>
                            </td>
                        </tr>
                        <tr data-index="0" class="">
                            <td data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0">南京贝贝帮教育咨询有限公司</div>
                            </td>
                            <td data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"></div>
                            </td>
                            <td data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2">梅园</div>
                            </td>
                            <td data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3">340995.9</div>
                            </td>
                            <td data-field="2019年车船税地方口径" data-key="1-0-4" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4">67287.3</div>
                            </td>
                            <td data-field="2019年印花税地方口径" data-key="1-0-5" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5">239232.6</div>
                            </td>
                            <td data-field="2019年房产税地方口径" data-key="1-0-6" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6">51354</div>
                            </td>
                            <td data-field="2019年车船税全口径" data-key="1-0-7" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7">0</div>
                            </td>
                            <td data-field="2019年印花税全口径" data-key="1-0-8" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8">239.6</div>
                            </td>
                            <td data-field="2019年房产税全口径" data-key="1-0-9" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9">0</div>
                            </td>
                        </tr>
                        <tr data-index="0" class="">
                            <td data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0">南京中人网络科技有限公司</div>
                            </td>
                            <td data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"></div>
                            </td>
                            <td data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2">新街口</div>
                            </td>
                            <td data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3">189845</div>
                            </td>
                            <td data-field="2019年车船税地方口径" data-key="1-0-4" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4">3264547</div>
                            </td>
                            <td data-field="2019年印花税地方口径" data-key="1-0-5" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5">682789.6</div>
                            </td>
                            <td data-field="2019年房产税地方口径" data-key="1-0-6" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6">324245.9</div>
                            </td>
                            <td data-field="2019年车船税全口径" data-key="1-0-7" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7">423325</div>
                            </td>
                            <td data-field="2019年印花税全口径" data-key="1-0-8" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8">239.6</div>
                            </td>
                            <td data-field="2019年房产税全口径" data-key="1-0-9" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9">0</div>
                            </td>
                        </tr>
                        <tr data-index="0" class="">
                            <td data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0">南京长日汽配供应中心(普通合伙)</div>
                            </td>
                            <td data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"></div>
                            </td>
                            <td data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2">新街口</div>
                            </td>
                            <td data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3">53297.6</div>
                            </td>
                            <td data-field="2019年车船税地方口径" data-key="1-0-4" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4">2425.46</div>
                            </td>
                            <td data-field="2019年印花税地方口径" data-key="1-0-5" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5">2541.6</div>
                            </td>
                            <td data-field="2019年房产税地方口径" data-key="1-0-6" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6">89217</div>
                            </td>
                            <td data-field="2019年车船税全口径" data-key="1-0-7" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7">53226</div>
                            </td>
                            <td data-field="2019年印花税全口径" data-key="1-0-8" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8">239.6</div>
                            </td>
                            <td data-field="2019年房产税全口径" data-key="1-0-9" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9">0</div>
                            </td>
                        </tr>
                        <tr data-index="0" class="">
                            <td data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0">南京康乾传媒有限公司</div>
                            </td>
                            <td data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"></div>
                            </td>
                            <td data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2">孝陵卫</div>
                            </td>
                            <td data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3">3425907.1</div>
                            </td>
                            <td data-field="2019年车船税地方口径" data-key="1-0-4" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4">1324368.4</div>
                            </td>
                            <td data-field="2019年印花税地方口径" data-key="1-0-5" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5">239343.6</div>
                            </td>
                            <td data-field="2019年房产税地方口径" data-key="1-0-6" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6">36271</div>
                            </td>
                            <td data-field="2019年车船税全口径" data-key="1-0-7" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7">14556</div>
                            </td>
                            <td data-field="2019年印花税全口径" data-key="1-0-8" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8">239.6</div>
                            </td>
                            <td data-field="2019年房产税全口径" data-key="1-0-9" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9">0</div>
                            </td>
                        </tr>
                        <tr data-index="0" class="">
                            <td data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0">南京志诚科技有限公司</div>
                            </td>
                            <td data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"></div>
                            </td>
                            <td data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2">徐庄</div>
                            </td>
                            <td data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3">7248219</div>
                            </td>
                            <td data-field="2019年车船税地方口径" data-key="1-0-4" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4">6435914.4</div>
                            </td>
                            <td data-field="2019年印花税地方口径" data-key="1-0-5" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5">46218.6</div>
                            </td>
                            <td data-field="2019年房产税地方口径" data-key="1-0-6" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6">267891</div>
                            </td>
                            <td data-field="2019年车船税全口径" data-key="1-0-7" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7">54267</div>
                            </td>
                            <td data-field="2019年印花税全口径" data-key="1-0-8" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8">239.6</div>
                            </td>
                            <td data-field="2019年房产税全口径" data-key="1-0-9" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9">0</div>
                            </td>
                        </tr>
                        <tr data-index="0" class="">
                            <td data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0">江苏佳贝购国际贸易有限公司</div>
                            </td>
                            <td data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"></div>
                            </td>
                            <td data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2">后宰门</div>
                            </td>
                            <td data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3">1545289.6</div>
                            </td>
                            <td data-field="2019年车船税地方口径" data-key="1-0-4" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4">9238.2</div>
                            </td>
                            <td data-field="2019年印花税地方口径" data-key="1-0-5" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5">67124.6</div>
                            </td>
                            <td data-field="2019年房产税地方口径" data-key="1-0-6" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6">10932</div>
                            </td>
                            <td data-field="2019年车船税全口径" data-key="1-0-7" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7">0</div>
                            </td>
                            <td data-field="2019年印花税全口径" data-key="1-0-8" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8">239.6</div>
                            </td>
                            <td data-field="2019年房产税全口径" data-key="1-0-9" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9">0</div>
                            </td>
                        </tr>
                        <tr data-index="0" class="">
                            <td data-field="纳税人名称" data-key="1-0-0" class="">
                                <div class="layui-table-cell laytable-cell-1-0-0">南京文锦网络科技有限公司</div>
                            </td>
                            <td data-field="纳税人识别号" data-key="1-0-1" class="">
                                <div class="layui-table-cell laytable-cell-1-0-1"></div>
                            </td>
                            <td data-field="地址" data-key="1-0-2" class="">
                                <div class="layui-table-cell laytable-cell-1-0-2">红山</div>
                            </td>
                            <td data-field="联系方式" data-key="1-0-3" class="">
                                <div class="layui-table-cell laytable-cell-1-0-3">415388.5</div>
                            </td>
                            <td data-field="2019年车船税地方口径" data-key="1-0-4" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-4">75375.1</div>
                            </td>
                            <td data-field="2019年印花税地方口径" data-key="1-0-5" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-5">14194.6</div>
                            </td>
                            <td data-field="2019年房产税地方口径" data-key="1-0-6" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-6">0</div>
                            </td>
                            <td data-field="2019年车船税全口径" data-key="1-0-7" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-7">0</div>
                            </td>
                            <td data-field="2019年印花税全口径" data-key="1-0-8" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-8">239.6</div>
                            </td>
                            <td data-field="2019年房产税全口径" data-key="1-0-9" align="right" class="">
                                <div class="layui-table-cell laytable-cell-1-0-9">0</div>
                            </td>
                        </tr>


                        </tbody>
                    </table>
                </div>
            </div>

            <style>.laytable-cell-1-0-0 {
                width: 330px;
            }

            .laytable-cell-1-0-1 {
                width: 163px;
            }

            .laytable-cell-1-0-2 {
                width: 163px;
            }

            .laytable-cell-1-0-3 {
                width: 163px;
            }

            .laytable-cell-1-0-4 {
                width: 330px;
            }

            .laytable-cell-1-0-5 {
                width: 330px;
            }

            .laytable-cell-1-0-6 {
                width: 330px;
            }

            .laytable-cell-1-0-7 {
                width: 330px;
            }

            .laytable-cell-1-0-8 {
                width: 330px;
            }

            .laytable-cell-1-0-9 {
                width: 330px;
            }</style>
        </div>
    </fieldset>
</form>
</body>

<!-- 注意：如果你直接复制所有代码到本地，上述js路径需要改成你本地的 -->
<script>
    var wait;
    var temp = "";
    var qxlayer;
    var files;

    queryDzqymb();
    queryDzcxmb();


    function queryDzqymb() {
        layui.use('form', function () {
            var form = layui.form;
            $.ajax({
                /*  url: "ajax.do?ctrl=Sjzdy_queryDzqymb",*/
                type: "post", //请求方式
                async: true, //是否异步
                data: {},
                dataType: "json",
                success: function (obj) {
                    console.log(obj)
                    if (obj.code == '000') {
                        row = '<option value="请选择">请选择</option>';
                        for (var i in obj.data) {
                            row += '<option value=' + obj.data[i].U_ID + '>' + obj.data[i].MBMC + '</option>';
                        }
                        $("#qymbmc").append(row);
                        form.render('select');
                    }
                }
            });
        })
    }

    function queryDzcxmb() {
        layui.use('form', function () {
            var form = layui.form;
            $.ajax({
                /* url: "ajax.do?ctrl=Sjzdy_queryDzcxmb",*/
                type: "post", //请求方式
                async: true, //是否异步
                data: {},
                dataType: "json",
                success: function (obj) {
                    console.log(obj)
                    if (obj.code == '000') {
                        row = '<option value="请选择">请选择</option>';
                        for (var i in obj.data) {
                            row += '<option value=' + obj.data[i].U_ID + '>' + obj.data[i].MBMC + '</option>';
                        }
                        $("#cxmbmc").append(row);
                        form.render('select');
                    }
                }, error: function (XMLHttpRequest, textStatus, errorThrown) {

                }

            });

        })
    }


    layui.use(['form', 'laydate', 'laypage'], function () {
        var form = layui.form, layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate,
            laypage = layui.laypage;
        //年月范围
        laydate.render({
            elem: '#rkrq',
            type: 'month',
            range: true
        });
        var date = new Date;
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        month = (month < 10 ? "0" + month : month);
        var mydate = (year.toString() + "-" + month.toString()) + " - " + (year.toString() + "-" + month.toString());

        $("#rkrq").val(mydate);

        //导出
        form.on('submit(exportExcel)', function (data) {
            $.ajax({
                type: "post", //请求方式
                async: true, //是否异步
                data: $("#form").serialize(),
                dataType: "json",
                success: function (obj) {
                    layer.msg('导出成功！');
                    layer.close(qxlayer);
                    $("#button").click();
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    layer.msg('导出成功！');
                }
            });
        });

        form.on('submit(XZ)', function (data) {
            //添加一个新的页面、
            layer.open({
                type: 2,
                shadeClose: true,
                shade: 0.8,
                maxmin: true,
                area: ['70%', '70%'],
                content: '${pageContext.request.contextPath}/jsp/sjfx/dzcxmb.jsp',
                success: function (layero, index) {
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    var inputList = body.find("input");
                    for (var i = 0; i < inputList.length; i++) {
                        $(inputList[i]).val(editList[i]);
                    }
                }
            });
        });
    });


    function getTitle() {
        $.ajax({
            type: "post", //请求方式
            async: false, //是否异步
            data: {

                dzcxmb_id: document.getElementById("cxmbmc").value
            },
            dataType: "json",
            success: function (obj) {
                console.log(obj)
                layer.alert("导出成功", {icon: 1}, function () {
                    layer.close(layer.index);
                    window.location.reload();
                });

            },
        });
    }

    layui.use(['table'], function () {

        var table = layui.table
        //查询
        $('#selectbyCondition').on('click',
            function () {
                $('#table').removeClass('layui-hide');


            });
    });

</script>


</html>