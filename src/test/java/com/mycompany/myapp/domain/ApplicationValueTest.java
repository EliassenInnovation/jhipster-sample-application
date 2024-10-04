package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ApplicationValueTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationValueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationValue.class);
        ApplicationValue applicationValue1 = getApplicationValueSample1();
        ApplicationValue applicationValue2 = new ApplicationValue();
        assertThat(applicationValue1).isNotEqualTo(applicationValue2);

        applicationValue2.setId(applicationValue1.getId());
        assertThat(applicationValue1).isEqualTo(applicationValue2);

        applicationValue2 = getApplicationValueSample2();
        assertThat(applicationValue1).isNotEqualTo(applicationValue2);
    }
}
