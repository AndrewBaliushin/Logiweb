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
    <li class="list-group-item">Location: ${driver.currentCity.name}</li>
    
    <c:if test="${!empty driver.currentTruck}">
        <li class="list-group-item">Current truck: ${driver.currentTruck.licencePlate}</li>
    </c:if>
    
    <c:if test="${!empty driver.currentTruck && fn:length(driver.currentTruck.drivers) > 1}">
        <li class="list-group-item">Co-driver:
            <c:forEach var="coDriver" items="${driver.currentTruck.drivers}">
                <c:if test="${coDriver.id != driver.id}">
                    <a href="${pageContext.request.contextPath}/driver/${coDriver.id}">
                        ${coDriver.name} ${coDriver.surname}</a><span class="comma-separator">,</span>
                </c:if>
            </c:forEach>
        </li>
    </c:if>

    <c:if test="${!empty driver.currentTruck || !empty driver.currentTruck.assignedOrder}">
			<li class="list-group-item">Current order: 
			   
			   <sec:authorize access="hasRole('ROLE_MANAGER')">
				     <a href="${pageContext.request.contextPath}/order/${driver.currentTruck.assignedDeliveryOrder.id}">
				     ${driver.currentTruck.assignedDeliveryOrder.id}</a>
				</sec:authorize>
                <sec:authorize access="hasRole('ROLE_DRIVER')">
					   ${driver.currentTruck.assignedDeliveryOrder.id}
                </sec:authorize>

			</li>
		</c:if>
    
    <li class="list-group-item">Working hours in this month: <span class="label label-info">
        <fmt:formatNumber type="number" value="${workingHours}" pattern=".#" /></span></li>
        
    </ul>
  
</div>
<!-- /Driver info -->