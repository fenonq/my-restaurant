package com.fenonq.myrestaurant.db.entity;


import java.io.Serializable;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Receipt implements Serializable {

    private int id;
    private int userId;
    private int managerId;
    private Status status;
    private int totalPrice;
    private Date createDate;
    private Map<Dish, Integer> dishes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Map<Dish, Integer> getDishes() {
        return dishes;
    }

    public void setDishes(Map<Dish, Integer> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return id == receipt.id &&
                userId == receipt.userId &&
                managerId == receipt.managerId &&
                status == receipt.status &&
                totalPrice == receipt.totalPrice &&
                Objects.equals(createDate, receipt.createDate) &&
                Objects.equals(dishes, receipt.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, managerId, status, totalPrice, createDate, dishes);
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", userId=" + userId +
                ", managerId=" + managerId +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                ", createDate=" + createDate +
                ", dishes=" + dishes +
                '}';
    }
}
