<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="card_operation.order_title" var="order_title"/>
<fmt:message key="card_operation.order_message" var="order_message"/>
<fmt:message key="card_operation.owner_name" var="owner_name"/>
<fmt:message key="card_operation.systems_select" var="systems_select"/>
<fmt:message key="card_operation.currency_select" var="currency_select"/>
<fmt:message key="card_operation.validity_select" var="validity_select"/>
<fmt:message key="card_operation.owner_pattern" var="owner_pattern"/>
<fmt:message key="card_operation.order" var="order"/>

<fmt:message key="card_operation_message.success" var="success"/>
<fmt:message key="card_operation_message.data_error" var="data_error"/>
<fmt:message key="message.service_error" var="service_error"/>

<main class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>
		
		<div class="block">
			<div class="block-column">			
				<div class="search">
				
					<div id="details">
						<p><b>${order_title}</b></p>
						<p>${order_message}</p>
					</div>
					<form action="controller" method="post">
						<input type="hidden" name="command" value="order_card" />

						<div id="transfer-title"><b>${owner_name}</b></div>
						<input type="text" 
							name="owner" 
							pattern="^([A-Za-z]-*){2,}\s([A-Za-z]-*){2,}$"
							title="${owner_pattern}"  
							value="" required/>
						<div class="break"></div>
						
						<div id="transfer-title"><b>${systems_select}</b></div>
						<select name="payment_system" required>
							<option disabled>payment system</option>
  							<option>VISA</option>
  							<option>MASTERCARD</option>
						</select>
						<div class="break"></div>
						
						<div id="transfer-title"><b>${currency_select}</b></div>
						<select name="currency" required>
							<option disabled>currency</option>
  							<option value=1>BYN</option>
  							<option value=2>USD</option>
						</select>
						<div class="break"></div>
						
						<div id="transfer-title"><b>${validity_select}</b></div>
						<select name="validity" required>
							<option disabled>validity</option>
  							<option>12</option>
  							<option>24</option>
  							<option>36</option>
						</select>
						<div class="break"></div>

						<input id="submit" type="submit" value="${order}"/>
						
						
						<c:choose>
							<c:when test="${pageContext.request.getParameter(\"message\") eq 'success'}">
								<p>${success}</p>
							</c:when>
							<c:when test="${pageContext.request.getParameter(\"message\") eq 'data_error'}">
								<p>${data_error}</p>
							</c:when>
							<c:when test="${pageContext.request.getParameter(\"message\") eq 'service_error'}">
								<p>${service_error}</p>
							</c:when>
						</c:choose>
					</form>
				</div>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>