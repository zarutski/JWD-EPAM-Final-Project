<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="card.details" var="details"/>
<fmt:message key="card.acc_connected" var="acc_connected"/>
<fmt:message key="card.bttn_block" var="card_block"/>
<fmt:message key="card.bttn_unblock" var="card_unblock"/>
<fmt:message key="no_operations" var="no_operations"/>

<fmt:message key="card.card_block_confirm" var="card_block_confirm"/>
<fmt:message key="card.card_unblock_confirm" var="card_unblock_confirm"/>

<fmt:message key="message.service_error" var="service_error"/>
<fmt:message key="message.wrong_data" var="wrong_data"/>

<main class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>
		
		<div class="block">
			<div class="block-column">
				<b>${details}</b>
				<div class="sheet">
						<%@ include file="cardSheet.jsp" %>
				</div>
				
				<div class="inline">
					<c:choose>
						<c:when test="${card.state eq 'заблокирована'}">
							<form action="#" method="post">
								<input type="submit" value="${card_block}" disabled/> 
							</form>
							<c:if test="${sessionScope.authentication_data.userRole eq 'admin'}">
								<form action="controller" method="post" onsubmit="return confirm('${card_unblock_confirm}');">
									<input type="hidden" name="command" value="unblock_card" />
									<input type="hidden" name="card_id" value="${card.cardId}" />
									<input type="hidden" name="user_id" value="${param.user_id}" />
									<input type="hidden" name="card_state" value="1" />
									<input type="submit" value="${card_unblock}"/> 
								</form>
							</c:if>
						</c:when>
						<c:otherwise>
							<form action="controller" method="post" onsubmit="return confirm('${card_block_confirm}');">
								<input type="hidden" name="command" value="block_card" />
								<input type="hidden" name="card_id" value="${card.cardId}" />
								<input type="hidden" name="user_id" value="${param.user_id}" />
								<input type="hidden" name="card_state" value="2" />
								<input type="submit" value="${card_block}"/> 
							</form>
							<c:if test="${sessionScope.authentication_data.userRole eq 'admin'}">
								<form action="#" method="post">
									<input type="submit" value="${card_unblock}" disabled/> 
								</form>
							</c:if>
						</c:otherwise>
					</c:choose>
				</div>
				
				<c:choose>
						<c:when test="${pageContext.request.getParameter(\"message\") eq 'wrong_data'}">
							<p><b>${wrong_data}</b></p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"message\") eq 'service_error'}">
							<p>${service_error}</p>
						</c:when>
				</c:choose>

				<div class="break"></div>
				${acc_connected}
				<c:forEach items="${requestScope.card_accounts}" var="account">
					<div class="sheet">
						<%@ include file="accSheet.jsp" %>
					</div>
				</c:forEach>

			</div>
			<div class="block-column">					
				<%@ include file="operationHistory.jsp" %>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>