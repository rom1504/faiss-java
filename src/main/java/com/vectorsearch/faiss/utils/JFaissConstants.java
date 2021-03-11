package com.vectorsearch.faiss.utils;

import java.util.Collections;
import java.util.List;

public class JFaissConstants {
    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String SWIGFAISS_SO_FILE_UNIX = "/linux/libswigfaiss.so";
    public static final String SWIGFAISS_SO_FILE_DARWIN = "/darwin/libswigfaiss.jnilib";
    public static final String SWIGFAISS_SO_FILE_WINDOWS = "/win32/libswigfaiss.dll";

    public static final String[] REQUIRED_SO_FILE_UNIX = new String[]{
            "/libgomp.so.1", "/libquadmath.so.0", "/libgfortran.so.3", "/libopenblas.so.0"};

    public static final String[] REQUIRED_SO_FILE_DARWIN = new String[]{
            "/libgomp.dylib.1", "/libquadmath.dylib.0", "/libgfortran.dylib.3", "/libopenblas.dylib.0"};

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
