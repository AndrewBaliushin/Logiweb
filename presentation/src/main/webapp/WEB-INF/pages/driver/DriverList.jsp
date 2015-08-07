<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="Drivers List" />
	<jsp:param value="common/common.css" name="css" />
	<jsp:param value="common/RemoveRecord.js" name="js" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="homeLink" value="/" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<jsp:include page="ext/DriverListSnippet.jsp">
    <jsp:param name="privelege" value="edit" />
</jsp:include>

<jsp:include page="../GlobalFooter.jsp" />