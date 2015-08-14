<%@page import="com.tsystems.javaschool.logiweb.entities.status.OrderStatus"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% pageContext.setAttribute("orderNotReady", OrderStatus.NOT_READY); %>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="Edit order # ${orderId}" />
	<jsp:param value="common/common.css" name="css" />
	<jsp:param
		value="manager/PostFormByAjax.js,order/RemoveTruckAndDriversFromOrder.js,order/ChangeOrderStatus.js,order/LimitCheckboxesForDriverAssignment.js"
		name="js" />

</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="homeLink" value="/" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<!-- Edit Order -->
<div class="panel panel-primary">
	<div class="panel-heading">
		<h1>
			Edit order #<c:out value="${orderId}"/>
		</h1>
	</div>

	<div class="panel-body">

		<!-- Info -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Order info</h3>
			</div>
			<div class="panel-body">
			 <h4>Max weight<small> x1000kg</small>:<span class="label label-info">${routeInfo.maxWeightOnCourse}</span></h4>
			 <h4>Estimated time to deliver<small> hours</small>:<span class="label label-info"><fmt:formatNumber value="${routeInfo.estimatedTime}" pattern="0.0"/></span></h4>
			 
			 <h4>Assigned truck<small> license plate</small>: 
			     <c:choose>
			         <c:when test="${empty order.assignedTruck}"><span class="label label-warning">Not assigned</span></c:when>
			         <c:otherwise><span class="label label-success">${order.assignedTruck.licencePlate }</span></c:otherwise>
			     </c:choose>
			 </h4>
			 
			 <h4>Assigned drivers 
			     <c:if test="${!empty order.assignedTruck}">
			         (${fn:length(order.assignedTruck.driversIdsAndSurnames)} / ${order.assignedTruck.crewSize})
                 </c:if>:
                 
                 <c:choose>
                     <c:when test="${!empty order.assignedTruck && !empty order.assignedTruck.driversIdsAndSurnames}">
                        <c:forEach items="${order.assignedTruck.driversIdsAndSurnames}" var="entry">
                            <a href="${pageContext.request.contextPath}/driver/${entry.key}">${entry.value}</a><span class="comma-separator">,</span>
                        </c:forEach>
                     </c:when>
                     <c:otherwise><span class="label label-danger">Not assigned</span></c:otherwise>
                 </c:choose>
             </h4>
			
			<h4>Status: ${order.status}</h4>

            <!-- Remove drivers and truck -->
            <c:if test="${order.status == orderNotReady}">
				<button type="button" class="btn btn-default btn-xs" onclick="removeTruckAndDriverFromOrder(${order.id})">
					<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
					Remove truck and drivers
				</button>
		    </c:if>

			</div>
		</div>
		<!-- /Info -->

        <!-- Buttons -->
		<div class="row margin-bottom">
			<div class="col-md-2 col-md-offset-5">

				<div class="btn-group-vertical" role="group" aria-label="...">

					<!-- Add cargo -->
					<button type="button" class="btn btn-default btn-lg <c:if test="${order.status != orderNotReady || !empty order.assignedTruck}">disabled</c:if>" data-toggle="modal" data-target="#add-cargo">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-oil" aria-hidden="true"></span> Add
						cargo
					</button>

					<!-- Assign truck -->
					<button type="button"
						class="btn btn-default btn-lg <c:if test="${!empty order.assignedTruck || order.status != orderNotReady}">disabled</c:if>"
						data-toggle="modal" data-target="#assign-truck">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-bed" aria-hidden="true"></span> Assign
						truck
					</button>

					<!-- Assign driver to tuck -->
					<button type="button" class="btn btn-default btn-lg <c:if test="${empty order.assignedTruck || (!empty order.assignedTruck.driversIdsAndSurnames && fn:length(order.assignedTruck.driversIdsAndSurnames) >= order.assignedTruck.crewSize)}">disabled</c:if>"
					data-toggle="modal" data-target="#assign-driver">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-user" aria-hidden="true"></span>
						Assign drivers to Truck
					</button>
					
					<!-- Status change -->
					<button type="button" class="btn btn-default btn-lg 
					    <c:choose>
	                       <c:when test="${order.status == 'NOT_READY' && !empty order.assignedTruck && (!empty order.assignedTruck.driversIdsAndSurnames && fn:length(order.assignedTruck.driversIdsAndSurnames) >= order.assignedTruck.crewSize)}"></c:when>
	                       
	                       <c:otherwise>disabled</c:otherwise>
                        </c:choose>"

						data-toggle="modal" data-target="#change-status-modal"  onclick="changeOrderStatusToReady(${order.id}) "><span
							class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>
						Ready!
					</button>

				</div>

			</div>
		</div>
		<!-- /Buttons -->


        <!-- Cargo list -->
		<div class="panel panel-info">
			<div class="panel-heading">
				<h5>
					Cargoes in order
				</h5>
			</div>
			<div class="panel-body">
				<table class="table">
					<thead>
						<tr>
							<th>Cargo ID</th>
							<th>Cargo title</th>
							<th>Cargo weight <small>x1000kg</small></th>
							<th>Cargo status</th>
							<th>Origin city</th>
							<th>Destination city</th>
						</tr>
					</thead>

					<tbody>

						<c:forEach items="${order.assignedCargoes}" var="cargo">
							<tr>
								<td>${cargo.id}</td>
								<td>${cargo.title}</td>
								<td>${cargo.weight}</td>
								<td>${cargo.status}</td>
								<td>${citites[cargo.originCityid].name}</td>
								<td>${citites[cargo.destinationCityId].name}</td>
							</tr>
						</c:forEach>

					</tbody>
				</table>
			</div>
		</div>
		<!-- /Cargo list -->

		<!-- Print waypoints-->
		<jsp:include page="ext/WaypointsSnippet.jsp">
			<jsp:param name="routeInfo" value="${routeInfo}" />
		</jsp:include>

	</div>

</div>

<!-- Modal: Add cargo -->
<jsp:include page="ext/AddCargoModal.jsp"/>
<!-- /Modal Add cargo -->

<!-- Modal: Assign truck -->
<jsp:include page="ext/AssignTruckModal.jsp"/>
<!-- /Modal Assign truck -->

<!-- Modal: Assign driver -->
<jsp:include page="ext/AssignDriversModal.jsp"/>
<!-- /Modal Assign driver -->
 
<jsp:include page="../GlobalFooter.jsp" />