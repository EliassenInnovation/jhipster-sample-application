package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ErrorLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ErrorLog getErrorLogSample1() {
        return new ErrorLog().id(1L).errorId(1);
    }

    public static ErrorLog getErrorLogSample2() {
        return new ErrorLog().id(2L).errorId(2);
    }

    public static ErrorLog getErrorLogRandomSampleGenerator() {
        return new ErrorLog().id(longCount.incrementAndGet()).errorId(intCount.incrementAndGet());
    }
}
