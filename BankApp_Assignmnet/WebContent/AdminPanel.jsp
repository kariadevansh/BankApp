<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Panel</title>
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

.logout-button {
	color: white;
	background-color: #007bff; /* Bootstrap primary color for button */
	border-color: #007bff;
}

.container {
	background-color: #343a40;
	/* Dark background for the main content area */
	border-radius: 8px;
	padding: 20px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.btn-primary {
	background-color: #007bff; /* Bootstrap primary color for buttons */
	border-color: #007bff;
}
</style>
</head>
<body>

	<c:choose>
		<c:when test="${sessionScope.userType == 'admin'}">
			<!-- Navbar -->
			<nav class="navbar navbar-expand-lg navbar-dark">
				<a class="navbar-brand" href="AdminPanel.jsp">Bank App</a>
				<button class="navbar-toggler" type="button" data-toggle="collapse"
					data-target="#navbarNav" aria-controls="navbarNav"
					aria-expanded="false" aria-label="Toggle navigation">
					<span class="navbar-toggler-icon"></span>
				</button>
				<div class="collapse navbar-collapse" id="navbarNav">
					<ul class="navbar-nav">
						<li class="nav-item"><a class="nav-link"
							href="AdminPanel.jsp?action=addCustomer">Add New Customer</a></li>
						<li class="nav-item"><a class="nav-link"
							href="AdminPanel.jsp?action=addAccount">Add Bank Account</a></li>
						<li class="nav-item"><a class="nav-link"
							href="AdminPanel.jsp?action=viewCustomers">View Customers</a></li>
						<li class="nav-item"><a class="nav-link"
							href="AdminPanel.jsp?action=viewTransactions">View
								Transactions</a></li>
					</ul>
					<form class="form-inline ml-auto"
						action="${pageContext.request.contextPath}/LogoutController"
						method="post">
						<button type="submit" class="btn logout-button">Logout</button>
					</form>
				</div>
			</nav>

			<!-- Main Content -->

			<div class="container mt-4">
				<h3 class="text-center">Welcome to Admin Panel: ${sessionScope.username}</h3>
				<!-- Placeholder for dynamic content -->
				<div id="content">
					<c:choose>
						<c:when test="${param.action == 'addCustomer'}">
							<jsp:include page="AddCustomer.jsp" />
						</c:when>
						<c:when test="${param.action == 'addAccount'}">
							<!-- Form to Fetch Customers Without Accounts -->
							<div class="d-flex justify-content-center">
								<form action="${pageContext.request.contextPath}/ViewCustomersWithoutAccount" method="get">
									<input type="hidden" name="action" value="viewCustomersWithoutAccount" />
									<button type="submit" class="btn btn-primary">Fetch Customers Without Accounts</button>
								</form>
							</div>
							<jsp:include page="AddAccount.jsp" />
						</c:when>
						<c:when test="${param.action == 'viewCustomers'}">
							<jsp:include page="ViewCustomers.jsp" />
						</c:when>
						<c:when test="${param.action == 'viewTransactions'}">
							<jsp:include page="ViewTransaction.jsp" />
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
