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

<div class="table_wrapper">
    <p class="table_title"><fmt:message key="account.settings.redactor.category.allCategories"/></p>
    <table class="table">
        <thead>
        <tr>
            <th>№</th>
            <th><fmt:message key="account.settings.redactor.category.table.name"/></th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <tbody>

        <c:forEach var="categoryEl" items="${categories}">
            <tr>
                <td>${categoryEl.id}</td>
                <td>${categoryEl.name}</td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/account/category-redactor"
                          class="form_button">
                        <input name="categoryId" value="${categoryEl.id}" style="display: none">
                        <input name="action" value="-1" style="display: none">
                        <button type="submit"><fmt:message
                                key="account.settings.redactor.category.table.button.delete"/></button>
                    </form>
                </td>

                <td>
                    <form method="post" action="${pageContext.request.contextPath}/account/category-redactor"
                          class="form_button">
                        <input name="categoryId" value="${categoryEl.id}" style="display: none">
                        <input name="action" value="0" style="display: none">
                        <button type="submit"><fmt:message
                                key="account.settings.redactor.edit.button"/></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<div class="strip"></div>

<div class="create_category_wrapper">
    <form method="post" action="${pageContext.request.contextPath}/account/category-redactor">
        <c:forEach var="localeEl" items="${localesArr}">
            <input name="name_${fn:toLowerCase(localeEl)}" class="" required id="name_${fn:toLowerCase(localeEl)}"
                   type="text"
                   placeholder="name_${fn:toLowerCase(localeEl)}">
        </c:forEach>
        <input name="action" value="1" style="display: none">
        <button type="submit"><fmt:message key="account.settings.redactor.category.table.button.create"/></button>
    </form>
</div>
</body>
</html>