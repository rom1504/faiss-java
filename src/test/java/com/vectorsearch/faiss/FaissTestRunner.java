package com.vectorsearch.faiss;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.vectorsearch.faiss.utils.JFaissConstants;
import java.io.*;
import java.util.stream.*;

public class FaissTestRunner {

    @Test
    public void runUnitTests() {
        System.out.print("hiiii");
        if (JFaissConstants.isValidOS()) {
            final Result result = JUnitCore.runClasses(FaissTestSuite.class);
            for (final Failure failure : result.getFailures()) {
        System.out.println("oh no " + failure);
                System.out.println(failure.toString());
            }
        System.out.print("wow");
            System.out.println(result.wasSuccessful());
        } else {
            System.out.print("Not supported os:" + System.getProperty("os.name"));
        }
    }
}
