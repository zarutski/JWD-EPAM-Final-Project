<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
	<title>Login</title>
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
     
     <fmt:message key="authentication_page_message" var="authentication_message"/>
     <fmt:message key="app_description_name" var="app_description"/>
     
     <fmt:message key="main_page_link" var="main_link"/>
     <fmt:message key="sign_up_message" var="sign_up"/>
     
     <fmt:message key="form_login" var="login_message"/>
     <fmt:message key="form_password" var="password_message"/>
     <fmt:message key="sign_in_message" var="sign_in"/>
     <fmt:message key="authentication_error.01" var="authentification_error"/>
     <fmt:message key="authentication_error.02" var="authentification_service_error"/>

</head>


<body>
	
	<header>
		<h1 align="center">${authentication_message}</h1>
		<h3 align="center">${app_description}</h3>
	</header>

	<main>
	<nav>
			<ul id="navbar">
				<li><a href="controller?command=go_to_main_page">${main_link}</a></li>
	  			<li><a href="controller?command=go_to_registration_page">${sign_up}</a></li>
			</ul>
	</nav>

				
		<form action="controller" method="post">
			<input type="hidden" name="command" value="authentication" />
			${login_message}<br/><input type="text" name="login" value="" required/><br/>
			${password_message}<br/><input type="password" name="password" value="" required/><br/>
			
			<c:choose>
				<c:when test="${pageContext.request.getParameter(\"error\") eq 'error_01'}">
					<p>${authentification_error}</p>
				</c:when>
				<c:when test="${pageContext.request.getParameter(\"error\") eq 'error_02'}">
					<p>${authentification_service_error}</p>
				</c:when>
			</c:choose>
			
			<input type="submit" value="${sign_in}"/><br /> 
		</form>
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