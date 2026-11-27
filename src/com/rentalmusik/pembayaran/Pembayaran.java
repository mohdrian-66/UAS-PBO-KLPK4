package com.rentalmusik.pembayaran;

import com.rentalmusik.exception.InputTidakValidException;
import com.rentalmusik.exception.PembayaranGagalException;

/**
 * Interface untuk strategi metode pembayaran pada sistem rental alat musik.
 *
 * @author Kelompok 4
 */
public interface Pembayaran {

    String getNama();

    String getKode();

    double getBiayaAdmin();

    /**
     * Memproses alur pembayaran sebesar total tagihan.
     */
    void prosesPembayaran(double jumlah) throws PembayaranGagalException;

    static Pembayaran dariKode(String kode) throws InputTidakValidException {
        if (kode == null) {
            throw new InputTidakValidException("Kode pembayaran tidak boleh kosong.");
        }
        String kodeBersih = kode.trim().toUpperCase();
        return switch (kodeBersih) {
            case "TRANSFER" -> new TransferBank();
            case "QRIS" -> new QRIS();
            case "TUNAI" -> new Tunai();
            default -> throw new InputTidakValidException("Metode pembayaran '" + kode + "' tidak valid.");
        };
    }
}
