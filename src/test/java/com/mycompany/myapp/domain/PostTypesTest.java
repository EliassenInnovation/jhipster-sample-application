package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PostTypesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PostTypesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostTypes.class);
        PostTypes postTypes1 = getPostTypesSample1();
        PostTypes postTypes2 = new PostTypes();
        assertThat(postTypes1).isNotEqualTo(postTypes2);

        postTypes2.setId(postTypes1.getId());
        assertThat(postTypes1).isEqualTo(postTypes2);

        postTypes2 = getPostTypesSample2();
        assertThat(postTypes1).isNotEqualTo(postTypes2);
    }
}
