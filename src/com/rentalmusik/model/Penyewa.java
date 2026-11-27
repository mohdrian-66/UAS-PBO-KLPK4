package com.rentalmusik.model;

import com.rentalmusik.exception.BarangTidakDitemukanException;
import com.rentalmusik.exception.BarangTidakTersediaException;
import com.rentalmusik.exception.InputTidakValidException;
import com.rentalmusik.exception.PembayaranGagalException;
import com.rentalmusik.exception.PenyimpananException;
import com.rentalmusik.exception.TransaksiTidakValidException;
import com.rentalmusik.pembayaran.Pembayaran;
import com.rentalmusik.pembayaran.QRIS;
import com.rentalmusik.service.DaftarBarang;
import com.rentalmusik.service.DaftarTransaksi;
import com.rentalmusik.service.KeranjangSewa;
import com.rentalmusik.util.FormatHelper;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Akun Penyewa untuk melihat barang, mengelola keranjang, dan melakukan transaksi.
 *
 * @author Kelompok 4
 */
public class Penyewa extends Akun {

    private final KeranjangSewa keranjang;
    private final ArrayList<Transaksi> riwayat;
    private DaftarBarang daftarBarang;
    private DaftarTransaksi daftarTransaksi;

    public Penyewa(String id, String username, String password, String nama,
                   DaftarBarang daftarBarang, DaftarTransaksi daftarTransaksi) {
        super(id, username, password, nama);
        this.keranjang = new KeranjangSewa();
        this.riwayat = new ArrayList<>();
        this.daftarBarang = daftarBarang;
        this.daftarTransaksi = daftarTransaksi;
    }

    @Override
    public String getPeran() {
        return "Penyewa";
    }

    public void setDaftarBarang(DaftarBarang daftarBarang) {
        this.daftarBarang = daftarBarang;
    }

    public void setDaftarTransaksi(DaftarTransaksi daftarTransaksi) {
        this.daftarTransaksi = daftarTransaksi;
    }

    public KeranjangSewa getKeranjang() {
        return keranjang;
    }

    public DaftarBarang getDaftarBarang() {
        return daftarBarang;
    }

    public DaftarTransaksi getDaftarTransaksi() {
        return daftarTransaksi;
    }

    public void lihatBarang() {
        ArrayList<Barang> daftar = daftarBarang.getSemuaBarang();
        if (daftar.isEmpty()) {
            System.out.println("[INFO] Belum ada barang.");
            return;
        }

        System.out.printf("%-8s  %-20s  %-8s  %10s  %14s  %-9s%n",
                "ID", "Nama", "Kategori", "Tarif/Hari", "Biaya Tambahan", "Status");
        System.out.println("--------  --------------------  --------  ----------  --------------  ---------");
        for (Barang b : daftar) {
            String status = b.isTersedia() ? "Tersedia" : "Disewa";
            System.out.printf("%-8s  %-20s  %-8s  %10s  %14s  %-9s%n",
                    b.getId(),
                    b.getNama(),
                    b.getKategori().getNamaTampil(),
                    FormatHelper.rupiah(b.getTarifHarian()),
                    FormatHelper.rupiah(b.getBiayaTambahan()),
                    status);
        }
    }

    public void tambahKeKeranjang(String idBarang)
            throws BarangTidakDitemukanException, BarangTidakTersediaException, InputTidakValidException {
        if (idBarang == null || idBarang.trim().isEmpty()) {
            throw new BarangTidakDitemukanException("[GAGAL] ID barang tidak valid.");
        }
        Barang b = daftarBarang.cariById(idBarang);
        if (b == null) {
            throw new BarangTidakDitemukanException("Barang dengan ID " + idBarang.trim().toUpperCase() + " tidak ditemukan.");
        }
        if (!b.isTersedia()) {
            throw new BarangTidakTersediaException(
                    "[GAGAL] Barang " + b.getNama() + " sedang disewa dan tidak dapat dimasukkan ke keranjang.");
        }
        keranjang.tambahBarang(b);
        System.out.printf("[OK] %s ditambahkan ke keranjang. Isi keranjang: %d barang.%n",
                b.getNama(), keranjang.getJumlah());
    }

    public void hapusDariKeranjang(String idBarang) throws BarangTidakDitemukanException {
        keranjang.hapusBarang(idBarang);
    }

