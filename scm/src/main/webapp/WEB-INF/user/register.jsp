<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
</head>
<body>
    <form action="register.action" method="post">
        用户名: <input type="text" name="username"  /><br/>
        密码: <input type="text" name="password"  /><br/>
        <select name="roleId">
        <c:forEach items="${roles}" var="role">
            <option value="${role.id}"><c:out value="${role.name}"/></option>
        </c:forEach>
        </select>
        <input type="submit" value="提交" />
    </form>

</body>
</html>