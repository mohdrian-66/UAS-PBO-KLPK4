package com.rentalmusik.app;

import com.rentalmusik.exception.InputTidakValidException;
import com.rentalmusik.exception.LoginGagalException;
import com.rentalmusik.exception.PenyimpananException;
import com.rentalmusik.model.Admin;
import com.rentalmusik.model.Akun;
import com.rentalmusik.model.Penyewa;
import com.rentalmusik.service.DaftarBarang;
import com.rentalmusik.service.DaftarTransaksi;
import com.rentalmusik.service.PenyimpananFile;
import com.rentalmusik.ui.MenuAdmin;
import com.rentalmusik.ui.MenuPenyewa;
import com.rentalmusik.util.FormatHelper;
import com.rentalmusik.util.InputHelper;
import com.rentalmusik.util.Konstanta;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Titik masuk utama aplikasi Sistem Rental Alat Musik.
 *
 * @author Kelompok 4
 */
public class Main {

    private Akun akunAktif;
    private final ArrayList<Akun> daftarAkun;
    private final DaftarBarang daftarBarang;
    private final DaftarTransaksi daftarTransaksi;

    public Main() throws PenyimpananException {
        this.daftarAkun = new ArrayList<>();

        PenyimpananFile penyimpananFile = new PenyimpananFile();
        this.daftarBarang = new DaftarBarang(penyimpananFile);

        HashMap<String, Penyewa> petaPenyewa = new HashMap<>();
        Penyewa penyewa = new Penyewa("PNY-001", "penyewa", "penyewa123", "Penyewa Demo",
                this.daftarBarang, null);
        petaPenyewa.put(penyewa.getId(), penyewa);

        this.daftarTransaksi = new DaftarTransaksi(penyimpananFile, petaPenyewa);
        penyewa.setDaftarTransaksi(this.daftarTransaksi);

        Admin admin = new Admin("ADM-001", "admin", "admin123", "Administrator",
                this.daftarBarang, this.daftarTransaksi);

        this.daftarAkun.add(admin);
        this.daftarAkun.add(penyewa);
    }

    /**
     * Memproses alur login pengguna berdasarkan pilihan peran.
     */
    public void login(String peran) {
        String judulPeran = peran.toUpperCase();
        System.out.println("\n--- LOGIN " + judulPeran + " ---");

        // batas 3 kali percobaan login
        int sisaPercobaan = Konstanta.MAKS_PERCOBAAN_LOGIN;
        while (sisaPercobaan > 0) {
            String username = InputHelper.bacaString("Username: ");
            String password = InputHelper.bacaPassword("Password: ");

            Akun akunDitemukan = null;
            if (!username.isEmpty()) {
                for (Akun a : daftarAkun) {
                    if (a.getUsername().equals(username) && a.getPeran().equalsIgnoreCase(peran)) {
                        if (a.login(username, password)) {
                            akunDitemukan = a;
                            break;
                        }
                    }
                }
            }

            if (akunDitemukan != null) {
                this.akunAktif = akunDitemukan;
                System.out.printf("[OK] Login berhasil. Selamat datang, %s (%s).%n",
                        akunAktif.getNama(), akunAktif.getPeran());
                return;
            }

            sisaPercobaan--;
            if (sisaPercobaan > 0) {
                System.out.printf("[GAGAL] Username atau password salah. Sisa percobaan: %d.%n", sisaPercobaan);
            } else {
                System.out.println("[GAGAL] Percobaan login habis. Program berhenti.");
                System.exit(1);
            }
        }
    }

    public void mulai() {
        boolean berjalan = true;
        while (berjalan) {
            System.out.println(FormatHelper.garis('=', 50));
            System.out.println("            SISTEM RENTAL ALAT MUSIK");
            System.out.println(FormatHelper.garis('=', 50));
            System.out.println("1. Login sebagai Admin");
            System.out.println("2. Login sebagai Penyewa");
            System.out.println("3. Keluar");

            int pilihan;
            try {
                pilihan = InputHelper.bacaIntRentang("Pilih menu [1-3]: ", 1, 3, "Pilihan tidak valid.");
            } catch (InputTidakValidException e) {
                System.out.println("Pilihan tidak valid.");
                continue;
            }

            switch (pilihan) {
                case 1 -> {
                    login("Admin");
                    if (akunAktif instanceof Admin admin) {
                        new MenuAdmin(admin).jalankan();
                        akunAktif = null;
                        System.out.println("[INFO] Anda telah logout.");
                    }
                }
                case 2 -> {
                    login("Penyewa");
                    if (akunAktif instanceof Penyewa penyewa) {
                        new MenuPenyewa(penyewa).jalankan();
                        akunAktif = null;
                        System.out.println("[INFO] Anda telah logout.");
                    }
                }
                case 3 -> {
                    System.out.println("[INFO] Terima kasih. Program selesai.");
                    berjalan = false;
                }
                default -> System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            Main aplikasi = new Main();
            aplikasi.mulai();
        } catch (PenyimpananException e) {
            System.err.println("[GAGAL] Gagal memulai aplikasi: " + e.getMessage());
            System.exit(1);
        }
    }
}
