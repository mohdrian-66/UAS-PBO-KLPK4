package com.rentalmusik.model;

import com.rentalmusik.exception.TransaksiTidakValidException;
import com.rentalmusik.pembayaran.Pembayaran;
import com.rentalmusik.util.FormatHelper;
import com.rentalmusik.util.Konstanta;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Entitas yang merepresentasikan transaksi peminjaman alat musik.
 *
 * @author Kelompok 4
 */
public class Transaksi {

    private String id;
    private Penyewa penyewa;
    private ArrayList<ItemSewa> items;
    private int lamaSewa;
    private LocalDate tanggalDibuat;
    private LocalDate tanggalSewa;
    private LocalDate tanggalJatuhTempo;
    private LocalDate tanggalKembali;
    private StatusTransaksi status;
    private Pembayaran pembayaran;

    public Transaksi(String id, Penyewa penyewa, ArrayList<ItemSewa> items, int lamaSewa,
                     LocalDate tanggalDibuat, Pembayaran pembayaran) {
        this.id = id;
        this.penyewa = penyewa;
        this.items = items != null ? items : new ArrayList<>();
        this.lamaSewa = lamaSewa;
        this.tanggalDibuat = tanggalDibuat;
        this.pembayaran = pembayaran;
        this.status = StatusTransaksi.MENUNGGU;
        this.tanggalSewa = null;
        this.tanggalJatuhTempo = null;
        this.tanggalKembali = null;
    }

    public Transaksi(String id, Penyewa penyewa, ArrayList<ItemSewa> items, int lamaSewa,
                     LocalDate tanggalDibuat, LocalDate tanggalSewa, LocalDate tanggalJatuhTempo,
                     LocalDate tanggalKembali, StatusTransaksi status, Pembayaran pembayaran) {
        this.id = id;
        this.penyewa = penyewa;
        this.items = items != null ? items : new ArrayList<>();
        this.lamaSewa = lamaSewa;
        this.tanggalDibuat = tanggalDibuat;
        this.tanggalSewa = tanggalSewa;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
        this.tanggalKembali = tanggalKembali;
        this.status = status;
        this.pembayaran = pembayaran;
    }

    public double hitungSubtotal() {
        double subtotal = 0.0;
        for (ItemSewa item : items) {
            subtotal += item.getBiayaTotal();
        }
        return subtotal;
    }

    public double hitungBiayaAdmin() {
        if (pembayaran != null) {
            return pembayaran.getBiayaAdmin();
        }
        return 0.0;
    }

    public double hitungTotal() {
        return hitungSubtotal() + hitungBiayaAdmin();
    }

    /**
     * Menghitung jumlah hari keterlambatan pengembalian dari tanggal jatuh tempo.
     */
    public int hitungHariTerlambat() {
        // tidak terlambat bila kembali tepat jatuh tempo
        if (tanggalKembali == null || tanggalJatuhTempo == null) {
            return 0;
        }
        long selisih = ChronoUnit.DAYS.between(tanggalJatuhTempo, tanggalKembali);
        return (int) Math.max(0, selisih);
    }

    /**
     * Menghitung total denda berdasarkan jumlah barang dan hari keterlambatan.
     */
    public double hitungDenda() {
        // hari terlambat x jumlah barang x denda per hari
        int hariTerlambat = hitungHariTerlambat();
        return (double) hariTerlambat * items.size() * Konstanta.DENDA_PER_BARANG_PER_HARI;
    }

    public double hitungTotalAkhir() {
        return hitungTotal() + hitungDenda();
    }

    /**
     * Mengubah status transaksi sesuai aturan transisi siklus transaksi.
     */
    public void ubahStatus(StatusTransaksi baru) throws TransaksiTidakValidException {
        // hanya transisi status yang sah
        if (this.status == baru) {
            return;
        }

        boolean valid = switch (this.status) {
            case MENUNGGU -> baru == StatusTransaksi.DISEWA
                    || baru == StatusTransaksi.DITOLAK
                    || baru == StatusTransaksi.DIBATALKAN;
            case DISEWA -> baru == StatusTransaksi.SELESAI;
            case SELESAI, DITOLAK, DIBATALKAN -> false;
        };

        if (!valid) {
            throw new TransaksiTidakValidException(
                    "Transisi status dari " + this.status.getNamaTampil()
                            + " ke " + baru.getNamaTampil() + " tidak valid.");
        }

        this.status = baru;
    }

    public void mulaiSewa(LocalDate tanggal) throws TransaksiTidakValidException {
        ubahStatus(StatusTransaksi.DISEWA);
        this.tanggalSewa = tanggal;
        this.tanggalJatuhTempo = tanggal.plusDays(this.lamaSewa);
    }

    public void selesaikan(LocalDate tanggal) throws TransaksiTidakValidException {
        if (this.tanggalSewa != null && tanggal.isBefore(this.tanggalSewa)) {
            throw new TransaksiTidakValidException(
                    "Tanggal kembali tidak boleh sebelum tanggal sewa ("
                            + FormatHelper.tanggal(this.tanggalSewa) + ").");
        }
        ubahStatus(StatusTransaksi.SELESAI);
        this.tanggalKembali = tanggal;
    }

    public String getId() {
        return id;
    }

    public Penyewa getPenyewa() {
        return penyewa;
    }

    public void setPenyewa(Penyewa penyewa) {
        this.penyewa = penyewa;
    }

    public ArrayList<ItemSewa> getItems() {
        return items;
    }

    public int getLamaSewa() {
        return lamaSewa;
    }

    public LocalDate getTanggalDibuat() {
        return tanggalDibuat;
    }

    public LocalDate getTanggalSewa() {
        return tanggalSewa;
    }

    public LocalDate getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    public LocalDate getTanggalKembali() {
        return tanggalKembali;
    }

    public StatusTransaksi getStatus() {
        return status;
    }

    public Pembayaran getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
    }
}
