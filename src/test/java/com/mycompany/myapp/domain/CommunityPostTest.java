package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CommunityPostTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunityPostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunityPost.class);
        CommunityPost communityPost1 = getCommunityPostSample1();
        CommunityPost communityPost2 = new CommunityPost();
        assertThat(communityPost1).isNotEqualTo(communityPost2);

        communityPost2.setId(communityPost1.getId());
        assertThat(communityPost1).isEqualTo(communityPost2);

        communityPost2 = getCommunityPostSample2();
        assertThat(communityPost1).isNotEqualTo(communityPost2);
    }
}
