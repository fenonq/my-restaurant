package com.fenonq.myrestaurant.db.entity;

import java.io.Serializable;
import java.util.Objects;

public class Category implements Serializable {

    private int id;
    private String name;
    private String nameEn;
    private String nameUa;

    public Category() {

    }

    public Category(String [] names) {
        this.nameEn = names[0];
        this.nameUa = names[1];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameUa() {
        return nameUa;
    }

    public void setNameUa(String nameUa) {
        this.nameUa = nameUa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id && Objects.equals(name, category.name) && Objects.equals(nameEn, category.nameEn) && Objects.equals(nameUa, category.nameUa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameEn, nameUa);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", nameUa='" + nameUa + '\'' +
                '}';
    }
}
