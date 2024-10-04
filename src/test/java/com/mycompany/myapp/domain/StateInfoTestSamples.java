package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StateInfoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StateInfo getStateInfoSample1() {
        return new StateInfo().id(1L).stateId(1).stateName("stateName1");
    }

    public static StateInfo getStateInfoSample2() {
        return new StateInfo().id(2L).stateId(2).stateName("stateName2");
    }

    public static StateInfo getStateInfoRandomSampleGenerator() {
        return new StateInfo().id(longCount.incrementAndGet()).stateId(intCount.incrementAndGet()).stateName(UUID.randomUUID().toString());
    }
}
