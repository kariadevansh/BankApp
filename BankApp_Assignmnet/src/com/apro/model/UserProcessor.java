package com.apro.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.apro.entity.Users;

public class UserProcessor {
    private Connection connection;
	private PreparedStatement prepareStatement = null;
	private ResultSet result = null;
    
    public UserProcessor(Connection connection) {
        this.connection = connection;
    }

    public Users validateCredentials(String username, String password, String userType) {
		Users currentUser = null;
		String query = "SELECT * FROM users WHERE username = ? AND password = ? AND user_type=?";
		try {
			prepareStatement = connection.prepareStatement(query);
			prepareStatement.setString(1, username);
			prepareStatement.setString(2, password);
			prepareStatement.setString(3, userType);
			result = prepareStatement.executeQuery();
			
			if (result.next()) {
				int user_id =result.getInt("user_id");
				currentUser = new Users(user_id,username,userType);
				return currentUser; // User found
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return currentUser; // User not found
	}

    public String getUserType(String username) {
		String query = "SELECT user_type FROM users WHERE username = ?";
		try {
			prepareStatement = connection.prepareStatement(query);
			prepareStatement.setString(1, username);
			result = prepareStatement.executeQuery();
			if (result.next()) {
				return result.getString("user_type");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; // User type not found
	}

    
}
