package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AllergenMstTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllergenMstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllergenMst.class);
        AllergenMst allergenMst1 = getAllergenMstSample1();
        AllergenMst allergenMst2 = new AllergenMst();
        assertThat(allergenMst1).isNotEqualTo(allergenMst2);

        allergenMst2.setId(allergenMst1.getId());
        assertThat(allergenMst1).isEqualTo(allergenMst2);

        allergenMst2 = getAllergenMstSample2();
        assertThat(allergenMst1).isNotEqualTo(allergenMst2);
    }
}
