$(function () {

    var tipsRunning,
        captcha = $('#img_captcha');

    function tips(content) {
        var msg = $('#msg');
        msg.stop().show().html(content);
        clearInterval(tipsRunning);
        tipsRunning = setInterval(function () {
            msg.stop().hide().html('');
        }, 1000);
    }

    function reLoadCaptcha() {
        captcha.attr('src', '/captcha.action?' + Math.random());
    }

    $('.content input').each(function () {
        var self = $(this);
        self.attr('placeholder', self.prev('label').text());
    });

    captcha.on('click', function () {
        reLoadCaptcha();
    });

    $('#login-form').submit(function (e) {
        e.preventDefault();
        var username = $('#username'),
            password = $('#password'),
            captcha = $('#captcha');

        if (username.val() === '') {
            tips('用户名不能为空');
            return;
        }

        if (password.val() === '') {
            tips('密码不能为空');
            return;
        }

        if (captcha.val() === '') {
            tips('验证码不能为空');
            return;
        }

        $.ajax({
            type: 'POST',
            url: '/login.html',
            data: $(this).serialize(),
            dataType: 'json',
            success: function (response) {
                if (response.success) {
                    location.href = 'index.html';
                } else {
                    tips(response.msg);
                    reLoadCaptcha();
                }
            },
            error: function () {
                tips('服务器发生错误');
            }
        });
    });
});