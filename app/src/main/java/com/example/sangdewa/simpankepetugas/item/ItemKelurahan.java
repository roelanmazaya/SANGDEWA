package com.example.sangdewa.simpankepetugas.item;

public class ItemKelurahan {
    public String id_kel;
    public String id_kec;
    public String nama_kel;

    public ItemKelurahan(String id_kel, String id_kec, String nama_kel) {
        this.id_kel = id_kel;
        this.id_kec = id_kec;
        this.nama_kel = nama_kel;
    }

    public String getId_kel() {
        return id_kel;
    }

    public void setId_kel(String id_kel) {
        this.id_kel = id_kel;
    }

    public String getId_kec() {
        return id_kec;
    }

    public void setId_kec(String id_kec) {
        this.id_kec = id_kec;
    }

    public String getNama_kel() {
        return nama_kel;
    }

    public void setNama_kel(String nama_kel) {
        this.nama_kel = nama_kel;
    }

    @Override
    public String toString() {
        return nama_kel;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemKelurahan){
            ItemKelurahan c = (ItemKelurahan)obj;
            if(c.getNama_kel().equals(nama_kel) && c.getId_kel().equals(id_kel) ) return true;
        }

        return false;
    }
}
