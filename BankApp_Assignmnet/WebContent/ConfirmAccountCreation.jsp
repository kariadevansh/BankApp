<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.apro.entity.Customer" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirm Account Creation</title>
</head>
<body>
    <h1>Confirm Account Creation</h1>
    
    <%-- Retrieve the customer and account number from the request --%>
    <%
        Customer customer = (Customer) request.getAttribute("customer");
        String accountNumber = (String) request.getAttribute("accountNumber");
        String balanceStr = (String) request.getAttribute("balance");
    %>

    <form action="AdminController" method="post">
        <input type="hidden" name="action" value="addAccount">
        <input type="hidden" name="customerId" value="<%= customer.getId() %>">
        <input type="hidden" name="accountNumber" value="<%= accountNumber %>">

        <h2>Customer Details</h2>
        <p><strong>Name:</strong> <%= customer.getFirstName() %> <%= customer.getLastName() %></p>
        <p><strong>Email:</strong> <%= customer.getEmail() %></p>

        <h2>Account Details</h2>
        <p><strong>Generated Account Number:</strong> <%= accountNumber %></p>

        <label for="balance">Initial Balance:</label>
        <input type="text" id="balance" name="balance" value="<%= balanceStr %>" required>
        
        <button type="submit">Confirm Account Creation</button>
    </form>

    <%-- Display a message if available --%>
    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
            out.println("<p>" + message + "</p>");
        }
    %>
</body>
</html>
