<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="main_page_link" var="main_link"/>
<fmt:message key="sign_in_message" var="sign_in"/>
<fmt:message key="sign_up_message" var="sign_up"/>

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

<fmt:message key="form_pattern_phone" var="phone_pattern"/>
<fmt:message key="form_pattern_passport_series" var="passport_series_pattern"/>
<fmt:message key="form_pattern_passport_number" var="passport_number_pattern"/>
<fmt:message key="form_pattern_date" var="date_pattern"/>
<fmt:message key="form_pattern_email" var="email_pattern"/>
<fmt:message key="form_pattern_login" var="login_pattern"/>
<fmt:message key="form_pattern_password" var="password_pattern"/>
    
<fmt:message key="registration_error.11" var="registration_data_exists_error"/>
<fmt:message key="registration_error.12" var="registration_service_error"/>
<fmt:message key="registration_error.13" var="registration_data_format_error"/>
<fmt:message key="registration_error.14" var="user_exists_error"/>
<fmt:message key="registration_message.passwords_no_match" var="passwords_no_match"/>

<main class="main">
	<div class="content">
		<div class="block">
			<nav>
				<ul id="navbar">
					<li><a href="controller?command=go_to_main_page">${main_link}</a></li>
					<li><a href="controller?command=go_to_authentication_page">${sign_in}</a></li>
				</ul>
			</nav>
			<div class="block-column">
				<form action="controller" method="post" accept-charset="utf-8">
					<input type="hidden" name="command" value="registration" />
					${name_message}<br/>
					<input type="text" name="name" value="" required/><br/>
					${surname_message}<br/>
					<input type="text" name="surname" value="" required/><br/>
					${patronymic_message}<br/>
					<input type="text" name="patronymic" value="" required/><br/>
					${phone_number_message}<br/>
					<input type="text" pattern="^\+375([0-9]){7,9}$" title="${phone_pattern}" name="phone_number" value="" placeholder="+375xxxxxxxx" required/><br/>
					${passport_serial_message}<br/>
					<input type="text" pattern="^[a-zA-Z]{2}$" title="${passport_series_pattern}" name="passport_series" value="" required/><br/>
					${passport_number_message}<br/>
					<input type="text" pattern="^\d{7}$" title="${passport_number_pattern}" name="passport_number" value="" required/><br/>
					${date_of_birth_message}<br/>
					<input type="text" pattern="^\d{4}-\d{2}-\d{2}$" title="${date_pattern}" name="date_of_birth" value="" placeholder="1999-12-31" required/><br/>
					${address_message}<br/>
					<input type="text" name="address" value="" required/><br/>
					${post_code_message}<br/>
					<input type="text" name="post_code" value="" required/><br/>
					${email_message}<br/>
					<input type="text" pattern="^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$" title="${email_pattern}" name="email" value="" required/><br/>
					${login_message}<br/>
					<input type="text" pattern="^[a-z0-9_-]{4,16}$" title="${login_pattern}" name="login" value="" required/><br/>
					${password_message}<br/> 
					<input type="password" pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,16}$" title="${password_pattern}" name="password" value="" required/><br />
					${confirm_password_message}<br/> 
					<input type="password" pattern="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\S+$).{8,16}$" title="${password_pattern}" name="confirm_password" value="" required/><br /><br />
		
				
					<c:choose>
						<c:when test="${pageContext.request.getParameter(\"error\") eq 'error_11'}">
							<p>${registration_data_exists_error}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"error\") eq 'error_12'}">
							<p>${registration_service_error}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"error\") eq 'error_13'}">
							<p>${registration_data_format_error}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"error\") eq 'error_14'}">
							<p>${user_exists_error}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"message\") eq 'no_match'}">
							<p>${passwords_no_match}</p>
						</c:when>
					</c:choose>
					 
					<input type="submit" value="${sign_up}"/><br /> 
				</form>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>