<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="account.settings.redactor.category"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">
</head>

<body>
<%@include file="header.jspf" %>

<div class="update_wrapper">
    <p class="table_title"><fmt:message key="account.settings.redactor.edit.title"/></p>

    <div class="strip strip_update"></div>

    <div class="update_category_wrapper">

        <form method="post" action="${pageContext.request.contextPath}/account/category-redactor/update-category">

            <c:forEach var="localeEl" items="${localesArr}">


                <c:set var="property"
                       value="name${fn:toUpperCase(fn:substring(localeEl, 0, 1))}${fn:toLowerCase(fn:substring(localeEl, 1, fn:length(localeEl.name())))}"/>

                <input name="name_${fn:toLowerCase(localeEl)}" value="${sessionScope.category[property]}"
                       id="name_${fn:toLowerCase(localeEl)}" type="text"
                       placeholder="name_${fn:toLowerCase(localeEl)}">
            </c:forEach>
            <button type="submit"><fmt:message
                    key="account.settings.redactor.edit.button"/></button>
        </form>
    </div>
</div>

</body>
</html>
