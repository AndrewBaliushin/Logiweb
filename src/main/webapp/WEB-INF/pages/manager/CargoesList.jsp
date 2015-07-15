<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="../GlobalHeader.jsp">
	<jsp:param name="title" value="Cargoes List" />
	<jsp:param value="manager/manager.css" name="css" />
</jsp:include>

<jsp:include page="../GlobalHeaderMenu.jsp">
	<jsp:param name="homeLink" value="/manager" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<div class="panel panel-default">
	<div class="panel-heading">
		<h1>List of cargoes</h1>
	</div>
	<div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Cargo ID</th>
					<th>Status</th>
					<th>Title</th>
					<th>Weight <small>x1000kg</small></th>
					<th>Origin city</th>
					<th>Destination</th>
					<th class="text-center">Order</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${cargoes}" var="cargo">
					<tr>
					   <td>${cargo.id}</td>
					   <td>${cargo.status}</td>
					   <td>${cargo.title}</td>
					   <td>${cargo.weight}</td>
					   <td>${cargo.originCity.name}</td>
					   <td>${cargo.destinationCity.name}</td>

						<td class="text-center"><a href="
                            <c:url value="editOrder">
                                <c:param name="orderId" value="${cargo.orderForThisCargo.id}" />
                            </c:url>">${cargo.orderForThisCargo.id}</a>
                        </td>

					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../GlobalFooter.jsp" />