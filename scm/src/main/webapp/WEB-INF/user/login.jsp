<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty errorMsg}">
    <h3>错误消息:${errorMsg}</h3>
</c:if>
<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<body>
    <form action="login.action" method="post">
        用户名: <input type="text" name="username" value="${param.username}" /><br/>
        密码: <input type="text" name="password"  /><br/>
        验证码: <input type="text" id="captcha" name="captcha" size="4" maxlength="4" /><br/>
        <img title="点击更换" id="img_captcha" onclick="javascript:refreshCaptcha();" src="captcha.action">
        (看不清<a href="javascript:void(0)" onclick="javascript:refreshCaptcha()">换一张</a>)<br/>

        remember me: <input type="checkbox" name="rememberMe" value="true"/><br/>
        <input type="submit" value="登录" /><!--<input type="button" value="添加用户" onclick="javascript:window.location.href='register.action'" />-->
    </form>
</body>
<script type="text/javascript">
    function refreshCaptcha() {
        document.getElementById("img_captcha").src="captcha.action?t=" + Math.random();
    }
</script>
</html>