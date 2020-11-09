<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="payment.card_choose" var="card_choose"/>
<fmt:message key="payment.card_blocked" var="card_blocked"/>
<fmt:message key="payment.card_choose" var="card_choose"/>
<fmt:message key="payment.card_blocked" var="card_blocked"/>

<fmt:message key="message.no_cards" var="no_cards"/>

<main class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>
		
		<div class="block">
			<div class="block-column">			
			
				<div id="pay-title" class="payment"><b>${card_choose}</b></div>	
				<c:choose>
					<c:when test="${requestScope.message eq 'no_cards'}">
				       	<br><b>${no_cards}</b>
				    </c:when>
				</c:choose>
				
				<div class="payment">
					<c:forEach items="${requestScope.user_cards}" var="card">
						<div class="payment-sheet">	
							<c:choose>
								<c:when test="${card.state eq 'заблокирована'}">
									<div class=unavailable data-title="${card_blocked}">
										<%@ include file="cardSheet.jsp" %>
									</div>
								</c:when>
								<c:otherwise>
									<a id="element" href="controller?command=go_to_card_details&action=payment&card_id=${card.cardId}">
										<%@ include file="cardSheet.jsp" %>
									</a>
								</c:otherwise>
							</c:choose>		
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>