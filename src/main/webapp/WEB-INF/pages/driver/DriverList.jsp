<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="Drivers List" />
	<jsp:param value="manager/manager.css" name="css" />
	<jsp:param value="manager/RemoveDriver.js" name="js" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="userRoleForTitle" value="Driver" />
    <jsp:param name="homeLink" value="/driver" />
</jsp:include>

<%-- Driver list --%>
<jsp:include page="../manager/ext/DriverListSnippet.jsp">
    <jsp:param name="privelege" value="viewOnly" />
</jsp:include>

<jsp:include page="../GlobalFooter.jsp" />