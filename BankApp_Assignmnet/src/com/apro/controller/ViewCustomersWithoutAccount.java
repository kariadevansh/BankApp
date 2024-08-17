package com.apro.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.apro.entity.Customer;
import com.apro.model.DBUtil;

@WebServlet("/ViewCustomersWithoutAccount")
public class ViewCustomersWithoutAccount extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ViewCustomersWithoutAccount() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DBUtil dbUtil = (DBUtil) session.getAttribute("dbUtil");

        // Debug statement to check if the session contains DBUtil
        System.out.println("DBUtil retrieved from session: " + dbUtil);

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
            // Debug statement before fetching customers
            System.out.println("Attempting to fetch customers without accounts.");
            List<Customer> customersWithoutAccount = dbUtil.getCustomersWithoutAccount();

            // Debug statement to check the size of the fetched list
            System.out.println("Number of customers without accounts: " + customersWithoutAccount.size());

            // Debug statement to output customer details
            for (Customer customer : customersWithoutAccount) {
                System.out.println("Customer ID: " + customer.getId() + ", Name: " + customer.getFirstName() + " " + customer.getLastName() + ", Email: " + customer.getEmail());
            }

            request.setAttribute("customersWithoutAccount", customersWithoutAccount);
            RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=addAccount");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace for SQLException
            request.setAttribute("message", "An error occurred while retrieving customer data.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
