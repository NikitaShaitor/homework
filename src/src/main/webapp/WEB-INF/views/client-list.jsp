<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Клиенты</title></head>
<body>
<h1>Список клиентов</h1>
<a href="/admin/clients/new">Создать нового клиента</a>
<a href="/logout">Выйти</a>
<table border="1">
    <tr><th>ID</th><th>Имя</th></tr>
    <c:forEach items="${clients}" var="client">
        <tr>
            <td>${client.id}</td>
            <td>${client.name}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>