<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>

<fmt:message key="app_home_page" var="home"/>
<fmt:message key="app_about_us" var="about"/>
<fmt:message key="app_contacts" var="contacts"/>

		<footer class="footer">
			<div class="container-foot">
				<div class="nav">
					<ul class="menu">
						<li>
							<a href="controller?command=go_to_main_page">${home}</a>
						</li>
						<li>
							<a href="controller?command=go_to_about_us_page">
								${about}
							</a>
						</li>
						<li>
							<a href="controller?command=go_to_contacts_page">
								${contacts}
							</a>
						</li>
					</ul>
				</div>
			</div>
		</footer>
	</body>
</html>