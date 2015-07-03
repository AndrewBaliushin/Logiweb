<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-inverse navbar-static-top">
	<div class="container-fluid">

		<!-- Logo -->
		<div class="navbar-header">
			<span class="navbar-brand">Manager@LogiWeb</span>
		</div>

		<!-- Menu Items -->
		<div>
			<ul class="nav navbar-nav">
				<li><a href="<c:url value="${param.homeLink}"/>">Home${homeLink}</a></li>
			</ul>
		</div>

	</div>
</nav>