<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
    <jsp:param name="title" value="Manager front page" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
    <jsp:param name="homeLink" value="/manager" />
</jsp:include>

<div class="well">
	<legend class="the-legend">
		Drivers <span class="glyphicon glyphicon-user" aria-hidden="true"></span>
	</legend>

	<a href="manager/driverList" role="button" class="btn btn-success btn-large"><span
		class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a> 
		
	<a href="#" role="button" class="btn btn-success btn-large"><span
		class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</a>

</div>

<div class="well">
	<legend class="the-legend">
		Trucks <span class="glyphicon glyphicon-bed" aria-hidden="true"></span>
	</legend>

	<a href="manager/showTrucks" role="button" class="btn btn-success btn-large"><span
		class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a> 
		
	<a href="#" role="button" class="btn btn-success btn-large"><span
		class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</a>
</div>

<div class="well">
	<legend class="the-legend">
		Orders <span class="glyphicon glyphicon-briefcase" aria-hidden="true"></span>
	</legend>

	<a href="#" role="button" class="btn btn-success btn-large"><span
		class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a> 
		
	<a href="#" role="button" class="btn btn-success btn-large"><span
		class="glyphicon glyphicon-plus" aria-hidden="true"></span> New</a>
</div>

<div class="well">
	<legend class="the-legend">
		Cargoes <span class="glyphicon glyphicon-oil" aria-hidden="true"></span>
	</legend>

	<a href="#" role="button" class="btn btn-success btn-large"><span
		class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a>
</div>

<!-- Separator between .well and #footer -->
<span>&nbsp;</span>
<jsp:include page="../GlobalFooter.jsp"/>