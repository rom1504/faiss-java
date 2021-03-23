package com.vectorsearch.faiss.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class JFaissConstants {
    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String SWIGFAISS_SO_FILE_UNIX = "/linux/libswigfaiss.so";
    public static final String SWIGFAISS_SO_FILE_AVX2_UNIX = "/linux/libswigfaiss_avx2.so";

    public static final String SWIGFAISS_SO_FILE_DARWIN = "/darwin/libswigfaiss.jnilib";
    public static final String SWIGFAISS_SO_FILE_AVX2_DARWIN = "/darwin/libswigfaiss_avx2.jnilib";
    public static final String SWIGFAISS_SO_FILE_WINDOWS = "/win32/libswigfaiss.dll";

    public static final String[] REQUIRED_SO_FILE_UNIX = new String[]{
            "/linux/libgomp.so.1", "/linux/libquadmath.so.0", "/linux/libgfortran.so.3", "/linux/libopenblas.so.0"};

    public static boolean isValidOS() {
        return isUnix() || isMac() || isWindows();
    }

    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0
                || OS.indexOf("nux") >= 0
                || OS.indexOf("aix") > 0);
    }


    private static boolean isAvxdefined = false;
    private static boolean isAvx2Available = false;

    private static boolean isAvx2AvailableDarwin() throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("/usr/sbin/sysctl", "hw.optional.avx2_0");
        Process process = builder.start();
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            sb.append(line);
            sb.append(System.getProperty("line.separator"));
        }
        String result = sb.toString().trim();
        System.out.println(result);
        process.waitFor();
        return result.charAt(result.length() - 1) == '1';
    }

    private static boolean isAvx2AvailableUnix() throws Exception {
        Path cpuinfoPath = Paths.get("/proc/cpuinfo");
        return Files.lines(cpuinfoPath).anyMatch(s -> s.toLowerCase().contains("avx2"));
    }

    public static boolean isAvx2Available() {
        if (isAvxdefined) {
            return isAvx2Available;
        }
        try {
            if (isMac()) {
                isAvx2Available = isAvx2AvailableDarwin();
                isAvxdefined = true;
            } else if (isUnix()) {
                isAvx2Available = isAvx2AvailableUnix();
                isAvxdefined = true;
            }
            return isAvx2Available;
        }
        catch (Exception e) {
            System.err.println("Error when fetch avx2 capability:" + e.toString());
            return false;
        }
    }

}
