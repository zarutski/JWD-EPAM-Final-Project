<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html>
<html>
<head>
     <title>e-payments</title>

	<style>
		#navbar {
		  margin: 0;
		  padding: 0;
		  list-style-type: none;
		}
		#navbar li { display: inline; }
	</style>
	
	<c:if test="${not empty sessionScope.local}">
        <fmt:setLocale value="${sessionScope.local}"/>
    </c:if>
    
     <fmt:setBundle basename="localization.payment_app"/>
     
     <fmt:message key="app_title_name" var="app_title"/>
     <fmt:message key="app_description_name" var="app_description"/>
     
     <fmt:message key="sign_in_message" var="sign_in"/>
     <fmt:message key="sign_up_message" var="sign_up"/>

</head>

<body>
	<header>
		<h1 align="center">${app_title}</h1>
		<h3 align="center">${app_description}</h3>
	</header>

	<main>
		<nav>
			<ul id="navbar">
				<li><a href="controller?command=go_to_authentication_page">${sign_in}</a></li>
	  			<li><a href="controller?command=go_to_registration_page">${sign_up}</a></li>
			</ul>
			<br/>
		</nav>
	</main>		
	
	<br /> 
	<br /> 
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