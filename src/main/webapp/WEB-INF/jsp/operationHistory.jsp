<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>

<fmt:message key="history.title" var="title"/>

<fmt:message key="history.operation_time" var="operation_time"/>
<fmt:message key="history.operation_type" var="operation_type"/>
<fmt:message key="history.status" var="status"/>
<fmt:message key="history.amount" var="amount"/>
<fmt:message key="history.destination_account" var="destination_account"/>
<fmt:message key="history.allowed" var="allowed"/>
<fmt:message key="history.rejected" var="rejected"/>
<fmt:message key="message.no_operations" var="no_operations"/>

<div class="history">
	<div class="history-title"><b>${title}</b></div>
	<c:choose>
		<c:when test="${requestScope.message eq 'no_operations'}">
		   	<br><b>${no_operations}</b>
	    </c:when>
	</c:choose>	
	
	<c:forEach items="${requestScope.operations_history}" var="history">
		<div class="history-sheet">	
		
			<b>${operation_time}</b>
			<i><fmt:formatDate value="${history.date}" pattern="yyyy-MM-dd HH:mm:ss" /></i>
			<div class="history-break"></div>
			
			<div class="history-row">
				<div class="col1">${operation_type}</div>
				<div class="col2">${status}</div>
				<c:choose>
					<c:when test="${history.detail ne null}">
						<div class="col3">${amount}</div>
							<c:if test="${not empty history.detail.destinationAccount}">
    							<div class="col4">${destination_account}</div>
							</c:if>
					</c:when>
				</c:choose>
			</div>
			<div class="history-break"></div>
			
			<div class="history-row">
				<div class="col1">${history.operationName}</div>
				<div class="col2">
					<c:choose>
						<c:when test="${history.allowed}">
							${allowed}
						</c:when>
						<c:otherwise>
							<i>${rejected}</i>
						</c:otherwise>
					</c:choose>							
				</div>
				<c:choose>
					<c:when test="${history.detail ne null}">
						<div class="col3">${history.detail.amount/100} ${history.detail.currency}</div>
						<c:if test="${not empty history.detail.destinationAccount}">
	    					<div class="col4">${history.detail.destinationAccount}</div>
						</c:if>
					</c:when>
				</c:choose>
			</div>
		</div>
	</c:forEach>
</div>