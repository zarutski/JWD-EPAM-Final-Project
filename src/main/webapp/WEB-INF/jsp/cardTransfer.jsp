<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib uri="http://localhost:8080/banktag" prefix="bnk"%>
<%@ include file="header.jsp" %>

<fmt:message key="card.number" var="card_number"/>
<fmt:message key="acc.amount" var="acc_amount"/>

<fmt:message key="payment.card_transfer_title" var="card_transfer_title"/>
<fmt:message key="payment.currency_select" var="currency_select"/>
<fmt:message key="payment.destination_card" var="destination_card"/>
<fmt:message key="payment.confirmation_code" var="confirmation_code"/>

<fmt:message key="payment.transfer_amount" var="transfer_amount"/>
<fmt:message key="payment.commit_transfer" var="commit_transfer"/>
<fmt:message key="payment.amount_pattern" var="amount_pattern"/>
<fmt:message key="payment.card_pattern" var="card_pattern"/>

<fmt:message key="payment_message.successful" var="successful"/>
<fmt:message key="payment_message.match_currency" var="match_currency"/>
<fmt:message key="payment_message.input_data" var="input_data"/>
<fmt:message key="payment_message.insufficient_funds" var="insufficient_funds"/>
<fmt:message key="message.service_error" var="service_error"/>

<main class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>
		
		<div class="block">
			<div class="block-column">			
				<div class="search">
					<div id="details">
						<p><b>${card_transfer_title}</b></p>
						<b>${card_number}</b><br>
						<bnk:card cardNumber="${card.cardNumber}"/><br>
						<b>${acc_amount}</b><br>
						<c:if test="${param.selected ne null}">					
							<c:forEach items="${requestScope.card_accounts}" var="account">
								<c:if test="${param.selected eq account.accountId}">
									${account.amount/100} ${account.currency}
								</c:if>
							</c:forEach>
						</c:if>
					</div>
	
					<form action="controller" method="post">
						<div class="break"></div>
						<input type="hidden" name="command" value="transfer" />
						<input type="hidden" name="transfer_from" value="card" />
						<input type="hidden" name="card_id" value="${card.cardId}" />
						<input type="hidden" name="card_number" value="${card.cardNumber}" />	
						
						<b>${currency_select}</b>
						<c:forEach items="${requestScope.card_accounts}" var="account">
							<div id="currency" class="sheet">
								<a id="element" href="controller?command=go_to_card_details&selected=${account.accountId}&action=payment&card_id=${card.cardId}">
									${account.currency}
								</a>
							</div>
						</c:forEach>

						<c:if test="${param.selected ne null}">			
													
							<div id="transfer-title"><b>${destination_card}</b></div>
							<input type="text" 
								name="destination_number" 
								pattern="^[0-9]{16}$"
								title="${card_pattern}" 
								placeholder="0000111122223333" 
								value="" 
								required/><br/>
							<c:forEach items="${requestScope.card_accounts}" var="account">
								<c:if test="${param.selected eq account.accountId}">
									<div id="transfer-title"><b>${transfer_amount}</b></div>
									<input type="number" 
										name="transfer_amount" 
										pattern="^[0-9]+(\.[0-9]{1,2})?$" 
										title="${amount_pattern}"
										min="1" 
										max="${account.amount/100}" 
										step="0.01" 
										value="" 
										required/>
										
										<input type="hidden" name="account_id" value="${account.accountId}" />
										<input type="hidden" name="account_amount" value="${account.amount/100}" />
										<input type="hidden" name="account_number" value="${account.accNumber}" />
										<input type="hidden" name="payment_currency" value="${account.currency}" />
								</c:if>
							</c:forEach>
							<div id="transfer-title"><b>${confirmation_code}</b></div>
							<input type="text" 
								name="confirmation_code"  
								value="" 
								required/><br/>
							<input id="submit" type="submit" value="${commit_transfer}"/> 
						</c:if>			
					</form>

					
					<c:choose>
						<c:when test="${pageContext.request.getParameter(\"message\") eq 'successful'}">
							<p>${successful}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"message\") eq 'match_currency'}">
							<p>${match_currency}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"message\") eq 'input_data'}">
							<p>${input_data}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"message\") eq 'insufficient_funds'}">
							<p>${insufficient_funds}</p>
						</c:when>
						<c:when test="${pageContext.request.getParameter(\"message\") eq 'service_error'}">
							<p>${service_error}</p>
						</c:when>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>