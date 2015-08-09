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
			         (${fn:length(order.assignedTruck.drivers)} / ${order.assignedTruck.crewSize})
                 </c:if>:
                 
                 <c:choose>
                     <c:when test="${!empty order.assignedTruck && !empty order.assignedTruck.drivers}">
                        <c:forEach items="${order.assignedTruck.drivers}" var="driver">
                            <a href="${pageContext.request.contextPath}/driver/${driver.id}">${driver.name} ${driver.surname}</a><span class="comma-separator">,</span>
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
					<button type="button" class="btn btn-default btn-lg <c:if test="${empty order.assignedTruck || (!empty order.assignedTruck.drivers && fn:length(order.assignedTruck.drivers) >= order.assignedTruck.crewSize)}">disabled</c:if>"
					data-toggle="modal" data-target="#assign-driver">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-user" aria-hidden="true"></span>
						Assign drivers to Truck
					</button>
					
					<!-- Status change -->
					<button type="button" class="btn btn-default btn-lg 
					    <c:choose>
	                       <c:when test="${order.status == 'NOT_READY' && !empty order.assignedTruck && (!empty order.assignedTruck.drivers && fn:length(order.assignedTruck.drivers) >= order.assignedTruck.crewSize)}"></c:when>
	                       
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
								<td>${cargo.originCity.name}</td>
								<td>${cargo.destinationCity.name}</td>
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
<div class="modal fade modal-wide" id="add-cargo" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					Add cargo to order #
					<c:out value="${order.id}"></c:out>
				</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal" id="addCargoForm" method="POST" action="addCargo">
				<input type="hidden" name="orderId" value="${order.id}">
				
					<fieldset>
					
						<!-- Title: Text input-->
						<div class="form-group">
							<label class="col-md-4 control-label" for="cargoTitle">Cargo
								Title</label>
							<div class="col-md-4">
								<input id="cargoTitle" name="cargoTitle" type="text"
									placeholder="Title" class="form-control input-md" required="">

							</div>
						</div>
						
						<!-- Weight: Text input-->
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="cargoWeight">Cargo
                                Weight <small>x1000kg</small></label>
                            <div class="col-md-4">
                                <input id="cargoWeight" name="cargoWeight" type="text"
                                    placeholder="Wieght" class="form-control input-md" required="">

                            </div>
                        </div>
						

						<!-- Origin: Select Basic -->
						<div class="form-group">
							<label class="col-md-4 control-label" for="originCity">Origin
								City</label>
							<div class="col-md-4">
								<select id="originCity" name="originCity" class="form-control">
									<c:forEach items="${cities}" var="cityOption">
										<option value="${cityOption.id}">${cityOption.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<!-- Origin: Select Basic -->
						<div class="form-group">
							<label class="col-md-4 control-label" for="destinationCity">Destination
								City</label>
							<div class="col-md-4">
								<select id="destinationCity" name="destinationCity"
									class="form-control">
									<c:forEach items="${cities}" var="cityOption">
                                        <option value="${cityOption.id}">${cityOption.name}</option>
                                    </c:forEach>
								</select>
							</div>
						</div>

					</fieldset>
				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-success" 
					onclick="postFormByAjax('#addCargoForm')">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- /Modal -->

<!-- Modal: Assign truck -->
<div class="modal fade modal-wide" id="assign-truck" tabindex="-1"
    role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    Assign truck to order #
                    <c:out value="${order.id}"></c:out>
                </h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" id="assignTruckForm" method="POST" action="assignTruck">
                <input type="hidden" name="orderId" value="${order.id}">
                
                    <fieldset>
                    
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="truck">Suggested trucks</label>
                            <div class="col-md-9">
                                <select id="truck" name="truckId" class="form-control">
                                    <c:forEach items="${suggestedTrucks}" var="truck">
                                        <option value="${truck.id}">
                                            Plate: ${truck.licencePlate} |
                                            Max weight: ${truck.cargoCapacity} |
                                            Currnet city: ${truck.currentCity.name} |
                                            Crew size: ${truck.crewSize}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        
                    </fieldset>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" 
                    onclick="postFormByAjax('#assignTruckForm')">Save</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- /Modal Assign truck -->


<!-- Modal: Assign driver -->
<div class="modal fade modal-wide" id="assign-driver" tabindex="-1"
    role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    Assign drivers to order #
                    <c:out value="${order.id}"></c:out>
                    who had less than <fmt:formatNumber value="${maxWorkingHoursLimit - routeInfo.estimatedTime}" pattern="0.0"/>
                    working hours in this month
                </h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" id="assignDriverForm" method="POST" action="addDriverToTruck">
                <input type="hidden" name="truckId" value='<c:if test="${!empty order.assignedTruck }">${order.assignedTruck.id}</c:if>'>
                
                <c:if test="${!empty order.assignedTruck && empty order.assignedTruck.drivers}">
                    <input type="hidden" name="maxDriversToAssign" value='${order.assignedTruck.crewSize}'>
                </c:if>
                <c:if test="${!empty order.assignedTruck && !empty order.assignedTruck.drivers}">
                    <input type="hidden" name="maxDriversToAssign" value='${order.assignedTruck.crewSize - fn:length(order.assignedTruck.drivers)}'>
                </c:if>
                
                    <fieldset>
                    
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="driverId">Suggested drivers</label>
                            <div class="col-md-9">
								
                                <c:forEach items="${suggestedDrivers}" var="driver">
									<label for="driver-checkbox-${driver.id}"> <input type="checkbox"
										name="driversIds" id="driver-checkbox-${driver.id}" value="${driver.id}">
										${driver.name} ${driver.surname} | Currnet city:
										${driver.currentCity.name} | This month working hours:
										<fmt:formatNumber type="number" value="${workingHoursForDrivers[driver]}" pattern=".#" />
									</label>
								</c:forEach>
                                    
                            </div>
                        </div>
                        
                    </fieldset>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" 
                    onclick="postFormByAjax('#assignDriverForm')">Save</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- /Modal Assign driver -->
 
<jsp:include page="../GlobalFooter.jsp" />