package org.synrgy.setara.vendor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.user.dto.UserBalanceResponse;
import org.synrgy.setara.vendor.dto.MerchantResponse;
import org.synrgy.setara.vendor.model.Merchant;
import org.synrgy.setara.vendor.service.MerchantService;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MerchantControllerTest {

    @Mock
    private MerchantService merchantService;

    @InjectMocks
    private MerchantController merchantController;

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

        MerchantResponse expectedResponse = MerchantResponse.builder()
                .name("Jane Smith")
                .nmid("ID5958987675019")
                .terminalId("JYW")
                .imagePath("/images/janesmith.png")
                .address("Fernvale Rd.")
                .qrisCode("qrisCode")
                .build();

        when(merchantService.getQrisData(qrisId)).thenReturn(expectedResponse);

        ResponseEntity<BaseResponse<MerchantResponse>> response = merchantController.getQrisData(qrisId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Get qris data successful", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(expectedResponse, response.getBody().getData());
    }
}
