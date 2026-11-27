package com.rentalmusik.util;

import com.rentalmusik.exception.InputTidakValidException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Utilitas untuk memformat mata uang, tanggal, perataan teks, dan garis batas.
 *
 * @author Kelompok 4
 */
public final class FormatHelper {

    public static final DateTimeFormatter FORMATTER_TAMPIL = DateTimeFormatter
            .ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    public static final DateTimeFormatter FORMATTER_FILE = DateTimeFormatter.ISO_LOCAL_DATE;

    private FormatHelper() {
    }

    public static String rupiah(double nilai) {
        long nilaiBulat = Math.round(nilai);
        String formatted = String.format(Locale.US, "%,d", nilaiBulat).replace(',', '.');
        return "Rp" + formatted;
    }

    public static String tanggal(LocalDate tanggal) {
        if (tanggal == null) {
            return "-";
        }
        return tanggal.format(FORMATTER_TAMPIL);
    }

    public static LocalDate parseTanggal(String input) throws InputTidakValidException {
        if (input == null || input.trim().isEmpty()) {
            throw new InputTidakValidException("Format tanggal harus dd-MM-yyyy dan tanggal harus valid.");
        }
        try {
            return LocalDate.parse(input.trim(), FORMATTER_TAMPIL);
        } catch (DateTimeParseException e) {
            throw new InputTidakValidException("Format tanggal harus dd-MM-yyyy dan tanggal harus valid.");
        }
    }

    public static String garis(char c, int lebar) {
        if (lebar <= 0) {
            return "";
        }
        return String.valueOf(c).repeat(lebar);
    }

    public static String rataKanan(String kiri, String kanan, int lebar) {
        if (kiri == null) {
            kiri = "";
        }
        if (kanan == null) {
            kanan = "";
        }
        int spasi = lebar - kiri.length() - kanan.length();
        if (spasi > 0) {
            return kiri + " ".repeat(spasi) + kanan;
        }
        return kiri + " " + kanan;
    }
}
