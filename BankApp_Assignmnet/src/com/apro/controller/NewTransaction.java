package com.apro.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.apro.model.DBUtil;

@WebServlet("/NewTransaction")
public class NewTransaction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public NewTransaction() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String transactionType = request.getParameter("transaction_type");

		// Set the transaction type in the request scope
		request.setAttribute("transactionType", transactionType);

		// Forward to CustomerPanel.jsp with the action newTransaction
		request.getRequestDispatcher("CustomerPanel.jsp?action=newTransaction").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object userIdObject = session.getAttribute("user_id");
		String userId = null;

		if (userIdObject instanceof String) {
			userId = (String) userIdObject;
		} else if (userIdObject instanceof Integer) {
			userId = ((Integer) userIdObject).toString();
		}

		// Debugging: Check if userId is retrieved correctly
		System.out.println("Retrieved user_id: " + userId);
		if (userId == null || userId.isEmpty()) {
			request.setAttribute("message", "User not logged in");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

		String transactionType = request.getParameter("transaction_type");
		String amountStr = request.getParameter("amount");
		String toAccountNumber = request.getParameter("to_account_number");

		if (transactionType == null || amountStr == null || amountStr.isEmpty()) {
			request.setAttribute("message", "Transaction details are missing");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

		double amountValue;
		try {
			amountValue = Double.parseDouble(amountStr);
		} catch (NumberFormatException e) {
			request.setAttribute("message", "Invalid amount");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

		// Process the transaction and handle redirection
		boolean success = processTransaction(request, response, userId, transactionType, toAccountNumber, amountValue);

		if (success) {
			// Redirect to avoid response commit issues
			request.getRequestDispatcher("CustomerPanel.jsp?action=transactionComplete").forward(request, response);
		} else {
			// Handle failure by forwarding to the error page
			request.setAttribute("message", "Transaction failed");
			request.getRequestDispatcher("CustomerPanel.jsp?action=transactionFailed").forward(request, response);
		}
	}

	private boolean processTransaction(HttpServletRequest request, HttpServletResponse response, String userId,
			String transactionType, String toAccountNumber, double amount) throws ServletException, IOException {
		boolean processSuccess = false;
		System.out.println("processTransaction method called");

		// Retrieve DBUtil from session
		HttpSession session = request.getSession();
		DBUtil dbUtil = (DBUtil) session.getAttribute("dbUtil");

		if (dbUtil == null || !dbUtil.isConnectionValid()) {
			// Attempt to reconnect if DBUtil is null or connection is not valid
			dbUtil = DBUtil.getDBUtil();
			dbUtil.connectToDb();
			session.setAttribute("dbUtil", dbUtil);

			// Check if reconnection was successful
			if (!dbUtil.isConnectionValid()) {
				request.setAttribute("message", "Database connection is not available.");
				return processSuccess;
			}
		}

		try {
			// Call the DBUtil method to process the transaction
			processSuccess = dbUtil.processTransaction(userId, transactionType, toAccountNumber, amount);
			if (processSuccess) {
				System.out.println("Transaction processed successfully.");
			} else {
				System.out.println("Failed to process transaction.");
			}
		} catch (SQLException e) {
			// Handle SQL exceptions
			e.printStackTrace();
			request.setAttribute("message", "An error occurred while processing the transaction.");
		}

		return processSuccess;
	}

}