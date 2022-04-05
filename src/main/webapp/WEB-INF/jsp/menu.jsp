<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pricetag" uri="/WEB-INF/tld/price.tld" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>

<html>
<head>
    <title><fmt:message key="title.menu"/></title>

    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />
    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">
</head>

<body>

<%@include file="header.jspf" %>

<main class="main">
    <div>
        <h1 class="menu"><fmt:message key="menu.info.main.menu"/></h1>
        <div class="filter_wrapper">
            <form class="filter_form" action="${pageContext.request.contextPath}/menu" method="get">
                <div>
                    <fmt:message key="menu.info.select.category"/>:
                        <select name="category" class="filter-select">
                            <option value="0"><fmt:message key="menu.info.select.allDishes"/></option>
                            <c:forEach var="category" items="${categories}">
                                <option ${param.category == category.id ? "selected" : ""} value="${category.id}">
                                        ${category.name}
                                </option>
                            </c:forEach>
                        </select>
                </div>
                <div>
                    <fmt:message key="menu.info.select.sortBy"/>:
                    <select name="orderBy" class="filter-select">
                        <c:forEach var="orderByEl" items="${orderByParams}">
                            <option ${param.orderBy == orderByEl.value ? "selected" : ""} value="${orderByEl.value}">
                                <fmt:message key="${orderByEl.key}"/></option>
                        </c:forEach>
                    </select>
                </div>
                <div>
                    <fmt:message key="menu.info.select.dishesOnPage"/>:
                    <select name="dishesOnPage" class="filter-select">
                        <c:forTokens items="4,8,12" delims="," var="item">
                            <option ${param.dishesOnPage == item ? "selected" : ""}>${item}</option>
                        </c:forTokens>
                    </select>
                </div>
                <select name="page" style="display: none">
                    <option value="0" selected></option>
                </select>
                <button type="submit">
                    <fmt:message key="menu.info.button.filter"/></button>
            </form>
        </div>
        <div class="dishes_wrapper">
            <div class="dishes">
                <c:forEach var="dish" items="${dishes}">
                    <div class="dish">
                        <img src="${pageContext.request.contextPath}/static/img/dish-${dish.id}.png"
                             alt="${dish.name} img"
                             class="dish_img">
                        <p class="dish_text dish_name">${dish.name}</p>
                        <p class="dish_text dish_description">${fn:substring(dish.description, 0, 110)}</p>
                        <p class="dish_text dish_weight">${dish.weight} <fmt:message key="menu.info.gram"/></p>
                        <p class="dish_text dish_price">${dish.price}<pricetag:priceSign/></p>
                        <c:if test="${user.roleId != 1}">
                            <form action="${pageContext.request.contextPath}/cart" method="post">
                                <input name="id" style="display: none" value="${dish.id}">
                                <input value="1" name="count" style="display: none">
                                <button type="submit"
                                        class="dish_button"><fmt:message key="menu.nav.bar.button.add"/></button>
                            </form>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <nav class="bottom_nav">
        <ul class="nav_list">
            <li class="nav_list__item ${param.page > 0 ? "" : "disabled"}">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/menu?category=${param.category}&orderBy=${param.orderBy}&page=${param.page-1}&dishesOnPage=${param.dishesOnPage}"
                   tabindex="-1">
                    <fmt:message key="menu.nav.bar.button.previous"/>
                </a>
            </li>
            <c:forEach var="num" begin="0" end="${pages}">
                <li class="nav_list__item ${page == num ? "disabled" : ""}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/menu?category=${param.category}&orderBy=${param.orderBy}&page=${num}&dishesOnPage=${param.dishesOnPage}">
                            ${num+1}
                    </a>
                </li>
            </c:forEach>
            <li class="nav_list__item ${param.page < pages ? "" : "disabled"}">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/menu?category=${param.category}&orderBy=${param.orderBy}&page=${param.page+1}&dishesOnPage=${param.dishesOnPage}">
                    <fmt:message key="menu.nav.bar.button.next"/>
                </a>
            </li>
        </ul>
    </nav>
</main>

</body>
</html>