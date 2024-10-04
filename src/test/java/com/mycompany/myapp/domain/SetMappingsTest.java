package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SetMappingsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SetMappingsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SetMappings.class);
        SetMappings setMappings1 = getSetMappingsSample1();
        SetMappings setMappings2 = new SetMappings();
        assertThat(setMappings1).isNotEqualTo(setMappings2);

        setMappings2.setId(setMappings1.getId());
        assertThat(setMappings1).isEqualTo(setMappings2);

        setMappings2 = getSetMappingsSample2();
        assertThat(setMappings1).isNotEqualTo(setMappings2);
    }
}
