package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductBeforeApproveTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductBeforeApproveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductBeforeApprove.class);
        ProductBeforeApprove productBeforeApprove1 = getProductBeforeApproveSample1();
        ProductBeforeApprove productBeforeApprove2 = new ProductBeforeApprove();
        assertThat(productBeforeApprove1).isNotEqualTo(productBeforeApprove2);

        productBeforeApprove2.setId(productBeforeApprove1.getId());
        assertThat(productBeforeApprove1).isEqualTo(productBeforeApprove2);

        productBeforeApprove2 = getProductBeforeApproveSample2();
        assertThat(productBeforeApprove1).isNotEqualTo(productBeforeApprove2);
    }
}
