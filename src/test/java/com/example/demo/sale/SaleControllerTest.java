package com.example.demo.sale;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.demo.sale.SaleStatus.PENDING;
import static org.mockito.Mockito.*;

@WebMvcTest(SaleController.class)
class SaleControllerTest {
    @MockBean
    private SaleService saleService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllSales() throws Exception {
        List<Sale> sales = List.of(
                new Sale(1L,"mengstu",null, BigDecimal.valueOf(12),PENDING,null,null),
                new Sale(2L,"alem",null, BigDecimal.valueOf(124),PENDING,null,null)
        );
        when(saleService.findAll()).thenReturn(sales);
        mockMvc.perform(MockMvcRequestBuilders.get("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sales)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].customerName").value("mengstu"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].customerName").value("alem"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void saveValidSaleTest() throws Exception {
        Sale sale =new Sale(1L,"mengstu",null, BigDecimal.valueOf(12),PENDING,null,null);
        when(saleService.save(sale)).thenReturn(sale);
        mockMvc.perform(MockMvcRequestBuilders.post("/sales")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(sale)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("mengstu"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void tryToSaveNullValueSaleTest() throws Exception {
       mockMvc.perform(MockMvcRequestBuilders.post("/sales")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(null)))
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void getSaleByValidIdTest() throws Exception {
        Sale sale =new Sale(1L,"mengstu",null, BigDecimal.valueOf(12),PENDING,null,null);
        when(saleService.findById(1L)).thenReturn(Optional.of(sale));
        mockMvc.perform(MockMvcRequestBuilders.get("/sales/1")
                .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(sale)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("mengstu"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void getSaleByNonExistedIdTest() throws Exception {
        when(saleService.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/sales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
     void getSaleByInvalidIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sales/-1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void deleteSaleByValidIdTest() throws Exception {
        doNothing().when(saleService).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/sales/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    void deleteSaleByNonExistedIdTest() throws Exception {
        when(saleService.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.delete("/sales/l")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    @Test
    void deleteSaleByInvalidIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/sales/-1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateSaleByValidIdTest() throws Exception {
        Sale sale =new Sale(1L,"mengstu",null, BigDecimal.valueOf(12),PENDING,null,null);
        when(saleService.update(1L,sale)).thenReturn(sale);
        mockMvc.perform(MockMvcRequestBuilders.put("/sales/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(sale)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("mengstu"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    @Test
    void updateSaleByNonExistedIdTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(saleService).update(1L,null);
        mockMvc.perform(MockMvcRequestBuilders.put("/sales/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(null)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}