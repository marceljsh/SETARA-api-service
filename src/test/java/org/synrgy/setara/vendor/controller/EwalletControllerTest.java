package org.synrgy.setara.vendor.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.synrgy.setara.common.dto.BaseResponse;
import org.synrgy.setara.vendor.dto.EwalletResponse;
import org.synrgy.setara.vendor.service.EwalletService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class EwalletControllerTest {

    @Mock
    private EwalletService ewalletService;

    @InjectMocks
    private EwalletController ewalletController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEwallets_Success() {
        EwalletResponse ewalletResponse1 = EwalletResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("Ovo")
                .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722860518/SETARA_FC-8/NewOvo.png")
                .build();

        EwalletResponse ewalletResponse2 = EwalletResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("DANA")
                .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722860518/SETARA_FC-8/NewDana.png")
                .build();

        List<EwalletResponse> ewalletResponseList = List.of(ewalletResponse1, ewalletResponse2);

        when(ewalletService.getAllEwallets()).thenReturn(ewalletResponseList);

        ResponseEntity<BaseResponse<List<EwalletResponse>>> response = ewalletController.getAllEwallets();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success Get All E-Wallet", Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(ewalletResponseList, response.getBody().getData());
    }
}
