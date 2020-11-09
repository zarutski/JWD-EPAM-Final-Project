<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>
     
<fmt:message key="sign_in_message" var="sign_in"/>
<fmt:message key="sign_up_message" var="sign_up"/>	     
     
<main class="main">
	<div class="content">
		<div class="block">
			<nav>
				<ul id="navbar">
					<li><a href="controller?command=go_to_authentication_page">${sign_in}</a></li>
					<li><a href="controller?command=go_to_registration_page">${sign_up}</a></li>
				</ul>
				<br/>
			</nav>
				
			<div class="block-column" id="about">
				<%@ include file="about.jsp" %>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>