<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="personal_area.link" var="personal_link"/>
<fmt:message key="personal_area.cards_link" var="cards_link"/>
<fmt:message key="personal_area.accounts_link" var="accounts_link"/>
<fmt:message key="personal_area.payments_link" var="payments_link"/>
<fmt:message key="personal_area.search_user_link" var="search_user_link"/>
<fmt:message key="button.logout_message" var="logout_message"/>


<fmt:message key="personal_area.title" var="personal_area_title"/>
<fmt:message key="personal_area.user_info" var="personal_area_user_info"/>
<fmt:message key="personal_area.user_data_info" var="personal_area_user_data_info"/>

<fmt:message key="personal.phone_number" var="personal_phone_number"/>
<fmt:message key="personal.passport" var="personal_passport"/>
<fmt:message key="personal.date_of_birth" var="personal_date_of_birth"/>
<fmt:message key="personal.address" var="personal_address"/>
<fmt:message key="personal.post_code" var="personal_post_code"/>

<fmt:message key="personal.load_photo" var="load_photo"/>

<main class="main">
	<div class="content">
		<div class="side-panel">
			
			<li>
				<a href="controller?command=go_to_personal_area">${personal_link}</a>
			</li>
			
			<c:choose>
				<c:when test="${sessionScope.authentication_data.userRole eq 'user'}">
					<li>
						<a href="controller?command=go_to_cards">${cards_link}</a>
					</li>
					<li>
						<a href="controller?command=go_to_accounts">${accounts_link}</a>
					<li>
						<a href="controller?command=go_to_payments">${payments_link}</a>
					</li>
				</c:when>
				<c:otherwise>
					<li>
						<a href="controller?command=go_to_search_user">${search_user_link}</a>
					</li>
				</c:otherwise>
			</c:choose>
			

			<div class="logout" >
				<form action="controller" method="post">
					<input type="hidden" name="command" value="logout" /><br/>
					<input type="submit" value="${logout_message}"/><br /> 
				</form>
			</div>		
		</div>
		
		<div class="block">
			<div class="block-column">
				<li>
					PHOTO
				</li>
				<li>
					${load_photo}
				</li>
			</div>
			<div class="block-column">	
				<li>
					${personal_area_user_info}	
					<c:out value="${user_data.surname}" />
					<c:out value="${user_data.name}" />
					<c:out value="${user_data.patronymic}" />
					(<c:out value="${user_data.roleName}" />)
					
					<!-- <div class="break"></div> -->
					<br/><br/>
					<b>${personal_area_user_data_info}</b>
					<br/>
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
				</li>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>