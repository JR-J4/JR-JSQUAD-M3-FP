<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Available Modules</title>

    <!-- Tailwind Play CDN -->
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
    <style type="text/tailwindcss">
        @theme {
            --color-primary: #4f46e5;
        }
    </style>
</head>


<body class="bg-gray-100 min-h-screen flex flex-col">

<header class="bg-white shadow">
    <div class="container mx-auto px-6 py-4">
        <h1 class="text-3xl font-bold text-primary">Available Student Projects</h1>
    </div>
</header>

<main class="container mx-auto px-6 py-8 flex-grow">
    <c:choose>
        <c:when test="${not empty requestScope.modules}">
            <div class="grid gap-6 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3">
                <c:forEach var="moduleName" items="${requestScope.modules}">
                    <a href="${pageContext.request.contextPath}/${moduleName}/">
                        <div class="bg-white rounded-2xl shadow p-6 transform hover:scale-105 transition text-xl font-semibold text-gray-800 hover:text-primary">
                                ${moduleName}
                    </div>
                    </a>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <p class="text-center text-gray-600">No modules found.</p>
        </c:otherwise>
    </c:choose>
</main>

<footer class="bg-white shadow-inner">
    <div class="container mx-auto px-6 py-4 text-center text-gray-500">
        &copy; ${pageContext.request.serverName}
    </div>
</footer>

</body>
</html>
