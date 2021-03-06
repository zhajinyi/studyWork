<%--
  Created by IntelliJ IDEA.
  User: ZhaJinyi
  Date: 2019-2-20
  Time: 15:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/include/taglib.jsp"%>
<html>
<head>
    <title>宿舍管理</title>
    <%@ include file="/include/head_include.jsp"%>
</head>
<style>
    .layui-form-label{
        text-align: left;
    }
    .layui-form-item{
        text-align: center;
    }
</style>
<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body" style="padding: 15px;">

            <form class="layui-form" lay-filter="component-form-group" id="iframeForm">

                <div class="layui-form-item">


                    <%--宿舍基本信息--%>
                    <fieldset class="layui-elem-field layui-field-title">
                        <legend>宿舍信息</legend>
                    </fieldset>
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">宿舍编号</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="id" placeholder="请输入" autocomplete="off" class="layui-input" lay-verify="id"readonly>
                                </div>
                            </div>

                            <div class="layui-inline">
                                <label class="layui-form-label">所在地区</label>
                                <div class="layui-input-inline">
                                    <select name="area" lay-filter="professional"lay-verify="required">
                                        <option value="">全部</option>
                                        <option value="校本部">校本部</option>
                                        <option value="其他">其他</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">所在楼号</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="buildingNum" placeholder="请输入" autocomplete="off" class="layui-input"lay-verify="required">
                                </div>
                            </div>

                            <div class="layui-inline">
                                <label class="layui-form-label">所在楼层</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="floor" placeholder="请输入" autocomplete="off" class="layui-input"lay-verify="required">
                                </div>
                            </div>
                        </div>


                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">使用情况</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="state"  placeholder="请输入" autocomplete="off" class="layui-input">
                                </div>
                            </div>

                            <div class="layui-inline">
                                <label class="layui-form-label">类型（男/女）</label>
                                <div class="layui-input-inline">
                                    <input type="radio" name="type" value="男" title="男">
                                    <input type="radio" name="type" value="女" title="女">
                                </div>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">当前入住人数</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="currentNum" placeholder="请输入" autocomplete="off" class="layui-input">
                                </div>
                            </div>

                            <div class="layui-inline">
                                <label class="layui-form-label">床位数</label>
                                <div class="layui-input-inline">
                                    <input type="text" name="bunkNum"  placeholder="请输入" autocomplete="off" class="layui-input">
                                </div>
                            </div>
                        </div>

                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">寝室长</label>
                                <div class="layui-input-inline" id="leader_div">
                                    <select id="leader" name="leader">
                                    </select>
                                </div>
                            </div>

                            <div class="layui-inline">
                                <label class="layui-form-label">朝向（南/北）</label>
                                <div class="layui-input-inline">
                                    <input type="radio" name="orientation" value="南" title="南">
                                    <input type="radio" name="orientation" value="北" title="北">
                                </div>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <div class="layui-inline">
                                <label class="layui-form-label">是否参与分配</label>
                                <div class="layui-input-inline">
                                    <input type="radio" name="isAllot" value="Y" title="是">
                                    <input type="radio" name="isAllot" value="N" title="否">
                                </div>
                            </div>
                            <div class="layui-inline">
                                <label class="layui-form-label">备注</label>
                                <div class="layui-input-inline">
                                    <textarea name="remark" placeholder="请输入" class="layui-textarea"></textarea>
                                </div>
                            </div>
                        </div>

                    <div class="layui-form-item layui-layout-admin">
                        <div class="layui-input-block">
                            <div class="layui-footer" style="left: 0;">
                                <button class="layui-btn" lay-submit lay-filter="formSubmit" id="formSubmit">保存</button>
                                <button class="layui-btn layui-btn-primary" type="button" id="close">关闭</button>
                            </div>
                        </div>
                    </div>


                </div>

            </form>
        </div>
    </div>
