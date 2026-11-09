<jsp:useBean id="score" scope="request" type="java.lang.Integer"/>
<jsp:useBean id="totalQuestions" scope="request" type="java.lang.Integer"/>

<%@ page
        contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Result</title>
    <style>
        table, th, td {
            border: 1px solid #333;
            padding: 8px;
            border-collapse: collapse;
        }

        th {
            background: #eee;
        }
    </style>
</head>
<body>
    <h1>Your Score: ${score} / ${totalQuestions}</h1>
</body>
</html>
