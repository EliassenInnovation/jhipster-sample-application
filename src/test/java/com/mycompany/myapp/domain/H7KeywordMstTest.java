package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.H7KeywordMstTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class H7KeywordMstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(H7KeywordMst.class);
        H7KeywordMst h7KeywordMst1 = getH7KeywordMstSample1();
        H7KeywordMst h7KeywordMst2 = new H7KeywordMst();
        assertThat(h7KeywordMst1).isNotEqualTo(h7KeywordMst2);

        h7KeywordMst2.setId(h7KeywordMst1.getId());
        assertThat(h7KeywordMst1).isEqualTo(h7KeywordMst2);

        h7KeywordMst2 = getH7KeywordMstSample2();
        assertThat(h7KeywordMst1).isNotEqualTo(h7KeywordMst2);
    }
}
