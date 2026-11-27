package com.rentalmusik.util;

import com.rentalmusik.exception.InputTidakValidException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Utilitas untuk membaca dan memvalidasi input pengguna dari konsol.
 *
 * @author Kelompok 4
 */
public final class InputHelper {

    // semua input memakai satu Scanner
    private static final Scanner SCANNER = new Scanner(System.in);

    private InputHelper() {
    }

    public static String bacaBarisMentah(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        try {
            return SCANNER.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("\n[INFO] Input ditutup. Program berhenti.");
            System.exit(0);
            return "";
        }
    }

    public static String bacaString(String prompt) {
        return bacaBarisMentah(prompt).trim();
    }

    public static String bacaPassword(String prompt) {
        return bacaBarisMentah(prompt);
    }

    public static int bacaInt(String prompt) throws InputTidakValidException {
        String baris = bacaString(prompt);
        try {
            return Integer.parseInt(baris);
        } catch (NumberFormatException e) {
            throw new InputTidakValidException("Input harus berupa angka bulat.");
        }
    }

    public static Integer bacaIntOpsional(String prompt) throws InputTidakValidException {
        String baris = bacaString(prompt);
        if (baris.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(baris);
        } catch (NumberFormatException e) {
            throw new InputTidakValidException("Input harus berupa angka bulat.");
        }
    }

    public static int bacaIntRentang(String prompt, int min, int maks) throws InputTidakValidException {
        int nilai = bacaInt(prompt);
        if (nilai < min || nilai > maks) {
            throw new InputTidakValidException("Nilai harus antara " + min + " dan " + maks + ".");
        }
        return nilai;
    }

    public static int bacaIntRentang(String prompt, int min, int maks, String pesanKesalahanRentang)
            throws InputTidakValidException {
        int nilai = bacaInt(prompt);
        if (nilai < min || nilai > maks) {
            throw new InputTidakValidException(pesanKesalahanRentang);
        }
        return nilai;
    }

    public static LocalDate bacaTanggal(String prompt) throws InputTidakValidException {
        String baris = bacaString(prompt);
        return FormatHelper.parseTanggal(baris);
    }

    public static boolean bacaYaTidak(String prompt) throws InputTidakValidException {
        String baris = bacaString(prompt);
        if ("y".equalsIgnoreCase(baris)) {
            return true;
        } else if ("n".equalsIgnoreCase(baris)) {
            return false;
        } else {
            throw new InputTidakValidException("Jawaban harus y atau n.");
        }
    }

    public static void bacaEnter(String prompt) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        try {
            SCANNER.nextLine();
        } catch (NoSuchElementException e) {
            System.out.println("\n[INFO] Input ditutup. Program berhenti.");
            System.exit(0);
        }
    }
}
