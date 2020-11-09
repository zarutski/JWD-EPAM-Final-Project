<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>

<fmt:message key="user.search" var="search_user"/>
<fmt:message key="user.search_title" var="search_title"/>

<fmt:message key="user.full_name" var="full_name"/>
<fmt:message key="user.passport" var="passport"/>
<fmt:message key="user.phone" var="phone"/>
<fmt:message key="user.date_of_birth" var="date_of_birth"/>

<fmt:message key="message.no_users" var="no_users"/>
<fmt:message key="message.wrong_user" var="wrong_user"/>
<fmt:message key="message.service_error" var="service_error"/>

<main class="main">
	<div class="content">
		<%@ include file="sidePanel.jsp" %>
		
		<div class="block">
			<div class="block-column">
				<div class="search">
					<p><b>${search_title}</b></p>

					<form action="controller?command=search_user" method="post">
						<input type="hidden" name="command" value="search_user" />
						<input type="text" name="search_request" value="" required/><br/>
						<input id="submit" type="submit" value="${search_user}"/> 
					</form>
				</div>

				<c:choose>
				    <c:when test="${requestScope.message eq 'no_users'}">
			        	<p><b>${no_users}</b></p>
			    	</c:when>
			    	<c:when test="${requestScope.message eq 'wrong_user'}">
			        	<p><b>${wrong_user}</b></p>
			    	</c:when>
					<c:when test="${requestScope.message eq 'service_error'}">
						<p>${service_error}</p>
					</c:when>
				</c:choose>

				<div id="search" class="history">
					<c:forEach items="${requestScope.users}" var="user">
						<div class="history-sheet">	
							<a href="controller?command=go_to_user_details&user_id=${user.userId}">
								<div class="history-row">
									<div class="col4">${full_name}</div>
									<div class="col3">${passport}</div>
									<div class="col1">${phone}</div>
									<div class="col2">${date_of_birth}</div>
								</div>
								<div class="history-break"></div>
								<div class="history-row">
									<div class="col4">${user.surname} ${user.name} ${user.patronymic}</div>
									<div class="col3">${user.passportSeries} ${user.passportNumber}</div>
									<div class="col1">${user.phoneNumber}</div>
									<div class="col2">${user.dateOfBirth}</div>
								</div>
							</a>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>