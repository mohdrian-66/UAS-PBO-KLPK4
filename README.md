# Rental System with Java — Rental Alat Musik

## Deskripsi

Rental System with Java merupakan aplikasi penyewaan alat musik berbasis Java dengan antarmuka Command Line Interface (CLI).

Aplikasi ini dibuat untuk memenuhi tugas UAS Praktikum Pemrograman Berorientasi Objek (PBO). Sistem menyediakan dua jenis akun, yaitu Admin dan Penyewa.

### Fitur Aplikasi

- Login Admin dan Penyewa.
- Pengelolaan barang oleh Admin (tambah, edit, dan hapus).
- Melihat daftar alat musik yang tersedia.
- Menambahkan beberapa barang ke keranjang sewa.
- Melakukan transaksi penyewaan berdasarkan lama sewa.
- Mendukung metode pembayaran Transfer Bank, QRIS, dan Tunai.
- Penerimaan transaksi dan konfirmasi pengembalian oleh Admin.
- Pembuatan invoice dan riwayat transaksi.
- Perhitungan denda keterlambatan pengembalian.

### Teknologi

- Bahasa pemrograman: Java
- Antarmuka: Command Line Interface (CLI)
- Penyimpanan data: File TXT
- Version Control: Git dan GitHub

## Daftar Anggota Kelompok 4

1. Mohd Rian Maulana (250810701100082)
2. Shelvya Pratiwi (250810701100041)
3. Nur Asysyifa Nadhira (250810701100060)

## Cara Menjalankan Program

1. Pastikan Java JDK sudah terpasang.
2. Buka terminal pada folder utama proyek.
3. Kompilasi seluruh source code Java:

   ```powershell
   $files = Get-ChildItem src -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
   javac -encoding UTF-8 -d out $files
   ```

4. Jalankan aplikasi:

   ```powershell
   java -cp out com.rentalmusik.app.Main
   ```

---

**UAS Praktikum Pemrograman Berorientasi Objek (PBO)**  
**Kelompok 4**
