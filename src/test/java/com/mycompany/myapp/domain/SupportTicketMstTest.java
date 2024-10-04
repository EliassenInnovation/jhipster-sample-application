package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SupportTicketMstTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupportTicketMstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupportTicketMst.class);
        SupportTicketMst supportTicketMst1 = getSupportTicketMstSample1();
        SupportTicketMst supportTicketMst2 = new SupportTicketMst();
        assertThat(supportTicketMst1).isNotEqualTo(supportTicketMst2);

        supportTicketMst2.setId(supportTicketMst1.getId());
        assertThat(supportTicketMst1).isEqualTo(supportTicketMst2);

        supportTicketMst2 = getSupportTicketMstSample2();
        assertThat(supportTicketMst1).isNotEqualTo(supportTicketMst2);
    }
}
