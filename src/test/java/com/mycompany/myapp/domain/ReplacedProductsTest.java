package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ReplacedProductsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReplacedProductsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReplacedProducts.class);
        ReplacedProducts replacedProducts1 = getReplacedProductsSample1();
        ReplacedProducts replacedProducts2 = new ReplacedProducts();
        assertThat(replacedProducts1).isNotEqualTo(replacedProducts2);

        replacedProducts2.setId(replacedProducts1.getId());
        assertThat(replacedProducts1).isEqualTo(replacedProducts2);

        replacedProducts2 = getReplacedProductsSample2();
        assertThat(replacedProducts1).isNotEqualTo(replacedProducts2);
    }
}
