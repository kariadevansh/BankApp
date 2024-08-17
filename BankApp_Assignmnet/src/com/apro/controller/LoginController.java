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

import com.apro.entity.Users;
import com.apro.model.DBUtil;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            DBUtil dbUtil = DBUtil.getDBUtil();
            dbUtil.connectToDb();

            try {
                boolean success = handleLogin(request, dbUtil);
                if (success) {
                    HttpSession session = request.getSession();
                    String userType = (String) session.getAttribute("userType");

                    // Store DBUtil instance in the session
                    session.setAttribute("dbUtil", dbUtil);
//                    session.setAttribute("user_id", user_id);
                    if ("admin".equals(userType)) {
//                        response.sendRedirect("Login.jsp?action=admin");
                    	response.sendRedirect("AdminPanel.jsp");
                    } else if ("customer".equals(userType)) {
//                        response.sendRedirect("Login.jsp?action=customer");
                    	response.sendRedirect("CustomerPanel.jsp");
                    }
                } else {
                    request.setAttribute("message", "Invalid username or password.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("Login.jsp");
                    dispatcher.forward(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("message", "An error occurred.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("Login.jsp");
                dispatcher.forward(request, response);
            }
        } else {
            response.sendRedirect("Login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean handleLogin(HttpServletRequest request, DBUtil dbUtil)
            throws SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = request.getParameter("user_type");
        
        Users currentUser = dbUtil.validateCredentials(username, password, userType);
        if (currentUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user_id", currentUser.getUser_id());
            session.setAttribute("username", currentUser.getUsername());
            session.setAttribute("userType", currentUser.getUser_type());
            return true;
        } else {
            return false;
        }
    }
}
