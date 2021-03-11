package com.vectorsearch.faiss.utils;

import java.io.IOException;

public class JFaissInitializer {

    private static volatile boolean initialized = false;

    public static void initialize() throws Exception {
        if (!initialized) {
            initialized = true;
            if (JFaissConstants.isUnix()) {
                NativeUtils2.loadLibraryFromJar(JFaissConstants.SWIGFAISS_SO_FILE_UNIX);
            } else if (JFaissConstants.isMac()) {
                NativeUtils2.loadLibraryFromJar(JFaissConstants.SWIGFAISS_SO_FILE_DARWIN);
            } else {
                throw new Exception("This OS is not supported");
            }
        }
    }
}
