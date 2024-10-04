package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductDistrictAllocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductDistrictAllocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDistrictAllocation.class);
        ProductDistrictAllocation productDistrictAllocation1 = getProductDistrictAllocationSample1();
        ProductDistrictAllocation productDistrictAllocation2 = new ProductDistrictAllocation();
        assertThat(productDistrictAllocation1).isNotEqualTo(productDistrictAllocation2);

        productDistrictAllocation2.setId(productDistrictAllocation1.getId());
        assertThat(productDistrictAllocation1).isEqualTo(productDistrictAllocation2);

        productDistrictAllocation2 = getProductDistrictAllocationSample2();
        assertThat(productDistrictAllocation1).isNotEqualTo(productDistrictAllocation2);
    }
}
