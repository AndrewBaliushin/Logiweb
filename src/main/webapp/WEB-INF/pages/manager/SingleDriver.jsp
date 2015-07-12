<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="../GlobalHeader.jsp">
    <jsp:param name="title" value="Driver info" />
    <jsp:param value="manager/manager.css" name="css" />
    <jsp:param value="manager/RemoveDriver.js" name="js" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
    <jsp:param name="homeLink" value="/manager" />
</jsp:include>

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
		            <a href="
	                   <c:url value="showDriver">
	                       <c:param name="driverId" value="${coDriver.id}" />
	                   </c:url>">${coDriver.name} ${coDriver.surname}</a><span class="comma-separator">,</span>
	            </c:if>
	        </c:forEach>
        </li>
    </c:if>

	<c:if test="${!empty driver.currentTruck || !empty driver.currentTruck.assignedOrder}">
		<li class="list-group-item">Current order: <a href="
			      <c:url value="editOrder">
	                         <c:param name="orderId" value="${driver.currentTruck.assignedDeliveryOrder.id}" />
	              </c:url>
              ">${driver.currentTruck.assignedDeliveryOrder.id}</a></li>
	</c:if>
	
	<li class="list-group-item">Working hours in this month: <span class="label label-info">${workingHours}</span></li>
		
	</ul>
  
</div>
<!-- /Driver info -->

<c:if test="${!empty routeInfo}">
	<!-- Print waypoints-->
	<jsp:include page="ext/WaypointsSnippet.jsp">
		<jsp:param name="routeInfo" value="${routeInfo}" />
	</jsp:include>
</c:if>

<c:if test="${!empty journals}">
	<!-- Print shift records-->
	<jsp:include page="ext/ShiftJournalsSnippet.jsp">
		<jsp:param name="comment" value="this month" />
		<jsp:param name="journals" value="${journals}" />
	</jsp:include>
</c:if>

<jsp:include page="../GlobalFooter.jsp" />