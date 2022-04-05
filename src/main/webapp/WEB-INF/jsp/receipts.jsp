<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="pricetag" uri="/WEB-INF/tld/price.tld" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>

<head>
    <title><fmt:message key="title.receipts"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">
</head>
<body>

<%@include file="header.jspf" %>

<div class="table_wrapper">
    <p class="table_title"><fmt:message key="receipts.info.allReceipts"/></p>
    <div class="filter_wrapper">
        <form action="${pageContext.request.contextPath}/receipts" class="filter_form">
            <select name="statusId" class="filter-select">
                <option value="0"><fmt:message key="receipts.info.allReceipts"/></option>
                <c:forEach var="status" items="${statuses}">
                    <option ${param.statusId == status.id ? "selected" : ""} value="${status.id}">
                            ${status.name}
                    </option>
                </c:forEach>
                <option ${param.statusId == statuses.size() + 1 ? "selected" : ""} value="${statuses.size() + 1}">
                    <fmt:message
                            key="receipts.info.acceptedReceipts"/>
                </option>
            </select>
            <button type="submit"><fmt:message key="receipts.info.button.filter"/></button>
        </form>
    </div>
    <table class="table">
        <thead>
        <tr>
            <th>№</th>
            <th><fmt:message key="receipts.info.table.userId"/></th>
            <th><fmt:message key="receipts.info.table.managerId"/></th>
            <th><fmt:message key="receipts.info.table.status"/></th>
            <th><fmt:message key="receipts.info.table.orderDate"/></th>
            <th><fmt:message key="receipts.info.table.dishes"/></th>
            <th><fmt:message key="receipts.info.table.totalPrice"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="receipt" items="${receipts}">
            <%--        <jsp:useBean id="receipt" class="com.denbondd.restaurant.db.entity.Receipt"/>--%>
            <tr>
                <td>${receipt.id}</td>
                <td>${receipt.userId}</td>
                <td>${receipt.managerId}</td>
                <td>${receipt.status.name}
                    <c:if test="${receipt.status.id != 5 && (receipt.managerId == 0 || receipt.managerId == user.id)}">
                        <form method="post" action="${pageContext.request.contextPath}/receipts">
                            <input value="${receipt.id}" name="id" style="display: none">
                            <input value="${receipt.status.id + 1}" name="statusId" style="display: none">
                            <button type="submit"><fmt:message key="receipts.info.table.button.changeStatus"/></button>
                        </form>
                    </c:if>
                </td>
                <td>${fn:substring(receipt.createDate, 0, 19)}</td>
                <td>
                    <c:forEach var="dish" items="${receipt.dishes}">
                        ${dish.key.name}: ${dish.key.price}<pricetag:priceSign/> * ${dish.value} <br>
                    </c:forEach>
                </td>
                <td>${receipt.totalPrice}<pricetag:priceSign/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>