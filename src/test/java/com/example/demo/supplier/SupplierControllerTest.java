package com.example.demo.supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@WebMvcTest(SupplierController.class)
class SupplierControllerTest {
    @MockBean
    private SupplierService supplierService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllSuppliersTest() throws Exception {
        List<Supplier> suppliers = List.of(
                new Supplier(1L,"vdfon","mengstu","0944251640","mengstu@gmail.com","addis",null),
                new Supplier(2L,"vdfon2","alem","0944251640","mengstu@gmail.com","mekelle",null)

        );
        when(supplierService.findAll()).thenReturn(suppliers);
        mockMvc.perform(MockMvcRequestBuilders.get("/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(suppliers)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("vdfon"));
    }

    @Test
    void saveSupplierTest() throws Exception {
        Supplier supplier= new Supplier(1L,"vdfon","mengstu","0944251640",
                "mengstu@gmail.com","addis",null);
        when(supplierService.save(supplier)).thenReturn(supplier);
        mockMvc.perform(MockMvcRequestBuilders.post("/suppliers")
                .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("vdfon"));
    }

    @Test
    void getSupplierByIdTest() throws Exception {
     Supplier supplier=   new Supplier(1L,"vdfon","mengstu",
                "0944251640","mengstu@gmail.com","addis",
                null);
        when(supplierService.findById(1L)).thenReturn(Optional.of(supplier));
        mockMvc.perform(MockMvcRequestBuilders.get("/suppliers/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("vdfon"));

    }
    @Test
    void tryGetNonExistingSupplierByIdTest() throws Exception {
        when(supplierService.findById(1L)).thenReturn(Optional.empty());
      mockMvc.perform(MockMvcRequestBuilders.get("/suppliers/1"))
              .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
    @Test
    void getSupplierByInvalidIdTest() throws Exception {
     mockMvc.perform(MockMvcRequestBuilders.get("/suppliers/0"))
             .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void updateSupplierTest() throws Exception {
        Supplier supplier=   new Supplier(1L,"vdfon","mengstu",
                "0944251640","mengstu@gmail.com","addis",
                null);
        when(supplierService.Update(1l,supplier)).thenReturn(supplier);
        mockMvc.perform(MockMvcRequestBuilders.put("/suppliers/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("vdfon"));


    }
    @Test
    void tryUpdateNonExistingSupplierByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/suppliers/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    void tryUpdateSupplierByInvalidIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/suppliers/0"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void tryUpdateSupplierByNullIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/suppliers/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    void deleteSupplierByIdTest() throws Exception {
        doNothing().when(supplierService).deleteById(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/suppliers/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    @Test
    void deleteSupplierByInvalidIdTest() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
                .when(supplierService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/suppliers/1"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }



}