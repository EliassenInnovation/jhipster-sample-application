package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.UserDistrictAllocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserDistrictAllocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDistrictAllocation.class);
        UserDistrictAllocation userDistrictAllocation1 = getUserDistrictAllocationSample1();
        UserDistrictAllocation userDistrictAllocation2 = new UserDistrictAllocation();
        assertThat(userDistrictAllocation1).isNotEqualTo(userDistrictAllocation2);

        userDistrictAllocation2.setId(userDistrictAllocation1.getId());
        assertThat(userDistrictAllocation1).isEqualTo(userDistrictAllocation2);

        userDistrictAllocation2 = getUserDistrictAllocationSample2();
        assertThat(userDistrictAllocation1).isNotEqualTo(userDistrictAllocation2);
    }
}
