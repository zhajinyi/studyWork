<!DOCTYPE html>
<html lang="zh-cn">
<%--
  Created by IntelliJ IDEA.
  User: liupengzheng
  Date: 2019/2/15
  Time: 10:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/include/taglib.jsp"%>
<head>
    <meta charset="utf-8">
    <title>苏州工业园区服务外包学院-学工系统-首页</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="${ctxStatic}/layuiadmin/layui/css/layui.css" media="all">
    <link rel="stylesheet" href="${ctxStatic}/layuiadmin/style/admin.css" media="all">
    <link rel="stylesheet" href="${ctxStatic}/layuiadmin/style/main.css" media="all">
    <style>
        html {
            background-color: #fff;
        }
    </style>
</head>

<body>
<div class="layui-fluid">
    <form class="layui-form" action="${ctx}/sys/Dict/add/childrens" lay-filter="component-form-group">
        <input type="hidden" name="label" autocomplete="off" class="layui-input">
        <div class="layui-form-item">
            <label class="layui-form-label">父字典组ID：</label>
            <div class="layui-input-block">
                <input type="text" name="parentId" lay-verify="required" autocomplete="off" placeholder="请输入"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">本字典ID：</label>
            <div class="layui-input-block">
                <input type="text" name="id" lay-verify="required" autocomplete="off" placeholder="请输入"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">本字典名称：</label>
            <div class="layui-input-block">
                <input type="text" name="value" lay-verify="required" autocomplete="off" placeholder="请输入"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">描述：</label>
            <div class="layui-input-block">
                <input type="text" name="description" lay-verify="required" autocomplete="off" placeholder="请输入"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">排序：</label>
            <div class="layui-input-block">
                <input type="text" name="sort" lay-verify="required" autocomplete="off" placeholder="请输入"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">备注：</label>
            <div class="layui-input-block">
                <textarea id="remarks" name="remarks" placeholder="请输入内容" class="layui-textarea"></textarea>
            </div>
        </div>
        <%--<div class="layui-form-item">
            <label class="layui-form-label">状态：</label>
            <div class="layui-input-block">
                <input type="radio" name="sex" value="启用" title="启用">
                <input type="radio" name="sex" value="禁止" title="禁止" checked>
            </div>
        </div>--%>

        <div class="layui-form-item layui-layout-admin btn-sumbit">
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-normal" type="button" lay-submit="" lay-filter="class_form_sub">保存</button>
            </div>
        </div>
    </form>
</div>
<script src="${ctxStatic}/layuiadmin/layui/layui.js?t=1"></script>
<script>
    layui.config({
        base: '${ctxStatic}/layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index' //主入口模块
    }).use(['index', 'tree', 'table', 'layer', 'form'], function () {
        var $ = layui.$
            , tree = layui.tree
            , fun = layui.fun
            , table = layui.table
            , form = layui.form
            , $body = $('body') //body

        //获取url传值
        var theRequest = fun.GetRequest();
        var id = theRequest.id;
        var event = theRequest.event;
        $('input[name=label]').val(event);
        if (id != undefined&&event=="edit") {
            $.ajax({
                "url":"${ctx}/sys/Dict/querySysDictInfoById",
                'dataType': 'json',
                "data": {"id":id},
                "type":"POST",
                "success":function (re) {
                    var sysDictInfo = re;
                    $('input[name=id]').val(sysDictInfo.id);
                    $('input[name=parentId]').val(sysDictInfo.parentId);
                    $('input[name=value]').val(sysDictInfo.value);
                    $('input[name=description]').val(sysDictInfo.description);
                    $('input[name=sort]').val(sysDictInfo.sort);
                    $("#remarks").val(sysDictInfo.remarks);
                    $('input[name=parentId]').attr("disabled","disabled");
                    $('input[name=id]').attr("disabled","disabled");
                }
            });
        }else {
            $('input[name=parentId]').val(id);
            $('input[name=parentId]').attr("disabled","disabled");
        }

        /* 表单提交 */
        form.on('submit(class_form_sub)', function (data) {
            var sysDictDto=data.field;
            $.ajax({
                "url":data.form.action,
                'dataType': 'json',
                "data": JSON.stringify(sysDictDto),
                "contentType" : 'application/json',
                "type":"POST",
                "success":function (re) {
                    debugger;
                    if(re.falg2==0){
                        if(re.falg){
                            layer.alert('提交成功！', {
                                skin: 'layui-layer-molv' //样式类名  自定义样式
                                ,closeBtn: 1    // 是否显示关闭按钮
                                ,anim: 1 //动画类型
                                ,btn: ['确定'] //按钮
                                ,icon: 6    // icon
                                ,yes:function(){
                                    //window.parent.location.reload();
                                    //当你在iframe页面关闭自身时
                                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                    parent.layer.close(index); //再执行关闭
                                }
                            });
                        }else {
                            layer.alert('提交失败,请联系管理员！', {
                                skin: 'layui-layer-molv' //样式类名  自定义样式
                                ,closeBtn: 1    // 是否显示关闭按钮
                                ,anim: 1 //动画类型
                                ,btn: ['确定'] //按钮
                                ,icon: 6    // icon
                                ,yes:function(){
                                    //当你在iframe页面关闭自身时
                                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                    parent.layer.close(index); //再执行关闭
                                }
                            });
                        }
                    }else{
                        layer.alert('您的业务字典id与之前的重复，请重新输入！');
                        $("input[name=id]").val("");
                    }

                }
            });
            return false;

        });
    })
</script>