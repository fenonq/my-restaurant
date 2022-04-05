<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="title.change.password"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">
</head>

<body>
<%@include file="header.jspf" %>

<div class="wrapper">
    <form action="${pageContext.request.contextPath}/account/change-password" method="post" class="ls-form">
        <h1 class="ls-form__header change_pass__header"><fmt:message key="change.password.info"/></h1>
        <div class="mandatory_field">
            <input name="old-password" class="ls-form__input" id="old-password" type="password"
                   placeholder="<fmt:message key="change.password.placeholder.password.old"/>">
        </div>
        <div class="mandatory_field">
            <input name="new-password" class="ls-form__input" id="new-password" type="password"
                   placeholder="<fmt:message key="change.password.placeholder.password.new"/>">
        </div>

        <button class="ls-form__button" type="submit"><fmt:message key="change.password.button.submit"/></button>

        <c:if test="${param.err ne null}">
            <p style="font-size: .8rem; color: red; font-style: italic; margin: 0"><fmt:message
                    key="change.password.message.error"/></p>
        </c:if>

    </form>
</div>
</body>
</html>
