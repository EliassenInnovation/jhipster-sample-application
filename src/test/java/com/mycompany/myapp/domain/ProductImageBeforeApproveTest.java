package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductImageBeforeApproveTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductImageBeforeApproveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductImageBeforeApprove.class);
        ProductImageBeforeApprove productImageBeforeApprove1 = getProductImageBeforeApproveSample1();
        ProductImageBeforeApprove productImageBeforeApprove2 = new ProductImageBeforeApprove();
        assertThat(productImageBeforeApprove1).isNotEqualTo(productImageBeforeApprove2);

        productImageBeforeApprove2.setId(productImageBeforeApprove1.getId());
        assertThat(productImageBeforeApprove1).isEqualTo(productImageBeforeApprove2);

        productImageBeforeApprove2 = getProductImageBeforeApproveSample2();
        assertThat(productImageBeforeApprove1).isNotEqualTo(productImageBeforeApprove2);
    }
}
