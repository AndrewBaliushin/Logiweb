<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
    <jsp:param name="title" value="Trucks List" />
    <jsp:param value="manager/manager.css" name="css"/>
    <jsp:param value="manager/RemoveTruck.js" name="js"/>
    
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
    <jsp:param name="homeLink" value="/manager" />
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading"><h1>List of trucks</h1></div>
    <div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>License plate</th>
					<th>Crew size</th>
					<th>Cargo capacity <small>x1000kg<small></th>
					<th>Status</th>
					<th>Current City</th>
					<th>Delivery order</th>
				</tr>
			</thead>
			<tbody>
		
				<c:forEach items="${trucks}" var="truck">
					<tr id="truck-id-${truck.id}-row">
					
						<td><c:out value="${truck.licencePlate}" /></td>
						<td><c:out value="${truck.crewSize}" /></td>
						<td><c:out value="${truck.cargoCapacity}" /></td>
						<td><c:out value="${truck.status}" /></td>
						<td><c:out value="${truck.currentCity.name}" /></td>
						<td><c:if test="${empty truck.assignedDeliveryOrder.id}">Not assigned</c:if>
							<c:out value="${truck.assignedDeliveryOrder.id}" /></td>
						<td class="text-center">
						  <span class="glyphicon glyphicon-pencil red-on-hover" aria-hidden="true"></span>
		                </td>
		                <td class="text-center">
		                    <span onclick="removeTruck(this, ${truck.id})" class="glyphicon glyphicon-remove red-on-hover" aria-hidden="true"></span>
		                </td>
		
					</tr>
				</c:forEach>
		
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../GlobalFooter.jsp"/>