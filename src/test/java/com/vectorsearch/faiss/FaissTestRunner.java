package com.vectorsearch.faiss;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static com.vectorsearch.faiss.utils.JFaissConstants.SUPPORTED_OS;
import java.io.*;

public class FaissTestRunner {

    public static boolean isValidOS() {
        return SUPPORTED_OS.contains(System.getProperty("os.name"));
    }

    @Test
    public void runUnitTests() {
        System.out.print("hiiii");
        if (isValidOS()) {
            final Result result = JUnitCore.runClasses(FaissTestSuite.class);
            for (final Failure failure : result.getFailures()) {
        System.out.println("oh no " + failure);
                System.out.println(failure.toString());
            }
        System.out.print("wow");
            System.out.println(result.wasSuccessful());
        }
    }
}
