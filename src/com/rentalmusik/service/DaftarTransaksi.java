package com.rentalmusik.service;

import com.rentalmusik.exception.PenyimpananException;
import com.rentalmusik.model.ItemSewa;
import com.rentalmusik.model.Penyewa;
import com.rentalmusik.model.StatusTransaksi;
import com.rentalmusik.model.Transaksi;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Service pengelola data seluruh transaksi penyewaan alat musik.
 *
 * @author Kelompok 4
 */
public class DaftarTransaksi {

    private final ArrayList<Transaksi> daftar;
    private int nomorBerikutnya;
    private final PenyimpananFile penyimpananFile;

    public DaftarTransaksi(PenyimpananFile penyimpananFile, HashMap<String, Penyewa> penyewaById)
            throws PenyimpananException {
        this.penyimpananFile = penyimpananFile;
        this.daftar = penyimpananFile.muatTransaksi(penyewaById);
        this.daftar.sort(Comparator.comparing(Transaksi::getId));

        int counterDimuat = penyimpananFile.muatCounter();
        if (counterDimuat <= 0) {
            this.nomorBerikutnya = hitungNomorBerikutnyaDariDaftar();
            this.penyimpananFile.simpanCounter(this.nomorBerikutnya);
        } else {
            this.nomorBerikutnya = counterDimuat;
        }
    }

    private int hitungNomorBerikutnyaDariDaftar() {
        int maks = 0;
        for (Transaksi t : daftar) {
            String id = t.getId().toUpperCase();
            if (id.startsWith("TRX-")) {
                try {
                    int nomor = Integer.parseInt(id.substring("TRX-".length()));
                    if (nomor > maks) {
                        maks = nomor;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return maks + 1;
    }

    public String lihatNomorBerikutnya() {
        return String.format("TRX-%04d", nomorBerikutnya);
    }

    public void tambah(Transaksi t) throws PenyimpananException {
        if (t == null) {
            return;
        }
        this.daftar.add(t);
        this.daftar.sort(Comparator.comparing(Transaksi::getId));
        this.nomorBerikutnya++;
        simpan();
    }

    public Transaksi cariById(String id) {
        if (id == null) {
            return null;
        }
        String idBersih = id.trim().toUpperCase();
        for (Transaksi t : daftar) {
            if (t.getId().equalsIgnoreCase(idBersih)) {
                return t;
            }
        }
        return null;
    }

    public ArrayList<Transaksi> filterStatus(StatusTransaksi s) {
        ArrayList<Transaksi> hasil = new ArrayList<>();
        for (Transaksi t : daftar) {
            if (t.getStatus() == s) {
                hasil.add(t);
            }
        }
        return hasil;
    }

    public ArrayList<Transaksi> filterPenyewa(Penyewa p) {
        ArrayList<Transaksi> hasil = new ArrayList<>();
        if (p == null) {
            return hasil;
        }
        for (Transaksi t : daftar) {
            if (t.getPenyewa() != null && t.getPenyewa().getId().equalsIgnoreCase(p.getId())) {
                hasil.add(t);
            }
        }
        return hasil;
    }

    public boolean adaBarangDiajukan(String idBarang) {
        if (idBarang == null) {
            return false;
        }
        String idBersih = idBarang.trim().toUpperCase();
        for (Transaksi t : daftar) {
            if (t.getStatus() == StatusTransaksi.MENUNGGU) {
                for (ItemSewa it : t.getItems()) {
                    if (it.getIdBarang().equalsIgnoreCase(idBersih)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<Transaksi> getDaftar() {
        return new ArrayList<>(this.daftar);
    }

    public void simpan() throws PenyimpananException {
        this.penyimpananFile.simpanTransaksi(this.daftar);
        this.penyimpananFile.simpanCounter(this.nomorBerikutnya);
    }
}
