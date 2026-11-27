package com.rentalmusik.service;

import com.rentalmusik.exception.BarangTidakDitemukanException;
import com.rentalmusik.exception.BarangTidakTersediaException;
import com.rentalmusik.exception.InputTidakValidException;
import com.rentalmusik.exception.PenyimpananException;
import com.rentalmusik.model.Barang;
import com.rentalmusik.model.KategoriBarang;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Service pengelola data master seluruh alat musik.
 *
 * @author Kelompok 4
 */
public class DaftarBarang {

    // ArrayList sebagai penyimpan utama, HashMap sebagai indeks ID
    private final ArrayList<Barang> barang;
    private final HashMap<String, Barang> indeksId;
    private final PenyimpananFile penyimpananFile;

    public DaftarBarang(PenyimpananFile penyimpananFile) throws PenyimpananException {
        this.penyimpananFile = penyimpananFile;
        this.barang = new ArrayList<>();
        this.indeksId = new HashMap<>();

        ArrayList<Barang> dimuat = penyimpananFile.muatBarang();
        for (Barang b : dimuat) {
            this.barang.add(b);
            this.indeksId.put(b.getId().toUpperCase(), b);
        }
        urutkan();
    }

    private void urutkan() {
        this.barang.sort(Comparator.comparing(Barang::getId));
    }

    public void tambahBarang(Barang b) throws InputTidakValidException, PenyimpananException {
        if (b == null) {
            throw new InputTidakValidException("Data barang tidak boleh kosong.");
        }
        for (Barang existing : barang) {
            if (existing.getNama().equalsIgnoreCase(b.getNama())) {
                throw new InputTidakValidException("Nama barang tidak valid atau sudah ada.");
            }
        }
        this.barang.add(b);
        this.indeksId.put(b.getId().toUpperCase(), b);
        urutkan();
        simpan();
    }

    public void editBarang(String id, String nama, Double tarif, Double biaya)
            throws BarangTidakDitemukanException, InputTidakValidException, PenyimpananException {
        Barang b = cariById(id);
        if (b == null) {
            throw new BarangTidakDitemukanException("Barang dengan ID " + id + " tidak ditemukan.");
        }

        if (nama != null && !nama.trim().isEmpty()) {
            String namaBaru = nama.trim();
            for (Barang existing : barang) {
                if (!existing.getId().equalsIgnoreCase(b.getId()) && existing.getNama().equalsIgnoreCase(namaBaru)) {
                    throw new InputTidakValidException("Nama barang tidak valid atau sudah ada.");
                }
            }
            b.setNama(namaBaru);
        }

        if (tarif != null) {
            b.setTarifHarian(tarif);
        }

        if (biaya != null) {
            b.setBiayaTambahan(biaya);
        }

        simpan();
    }

    public void hapusBarang(String id)
            throws BarangTidakDitemukanException, BarangTidakTersediaException, PenyimpananException {
        Barang b = cariById(id);
        if (b == null) {
            throw new BarangTidakDitemukanException("Barang dengan ID " + id + " tidak ditemukan.");
        }
        if (!b.isTersedia()) {
            throw new BarangTidakTersediaException(
                    "[GAGAL] Barang " + b.getNama() + " tidak dapat dihapus karena sedang disewa atau diajukan dalam transaksi.");
        }
        this.barang.remove(b);
        this.indeksId.remove(b.getId().toUpperCase());
        simpan();
    }

    public Barang cariById(String id) {
        if (id == null) {
            return null;
        }
        return this.indeksId.get(id.trim().toUpperCase());
    }

    public ArrayList<Barang> cariByNama(String kata) {
        ArrayList<Barang> hasil = new ArrayList<>();
        if (kata == null || kata.trim().isEmpty()) {
            return hasil;
        }
        String lower = kata.trim().toLowerCase();
        for (Barang b : barang) {
            if (b.getNama().toLowerCase().contains(lower)) {
                hasil.add(b);
            }
        }
        return hasil;
    }

    public ArrayList<Barang> filterTersedia() {
        ArrayList<Barang> hasil = new ArrayList<>();
        for (Barang b : barang) {
            if (b.isTersedia()) {
                hasil.add(b);
            }
        }
        return hasil;
    }

    public ArrayList<Barang> filterKategori(KategoriBarang k) {
        ArrayList<Barang> hasil = new ArrayList<>();
        for (Barang b : barang) {
            if (b.getKategori() == k) {
                hasil.add(b);
            }
        }
        return hasil;
    }

    public void ubahKetersediaan(String id, boolean tersedia) throws PenyimpananException {
        Barang b = cariById(id);
        if (b != null) {
            b.setTersedia(tersedia);
            simpan();
        }
    }

    public String buatIdBaru(KategoriBarang k) {
        String prefix = k.getPrefixId() + "-";
        int maks = 0;
        for (Barang b : barang) {
            String id = b.getId().toUpperCase();
            if (id.startsWith(prefix)) {
                String nomorStr = id.substring(prefix.length());
                try {
                    int nomor = Integer.parseInt(nomorStr);
                    if (nomor > maks) {
                        maks = nomor;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return prefix + String.format("%03d", maks + 1);
    }

    public ArrayList<Barang> getSemuaBarang() {
        return new ArrayList<>(this.barang);
    }

    public void simpan() throws PenyimpananException {
        this.penyimpananFile.simpanBarang(this.barang);
    }
}
