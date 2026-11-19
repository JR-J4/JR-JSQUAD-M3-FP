<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Фінал · Судний день для Java-джуна</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/style.css">
</head>
<body>
<main class="terminal">
    <header class="terminal-header"><span>ТЕРМІНАЛ JAVA-СПІВБЕСІДИ</span><span class="status">● СЕСІЮ ЗАВЕРШЕНО</span></header>
    <section class="hero final-hero">
        <div>
            <p class="eyebrow">// РЕЗУЛЬТАТ СПІВБЕСІДИ</p>
            <c:choose>
                <c:when test="${outcomeName eq 'OFFER'}">
                    <h1 class="success">Офер отримано!</h1>
                    <p>Синьйор посміхається: «Вітаю тебе, <c:out value="${game.candidateName}"/>! Ти не зламався, знаєш базу і вмієш тримати удар. HR вже готує твій офер. Ласкаво просимо в команду!»</p>
                </c:when>
                <c:when test="${outcomeName eq 'DEVOPS'}">
                    <h1 class="success">Народжений для DevOps</h1>
                    <p>Синьйор задумливо дивиться на тебе: «Знаєш, <c:out value="${game.candidateName}"/>, на Java-джуна ти трохи занадто відчайдушний... Але нам якраз потрібна людина, яка не боїться перезавантажувати прод у п'ятницю ввечері. Беремо тебе стажером у DevOps!»</p>
                </c:when>
                <c:otherwise>
                    <h1 class="failure">Ми вам зателефонуємо</h1>
                    <p>На екрані з'являється ввічлива посмішка: «Дякуємо за приділений час! У вас дуже цікавий досвід. Ми обов'язково збережемо ваше резюме в нашій базі і зателефонуємо... колись».</p>
                </c:otherwise>
            </c:choose>
        </div>
        <img class="hero-image ending-image" src="${pageContext.request.contextPath}/assets/images/ending.svg" alt="Завершення технічної співбесіди">
    </section>
    <section class="panel result-details">
        <p>Кандидат: <strong><c:out value="${game.candidateName}"/></strong></p>
        <p>Нервові клітини наприкінці: <strong><c:out value="${game.lives}"/></strong></p>
        <form method="post" action="${pageContext.request.contextPath}/quest">
            <input type="hidden" name="action" value="restart">
            <button type="submit" class="button">Пройти співбесіду ще раз</button>
        </form>
        <a class="text-link" href="${pageContext.request.contextPath}/quest?view=welcome">Повернутися на стартову сторінку</a>
    </section>
</main>
</body>
</html>
