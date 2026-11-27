package com.rentalmusik.service;

import com.rentalmusik.exception.BarangTidakDitemukanException;
import com.rentalmusik.exception.InputTidakValidException;
import com.rentalmusik.model.Barang;
import java.util.ArrayList;

/**
 * Service pengelola keranjang belanja sewa penyewa di memori.
 *
 * @author Kelompok 4
 */
public class KeranjangSewa {

    private final ArrayList<Barang> barang;

    public KeranjangSewa() {
        this.barang = new ArrayList<>();
    }

    public void tambahBarang(Barang b) throws InputTidakValidException {
        if (b == null) {
            return;
        }
        for (Barang item : barang) {
            if (item.getId().equalsIgnoreCase(b.getId())) {
                throw new InputTidakValidException("[GAGAL] Barang " + b.getNama() + " sudah ada di keranjang.");
            }
        }
        this.barang.add(b);
    }

    public void hapusBarang(String id) throws BarangTidakDitemukanException {
        if (id == null) {
            throw new BarangTidakDitemukanException("[GAGAL] ID barang tidak valid.");
        }
        String idBersih = id.trim().toUpperCase();
        Barang target = null;
        for (Barang item : barang) {
            if (item.getId().equalsIgnoreCase(idBersih)) {
                target = item;
                break;
            }
        }
        if (target == null) {
            throw new BarangTidakDitemukanException("[GAGAL] Barang " + id + " tidak ada di keranjang.");
        }
        this.barang.remove(target);
    }

    public void kosongkan() {
        this.barang.clear();
    }

    public ArrayList<Barang> getBarang() {
        return this.barang;
    }

    public boolean isKosong() {
        return this.barang.isEmpty();
    }

    public int getJumlah() {
        return this.barang.size();
    }
}
