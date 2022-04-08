package com.fenonq.myrestaurant.db.entity.dishlocalization;

import java.util.Objects;

public class SubDish {
    private int id;
    private int price;
    private int weight;
    private int categoryId;
    private int isVisible;

    public SubDish() {
    }

    public SubDish(int price, int weight, int categoryId, int isVisible) {
        this.price = price;
        this.weight = weight;
        this.categoryId = categoryId;
        this.isVisible = isVisible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubDish subDish = (SubDish) o;
        return id == subDish.id &&
                price == subDish.price &&
                weight == subDish.weight &&
                categoryId == subDish.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, weight, categoryId);
    }

    @Override
    public String toString() {
        return "SubDish{" +
                "id=" + id +
                ", price=" + price +
                ", weight=" + weight +
                ", categoryId=" + categoryId +
                ", isVisible=" + isVisible +
                '}';
    }
}
