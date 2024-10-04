package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductH7TestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductH7Test {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductH7.class);
        ProductH7 productH71 = getProductH7Sample1();
        ProductH7 productH72 = new ProductH7();
        assertThat(productH71).isNotEqualTo(productH72);

        productH72.setId(productH71.getId());
        assertThat(productH71).isEqualTo(productH72);

        productH72 = getProductH7Sample2();
        assertThat(productH71).isNotEqualTo(productH72);
    }
}
