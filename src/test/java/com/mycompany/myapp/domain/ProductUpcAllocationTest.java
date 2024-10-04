package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductUpcAllocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductUpcAllocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductUpcAllocation.class);
        ProductUpcAllocation productUpcAllocation1 = getProductUpcAllocationSample1();
        ProductUpcAllocation productUpcAllocation2 = new ProductUpcAllocation();
        assertThat(productUpcAllocation1).isNotEqualTo(productUpcAllocation2);

        productUpcAllocation2.setId(productUpcAllocation1.getId());
        assertThat(productUpcAllocation1).isEqualTo(productUpcAllocation2);

        productUpcAllocation2 = getProductUpcAllocationSample2();
        assertThat(productUpcAllocation1).isNotEqualTo(productUpcAllocation2);
    }
}
