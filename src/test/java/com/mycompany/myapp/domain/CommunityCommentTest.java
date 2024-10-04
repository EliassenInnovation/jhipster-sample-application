package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CommunityCommentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunityCommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunityComment.class);
        CommunityComment communityComment1 = getCommunityCommentSample1();
        CommunityComment communityComment2 = new CommunityComment();
        assertThat(communityComment1).isNotEqualTo(communityComment2);

        communityComment2.setId(communityComment1.getId());
        assertThat(communityComment1).isEqualTo(communityComment2);

        communityComment2 = getCommunityCommentSample2();
        assertThat(communityComment1).isNotEqualTo(communityComment2);
    }
}
