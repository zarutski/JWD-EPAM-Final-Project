<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="personal_area.link" var="personal_link"/>
<fmt:message key="personal_area.cards_link" var="cards_link"/>
<fmt:message key="personal_area.accounts_link" var="accounts_link"/>
<fmt:message key="personal_area.payments_link" var="payments_link"/>
<fmt:message key="personal_area.search_user_link" var="search_user_link"/>
<fmt:message key="button.logout_message" var="logout_message"/>


<fmt:message key="acc.number" var="card_number"/>
<fmt:message key="acc.opening_date" var="opening_date"/>
<fmt:message key="acc.amount" var="acc_amount"/>
<fmt:message key="acc.state" var="state"/>
<fmt:message key="acc.open_new" var="open_new"/>


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
				

				<c:forEach items="${requestScope.user_accounts}" var="account">
					<div class="card">
						<!-- добавить переход на историю операций -->	
						<b>${card_number}</b><br> 
						${account.accNumber}<br>
						<b>${opening_date}</b><br>
						${account.openingDate}<br>
						<b>${acc_amount}</b><br>
						${account.amount/100} ${account.currency}<br>
						<b>${state}</b><br>
						${account.state}
					</div>
				</c:forEach>

				<li>
					${open_new}
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