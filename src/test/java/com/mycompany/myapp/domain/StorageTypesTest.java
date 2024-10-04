package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.StorageTypesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StorageTypesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StorageTypes.class);
        StorageTypes storageTypes1 = getStorageTypesSample1();
        StorageTypes storageTypes2 = new StorageTypes();
        assertThat(storageTypes1).isNotEqualTo(storageTypes2);

        storageTypes2.setId(storageTypes1.getId());
        assertThat(storageTypes1).isEqualTo(storageTypes2);

        storageTypes2 = getStorageTypesSample2();
        assertThat(storageTypes1).isNotEqualTo(storageTypes2);
    }
}
