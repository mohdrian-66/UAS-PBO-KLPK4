package com.rentalmusik.exception;

/**
 * Kelas dasar checked exception untuk seluruh exception pada sistem rental alat musik.
 *
 * @author Kelompok 4
 */
public class RentalException extends Exception {

    public RentalException(String pesan) {
        super(pesan);
    }

    public RentalException(String pesan, Throwable penyebab) {
        super(pesan, penyebab);
    }
}
