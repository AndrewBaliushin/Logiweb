<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<c:choose>
    <c:when test="${formAction == 'new'}">
       <c:set var="title" value="Add truck"></c:set>
       <c:set var="buttonText" value="Create truck"></c:set>
    </c:when>
    <c:when test="${formAction == 'edit'}">
       <c:set var="title" value="Edit truck"></c:set>
       <c:set var="buttonText" value="Edit truck"></c:set>
    </c:when>
</c:choose>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="${title}" />
	<jsp:param value="truck/PlateValidator.js" name="js"/>
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="homeLink" value="/" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<form:form modelAttribute="truckModel" method="post" cssClass="form-horizontal">
    <fieldset>

		<!-- Form Name -->
		<legend>${title}</legend>
		
		<%--Error message --%>
		<c:if test="${not empty error}">
			<div class="form-group">
			    <div class="col-md-4"><!-- blank --></div>
				<div class="col-md-4 alert alert-warning">
					<strong>Warning!</strong> ${error}
				</div>
			</div>
		</c:if>
		
		<form:hidden path="id" />

		<!-- License Plate: Text input-->
		<spring:bind path="licencePlate">
          <div class="form-group ${status.error ? 'has-error' : ''}">
            <label class="col-md-4 control-label">License plate</label>
            <div class="col-md-4">
                <form:input path="licencePlate" type="text" class="form-control input-md" 
                                id="licencePlate" placeholder="License plate" />
                <form:errors path="licencePlate" class="control-label has-error" />
            </div>
          </div>
        </spring:bind>

		<!-- Crew Size: Multiple Radios (inline) -->
		<spring:bind path="crewSize">
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="col-md-4 control-label">Crew
					Size</label>
				<div class="col-md-4">
				
					<c:forEach var="i" begin="1" end="4">
					    <label class="radio-inline">
	                        <form:radiobutton path="crewSize" value="${i}"/> ${i}
	                    </label>
					</c:forEach>
					
					<form:errors path="crewSize" class="control-label has-error" />
				</div>
			</div>
		</spring:bind>

		<!-- Capacity: Text input-->
		<spring:bind path="cargoCapacity">
          <div class="form-group ${status.error ? 'has-error' : ''}">
            <label class="col-md-4 control-label">Cargo Capacity</label>
            <div class="col-md-4">
                <form:input path="cargoCapacity" type="text" class="form-control input-md" 
                                id="cargoCapacity" placeholder="Cargo capacity" />
                <form:errors path="cargoCapacity" class="control-label has-error" />
            </div>
          </div>
        </spring:bind>

		<!-- City: Select Basic -->
		<spring:bind path="currentCityId">
          <div class="form-group ${status.error ? 'has-error' : ''}">
            <label class="col-md-4 control-label">City</label>
            <div class="col-md-4">
                <form:select path="currentCityId" class="form-control">
                    <form:options items="${cities}" itemLabel="name"/>
                </form:select>
                <form:errors path="currentCityId" class="control-label" />
            </div>
          </div>
        </spring:bind>
        
        <!-- Status -->
        <c:if test="${formAction == 'edit'}">
            <spring:bind path="status">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-md-4 control-label">Status</label>
                    <div class="col-md-4">
                        <form:select path="status" class="form-control">
                            <form:options items="${truckStatuses}"/>
                        </form:select>
                        <form:errors path="status" class="control-label" />
                    </div>
                </div>
            </spring:bind>
        </c:if>
        
		<!-- Submit -->
		<div class="form-group">
			<label class="col-md-4 control-label"></label>
			<div class="col-md-4">
				<button class="btn btn-primary" type="submit">${buttonText}</button>
			</div>
		</div>

	</fieldset>
</form:form>


<jsp:include page="../GlobalFooter.jsp" />