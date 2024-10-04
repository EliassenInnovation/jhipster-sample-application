package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.StateInfoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateInfo.class);
        StateInfo stateInfo1 = getStateInfoSample1();
        StateInfo stateInfo2 = new StateInfo();
        assertThat(stateInfo1).isNotEqualTo(stateInfo2);

        stateInfo2.setId(stateInfo1.getId());
        assertThat(stateInfo1).isEqualTo(stateInfo2);

        stateInfo2 = getStateInfoSample2();
        assertThat(stateInfo1).isNotEqualTo(stateInfo2);
    }
}
