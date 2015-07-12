<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="Add truck" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="homeLink" value="/manager" />
</jsp:include>


<form class="form-horizontal" method="POST">
	<fieldset>

		<!-- Form Name -->
		<legend>Add truck</legend>
		
		<%--Error message --%>
		<c:if test="${not empty error}">
			<div class="form-group">
			    <div class="col-md-4"><!-- blank --></div>
				<div class="col-md-4 alert alert-warning">
					<strong>Warning!</strong> ${error}
				</div>
			</div>
		</c:if>

		<!-- License Plate: Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="license-plate">License
				plate</label>
			<div class="col-md-4">
				<input id="license-plate" name="license-plate" type="text"
					placeholder="License plate" class="form-control input-md"
					required value="${licensePlate}"> <span class="help-block">2 letters + 5
					digits</span>
			</div>
		</div>

		<!-- Crew Size: Multiple Radios (inline) -->
		<div class="form-group">
			<label class="col-md-4 control-label" for="crew-size">Crew
				Size</label>
			<div class="col-md-4">
				<label class="radio-inline" for="crew-size-0"> <input
					type="radio" name="crew-size" id="crew-size-0" value="1"
					<c:if test="${empty crewSize || crewSize == 1}">checked="checked"</c:if>> 1
				</label> <label class="radio-inline" for="crew-size-1"> <input
					type="radio" name="crew-size" id="crew-size-1" value="2"
					<c:if test="${not empty crewSize && crewSize == 2}">checked="checked"</c:if>> 2
				</label> <label class="radio-inline" for="crew-size-2"> <input
					type="radio" name="crew-size" id="crew-size-2" value="3"
					<c:if test="${not empty crewSize && crewSize == 3}">checked="checked"</c:if>> 3
				</label> <label class="radio-inline" for="crew-size-3"> <input
					type="radio" name="crew-size" id="crew-size-3" value="4"
					<c:if test="${not empty crewSize && crewSize == 4}">checked="checked"</c:if>> 4
				</label>
			</div>
		</div>

		<!-- Capacity: Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="cargo-capacity">Cargo
				capacity</label>
			<div class="col-md-4">
				<input id="cargo-capacity" name="cargo-capacity" type="text"
					placeholder="Tons" class="form-control input-md" value="${cargoCapacity}">
				<span class="help-block">x1000kg</span>
			</div>
		</div>

		<!-- City: Select Basic -->
		<div class="form-group">
			<label class="col-md-4 control-label" for="city">City</label>
			<div class="col-md-4">
				<select id="city" name="city" class="form-control">
					<c:forEach items="${cities}" var="cityOption">
						<option value="${cityOption.id}"
						  <c:if test="${not empty city && city == cityOption.id}">selected="selected"</c:if>>${cityOption.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>

		<!-- Submit -->
		<div class="form-group">
			<label class="col-md-4 control-label"></label>
			<div class="col-md-4">
				<button class="btn btn-primary" type="submit">Create Truck</button>
			</div>
		</div>

	</fieldset>
</form>


<jsp:include page="../GlobalFooter.jsp" />