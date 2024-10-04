package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.DistributorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DistributorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Distributor.class);
        Distributor distributor1 = getDistributorSample1();
        Distributor distributor2 = new Distributor();
        assertThat(distributor1).isNotEqualTo(distributor2);

        distributor2.setId(distributor1.getId());
        assertThat(distributor1).isEqualTo(distributor2);

        distributor2 = getDistributorSample2();
        assertThat(distributor1).isNotEqualTo(distributor2);
    }
}
