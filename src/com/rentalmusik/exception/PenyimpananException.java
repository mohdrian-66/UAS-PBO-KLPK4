package com.rentalmusik.exception;

/**
 * Dilempar ketika terjadi kesalahan I/O saat membaca atau menulis file data penyimpanan.
 *
 * @author Kelompok 4
 */
public class PenyimpananException extends RentalException {

    public PenyimpananException(String pesan) {
        super(pesan);
    }

    public PenyimpananException(String pesan, Throwable penyebab) {
        super(pesan, penyebab);
    }
}
