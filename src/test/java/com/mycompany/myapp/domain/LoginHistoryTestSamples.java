package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LoginHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static LoginHistory getLoginHistorySample1() {
        return new LoginHistory().id(1L).forgotPin(1).loginLogId(1).loginType("loginType1").userId(1);
    }

    public static LoginHistory getLoginHistorySample2() {
        return new LoginHistory().id(2L).forgotPin(2).loginLogId(2).loginType("loginType2").userId(2);
    }

    public static LoginHistory getLoginHistoryRandomSampleGenerator() {
        return new LoginHistory()
            .id(longCount.incrementAndGet())
            .forgotPin(intCount.incrementAndGet())
            .loginLogId(intCount.incrementAndGet())
            .loginType(UUID.randomUUID().toString())
            .userId(intCount.incrementAndGet());
    }
}
