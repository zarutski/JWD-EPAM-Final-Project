<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ taglib uri="http://localhost:8080/banktag" prefix="bnk"%>

<fmt:message key="card.number" var="card_number"/>
<fmt:message key="card.owner" var="card_owner"/>
<fmt:message key="card.expiration_date" var="expiration_date"/>
<fmt:message key="card.state" var="card_state"/>

<b>${card_number}</b>
<bnk:card cardNumber="${card.cardNumber}"/><br>
<b>${card_owner}</b> ${card.owner}<br>
<b>${expiration_date}</b>
<bnk:date expirationDate="${card.expirationDate}"/>
<b>${card_state}</b> ${card.state}