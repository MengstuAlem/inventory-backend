package com.example.demo.supplier;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Supplier save(Supplier supplier) {
        if (supplier==null){
            throw new IllegalArgumentException("supplier must not be null");
        }
        return supplierRepository.save(supplier);
    }

    public Optional<Supplier> findById(long id) {
        if (id<=0){
            throw new IllegalArgumentException("id must be positive");
        }
        return supplierRepository.findById(id);
    }

    public void deleteById(long id) {
        if (id<=0 || !supplierRepository.existsById(id)){
            throw new IllegalArgumentException("id must be positive");
        }
        supplierRepository.deleteById(id);
    }

    public Supplier Update(Long id, Supplier supplier) {
        if (id<=0 ){
            throw new IllegalArgumentException("id must be positive");
        }
        if (supplier==null){
            throw new IllegalArgumentException("supplier must not be null");
        }
        Supplier existingSupplier = supplierRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Supplier with id "+id+" not found")
        );
        if(supplier.getName() != null) {
            existingSupplier.setName(supplier.getName());
        }
       if (supplier.getAddress() != null) {
           existingSupplier.setAddress(supplier.getAddress());
       }
       if (supplier.getEmail() != null) {
           existingSupplier.setEmail(supplier.getEmail());
       }
       if (supplier.getPhone() != null) {
           existingSupplier.setPhone(supplier.getPhone());
       }



        return supplierRepository.save(existingSupplier);
    }
}
