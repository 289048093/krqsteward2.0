/**
 * User: Hualei Du
 * Date: 13-9-26
 * Time: 下午2:53
 */

// 需要注意 打印控件在“chrome正式版32”和“chrome开发版33”设计物流单显示不出来，需要缩放窗口才能正常显示，暂时解决办法就是尽量避开使用这两个版本

var LODOP;
$(function () {
    "use strict";

    // 初始化打印控件
    (function () {
        var reg = /(?:\?id=)(\w+)/,
            logisticsInfoId = reg.exec(window.location.search)[1];

        document.getElementById('logisticsInfoId').value = logisticsInfoId;


        $.ajax({
            type: 'post',
            url: '/logisticsprint/detail',
            dataType: 'json',
            data: 'id=' + logisticsInfoId,
            success: function (response) {

                var lodopData = response.data.obj.printHtml;

                //物流设计尺寸宽高 回填
                console.log(response.data.obj.pageHeight);
                console.log(response.data.obj.pageWidth);
                $('#pageHeight').val(response.data.obj.pageHeight);
                $('#pageWidth').val(response.data.obj.pageWidth);

                //console.log(lodopData);
                //console.log(response);
                LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));

                //添加控件数据
                eval(lodopData);

                LODOP.SET_SHOW_MODE('DESIGN_IN_BROWSE', 1);
                LODOP.SET_SHOW_MODE('BKIMG_IN_PREVIEW', 1);
                LODOP.PRINT_DESIGN();
            }
        });
    }());

    // 获取设计控件
    function getDesignWidget() {
        return getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
    }

    // 保存设计好的物流面单
    function saveDesign() {
        LODOP = getDesignWidget();
        $('#printHtml').val(LODOP.GET_VALUE('ProgramCodes', 0));

        var form = $('#design_form'),
            url = form.attr('action'),
                formData = form.serialize();

        $.ajax({
            type: 'POST',
            url: url,
            data: formData,
            dataType: 'json',
            success: function (data) {

                if (data.success) {
                    alert('保存成功');
                    return;
                }

                alert(data.msg);
            },
            error: function (response) {
                var data,
                    msg;

                if (!!response.responseText) {
                    data = eval('(' + response.responseText + ')');
                }

                if (response.status == 999) {
                    alert('会话超时，请重新登录!');
                    window.location.href = '/login.html';
                    return;
                } else if (response.status == 998) {
                    msg = data.msg || '您没有权限!';
                    alert(msg);
                    return;
                } else if (response.status == 997) {
                    msg = data.msg || '服务器错误!';
                    alert(msg);
                    return;
                }

                alert('服务器发生错误');
            }
        });
    }

    // 修改物流单设计控件
    function modifyDesign(item) {
        LODOP = getDesignWidget();
        var inputName = item.attr('name'),
            inputVal = item.attr('val'),
            inputValue = item.val(),
            inputChecked = item.get(0).checked;

        if ((!LODOP.GET_VALUE("ItemIsAdded", inputName)) && (inputChecked == true)) {
            LODOP.ADD_PRINT_TEXTA(inputName, inputValue, 32, 175, 22, inputVal);
        } else {
            LODOP.SET_PRINT_STYLEA(inputName, 'Deleted', inputChecked != true);
        }
    }

    // 展现物流单设计控件
    function showDesignWidget() {
        var designConfig = $('#printHtml').val();
        eval(designConfig);
        LODOP.SET_SHOW_MODE("DESIGN_IN_BROWSE", 1);
        LODOP.SET_SHOW_MODE("BKIMG_IN_PREVIEW", 1); //注："BKIMG_IN_PREVIEW"-预览包含背景图 "BKIMG_IN_FIRSTPAGE"- 仅首页包含背景图
        LODOP.PRINT_DESIGN();
    }

    // 注册事件
    (function () {

        // 设计物流面单操作
        $('#design_box').on('click', 'input', function () {
            modifyDesign($(this));
        });

        // 保存设计好的物流面单代码
        $('#save_btn').on('click', function () {
            saveDesign();
        });

        // 删除选定的设计控件
        $('#delete_btn').on('click', function () {
            LODOP.SET_PRINT_STYLEA('Selected', 'Deleted', true);
        });

    }());

});
