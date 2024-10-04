package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.LoginHistoryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoginHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoginHistory.class);
        LoginHistory loginHistory1 = getLoginHistorySample1();
        LoginHistory loginHistory2 = new LoginHistory();
        assertThat(loginHistory1).isNotEqualTo(loginHistory2);

        loginHistory2.setId(loginHistory1.getId());
        assertThat(loginHistory1).isEqualTo(loginHistory2);

        loginHistory2 = getLoginHistorySample2();
        assertThat(loginHistory1).isNotEqualTo(loginHistory2);
    }
}
