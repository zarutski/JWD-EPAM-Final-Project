<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<title>E-payments</title>
	<link rel="stylesheet" media="all" type="text/css" href="${pageContext.request.contextPath}/css/base.css">
	<link href="${pageContext.request.contextPath}/img/testicon.png" rel="icon">
	<link href="https://fonts.googleapis.com/css2?family=Nunito+Sans&display=swap" rel="stylesheet"> 
	
	<c:if test="${not empty sessionScope.local}">
        <fmt:setLocale value="${sessionScope.local}"/>
    </c:if>
    
     <fmt:setBundle basename="localization.payment_app"/>

     <fmt:message key="app_title_name" var="app_title"/>
</head>

<body>
	<header>
		<div class="header">
			<div class="container">
				<div class="nav">
					<img src="img/logo-50.png" alt="Bank logo"/>
					<p align="center">${app_title}</p>
					<a href="tel:+375001234567" class="tel">+375 00 123-45-67</a>
					
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
				</div>
			</div>
		</div>
	</header>
</body>
