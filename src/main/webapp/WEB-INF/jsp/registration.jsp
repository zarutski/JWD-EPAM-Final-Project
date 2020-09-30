<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>Registration form</title>
	
	<c:if test="${not empty sessionScope.local}">
        <fmt:setLocale value="${sessionScope.local}"/>
    </c:if>
    
	<fmt:setBundle basename="localization.payment_app"/>
     
    <fmt:message key="registration_page_message" var="registration_message"/>
    <fmt:message key="app_description_name" var="app_description"/>
    <fmt:message key="main_page_link" var="main_link"/>
    
    
    <fmt:message key="form_name" var="name_message"/>
    <fmt:message key="form_surname" var="surname_message"/>
    <fmt:message key="form_patronymic" var="patronymic_message"/>
    <fmt:message key="form_phone_number" var="phone_number_message"/>
    <fmt:message key="form_passport_serial" var="passport_serial_message"/>
    <fmt:message key="form_passport_number" var="passport_number_message"/>
    <fmt:message key="form_date_of_birth" var="date_of_birth_message"/>
    <fmt:message key="form_address" var="address_message"/>
    <fmt:message key="form_post_code" var="post_code_message"/>
    <fmt:message key="form_email" var="email_message"/>
    <fmt:message key="form_login" var="login_message"/>
    <fmt:message key="form_password" var="password_message"/>
    <fmt:message key="form_confirm_password" var="confirm_password_message"/>
</head>
<body>
	
	<header>
		<h1 align="center">${registration_message}</h1>
		<h3 align="center">${app_description}</h3>
	</header>

	<main>
		<a href="controller?command=go_to_main_page">${main_link}</a>

		<form action="controller" method="post" accept-charset="utf-8">
			<input type="hidden" name="command" value="registration" />
			${name_message}<br/>
			<input type="text" name="name" value="" /><br/>
			${surname_message}<br/>
			<input type="text" name="surname" value="" /><br/>
			${patronymic_message}<br/>
			<input type="text" name="patronymic" value="" /><br/>
			${phone_number_message}<br/>
			<input type="text" name="phone_number" value="" /><br/>
			${passport_serial_message}<br/>
			<input type="text" name="passport_series" value="" /><br/>
			${passport_number_message}<br/>
			<input type="text" name="passport_number" value="" /><br/>
			${date_of_birth_message}<br/>
			<input type="text" name="date_of_birth" value="" /><br/>
			${address_message}<br/>
			<input type="text" name="address" value="" /><br/>
			${post_code_message}<br/>
			<input type="text" name="post_code" value="" /><br/>
			${email_message}<br/>
			<input type="text" name="email" value="" /><br/>
			${login_message}<br/>
			<input type="text" name="login" value="" /><br/>
			${password_message}<br/> 
			<input type="password" name="password" value="" /><br />
			${confirm_password_message}<br/> 
			<input type="password" name="password" value="" /><br /> 
			<input type="submit" value="регистрация"/><br /> 
		</form>
	</main>
	
	<br/><br/> 
	<form action="controller?command=localization" method="post">
                        <input type="hidden" name="local" value="ru"/>
                        <input type="hidden" name="previous_command" value="${pageContext.request.getParameter("command")}">
                        <input type="submit" value="ru"/>
                    </form>
                    <form action="controller?command=localization" method="post">
                        <input type="hidden" name="local" value="en"/>
                        <input type="hidden" name="previous_command" value="${pageContext.request.getParameter("command")}">
                        <input type="submit" value="en"/>
	</form>

</body>
</html>