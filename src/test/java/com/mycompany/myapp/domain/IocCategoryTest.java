package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.IocCategoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IocCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IocCategory.class);
        IocCategory iocCategory1 = getIocCategorySample1();
        IocCategory iocCategory2 = new IocCategory();
        assertThat(iocCategory1).isNotEqualTo(iocCategory2);

        iocCategory2.setId(iocCategory1.getId());
        assertThat(iocCategory1).isEqualTo(iocCategory2);

        iocCategory2 = getIocCategorySample2();
        assertThat(iocCategory1).isNotEqualTo(iocCategory2);
    }
}
