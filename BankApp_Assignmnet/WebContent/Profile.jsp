<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Profile</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: #007bff; /* Bootstrap primary color for background */
            color: white; /* Text color for contrast */
        }

        .container {
            background-color: #343a40; /* Dark background for the main content area */
            border-radius: 8px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            max-width: 900px;
            margin: auto;
        }

        .form-group label {
            color: white;
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

        <!-- Display Profile Information -->
        <c:if test="${not empty requestScope.profile}">
            <h3 class="text-center text-white">Profile Information</h3>
            <p class="text-center">First Name: <strong>${requestScope.profile.firstName}</strong></p>
            <p class="text-center">Last Name: <strong>${requestScope.profile.lastName}</strong></p>
            <p class="text-center">Email: <strong>${requestScope.profile.email}</strong></p>

            <!-- Update Profile Form -->
            <form action="${pageContext.request.contextPath}/ProfileUpdate" method="post">
                <h4 class="text-center text-white">Update Profile</h4>
                <div class="form-group">
                    <label for="firstName">First Name:</label>
                    <input type="text" id="firstName" name="firstName" value="${requestScope.profile.firstName}" class="form-control" />
                </div>
                <div class="form-group">
                    <label for="lastName">Last Name:</label>
                    <input type="text" id="lastName" name="lastName" value="${requestScope.profile.lastName}" class="form-control" />
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" class="form-control" 
                    pattern="(?=.*[a-z])(?=.*[A-Z])(?=.*\W).{8,20}" 
					title="Password must be 8-20 characters long, include at least one uppercase letter, one lowercase letter, and one special character." 
					/>
                </div>
                <div class="form-group text-center">
                    <button type="submit" class="btn btn-primary">Update</button>
                    <button type="button" class="btn btn-secondary" onclick="window.location.href='CustomerPanel.jsp?action=viewProfile';">Cancel</button>
                </div>
            </form>
        </c:if>
    </div>

    <!-- Bootstrap JS and dependencies -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
