<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

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
					<th class="text-center">View</th>

					<%-- Edit priveleges --%>
					<sec:authorize access="hasRole('ROLE_MANAGER')">
						<th class="text-center">Edit</th>
						<th class="text-center">Delete</th>
					</sec:authorize>

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

						<td><c:if
								test="${empty driver.currentTruck.assignedDeliveryOrder}">Not assigned</c:if>


							<sec:authorize access="hasRole('ROLE_MANAGER')">
								<a href="${pageContext.request.contextPath}/order/${driver.currentTruck.assignedDeliveryOrder.id}">
								    ${driver.currentTruck.assignedDeliveryOrder.id}
								</a>
							</sec:authorize> <sec:authorize access="hasRole('ROLE_DRIVER')">
			                       ${driver.currentTruck.assignedDeliveryOrder.id}
			                </sec:authorize></td>

						<td><c:out value="${workingHoursForDrivers[driver]}" /></td>

						<td class="text-center"><a
							href="${pageContext.request.contextPath}/driver/${driver.id}"><span
								class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
						</td>

						<%-- Edit priveleges --%>
						<sec:authorize access="hasRole('ROLE_MANAGER')">
							<td class="text-center">
								 <a
	                             href="${pageContext.request.contextPath}/driver/${driver.id}/edit"><span
									class="glyphicon glyphicon-pencil"
									aria-hidden="true"></span></a>
							</td>

							<td class="text-center"><span
								onclick="removeDriver(this, ${driver.id})"
								class="glyphicon glyphicon-remove red-on-hover"
								aria-hidden="true"></span></td>

						</sec:authorize>

					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>

	<%-- Edit priveleges --%>
	<sec:authorize access="hasRole('ROLE_MANAGER')">
		<div class="panel-footer">
			<a href="${pageContext.request.contextPath}/driver/new" role="button"
				class="btn btn-default btn-large btn-block"><span
				class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</a>
		</div>
	</sec:authorize>

</div>