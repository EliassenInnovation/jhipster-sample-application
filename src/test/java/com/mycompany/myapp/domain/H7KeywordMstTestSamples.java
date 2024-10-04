package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class H7KeywordMstTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static H7KeywordMst getH7KeywordMstSample1() {
        return new H7KeywordMst().id(1L).h7Group("h7Group1").h7Keyword("h7Keyword1").h7keywordId(1).iocGroup("iocGroup1");
    }

    public static H7KeywordMst getH7KeywordMstSample2() {
        return new H7KeywordMst().id(2L).h7Group("h7Group2").h7Keyword("h7Keyword2").h7keywordId(2).iocGroup("iocGroup2");
    }

    public static H7KeywordMst getH7KeywordMstRandomSampleGenerator() {
        return new H7KeywordMst()
            .id(longCount.incrementAndGet())
            .h7Group(UUID.randomUUID().toString())
            .h7Keyword(UUID.randomUUID().toString())
            .h7keywordId(intCount.incrementAndGet())
            .iocGroup(UUID.randomUUID().toString());
    }
}
