<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>
     
<fmt:message key="info.about_title" var="about_title"/>
<fmt:message key="info.social_culture" var="social_culture"/>
<fmt:message key="info.social_arts" var="social_arts"/>
<fmt:message key="info.social_sport" var="social_sport"/>
<fmt:message key="info.social_assistance" var="social_assistance"/>
<fmt:message key="info.social_trainings" var="social_trainings"/>

<main class="main">

	<div class="content" id="content-about">
		<div class="content-title">${about_title}</div>
		<div class="block">
			<div class="block-column">				
				<img src="img\about-support.jpg" class="about-image">
			</div>
			
			<div class="block-info">
			<p>${social_culture}</p>
			<p>${social_arts}</p>
			<p>${social_sport}</p>		
			<p>${social_assistance}</p>
			<p>${social_trainings}</p>
			</div>
		</div>			
	</div>
</main>

<%@ include file="footer.jsp" %>