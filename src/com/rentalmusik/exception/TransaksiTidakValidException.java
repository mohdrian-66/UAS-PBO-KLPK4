package com.rentalmusik.exception;

/**
 * Dilempar ketika transaksi tidak ditemukan, transisi status tidak sah, atau data melanggar aturan.
 *
 * @author Kelompok 4
 */
public class TransaksiTidakValidException extends RentalException {

    public TransaksiTidakValidException(String pesan) {
        super(pesan);
    }
}
