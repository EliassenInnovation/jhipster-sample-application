package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductAllergenTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductAllergenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductAllergen.class);
        ProductAllergen productAllergen1 = getProductAllergenSample1();
        ProductAllergen productAllergen2 = new ProductAllergen();
        assertThat(productAllergen1).isNotEqualTo(productAllergen2);

        productAllergen2.setId(productAllergen1.getId());
        assertThat(productAllergen1).isEqualTo(productAllergen2);

        productAllergen2 = getProductAllergenSample2();
        assertThat(productAllergen1).isNotEqualTo(productAllergen2);
    }
}
