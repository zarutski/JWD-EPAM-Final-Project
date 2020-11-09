<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="main_page_link" var="main_link"/>
<fmt:message key="sign_up_message" var="sign_up"/>
     
<fmt:message key="form_login" var="login_message"/>
<fmt:message key="form_password" var="password_message"/>
<fmt:message key="sign_in_message" var="sign_in"/>

<fmt:message key="authentication_error.01" var="authentification_error"/>
<fmt:message key="authentication_error.02" var="authentification_service_error"/>
<fmt:message key="authentication.reg_success" var="reg_success"/>

<main class="main">
	<div class="content">
		<div class="block">
			<nav>
				<ul id="navbar">
					<li><a href="controller?command=go_to_main_page">${main_link}</a></li>
					<li><a href="controller?command=go_to_registration_page">${sign_up}</a></li>
				</ul>
			</nav>
			<div id="about" class="block-column">
				<form action="controller" method="post">
					<input type="hidden" name="command" value="authentication" />
					${login_message}<br/><input type="text" name="login" value="" required/><br/>
					${password_message}<br/><input type="password" name="password" value="" required/><br/><br/>

					<input type="submit" value="${sign_in}"/><br/> 
					
					<c:choose>
						<c:when test="${pageContext.request.getParameter(\"error\") eq 'error_01'}">
							<p>${authentification_error}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"error\") eq 'error_02'}">
							<p>${authentification_service_error}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"message\") eq 'reg_success'}">
							<p>${reg_success}</p>
						</c:when>
					</c:choose>
				</form>
			</div>
		</div>
	</div>
</main>
<%@ include file="footer.jsp" %>