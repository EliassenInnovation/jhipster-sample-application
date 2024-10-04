package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductH7OldTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductH7OldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductH7Old.class);
        ProductH7Old productH7Old1 = getProductH7OldSample1();
        ProductH7Old productH7Old2 = new ProductH7Old();
        assertThat(productH7Old1).isNotEqualTo(productH7Old2);

        productH7Old2.setId(productH7Old1.getId());
        assertThat(productH7Old1).isEqualTo(productH7Old2);

        productH7Old2 = getProductH7OldSample2();
        assertThat(productH7Old1).isNotEqualTo(productH7Old2);
    }
}
