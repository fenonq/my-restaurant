<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="pricetag" uri="/WEB-INF/tld/price.tld" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="local"/>


<html>

<head>
    <title><fmt:message key="account.settings.redactor.dish"/></title>
    <link
            href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap"
            rel="stylesheet"
    />

    <link href="${pageContext.request.contextPath}/static/css/styles.css" rel="stylesheet" type="text/css">

</head>

<body>
<%@include file="header.jspf" %>

<div class="table_wrapper">
    <p class="table_title"><fmt:message key="account.settings.redactor.dish.table.allDishes"/></p>
    <table class="table">
        <thead>
        <tr>
            <th>№</th>
            <th><fmt:message key="account.settings.redactor.dish.table.name"/></th>
            <th><fmt:message key="account.settings.redactor.dish.table.description"/></th>
            <th><fmt:message key="account.settings.redactor.dish.table.category"/></th>
            <th><fmt:message key="account.settings.redactor.dish.table.weight"/></th>
            <th><fmt:message key="account.settings.redactor.dish.table.price"/></th>
            <th><fmt:message key="account.info.table.status"/></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="dishEl" items="${dishes}">
            <tr>
                <td>${dishEl.id}</td>
                <td>${dishEl.name}</td>
                <td>${dishEl.description}</td>
                <td>${dishEl.categoryId}</td>
                <td>${dishEl.weight}</td>
                <td>${dishEl.price}<pricetag:priceSign/></td>
                    <%--                <td>--%>
                    <%--                    <form method="post" action="${pageContext.request.contextPath}/account/dish-redactor"--%>
                    <%--                          class="form_button">--%>
                    <%--                        <input name="dishId" value="${dishEl.id}" style="display: none">--%>
                    <%--                        <input name="action" value="-1" style="display: none">--%>
                    <%--                        <button type="submit"><fmt:message--%>
                    <%--                                key="account.settings.redactor.dish.table.button.delete"/></button>--%>
                    <%--                    </form>--%>
                    <%--                </td>--%>

                <td>
                    <form method="post" action="${pageContext.request.contextPath}/account/dish-redactor"
                          class="form_button">
                        <input name="dishId" value="${dishEl.id}" style="display: none">
                        <input name="isVisible" value="${dishEl.isVisible}" style="display: none">
                        <input name="action" value="-1" style="display: none">
                        <button type="submit">
                            <c:if test="${dishEl.isVisible == 1}">
                                <fmt:message key="account.settings.redactor.dish.table.button.visible"/>
                            </c:if>
                            <c:if test="${dishEl.isVisible == 0}">
                                <fmt:message key="account.settings.redactor.dish.table.button.invisible"/>
                            </c:if>
                        </button>
                    </form>
                </td>

                <td>
                    <form method="post" action="${pageContext.request.contextPath}/account/dish-redactor"
                          class="form_button">
                        <input name="dishId" value="${dishEl.id}" style="display: none">
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
    <form method="post" action="${pageContext.request.contextPath}/account/dish-redactor">
        <c:forEach var="localeEl" items="${localesArr}">
            <div class="localization_input">
                <input name="name_${fn:toLowerCase(localeEl)}" required id="name_${fn:toLowerCase(localeEl)}"
                       type="text"
                       placeholder="<fmt:message key="account.settings.redactor.dish.table.name"/>_${fn:toLowerCase(localeEl)}">

                <textarea name="description_${fn:toLowerCase(localeEl)}" required
                          id="description_${fn:toLowerCase(localeEl)}" cols="30" rows="10"
                          placeholder="<fmt:message key="account.settings.redactor.dish.table.description"/>_${fn:toLowerCase(localeEl)}"></textarea>
            </div>
        </c:forEach>
        <label>
            <select name="category" class="filter-select">
                <c:forEach var="category" items="${categories}">
                    <option ${param.category == category.id ? "selected" : ""} value="${category.id}">
                            ${category.name}
                    </option>
                </c:forEach>
            </select>
        </label>
        <input name="weight" required id="weight" type="number" min="1"
               placeholder="<fmt:message key="account.settings.redactor.dish.table.weight"/>">
        <input name="price" required id="price" type="number" min="1"
               placeholder="<fmt:message key="account.settings.redactor.dish.table.price"/>">
        <input name="action" value="1" style="display: none">
        <button type="submit"><fmt:message key="account.settings.redactor.dish.table.button.create"/></button>
    </form>
</div>
</body>
</html>