    public void lihatKeranjang() {
        if (keranjang.isKosong()) {
            System.out.println("[INFO] Keranjang kosong.");
            return;
        }

        System.out.printf("%-8s  %-20s  %-8s  %10s  %14s  %14s%n",
                "ID", "Nama", "Kategori", "Tarif/Hari", "Biaya Tambahan", "Est. 1 Hari");
        System.out.println("--------  --------------------  --------  ----------  --------------  --------------");
        for (Barang b : keranjang.getBarang()) {
            System.out.printf("%-8s  %-20s  %-8s  %10s  %14s  %14s%n",
                    b.getId(),
                    b.getNama(),
                    b.getKategori().getNamaTampil(),
                    FormatHelper.rupiah(b.getTarifHarian()),
                    FormatHelper.rupiah(b.getBiayaTambahan()),
                    FormatHelper.rupiah(b.hitungBiaya(1)));
        }
    }

    /**
     * Membuat transaksi sewa baru dari barang-barang di keranjang.
     */
    public Transaksi buatTransaksi(int lamaSewa, Pembayaran p)
            throws InputTidakValidException, BarangTidakDitemukanException,
            BarangTidakTersediaException, PembayaranGagalException, PenyimpananException {
        if (keranjang.isKosong()) {
            throw new InputTidakValidException("[GAGAL] Keranjang kosong. Tambahkan barang terlebih dahulu.");
        }

        for (Barang b : keranjang.getBarang()) {
            Barang terkini = daftarBarang.cariById(b.getId());
            if (terkini == null) {
                throw new BarangTidakDitemukanException(
                        "Barang dengan ID " + b.getId() + " tidak ditemukan.");
            }
            if (!terkini.isTersedia()) {
                throw new BarangTidakTersediaException(
                        "[GAGAL] Barang " + terkini.getNama() + " sudah tidak tersedia.");
            }
        }

        ArrayList<ItemSewa> snapshotItems = new ArrayList<>();
        double subtotal = 0.0;
        // hitungBiaya dipanggil polimorfik lewat tipe Barang
        for (Barang b : keranjang.getBarang()) {
            ItemSewa item = new ItemSewa(b, lamaSewa);
            snapshotItems.add(item);
            subtotal += item.getBiayaTotal();
        }

        double totalTagihan = subtotal + p.getBiayaAdmin();

        String idCalon = daftarTransaksi.lihatNomorBerikutnya();
        if (p instanceof QRIS qris) {
            qris.setKodeTransaksi(idCalon);
        }

        p.prosesPembayaran(totalTagihan);

        Transaksi baru = new Transaksi(idCalon, this, snapshotItems, lamaSewa, LocalDate.now(), p);
        daftarTransaksi.tambah(baru);
        keranjang.kosongkan();

        return baru;
    }

    public void batalkanTransaksi(Transaksi t) throws TransaksiTidakValidException, PenyimpananException {
        if (t == null) {
            throw new TransaksiTidakValidException("[GAGAL] Transaksi tidak ditemukan.");
        }
        if (t.getStatus() != StatusTransaksi.MENUNGGU) {
            throw new TransaksiTidakValidException(
                    "[GAGAL] Transaksi " + t.getId() + " tidak dapat dibatalkan karena tidak berstatus MENUNGGU.");
        }
        t.ubahStatus(StatusTransaksi.DIBATALKAN);
        daftarTransaksi.simpan();
    }

    public void lihatRiwayat() {
        ArrayList<Transaksi> list = getRiwayat();
        if (list.isEmpty()) {
            System.out.println("[INFO] Belum ada riwayat sewa.");
            return;
        }

        System.out.printf("%-10s  %-12s  %-12s  %-12s  %12s  %-10s%n",
                "ID", "Tgl Sewa", "Jatuh Tempo", "Tgl Kembali", "Total", "Status");
        System.out.println("----------  ------------  ------------  ------------  ------------  ----------");
        for (Transaksi t : list) {
            System.out.printf("%-10s  %-12s  %-12s  %-12s  %12s  %-10s%n",
                    t.getId(),
                    FormatHelper.tanggal(t.getTanggalSewa()),
                    FormatHelper.tanggal(t.getTanggalJatuhTempo()),
                    FormatHelper.tanggal(t.getTanggalKembali()),
                    FormatHelper.rupiah(t.hitungTotalAkhir()),
                    t.getStatus().name());
        }
    }

    public ArrayList<Transaksi> getRiwayat() {
        this.riwayat.clear();
        if (daftarTransaksi != null) {
            ArrayList<Transaksi> miliknya = daftarTransaksi.filterPenyewa(this);
            for (Transaksi t : miliknya) {
                if (t.getStatus() == StatusTransaksi.DISEWA || t.getStatus() == StatusTransaksi.SELESAI) {
                    this.riwayat.add(t);
                }
            }
        }
        return new ArrayList<>(this.riwayat);
    }
}
