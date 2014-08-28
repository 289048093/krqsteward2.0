<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" />
<c:if test="${ctxPath == '/' }"><c:set var="ctxPath" value="" /></c:if>
var GV = {ctxPath: "${ctxPath}", employeeId: "${employee.id}", username: "${employee.username}"};
