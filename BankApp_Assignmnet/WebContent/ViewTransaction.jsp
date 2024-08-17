<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Transaction List</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: #007bff; /* Bootstrap primary color for background */
            color: white; /* Text color for contrast */
        }
        .logout-button {
            color: white;
            background-color: #007bff; /* Bootstrap primary color for button */
            border-color: #007bff;
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
        .filter-row .form-group {
            margin-bottom: 0;
        }
        .filter-row .form-control {
            width: 100%;
        }
    </style>
</head>
<body>
    <!-- View All Transactions Button -->
    <form action="${pageContext.request.contextPath}/ViewTransactions" method="get">
        <input type="hidden" name="action" value="viewTransactions"/>
        <button type="submit" class="btn btn-primary">View Now</button>
    </form>

    <!-- Filter Form -->
    <form action="${pageContext.request.contextPath}/ViewTransactions" method="get" class="mt-3">
        <input type="hidden" name="action" value="viewTransactions"/>
        <div class="container">
            <div class="row filter-row">
                <div class="col-md-3 form-group">
                    <label for="transactionType">Transaction Type</label>
                    <input type="text" name="transactionType" id="transactionType" class="form-control" value="${param.transactionType}" placeholder="Type">
                </div>
                <div class="col-md-3 form-group">
                    <label for="fromDate">From Date</label>
                    <input type="date" name="fromDate" id="fromDate" class="form-control" value="${param.fromDate}">
                </div>
                <div class="col-md-3 form-group">
                    <label for="toDate">To Date</label>
                    <input type="date" name="toDate" id="toDate" class="form-control" value="${param.toDate}">
                </div>
                <div class="col-md-3 form-group">
                    <label for="accountNumber">Account Number</label>
                    <input type="text" name="accountNumber" id="accountNumber" class="form-control" value="${param.accountNumber}">
                </div>
            </div>
            <div class="form-group text-center my-4">
                <button type="submit" class="btn btn-primary">Filter</button>
            </div>
        </div>
    </form>

    <!-- Display Transactions -->
    <c:if test="${action eq 'viewTransactions'}">
        <c:choose>
            <c:when test="${not empty transactions}">
                <div class="table-responsive mt-3">
                    <table class="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>Transaction ID</th>
                                <th>From Account</th>
                                <th>To Account</th>
                                <th>Transaction Type</th>
                                <th>Amount</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="transaction" items="${transactions}">
                                <tr>
                                    <td><c:out value="${transaction.transactionId}" /></td>
                                    <td><c:out value="${transaction.fromAccount}" /></td>
                                    <td><c:out value="${transaction.toAccount}" /></td>
                                    <td><c:out value="${transaction.transactionType}" /></td>
                                    <td><c:out value="${transaction.amount}" /></td>
                                    <td><c:out value="${transaction.transactionDate}" /></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <p class="text-white">No transactions found.</p>
            </c:otherwise>
        </c:choose>
        
        <!-- Back Button -->
        <div class="text-center mt-3">
            <a class="btn btn-secondary" href="AdminPanel.jsp?action=viewTransactions">Back</a>
        </div>
    </c:if>
</body>
</html>
