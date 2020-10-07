<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>User's personal area</title>
	
	<c:if test="${not empty sessionScope.local}">
        <fmt:setLocale value="${sessionScope.local}"/>
    </c:if>
    
	<fmt:setBundle basename="localization.payment_app"/>
    
    <fmt:message key="personal_area.title" var="personal_area_title"/>
    <fmt:message key="personal_area.user_info" var="personal_area_user_info"/>
    <fmt:message key="personal_area.user_data_info" var="personal_area_user_data_info"/>
    <fmt:message key="personal.phone_number" var="personal_phone_number"/>
    <fmt:message key="personal.passport" var="personal_passport"/>
    <fmt:message key="personal.date_of_birth" var="personal_date_of_birth"/>
    <fmt:message key="personal.address" var="personal_address"/>
    <fmt:message key="personal.post_code" var="personal_post_code"/>
    
    <fmt:message key="button.logout_message" var="logout_message"/>

</head>

<body>

	<h2>${personal_area_title}</h2>
	
	<h3>${personal_area_user_info}
	<c:out value="${user_data.surname}" />
	<c:out value="${user_data.name}" />
	<c:out value="${user_data.patronymic}" />
	(<c:out value="${user_data.roleName}" />)<br/>	
	</h3>
	
	<b>${personal_area_user_data_info}</b><br/>
	${personal_phone_number}
	<c:out value="${user_data.phoneNumber}" /><br/>
	${personal_passport}
	<c:out value="${user_data.passportSeries}" />
	<c:out value="${user_data.passportNumber}" /><br/>
	${personal_date_of_birth}
	<c:out value="${user_data.dateOfBirth}" /><br/>
	${personal_address}
	<c:out value="${user_data.address}" /><br/>
	${personal_post_code}
	<c:out value="${user_data.postCode}" /><br/><br/>


	<main>	
		<form action="controller" method="post">
				<input type="hidden" name="command" value="logout" /><br/>
				<input type="submit" value="${logout_message}"/><br /> 
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