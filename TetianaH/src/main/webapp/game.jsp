<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>НЛО: текстовий квест</title>
    <style>
        body { font-family: Arial, Helvetica, sans-serif; max-width: 760px; margin: 40px auto; padding: 0 20px; color: #222; }
        h1 { font-weight: 400; }
        .option { display: block; margin: 12px 0; font-size: 18px; }
        button { background: #000; color: #fff; border: 0; padding: 10px 24px; font-size: 16px; cursor: pointer; text-transform: uppercase; }
        .result { font-size: 22px; font-weight: bold; margin: 20px 0; }
        .win { color: #2e7d32; }
        .lose { color: #d84315; }
        .stats { margin-top: 40px; padding: 10px 20px; background: #dffcff; border-left: 5px solid #2196f3; }
    </style>
</head>
<body>
<c:choose>
    <c:when test="${step.finished}">
        <h1><c:out value="${step.text}"/></h1>
        <c:choose>
            <c:when test="${step.win}"><p class="result win">Перемога!</p></c:when>
            <c:otherwise><p class="result lose">Поразка</p></c:otherwise>
        </c:choose>
        <form action="${pageContext.request.contextPath}/restart" method="post">
            <button type="submit">Зіграти ще раз</button>
        </form>
    </c:when>
    <c:otherwise>
        <h1><c:out value="${step.text}"/></h1>
        <form action="${pageContext.request.contextPath}/game" method="post">
            <c:forEach var="option" items="${step.options}" varStatus="status">
                <label class="option">
                    <input type="radio" name="answer" value="${status.index}" required>
                    <c:out value="${option.text}"/>
                </label>
            </c:forEach>
            <button type="submit">Відповісти</button>
        </form>
    </c:otherwise>
</c:choose>

<div class="stats">
    <p><b>Статистика</b></p>
    <p>IP-адреса: <c:out value="${pageContext.request.remoteAddr}"/></p>
    <p>Ім'я гравця: <c:out value="${sessionScope.game.playerName}"/></p>
    <p>Зіграно ігор: ${sessionScope.game.gamesPlayed}
        (перемог: ${sessionScope.game.wins}, поразок: ${sessionScope.game.losses})</p>
</div>
</body>
</html>
