package com.example.demo.purchase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(PurchaseOrderController.class)
class PurchaseOrderControllerTest {
    @MockitoBean
   private PurchaseOrderService purchaseOrderService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPurchaseOrders() throws Exception {
        List<PurchaseOrder>  purchaseOrders=List.of(
                new PurchaseOrder(1L, Instant.now(),
                        BigDecimal.valueOf(12),PurchaseOrderStutas.PENDING,Instant.now(),
                        null,null),
                new PurchaseOrder(2L, Instant.now(),
                        BigDecimal.valueOf(122),PurchaseOrderStutas.PENDING,Instant.now(),
                        null,null)
        );
        when(purchaseOrderService.findAll()).thenReturn(purchaseOrders);
        mockMvc.perform(MockMvcRequestBuilders.get("/purchaseOrders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseOrders)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getExistedPurchaseOrderById() throws Exception {
        PurchaseOrder purchaseOrder=   new PurchaseOrder(1L, Instant.now(),
                BigDecimal.valueOf(12),PurchaseOrderStutas.PENDING,Instant.now(),
                null,null);
        when(purchaseOrderService.findById(1l)).thenReturn(Optional.of(purchaseOrder));
        mockMvc.perform(MockMvcRequestBuilders.get("/purchaseOrders/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(purchaseOrder)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getNonExistedPurchaseOrderById() throws Exception {
        when(purchaseOrderService.findById(1l)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/purchaseOrders/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString("{}")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void getPurchaseOrderByInValidId() throws Exception {
       doThrow(IllegalArgumentException.class).when(purchaseOrderService).findById(0L);
        mockMvc.perform(MockMvcRequestBuilders.get("/purchaseOrders/0")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString("{}")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void createValidPurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder=   new PurchaseOrder(1L, Instant.now(),
                BigDecimal.valueOf(12),PurchaseOrderStutas.PENDING,Instant.now(),
                null,null);
        when(purchaseOrderService.save(purchaseOrder)).thenReturn(purchaseOrder);
        mockMvc.perform(MockMvcRequestBuilders.post("/purchaseOrders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(purchaseOrder)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void tryCreateNullPurchaseOrder() throws Exception {
        doThrow(IllegalArgumentException.class).when(purchaseOrderService).save(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/purchaseOrders")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString("{}")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deletePurchaseOrderByValidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/purchaseOrders/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    void deletePurchaseOrderByInvalidId() throws Exception {
        doThrow(IllegalArgumentException.class).when(purchaseOrderService).deleteById(0L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/purchaseOrders/0"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateValidPurchaseOrder() throws Exception {
        PurchaseOrder purchaseOrder=   new PurchaseOrder(1L, Instant.now(),
                BigDecimal.valueOf(12),PurchaseOrderStutas.PENDING,Instant.now(),
                null,null);
        when(purchaseOrderService.update(1L,purchaseOrder)).thenReturn(purchaseOrder);
        mockMvc.perform(MockMvcRequestBuilders.put("/purchaseOrders/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(purchaseOrder)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void updateNullValuePurchaseOrder() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/purchaseOrders/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString("{}")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void updatePurchaseOrderInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/purchaseOrders/0"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}