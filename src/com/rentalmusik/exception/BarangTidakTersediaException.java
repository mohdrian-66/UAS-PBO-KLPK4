package com.rentalmusik.exception;

/**
 * Dilempar ketika barang sedang disewa, diajukan dalam transaksi, atau tidak berstatus tersedia.
 *
 * @author Kelompok 4
 */
public class BarangTidakTersediaException extends RentalException {

    public BarangTidakTersediaException(String pesan) {
        super(pesan);
    }
}
