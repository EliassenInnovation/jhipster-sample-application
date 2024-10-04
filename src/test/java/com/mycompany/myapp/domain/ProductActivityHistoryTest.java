package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductActivityHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductActivityHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductActivityHistory.class);
        ProductActivityHistory productActivityHistory1 = getProductActivityHistorySample1();
        ProductActivityHistory productActivityHistory2 = new ProductActivityHistory();
        assertThat(productActivityHistory1).isNotEqualTo(productActivityHistory2);

        productActivityHistory2.setId(productActivityHistory1.getId());
        assertThat(productActivityHistory1).isEqualTo(productActivityHistory2);

        productActivityHistory2 = getProductActivityHistorySample2();
        assertThat(productActivityHistory1).isNotEqualTo(productActivityHistory2);
    }
}
