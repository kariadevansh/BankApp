<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add Account</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<style>
body {
	background: linear-gradient(135deg, cyan, violet);
	color: white; /* Text color for contrast */
}

.logout-button {
	color: white;
	background-color: #007bff; /* Bootstrap primary color for button */
	border-color: #007bff;
}

.container {
	background-color: #343a40;
	/* Consistent background with AdminPanel.jsp */
	border-radius: 8px;
	padding: 20px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
	max-width: 800px;
	margin: auto;
}

.table {
	background-color: #495057; /* Consistent table background */
	color: white;
}

.table th, .table td {
	text-align: center; /* Center-align text in the table cells */
}

.table-striped tbody tr:nth-of-type(odd) {
	background-color: #6c757d;
	/* Alternating row colors for better readability */
}

.form-group label {
	color: white;
}

.btn-primary {
	background-color: #007bff; /* Consistent button color */
	border-color: #007bff;
}

.form-control {
	width: auto; /* Adjust width based on content */
	display: inline-block;
}
</style>
</head>
<body>
	<div class="container mt-4">
		<!-- Display List of Customers Without Accounts -->
		<c:if test="${not empty customersWithoutAccount}">
			<h3 class="text-center text-white">Customers Without Accounts</h3>
			<div class="table-responsive">
				<table class="table table-striped table-bordered">
					<thead>
						<tr>
							<th>ID</th>
							<th>First Name</th>
							<th>Last Name</th>
							<th>Email</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="customer" items="${customersWithoutAccount}">
							<tr>
								<td><c:out value="${customer.id}" /></td>
								<td><c:out value="${customer.firstName}" /></td>
								<td><c:out value="${customer.lastName}" /></td>
								<td><c:out value="${customer.email}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>

		<c:if test="${empty customersWithoutAccount}">
			<p class="text-center text-white">No customers found without
				accounts.</p>
		</c:if>

		<!-- Form to Create New Account -->
		<h3 class="text-center text-white">Create New Account</h3>
		<form action="${pageContext.request.contextPath}/Admin" method="post">
			<input type="hidden" name="action" value="generateAccountNumber">
			<div class="form-group">
				<label for="customer_id">Customer ID:</label> <select
					id="customer_id" name="customer_id" class="form-control" required>
					<c:forEach var="customer" items="${customersWithoutAccount}">
						<option value="${customer.id}">${customer.firstName}
							${customer.lastName} (${customer.email})</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label for="balance">Initial Balance:</label> <input type="number"
					id="balance" name="balance" class="form-control" min="1000"
					title="min balance should be more than 1000" required>
			</div>
			<button type="submit" class="btn btn-primary">Generate
				Account Number</button>
			<a class="btn btn-secondary" href="AdminPanel.jsp">Back</a>
		</form>
	</div>

	<!-- Bootstrap JS and dependencies -->
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
