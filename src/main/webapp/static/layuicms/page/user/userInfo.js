var user;
$(function() {
	$.ajax({
		url : "/fast/ajax.do?ctrl=rygl_GetUser",
		type : 'post',
		data : {},
		dataType : "json",
		success : function(obj) {
			user = obj.data;
			$("#username").val(obj.data.username);
			$("#rygh").val(obj.data.rygh);
			$("#sfzh").val(obj.data.sfzh);
			$("#sex").val(obj.data.sex);
			$("#phone").val(obj.data.phone);
			$("#name").val(obj.data.name);
			layui.form.render();
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
});

var form, $,areaData;
layui.config({
    base : "../../js/"
}).extend({
    "address" : "address"
})
layui.use(['form','layer','upload','laydate',"address"],function(){
    form = layui.form;
    $ = layui.jquery;
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        upload = layui.upload,
        laydate = layui.laydate,
        address = layui.address;


    //获取省信息
    address.provinces();

    //提交个人资料
    form.on("submit(changeUser)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
    	$.ajax({
    		url : "/fast/ajax.do?ctrl=rygl_UpdateUserInfo",
    		type : 'post',
    		data : $("#userinfoform").serialize(),
    		dataType : "json",
    		success : function(obj) {
                layer.close(index);
                layer.msg("提交成功！");
                window.parent.location.reload();
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
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })

    //修改密码
    form.on("submit(changePwd)",function(data){
        var index = layer.msg('提交中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            layer.msg("密码修改成功！");
            $(".pwd").val('');
        },2000);
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    })
})