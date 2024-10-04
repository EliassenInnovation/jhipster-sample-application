package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SupportTicketTransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SupportTicketTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SupportTicketTransaction.class);
        SupportTicketTransaction supportTicketTransaction1 = getSupportTicketTransactionSample1();
        SupportTicketTransaction supportTicketTransaction2 = new SupportTicketTransaction();
        assertThat(supportTicketTransaction1).isNotEqualTo(supportTicketTransaction2);

        supportTicketTransaction2.setId(supportTicketTransaction1.getId());
        assertThat(supportTicketTransaction1).isEqualTo(supportTicketTransaction2);

        supportTicketTransaction2 = getSupportTicketTransactionSample2();
        assertThat(supportTicketTransaction1).isNotEqualTo(supportTicketTransaction2);
    }
}
