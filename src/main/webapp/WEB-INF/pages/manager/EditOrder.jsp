<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="Edit order # ${orderId}" />
	<jsp:param value="manager/manager.css" name="css" />
	<jsp:param value="manager/PostFormByAjax.js" name="js" />

</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="homeLink" value="/manager" />
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
                            ${driver.name} ${driver.surname} 
                        </c:forEach>
                     </c:when>
                     <c:otherwise><span class="label label-danger">Not assigned</span></c:otherwise>
                 </c:choose>
             </h4>
			
			</div>
		</div>
		<!-- /Info -->

        <!-- Buttons -->
		<div class="row margin-bottom">
			<div class="col-md-2 col-md-offset-5">

				<div class="btn-group-vertical" role="group" aria-label="...">

					<!-- Add cargo -->
					<button type="button" class="btn btn-default btn-lg" data-toggle="modal" data-target="#add-cargo">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-oil" aria-hidden="true"></span> Add
						cargo
					</button>

					<!-- Assign truck -->
					<button type="button" class="btn btn-default btn-lg">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-bed" aria-hidden="true"></span> Assign
						truck
					</button>

					<!-- Assign driver to tuck -->
					<button type="button" class="btn btn-default btn-lg disabled">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-user" aria-hidden="true"></span>
						Assign drivers to Truck
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

<jsp:include page="../GlobalFooter.jsp" />