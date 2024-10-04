package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductGtinAllocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductGtinAllocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductGtinAllocation.class);
        ProductGtinAllocation productGtinAllocation1 = getProductGtinAllocationSample1();
        ProductGtinAllocation productGtinAllocation2 = new ProductGtinAllocation();
        assertThat(productGtinAllocation1).isNotEqualTo(productGtinAllocation2);

        productGtinAllocation2.setId(productGtinAllocation1.getId());
        assertThat(productGtinAllocation1).isEqualTo(productGtinAllocation2);

        productGtinAllocation2 = getProductGtinAllocationSample2();
        assertThat(productGtinAllocation1).isNotEqualTo(productGtinAllocation2);
    }
}
