<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
    <jsp:param name="title" value="Driver List" />
    <jsp:param value="manager/manager.css" name="css"/>
    <jsp:param value="manager/RemoveDriver.js" name="js"/>
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
    <jsp:param name="homeLink" value="/manager" />
</jsp:include>

<table>
	
</table>

<table class="table table-striped">
	<thead>
		<tr>
			<th class="text-center">Employee ID</th>
			<th>Name</th>
			<th>Surname</th>
			<th>Status</th>
			<th>Current City</th>
			<th>Current Truck</th>
			<th class="text-center">Edit</th>
			<th class="text-center">Delete</th>
		</tr>
	</thead>
	<tbody>

		<c:forEach items="${drivers}" var="driver">
			<tr>
			
				<td class="text-center"><c:out value="${driver.employeeId}" /></td>
				<td><c:out value="${driver.name}" /></td>
				<td><c:out value="${driver.surname}" /></td>
				<td><c:out value="${driver.status}" /></td>
				<td><c:out value="${driver.currentCity.name}" /></td>
				<td><c:if test="${empty driver.currentTruck.licencePlate}">Not assigned</c:if>
					<c:out value="${driver.currentTruck.licencePlate}" /></td>
				<td class="text-center">
					<span class="glyphicon glyphicon-pencil red-on-hover" aria-hidden="true"></span>
				</td>
				<td class="text-center">
					<span onclick="removeDriver(this, ${driver.id})" class="glyphicon glyphicon-remove red-on-hover" aria-hidden="true"></span>
				</td>

			</tr>
		</c:forEach>

	</tbody>
</table>

<jsp:include page="../GlobalFooter.jsp"/>