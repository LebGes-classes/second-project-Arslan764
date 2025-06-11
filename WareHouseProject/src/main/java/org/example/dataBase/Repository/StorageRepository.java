package org.example.dataBase.Repository;

import org.example.Models.storage.Storage;
import org.example.Models.storage.SalePoint;
import org.example.dataBase.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StorageRepository {
    //добавление склада
    public void add(Storage storage) throws SQLException {
        String sql = "INSERT INTO Storages (Street, Admin_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = DataBase.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, storage.street);
            pstmt.setInt(2, storage.adminId);
            pstmt.executeUpdate();
        }
    }

    //Обновление данных о складе
    public void update(Storage storage) throws SQLException {
        String sql = "UPDATE Storages SET Admin_id = ? WHERE ID = ?";

        try (PreparedStatement pstmt = DataBase.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, storage.adminId);
            pstmt.setInt(2, storage.id);
            pstmt.executeUpdate();
        }
    }

    //удаление складов
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Storages WHERE ID = " + id;
        try (Statement stmt = DataBase.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    //получение списка всех складов
    public ArrayList<Storage> getAll() throws SQLException {
        ArrayList<Storage> storages = new ArrayList<>();
        String sql = "SELECT * FROM Storages";

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            while (set.next()) {
                Storage storage = new Storage();
                storage.setId(set.getInt("ID"));
                storage.setAdminId(set.getInt("Admin_id"));
                storage.setStreet(set.getString("Street"));
                storages.add(storage);
            }
        }

        return storages;
    }

    //получение списка всех складов, удовлетворяющих условию
    public ArrayList<Storage> getAll(String condition) throws SQLException {
        ArrayList<Storage> storages = new ArrayList<>();
        String sql = "SELECT * FROM Storages WHERE " + condition;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            while (set.next()) {
                Storage storage = new Storage();
                storage.setId(set.getInt("ID"));
                storage.setStreet(set.getString("Street"));
                storages.add(storage);
            }
        }

        return storages;
    }

    //получение склада по его id
    public Storage getById(int id) throws SQLException {
        Storage storage = null;
        String sql = "SELECT * FROM Storages WHERE ID = " + id;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            if (set.next()) {
                storage = new Storage();
                storage.setId(set.getInt("ID"));
                storage.setStreet(set.getString("Street"));
            }
        }

        return storage;
    }

    //получение id склада, удовлетворяющего условию
    public int getId(String condition) throws SQLException {
        int id = 0;
        String sql = "SELECT ID FROM Storages WHERE " + condition;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet result = stmt.executeQuery(sql)) {
            if (result.next()) {
                id = result.getInt(1);
            }
        }

        return id;
    }

}
