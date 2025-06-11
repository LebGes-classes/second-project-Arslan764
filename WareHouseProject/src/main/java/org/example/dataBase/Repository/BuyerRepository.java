package org.example.dataBase.Repository;

import org.example.Models.Person.Buyer;
import org.example.dataBase.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class BuyerRepository {
    //добавление покупателя
    public void add(Buyer buyer) throws SQLException {
        if (getId("First_name = '" + buyer.firstName + "' AND Last_name = '" + buyer.lastName + "'") != 0) {
            return;
        }

        String sql = "INSERT INTO Buyers (First_name, Last_name, Phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = DataBase.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, buyer.firstName);
            pstmt.setString(2, buyer.lastName);
            pstmt.setString(3, buyer.phoneNumber);
            pstmt.executeUpdate();
        }
    }

    //обновление данных о покупателе
    public void update(Buyer buyer) throws SQLException {
        String sql = "UPDATE Buyers SET First_name = ?, Last_name = ?, Phone_number = ? WHERE ID = ?";

        try (PreparedStatement pstmt = DataBase.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, buyer.firstName);
            pstmt.setString(2, buyer.lastName);
            pstmt.setString(3, buyer.phoneNumber);
            pstmt.setInt(4, buyer.id);
            pstmt.executeUpdate();
        }
    }

    //удаление покупателя
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Buyers WHERE ID = " + id;
        try (Statement stmt = DataBase.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    //получение списка покупателей
    public ArrayList<Buyer> getAll() throws SQLException {
        ArrayList<Buyer> buyers = new ArrayList<>();
        String sql = "SELECT * FROM Buyers";

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            while (set.next()) {
                Buyer buyer = new Buyer();
                buyer.setId(set.getInt("ID"));
                buyer.setFirstName(set.getString("First_name"));
                buyer.setLastName(set.getString("Last_name"));
                buyer.setPhoneNumber(set.getString("Phone_number"));
                buyers.add(buyer);
            }
        }

        return buyers;
    }

    //получение списка покупателей, удовлетворяющих условию
    public ArrayList<Buyer> getAll(String condition) throws SQLException {
        ArrayList<Buyer> buyers = new ArrayList<>();
        String sql = "SELECT * FROM Buyers WHERE " + condition;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            while (set.next()) {
                Buyer buyer = new Buyer();
                buyer.setId(set.getInt("ID"));
                buyer.setFirstName(set.getString("First_name"));
                buyer.setLastName(set.getString("Last_name"));
                buyer.setPhoneNumber(set.getString("Phone_number"));
                buyers.add(buyer);
            }
        }

        return buyers;
    }

    //получение сотрудника по его id
    public Buyer getById(int id) throws SQLException {
        Buyer buyer = null;
        String sql = "SELECT * FROM Buyers WHERE ID = " + id;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            if (set.next()) {
                buyer = new Buyer();
                buyer.setId(set.getInt("ID"));
                buyer.setFirstName(set.getString("First_name"));
                buyer.setLastName(set.getString("Last_name"));
                buyer.setPhoneNumber(set.getString("Phone_number"));
            }
        }

        return buyer;
    }


    //получение id покупателя, удовлетворяющего условию
    public int getId(String condition) throws SQLException {
        int id = 0;
        String sql = "SELECT ID FROM Buyers WHERE " + condition;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet result = stmt.executeQuery(sql)) {
            if (result.next()) {
                id = result.getInt(1);
            }
        }

        return id;
    }
}
