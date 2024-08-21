package org.synrgy.setara.vendor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.synrgy.setara.vendor.dto.EwalletResponse;
import org.synrgy.setara.vendor.model.Ewallet;
import org.synrgy.setara.vendor.repository.EwalletRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EwalletServiceTest {

    @Mock
    private EwalletRepository ewalletRepo;

    @InjectMocks
    private EwalletServiceImpl ewalletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSeedEwallet_Success() {
        when(ewalletRepo.existsByName(anyString())).thenReturn(false); // Mock all e-wallets as not existing
        ewalletService.seedEwallet();

        verify(ewalletRepo, times(5)).save(any(Ewallet.class));
    }

    @Test
    void testSeedEwallet_AlreadyExists() {
        when(ewalletRepo.existsByName("Ovo")).thenReturn(true); // Mock "Ovo" as already existing
        when(ewalletRepo.existsByName("ShopeePay")).thenReturn(false);

        ewalletService.seedEwallet();

        verify(ewalletRepo, times(1)).save(argThat(ewallet -> ewallet.getName().equals("ShopeePay")));
        verify(ewalletRepo, never()).save(argThat(ewallet -> ewallet.getName().equals("Ovo")));
    }

    @Test
    void testGetAllEwallets_Success() {
        Ewallet ewallet1 = Ewallet.builder()
                .name("Ovo")
                .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722860518/SETARA_FC-8/NewOvo.png")
                .build();
        Ewallet ewallet2 = Ewallet.builder()
                .name("GoPay")
                .imagePath("https://res.cloudinary.com/dmuuypm2t/image/upload/v1722860520/SETARA_FC-8/Newgopay.png")
                .build();

        ewallet1.setId(UUID.randomUUID());
        ewallet2.setId(UUID.randomUUID());

        when(ewalletRepo.findAll()).thenReturn(Arrays.asList(ewallet1, ewallet2));

        List<EwalletResponse> responses = ewalletService.getAllEwallets();

        responses.forEach(response -> {
            System.out.println("EwalletResponse: " + response.getId() + ", " + response.getName() + ", " + response.getImagePath());
        });

        assertEquals(2, responses.size());
        assertEquals("Ovo", responses.get(0).getName());
        assertEquals("GoPay", responses.get(1).getName());
        assertNotNull(responses.get(0).getId());
        assertNotNull(responses.get(1).getId());
    }


}
