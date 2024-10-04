package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MonthMstTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonthMstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonthMst.class);
        MonthMst monthMst1 = getMonthMstSample1();
        MonthMst monthMst2 = new MonthMst();
        assertThat(monthMst1).isNotEqualTo(monthMst2);

        monthMst2.setId(monthMst1.getId());
        assertThat(monthMst1).isEqualTo(monthMst2);

        monthMst2 = getMonthMstSample2();
        assertThat(monthMst1).isNotEqualTo(monthMst2);
    }
}
