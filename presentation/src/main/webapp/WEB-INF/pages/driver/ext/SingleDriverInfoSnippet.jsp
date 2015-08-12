<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Driver info -->
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">Driver info</h3>
  </div>
  
  <ul class="list-group">
    <li class="list-group-item">Employee ID: <span class="label label-info"> ${driver.employeeId}</span></li>
    <li class="list-group-item">${driver.name} ${driver.surname}</li>
    <li class="list-group-item">Status: <span class="label label-info">${driver.status}</span></li>
    <li class="list-group-item">Location: ${cities[driver.currentCityId].name}</li>
    
    <c:if test="${!empty driver.currentTruckLicensePlate}">
        <li class="list-group-item">Current truck: ${driver.currentTruckLicensePlate}</li>
    </c:if>
    
    <c:if test="${!empty driver.coDriversIds}">
        <li class="list-group-item">Co-driver:
            <c:forEach var="coDriverId" items="${driver.coDriversIds}">
                <c:if test="${coDriverId != driver.id}">
                    <a href="${pageContext.request.contextPath}/driver/${coDriverId}">
                        ${coDrivers[coDriverId].surname}</a><span class="comma-separator">,</span>
                </c:if>
            </c:forEach>
        </li>
    </c:if>

    <c:if test="${!empty driver.orderId}">
			<li class="list-group-item">Current order: 
			   
			   <sec:authorize access="hasRole('ROLE_MANAGER')">
				     <a href="${pageContext.request.contextPath}/order/${driver.orderId}">
				     ${driver.orderId}</a>
				</sec:authorize>
                <sec:authorize access="hasRole('ROLE_DRIVER')">
					   ${driver.orderId}
                </sec:authorize>

			</li>
		</c:if>
    
    <li class="list-group-item">Working hours in this month: <span class="label label-info">
        <fmt:formatNumber type="number" value="${driver.workingHoursThisMonth}" pattern=".#" /></span></li>
        
    </ul>
  
</div>
<!-- /Driver info -->