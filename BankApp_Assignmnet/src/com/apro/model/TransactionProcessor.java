package com.apro.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.apro.entity.Transactions;
public class TransactionProcessor {
    private Connection connection;
    private PreparedStatement prepareStatement = null;
	private ResultSet result = null;

    public TransactionProcessor(Connection connection) {
        this.connection = connection;
    }

    public boolean debit(long accountNumber, double amount) throws SQLException {
        double balance = getAccountBalance(accountNumber);
        System.out.println("Debit Attempt - Account Number: " + accountNumber + ", Amount: " + amount + ", Current Balance: " + balance);

        if (balance < amount) {
            System.out.println("Debit Failed - Insufficient funds.");
            throw new SQLException("Insufficient funds for debit operation.");
        }

        String query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, amount);
            stmt.setLong(2, accountNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Debit Successful - Account Number: " + accountNumber + ", Amount: " + amount);
                return true;
            }
        }
        System.out.println("Debit Failed - No rows affected.");
        return false;
    }

    public boolean credit(long accountNumber, double amount) throws SQLException {
        if (amount <= 0) {
            System.out.println("Credit Failed - Amount must be positive.");
            throw new SQLException("Amount for credit operation must be positive.");
        }

        String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, amount);
            stmt.setLong(2, accountNumber);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Credit Successful - Account Number: " + accountNumber + ", Amount: " + amount);
                return true;
            }
        }
        System.out.println("Credit Failed - No rows affected.");
        return false;
    }

    public boolean transfer(long fromAccountNumber, long toAccountNumber, double amount) throws SQLException {
        // Check sufficient funds
        double balance = getAccountBalance(fromAccountNumber);
        System.out.println("Transfer Attempt - From Account Number: " + fromAccountNumber + ", To Account Number: " + toAccountNumber + ", Amount: " + amount + ", Current Balance: " + balance);

        if (balance < amount) {
            System.out.println("Transfer Failed - Insufficient funds.");
            throw new SQLException("Insufficient funds for transfer.");
        }

        // Deduct from sender's account
        boolean deductSuccess = debit(fromAccountNumber, amount);
        // Add to receiver's account
        boolean addSuccess = credit(toAccountNumber, amount);

        if (deductSuccess && addSuccess) {
            System.out.println("Transfer Successful - From Account Number: " + fromAccountNumber + ", To Account Number: " + toAccountNumber + ", Amount: " + amount);
            return true;
        }
        System.out.println("Transfer Failed - Deduction or addition unsuccessful.");
        return false;
    }

    private double getAccountBalance(long accountNumber) throws SQLException {
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double balance = rs.getDouble("balance");
                    System.out.println("Fetched Balance - Account Number: " + accountNumber + ", Balance: " + balance);
                    return balance;
                }
            }
        }
        System.out.println("Balance Fetch Failed - Account Number: " + accountNumber);
        return 0.0;
    }

    public void logTransaction(Long fromAccountNumber, Double amount, Long toAccountNumber, String transactionType) throws SQLException {
        String query = "INSERT INTO transactions (from_account, to_account, transactions_type, amount, transaction_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, fromAccountNumber);
            stmt.setObject(2, toAccountNumber); // Can be null for debit/credit
            stmt.setString(3, transactionType);
            stmt.setDouble(4, amount);
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction Logged - From Account Number: " + fromAccountNumber + ", Amount: " + amount + ", To Account Number: " + toAccountNumber);
            } else {
                System.out.println("Transaction Logging Failed - No rows affected.");
            }
        }
    }

    public long getAccountNumberByUserId(String userId) throws SQLException {
        String query = "SELECT a.account_number FROM accounts a JOIN customers c ON a.customer_id = c.customer_id WHERE c.user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(userId));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long accountNumber = rs.getLong("account_number");
                    System.out.println("Fetched Account Number - User ID: " + userId + ", Account Number: " + accountNumber);
                    return accountNumber;
                }
            }
        }
        System.out.println("Account Number Fetch Failed - User ID: " + userId);
        return -1;
    }
    
    public boolean processTransaction(String userId, String transactionType, String toAccountNumber, double amount) throws SQLException {
	    if (connection == null) {
	        throw new SQLException("Database connection is not available.");
	    }

	    boolean success = false;
	    TransactionProcessor processor = new TransactionProcessor(connection);

	    try {
	        // Start a transaction
	        connection.setAutoCommit(false);

	        // Retrieve the account number of the logged-in user
	        long fromAccountNumber = processor.getAccountNumberByUserId(userId);
	        if (fromAccountNumber == -1) {
	            throw new SQLException("No account found for user ID: " + userId);
	        }

	        if (transactionType.equalsIgnoreCase("credit")) {
	            success = processor.credit(fromAccountNumber, amount);
	        } else if (transactionType.equalsIgnoreCase("debit")) {
	            success = processor.debit(fromAccountNumber, amount);
	        } else if (transactionType.equalsIgnoreCase("transfer")) {
	            long toAccountNumberLong = Long.parseLong(toAccountNumber);
	            success = processor.transfer(fromAccountNumber, toAccountNumberLong, amount);
	        } else {
	            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
	        }
	        long toAccountNumberLong = 0;
	        // Commit the transaction if successful
	        if (success) {
	            connection.commit();
	            // Log the transaction
	            if (transactionType.equalsIgnoreCase("transfer")) {
	                toAccountNumberLong = Long.parseLong(toAccountNumber);
	                processor.logTransaction(fromAccountNumber, amount, toAccountNumberLong, "transfer");
//	                processor.logTransaction(toAccountNumberLong, amount, fromAccountNumber, "transfer");
	            }else if(transactionType.equalsIgnoreCase("credit")) {
	            	processor.logTransaction(toAccountNumberLong, amount, fromAccountNumber, "credit");
	            }else if(transactionType.equalsIgnoreCase("debit")) {
	            	processor.logTransaction(fromAccountNumber, amount, toAccountNumberLong, "debit");
	            }
	            System.out.println("Transaction processed successfully.");
	        } else {
	            throw new SQLException("Transaction failed.");
	        }
	    } catch (SQLException | IllegalArgumentException e) {
	        // Roll back the transaction in case of an error
	        if (connection != null) {
	            try {
	                connection.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	        }
	        e.printStackTrace();
	        throw new SQLException("Error processing transaction", e);
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
    
    public List<Transactions> getAllTransactions() throws SQLException {
		List<Transactions> transactions = new ArrayList<Transactions>();
		String query = "SELECT * FROM transactions";

		try {
			prepareStatement = connection.prepareStatement(query);
			result = prepareStatement.executeQuery();
			while (result.next()) {
				int transactionId = result.getInt("transaction_id");
				long fromAccount = result.getLong("from_account");
				long toAccount = result.getLong("to_account");
				String transactionType = result.getString("transactions_type");
				double amount = result.getDouble("amount");
				Timestamp transactionDate = result.getTimestamp("transaction_date");

				Transactions transaction = new Transactions(transactionId, fromAccount, toAccount, transactionType,
						amount, transactionDate);
				transactions.add(transaction);
				System.out.println("Transaction retrieved: ID = " + transactionId + ", From = " + fromAccount
						+ ", To = " + toAccount);
			}
		} catch (SQLException e) {
			System.err.println("SQL Exception while retrieving transactions.");
			e.printStackTrace();
			throw new SQLException("Error retrieving transactions", e);
		}

		System.out.println("Total transactions retrieved: " + transactions.size());
		return transactions;
	}
    
    public List<Transactions> getAllTransactionsOfCustomer(String userId) throws SQLException {
		List<Transactions> transactions = new ArrayList<>();
		int id = Integer.parseInt(userId);

		String customerQuery = "SELECT customer_id FROM customers WHERE user_id = ?";
		try (PreparedStatement customerStmt = connection.prepareStatement(customerQuery)) {
			customerStmt.setInt(1, id);
			try (ResultSet customerResult = customerStmt.executeQuery()) {
				if (!customerResult.next()) {
					throw new SQLException("No customer found for user_id: " + userId);
				}
				int customerId = customerResult.getInt("customer_id");

				String accountQuery = "SELECT account_number FROM accounts WHERE customer_id = ?";
				try (PreparedStatement accountStmt = connection.prepareStatement(accountQuery)) {
					accountStmt.setInt(1, customerId);
					try (ResultSet accountResult = accountStmt.executeQuery()) {
						if (!accountResult.next()) {
							throw new SQLException("No accounts found for customer_id: " + customerId);
						}
						long accountNumber = accountResult.getLong("account_number");

						String transactionQuery = "SELECT * FROM transactions WHERE from_account = ? OR to_account = ? ORDER BY transaction_date DESC";
						try (PreparedStatement transactionStmt = connection.prepareStatement(transactionQuery)) {
							transactionStmt.setLong(1, accountNumber);
							transactionStmt.setLong(2, accountNumber);
							try (ResultSet transactionResult = transactionStmt.executeQuery()) {
								while (transactionResult.next()) {
									int transactionId = transactionResult.getInt("transaction_id");
									long fromAccount = transactionResult.getLong("from_account");
									long toAccount = transactionResult.getLong("to_account");
									String transactionType = transactionResult.getString("transactions_type");
									double amount = transactionResult.getDouble("amount");
									Timestamp transactionDate = transactionResult.getTimestamp("transaction_date");

									Transactions transaction = new Transactions(transactionId, fromAccount, toAccount,
											transactionType, amount, transactionDate);
									transactions.add(transaction);
									System.out.println("Transaction retrieved: ID = " + transactionId + ", From = "
											+ fromAccount + ", To = " + toAccount);
								}
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL Exception while retrieving transactions.");
			e.printStackTrace();
			throw new SQLException("Error retrieving transactions", e);
		}

		System.out.println("Total transactions retrieved: " + transactions.size());
		return transactions;
	}

    
    public List<Transactions> getFilteredTransactions(String transactionType, String fromDate, String toDate,
			String accountNumber) throws SQLException {
		List<Transactions> transactions = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT * FROM transactions WHERE 1=1");

		if (transactionType != null && !transactionType.isEmpty()) {
			query.append(" AND transactions_type = ?");
		}
		if (fromDate != null && !fromDate.isEmpty()) {
			query.append(" AND transaction_date >= ?");
		}
		if (toDate != null && !toDate.isEmpty()) {
			query.append(" AND transaction_date <= ?");
		}
		if (accountNumber != null && !accountNumber.isEmpty()) {
			query.append(" AND (from_account = ? OR to_account = ?)");
		}

		try (PreparedStatement prepareStatement = connection.prepareStatement(query.toString())) {
			int index = 1;

			if (transactionType != null && !transactionType.isEmpty()) {
				prepareStatement.setString(index++, transactionType);
			}
			if (fromDate != null && !fromDate.isEmpty()) {
				prepareStatement.setTimestamp(index++, Timestamp.valueOf(fromDate + " 00:00:00"));
			}
			if (toDate != null && !toDate.isEmpty()) {
				prepareStatement.setTimestamp(index++, Timestamp.valueOf(toDate + " 23:59:59"));
			}
			if (accountNumber != null && !accountNumber.isEmpty()) {
				prepareStatement.setLong(index++, Long.parseLong(accountNumber));
				prepareStatement.setLong(index, Long.parseLong(accountNumber));
			}

			try (ResultSet result = prepareStatement.executeQuery()) {
				while (result.next()) {
					int transactionId = result.getInt("transaction_id");
					long fromAccount = result.getLong("from_account");
					long toAccount = result.getLong("to_account");
					String transactionTypeResult = result.getString("transactions_type");
					double amount = result.getDouble("amount");
					Timestamp transactionDate = result.getTimestamp("transaction_date");

					Transactions transaction = new Transactions(transactionId, fromAccount, toAccount,
							transactionTypeResult, amount, transactionDate);
					transactions.add(transaction);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("Error retrieving filtered transactions", e);
		}
		return transactions;
	}
}

