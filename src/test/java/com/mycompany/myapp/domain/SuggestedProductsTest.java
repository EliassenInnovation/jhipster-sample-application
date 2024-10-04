package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SuggestedProductsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SuggestedProductsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuggestedProducts.class);
        SuggestedProducts suggestedProducts1 = getSuggestedProductsSample1();
        SuggestedProducts suggestedProducts2 = new SuggestedProducts();
        assertThat(suggestedProducts1).isNotEqualTo(suggestedProducts2);

        suggestedProducts2.setId(suggestedProducts1.getId());
        assertThat(suggestedProducts1).isEqualTo(suggestedProducts2);

        suggestedProducts2 = getSuggestedProductsSample2();
        assertThat(suggestedProducts1).isNotEqualTo(suggestedProducts2);
    }
}
