<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="personal_area.user_info" var="personal_area_user_info"/>
<fmt:message key="personal_area.user_data_info" var="personal_area_user_data_info"/>

<fmt:message key="personal.phone_number" var="personal_phone_number"/>
<fmt:message key="personal.passport" var="personal_passport"/>
<fmt:message key="personal.date_of_birth" var="personal_date_of_birth"/>
<fmt:message key="personal.address" var="personal_address"/>
<fmt:message key="personal.post_code" var="personal_post_code"/>
<fmt:message key="personal.load_photo" var="load_photo"/>

<fmt:message key="card.cards_title" var="cards_title"/>
<fmt:message key="acc.accounts_title" var="accounts_title"/>

<fmt:message key="message.wrong_user" var="wrong_user"/>
<fmt:message key="message.no_cards" var="no_cards"/>
<fmt:message key="message.service_error" var="service_error"/>
<fmt:message key="user.wrong_data" var="wrong_data"/>

<fmt:message key="user.give_rights_confirm" var="give_rights_confirm"/>
<fmt:message key="user.remove_rights_confirm" var="remove_rights_confirm"/>
<fmt:message key="user.give_rights" var="give_rights"/>
<fmt:message key="user.remove_rights" var="remove_rights"/>


<div class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>

		<div class="block">
			<c:choose>
				<c:when test="${requestScope.message eq 'service_error'}">
					<div class="block-column">
						<p>${service_error}</p>
					</div>
				</c:when>
				
				<c:otherwise>
					<div class="block-column" id="profile-image">
						<%@ include file="profileImage.jsp" %>
					</div>
						
					<div class="block-column">
						<c:choose>
							<c:when test="${requestScope.card_message eq 'wrong_user'}">
								<br><b>${wrong_user}</b>
							</c:when>
							<c:otherwise>
								<b>${personal_area_user_info}</b>	
								<c:out value="${user_data.surname}" />
								<c:out value="${user_data.name}" />
								<c:out value="${user_data.patronymic}" />						
								<div class="break"></div>
				
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
		
								<div class="inline">
									<c:if test="${sessionScope.authentication_data.userRole eq 'admin'}">
										<c:choose>
											<c:when test="${user_data.roleName eq 'user'}">
												<form action="controller" method="post" onsubmit="return confirm('${give_rights_confirm} ${user_data.surname} ${user_data.name}?');">
													<input type="hidden" name="command" value="change_user_role" />
													<input type="hidden" name="user_id" value="${user_data.userId}" />
													<input type="hidden" name="user_role" value="2" />
													<input type="submit" value="${give_rights}"/> 
												</form>
											</c:when>
											<c:otherwise>
												<c:if test="${sessionScope.authentication_data.userId ne user_data.userId}">
													<form action="controller" method="post" onsubmit="return confirm('${remove_rights_confirm} ${user_data.surname} ${user_data.name}?');">
														<input type="hidden" name="command" value="change_user_role" />
														<input type="hidden" name="user_id" value="${user_data.userId}" />
														<input type="hidden" name="user_role" value="1" />
														<input type="submit" value="${remove_rights}"/> 
													</form>
												</c:if>
											</c:otherwise>
										</c:choose>
									</c:if>
								</div>
								<c:choose>
									<c:when test="${param.message eq 'wrong_data'}">
										<p><b>${wrong_data}</b></p>
									</c:when>
								</c:choose>
							</c:otherwise>
						</c:choose>	
						
					</div>
		
					<div class="block-column">
						<b>${cards_title}</b>
						<c:forEach items="${requestScope.user_cards}" var="card">
							<div class="sheet">
								<a href="controller?command=go_to_card_details&card_id=${card.cardId}&user_id=${user_data.userId}"> 
								<%@ include file="cardSheet.jsp" %>
								</a>
							</div>
						</c:forEach>
						<c:choose>
							<c:when test="${requestScope.card_message eq 'no_cards'}">
						       	<br><b>${no_cards}</b>
						    </c:when>
						</c:choose>
					</div>
		
					<div class="block-column">
						<b>${accounts_title}</b>
						<c:forEach items="${requestScope.user_accounts}" var="account">
							<div class="sheet">
								<a href="controller?command=go_to_account_details&account_id=${account.accountId}&user_id=${user_data.userId}">	
								<%@ include file="accSheet.jsp" %>
								</a>
							</div>
						</c:forEach>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>

<%@ include file="footer.jsp" %>