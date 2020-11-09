<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="payment.acc_transfer" var="acc_transfer"/>
<fmt:message key="payment.card_transfer" var="card_transfer"/>

<main class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>
		
		<div class="block">
			<div id="pay" class="block-column">
				<div class="payment">
					<div class="payment-sheet">
						<a href="controller?command=go_to_accounts&action=payment">${acc_transfer}</a>
					</div>
					<div class="payment-sheet">	
						<a href="controller?command=go_to_cards&action=payment">${card_transfer}</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>