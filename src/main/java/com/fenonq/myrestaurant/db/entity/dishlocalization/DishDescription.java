package com.fenonq.myrestaurant.db.entity.dishlocalization;

import java.util.Objects;

public class DishDescription {
    private int dishId;
    private int languageId;
    private String name;
    private String description;

    public DishDescription() {
    }

    public DishDescription(int languageId, String name, String description) {
        this.languageId = languageId;
        this.name = name;
        this.description = description;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DishDescription that = (DishDescription) o;
        return dishId == that.dishId &&
                languageId == that.languageId &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishId, languageId, name, description);
    }

    @Override
    public String toString() {
        return "DishDescription{" +
                "dishId=" + dishId +
                ", languageId=" + languageId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
