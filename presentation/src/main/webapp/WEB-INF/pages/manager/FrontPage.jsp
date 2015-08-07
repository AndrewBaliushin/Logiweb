<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
    <jsp:param name="title" value="Manager front page" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
    <jsp:param name="homeLink" value="/" />
    <jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<div class="container">
	<div class="row">
	
		<!-- Drivers -->
		<div class="col-md-6">
			<div class="well">
				<legend class="the-legend">
					Drivers <span class="glyphicon glyphicon-user" aria-hidden="true"></span>
				</legend>
	
	            <div class="btn-group btn-group-justified" role="group"> 
	            
					<a href="${pageContext.request.contextPath}/driver"
						role="button" class="btn btn-default btn-large btn-block"><span
						class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a>
		
					<a href="${pageContext.request.contextPath}/driver/new"
						role="button" class="btn btn-default btn-large btn-block"><span
						class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</a>
				
				</div>
	
			</div>
		</div>
		<!-- /Drivers -->
	
		<!-- Truck -->
		<div class="col-md-6">
			<div class="well">
				<legend class="the-legend">
					Trucks <span class="glyphicon glyphicon-bed" aria-hidden="true"></span>
				</legend>
	
	            <div class="btn-group btn-group-justified" role="group"> 
	            
					<a href="${pageContext.request.contextPath}/truck"
						role="button" class="btn btn-default btn-large"><span
						class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a>
		
					<a href="${pageContext.request.contextPath}/truck/new"
						role="button" class="btn btn-default btn-large"><span
						class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</a>
					
				</div>
			</div>
		</div>
		<!-- /Truck -->
	
	</div>
	
	<div class="row">
	
		<!-- Orders -->
		<div class="col-md-6">
			<div class="well">
				<legend class="the-legend">
					Orders <span class="glyphicon glyphicon-briefcase"
						aria-hidden="true"></span>
				</legend>
	
	            <div class="btn-group btn-group-justified" role="group"> 
					<a href="${pageContext.request.contextPath}/order"
						role="button" class="btn btn-default btn-large"><span
						class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a>
		
					<a href="${pageContext.request.contextPath}/order/new"
						role="button" class="btn btn-default btn-large"><span
						class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</a>
				</div>
				
			</div>
		</div>
		<!-- /Orders -->
	
	    <!-- Cargoes -->
		<div class="col-md-6">
			<div class="well">
				<legend class="the-legend">
					Cargoes <span class="glyphicon glyphicon-oil" aria-hidden="true"></span>
				</legend>
	
	            <div class="btn-group btn-group-justified" role="group"> 
				<a href="${pageContext.request.contextPath}/cargo" role="button" class="btn btn-default btn-large"><span
					class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a>
				</div>
				
			</div>
		</div>
		<!-- /Cargoes -->
		
	</div>
	<!-- /.row --> 

</div>
<!-- /Container -->

<jsp:include page="../GlobalFooter.jsp"/>