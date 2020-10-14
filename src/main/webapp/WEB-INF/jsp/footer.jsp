<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>


<fmt:message key="app_home_page" var="home"/>
<fmt:message key="app_about_us" var="about"/>
<fmt:message key="app_contacts" var="contacts"/>


		<footer class="footer">
			<div class="container">
					<div class="nav">
						<ul class="menu">
							<li>
								<a href="controller?command=go_to_main_page">${home}</a>
							</li>
							<li>
								<a href="#">
									${about}
								</a>
							</li>
							<li>
								<a href="#">
									${contacts}
								</a>
							</li>
						</ul>
					</div>
				</div>
		</footer>
	</body>
</html>