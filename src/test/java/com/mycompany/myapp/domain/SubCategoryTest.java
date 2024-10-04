package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SubCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCategory.class);
        SubCategory subCategory1 = getSubCategorySample1();
        SubCategory subCategory2 = new SubCategory();
        assertThat(subCategory1).isNotEqualTo(subCategory2);

        subCategory2.setId(subCategory1.getId());
        assertThat(subCategory1).isEqualTo(subCategory2);

        subCategory2 = getSubCategorySample2();
        assertThat(subCategory1).isNotEqualTo(subCategory2);
    }
}
