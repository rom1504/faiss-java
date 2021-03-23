package com.vectorsearch.faiss.utils;

import java.io.IOException;

public class JFaissInitializer {

    private static volatile boolean initialized = false;

    public static void initialize() throws Exception {
        if (!initialized) {
            initialized = true;
            boolean isAvxAvailable = JFaissConstants.isAvx2Available();
            if (JFaissConstants.isUnix()) {
                String faiss_library;
                if (isAvxAvailable) {
                    faiss_library = JFaissConstants.SWIGFAISS_SO_FILE_AVX2_UNIX;
                    System.out.println("Using AVX2 optmized library for faiss");
                } else {
                    faiss_library = JFaissConstants.SWIGFAISS_SO_FILE_UNIX;
                    System.out.println("AVX2 was not found on the system. Using generic library for faiss");
                }
                try {
                    NativeUtils2.loadLibraryFromJar(faiss_library, JFaissConstants.REQUIRED_SO_FILE_UNIX);
                } catch( Exception e) {
                    if (isAvxAvailable) {
                        // Fallback to generic library
                        System.out.println("AVX2 library was not found");
                        NativeUtils2.loadLibraryFromJar(JFaissConstants.SWIGFAISS_SO_FILE_UNIX, JFaissConstants.REQUIRED_SO_FILE_UNIX);
                    }
                }

            } else if (JFaissConstants.isMac()) {
                String faiss_library;
                if (isAvxAvailable) {
                    faiss_library = JFaissConstants.SWIGFAISS_SO_FILE_AVX2_DARWIN;
                    System.out.println("Using AVX2 optmized library for faiss");
                } else {
                    faiss_library = JFaissConstants.SWIGFAISS_SO_FILE_DARWIN;
                    System.out.println("AVX2 was not found on the system. Using generic library for faiss");
                }
                try {
                    NativeUtils2.loadLibraryFromJar(faiss_library);
                }
                catch( Exception e) {
                    if (isAvxAvailable) {
                        // Fallback to generic library
                        System.out.println("AVX2 library was not found");
                        NativeUtils2.loadLibraryFromJar(JFaissConstants.SWIGFAISS_SO_FILE_DARWIN);
                    }
                }
            } else {
                throw new Exception("This OS is not supported");
            }
        }
    }
}
