<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Transaction</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<style>
body {
	background-color: #007bff; /* Bootstrap primary color for background */
	color: white; /* Text color for contrast */
}

.container {
	background-color: #343a40;
	/* Dark background for the main content area */
	border-radius: 8px;
	padding: 20px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
	max-width: 900px; /* Ensure container has a maximum width */
	width: 100%; /* Ensure container adapts to screen width */
	margin: auto;
	min-height: 300px; /* Minimum height to ensure consistent size */
}

.form-group label {
	color: white;
}

.form-control {
	border-radius: 0.25rem;
	/* Bootstrap default border-radius for form controls */
	width: auto; /* Adjust width based on content */
}

.btn-primary {
	background-color: #007bff; /* Bootstrap primary color for buttons */
	border-color: #007bff;
}

.btn-secondary {
	background-color: #6c757d; /* Bootstrap secondary color for buttons */
	border-color: #6c757d;
}
</style>
</head>
<body>
	<div class="container mt-4">
		<h2 class="text-center text-white">Make Transaction</h2>

		<!-- Form to submit transaction details -->
		<form action="${pageContext.request.contextPath}/NewTransaction"
			method="post">
			<div class="form-group">
				<label for="to_account_number">Transfer To Account Number:</label>
				<c:choose>
					<c:when test="${param.transaction_type == 'transfer'}">
						<input type="text" id="to_account_number" name="to_account_number"
							value="${param.to_account_number}" class="form-control" />
					</c:when>
					<c:otherwise>
						<input type="hidden" name="to_account_number" value="" />
					</c:otherwise>
				</c:choose>
			</div>

			<div class="form-group">
				<label for="amount">Amount:</label> <input type="number" id="amount"
					name="amount" value="${param.amount}" class="form-control"
					min="0.01" step="0.01" required />
			</div>

			<input type="hidden" name="transaction_type"
				value="${param.transaction_type}" />

			<div class="form-group text-center">
				<button type="submit" class="btn btn-primary">Submit</button>
				<button type="button" class="btn btn-secondary"
					onclick="window.location.href='CustomerPanel.jsp?action=newTransaction';">Cancel</button>
			</div>
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
