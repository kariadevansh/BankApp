package com.apro.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.apro.entity.Account;
import com.apro.entity.Customer;
import com.apro.entity.Profile;
import com.apro.entity.Transactions;
import com.apro.entity.Users;

public class DBUtil {
	private Connection connection = null;
	private PreparedStatement prepareStatement = null;
	private Statement statement = null;
	private ResultSet result = null;
	private static DBUtil dbUtil = null;

	private DBUtil() {
	}

	public static DBUtil getDBUtil() {
		if (dbUtil == null) {
			dbUtil = new DBUtil();
		}
		return dbUtil;
	}

	public boolean isConnectionValid() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public void connectToDb() {
		try {
			// 1. register driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// 2. create connection
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "root");
			System.out.println("Connection successful");

			// 3. create statement of connection
			statement = connection.createStatement();

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Users validateCredentials(String username, String password, String userType) throws SQLException {
		UserProcessor userProcessor = new UserProcessor(connection);
		return userProcessor.validateCredentials(username, password, userType);
	}

	public String getUserType(String username) throws SQLException {
		UserProcessor userProcessor = new UserProcessor(connection);
		return userProcessor.getUserType(username);
	}

	//Customer Processes
	public boolean addCustomer(String firstName, String lastName, String email, String password) throws SQLException {
		CustomerProcessor customerProcessor = new CustomerProcessor(connection);
		return customerProcessor.addCustomer(firstName, lastName, email, password);
	}

	public List<Customer> getAllCustomers() throws SQLException {
		CustomerProcessor customerProcessor = new CustomerProcessor(connection);
		return customerProcessor.getAllCustomers();
	}

	public List<Customer> searchCustomers(String searchQuery) throws SQLException {
		List<Customer> customers = new ArrayList<>();
		Connection connection = getConnection();

		// SQL query to join customers and accounts
		String sql = "SELECT c.customer_id, c.first_name, c.last_name, c.email, c.password, "
				+ "a.account_number, a.balance " + "FROM Customers c "
				+ "LEFT JOIN Accounts a ON c.customer_id = a.customer_id "
				+ "WHERE c.first_name LIKE ? OR c.last_name LIKE ? OR c.email LIKE ? OR a.account_number LIKE ?";

		PreparedStatement statement = connection.prepareStatement(sql);
		String searchPattern = "%" + searchQuery + "%";
		statement.setString(1, searchPattern);
		statement.setString(2, searchPattern);
		statement.setString(3, searchPattern);
		statement.setString(4, searchPattern);

		ResultSet resultSet = statement.executeQuery();

		List<Customer> tempCustomerList = new ArrayList<>();

		while (resultSet.next()) {
			int id = resultSet.getInt("customer_id");
			String firstName = resultSet.getString("first_name");
			String lastName = resultSet.getString("last_name");
			String email = resultSet.getString("email");
			String password = resultSet.getString("password");

			System.out.println("Processing customer ID: " + id); // Debug statement

			// Check if customer already exists in the list
			Customer customer = findCustomerById(tempCustomerList, id);
			if (customer == null) {
				customer = new Customer(id, firstName, lastName, email, password);
				customer.setAccounts(new ArrayList<>()); // Initialize accounts list
				tempCustomerList.add(customer);
				System.out.println("Added new customer: " + customer); // Debug statement
			}
			// Retrieve account information if available
			long accountNumber = resultSet.getLong("account_number");
			if (!resultSet.wasNull()) { // Check if the account number is not null
				double balance = resultSet.getDouble("balance");
				customer.getAccounts().add(new Account(accountNumber, balance));
				System.out.println(
						"Added account to customer ID " + id + ": " + accountNumber + " with balance " + balance); // Debug
			}
		}
		// Add all customers to the result list
		customers.addAll(tempCustomerList);
		System.out.println("Total customers found: " + customers.size()); // Debug statement
		return customers;
	}

	// Helper method to find a customer by ID in the list
	private Customer findCustomerById(List<Customer> customerList, int id) {
		for (Customer customer : customerList) {
			if (customer.getId() == id) {
				return customer;
			}
		}
		return null;
	}

	public List<Customer> getCustomersWithoutAccount() throws SQLException {
		List<Customer> customers = new ArrayList<>();
		String sql = "SELECT * FROM customers WHERE customer_id NOT IN (SELECT DISTINCT customer_id FROM accounts)";

		System.out.println("Executing SQL query: " + sql);

		try {
			connection = getConnection();
			statement = connection.createStatement();
			result = statement.executeQuery(sql);
			System.out.println("Database connection and statement created successfully.");

			while (result.next()) {
				Customer customer = new Customer();
				customer.setId(result.getInt("customer_id"));
				customer.setFirstName(result.getString("first_name"));
				customer.setLastName(result.getString("last_name"));
				customer.setEmail(result.getString("email"));

				System.out.println("Fetched customer: ID=" + customer.getId() + ", FirstName=" + customer.getFirstName()+ ", LastName=" + customer.getLastName() + ", Email=" + customer.getEmail());
				customers.add(customer);
			}

			System.out.println("Total customers fetched: " + customers.size());
		} catch (SQLException e) {
			System.err.println("SQL error occurred: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return customers;
	}

	public Customer getCustomerById(String customerId) throws SQLException {
		CustomerProcessor customerProcessor = new CustomerProcessor(connection);
		return customerProcessor.getCustomerById(customerId);
	}

	//Transaction lists
    public List<Transactions> getAllTransactions() throws SQLException {
        TransactionProcessor transactionProcessor = new TransactionProcessor(connection);
        return transactionProcessor.getAllTransactions();
    }

    public List<Transactions> getAllTransactionsOfCustomer(String userId) throws SQLException {
        TransactionProcessor transactionProcessor = new TransactionProcessor(connection);
        return transactionProcessor.getAllTransactionsOfCustomer(userId);
    }
    
    public List<Transactions> getFilteredTransactions(String transactionType, String fromDate, String toDate, String accountNumber) throws SQLException {
        TransactionProcessor transactionProcessor = new TransactionProcessor(connection);
        return transactionProcessor.getFilteredTransactions(transactionType, fromDate, toDate, accountNumber);
    }
    
    public boolean processTransaction(String userId, String transactionType, String toAccountNumber, double amount)
			throws SQLException {
		if (connection == null) {
			throw new SQLException("Database connection is not available.");
		}

		// boolean success = false;
		TransactionProcessor processor = new TransactionProcessor(connection);

		return processor.processTransaction(userId, transactionType, toAccountNumber, amount);
	}
    
    
    //Account Processor

	public String generateAccountNumber() throws SQLException {
		if (connection == null) {
			throw new SQLException("Database connection is not available.");
		}

		AccountProcessor generator = new AccountProcessor(connection);
		return generator.generateAccountNumber();
	}

	public boolean addAccount(String customerId, String accountNumber, double initialBalance) throws SQLException {
		if (connection == null) {
			throw new SQLException("Database connection is not available.");
		}
		AccountProcessor generator = new AccountProcessor(connection);

		return generator.addAccount(customerId, accountNumber, initialBalance);
	}

	

	public Profile getProfileByUserId(String userId) throws SQLException {
		Profile profile = new Profile();
		String query = "SELECT first_name, last_name, email FROM customers WHERE user_id = ?";
		try (PreparedStatement prepareStatement = connection.prepareStatement(query)) {
			prepareStatement.setString(1, userId);
			result = prepareStatement.executeQuery();
			if (result.next()) {
				profile.setFirstName(result.getString("first_name"));
				profile.setLastName(result.getString("last_name"));
				profile.setEmail(result.getString("email"));
			}
		}
		return profile;
	}

	public void updateProfile(String userId, String firstName, String lastName, String password) throws SQLException {
		// Query to update first name and last name
		String updateBasicInfoQuery = "UPDATE customers SET first_name = ?, last_name = ? WHERE user_id = ?";

		// Query to update first name, last name, and password
		String updateAllInfoQuery = "UPDATE customers SET first_name = ?, last_name = ?, password = ? WHERE user_id = ?";

		// Determine which query to use based on the password field
		String queryToUse;
		if (password != null && !password.trim().isEmpty()) {
			queryToUse = updateAllInfoQuery;
		} else {
			queryToUse = updateBasicInfoQuery;
		}
		int user_id = Integer.parseInt(userId);
		// Execute the determined query
		try {
			prepareStatement = connection.prepareStatement(queryToUse);
			// Set the common parameters
			prepareStatement.setString(1, firstName);
			prepareStatement.setString(2, lastName);

			// Conditionally set the password field and user_id
			if (queryToUse.equals(updateAllInfoQuery)) {
				prepareStatement.setString(3, password);
				prepareStatement.setInt(4, user_id);

				PreparedStatement prepareStatement2 = connection
						.prepareStatement("UPDATE users SET password = ? WHERE user_id = ?");
				prepareStatement2.setString(1, password);
				prepareStatement2.setInt(2, user_id);
				prepareStatement2.executeUpdate();
			} else {
				prepareStatement.setInt(3, user_id);
			}

			// Execute the update
			prepareStatement.executeUpdate();

			// prepareStatement = connection.prepareStatement("UPDATE users SET password = ?
			// WHERE user_id = ?");
			// prepareStatement.setString(1, password);
			// prepareStatement.setInt(2, user_id);
			// prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error  updating proflie", e);
		}
	}

	public double getCurrentBalance(String userId) throws SQLException {
		double balance = 0.0;
		String query = "SELECT a.balance FROM accounts a " + "JOIN customers c ON a.customer_id = c.customer_id "
				+ "JOIN users u ON c.user_id = u.user_id " + "WHERE u.user_id = ?";

		try (PreparedStatement prepareStatement = connection.prepareStatement(query)) {
			prepareStatement.setInt(1, Integer.parseInt(userId));
			try (ResultSet result = prepareStatement.executeQuery()) {
				if (result.next()) {
					balance = result.getDouble("balance");
				} else {
					throw new SQLException("No balance found for user_id: " + userId);
				}
			}
		} catch (NumberFormatException e) {
			// Handle invalid userId format
			e.printStackTrace();
			throw new SQLException("Invalid user ID format", e);
		} catch (SQLException e) {
			// Handle SQL exceptions
			e.printStackTrace();
			throw new SQLException("Error retrieving balance", e);
		}

		return balance;
	}

}
