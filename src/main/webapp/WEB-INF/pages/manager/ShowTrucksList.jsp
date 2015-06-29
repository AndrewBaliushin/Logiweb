<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ include file="ManagerHeader.jsp"%>
<table>
	
</table>

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
			<tr>
			
				<td><c:out value="${truck.licencePlate}" /></td>
				<td><c:out value="${truck.crewSize}" /></td>
				<td><c:out value="${truck.cargoCapacity}" /></td>
				<td><c:out value="${truck.status}" /></td>
				<td><c:out value="${truck.currentCity.name}" /></td>
				<td><c:if test="${empty truck.assignedDeliveryOrder.id}">Not assigned</c:if>
					<c:out value="${truck.assignedDeliveryOrder.id}" /></td>

			</tr>
		</c:forEach>

	</tbody>
</table>
<%@ include file="../GlobalFooter.jsp"%>