<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
    <jsp:param name="title" value="Order List" />
    <jsp:param value="manager/manager.css" name="css"/>
    
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
    <jsp:param name="homeLink" value="/manager" />
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading"><h1>List of orders</h1></div>
    <div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Order ID</th>
					<th>Status</th>
					<th>Assigned truck</th>
					<th class="text-center">Edit</th>
				</tr>
			</thead>
			<tbody>
		
				<c:forEach items="${orders}" var="order">
					<tr id="truck-id-${order.id}-row">
					
						<td>${order.id}</td>
						<td>${order.status}</td>
						<td><c:if test="${empty order.assignedTruck.licencePlate}">Not assigned</c:if>
							${order.assignedTruck.licencePlate}
						</td>
                            
						<td class="text-center">
						  <span class="glyphicon glyphicon-pencil red-on-hover" aria-hidden="true"></span>
		                </td>
		
					</tr>
				</c:forEach>
		
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../GlobalFooter.jsp"/>