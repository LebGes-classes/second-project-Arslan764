package org.example.Models.storage;

public class Cell {
    public int id;
    public int storageId;
    public int  productId;
    public int productAmount;

    public Cell() {}

    public Cell(int storageId) {
        this.storageId = storageId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStorageId(int storageId) {
        this.storageId = storageId;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Cell " +
                "id: " + id +
                ", storageId: " + storageId +
                ", productId: " + productId +
                ", productQuantity: " + productAmount +
                "\n";
    }
}
