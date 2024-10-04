package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductManufacturerAllocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductManufacturerAllocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductManufacturerAllocation.class);
        ProductManufacturerAllocation productManufacturerAllocation1 = getProductManufacturerAllocationSample1();
        ProductManufacturerAllocation productManufacturerAllocation2 = new ProductManufacturerAllocation();
        assertThat(productManufacturerAllocation1).isNotEqualTo(productManufacturerAllocation2);

        productManufacturerAllocation2.setId(productManufacturerAllocation1.getId());
        assertThat(productManufacturerAllocation1).isEqualTo(productManufacturerAllocation2);

        productManufacturerAllocation2 = getProductManufacturerAllocationSample2();
        assertThat(productManufacturerAllocation1).isNotEqualTo(productManufacturerAllocation2);
    }
}
