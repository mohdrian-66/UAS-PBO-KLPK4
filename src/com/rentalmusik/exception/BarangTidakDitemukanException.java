package com.rentalmusik.exception;

/**
 * Dilempar ketika barang dengan ID tertentu tidak ditemukan pada daftar barang atau keranjang.
 *
 * @author Kelompok 4
 */
public class BarangTidakDitemukanException extends RentalException {

    public BarangTidakDitemukanException(String pesan) {
        super(pesan);
    }
}
