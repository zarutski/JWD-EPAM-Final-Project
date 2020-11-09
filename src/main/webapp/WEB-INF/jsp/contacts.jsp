<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="header.jsp" %>
     
<fmt:message key="info.contacts_licenses" var="contacts_licenses"/>
<fmt:message key="info.contacts_licenses_national_bank" var="contacts_licenses_national_bank"/>
<fmt:message key="info.contacts_licenses_ministry" var="contacts_licenses_ministry"/>
<fmt:message key="info.bank_address" var="bank_address"/>
<fmt:message key="info.bank_tax_number" var="bank_tax_number"/>
<fmt:message key="info.bank_swift" var="bank_swift"/>
<fmt:message key="info.bank_fax" var="bank_fax"/>
<fmt:message key="info.bank_phone" var="bank_phone"/>
<fmt:message key="info.bank_email" var="bank_email"/>
     
<main class="main">
	<div class="content" id="content-about">
		<div class="block-info" id="contacts-licence">
			<p><b>${contacts_licenses}</b></p>
			<p>
			${contacts_licenses_national_bank}
			</p>
			<p>
			${contacts_licenses_ministry}
		</div>
	
		<div class="block-info" id="contacts">
			<p>
			${bank_address}
			</p>
			<p>
			${bank_tax_number}
			</p>
			<p>
			${bank_swift}
			</p>
			<p>
			${bank_fax}
			</p>
			<p>
			${bank_phone}
			</p>
			<p>
			${bank_email} 
			</p>
		</div>
	</div>
</main>

<%@ include file="footer.jsp" %>