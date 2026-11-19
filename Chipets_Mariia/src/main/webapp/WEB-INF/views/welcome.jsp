<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Судний день для Java-джуна</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<main class="terminal">
    <header class="terminal-header"><span>ТЕРМІНАЛ JAVA-СПІВБЕСІДИ</span><span class="status">● НА ЗВ’ЯЗКУ</span></header>
    <section class="hero">
        <div>
            <p class="eyebrow">// ВХІДНИЙ ВИКЛИК</p>
            <h1>Судний день для Java-джуна</h1>
            <p>Після 99 автоматичних відмов із текстом «На жаль, ми обрали кандидата з 10-річним досвідом на позицію стажера» сталося диво — тобі надіслали лінк на технічну співбесіду. І ось ця мить — ти закриваєш вкладку з теорією про «HashMap», перевіряєш мікрофон і заходиш у Zoom. На екрані — суворий Синьйор у розтягнутому светрі з літровою чашкою еспресо. Він уже готовий питати про «багатопоточність» і «сміттєзбирач». У запасі в тебе лише 3 нервові клітини. Протримайся до кінця дзвінка й отримай свій перший офер — або почуй легендарне «Ми вам зателефонуємо».</p>
        </div>
        <img class="hero-image" src="${pageContext.request.contextPath}/assets/images/interview.svg" alt="Термінал технічної співбесіди з Java">
    </section>

    <section class="panel">
        <h2>Ідентифікація кандидата</h2>
        <form method="get" action="${pageContext.request.contextPath}/quest" class="form-row">
            <input type="hidden" name="view" value="welcome">
            <label for="candidateName">Ім’я кандидата</label>
            <input id="candidateName" name="candidateName" type="text" maxlength="80" required value="${fn:escapeXml(candidateName)}" placeholder="Наприклад, Марія">
            <button type="submit" class="button secondary">Перевірити спроби</button>
        </form>
        <c:if test="${not empty candidateName}">
            <p class="attempt-count">Кількість спроб для <strong><c:out value="${candidateName}"/></strong>: <strong><c:out value="${attemptCount}"/></strong></p>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <p class="error" role="alert"><c:out value="${errorMessage}"/></p>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/quest">
            <input type="hidden" name="action" value="start">
            <input type="hidden" name="candidateName" value="${fn:escapeXml(candidateName)}">
            <c:choose>
                <c:when test="${empty candidateName}"><button type="submit" class="button" disabled>Почати співбесіду</button></c:when>
                <c:otherwise><button type="submit" class="button">Почати співбесіду</button></c:otherwise>
            </c:choose>
        </form>
    </section>
    <p class="hint">Початковий запас: 3 нервові клітини. Удачі — вона тобі знадобиться.</p>
</main>
</body>
</html>
