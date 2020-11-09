<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="card.cards_title" var="cards_title"/>
<fmt:message key="card.order_new" var="new_card_order"/>
<fmt:message key="message.no_cards" var="no_cards"/>

<main class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>
		
		<div class="block">
			<div class="block-column">
			
				<b>${cards_title}</b>
				
				<c:forEach items="${requestScope.user_cards}" var="card">
					<div class="sheet">
						<a href="controller?command=go_to_card_details&card_id=${card.cardId}"> 
							<%@ include file="cardSheet.jsp" %>
						</a>					
					</div>
				</c:forEach>
				<c:choose>
					<c:when test="${requestScope.message eq 'no_cards'}">
				       	<br><b>${no_cards}</b>
				    </c:when>
				</c:choose>

				<form action="controller?" method="get">
					<input type="hidden" name="command" value="go_to_card_order" />
					<input type="submit" value="${new_card_order}"/> 
				</form>

			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>