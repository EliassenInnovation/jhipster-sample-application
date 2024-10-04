package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductMstTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductMstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductMst.class);
        ProductMst productMst1 = getProductMstSample1();
        ProductMst productMst2 = new ProductMst();
        assertThat(productMst1).isNotEqualTo(productMst2);

        productMst2.setId(productMst1.getId());
        assertThat(productMst1).isEqualTo(productMst2);

        productMst2 = getProductMstSample2();
        assertThat(productMst1).isNotEqualTo(productMst2);
    }
}
