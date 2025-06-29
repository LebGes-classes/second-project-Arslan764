package org.example.Models;

import java.time.LocalDateTime;

public class Order {
    public int id;
    public int buyerId;
    public int productId;
    public int amount;
    public double totalPrice;
    public String date;
    public int workerId;
    public int salePointId;
    public String status;

    public Order() {}

    public void setId(int id) {
        this.id = id;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDate(LocalDateTime date) {
        this.date = date.withNano(0).toString().replace('T', ' ');;
    }

    public void setDate(String data) {
        this.date = data;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public void setSalePointId(int salePointId) {
        this.salePointId = salePointId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order " +
                "id: " + id +
                ", status: " + status +
                ", buyerId: " + buyerId +
                ", productId: " + productId +
                ", product quantity: " + amount +
                ", total price: " + totalPrice +
                ", date: " + date +
                ", workerId: " + workerId +
                ", salePointId: " + salePointId +
                "\n";
    }
}
