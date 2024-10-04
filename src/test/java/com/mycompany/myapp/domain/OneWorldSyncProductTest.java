package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.OneWorldSyncProductTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OneWorldSyncProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OneWorldSyncProduct.class);
        OneWorldSyncProduct oneWorldSyncProduct1 = getOneWorldSyncProductSample1();
        OneWorldSyncProduct oneWorldSyncProduct2 = new OneWorldSyncProduct();
        assertThat(oneWorldSyncProduct1).isNotEqualTo(oneWorldSyncProduct2);

        oneWorldSyncProduct2.setId(oneWorldSyncProduct1.getId());
        assertThat(oneWorldSyncProduct1).isEqualTo(oneWorldSyncProduct2);

        oneWorldSyncProduct2 = getOneWorldSyncProductSample2();
        assertThat(oneWorldSyncProduct1).isNotEqualTo(oneWorldSyncProduct2);
    }
}
