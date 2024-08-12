package org.synrgy.setara.vendor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class MerchantServiceTest {
    @InjectMocks
    private MerchantServiceImpl merchantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetQrisData_Success() {

    }

    @Test
    void testGetQrisData_MerchantNotFound() {

    }
}
