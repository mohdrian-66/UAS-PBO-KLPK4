package com.rentalmusik.model;

/**
 * Enumerasi status siklus hidup transaksi penyewaan alat musik.
 *
 * @author Kelompok 4
 */
public enum StatusTransaksi {

    MENUNGGU("Menunggu"),
    DISEWA("Disewa"),
    SELESAI("Selesai"),
    DITOLAK("Ditolak"),
    DIBATALKAN("Dibatalkan");

    private final String namaTampil;

    StatusTransaksi(String namaTampil) {
        this.namaTampil = namaTampil;
    }

    public String getNamaTampil() {
        return namaTampil;
    }
}
