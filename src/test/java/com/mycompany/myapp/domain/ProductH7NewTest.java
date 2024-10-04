package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductH7NewTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductH7NewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductH7New.class);
        ProductH7New productH7New1 = getProductH7NewSample1();
        ProductH7New productH7New2 = new ProductH7New();
        assertThat(productH7New1).isNotEqualTo(productH7New2);

        productH7New2.setId(productH7New1.getId());
        assertThat(productH7New1).isEqualTo(productH7New2);

        productH7New2 = getProductH7NewSample2();
        assertThat(productH7New1).isNotEqualTo(productH7New2);
    }
}
