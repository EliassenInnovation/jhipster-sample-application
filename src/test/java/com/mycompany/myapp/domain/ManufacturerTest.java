package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ManufacturerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ManufacturerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Manufacturer.class);
        Manufacturer manufacturer1 = getManufacturerSample1();
        Manufacturer manufacturer2 = new Manufacturer();
        assertThat(manufacturer1).isNotEqualTo(manufacturer2);

        manufacturer2.setId(manufacturer1.getId());
        assertThat(manufacturer1).isEqualTo(manufacturer2);

        manufacturer2 = getManufacturerSample2();
        assertThat(manufacturer1).isNotEqualTo(manufacturer2);
    }
}
