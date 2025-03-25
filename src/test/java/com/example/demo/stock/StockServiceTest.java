package com.example.demo.stock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {
    @Mock
    StockRepository stockRepository;
    @InjectMocks
    StockService stockService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAllStocks() {
        List<Stock> stocks = List.of(
                new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null),
                new Stock(2L,56,StockType.STOCK_IN,null,"this is ref2",null,null,null)
        );
        when(stockRepository.findAll()).thenReturn(stocks);
        assertEquals(2,stockService.findAllStocks().size());

    }
   @Test
    void findStockById() {
      Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);
       when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
       assertEquals(stock.getId(),stockService.findStockById(1L).get().getId());
    }

    @Test
    void findStockByInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.findStockById(0);
        });

    }

    @Test
    void saveValidStock() {
        Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);
        when(stockRepository.save(stock)).thenReturn(stock);
        assertEquals(stock.getId(),stockService.save(stock).getId());
        verify(stockRepository,times(1)).save(stock);

    }
    @Test
    void saveNullStockValueThenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.save(null);
        });
        verify(stockRepository, never()).save(any(Stock.class));
    }
    @Test
    void saveExistingStockValueThenThrowException() {
        Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
       IllegalArgumentException illegalArgumentException= assertThrows(IllegalArgumentException.class, () -> {
            stockService.save(stock);
        });
       assertTrue(illegalArgumentException.getMessage().contains("Stock already exists"));
        verify(stockRepository,never()).save(stock);

    }

    @Test
    void deleteStockByValidId() {
        Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        doNothing().when(stockRepository).deleteById(1L);
        stockService.delete(1L);
        verify(stockRepository,times(1)).deleteById(1L);
    }

    @Test
    void deleteStockByInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.delete(0);
        });
    }

    @Test
    void tryToDeleteNonExistentStockThenThrowException() {
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.delete(1L);
        });
    verify(stockRepository,never()).deleteById(1L);
    }

    @Test
    void updateExistingStock() {
        Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(stock)).thenReturn(stock);
        assertEquals(stock.getId(),stockService.update(1L,stock).getId());
    }
    @Test
    void updateNullStockValueThenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.update(1L,null);
        });
        verify(stockRepository,never()).save(any(Stock.class));
    }
    @Test
    void updateNonExistingStockValueThenThrowException() {
        Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            stockService.update(1L,stock);
        });
        verify(stockRepository,never()).save(any(Stock.class));
    }

    @Test
    void updateExistingStockValueByInvalidIdThenThrowException() {
        Stock stock= new Stock(1L,23,StockType.STOCK_IN,null,"this is ref",null,null,null);

        assertThrows(IllegalArgumentException.class, () -> {
            stockService.update(0L,stock);
        });
        verify(stockRepository,never()).save(any(Stock.class));
    }


}