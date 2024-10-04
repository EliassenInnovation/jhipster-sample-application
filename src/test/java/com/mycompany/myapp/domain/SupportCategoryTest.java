package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SupportCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupportCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupportCategory.class);
        SupportCategory supportCategory1 = getSupportCategorySample1();
        SupportCategory supportCategory2 = new SupportCategory();
        assertThat(supportCategory1).isNotEqualTo(supportCategory2);

        supportCategory2.setId(supportCategory1.getId());
        assertThat(supportCategory1).isEqualTo(supportCategory2);

        supportCategory2 = getSupportCategorySample2();
        assertThat(supportCategory1).isNotEqualTo(supportCategory2);
    }
}
