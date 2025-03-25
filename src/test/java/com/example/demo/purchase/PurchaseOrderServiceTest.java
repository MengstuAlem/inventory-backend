package com.example.demo.purchase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseOrderServiceTest {
    @Mock
    PurchaseOrderRepository purchaseOrderRepository;
    @InjectMocks
    PurchaseOrderService purchaseOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = List.of(
                new PurchaseOrder(1L, Instant.now(), BigDecimal.valueOf(12),PurchaseOrderStutas.PENDING,Instant.now(),null,null),
                new PurchaseOrder(2L, Instant.now(), BigDecimal.valueOf(1223),PurchaseOrderStutas.PENDING,Instant.now(),null,null)
        );
        when(purchaseOrderRepository.findAll()).thenReturn(purchaseOrders);
        assertEquals(2,purchaseOrderService.findAll().size());
    }
    @Test
    void findPurchaseOrderByValidId() {
        PurchaseOrder purchaseOrder=new PurchaseOrder(1L, Instant.now(),
                BigDecimal.valueOf(12),PurchaseOrderStutas.PENDING,Instant.now(),
                null,null);
        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrder));
        assertEquals(purchaseOrder.getId(),purchaseOrderService.findById(1L).get().getId());

    }

    @Test
    void findPurchaseOrderByInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> purchaseOrderService.findById(0L));
        verify(purchaseOrderRepository,never()).findById(anyLong());

    }

    @Test
    void saveValidPurchaseOrder() {
        PurchaseOrder purchaseOrder=new PurchaseOrder(1L, Instant.now(),
                BigDecimal.valueOf(12),PurchaseOrderStutas.PENDING,Instant.now(),
                null,null);
        when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);
        assertEquals(purchaseOrder.getId(),purchaseOrderService.save(purchaseOrder).getId());
        verify(purchaseOrderRepository,times(1)).save(purchaseOrder);

    }
    @Test
    void saveInvalidPurchaseOrder() {
        assertThrows(NullPointerException.class,()->{
            purchaseOrderService.save(null);
        });
        verify(purchaseOrderRepository,never()).save(any());
    }
    @Test
    void tryToSaveExistingPurchaseOrder() {
        PurchaseOrder purchaseOrder=new PurchaseOrder(1L, Instant.now(),
                BigDecimal.valueOf(12),PurchaseOrderStutas.PENDING,Instant.now(),
                null,null);
        when(purchaseOrderRepository.existsById(1L)).thenReturn(true);
        assertThrows(IllegalArgumentException.class,()->{
            purchaseOrderService.save(purchaseOrder);
        });

        verify(purchaseOrderRepository,never()).save(purchaseOrder);
    }
    @Test
    void deletePurchaseOrderByValidId() {
        when(purchaseOrderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(purchaseOrderRepository).deleteById(1L);
        purchaseOrderService.deleteById(1L);
        verify(purchaseOrderRepository,times(1)).deleteById(1L);
    }
    @Test
    void tryToDeleteNonExistedPurchaseOrderById() {
       when(purchaseOrderRepository.existsById(1L)).thenReturn(false);
       assertThrows(IllegalArgumentException.class,()->{
           purchaseOrderService.deleteById(1L);
       });
        verify(purchaseOrderRepository,never()).deleteById(1L);
    }

    @Test
    void deletePurchaseOrderByInvalidId() {
        assertThrows(IllegalArgumentException.class,()->{
            purchaseOrderService.deleteById(0L);
        });
        verify(purchaseOrderRepository,never()).deleteById(0L);
    }

    @Test
    void updateValidPurchaseOrder() {
        PurchaseOrder purchaseOrder=new PurchaseOrder(1L, Instant.now(),
                BigDecimal.valueOf(12),PurchaseOrderStutas.PENDING,Instant.now(),
                null,null);
        PurchaseOrder ExistingPurchaseOrder=new PurchaseOrder(1L, Instant.now(),
                BigDecimal.valueOf(121),PurchaseOrderStutas.PENDING,Instant.now(),
                null,null);
        when(purchaseOrderRepository.existsById(1L)).thenReturn(true);
        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(ExistingPurchaseOrder));
        when(purchaseOrderRepository.save(ExistingPurchaseOrder)).thenReturn(ExistingPurchaseOrder);
       PurchaseOrder res=purchaseOrderService.update(1L,purchaseOrder);
       assertEquals(purchaseOrder.getId(),res.getId());

    }

}