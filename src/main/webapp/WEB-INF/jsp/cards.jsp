<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="personal_area.link" var="personal_link"/>
<fmt:message key="personal_area.cards_link" var="cards_link"/>
<fmt:message key="personal_area.accounts_link" var="accounts_link"/>
<fmt:message key="personal_area.payments_link" var="payments_link"/>
<fmt:message key="personal_area.search_user_link" var="search_user_link"/>
<fmt:message key="button.logout_message" var="logout_message"/>


<fmt:message key="card.number" var="card_number"/>
<fmt:message key="card.owner" var="card_owner"/>
<fmt:message key="card.expiration_date" var="expiration_date"/>
<fmt:message key="card.state" var="card_state"/>
<fmt:message key="card.order_new" var="new_card_order"/>

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
				
				<c:forEach items="${requestScope.user_cards}" var="card">
					<div class="card">
						<!-- добавить переход на историю операций -->

						<b>${card_number}</b> ${card.cardNumber}<br>
						<b>${card_owner}</b> ${card.owner}<br>
						<b>${expiration_date}</b> ${card.expirationDate}<br>
						<b>${card_state}</b> ${card.state}
					</div>
				</c:forEach>


				<li>
					${new_card_order}
				</li>
				<%-- <form action="controller" method="post">
					<input type="hidden" name="command" value="card_order" /><br/>
					<input type="submit" value="${card_order_message}"/><br /> 
				</form> --%>
			</div>
			<div class="block-column">	
				<li>

				</li>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>