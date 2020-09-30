<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<body>
	<c:if test="${sessionScope.authentication_data != null}">
		<c:redirect url="controller?command=go_to_personal_area"></c:redirect>
	</c:if>
	<c:redirect url="controller?command=go_to_main_page"></c:redirect>	
</body>
</html>