</div>
<script src="${ctxStatic}/layuiadmin/layui/layui.js"></script>
<script>
    layui.config({
        base: '${ctxStatic}/layuiadmin/' //静态资源所在路径
    }).extend({
        index: 'lib/index',//主入口模块
        formSelects: 'formSelects-v4'
    }).use(['index', 'form', 'layer', 'formSelects'], function () {
        var $ = layui.$,
            $body = $('body'),
            fun = layui.fun,
            layer = layui.layer,
            form = layui.form,
            iframeForm = "iframeForm",
            submitBtn = "formSubmit",
            formLayFilter = "component-form-group",
            tableUrl = "${ctx}/sisoDormInfo/selective",
            insertUrl = "${ctx}/sisoDormInfo/insert/selective",
            updateUrl = "${ctx}/sisoDormInfo/update/selective",
            stuSelectiveUrl = "${ctx}/empInfo/selectIdsByName",
            theRequest = fun.GetRequest(),
            type = theRequest.type,
            id = type == 'update' ? theRequest.id : null,
            defaultData = {id:id};
            form.render(null,"component-form-group");
            switch(type){
                case 'add': addDefaultData();
                    break;
                case 'update': updateDefaultData();
                    break;
                case 'multiSearch':multiSearchDefaultData();
                    break;
            };
            // 启动select2
            $("#leader").select2({
                ajax: {
                    url: stuSelectiveUrl,
                    dataType: 'json',
                    data: function (params) {
                        return {
                            q: params.term,
                            page: params.page
                        };
                    },
                    processResults: function (data, params) {
                        var results = data.extend.sysEmpInfoDtos;
                        var objs = objFormat(results);
                        params.page = params.page || 1;
                        return {
                            results: objs,
                            pagination: {
                                more: (params.page * 30) < data.total_count
                            }
                        };
                    },
                    cache: true
                },
                language: "zh-CN",
                placeholder: '请输入',
                allowClear: true,//允许清空
                escapeMarkup: function (markup) { return markup; }, // 自定义格式化防止xss注入
                minimumInputLength: 1,
                formatResult: function formatRepo(repo){return repo.text;}, // 函数用来渲染结果
                formatSelection: function formatRepoSelection(repo){return repo.text;} // 函数用于呈现当前的选择
            });


        //样式美化
        $("#leader_div div:last-child").hide();
        $("#leader_div span:eq(0)").css('width', '100%');

        function objFormat(results){
            var objFormat = new Array();
            for (var i = 0; i < results.length; i++) {
                var object = {id:null,text:null}
                object.id = results[i].id;
                object.text = results[i].realname;
                objFormat.push(object);
            }
            return objFormat;
        }



            function updateDefaultData(){
                $.ajax({
                    url: tableUrl,
                    data:JSON.stringify(defaultData),
                    type: 'post',
                    contentType: 'application/json; charset=utf-8',
                    success: function(result){
                        if (result.code === 200) {
                            form.render(null, formLayFilter);
                            iframeForm = "#" + iframeForm;
                            var formElement = $(iframeForm);
                            var obj = result.extend.PageInfo.list[0];
                            setEntity(formElement, obj);
                            form.render(null, formLayFilter);
                        }
                    }
                })
            }

        function addDefaultData() {
            $("#iframeForm input[name=id]").attr("readonly",false);
            form.verify({id: function(value, item){if(!validateForm(tableUrl,{id:value})){return '宿舍编号已存在!';}} });//value：表单的值、item：表单的DOM对象
        }

        function multiSearchDefaultData(){
            $("#iframeForm input[name=id]").attr("readonly",false);
            $("#iframeForm select[name=area]").removeAttr("lay-verify");
            $("#iframeForm input[name=buildingNum]").removeAttr("lay-verify");
            $("#iframeForm input[name=floor]").removeAttr("lay-verify");
            $("#formSubmit").html("查询");
        }



        function validateForm(requestUrl,data){
            var flag = false;
            $.ajax({
                url: requestUrl,
                data: JSON.stringify(data),
                async: false,
                type: "post",
                contentType: "application/json;charset=utf-8",
                success: function(res){
                   flag = res.extend.PageInfo.list.length == 0 ? true : false;
                }
            })
            return flag;
        }


        $("#close").on("click",function(){parent.layer.closeAll();});




        /* 监听提交 */
        form.on('submit('+submitBtn+')', function (data) {
            var index = parent.layer.getFrameIndex(window.name);
            if(type == 'multiSearch'){
                top.parent.fromIframeData = data.field;
                parent.layer.close(index);
            }else {
                var flagMsg = type == "add" ?  "提交错误" : "修改错误";
                var flag = saveData(type,data.field);
                if(flag){
                    parent.layer.close(index);
                }else {
                    parent.layer.msg("出错", {
                        time: 5000, //20s后自动关闭
                        content:flagMsg
                    })
                }
            }
        });

        function saveData(type,data){
            var requestUrl = type == 'add' ?  insertUrl : updateUrl;
            var result;
            $.ajax({
                url: requestUrl,
                data: JSON.stringify(data),
                async: false,
                type: 'post',
                contentType: 'application/json;charset=utf-8',
                success: function(res){
                    result = res.code == 200 ? true : false;
                }
            });
            return result;
        }
    });
</script>
</body>
</html>
