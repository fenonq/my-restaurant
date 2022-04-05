<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="pricetag" uri="/WEB-INF/tld/price.tld" %>


<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="title.account"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">
</head>
<body>

<%@include file="header.jspf" %>
<div class="hello_wrapper">
    <fmt:message key="account.hello"/>, ${user.name}
</div>
<div class="settings_links">
    <a href="${pageContext.request.contextPath}/account?logout=" class="a_changepass"><fmt:message
            key="account.settings.logout"/></a>
    <a href="${pageContext.request.contextPath}/account/change-password" class="a_changepass"><fmt:message
            key="account.settings.change.password"/></a>
    <c:if test="${user.roleId == 1}">
        <a href="${pageContext.request.contextPath}/account/dish-redactor" class="a_changepass"><fmt:message
                key="account.settings.redactor.dish"/></a>
        <a href="${pageContext.request.contextPath}/account/category-redactor" class="a_changepass"><fmt:message
                key="account.settings.redactor.category"/></a>
    </c:if>
</div>

<c:if test="${user.roleId != 1}">
<div class="table_wrapper">
    <p class="table_title"><fmt:message key="account.info.yourReceipts"/></p>
    <table class="table">
        <thead>
        <tr>
            <th>№</th>
            <th><fmt:message key="account.info.table.status"/></th>
            <th><fmt:message key="account.info.table.orderDate"/></th>
            <th><fmt:message key="account.info.table.dishes"/></th>
            <th><fmt:message key="account.info.table.totalPrice"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="receipt" items="${receipts}">
            <tr>
                <td>${receipt.id}</td>
                <td>${receipt.status.name}</td>
                <td>${fn:substring(receipt.createDate, 0, 19)}</td>
                <td>
                    <c:forEach var="dish" items="${receipt.dishes}">
                        ${dish.key.name}: ${dish.key.price}<pricetag:priceSign/> * ${dish.value}<br>
                    </c:forEach>
                </td>
                <td>${receipt.totalPrice}<pricetag:priceSign/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>
</div>
</body>
</html>