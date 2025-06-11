package org.example.dataBase;

import java.sql.*;


public class DataBase {
    private static final String DB_URL = "jdbc:sqlite:database.db";//ссылка на бд

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);//подключаемся
    }

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:trade.db");
             Statement stmt = conn.createStatement()) {

            //Таблица Buyers
            stmt.execute("CREATE TABLE IF NOT EXISTS Buyers (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "First_name TEXT, " +
                    "Last_name TEXT, " +
                    "Phone_number TEXT)");

            // Таблица Cells
            stmt.execute("CREATE TABLE IF NOT EXISTS Cells (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Storage_id INTEGER, " +
                    "Product_id INTEGER, " +
                    "Product_quantity INTEGER, " +
                    "FOREIGN KEY (Storage_id) REFERENCES Storages(ID), " +
                    "FOREIGN KEY (Product_id) REFERENCES Products(ID))");

            // Таблица Orders
            stmt.execute("CREATE TABLE IF NOT EXISTS Orders (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Status TEXT, " +
                    "Buyer_id INTEGER, " +
                    "Product_id INTEGER, " +
                    "Quantity INTEGER, " +
                    "Total_price REAL, " +
                    "Date TIMESTAMP, " +
                    "Worker_id INTEGER, " +
                    "Sale_point_id INTEGER, " +
                    "FOREIGN KEY (Buyer_id) REFERENCES Buyers(ID), " +
                    "FOREIGN KEY (Product_id) REFERENCES Products(ID), " +
                    "FOREIGN KEY (Worker_id) REFERENCES Workers(ID), " +
                    "FOREIGN KEY (Sale_point_id) REFERENCES Sale_points(ID))");

            //аблица Producers
            stmt.execute("CREATE TABLE IF NOT EXISTS Producers (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Name TEXT)");

            //Таблица Products
            stmt.execute("CREATE TABLE IF NOT EXISTS Products (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Name TEXT, " +
                    "Price REAL, " +
                    "Sell_price REAL, " +
                    "Status TEXT, " +
                    "Producer_id INTEGER, " +
                    "FOREIGN KEY (Producer_id) REFERENCES Producers(ID))");

            // аблица Sale_points
            stmt.execute("CREATE TABLE IF NOT EXISTS Sale_points (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Admin_id INTEGER, " +
                    "Revenue REAL, " +
                    "FOREIGN KEY (Admin_id) REFERENCES Workers(ID))" +
                    "Street TEXT, ");

            //Таблица Storages
            stmt.execute("CREATE TABLE IF NOT EXISTS Storages (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Street TEXT, " +
                    "FOREIGN KEY(Admin_id) REFERNCES Workers(ID)");


            //Таблица Workers
            stmt.execute("CREATE TABLE IF NOT EXISTS Workers (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "First_name TEXT, " +
                    "Last_name TEXT, " +
                    "Phone_number TEXT, " +
                    "Work_place_id INTEGER, " +
                    "Status TEXT)");

        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблиц: " + e.getMessage());
        }
    }
}
