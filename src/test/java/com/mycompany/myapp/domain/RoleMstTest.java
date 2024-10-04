package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.RoleMstTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleMstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleMst.class);
        RoleMst roleMst1 = getRoleMstSample1();
        RoleMst roleMst2 = new RoleMst();
        assertThat(roleMst1).isNotEqualTo(roleMst2);

        roleMst2.setId(roleMst1.getId());
        assertThat(roleMst1).isEqualTo(roleMst2);

        roleMst2 = getRoleMstSample2();
        assertThat(roleMst1).isNotEqualTo(roleMst2);
    }
}
