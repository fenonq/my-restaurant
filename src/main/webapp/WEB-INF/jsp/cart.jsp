<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pricetag" uri="/WEB-INF/tld/price.tld" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="title.cart"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">

</head>

<body>
<%@include file="header.jspf" %>

<c:if test="${user.roleId != 1}">
    <jsp:useBean id="cart" scope="session" type="java.util.Map"/>

    <c:if test="${cart.size() == 0}">
        <p class="cart_empty"><fmt:message key="cart.info.empty"/></p>
    </c:if>

    <c:if test="${cart.size() != 0}">
        <div class="cart_wrapper">
            <div class="table_wrapper">
                <p class="table_title"><fmt:message key="cart.info.yourCart"/></p>
                <table class="table">
                    <thead>
                    <tr>
                        <th></th>
                        <th><fmt:message key="cart.info.table.name"/></th>
                        <th><fmt:message key="cart.info.table.weight"/></th>
                        <th><fmt:message key="cart.info.table.price"/></th>
                        <th><fmt:message key="cart.info.table.count"/></th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${cart}">
                        <tr>
                            <td>
                                <img src="${pageContext.request.contextPath}/static/img/dish-${item.key.id}.png"
                                     class="cart_dish_img" alt="dish-${item.key.id}.png"/>
                            </td>
                            <td>${item.key.name}</td>
                            <td>${item.key.weight} <fmt:message key="menu.info.gram"/></td>
                            <td>${item.key.price}<pricetag:priceSign/></td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/cart"
                                      class="form_button">
                                    <input name="count" type="number" min="1" value="${item.value}" max="10"/>
                                    <input name="id" style="display: none" value="${item.key.id}">
                                    <button type="submit"><fmt:message key="cart.button.save"/></button>
                                </form>
                            </td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/cart"
                                      class="form_button">
                                    <input name="count" style="display: none" type="number" value="-1"/>
                                    <input name="id" style="display: none" value="${item.key.id}">
                                    <button type="submit"><fmt:message key="cart.button.delete"/></button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="strip"></div>
            <div class="order_wrapper">
                <fmt:message key="cart.info.totalPrice"/>: ${sessionScope.totalPrice}
                <pricetag:priceSign/>
                <form action="${pageContext.request.contextPath}/account" method="post" class="order_button">
                    <button type="submit"><fmt:message key="cart.button.order"/></button>
                </form>
            </div>
        </div>
    </c:if>
</c:if>
</body>
</html>