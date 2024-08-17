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

@WebServlet("/Admin")
public class Admin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Admin() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		System.out.println("inside admin controller get : " + action);

		if ("addCustomer".equals(action)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=addCustomer");
			dispatcher.forward(request, response);
		} else if ("viewCustomersWithoutAccount".equals(action)) {
//			viewCustomersWithoutAccount(request, response);
			RequestDispatcher dispatcher = request.getRequestDispatcher("ViewCustomersWithoutAccount");
			dispatcher.forward(request, response);
		} else if ("addAccount".equals(action)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=addAccount");
			dispatcher.forward(request, response);
		} else if ("viewCustomers".equals(action)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("ViewCustomers");
			dispatcher.forward(request, response);
		} else if ("viewTransactions".equals(action)) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("ViewTransactions");
			dispatcher.forward(request, response);
		} else {
			response.sendRedirect("AdminPanel.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("inside admin controller post " + request.getParameter("action"));
		String action = request.getParameter("action");

		if ("addCustomer".equals(action)) {
			handleAddCustomer(request, response);
//			RequestDispatcher dispatcher = request.getRequestDispatcher("HandleAddCustomer");
//			dispatcher.forward(request, response);
			
		}else if("generateAccountNumber".equals(action)){
			handleAddAccount(request,response);
		}else {
			doGet(request, response);
		}
	}

	private void handleAddCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
	
//	private void viewCustomersWithoutAccount(HttpServletRequest request, HttpServletResponse response)
//	        throws ServletException, IOException {
//	    HttpSession session = request.getSession();
//	    DBUtil dbUtil = (DBUtil) session.getAttribute("dbUtil");
//
//	    // Debug statement to check if the session contains DBUtil
//	    System.out.println("DBUtil retrieved from session: " + dbUtil);
//	    if (dbUtil == null || !dbUtil.isConnectionValid()) {
//	        // Attempt to reconnect if DBUtil is null or connection is not valid
//	        dbUtil = DBUtil.getDBUtil();
//	        dbUtil.connectToDb();
//	        session.setAttribute("dbUtil", dbUtil);
//
//	        // Check if reconnection was successful
//	        if (!dbUtil.isConnectionValid()) {
//	            request.setAttribute("message", "Database connection is not available.");
//	            RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=addAccount");
//	            dispatcher.forward(request, response);
//	            return;
//	        }
//	    }
//
//	    try {
//	        // Debug statement before fetching customers
//	        System.out.println("Attempting to fetch customers without accounts.");
//	        List<Customer> customersWithoutAccount = dbUtil.getCustomersWithoutAccount();
//
//	        // Debug statement to check the size of the fetched list
//	        System.out.println("Number of customers without accounts: " + customersWithoutAccount.size());
//
//	        // Debug statement to output customer details
//	        for (Customer customer : customersWithoutAccount) {
//	            System.out.println("Customer ID: " + customer.getId() + ", Name: " + customer.getFirstName() + " " + customer.getLastName() + ", Email: " + customer.getEmail());
//	        }
//
//	        request.setAttribute("customersWithoutAccount", customersWithoutAccount);
//	        request.setAttribute("action", "customerList");
//	        RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp");
//	        dispatcher.forward(request, response);
//	    } catch (SQLException e) {
//	        e.printStackTrace();  // Print stack trace for SQLException
//	        request.setAttribute("message", "An error occurred while retrieving customer data.");
//	        RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
//	        dispatcher.forward(request, response);
//	    }
//	}
	
	private void handleAddAccount(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
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

	    System.out.println("handleAddAccount method called");

	    // Retrieve parameters
	    String customerId = request.getParameter("customer_id");
	    String initialBalanceStr = request.getParameter("balance");
	    
	    System.out.println("for customer_id :" + customerId);
	    
	    // Validate customerId and initialBalanceStr
	    if (customerId == null || initialBalanceStr == null || customerId.isEmpty() || initialBalanceStr.isEmpty()) {
	        request.setAttribute("message", "Invalid customer ID or initial balance.");
	        RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=addAccount");
	        dispatcher.forward(request, response);
	        return;
	    }

	    double initialBalance;
	    try {
	        initialBalance = Double.parseDouble(initialBalanceStr);
	    } catch (NumberFormatException e) {
	        request.setAttribute("message", "Invalid initial balance format.");
	        RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=addAccount");
	        dispatcher.forward(request, response);
	        return;
	    }

	    try {
	        // Generate account number
	        String accountNumber = dbUtil.generateAccountNumber();
	        System.out.println("Generated account number: " + accountNumber);

	        // Add the account
	        boolean success = dbUtil.addAccount(customerId, accountNumber, initialBalance);
	        if (success) {
	            request.setAttribute("message", "Account added successfully.");
	        } else {
	            request.setAttribute("message", "Failed to add account.");
	        }
	    } catch (SQLException e) {
	        // Handle SQL exceptions
	        e.printStackTrace();
	        request.setAttribute("message", "An error occurred while adding the account.");
	    }

	    // Forward to the appropriate view (AdminPanel.jsp)
	    RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=addAccount");
	    dispatcher.forward(request, response);
	}


}
