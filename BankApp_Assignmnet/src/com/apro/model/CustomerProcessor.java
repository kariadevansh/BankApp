package com.apro.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.apro.entity.Account;
import com.apro.entity.Customer;

public class CustomerProcessor {
    private Connection connection;
    private PreparedStatement prepareStatement = null;
	private ResultSet result = null;

    public CustomerProcessor(Connection connection) {
        this.connection = connection;
    }

    public boolean addCustomer(String firstName, String lastName, String email, String password) throws SQLException {
		// Ensure the connection is not null and handle transaction management
		if (connection == null) {
			throw new SQLException("Database connection is not available.");
		}

		boolean success = false;
		try {
			// Start a transaction
			connection.setAutoCommit(false);
			System.out.println("Attempting to add customer");

			// Insert into users table and retrieve generated keys
			prepareStatement = connection.prepareStatement(
					"INSERT INTO users (username, password, user_type) VALUES (?, ?, 'customer')",
					Statement.RETURN_GENERATED_KEYS);
			prepareStatement.setString(1, email);
			prepareStatement.setString(2, password);
			int rowsAffected = prepareStatement.executeUpdate();
			System.out.println("Rows affected: " + rowsAffected);

			if (rowsAffected == 0) {
				throw new SQLException("Inserting user failed, no rows affected.");
			}
			// Retrieve the generated user_id
			result = prepareStatement.getGeneratedKeys();
			if (result.next()) {
				int userId = result.getInt(1);
				System.out.println("Generated user_id: " + userId);
				// Insert into customers table
				try (PreparedStatement insertCustomerStmt = connection.prepareStatement(
						"INSERT INTO customers (first_name, last_name, email, password, user_id)  VALUES (?, ?, ?, ?, ?)")) {
					insertCustomerStmt.setString(1, firstName);
					insertCustomerStmt.setString(2, lastName);
					insertCustomerStmt.setString(3, email);
					insertCustomerStmt.setString(4, password);
					insertCustomerStmt.setInt(5, userId);
					int rowsInserted = insertCustomerStmt.executeUpdate();

					// Check if the customer was successfully added
					if (rowsInserted > 0) {
						success = true;
						System.out.println("customer added successfully");
					}
				}
			} else {
				throw new SQLException("User ID retrieval failed.");
			}
			// Commit the transaction
			connection.commit();
		} catch (SQLException e) {
			// Roll back the transaction in case of an error
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException rollbackEx) {
					rollbackEx.printStackTrace();
				}
			}
			e.printStackTrace();
			throw new SQLException("Error adding customer", e);
		} finally {
			// Restore auto-commit mode
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException autoCommitEx) {
					autoCommitEx.printStackTrace();
				}
			}
		}
		return success;
	}


    public List<Customer> getAllCustomers() throws SQLException {
		List<Customer> customers = new ArrayList<>();

		prepareStatement = connection.prepareStatement("SELECT * FROM customers");
		result = prepareStatement.executeQuery();

		while (result.next()) {
			int id = result.getInt("customer_id");
			String firstName = result.getString("first_name");
			String lastName = result.getString("last_name");
			String email = result.getString("email");
			String password = result.getString("password");

			PreparedStatement accountStmt = connection.prepareStatement("SELECT * FROM accounts WHERE customer_id = ?");
			accountStmt.setInt(1, id);
			ResultSet accountRs = accountStmt.executeQuery();

			List<Account> accounts = new ArrayList<>();
			while (accountRs.next()) {
				long accountNumber = accountRs.getLong("account_number");
				double balance = accountRs.getDouble("balance");
				accounts.add(new Account(accountNumber, balance));
			}

			Customer customer = new Customer(id, firstName, lastName, email, password);
			customer.setAccounts(accounts); // Ensure accounts are set
			customers.add(customer);
		}

		return customers;
	}
	

 

    public Customer getCustomerById(String customerId) throws SQLException {
		Customer customer = null;
		String query = "SELECT * FROM customers WHERE id = ?";

		try {
			prepareStatement = connection.prepareStatement(query);
			// Set the customerId parameter
			prepareStatement.setInt(1, Integer.parseInt(customerId));

			// Execute the query

			result = prepareStatement.executeQuery();
			if (result.next()) {
				// Create a Customer object from the result set
				customer = new Customer(result.getInt("id"), result.getString("first_name"),
						result.getString("last_name"), result.getString("email"), result.getString("password"));
			}

		} catch (NumberFormatException e) {
			// Handle invalid customerId format
			e.printStackTrace();
			throw new SQLException("Invalid customer ID format", e);
		} catch (SQLException e) {
			// Handle SQL exceptions
			e.printStackTrace();
			throw new SQLException("Error retrieving customer by ID", e);
		}

		return customer;
	}

}
