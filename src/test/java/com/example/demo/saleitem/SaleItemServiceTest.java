package com.example.demo.saleitem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleItemServiceTest {
    @Mock
    private SaleItemRepository saleItemRepository;
    @InjectMocks
    private SaleItemService saleItemService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAllExistingSaleItems() {
        List<SaleItem> saleItems = List.of(
                new SaleItem(1L,12, BigDecimal.valueOf(12),null,null),
                new SaleItem(1L,12, BigDecimal.valueOf(12),null,null)
        );
        when(saleItemRepository.findAll()).thenReturn(saleItems);
        assertEquals(saleItems.size(),saleItemService.findAll().size());
    }

    @Test
    void tryFindNonExistingSaleItem() {
        when(saleItemRepository.findAll()).thenReturn(List.of());
        assertEquals(0, saleItemRepository.findAll().size());
    }

    @Test
    void saveValidSaleItem() {
        SaleItem saleItem =  new SaleItem(1L,12, BigDecimal.valueOf(12),null,null);
        when(saleItemRepository.save(saleItem)).thenReturn(saleItem);
        SaleItem saved = saleItemService.save(saleItem);
        assertEquals(1, saved.getId());
        assertEquals(12, saved.getQuantity());

    }
    @Test
    void saveInvalidSaleItem() {
        assertThrows(IllegalArgumentException.class, () -> {
            saleItemService.save(null);
        });
    }
    @Test
    void deleteExistingSaleItem() {
        when(saleItemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(saleItemRepository).deleteById(1L);
        saleItemService.delete(1L);
        verify(saleItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteNonExistingSaleItem() {
        when(saleItemRepository.existsById(1L)).thenReturn(false);
       assertThrows(IllegalArgumentException.class, () -> {
           saleItemService.delete(1L);
       });
    }
    @Test
    void updateExistingSaleItem() {
        SaleItem saleItem =  new SaleItem(1L,12, BigDecimal.valueOf(12),null,null);
        when(saleItemRepository.existsById(1L)).thenReturn(true);
        when(saleItemRepository.save(saleItem)).thenReturn(saleItem);
        assertEquals(1,saleItemService.update(1L,saleItem).getId());
    }
    @Test
    void updateNonExistingSaleItem() {
        when(saleItemRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> {
            saleItemService.update(1L,null);
        });
    }

    @Test
    void findExistSaleItemByValidId(){
        SaleItem saleItem =  new SaleItem(1L,12, BigDecimal.valueOf(12),null,null);
        when(saleItemRepository.findById(1L)).thenReturn(Optional.of(saleItem));
        assertEquals(saleItemService.findById(1L).get().getId(),saleItem.getId());
    }

    @Test
    void findNonExistSaleItemByValidId(){
        when(saleItemRepository.findById(1L)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(),saleItemService.findById(1L));
    }

}