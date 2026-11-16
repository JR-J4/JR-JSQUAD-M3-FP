<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Падіння продакшену</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="card">
    <c:choose>
        <%-- финал: результат и две кнопки в ряд --%>
        <c:when test="${step.outcome != 'NONE'}">
            <div class="result ${step.outcome == 'WIN' ? 'win' : 'lose'}">
                <c:out value="${step.text}"/>
            </div>
            <form action="${pageContext.request.contextPath}/restart" method="post">
                <div class="actions">
                    <button class="btn" type="submit">Почати заново</button>
                    <button class="btn btn-danger" type="submit"
                            formaction="${pageContext.request.contextPath}/finish">Завершити гру</button>
                </div>
            </form>
        </c:when>

        <%-- обычный вопрос: радиокнопки и две кнопки в ряд --%>
        <c:otherwise>
            <h1><c:out value="${step.text}"/></h1>
            <form action="${pageContext.request.contextPath}/game" method="post">
                <c:forEach var="option" items="${step.options}" varStatus="status">
                    <label class="option">
                        <input type="radio" name="answer" value="${status.index}" required>
                        <c:out value="${option.label}"/>
                    </label>
                </c:forEach>

                <div class="actions">
                    <button class="btn" type="submit">Відповісти</button>
                    <button class="btn btn-danger" type="submit" formnovalidate
                            formaction="${pageContext.request.contextPath}/finish">Завершити гру</button>
                </div>
            </form>
        </c:otherwise>
    </c:choose>

    <div class="stats">
        <h3>Відомості</h3>
        <p>Гравець: <c:out value="${sessionScope.playerName}"/></p>
        <p>Рахунок: <c:out value="${sessionScope.gamesCount}"/></p>
    </div>
</div>
</body>
</html>