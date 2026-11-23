<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>НЛО: текстовий квест</title>
    <style>
        body { margin: 0; font-family: Arial, Helvetica, sans-serif; color: #222; }
        .hero { background: #4caf50; color: #fff; text-align: center; padding: 90px 20px; }
        .hero h1 { font-size: 48px; font-weight: 400; letter-spacing: 3px; margin: 0 0 30px; }
        .hero a { background: #000; color: #fff; padding: 12px 28px; text-decoration: none; }
        .story { max-width: 800px; margin: 50px auto; padding: 0 20px; line-height: 1.6; }
        .story .icon { float: right; font-size: 90px; margin-left: 20px; }
        form { margin-top: 30px; padding: 20px; background: #f1f8f2; border-left: 5px solid #4caf50; }
        input[type=text] { padding: 8px; font-size: 16px; width: 220px; }
        button { background: #000; color: #fff; border: 0; padding: 10px 24px; font-size: 16px; cursor: pointer; }
    </style>
</head>
<body>
<div class="hero">
    <h1>ТРОХИ ІСТОРІЇ</h1>
    <a href="#story">Читати</a>
</div>

<div class="story" id="story">
    <div class="icon" aria-hidden="true">&#128760;</div>
    <h2>Невпізнаний політ</h2>
    <p>
        Ви приходите до тями посеред нічного поля. Голова гуде, а в пам'яті порожньо: ви не пам'ятаєте
        ані власного імені, ані того, як тут опинилися. Над вами завис велетенський диск, що тихо мерехтить
        крижаним світлом.
    </p>
    <p>
        З диска лунає голос і пропонує вам прийняти виклик. Кожне ваше рішення має наслідки:
        одні відповіді наближають до дому, інші закривають дорогу назад назавжди.
        Спробуйте пройти квест до кінця, а якщо не вийде&nbsp;&mdash; розпочніть спочатку.
    </p>

    <form action="${pageContext.request.contextPath}/start" method="post" accept-charset="UTF-8">
        <p><label for="name"><b>Як вас звати, мандрівнику?</b></label></p>
        <p><input type="text" id="name" name="name" maxlength="30" placeholder="Ваше ім'я"></p>
        <button type="submit">Розпочати гру</button>
    </form>
</div>
</body>
</html>
