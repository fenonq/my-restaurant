<%@ page contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="title.signup"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">
</head>

<body>

<%@include file="header.jspf" %>

<div class="wrapper">
    <form action="${pageContext.request.contextPath}/account/signup" method="post" class="ls-form">
        <h1 class="ls-form__header"><fmt:message key="signup"/></h1>
        <div class="mandatory_field">
            <input name="name" class="ls-form__input" id="name" type="text" maxlength="18"
                   placeholder="<fmt:message key="placeholder.name"/>">
        </div>
        <div class="mandatory_field">
            <input name="surname" class="ls-form__input" id="surname" type="text" maxlength="18"
                   placeholder="<fmt:message key="placeholder.surname"/>">
        </div>
        <div class="mandatory_field">
            <input name="login" class="ls-form__input" id="login" type="text" maxlength="18"
                   placeholder="<fmt:message key="placeholder.login"/>">
        </div>
        <div class="mandatory_field">
            <input name="password" class="ls-form__input" id="password" type="password" maxlength="18"
                   placeholder="<fmt:message key="placeholder.password"/>">
        </div>
        <button class="ls-form__button" type="button"><fmt:message key="signup.button.submit"/></button>
        <p class="under_text"><fmt:message key="signup.message.ask"/> <a
                class="click-reference"
                href="${pageContext.request.contextPath}/account/login"><fmt:message
                key="signup.message.ask.button"/></a></p>

        <c:if test="${param.err ne null}">
            <p style="font-size: .8rem; color: red; font-style: italic; margin: 0"><fmt:message
                    key="signup.message.error"/></p>
        </c:if>

    </form>
</div>

<script src="${pageContext.request.contextPath}/static/js/signup.js"></script>
</body>
</html>
