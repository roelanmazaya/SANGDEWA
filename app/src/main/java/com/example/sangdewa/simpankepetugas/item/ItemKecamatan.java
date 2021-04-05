package com.example.sangdewa.simpankepetugas.item;

public class ItemKecamatan {
    public String id_kec;
    public String nama_kec;

    public ItemKecamatan(String id_kec, String nama_kec) {
        this.id_kec = id_kec;
        this.nama_kec = nama_kec;
    }

    public String getId_kec() {
        return id_kec;
    }

    public void setId_kec(String id_kec) {
        this.id_kec = id_kec;
    }

    public String getNama_kec() {
        return nama_kec;
    }

    public void setNama_kec(String nama_kec) {
        this.nama_kec = nama_kec;
    }

    @Override
    public String toString() {
        return nama_kec;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ItemKecamatan){
            ItemKecamatan c = (ItemKecamatan)obj;
            if(c.getNama_kec().equals(nama_kec) && c.getId_kec().equals(id_kec) ) return true;
        }

        return false;
    }

}
