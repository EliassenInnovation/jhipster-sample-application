package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductChangeHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductChangeHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductChangeHistory.class);
        ProductChangeHistory productChangeHistory1 = getProductChangeHistorySample1();
        ProductChangeHistory productChangeHistory2 = new ProductChangeHistory();
        assertThat(productChangeHistory1).isNotEqualTo(productChangeHistory2);

        productChangeHistory2.setId(productChangeHistory1.getId());
        assertThat(productChangeHistory1).isEqualTo(productChangeHistory2);

        productChangeHistory2 = getProductChangeHistorySample2();
        assertThat(productChangeHistory1).isNotEqualTo(productChangeHistory2);
    }
}
