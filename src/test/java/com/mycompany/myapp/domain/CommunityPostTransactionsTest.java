package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CommunityPostTransactionsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunityPostTransactionsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunityPostTransactions.class);
        CommunityPostTransactions communityPostTransactions1 = getCommunityPostTransactionsSample1();
        CommunityPostTransactions communityPostTransactions2 = new CommunityPostTransactions();
        assertThat(communityPostTransactions1).isNotEqualTo(communityPostTransactions2);

        communityPostTransactions2.setId(communityPostTransactions1.getId());
        assertThat(communityPostTransactions1).isEqualTo(communityPostTransactions2);

        communityPostTransactions2 = getCommunityPostTransactionsSample2();
        assertThat(communityPostTransactions1).isNotEqualTo(communityPostTransactions2);
    }
}
