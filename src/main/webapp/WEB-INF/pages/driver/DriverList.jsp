<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="Drivers List" />
	<jsp:param value="manager/manager.css" name="css" />
	<jsp:param value="manager/RemoveDriver.js" name="js" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="userRoleForTitle" value="Driver" />
    <jsp:param name="homeLink" value="/driver" />
</jsp:include>


<div class="panel panel-default">
	<div class="panel-heading">
		<h1>List of drivers</h1>
	</div>
	<div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th class="text-center">Employee ID</th>
					<th>Name</th>
					<th>Surname</th>
					<th>Status</th>
					<th>Current City</th>
					<th>Current Truck</th>
					<th>Current Order</th>
					<th>Worked in this month <small>hours</small></th>
					<th class="text-center">View</th>				</tr>
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

						<td><c:if
								test="${empty driver.currentTruck.assignedDeliveryOrder}">Not assigned</c:if>
							${driver.currentTruck.assignedDeliveryOrder.id}</td>

						<td><c:out value="${workingHoursForDrivers[driver]}" /></td>

						<td class="text-center"><a
							href="${pageContext.request.contextPath}/driver/showDriver?driverId=${driver.id}"><span
								class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
						</td>
					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../GlobalFooter.jsp" />