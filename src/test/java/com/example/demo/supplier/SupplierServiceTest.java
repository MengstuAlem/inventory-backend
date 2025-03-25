package com.example.demo.supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class SupplierServiceTest {
    @Mock
    private SupplierRepository supplierRepository;
    @InjectMocks
    private SupplierService supplierService;

    @BeforeEach
    void setUp() {
      initMocks(this);
    }

    @Test
    public void getAllExistSuppliers() {
        List<Supplier> suppliers = List.of(
                new Supplier(1L,"vdfon","mengstu","0944251640","mengstu@gmail.com","addis",null),
                new Supplier(2L,"vdfon2","alem","0944251640","mengstu@gmail.com","mekelle",null)

        );
        when(supplierRepository.findAll()).thenReturn(suppliers);
        assertEquals(supplierService.findAll().size(), suppliers.size());
        assertEquals(supplierService.findAll().get(0).getId(), suppliers.get(0).getId());
    }

    @Test
    public void tryGetAllSuppliersNotExist() {
        when(supplierRepository.findAll()).thenReturn(List.of());
        assertEquals(0, supplierService.findAll().size());
    }

    @Test
    public void saveValidSupplier() {
      Supplier supplier=  new Supplier(1L,"vdfon","mengstu",
              "0944251640","mengstu@gmail.com","addis",
              null);
        when( supplierRepository.save(supplier)).thenReturn(supplier);
        assertEquals(supplierService.save(supplier), supplier);
        verify(supplierRepository,times(1)).save(supplier);

    }
    @Test
    public void saveInvalidSupplier() {
        doThrow(IllegalArgumentException.class).when(supplierRepository).save(any());
        assertThrows(IllegalArgumentException.class,() -> supplierService.save(null));
    }

    @Test
    public void findSupplierByValidId() {
        Supplier supplier=  new Supplier(1L,"vdfon","mengstu",
                "0944251640","mengstu@gmail.com","addis",
                null);
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        var res=supplierService.findById(1L).orElseThrow(()-> new IllegalArgumentException("Supplier not found"));
        assertEquals(supplier,res);

    }
    @Test
    public void findSupplierByInvalidId() {
        assertThrows(IllegalArgumentException.class,() -> supplierService.findById(0L));
    }
    @Test
    public void deleteSupplierByValidId() {
        when(supplierRepository.existsById(1L)).thenReturn(true);
        doNothing().when(supplierRepository).deleteById(1L);
        supplierService.deleteById(1L);
        verify(supplierRepository,times(1)).deleteById(1L);
    }
    @Test
    public void deleteSupplierByInvalidId() {
        assertThrows(IllegalArgumentException.class,() -> supplierService.deleteById(0L));
    }

    @Test
    public void updateSupplierByValidId() {
        Supplier supplier=  new Supplier(1L,"vdfon","mengstu",
                "0944251640","mengstu@gmail.com","addis",
                null);
        when( supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(supplierRepository.save(supplier)).thenReturn(supplier);
        supplierService.Update(1L,supplier);
        verify(supplierRepository,times(1)).save(supplier);
        verify(supplierRepository,times(1)).findById(1L);

    }

    @Test
    public void updateSupplierByInvalidId() {
        Supplier supplier=  new Supplier(1L,"vdfon","mengstu",
                "0944251640","mengstu@gmail.com","addis",
                null);
        assertThrows(IllegalArgumentException.class,() -> supplierService.Update(0L,supplier));
    }
    @Test
    public void TryUpdateNullSupplier() {
        assertThrows(IllegalArgumentException.class,() -> supplierService.Update(1L,null));
    }

}