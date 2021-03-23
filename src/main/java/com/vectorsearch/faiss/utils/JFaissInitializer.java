package com.vectorsearch.faiss.utils;

import java.io.IOException;

public class JFaissInitializer {

    private static volatile boolean initialized = false;

    public static void initialize() throws Exception {
        if (!initialized) {
            initialized = true;
            boolean isAvxAvailable = JFaissConstants.isAvx2Available();
            if (JFaissConstants.isUnix()) {
                String faiss_library = JFaissConstants.SWIGFAISS_SO_FILE_UNIX;
                if (isAvxAvailable) {
                    faiss_library = JFaissConstants.SWIGFAISS_SO_FILE_AVX2_UNIX;
                }
                NativeUtils2.loadLibraryFromJar(faiss_library, JFaissConstants.REQUIRED_SO_FILE_UNIX);
            } else if (JFaissConstants.isMac()) {
                String faiss_library = JFaissConstants.SWIGFAISS_SO_FILE_DARWIN;
                if (isAvxAvailable) {
                    faiss_library = JFaissConstants.SWIGFAISS_SO_FILE_AVX2_DARWIN;
                }
                NativeUtils2.loadLibraryFromJar(faiss_library);
            } else {
                throw new Exception("This OS is not supported");
            }
        }
    }
}
