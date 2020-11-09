<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="acc.details" var="details"/>

<main class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>
		<c:choose>
			<c:when test="${requestScope.message eq 'service_error'}">
				<div class="block-column">
					<p>${service_error}</p>
				</div>
			</c:when>
			<c:otherwise>
				<div class="block">
					<div class="block-column">
						<b>${details}</b>
						<div class="sheet">		
							<%@ include file="accSheet.jsp" %>
						</div>
					</div>
					<div class="block-column">	
						<%@ include file="operationHistory.jsp" %>
					</div>
				</div>
			</c:otherwise>
		</c:choose>		
	</div>
</main>

<%@ include file="footer.jsp" %>