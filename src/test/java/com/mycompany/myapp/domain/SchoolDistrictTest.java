package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SchoolDistrictTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchoolDistrictTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchoolDistrict.class);
        SchoolDistrict schoolDistrict1 = getSchoolDistrictSample1();
        SchoolDistrict schoolDistrict2 = new SchoolDistrict();
        assertThat(schoolDistrict1).isNotEqualTo(schoolDistrict2);

        schoolDistrict2.setId(schoolDistrict1.getId());
        assertThat(schoolDistrict1).isEqualTo(schoolDistrict2);

        schoolDistrict2 = getSchoolDistrictSample2();
        assertThat(schoolDistrict1).isNotEqualTo(schoolDistrict2);
    }
}
