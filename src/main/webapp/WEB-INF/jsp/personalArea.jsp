<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
</head>

<body>

	<h2>Личный кабинет пользователя</h2>
	
	<h3>Пользователь:
	<c:out value="${user_data.surname}" />
	<c:out value="${user_data.name}" />
	<c:out value="${user_data.patronymic}" />
	(<c:out value="${user_data.roleName}" />)<br/>	
	</h3>
	
	<b>Личные данные:</b><br/>
	Номер телефона:
	<c:out value="${user_data.phoneNumber}" /><br/>
	Паспорт:
	<c:out value="${user_data.passportSeries}" />
	<c:out value="${user_data.passportNumber}" /><br/>
	Дата рождения:
	<c:out value="${user_data.dateOfBirth}" /><br/>
	Адрес:
	<c:out value="${user_data.address}" /><br/>
	Почтовый индекс:
	<c:out value="${user_data.postCode}" /><br/><br/>


	<main>	
		<form action="controller" method="post">
				<input type="hidden" name="command" value="logout" />
				Выйти:<br/>
				<input type="submit" value="Выйти"/><br /> 
			</form>
	</main>
</body>
</html>