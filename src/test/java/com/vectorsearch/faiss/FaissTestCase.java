package com.vectorsearch.faiss;

import com.vectorsearch.faiss.utils.JFaissInitializer;

import java.io.IOException;
import java.util.Random;
import java.io.*;

public abstract class FaissTestCase {
    public static final Random random = new Random(42);

    public abstract void train();

    public abstract void search();

    static {
        try {
            System.out.println("hi");
            JFaissInitializer.initialize();
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
