package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductDistributorAllocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductDistributorAllocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDistributorAllocation.class);
        ProductDistributorAllocation productDistributorAllocation1 = getProductDistributorAllocationSample1();
        ProductDistributorAllocation productDistributorAllocation2 = new ProductDistributorAllocation();
        assertThat(productDistributorAllocation1).isNotEqualTo(productDistributorAllocation2);

        productDistributorAllocation2.setId(productDistributorAllocation1.getId());
        assertThat(productDistributorAllocation1).isEqualTo(productDistributorAllocation2);

        productDistributorAllocation2 = getProductDistributorAllocationSample2();
        assertThat(productDistributorAllocation1).isNotEqualTo(productDistributorAllocation2);
    }
}
