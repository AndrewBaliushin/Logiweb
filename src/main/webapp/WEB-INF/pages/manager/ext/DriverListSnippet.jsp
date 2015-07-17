<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
					<c:if test="${param.privelege == 'edit'}">
						<th class="text-center">Edit</th>
						<th class="text-center">Delete</th>
					</c:if>

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
                            
                            <c:choose>
                                <c:when test="${param.privelege == 'edit'}">
		                                <a href="
		                            <c:url value="editOrder">
		                                <c:param name="orderId" value="${driver.currentTruck.assignedDeliveryOrder.id}" />
		                            </c:url>">${driver.currentTruck.assignedDeliveryOrder.id}</a>
                                </c:when>
                                <c:otherwise>
                                    ${driver.currentTruck.assignedDeliveryOrder.id}
                                </c:otherwise>
                            </c:choose>

                        </td>

                        <td><c:out value="${workingHoursForDrivers[driver]}" /></td>

                        <td class="text-center"><a
                            href="${pageContext.request.contextPath}/showDriver?driverId=${driver.id}"><span
                                class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
                        </td>

                         <%-- Edit priveleges --%>
						<c:if test="${param.privelege == 'edit'}">
							<td class="text-center"><span
								class="glyphicon glyphicon-pencil disabled-color"
								aria-hidden="true" disabled></span></td>

							<td class="text-center"><span
								onclick="removeDriver(this, ${driver.id})"
								class="glyphicon glyphicon-remove red-on-hover"
								aria-hidden="true"></span></td>

						</c:if>

					</tr>
                </c:forEach>

            </tbody>
        </table>
    </div>
</div>