<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>

<fmt:message key="acc.number" var="acc_number"/>
<fmt:message key="acc.opening_date" var="opening_date"/>
<fmt:message key="acc.amount" var="acc_amount"/>
<fmt:message key="acc.state" var="state"/>

<b>${acc_number}</b><br> 
${account.accNumber}<br>
<b>${opening_date}</b><br>
${account.openingDate}<br>
<b>${acc_amount}</b><br>
${account.amount/100} ${account.currency}<br>
<b>${state}</b><br>
${account.state}