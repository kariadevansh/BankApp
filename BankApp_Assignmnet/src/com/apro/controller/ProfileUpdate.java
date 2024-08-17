package com.apro.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.apro.entity.Profile;
import com.apro.model.DBUtil;

@WebServlet("/ProfileUpdate")
public class ProfileUpdate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProfileUpdate() {
        super();
    }

    @Override
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
        if (userId == null) {
            response.sendRedirect("error.jsp?message=User not logged in");
            return;
        }

        // Retrieve user profile from DB
        DBUtil dbUtil = (DBUtil) session.getAttribute("dbUtil");
        if (dbUtil == null || !dbUtil.isConnectionValid()) {
            dbUtil = DBUtil.getDBUtil();
            dbUtil.connectToDb();
            session.setAttribute("dbUtil", dbUtil);
        }

        try {
            // Fetch profile details
            Profile profile = dbUtil.getProfileByUserId(userId);
            request.setAttribute("profile", profile);
            request.getRequestDispatcher("CustomerPanel.jsp?action=viewProfile").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Failed to load profile");
        }
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
        if (userId == null) {
            response.sendRedirect("error.jsp?message=User not logged in");
            return;
        }

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("password");

        DBUtil dbUtil = (DBUtil) session.getAttribute("dbUtil");
        if (dbUtil == null || !dbUtil.isConnectionValid()) {
            dbUtil = DBUtil.getDBUtil();
            dbUtil.connectToDb();
            session.setAttribute("dbUtil", dbUtil);
        }

        try {
            // Update profile details
            dbUtil.updateProfile(userId, firstName, lastName, password);
            response.sendRedirect("CustomerPanel.jsp?action=viewProfile");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=Failed to update profile");
        }
    }
}
