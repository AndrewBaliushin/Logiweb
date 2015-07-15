<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="Add driver" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="homeLink" value="/manager" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>


<form class="form-horizontal" method="POST">
	<fieldset>

		<!-- Form Name -->
		<legend>Add driver</legend>

		<%--Error message --%>
		<c:if test="${not empty error}">
			<div class="form-group">
				<div class="col-md-4">
					<!-- blank -->
				</div>
				<div class="col-md-4 alert alert-warning">
					<strong>Warning!</strong> ${error}
				</div>
			</div>
		</c:if>

		<!-- ID : Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="employeeId">Employee
				ID</label>
			<div class="col-md-4">
				<input id="employeeId" name="employeeId" type="text"
					placeholder="ID" class="form-control input-md" value="${employeeId}" required="">

			</div>
		</div>

		<!-- Name : Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="name">Name</label>
			<div class="col-md-4">
				<input id="name" name="name" type="text" placeholder="Name"
					class="form-control input-md" required value="${name}">

			</div>
		</div>

		<!-- Surname : Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="surname">Surname</label>
			<div class="col-md-4">
				<input id="surname" name="surname" type="text" placeholder="Surname"
					class="form-control input-md" required value="${surname}">

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
				<button class="btn btn-primary" type="submit">Create Driver</button>
			</div>
		</div>

	</fieldset>
</form>


<jsp:include page="../GlobalFooter.jsp" />