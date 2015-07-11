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
					<th>Drivers</th>
				</tr>
			</thead>
			<tbody>
		
				<c:forEach items="${trucks}" var="truck">
					<tr id="truck-id-${truck.id}-row">
					
						<td>${truck.licencePlate}</td>
						<td>${truck.crewSize}</td>
						<td>${truck.cargoCapacity}</td>
						<td>${truck.status}</td>
						<td>${truck.currentCity.name}</td>
						
						<td><c:if
                                test="${empty truck.assignedDeliveryOrder}">Not assigned</c:if>
                            <a href="
                            <c:url value="editOrder">
                                <c:param name="orderId" value="${truck.assignedDeliveryOrder.id}" />
                            </c:url>">${truck.assignedDeliveryOrder.id}</a>

                        </td>
							
						<td><c:if test="${empty truck.drivers}">Not assigned</c:if>
                            <c:forEach items="${truck.drivers}" var="driver">
							    <a href="
		                            <c:url value="showDriver">
		                                <c:param name="driverId" value="${driver.id}" />
		                            </c:url>">${driver.surname}</a><span class="comma-separator">,</span>
							</c:forEach>
                        </td>	
                            
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