import java.sql.*;
import java.util.Scanner;

public class BankManagementSystem {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bank";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    private static Scanner scanner = new Scanner(System.in);

    // Method to establish database connection
    private static void connect() throws SQLException {
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    // Method to close database connection
    private static void close() throws SQLException {
        if (resultSet != null) resultSet.close();
        if (preparedStatement != null) preparedStatement.close();
        if (connection != null) connection.close();
    }

    // Method to create a new bank account
    public static void createAccount(int accountNumber, String accountHolderName, double balance) throws SQLException {
        connect();
        preparedStatement = connection.prepareStatement("INSERT INTO accounts (account_number, account_holder_name, balance) VALUES (?, ?, ?)");
        preparedStatement.setInt(1, accountNumber);
        preparedStatement.setString(2, accountHolderName);
        preparedStatement.setDouble(3, balance);
        preparedStatement.executeUpdate();
        close();
        System.out.println("Account created successfully.");
    }

    // Method to retrieve account details by account number
    public static void getAccountById(int accountNumber) throws SQLException {
        connect();
        preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ?");
        preparedStatement.setInt(1, accountNumber);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println("Account Number: " + resultSet.getInt("account_number") + ", Account Holder Name: " + resultSet.getString("account_holder_name") + ", Balance: $" + resultSet.getDouble("balance"));
        } else {
            System.out.println("No account found with account number: " + accountNumber);
        }
        close();
    }

    // Method to retrieve account details by account holder name
    public static void getAccountByName(String accountHolderName) throws SQLException {
        connect();
        preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_holder_name = ?");
        preparedStatement.setString(1, accountHolderName);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println("Account Number: " + resultSet.getInt("account_number") + ", Account Holder Name: " + resultSet.getString("account_holder_name") + ", Balance: $" + resultSet.getDouble("balance"));
        } else {
            System.out.println("No account found with account holder name: " + accountHolderName);
        }
        close();
    }

    // Method to update account balance by account number
    public static void updateBalance(int accountNumber, double newBalance) throws SQLException {
        connect();
        preparedStatement = connection.prepareStatement("UPDATE accounts SET balance = ? WHERE account_number = ?");
        preparedStatement.setDouble(1, newBalance);
        preparedStatement.setInt(2, accountNumber);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Account balance updated successfully.");
        } else {
            System.out.println("No account found with account number: " + accountNumber);
        }
        close();
    }

    // Method to delete an account by account number
    public static void deleteAccount(int accountNumber) throws SQLException {
        connect();
        preparedStatement = connection.prepareStatement("DELETE FROM accounts WHERE account_number = ?");
        preparedStatement.setInt(1, accountNumber);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("No account found with account number: " + accountNumber);
        }
        close();
    }

    public static void main(String[] args) {
        try {
            int choice;
            do {
                System.out.println("\nSelect an option:");
                System.out.println("1. Create a new account");
                System.out.println("2. Retrieve account details by account number");
                System.out.println("3. Retrieve account details by account holder name");
                System.out.println("4. Update account balance");
                System.out.println("5. Delete an account");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Enter account number: ");
                        int accountNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        System.out.print("Enter account holder name: ");
                        String accountHolderName = scanner.nextLine();
                        System.out.print("Enter initial balance: ");
                        double balance = scanner.nextDouble();
                        createAccount(accountNumber, accountHolderName, balance);
                        break;
                    case 2:
                        System.out.print("Enter account number: ");
                        int getByAccountNumber = scanner.nextInt();
                        getAccountById(getByAccountNumber);
                        break;
                    case 3:
                        System.out.print("Enter account holder name: ");
                        scanner.nextLine(); // Consume newline character
                        String getByAccountHolderName = scanner.nextLine();
                        getAccountByName(getByAccountHolderName);
                        break;
                    case 4:
                        System.out.print("Enter account number: ");
                        int updateAccountNumber = scanner.nextInt();
                        System.out.print("Enter new balance: ");
                        double newBalance = scanner.nextDouble();
                        updateBalance(updateAccountNumber, newBalance);
                        break;
                    case 5:
                        System.out.print("Enter account number to delete: ");
                        int deleteAccountNumber = scanner.nextInt();
                        deleteAccount(deleteAccountNumber);
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            } while (choice != 6);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

