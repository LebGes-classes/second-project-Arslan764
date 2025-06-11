package org.example.dataBase.Repository;

import org.example.Models.storage.Cell;
import org.example.dataBase.DataBase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CellRepository {
    //добавление ячейки склада
    public void addToStorage(Cell cell) throws SQLException {
        String sql = "INSERT INTO Cells (Storage_id, Product_id, Product_quantity, SalePoint_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = DataBase.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, cell.storageId);
            pstmt.setInt(2, cell.productId);
            pstmt.setInt(3, cell.productAmount);
            pstmt.setInt(4, 0);
            pstmt.executeUpdate();
        }
    }

    //добавления ячейки пункта продаж
    public void addToSalePoint(Cell cell) throws SQLException {
        String sql = "INSERT INTO Cells (Storage_id, Product_id, Product_quantity, SalePoint_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = DataBase.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, 0);
            pstmt.setInt(2, cell.productId);
            pstmt.setInt(3, cell.productAmount);
            pstmt.setInt(4, cell.storageId);
            pstmt.executeUpdate();
        }
    }

    //обновление ячейки
    public void update(Cell cell) throws SQLException {
        String sql = "UPDATE Cells SET Storage_id = ?, Product_id = ?, Product_quantity = ? WHERE ID = ?";

        try (PreparedStatement pstmt = DataBase.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, cell.storageId);
            pstmt.setInt(2, cell.productId);
            pstmt.setInt(3, cell.productAmount);
            pstmt.setInt(4, cell.id);
            pstmt.executeUpdate();
        }
    }

    //удаление ячейки
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Cells WHERE ID = " + id;
        try (Statement stmt = DataBase.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    //получение списка всех ячеек
    public ArrayList<Cell> getAll() throws SQLException {
        ArrayList<Cell> cells = new ArrayList<>();
        String sql = "SELECT * FROM Cells";

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            while (set.next()) {
                Cell cell = new Cell();
                cell.setId(set.getInt("ID"));
                cell.setStorageId(set.getInt("Storage_id"));
                cell.setProductId(set.getInt("Product_id"));
                cell.setProductAmount(set.getInt("Product_quantity"));
                cells.add(cell);
            }
        }

        return cells;
    }

    //получение списка всех ячеек удовлетворяющих условию,
    public ArrayList<Cell> getAll(String condition) throws SQLException {
        ArrayList<Cell> cells = new ArrayList<>();
        String sql = "SELECT * FROM Cells WHERE " + condition;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            while (set.next()) {
                Cell cell = new Cell();
                cell.setId(set.getInt("ID"));
                cell.setStorageId(set.getInt("Storage_id"));
                cell.setProductId(set.getInt("Product_id"));
                cell.setProductAmount(set.getInt("Product_quantity"));
                cells.add(cell);
            }
        }

        return cells;
    }

    //получение ячейки по id
    public Cell getById(int id) throws SQLException {
        Cell cell = null;
        String sql = "SELECT * FROM Cells WHERE ID = " + id;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet set = stmt.executeQuery(sql)) {
            if (set.next()) {
                cell = new Cell();
                cell.setId(set.getInt("ID"));
                cell.setStorageId(set.getInt("Storage_id"));
                cell.setProductId(set.getInt("Product_id"));
                cell.setProductAmount(set.getInt("Product_quantity"));
            }
        }

        return cell;
    }



    //получение id ячейки по условию
    public int getId(String condition) throws SQLException {
        int id = 0;
        String sql = "SELECT ID FROM Cells WHERE " + condition;

        try (Statement stmt = DataBase.getConnection().createStatement();
             ResultSet result = stmt.executeQuery(sql)) {
            if (result.next()) {
                id = result.getInt(1);
            }
        }

        return id;
    }

    //получение количества товара в ячейке склада
    public int getAmountOfProductInStorage(int productId, int storageId) throws SQLException {
        int amount = 0;

        ArrayList<Cell> cells = getAll("Storage_id = " + storageId + " AND Product_id = " + productId);
        Cell cell = cells.getFirst();
        amount = cell.productAmount;

        return amount;
    }

    //получение количества товара в ячейке пункта продаж
    public int getAmountOfProductInSalePoint(int productId, int salePointId) throws SQLException {
        int amount = 0;

        ArrayList<Cell> cells = getAll("SalePoint_id = " + salePointId + " AND Product_id = " + productId);
        Cell cell = cells.getFirst();
        amount = cell.productAmount;

        return amount;
    }

    //получение общего количества товара во всех складах
    public int getTotalAmountOfProduct(int productId) throws SQLException {
        int amount = 0;

        ArrayList<Cell> cells = getAll("Product_id = " + productId);
        for (Cell cell : cells) {
            amount += cell.productAmount;
        }

        return amount;
    }
}
