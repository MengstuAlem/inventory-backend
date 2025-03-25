package com.example.demo.sale;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.demo.sale.SaleStatus.PENDING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleServiceTest {
    @Mock
    SaleRepository saleRepository;
    @InjectMocks
    SaleService saleService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllExistSale() {
        List<Sale> sales = List.of(
                new Sale(1L,"mengstu",null, BigDecimal.valueOf(12),PENDING,null,null),
                new Sale(2L,"alem",null, BigDecimal.valueOf(124),PENDING,null,null)
        );
        when(saleRepository.findAll()).thenReturn(sales);
        assertEquals(2,saleService.findAll().size());
        assertEquals(sales.get(0).getCustomerName(),
                saleService.findAll().get(0).getCustomerName());

    }
    @Test
    void getAllNotExistSale() {
        when(saleRepository.findAll()).thenReturn(List.of());
        assertEquals(0,saleService.findAll().size());
    }
    @Test
    void saveValidSale() {
        Sale sale =  new Sale(1L,"mengstu",null
                , BigDecimal.valueOf(12),PENDING,null,null);
        when(saleRepository.save(sale)).thenReturn(sale);
        assertEquals(sale,saleService.save(sale));
        assertEquals(sale.getCustomerName(),saleService.save(sale).getCustomerName());
    }
    @Test
    void saveNotExistSale() {
       doThrow(IllegalArgumentException.class).when(saleRepository).save(null);
       assertThrows(IllegalArgumentException.class, () -> saleService.save(null));
       verify(saleRepository, never()).save(any());

    }

    @Test
    void getSaleByValidId() {
        Sale sale =  new Sale(1L,"mengstu",null
                , BigDecimal.valueOf(12),PENDING,null,null);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        assertEquals(sale.getCustomerName(),saleService.findById(1L).get().getCustomerName());
    }

    @Test
    void getSaleByInvalidId() {
        doThrow(IllegalArgumentException.class).when(saleRepository).save(any());
        assertThrows(IllegalArgumentException.class, () -> saleService.findById(null));
    }

    @Test
    void deleteSaleByValidId() {
        Sale sale =  new Sale(1L,"mengstu",null
                , BigDecimal.valueOf(12),PENDING,null,null);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        doNothing().when(saleRepository).deleteById(1L);
        saleService.deleteById(1L);
        verify(saleRepository, times(1)).deleteById(1L);
    }
    @Test
    void deleteSaleByInvalidId() {
        doThrow(EntityNotFoundException.class).when(saleRepository).deleteById(1L);
        assertThrows(EntityNotFoundException.class,()-> saleService.deleteById(1L));
        verify(saleRepository, never()).deleteById(1L);
    }

    @Test
    void updateSaleByValidId() {
        Sale sale =  new Sale(1L,"mengstu",null
                , BigDecimal.valueOf(12),PENDING,null,null);
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleRepository.save(sale)).thenReturn(sale);
        assertEquals(saleService.update(1L,sale),sale);
        verify(saleRepository, times(1)).save(sale);
    }
    @Test
    void updateSaleByInvalidId() {
        Sale sale =  new Sale(1L,"mengstu",null
                , BigDecimal.valueOf(12),PENDING,null,null);
        when( saleRepository.findById(1L)).thenReturn(Optional.empty());
        doThrow(EntityNotFoundException.class).when(saleRepository).save(any());
        assertThrows(EntityNotFoundException.class,()->saleService.update(1L,sale));
    }



}