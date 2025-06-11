package org.example.Models.storage;

import java.util.ArrayList;

public class Storage {
    public int id;
    public String street;
    ArrayList<Cell> cells = new ArrayList<>();
    public int adminId;

    public Storage() {}

    public Storage(int id, String street) {
        this.id = id;
        this.street = street;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setAdminId(int adminId) {this.adminId = adminId;}

    public void setCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    @Override
    public String toString() {
        return "Storage " +
                "id: " + id +
                ", street: " + street +
                ", adminId: " + adminId;
    }
}
