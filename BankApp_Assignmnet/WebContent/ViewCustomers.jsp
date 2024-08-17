<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Customer List</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<style>
body {
    background-color: #007bff;
    color: white;
}

.logout-button {
    color: white;
    background-color: #007bff;
    border-color: #007bff;
}

.table {
    background-color: #495057;
    color: white;
}

.table th, .table td {
    text-align: center;
}

.table-striped tbody tr:nth-of-type(odd) {
    background-color: #6c757d;
}
</style>
</head>
<body>
    <!-- View Now Button -->
    <form action="${pageContext.request.contextPath}/ViewCustomers" method="get">
    <input type="hidden" name="action" value="viewCustomers"/>
        <button type="submit" class="btn btn-primary">View Now</button>
    </form>

    <!-- Search Form -->
    <form action="${pageContext.request.contextPath}/ViewCustomers" method="get" class="mt-3">
    <input type="hidden" name="action" value="viewCustomers"/>
        <div class="form-group">
            <input type="text" name="search" class="form-control" placeholder="Search by First Name, Last Name, Email, or Account Number" value="${param.search}">
        </div>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>

    <c:if test="${action eq 'viewCustomers'}">
        <c:choose>
            <c:when test="${not empty customers}">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>First Name</th>
                                <th>Last Name</th>
                                <th>Email</th>
                                <th>Account Number</th>
                                <th>Balance</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="customer" items="${customers}">
                                <c:forEach var="account" items="${customer.accounts}">
                                    <tr>
                                        <td><c:out value="${customer.id}" /></td>
                                        <td><c:out value="${customer.firstName}" /></td>
                                        <td><c:out value="${customer.lastName}" /></td>
                                        <td><c:out value="${customer.email}" /></td>
                                        <td><c:out value="${account.accountNumber}" /></td>
                                        <td><c:out value="${account.balance}" /></td>
                                    </tr>
                                </c:forEach>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <p class="text-white">No customers found.</p>
            </c:otherwise>
        </c:choose>

		<!-- Back Button -->
		<div class="text-center mt-3">
			<a class="btn btn-secondary"
				href="AdminPanel.jsp?action=viewCustomers">Back</a>
		</div>
	</c:if>
</body>
</html>
