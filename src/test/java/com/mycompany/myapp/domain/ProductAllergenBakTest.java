package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductAllergenBakTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductAllergenBakTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductAllergenBak.class);
        ProductAllergenBak productAllergenBak1 = getProductAllergenBakSample1();
        ProductAllergenBak productAllergenBak2 = new ProductAllergenBak();
        assertThat(productAllergenBak1).isNotEqualTo(productAllergenBak2);

        productAllergenBak2.setId(productAllergenBak1.getId());
        assertThat(productAllergenBak1).isEqualTo(productAllergenBak2);

        productAllergenBak2 = getProductAllergenBakSample2();
        assertThat(productAllergenBak1).isNotEqualTo(productAllergenBak2);
    }
}
