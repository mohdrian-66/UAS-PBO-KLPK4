package com.rentalmusik.exception;

/**
 * Dilempar ketika input pengguna melanggar aturan format, rentang nilai, atau keunikan data.
 *
 * @author Kelompok 4
 */
public class InputTidakValidException extends RentalException {

    public InputTidakValidException(String pesan) {
        super(pesan);
    }
}
