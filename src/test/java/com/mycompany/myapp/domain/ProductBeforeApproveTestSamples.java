package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductBeforeApproveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProductBeforeApprove getProductBeforeApproveSample1() {
        return new ProductBeforeApprove()
            .id(1L)
            .addedSugarUom("addedSugarUom1")
            .brandName("brandName1")
            .caloriesUom("caloriesUom1")
            .carbohydratesUom("carbohydratesUom1")
            .categoryId(1)
            .cholesterolUOM("cholesterolUOM1")
            .createdBy(1)
            .dietaryFiberUom("dietaryFiberUom1")
            .iocCategoryId(1)
            .manufacturerId(1)
            .manufacturerProductCode("manufacturerProductCode1")
            .productId(1L)
            .productName("productName1")
            .proteinUom("proteinUom1")
            .servingUom("servingUom1")
            .sodiumUom("sodiumUom1")
            .storageTypeId(1)
            .subCategoryId(1)
            .sugarUom("sugarUom1")
            .updatedBy(1)
            .vendor("vendor1");
    }

    public static ProductBeforeApprove getProductBeforeApproveSample2() {
        return new ProductBeforeApprove()
            .id(2L)
            .addedSugarUom("addedSugarUom2")
            .brandName("brandName2")
            .caloriesUom("caloriesUom2")
            .carbohydratesUom("carbohydratesUom2")
            .categoryId(2)
            .cholesterolUOM("cholesterolUOM2")
            .createdBy(2)
            .dietaryFiberUom("dietaryFiberUom2")
            .iocCategoryId(2)
            .manufacturerId(2)
            .manufacturerProductCode("manufacturerProductCode2")
            .productId(2L)
            .productName("productName2")
            .proteinUom("proteinUom2")
            .servingUom("servingUom2")
            .sodiumUom("sodiumUom2")
            .storageTypeId(2)
            .subCategoryId(2)
            .sugarUom("sugarUom2")
            .updatedBy(2)
            .vendor("vendor2");
    }

    public static ProductBeforeApprove getProductBeforeApproveRandomSampleGenerator() {
        return new ProductBeforeApprove()
            .id(longCount.incrementAndGet())
            .addedSugarUom(UUID.randomUUID().toString())
            .brandName(UUID.randomUUID().toString())
            .caloriesUom(UUID.randomUUID().toString())
            .carbohydratesUom(UUID.randomUUID().toString())
            .categoryId(intCount.incrementAndGet())
            .cholesterolUOM(UUID.randomUUID().toString())
            .createdBy(intCount.incrementAndGet())
            .dietaryFiberUom(UUID.randomUUID().toString())
            .iocCategoryId(intCount.incrementAndGet())
            .manufacturerId(intCount.incrementAndGet())
            .manufacturerProductCode(UUID.randomUUID().toString())
            .productId(longCount.incrementAndGet())
            .productName(UUID.randomUUID().toString())
            .proteinUom(UUID.randomUUID().toString())
            .servingUom(UUID.randomUUID().toString())
            .sodiumUom(UUID.randomUUID().toString())
            .storageTypeId(intCount.incrementAndGet())
            .subCategoryId(intCount.incrementAndGet())
            .sugarUom(UUID.randomUUID().toString())
            .updatedBy(intCount.incrementAndGet())
            .vendor(UUID.randomUUID().toString());
    }
}
