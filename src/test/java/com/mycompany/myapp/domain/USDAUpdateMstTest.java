package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.USDAUpdateMstTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class USDAUpdateMstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(USDAUpdateMst.class);
        USDAUpdateMst uSDAUpdateMst1 = getUSDAUpdateMstSample1();
        USDAUpdateMst uSDAUpdateMst2 = new USDAUpdateMst();
        assertThat(uSDAUpdateMst1).isNotEqualTo(uSDAUpdateMst2);

        uSDAUpdateMst2.setId(uSDAUpdateMst1.getId());
        assertThat(uSDAUpdateMst1).isEqualTo(uSDAUpdateMst2);

        uSDAUpdateMst2 = getUSDAUpdateMstSample2();
        assertThat(uSDAUpdateMst1).isNotEqualTo(uSDAUpdateMst2);
    }
}
