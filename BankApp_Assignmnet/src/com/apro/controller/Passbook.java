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

import com.apro.entity.Transactions;
import com.apro.model.DBUtil;

@WebServlet("/Passbook")
public class Passbook extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Passbook() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
            request.setAttribute("message", "User ID is missing or invalid.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Retrieve DBUtil from session
        DBUtil dbUtil = (DBUtil) session.getAttribute("dbUtil");

        if (dbUtil == null || !dbUtil.isConnectionValid()) {
            // Attempt to reconnect if DBUtil is null or connection is not valid
            dbUtil = DBUtil.getDBUtil();
            dbUtil.connectToDb();
            session.setAttribute("dbUtil", dbUtil);

            // Check if reconnection was successful
            if (!dbUtil.isConnectionValid()) {
                request.setAttribute("message", "Database connection is not available.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("CustomerPanel.jsp?action=passbook");
                dispatcher.forward(request, response);
                return;
            }
        }

        try {
            List<Transactions> transactions = dbUtil.getAllTransactionsOfCustomer(userId);
            System.out.println("Number of transactions retrieved: " + (transactions != null ? transactions.size() : 0));
            
            // Retrieve the current balance of the user
            double currentBalance = dbUtil.getCurrentBalance(userId);
            System.out.println("Current balance: " + currentBalance);

            request.setAttribute("transactions", transactions);
            request.setAttribute("action", "passbook");
            request.setAttribute("currentBalance", currentBalance);

            RequestDispatcher dispatcher = request.getRequestDispatcher("CustomerPanel.jsp?action=passbook");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            System.err.println("SQL Exception while retrieving transactions.");
            e.printStackTrace();
            request.setAttribute("message", "An error occurred while retrieving transaction data.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
