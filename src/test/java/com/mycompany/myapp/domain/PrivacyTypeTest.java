package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PrivacyTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrivacyTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrivacyType.class);
        PrivacyType privacyType1 = getPrivacyTypeSample1();
        PrivacyType privacyType2 = new PrivacyType();
        assertThat(privacyType1).isNotEqualTo(privacyType2);

        privacyType2.setId(privacyType1.getId());
        assertThat(privacyType1).isEqualTo(privacyType2);

        privacyType2 = getPrivacyTypeSample2();
        assertThat(privacyType1).isNotEqualTo(privacyType2);
    }
}
