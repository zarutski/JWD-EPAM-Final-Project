<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="personal_area.link" var="personal_link"/>
<fmt:message key="personal_area.cards_link" var="cards_link"/>
<fmt:message key="personal_area.accounts_link" var="accounts_link"/>
<fmt:message key="personal_area.payments_link" var="payments_link"/>
<fmt:message key="personal_area.search_user_link" var="search_user_link"/>
<fmt:message key="button.logout_message" var="logout_message"/>


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
					платеж 1 --- ссылка
				</li>
				<li>
					платёж 2 --- ссылка
				</li>

			</div>
			<div class="block-column">	
				<li>

				</li>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>