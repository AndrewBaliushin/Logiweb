<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="../GlobalHeader.jsp">
    <jsp:param name="title" value="Driver info" />
    <jsp:param value="common/common.css" name="css" />
    <jsp:param value="manager/RemoveDriver.js" name="js" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
    <jsp:param name="homeLink" value="/manager" />
    <jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<jsp:include page="ext/SingleDriverInfoSnippet.jsp">
    <jsp:param name="privelege" value="edit" />
</jsp:include>

<c:if test="${!empty routeInfo}">
	<!-- Print waypoints-->
	<jsp:include page="../manager/ext/WaypointsSnippet.jsp">
		<jsp:param name="routeInfo" value="${routeInfo}" />
	</jsp:include>
</c:if>

<c:if test="${!empty journals}">
	<!-- Print shift records-->
	<jsp:include page="../manager/ext/ShiftJournalsSnippet.jsp">
		<jsp:param name="comment" value="this month" />
		<jsp:param name="journals" value="${journals}" />
	</jsp:include>
</c:if>

<jsp:include page="../GlobalFooter.jsp" />