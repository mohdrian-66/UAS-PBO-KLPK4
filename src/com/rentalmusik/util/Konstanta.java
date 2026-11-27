package com.rentalmusik.util;

/**
 * Nilai konstanta konfigurasi sistem rental alat musik.
 *
 * @author Kelompok 4
 */
public final class Konstanta {

    private Konstanta() {
    }

    public static final double DENDA_PER_BARANG_PER_HARI = 20000.0;
    public static final int LAMA_SEWA_MIN = 1;
    public static final int LAMA_SEWA_MAKS = 14;
    public static final int MAKS_PERCOBAAN_LOGIN = 3;
    public static final double BIAYA_ADMIN_TRANSFER = 2500.0;
    public static final double BIAYA_PERAWATAN_SENAR_DEFAULT = 5000.0;
    public static final double BIAYA_ANGKUT_SETUP_DEFAULT = 10000.0;
    public static final double BIAYA_SANITASI_DEFAULT = 5000.0;
    public static final double TARIF_MIN = 1000.0;
    public static final double TARIF_MAKS = 10000000.0;
    public static final double BIAYA_TAMBAHAN_MAKS = 10000000.0;
    public static final int PANJANG_NAMA_MAKS = 30;
    public static final String FOLDER_DATA = "data";
    public static final String REKENING_TRANSFER = "Bank Nusantara - 1234567890 a.n. Rental Alat Musik";
}
