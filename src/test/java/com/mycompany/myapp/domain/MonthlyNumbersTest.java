package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.MonthlyNumbersTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonthlyNumbersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MonthlyNumbers.class);
        MonthlyNumbers monthlyNumbers1 = getMonthlyNumbersSample1();
        MonthlyNumbers monthlyNumbers2 = new MonthlyNumbers();
        assertThat(monthlyNumbers1).isNotEqualTo(monthlyNumbers2);

        monthlyNumbers2.setId(monthlyNumbers1.getId());
        assertThat(monthlyNumbers1).isEqualTo(monthlyNumbers2);

        monthlyNumbers2 = getMonthlyNumbersSample2();
        assertThat(monthlyNumbers1).isNotEqualTo(monthlyNumbers2);
    }
}
