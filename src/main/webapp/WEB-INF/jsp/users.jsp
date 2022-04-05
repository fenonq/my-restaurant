<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="title.users"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">
</head>
<body>

<%@include file="header.jspf" %>


<div class="table_wrapper">
    <p class="table_title"><fmt:message key="users.info.allUsers"/></p>
    <table class="table">
        <thead>
        <tr>
            <th>№</th>
            <th><fmt:message key="users.info.table.name"/></th>
            <th><fmt:message key="users.info.table.surname"/></th>
            <th><fmt:message key="users.info.table.login"/></th>
            <th><fmt:message key="users.info.table.role"/></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="userEl" items="${users}">
            <tr>
                <td>${userEl.id}</td>
                <td>${userEl.name}</td>
                <td>${userEl.surname}</td>
                <td>${userEl.login}</td>
                <td>${userEl.roleId}
                    <form method="post" action="${pageContext.request.contextPath}/users">
                        <input name="userId" value="${userEl.id}" style="display: none">
                        <input name="roleId" value="${3 - userEl.roleId}" style="display: none">
                        <button type="submit"><fmt:message key="users.info.table.button.changeRole"/></button>
                    </form>
                </td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/users" class="form_button">
                        <input name="userId" value="${userEl.id}" style="display: none">
                        <input name="roleId" value="-1" style="display: none">
                        <button type="submit"><fmt:message key="users.info.table.button.deleteUser"/></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>