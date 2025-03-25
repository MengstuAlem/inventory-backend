package com.example.demo.saleitem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(SaleItemController.class)
class SaleItemControllerTest {
    @MockBean
    private SaleItemService saleItemService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllSaleItems() throws Exception {
        List<SaleItem> saleItems = List.of(
                new SaleItem(1L,12, BigDecimal.valueOf(123),null,null),
                new SaleItem(2L,36,BigDecimal.valueOf(20039),null,null)
        );
        when(saleItemService.findAll()).thenReturn(saleItems);
        mockMvc.perform(MockMvcRequestBuilders.get("/saleItems")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleItems)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getSaleItemByValidId() throws Exception {
        SaleItem saleItem = new SaleItem(1L,12, BigDecimal.valueOf(123),null,null);
        when(saleItemService.findById(1L)).thenReturn(Optional.of(saleItem));

        mockMvc.perform(MockMvcRequestBuilders.get("/saleItems/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleItem)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void getSaleItemByInvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/saleItems/0"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void saveValidSaleItem() throws Exception {
        SaleItem saleItem = new SaleItem(1L,12, BigDecimal.valueOf(123),null,null);
        when(saleItemService.save(saleItem)).thenReturn(saleItem);
        mockMvc.perform(MockMvcRequestBuilders.post("/saleItems")
                .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(saleItem)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void saveInvalidSaleItem() throws Exception {
        when(saleItemService.findById(0L)).thenThrow(new IllegalArgumentException("saleItem not found"));
        mockMvc.perform(MockMvcRequestBuilders.post("/saleItems")
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("{}")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void deleteExistSaleItemByValidId() throws Exception {
        doNothing().when(saleItemService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/saleItems/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    void deleteExistSaleItemByInvalidId() throws Exception {
        doThrow(new IllegalArgumentException("saleItem not found")).when(saleItemService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/saleItems/0"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateValidSaleItem() throws Exception {
        SaleItem saleItem = new SaleItem(1L,12, BigDecimal.valueOf(123),null,null);
        when(saleItemService.findById(1L)).thenReturn(Optional.of(saleItem));
        when(saleItemService.save(saleItem)).thenReturn(saleItem);
        mockMvc.perform(MockMvcRequestBuilders.put("/saleItems/1")
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleItem)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateInvalidSaleItem() throws Exception {
        doThrow(IllegalArgumentException.class).when(saleItemService).update(1L,null);
        mockMvc.perform(MockMvcRequestBuilders.put("/saleItems/0")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("{}")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}