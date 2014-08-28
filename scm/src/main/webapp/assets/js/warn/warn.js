/**
 * 订单预警
 * Created by HuaLei.Du on 14-1-18.
 */

$(function () {

    $('#warn_main').find('h2').click(function () {
        var self = $(this),
            box = self.next('.box');

        if (self.hasClass('down')) {
            self.removeClass('down');
            self.addClass('up');
            box.hide();
        } else {
            self.removeClass('up');
            self.addClass('down');
            box.show();
        }
    });

    function loadWarn(id, url) {
        var box = $('#' + id),
            msg = $('#' + id + '_msg');
        box.html('<span class="loading"></span>');
        $.ajax({
            type: 'GET',
            url: url,
            cache: false,
            dataType: 'json',
            success: function (response) {
                var data = response.data,
                    html;

                if (data.list.length > 0) {
                    html = template.render(id + '_tpl', data);

                    if (response.msg !== '') {
                        msg.html(response.msg);
                    } else {
                        msg.html('');
                    }

                } else {
                    msg.html('');
                    html = '<p>太棒了，没有预警的订单</p>';
                }

                box.find('.loading').remove();
                box.html(html);
            },
            error: function () {
                msg.html('');
                box.find('.loading').remove();
                box.html('<p class="error">服务器错误</p>');
            }
        });
        box.find('.loading').remove();
    }

    function loadWarnAll() {
        loadWarn('no_send', '/warn/nosend');
        loadWarn('no_logistics', '/warn/nologistics');
        loadWarn('stay_timeout', '/warn/staytimeout');
        loadWarn('no_sign', '/warn/nosign');
    }

    loadWarnAll();

    setInterval(loadWarnAll, 50000);

});
