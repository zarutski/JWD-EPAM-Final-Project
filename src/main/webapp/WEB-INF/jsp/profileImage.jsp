<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>

<fmt:message key="card.number" var="card_number"/>

<ul>
	<li>
		<c:choose>
			<c:when test="${user_data.photoLink ne null}">
				<img id="img" src="${user_data.photoLink}" class="avatar">
			</c:when>
			<c:otherwise>
				<img src="img\avatar_default.png" class="avatar">
			</c:otherwise>
		</c:choose>
	</li>
</ul>