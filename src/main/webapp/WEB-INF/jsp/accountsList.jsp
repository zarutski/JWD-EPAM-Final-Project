<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="payment.account_choose" var="account_choose"/>
<fmt:message key="message.no_accounts" var="no_accounts"/>

<main class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>
		
		<div class="block">
			<div class="block-column">			
			
				<div id="pay-title" class="payment"><b>${account_choose}</b></div>	
				<c:choose>
					<c:when test="${requestScope.message eq 'no_accounts'}">
				       	<br><b>${no_accounts}</b>
				    </c:when>
				</c:choose>	
				
				<div class="payment">
					<c:forEach items="${requestScope.user_accounts}" var="account">
						<div class="payment-sheet">	
							<a id="element" href="controller?command=go_to_account_details&action=payment&account_id=${account.accountId}">
								<%@ include file="accSheet.jsp" %>
							</a>
						</div>
					</c:forEach>

				</div>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>