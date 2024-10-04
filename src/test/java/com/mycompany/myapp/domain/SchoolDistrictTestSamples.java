package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SchoolDistrictTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static SchoolDistrict getSchoolDistrictSample1() {
        return new SchoolDistrict()
            .id(1L)
            .city("city1")
            .contractCompany("contractCompany1")
            .country("country1")
            .createdBy(1)
            .districtName("districtName1")
            .email("email1")
            .foodServiceOptions("foodServiceOptions1")
            .lastUpdatedBy(1)
            .phoneNumber("phoneNumber1")
            .schoolDistrictId(1)
            .siteCode("siteCode1")
            .state(1);
    }

    public static SchoolDistrict getSchoolDistrictSample2() {
        return new SchoolDistrict()
            .id(2L)
            .city("city2")
            .contractCompany("contractCompany2")
            .country("country2")
            .createdBy(2)
            .districtName("districtName2")
            .email("email2")
            .foodServiceOptions("foodServiceOptions2")
            .lastUpdatedBy(2)
            .phoneNumber("phoneNumber2")
            .schoolDistrictId(2)
            .siteCode("siteCode2")
            .state(2);
    }

    public static SchoolDistrict getSchoolDistrictRandomSampleGenerator() {
        return new SchoolDistrict()
            .id(longCount.incrementAndGet())
            .city(UUID.randomUUID().toString())
            .contractCompany(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .createdBy(intCount.incrementAndGet())
            .districtName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .foodServiceOptions(UUID.randomUUID().toString())
            .lastUpdatedBy(intCount.incrementAndGet())
            .phoneNumber(UUID.randomUUID().toString())
            .schoolDistrictId(intCount.incrementAndGet())
            .siteCode(UUID.randomUUID().toString())
            .state(intCount.incrementAndGet());
    }
}
