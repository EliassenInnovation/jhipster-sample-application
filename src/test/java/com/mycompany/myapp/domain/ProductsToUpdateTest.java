package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProductsToUpdateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductsToUpdateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductsToUpdate.class);
        ProductsToUpdate productsToUpdate1 = getProductsToUpdateSample1();
        ProductsToUpdate productsToUpdate2 = new ProductsToUpdate();
        assertThat(productsToUpdate1).isNotEqualTo(productsToUpdate2);

        productsToUpdate2.setId(productsToUpdate1.getId());
        assertThat(productsToUpdate1).isEqualTo(productsToUpdate2);

        productsToUpdate2 = getProductsToUpdateSample2();
        assertThat(productsToUpdate1).isNotEqualTo(productsToUpdate2);
    }
}
