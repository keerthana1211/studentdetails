 package student;
import java.sql.*;
import java.util.Scanner;
public class StudentRecordManagementSystem{
private static final String DB_URL = "jdbc:mysql://localhost:3306/Student_detail";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            createTables(connection);
            insertStudentRecords(connection);
            insertAboveAverageStudents(connection);
            insertBelowAverageStudents(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS StudentRecords (" +
                "id INT," +
                "name VARCHAR(100) NOT NULL," +
                "regno VARCHAR(20) NOT NULL," +
                "sem1 INT NOT NULL," +
                "sem2 INT NOT NULL," +
                "sem3 INT NOT NULL," +
                "sem4 INT NOT NULL," +
                "sem5 INT NOT NULL," +
                "average DOUBLE" +
                ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
        }
    }

    private static void insertStudentRecords(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of students: ");
        int numberOfStudents = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < numberOfStudents; i++) {
            System.out.println("\nStudent " + (i + 1) + ":");
            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Reg No: ");
            String regNo = scanner.nextLine();

            System.out.print("Semester 1 marks: ");
            int sem1Marks = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Semester 2 marks: ");
            int sem2Marks = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Semester 3 marks: ");
            int sem3Marks = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Semester 4 marks: ");
            int sem4Marks = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Semester 5 marks: ");
            int sem5Marks = scanner.nextInt();
            scanner.nextLine();

            double average = (sem1Marks + sem2Marks + sem3Marks + sem4Marks + sem5Marks) / 5.0;

            String insertRecordQuery = "INSERT INTO StudentRecords (name, regno, sem1, sem2, sem3, sem4, sem5, average) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertRecordQuery)) {
                statement.setString(1, name);
                statement.setString(2, regNo);
                statement.setInt(3, sem1Marks);
                statement.setInt(4, sem2Marks);
                statement.setInt(5, sem3Marks);
                statement.setInt(6, sem4Marks);
                statement.setInt(7, sem5Marks);
                statement.setDouble(8, average);

                statement.executeUpdate();
            }
        }
    }

    private static void insertAboveAverageStudents(Connection connection) throws SQLException {
        String insertAboveAverageQuery = "INSERT INTO AboveAverageStudents " +
                "SELECT * FROM StudentRecords WHERE average > 70 " +
                "ORDER BY average ASC";

        String createAboveAverageTableQuery = "CREATE TABLE IF NOT EXISTS AboveAverageStudents " +
                "(LIKE StudentRecords)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createAboveAverageTableQuery);
            statement.executeUpdate(insertAboveAverageQuery);
        }
    }

    private static void insertBelowAverageStudents(Connection connection) throws SQLException {
        String insertBelowAverageQuery = "INSERT INTO BelowAverageStudents " +
                "SELECT * FROM StudentRecords WHERE average < 70 " +
                "ORDER BY average ASC";

        String createBelowAverageTableQuery = "CREATE TABLE IF NOT EXISTS BelowAverageStudents " +
                "(LIKE StudentRecords)";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createBelowAverageTableQuery);
            statement.executeUpdate(insertBelowAverageQuery);
        }
    }
}
