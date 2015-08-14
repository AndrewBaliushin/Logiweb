<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


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

				<form class="form-horizontal" id="assignTruckForm" method="POST"
					action="assignTruck">
					<input type="hidden" name="orderId" value="${order.id}">

					<fieldset>

						<div class="form-group">
							<label class="col-md-3 control-label" for="truck">Suggested
								trucks</label>
							<div class="col-md-9">
								<select id="truck" name="truckId" class="form-control">
									<c:forEach items="${suggestedTrucks}" var="truck">
										<option value="${truck.id}">Plate:
											${truck.licencePlate} | Max weight: ${truck.cargoCapacity} |
											Currnet city: ${cities[truck.currentCityId].name} | Crew
											size: ${truck.crewSize}</option>
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