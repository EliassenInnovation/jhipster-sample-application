package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.CommunityLikeMstTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommunityLikeMstTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommunityLikeMst.class);
        CommunityLikeMst communityLikeMst1 = getCommunityLikeMstSample1();
        CommunityLikeMst communityLikeMst2 = new CommunityLikeMst();
        assertThat(communityLikeMst1).isNotEqualTo(communityLikeMst2);

        communityLikeMst2.setId(communityLikeMst1.getId());
        assertThat(communityLikeMst1).isEqualTo(communityLikeMst2);

        communityLikeMst2 = getCommunityLikeMstSample2();
        assertThat(communityLikeMst1).isNotEqualTo(communityLikeMst2);
    }
}
