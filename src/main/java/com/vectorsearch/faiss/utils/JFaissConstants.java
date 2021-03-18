package com.vectorsearch.faiss.utils;

import java.util.Collections;
import java.util.List;

public class JFaissConstants {
    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String SWIGFAISS_SO_FILE_UNIX = "/linux/libswigfaiss.so";
    public static final String SWIGFAISS_SO_FILE_DARWIN = "/darwin/libswigfaiss.jnilib";
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

}
