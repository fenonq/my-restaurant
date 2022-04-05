package com.fenonq.myrestaurant.db.entity;

import java.io.Serializable;
import java.util.Objects;

public class Status implements Serializable {

    private int id;
    private String name;
    private String nameEn;
    private String nameUa;

    public Status(){

    }

    public Status(int id, String name) {
        this.id = id;
        this.name = name;
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
        Status status = (Status) o;
        return id == status.id && Objects.equals(name, status.name) && Objects.equals(nameEn, status.nameEn) && Objects.equals(nameUa, status.nameUa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nameEn, nameUa);
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", nameUa='" + nameUa + '\'' +
                '}';
    }
}
