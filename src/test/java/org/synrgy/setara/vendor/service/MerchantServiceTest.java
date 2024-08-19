package org.synrgy.setara.vendor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.synrgy.setara.vendor.dto.MerchantResponse;
import org.synrgy.setara.vendor.exception.VendorExceptions;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.repository.MerchantRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MerchantServiceTest {
    @InjectMocks
    private MerchantServiceImpl merchantService;

    @Mock
    private MerchantRepository merchantRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetQrisData_Success() {
        UUID qrisId = UUID.randomUUID();
        Merchant merchant = Merchant.builder()
                .name("Jane Smith")
                .nmid("ID5958987675019")
                .terminalId("JYW")
                .imagePath("/images/janesmith.png")
                .address("Fernvale Rd.")
                .qrisCode("qrisCode")
                .build();
        merchant.setId(qrisId);

        when(merchantRepository.findById(qrisId)).thenReturn(Optional.of(merchant));

        MerchantResponse expectedResponse = MerchantResponse.builder()
                .name("Jane Smith")
                .nmid("ID5958987675019")
                .terminalId("JYW")
                .imagePath("/images/janesmith.png")
                .address("Fernvale Rd.")
                .build();

        MerchantResponse response = merchantService.getQrisData(qrisId);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetQrisData_MerchantNotFound() {
        UUID qrisId = UUID.randomUUID();

        when(merchantRepository.findById(qrisId)).thenReturn(Optional.empty());

        assertThrows(VendorExceptions.MerchantNotFoundException.class, () -> merchantService.getQrisData(qrisId));
    }
}
