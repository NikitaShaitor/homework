<!DOCTYPE html>
<html>
<head><title>Авторизация</title></head>
<body>
<h2>Вход для администратора</h2>
<c:if test="${not empty error}"><p style="color:red;">${error}</p></c:if>
<form method="post" action="/login">
    Логин: <input type="text" name="username"/><br/>
    Пароль: <input type="password" name="password"/><br/>
    <button type="submit">Войти</button>
</form>
</body>
</html>