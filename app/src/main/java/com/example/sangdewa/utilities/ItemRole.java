package com.example.sangdewa.utilities;

public class ItemRole {
    public String id_inc;
    public String role;

    public ItemRole(String id_inc, String role) {
        this.id_inc = id_inc;
        this.role = role;
    }

    public String getId_inc() {
        return id_inc;
    }

    public void setId_inc(String id_inc) {
        this.id_inc = id_inc;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
