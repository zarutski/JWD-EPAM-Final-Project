<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="acc.number" var="acc_number"/>
<fmt:message key="acc.amount" var="acc_amount"/>

<fmt:message key="payment.acc_transfer_title" var="acc_transfer_title"/>
<fmt:message key="payment.transfer_amount" var="transfer_amount"/>
<fmt:message key="payment.destination_acc" var="destination_acc"/>
<fmt:message key="payment.commit_transfer" var="commit_transfer"/>

<fmt:message key="payment.amount_pattern" var="amount_pattern"/>
<fmt:message key="payment.account_pattern" var="account_pattern"/>

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
						<p><b>${acc_transfer_title}</b></p>
						<b>${acc_number}</b><br> 
						${account.accNumber}<br>
						<b>${acc_amount}</b><br>
						${account.amount/100} ${account.currency}<br>
					</div>
					
					<form action="controller" method="post">
						<div class="break"></div>
						<input type="hidden" name="command" value="transfer" />

						<input type="hidden" name="account_id" value="${account.accountId}" />
						<input type="hidden" name="account_number" value="${account.accNumber}" />
						<input type="hidden" name="account_amount" value="${account.amount/100}" />
						<input type="hidden" name="payment_currency" value="${account.currency}" />
						<input type="hidden" name="transfer_from" value="acc" />

						<div id="transfer-title"><b>${destination_acc}</b></div>
						<input type="text" 
							name="destination_number" 
							pattern="^([a-zA-z]|[0-9]){24}$"
							title="${account_pattern}" 
							placeholder="BY00UNBS0000111122223333" 
							value="" 
							required/><br/>
							
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
							
						<input id="submit" type="submit" value="${commit_transfer}"/> 
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