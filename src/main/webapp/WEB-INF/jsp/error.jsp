<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>


<html>
<head>
    <title><fmt:message key="error.message.error"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">

</head>

<body>
<%@include file="header.jspf" %>

<div class="err_container">
    <p class="err_type"><fmt:message key="error.message.error"/></p>
    <p class="err_type"><fmt:message key="error.message.tryAgain"/></p>
    <p class="err_type"> ${sessionScope.errorMsg}</p>
</div>

</body>
</html>