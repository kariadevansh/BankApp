package com.apro.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class AccountProcessor {
    private Connection connection;

    public AccountProcessor(Connection connection) {
        this.connection = connection;
    }
    
    
    public String generateAccountNumber() throws SQLException {
        final int ACCOUNT_NUMBER_LENGTH = 10;
        String accountNumber;
        boolean isUnique;

        do {
            accountNumber = generateRandomNumber(ACCOUNT_NUMBER_LENGTH);
            isUnique = checkAccountNumberUnique(accountNumber);
        } while (!isUnique);

        return accountNumber;
    }

    private String generateRandomNumber(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Generate the number
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    private boolean checkAccountNumberUnique(String accountNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, accountNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // If the count is greater than 0, the account number is not unique
                    return resultSet.getInt(1) == 0;
                }
            }
        }

        return false;
    }
    
    
    public boolean addAccount(String customerId, String accountNumber, double initialBalance) throws SQLException {
		boolean success = false;
		String insertAccountQuery = "INSERT INTO accounts (account_number, customer_id, balance) VALUES (?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(insertAccountQuery)) {
			preparedStatement.setString(1, accountNumber);
			preparedStatement.setInt(2, Integer.parseInt(customerId));
			preparedStatement.setDouble(3, initialBalance); // Set the initial balance
			int rowsAffected = preparedStatement.executeUpdate();

			success = rowsAffected > 0;
		}

		return success;
	}
}

