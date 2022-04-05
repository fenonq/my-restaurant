<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

<div class="update_wrapper">
    <p class="table_title"><fmt:message key="account.settings.redactor.edit.title"/></p>

    <div class="strip strip_update"></div>

    <div class="update_category_wrapper">
        <form method="post" action="${pageContext.request.contextPath}/account/dish-redactor/update-dish">
            <c:forEach var="localeEl" items="${localesArr}">
                <div class="localization_input">
                    <input name="name_${fn:toLowerCase(localeEl)}" required id="name_${fn:toLowerCase(localeEl)}"
                           type="text" value="${sessionScope.dishDescriptionMap.get(localeEl.id).name}"
                           placeholder="<fmt:message key="account.settings.redactor.dish.table.name"/>_${fn:toLowerCase(localeEl)}">

                    <textarea name="description_${fn:toLowerCase(localeEl)}" required
                              id="description_${fn:toLowerCase(localeEl)}" cols="30" rows="10"
                              placeholder="<fmt:message key="account.settings.redactor.dish.table.description"/>_${fn:toLowerCase(localeEl)}">${sessionScope.dishDescriptionMap.get(localeEl.id).description}</textarea>
                </div>
            </c:forEach>
            <label>
                <select name="category" class="filter-select">
                    <c:forEach var="category" items="${categories}">
                        <option ${sessionScope.subDish.categoryId == category.id ? "selected" : ""}
                                value="${category.id}">
                                ${category.name}
                        </option>
                    </c:forEach>
                </select>
            </label>
            <input name="weight" required value="${sessionScope.subDish.weight}" id="weight" type="number" min="1"
                   placeholder="<fmt:message key="account.settings.redactor.dish.table.weight"/>">
            <input name="price" required value="${sessionScope.subDish.price}" id="price" type="number" min="1"
                   placeholder="<fmt:message key="account.settings.redactor.dish.table.price"/>">
            <button type="submit"><fmt:message
                    key="account.settings.redactor.edit.button"/></button>
        </form>
    </div>
</div>
</body>
</html>
