package com.rentalmusik.exception;

/**
 * Dilempar ketika proses pembayaran transaksi dibatalkan atau gagal diproses.
 *
 * @author Kelompok 4
 */
public class PembayaranGagalException extends RentalException {

    public PembayaranGagalException(String pesan) {
        super(pesan);
    }
}
