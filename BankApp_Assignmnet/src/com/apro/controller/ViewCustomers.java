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

@WebServlet("/ViewCustomers")
public class ViewCustomers extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve DBUtil from session
        HttpSession session = request.getSession();
        DBUtil dbUtil = (DBUtil) session.getAttribute("dbUtil");

        // Debugging: Check if DBUtil is retrieved from the session
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
        } else {
            System.out.println("DBUtil successfully retrieved from session.");
        }

        // Get search parameter from the request
        String searchQuery = request.getParameter("search");

        try {
            List<Customer> customers;
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                // No search query, fetch all customers
                customers = dbUtil.getAllCustomers();  
            } else {
                // Search query provided, fetch filtered customers
                customers = dbUtil.searchCustomers(searchQuery);  
            }

            // Debugging: Check the size of the customer list
            System.out.println("Number of customers retrieved: " + (customers != null ? customers.size() : 0));

            // Set the customers list as request attribute
            request.setAttribute("customers", customers);

            // Set action attribute to indicate success
            request.setAttribute("action", "viewCustomers");

            // Forward to JSP
            RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPanel.jsp?action=viewCustomers");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("message", "An error occurred while retrieving customer data.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
