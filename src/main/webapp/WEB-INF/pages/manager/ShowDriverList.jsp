<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<%@ include file="ManagerHeader.jsp"%>
<table>
	
</table>

<table class="table table-striped">
	<thead>
		<tr>
			<th>Employee ID</th>
			<th>Name</th>
			<th>Surname</th>
			<th>Status</th>
			<th>Current City</th>
			<th>Current Truck</th>
		</tr>
	</thead>
	<tbody>

		<c:forEach items="${drivers}" var="driver">
			<tr>
			
				<td><c:out value="${driver.employeeId}" /></td>
				<td><c:out value="${driver.name}" /></td>
				<td><c:out value="${driver.surname}" /></td>
				<td><c:out value="${driver.status}" /></td>
				<td><c:out value="${driver.currentCity.name}" /></td>
				<td><c:if test="${empty driver.currentTruck.licencePlate}">Not assigned</c:if>
					<c:out value="${driver.currentTruck.licencePlate}" /></td>

			</tr>
		</c:forEach>

	</tbody>
</table>
<%@ include file="../GlobalFooter.jsp"%>