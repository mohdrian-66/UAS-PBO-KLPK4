package com.rentalmusik.pembayaran;

import com.rentalmusik.exception.PembayaranGagalException;
import com.rentalmusik.util.FormatHelper;
import com.rentalmusik.util.InputHelper;

/**
 * Implementasi metode pembayaran melalui QRIS.
 *
 * @author Kelompok 4
 */
public class QRIS implements Pembayaran {

    private String kodeTransaksi;

    public QRIS() {
        this("");
    }

    public QRIS(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    @Override
    public String getNama() {
        return "QRIS";
    }

    @Override
    public String getKode() {
        return "QRIS";
    }

    @Override
    public double getBiayaAdmin() {
        return 0.0;
    }

    /**
     * Memproses pembayaran QRIS dengan menampilkan kode QR teks.
     */
    @Override
    public void prosesPembayaran(double jumlah) throws PembayaranGagalException {
        System.out.println("--- PEMBAYARAN: QRIS ---");
        System.out.println("#######  ##  #######");
        System.out.println("#     #  ## #     #");
        System.out.println("# ### # ##   # ### #");
        System.out.println("# ### #  # # # ### #");
        System.out.println("# ### # #  ## # ### #");
        System.out.println("#     # ## # #     #");
        System.out.println("####### # # # #######");
        if (kodeTransaksi != null && !kodeTransaksi.isEmpty()) {
            System.out.println("Kode   : QRIS-" + kodeTransaksi);
        }
        System.out.println("Jumlah : " + FormatHelper.rupiah(jumlah));
        InputHelper.bacaEnter("Tekan ENTER setelah pembayaran QRIS berhasil...");
        System.out.println("[OK] Pembayaran QRIS sebesar " + FormatHelper.rupiah(jumlah) + " diterima.");
    }
}
