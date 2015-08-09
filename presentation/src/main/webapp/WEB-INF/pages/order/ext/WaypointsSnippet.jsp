<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Route info: print waypoints -->
<div class="panel panel-default">
	<!-- Default panel contents -->
	<div class="panel-heading">Waypoints</div>
	
	<table class="table">
        <tr>
            <th>Operation</th>
            <th>City</th>
            <th>Cargo</th>
        </tr>
        
        <c:forEach var="waypoint" items="${routeInfo.bestOrderOfDelivery}">
                <tr>
                    <td>
	                    <c:choose>
	                    
						<c:when test="${waypoint.operation == 'PICKUP'}">
							<span class="glyphicon glyphicon-import"
								aria-hidden="true"></span> Pickup</c:when>
								
						<c:when test="${waypoint.operation == 'DELIVER'}">
							<span class="glyphicon glyphicon-export"
								aria-hidden="true"></span> Deliver</c:when>
						
					</c:choose>
				</td>
                    
                    <td>${waypoint.city.name}</td>
                    <td>#<c:out value="${waypoint.cargo.id}"/> &mdash; ${waypoint.cargo.title} </td>
                </tr>
        </c:forEach>
        
	</table>

</div>