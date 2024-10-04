package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.UserMstTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMst.class);
        UserMst userMst1 = getUserMstSample1();
        UserMst userMst2 = new UserMst();
        assertThat(userMst1).isNotEqualTo(userMst2);

        userMst2.setId(userMst1.getId());
        assertThat(userMst1).isEqualTo(userMst2);

        userMst2 = getUserMstSample2();
        assertThat(userMst1).isNotEqualTo(userMst2);
    }
}
