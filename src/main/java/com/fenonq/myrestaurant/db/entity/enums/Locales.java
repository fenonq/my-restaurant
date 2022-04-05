package com.fenonq.myrestaurant.db.entity.enums;


public enum Locales {
    EN(1),
    UA(2);

   private final int id;
   Locales(int id) {
       this.id = id;
   }

    public int getId() {
        return id;
    }

    public static Locales localeById(int localeId) {
        for (Locales value : Locales.values()) {
            if (localeId == value.id) {
                return value;
            }
        }
        return null;
    }

    public static boolean contains(String locale) {
        for (Locales l : Locales.values()) {
            if (l.name().equals(locale)) {
                return true;
            }
        }
        return false;
    }

}
