//package org.synrgy.setara.user.controller;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.synrgy.setara.security.service.JwtService;
//import org.synrgy.setara.user.dto.UserBalanceResponse;
//import org.synrgy.setara.user.service.UserService;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private JwtService jwtService;
//
//    @Test
//    void testGetBalance() throws Exception {
//        LocalDateTime fixedDateTime = LocalDateTime.of(2024, 8, 1, 10, 0);
//        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//
//        UserBalanceResponse userBalanceResponse = UserBalanceResponse.builder()
//                .checkTime(fixedDateTime)
//                .balance(BigDecimal.valueOf(1000))
//                .build();
//
//        when(userService.getBalance()).thenReturn(userBalanceResponse);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/getBalance")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("SUCCESS"))
//                .andExpect(jsonPath("$.message").value("Success Get Balance"))
//                .andExpect(jsonPath("$.data.balance").value(1000))
//                .andExpect(jsonPath("$.data.checkTime").value(fixedDateTime.format(formatter)));
//    }
//}
