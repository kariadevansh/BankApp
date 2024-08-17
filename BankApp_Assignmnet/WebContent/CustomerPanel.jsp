<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Customer Panel</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<!-- Custom CSS -->
<style>
body {
	background: linear-gradient(135deg, cyan, violet);
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

/* Center button inside form */
.center-button {
	display: flex;
	justify-content: center;
	margin-top: 20px; /* Optional: Adds some space above the button */
}
</style>
</head>
<body>
	<c:choose>
		<c:when test="${sessionScope.userType == 'customer'}">
			<!-- Navbar -->
			<nav class="navbar navbar-expand-lg navbar-dark">
				<a class="navbar-brand" href="CustomerPanel.jsp">Bank App</a>
				<button class="navbar-toggler" type="button" data-toggle="collapse"
					data-target="#navbarNav" aria-controls="navbarNav"
					aria-expanded="false" aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>
				<div class="collapse navbar-collapse" id="navbarNav">
					<ul class="navbar-nav">
						<li class="nav-item"><a class="nav-link"
							href="CustomerPanel.jsp?action=passbook">Passbook</a></li>
						<li class="nav-item"><a class="nav-link"
							href="CustomerPanel.jsp?action=newTransaction">New
								Transaction</a></li>
						<li class="nav-item"><a class="nav-link"
							href="CustomerPanel.jsp?action=viewProfile">Profile</a></li>
					</ul>
					<form class="form-inline ml-auto"
						action="${pageContext.request.contextPath}/LogoutController"
						method="post">
						<button type="submit" class="btn btn-primary">Logout</button>
					</form>
				</div>
			</nav>

			<!-- Main Content -->
			<div class="container mt-4">
				<h3 class="text-center">Welcome to Customer Panel: ${sessionScope.username}</h3>

				<!-- Placeholder for dynamic content -->
				<div id="content">
					<c:choose>
						<c:when test="${param.action == 'passbook'}">
							<jsp:include page="Passbook.jsp" />
							<!-- Center the "View Now" button -->
							<div class="center-button">
								<form action="${pageContext.request.contextPath}/Passbook" method="get">
									<button type="submit" class="btn btn-primary">View Now</button>
								</form>
							</div>
						</c:when>

						<c:when test="${param.action == 'newTransaction'}">
							<!-- Dropdown to select transaction type -->
							<div class="dropdown-container">
								<form action="${pageContext.request.contextPath}/NewTransaction" method="get">
									<div class="form-group">
										<label for="transaction_type">Transaction Type</label>
										<select name="transaction_type" id="transaction_type" class="form-control" onchange="this.form.submit()">
											<option value="" <c:if test="${empty param.transaction_type}">selected</c:if>>Select Type</option>
											<option value="credit" <c:if test="${param.transaction_type == 'credit'}">selected</c:if>>Credit</option>
											<option value="debit" <c:if test="${param.transaction_type == 'debit'}">selected</c:if>>Debit</option>
											<option value="transfer" <c:if test="${param.transaction_type == 'transfer'}">selected</c:if>>Transfer</option>
										</select>
									</div>
								</form>
							</div>

							<c:if test="${not empty param.transaction_type}">
								<jsp:include page="NewTransaction.jsp">
									<jsp:param name="transactionType" value="${param.transaction_type}" />
								</jsp:include>
							</c:if>
						</c:when>
						<c:when test="${param.action == 'transactionComplete'}">
							<div class="alert alert-success" role="alert">Transaction completed successfully!</div>
						</c:when>
						<c:when test="${param.action == 'transactionFailed'}">
							<div class="alert alert-danger" role="alert">Transaction failed!</div>
						</c:when>

						<c:when test="${param.action == 'viewProfile'}">
							<!-- View Profile Button -->
							<jsp:include page="Profile.jsp" />
							<!-- Center the "View Profile" button -->
							<div class="center-button">
								<form action="${pageContext.request.contextPath}/ProfileUpdate" method="get">
									<button type="submit" class="btn btn-primary">View Profile</button>
								</form>
							</div>
						</c:when>
					</c:choose>
				</div>
			</div>
		</c:when>
	</c:choose>

	<!-- Bootstrap JS and dependencies -->
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
