<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add New Customer</title>
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
	/* Consistent background color for the main content area */
	border-radius: 8px;
	padding: 20px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
	max-width: 600px;
	margin: auto;
}

.form-group label {
	color: white; /* Ensure labels are visible */
}

.btn-primary {
	background-color: #007bff; /* Consistent button color */
	border-color: #007bff;
}

.btn-secondary {
	background-color: #6c757d; /* Consistent color for the cancel button */
	border-color: #6c757d;
}
</style>
</head>
<body>
	<div class="container mt-4">
		<form action="${pageContext.request.contextPath}/Admin" method="post">
			<input type="hidden" name="action" value="addCustomer">
			<div class="form-group">
				<label for="first_name">First Name:</label> <input type="text"
					id="first_name" name="first_name" class="form-control"
					pattern="[A-Za-z]+" title="name must only contain letters."
					required>
			</div>
			<div class="form-group">
				<label for="last_name">Last Name:</label> <input type="text"
					id="last_name" name="last_name" class="form-control"
					pattern="[A-Za-z]+" title="name must only contain letters."
					required>
			</div>
			<div class="form-group">
				<label for="email">Email:</label> <input type="email" id="email"
					name="email" class="form-control" required>
			</div>
			<div class="form-group">
				<label for="password">Password:</label> <input type="password"
					id="password" name="password" class="form-control"
					pattern="(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}"
					title="Password must be 8-20 characters long, include at least one uppercase letter, one lowercase letter, and one special character."
					required>
			</div>
			<div class="form-group text-center">
				<button type="submit" class="btn btn-primary">Submit</button>
				<button type="reset" class="btn btn-secondary">Cancel</button>
				<a class="btn btn-secondary" href="AdminPanel.jsp">Back</a>
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
