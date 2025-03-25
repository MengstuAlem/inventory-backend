package com.example.demo.purchaseorderitem;

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

class PurchaseOrderItemServiceTest {
    @Mock
    PurchaseOrderItemRepository purchaseOrderItemRepository;
    @InjectMocks
    PurchaseOrderItemService purchaseOrderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getPurchaseOrderItem() {
        List<PurchaseOrderItem> purchaseOrderItems = List.of(
                new PurchaseOrderItem(1L, 2, BigDecimal.valueOf(23), 200, null, null),
                new PurchaseOrderItem(2L, 22, BigDecimal.valueOf(123), 400, null, null)
        );
        when(purchaseOrderItemRepository.findAll()).thenReturn(purchaseOrderItems);
        assertEquals(purchaseOrderItems.size(), purchaseOrderItemService.findAll().size());
        assertEquals(purchaseOrderItems.get(0).getId(), purchaseOrderItemService.findAll().get(0).getId());
    }

    @Test
    void getPurchaseOrderItemByValidId() {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(1L, 2, BigDecimal.valueOf(23), 200, null, null);
        when(purchaseOrderItemRepository.findById(1L)).thenReturn(Optional.of(purchaseOrderItem));
        assertEquals(purchaseOrderItem.getId(), purchaseOrderItemService.findById(1L).get().getId());
    }

    @Test
    void getPurchaseOrderItemByInvalidId() {
        doThrow(RuntimeException.class).when(purchaseOrderItemRepository).findById(0L);
    }

    @Test
    void createPurchaseOrderItem() {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(1L, 2, BigDecimal.valueOf(23), 200, null, null);
        when(purchaseOrderItemRepository.save(purchaseOrderItem)).thenReturn(purchaseOrderItem);
        assertEquals(purchaseOrderItem.getId(), purchaseOrderItemService.save(purchaseOrderItem).getId());
        verify(purchaseOrderItemRepository, times(1)).save(purchaseOrderItem);
    }

    @Test
    void tryToCreateNullPurchaseOrderItemThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            purchaseOrderItemService.save(null);
        });
        verify(purchaseOrderItemRepository,never()).save(any(PurchaseOrderItem.class));

    }
    @Test
    void tryToCreateExistPurchaseOrderItemThrowsException() {
        PurchaseOrderItem purchaseOrderItem= new PurchaseOrderItem(1L,2, BigDecimal.valueOf(23),200,null,null);
        when(purchaseOrderItemRepository.findById(1L)).thenReturn(Optional.of(purchaseOrderItem));
        assertThrows(RuntimeException.class, () -> {
            purchaseOrderItemService.save(purchaseOrderItem);
        });
        verify(purchaseOrderItemRepository,never()).save(any(PurchaseOrderItem.class));
    }
    @Test
    void deletePurchaseOrderItemByValidId() {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(1L, 2, BigDecimal.valueOf(23),
                200, null, null);
        when(purchaseOrderItemRepository.findById(1L)).thenReturn(Optional.of(purchaseOrderItem));
       doNothing().when(purchaseOrderItemRepository).deleteById(1L);
       purchaseOrderItemService.deleteById(1L);
       verify(purchaseOrderItemRepository, times(1)).deleteById(1L);

    }
    @Test
    void deletePurchaseOrderItemByInvalidId() {
        doThrow(RuntimeException.class).when(purchaseOrderItemRepository).deleteById(0L);
        verify(purchaseOrderItemRepository,never()).deleteById(0L);
    }
    @Test
    void deleteNonExistPurchaseOrderItemById() {
        when(purchaseOrderItemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            purchaseOrderItemService.deleteById(1L);
        });
        verify(purchaseOrderItemRepository,never()).deleteById(1L);
    }
    @Test
    void updatePurchaseOrderItem() {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(1L, 2, BigDecimal.valueOf(23),
                200, null, null);
        PurchaseOrderItem exist = new PurchaseOrderItem(1L, 22, BigDecimal.valueOf(232), 4, null, null);
        when(purchaseOrderItemRepository.findById(1L)).thenReturn(Optional.of(exist));
        when(purchaseOrderItemRepository.save(exist)).thenReturn(exist);
        assertEquals(purchaseOrderItem.getQuantity(),purchaseOrderItemService.update(1L,purchaseOrderItem).getQuantity());
    }

    @Test
    void TryToUpdateNonExistPurchaseOrderItemThrowsException() {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem(1L, 2, BigDecimal.valueOf(23),
                200, null, null);
        when(purchaseOrderItemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            purchaseOrderItemService.update(1L,purchaseOrderItem);
        });
        verify(purchaseOrderItemRepository,never()).save(any(PurchaseOrderItem.class));
    }

}