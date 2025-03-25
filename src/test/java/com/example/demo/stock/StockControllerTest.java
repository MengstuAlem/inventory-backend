package com.example.demo.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(StockController.class)
class StockControllerTest {
    @MockBean
    private StockService stockService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllStocks() throws Exception {
        List<Stock> stocks = List.of(
                 new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null),
                new Stock(2L,232,StockType.STOCK_OUT,null,"this is ref2",null,null,null)
        );
        when(stockService.findAllStocks()).thenReturn(stocks);
        mockMvc.perform(MockMvcRequestBuilders.get("/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stocks)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findStockByValidId() throws Exception {
        Stock stock=new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);

        when(stockService.findStockById(1l)).thenReturn(Optional.of(stock));
        mockMvc.perform(MockMvcRequestBuilders.get("/stocks/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void findNonExistingStockById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/stocks/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString("{}")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void saveValidStock() throws Exception {
       Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);
       when(stockService.save(stock)).thenReturn(stock);
        mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stock)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    void TryToSaveExistingStockThanThrowException() throws Exception {
       Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);
        when(stockService.findStockById(1L)).thenReturn(Optional.of(stock));
        mockMvc.perform(MockMvcRequestBuilders.post("/stocks")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteStockByValidId() throws Exception {
        Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);
        when(stockService.findStockById(1l)).thenReturn(Optional.of(stock));
        doNothing().when(stockService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/stocks/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString("{}")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    void deleteNonExistingStockThanThrowException() throws Exception {
        when(stockService.findStockById(1L)).thenReturn(Optional.empty());
        doThrow(IllegalArgumentException.class).when(stockService).delete(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/stocks/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString("{}")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


}