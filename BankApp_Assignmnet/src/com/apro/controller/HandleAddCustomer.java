package com.apro.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.apro.model.DBUtil;

@WebServlet("/HandleAddCustomer")
public class HandleAddCustomer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public HandleAddCustomer() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("handleAddCustomer method called");

		String firstName = request.getParameter("first_name");
		String lastName = request.getParameter("last_name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		System.out.println("Parameters - First Name: " + firstName + ", Last Name: " + lastName + ", Email: " + email);

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
				RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=addAccount");
				dispatcher.forward(request, response);
				return;
			}
		}

		try {
			boolean success = dbUtil.addCustomer(firstName, lastName, email, password);
			if (success) {
				request.setAttribute("message", "Customer added successfully.");
			} else {
				request.setAttribute("message", "Failed to add customer.");
			}
		} catch (SQLException e) {
			// Handle SQL exceptions
			e.printStackTrace();
			request.setAttribute("message", "An error occurred while adding the customer.");
		}

		// Forward to the appropriate view (AdminPanel.jsp)
		RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=addCustomer");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
