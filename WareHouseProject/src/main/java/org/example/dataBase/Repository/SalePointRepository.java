package org.example.dataBase.Repository;

import org.example.Models.storage.SalePoint;
import org.example.dataBase.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SalePointRepository {
    //добавление пункта продаж
    public void add(SalePoint salePoint) throws SQLException {
        String sql = "INSERT INTO Sale_points (Admin_id, Revenue, Street) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = DataBase.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, salePoint.adminId);
            pstmt.setDouble(2, salePoint.revenue);
            pstmt.setString(3, salePoint.street);
            pstmt.executeUpdate();
        }
    }

    //обновление данных о пункте продаж
    public void update(SalePoint salePoint) throws SQLException {
        String sql = "UPDATE Sale_points SET Admin_id = ?, Revenue = ? WHERE ID = ?";

        try (PreparedStatement pstmt = DataBase.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, salePoint.adminId);
            pstmt.setDouble(2, salePoint.revenue);
            pstmt.setInt(3, salePoint.id);
            pstmt.executeUpdate();
        }
    }

    //удаление пункта продаж
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Sale_points WHERE ID = " + id;
        try (Statement stmt = DataBase.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    //получение списка всех пунктов продаж
    public ArrayList<SalePoint> getAll() throws SQLException {
        ArrayList<SalePoint> salePoints = new ArrayList<>();
        String sql = "SELECT * FROM Sale_points";

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            while (set.next()) {
                SalePoint salePoint = new SalePoint();
                salePoint.setId(set.getInt("ID"));
                salePoint.setStreet(set.getString("Street"));
                salePoint.setAdminId(set.getInt("Admin_id"));
                salePoint.setRevenue(set.getDouble("Revenue"));
                salePoints.add(salePoint);
            }
        }

        return salePoints;
    }

    //получение списка всех пунктов продаж удовлетворяющих условию
    public ArrayList<SalePoint> getAll(String condition) throws SQLException {
        ArrayList<SalePoint> salePoints = new ArrayList<>();
        String sql = "SELECT * FROM Sale_points WHERE " + condition;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            while (set.next()) {
                SalePoint salePoint = new SalePoint();
                salePoint.setId(set.getInt("ID"));
                salePoint.setStreet(set.getString("Street"));
                salePoint.setAdminId(set.getInt("Admin_id"));
                salePoint.setRevenue(set.getDouble("Revenue"));
                salePoints.add(salePoint);
            }
        }

        return salePoints;
    }

    //получение пункта продаж по его id
    public SalePoint getById(int id) throws SQLException {
        SalePoint salePoint = null;
        String sql = "SELECT * FROM Sale_points WHERE ID = " + id;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            if (set.next()) {
                salePoint = new SalePoint();
                salePoint.setId(set.getInt("ID"));
                salePoint.setAdminId(set.getInt("Admin_id"));
                salePoint.setRevenue(set.getDouble("Revenue"));
            }
        }

        return salePoint;
    }

    //получение id пункта продаж удовлетворяющего условию
    public int getId(String condition) throws SQLException {
        int id = 0;
        String sql = "SELECT ID FROM Sale_points WHERE " + condition;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet result = stmt.executeQuery(sql)) {
            if (result.next()) {
                id = result.getInt(1);
            }
        }

        return id;
    }
}
