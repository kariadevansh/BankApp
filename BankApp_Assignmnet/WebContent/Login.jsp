<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bank Application</title>
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<!-- Custom CSS -->
<style>
body {
	background: linear-gradient(135deg, cyan, violet);  /* Background color for the entire page */
	color: white; /* Text color for contrast */
}

.login-container {
	background-color: #343a40;
	/* Background color for the login container */
	border-radius: 8px;
	padding: 20px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.login-card {
	max-width: 400px;
	margin: 0 auto;
}
</style>
</head>
<body>
	<div
		class="container d-flex justify-content-center align-items-center vh-100">
		<div class="login-card">
			<div class="login-container">
				<h1 class="text-center mb-4">Bank Application</h1>

				<c:if test="${empty param.action || param.action == 'login'}">
					<h2 class="text-center mb-4 text-white">Login</h2>
					<form action="LoginController" method="post">
						<div class="form-group">
							<label for="username" class="text-white">Username</label> <input
								type="text" id="username" name="username" class="form-control"
								required>
						</div>
						<div class="form-group">
							<label for="password" class="text-white">Password</label> <input
								type="password" id="password" name="password"
								class="form-control" required>
						</div>
						<div class="form-group">
							<label for="user_type" class="text-white">User Type</label> <select
								id="user_type" name="user_type" class="form-control">
								<option value="admin">Admin</option>
								<option value="customer">Customer</option>
							</select>
						</div>
						<input type="hidden" name="action" value="login">
						<button type="submit" class="btn btn-primary btn-block">SUBMIT</button>
					</form>
				</c:if>
				<c:if test="${not empty message}">
					<div class="alert alert-success my-3" role="alert">${message}</div>
				</c:if>
			</div>
		</div>

	</div>

	<c:if test="${sessionScope.userType == 'admin'}">
		<jsp:include page="AdminPanel.jsp" />
	</c:if>

	<c:if test="${sessionScope.userType == 'customer'}">
		<jsp:include page="CustomerPanel.jsp" />
	</c:if>



	<!-- Bootstrap JS and dependencies -->
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
	<script
		src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
