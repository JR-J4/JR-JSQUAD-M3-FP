<%--suppress XmlDefaultAttributeValue --%>
<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz</title>
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
<h1>Take the Quiz</h1>
<form action="${pageContext.request.contextPath}" method="post">
    <table>
        <tr>
            <th>#</th>
            <th>QuestStart.Question</th>
            <th>Options</th>
        </tr>
        <c:forEach var="question" items="${sessionScope.questions}" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td>${question.text}</td>
                <td>
                    <c:forEach var="opt" items="${question.options}">
                        <label>
                            <input type="radio"
                                   name="answer_${question.id}"
                                   value="${opt}"
                                   required/>
                                ${opt}
                        </label><br/>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
    </table>
    <button type="submit">Submit Quiz</button>
</form>
</body>
</html>
