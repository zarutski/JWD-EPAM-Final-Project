<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="personal_area.title" var="personal_area_title"/>
<fmt:message key="personal_area.user_info" var="personal_area_user_info"/>
<fmt:message key="personal_area.user_data_info" var="personal_area_user_data_info"/>

<fmt:message key="personal.phone_number" var="personal_phone_number"/>
<fmt:message key="personal.passport" var="personal_passport"/>
<fmt:message key="personal.date_of_birth" var="personal_date_of_birth"/>
<fmt:message key="personal.address" var="personal_address"/>
<fmt:message key="personal.post_code" var="personal_post_code"/>

<fmt:message key="form_name" var="name_message"/>
<fmt:message key="form_surname" var="surname_message"/>
<fmt:message key="form_patronymic" var="patronymic_message"/>
<fmt:message key="form_phone_number" var="phone_number_message"/>
<fmt:message key="form_passport_serial" var="passport_serial_message"/>
<fmt:message key="form_passport_number" var="passport_number_message"/>
<fmt:message key="form_date_of_birth" var="date_of_birth_message"/>
<fmt:message key="form_address" var="address_message"/>
<fmt:message key="form_post_code" var="post_code_message"/>

<fmt:message key="form_pattern_phone" var="phone_pattern"/>
<fmt:message key="form_pattern_passport_series" var="passport_series_pattern"/>
<fmt:message key="form_pattern_passport_number" var="passport_number_pattern"/>
<fmt:message key="form_pattern_date" var="date_pattern"/>

<fmt:message key="personal.load_photo" var="load_photo"/>
<fmt:message key="personal.save_button" var="save_button"/>
<fmt:message key="personal.change_button" var="change_button"/>
<fmt:message key="personal.cancel_button" var="cancel_button"/>

<fmt:message key="message.input_data" var="input_data"/>
<fmt:message key="message.service_error" var="service_error"/>

<fmt:message key="photo_message.wrong_extension" var="wrong_extension"/>
<fmt:message key="photo_message.upload_error" var="upload_error"/>
<fmt:message key="photo_message.only_file" var="only_file"/>

<main class="main">
	<div class="content">
		
		<%@ include file="sidePanel.jsp" %>
	
		<div class="block">
			<div class="block-column" id="profile-image" >
				
				<%@ include file="profileImage.jsp" %>
			
				<form action="controller?command=user_photo_upload" method="post" enctype="multipart/form-data">
					<input type="hidden" name="command" value="user_photo_upload" />
					<input type="file" name="file" />
					<input type="submit" value="${load_photo}"/>
				</form>  
				
				<c:choose>
					<c:when test="${pageContext.request.getParameter(\"photo_message\") eq 'wrong_extension'}">
						<p>${wrong_extension}</p>
					</c:when>
					<c:when test="${pageContext.request.getParameter(\"photo_message\") eq 'service_error'}">
						<p>${service_error}</p>
					</c:when>
					<c:when test="${pageContext.request.getParameter(\"photo_message\") eq 'upload_error'}">
						<p>${upload_error}</p>
					</c:when>
					<c:when test="${pageContext.request.getParameter(\"photo_message\") eq 'only_file'}">
						<p>${only_file}</p>
					</c:when>
				</c:choose>
			</div>

			<c:choose>
				<c:when test="${param.user_action eq 'update_user_data'}">
					<div class="block-column">
						<form action="controller" method="post" accept-charset="utf-8">
						
							<input type="hidden" name="command" value="update_user_data" />
							<input type="hidden" name="user_id" value="${user_data.userId}" />
							${name_message}<br/>
							<input type="text" name="name" value="${user_data.name}" required/><br/>
							${surname_message}<br/>
							<input type="text" name="surname" value="${user_data.surname}" required/><br/>
							${patronymic_message}<br/>
							<input type="text" name="patronymic" value="${user_data.patronymic}" required/>
							<div class="break"></div>
							${phone_number_message}<br/>
							<input type="text" pattern="^\+375([0-9]){7,9}$" title="${phone_pattern}" name="phone_number" value="${user_data.phoneNumber}" required/><br/>
							${address_message}<br/>
							<input type="text" name="address" value="${user_data.address}" required/><br/>
							${post_code_message}<br/>
							<input type="text" name="post_code" value="${user_data.postCode}" required/><br/>
							${email_message}<br/>
							
							<input type="submit" value="${save_button}"/><br />
						</form>
						
						<form action="controller?" method="get">
							<input type="hidden" name="command" value="go_to_personal_area" />
							<input type="submit" value="${cancel_button}"/> 
						</form>
					</div>
				</c:when>
				
				<c:otherwise>
					<div class="block-column">	
						<b>${personal_area_user_info}</b>	
						<c:out value="${user_data.surname}" />
						<c:out value="${user_data.name}" />
						<c:out value="${user_data.patronymic}" />
						
						<div class="break"></div>
						
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

						<form action="controller?" method="get">
							<input type="hidden" name="command" value="go_to_personal_area" />
							<input type="hidden" name="user_action" value="update_user_data" />
							<input type="submit" value="${change_button}"/> 
						</form>

						<c:choose>
							<c:when test="${pageContext.request.getParameter(\"message\") eq 'input_data'}">
								<p>${input_data}</p>
							</c:when>
							<c:when test="${pageContext.request.getParameter(\"message\") eq 'service_error'}">
								<p>${service_error}</p>
							</c:when>
						</c:choose>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</main>
 
<%@ include file="footer.jsp" %>