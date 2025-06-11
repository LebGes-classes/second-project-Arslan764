package org.example.Models.Person;

public class Worker extends Person {

    public int workPlaceId;
    public String status;

    public Worker() {}

    public void setWorkPlaceId(int workPlaceId) {
        this.workPlaceId = workPlaceId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Worker " +
                "id: " + id +
                ", firstName: " + firstName +
                ", lastName: " + lastName +
                ", phoneNumber: " + phoneNumber +
                ", workPlaceId: " + workPlaceId +
                ", status: " + status +
                "\n";
    }
}