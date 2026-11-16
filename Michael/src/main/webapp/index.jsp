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
<c:if test="${sessionScope.gameFinished}">
    <dialog id="finish-dialog" class="modal">
        <h2>Гра закінчена, <c:out value="${sessionScope.playerName}"/>!</h2>
        <p>Зіграно разів: <strong><c:out value="${sessionScope.gamesCount}"/></strong></p>
        <form method="dialog">
            <button class="btn" type="submit">Закрити</button>
        </form>
    </dialog>
    <script>document.getElementById('finish-dialog').showModal();</script>
    <c:remove var="gameFinished" scope="session"/>
</c:if>
<body>

<div class="hero">
    <h1>ПАДІННЯ ПРОДАКШЕНУ</h1>
    <a class="btn" href="#story">Читати</a>
</div>

<div class="story" id="story">
    <h2>П'ятниця, 18:47…</h2>
    <p>
        Ви джуніор-розробник і вже збираєтеся додому. Ноутбук закритий, куртка на плечах,
        попереду вихідні. Аж раптом чат вибухає повідомленнями: продакшен упав.
    </p>
    <p>
        Клієнти не можуть зайти на сайт, підтримка завалена скаргами, а тімлід у відпустці
        і не відповідає. Кожне ваше рішення визначить, чи врятуєте ви вечір, чи зробите гірше.
    </p>
    <p class="dim">Обирайте уважно: деякі відповіді одразу призводять до поразки!</p>

    <c:if test="${not empty sessionScope.playerName}">
        <p class="welcome-back">
            З поверненням, <c:out value="${sessionScope.playerName}"/>!
        </p>
    </c:if>

    <form action="${pageContext.request.contextPath}/start" method="post">
        <input type="text" name="playerName" maxlength="30" placeholder="Ваше ім'я"
               value="<c:out value='${sessionScope.playerName}'/>" required>
        <button class="btn" type="submit">Почати гру</button>
    </form>
</div>
</body>
</html>