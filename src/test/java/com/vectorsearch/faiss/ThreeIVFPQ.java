package com.vectorsearch.faiss;

import com.vectorsearch.faiss.swig.*;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.vectorsearch.faiss.swig.swigfaiss.omp_set_num_threads;
import static com.vectorsearch.faiss.utils.IndexHelper.makeRandomFloatArray;
import static com.vectorsearch.faiss.utils.IndexHelper.show;

public class ThreeIVFPQ extends FaissTestCase {

    private static final Logger LOGGER = Logger.getLogger(ThreeIVFPQ.class.getName());
    private static final int dimension = 64;            // dimension of the vector
    private static final int inputRowCount = 100000;    // no of input vectors
    private static final int queryRowCount = 1;     // no of of query vectors
    private static final int nlist = 100;               // no of clusters
    private static final int m = 8;

    private final floatArray inputVectors;
    private final floatArray queryVectors;
    private final IndexHNSWFlat quantizer;
    private final IndexIVFPQ index;

    public ThreeIVFPQ() {
        inputVectors = makeRandomFloatArray(inputRowCount, dimension, random);
        queryVectors = makeRandomFloatArray(queryRowCount, dimension, random);
        quantizer = new IndexHNSWFlat(dimension, 15);
        index = new IndexIVFPQ(quantizer, dimension, nlist, m, 8);
    }

    @Override
    public void train() {
        index.train(inputRowCount, inputVectors.cast());
        index.add(inputRowCount, inputVectors.cast());
        final boolean isTrained = index.getIs_trained();
        final int nTotal = index.getNtotal();
        final String msg = "isTrained = " + isTrained + ", nTotal = " + nTotal;
        LOGGER.info(msg);
    }

    @Override
    public void search() {
        final int rn = 40;
        final floatArray distances = new floatArray(queryRowCount * rn);
        final longArray indices = new longArray(queryRowCount * rn);
        index.setNprobe(10);
        quantizer.getHnsw().setEfSearch(20);
        omp_set_num_threads(1);
        long startTime = System.nanoTime();
        for(int i=0;i<1000;i++) {
            index.search(queryRowCount, queryVectors.cast(), rn, distances.cast(), indices.cast());
        }
        long endTime = System.nanoTime();
        System.out.println("Total execution time search: " + (endTime-startTime)/1000/1000 + "µs");
        LOGGER.info(show(distances, 5, rn));
        LOGGER.info(show(indices, 5, rn));
    }

    @Test
    public void threeIVFPQTest() {
        final ThreeIVFPQ threeIVFPQ = new ThreeIVFPQ();
        LOGGER.info("****************************************************");
        LOGGER.info("Training index..");
        threeIVFPQ.train();
        LOGGER.info("Searching index..");
        threeIVFPQ.search();
        LOGGER.info("****************************************************");
        assert true;
    }
}
