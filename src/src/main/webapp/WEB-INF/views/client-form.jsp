<!DOCTYPE html>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head><title>Новый клиент</title></head>
<body>
<h1>Создание клиента</h1>
<form:form method="post" modelAttribute="client" action="/admin/clients">
    Имя: <form:input path="name"/><br/>
    <button type="submit">Сохранить</button>
</form:form>
<a href="/admin/clients">Назад к списку</a>
</body>
</html>