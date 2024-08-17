<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Passbook</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<style>
body {
	background-color: #007bff; /* Bootstrap primary color */
	color: white; /* Text color for contrast */
}

.navbar {
	background-color: #343a40; /* Dark background for the navbar */
}

.navbar-nav .nav-link {
	color: white !important; /* White text color for the nav links */
}

.navbar-brand {
	color: white !important; /* White text color for the brand name */
}

.btn-primary {
	background-color: #007bff; /* Bootstrap primary color for buttons */
	border-color: #007bff;
}

.container {
	background-color: #343a40;
	/* Dark background for the main content area */
	border-radius: 18px;
	max-width: 900px;
	padding: 20px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.form-group label {
	color: white; /* White color for form labels */
}

.form-control {
	border-radius: 0.25rem; /* Rounded corners for input fields */
}

.dropdown-container {
	max-width: 400px; /* Adjust as needed */
	margin: auto; /* Center the container */
}

.table {
	background-color: #495057;
	/* Slightly lighter background for the table */
	color: white;
}

.table th, .table td {
	text-align: center; /* Center-align text in the table cells */
}

.table-striped tbody tr:nth-of-type(odd) {
	background-color: #6c757d;
	/* Alternating row colors for better readability */
}
</style>

</head>
<body>
	<div class="container mt-4">
		<c:if test="${action eq 'passbook'}">
			<c:if test="${not empty transactions}">
				<!-- Display Current Balance -->
				<div class="balance-section">
					<p class="text-white">Current Balance: ${currentBalance}</p>
				</div>
				<div class="table-responsive">
					<table class="table table-striped table-bordered">
						<thead>
							<tr>
								<th>Receiver Account No</th>
								<th>Transaction Type</th>
								<th>Amount</th>
								<th>Date</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="transaction" items="${transactions}">
								<tr>
									<td><c:choose>
											<c:when test="${transaction.toAccount eq 0}">
												<c:out value="SELF" />
											</c:when>
											<c:otherwise>
												<c:out value="${transaction.toAccount}" />
											</c:otherwise>
										</c:choose></td>
									<td><c:out value="${transaction.transactionType}" /></td>
									<td><c:out value="${transaction.amount}" /></td>
									<td><c:out value="${transaction.transactionDate}" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>

			<c:if test="${empty transactions}">
				<p class="text-center text-white">No transactions found.</p>
			</c:if>
			<!-- Back Button -->
			<div class="text-center mt-3">
				<a class="btn btn-secondary"
					href="CustomerPanel.jsp?action=passbook">Back</a>
			</div>
		</c:if>


	</div>
</body>
</html>
