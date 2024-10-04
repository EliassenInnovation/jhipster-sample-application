package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RoleMstTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static RoleMst getRoleMstSample1() {
        return new RoleMst().id(1L).createdBy(1).parentRoleId(1).roleId(1).roleName("roleName1").updatedBy(1);
    }

    public static RoleMst getRoleMstSample2() {
        return new RoleMst().id(2L).createdBy(2).parentRoleId(2).roleId(2).roleName("roleName2").updatedBy(2);
    }

    public static RoleMst getRoleMstRandomSampleGenerator() {
        return new RoleMst()
            .id(longCount.incrementAndGet())
            .createdBy(intCount.incrementAndGet())
            .parentRoleId(intCount.incrementAndGet())
            .roleId(intCount.incrementAndGet())
            .roleName(UUID.randomUUID().toString())
            .updatedBy(intCount.incrementAndGet());
    }
}
