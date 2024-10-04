package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.BlockReportPostTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BlockReportPostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlockReportPost.class);
        BlockReportPost blockReportPost1 = getBlockReportPostSample1();
        BlockReportPost blockReportPost2 = new BlockReportPost();
        assertThat(blockReportPost1).isNotEqualTo(blockReportPost2);

        blockReportPost2.setId(blockReportPost1.getId());
        assertThat(blockReportPost1).isEqualTo(blockReportPost2);

        blockReportPost2 = getBlockReportPostSample2();
        assertThat(blockReportPost1).isNotEqualTo(blockReportPost2);
    }
}
