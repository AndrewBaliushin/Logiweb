<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="Order List" />
	<jsp:param value="manager/manager.css" name="css" />

</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="homeLink" value="/manager" />
</jsp:include>

<div class="panel panel-default">
	<div class="panel-heading">
		<h1>List of orders</h1>
	</div>
	<div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Order ID</th>
					<th>Status</th>
					<th>Cargo</th>
					<th>Assigned truck</th>
					<th class="text-center">Edit</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${orders}" var="order">
					<tr id="truck-id-${order.id}-row">

						<td>${order.id}</td>
						<td>${order.status}</td>

						<td><c:choose>
								<c:when test="${empty order.assignedCargoes}">Empty</c:when>
								<c:otherwise>
									<!-- Modal button -->
									<button type="button" class="btn btn-default"
										data-toggle="modal" data-target="#myModal-${order.id}">
										<span class="glyphicon glyphicon-menu-down" aria-hidden="true"></span>
										View ${fn:length(order.assignedCargoes)} cargo<c:if test="${fn:length(order.assignedCargoes) gt 1}">es</c:if>
									</button>

									<!-- Modal -->
									<div class="modal fade modal-wide" id="myModal-${order.id}" tabindex="-1"
										role="dialog" aria-labelledby="myModalLabel">
										<div class="modal-dialog" role="document">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal"
														aria-label="Close">
														<span aria-hidden="true">&times;</span>
													</button>
													<h4 class="modal-title" id="myModalLabel">
														Cargoes in order #
														<c:out value="${order.id}"></c:out>
													</h4>
												</div>
												<div class="modal-body">
												
													<table class="table table-bordered">
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
												<div class="modal-footer">
													<button type="button" class="btn btn-default"
														data-dismiss="modal">Close</button>
												</div>
											</div>
										</div>
									</div>
									<!-- /Modal -->

								</c:otherwise>
							</c:choose></td>

						<td><c:if test="${empty order.assignedTruck.licencePlate}">Not assigned</c:if>
							${order.assignedTruck.licencePlate}</td>

						<td class="text-center"><a
							href="editOrder?orderId=${order.id}"> <span
								class="glyphicon glyphicon-pencil red-on-hover"
								aria-hidden="true"></span>
						</a></td>

					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../GlobalFooter.jsp" />