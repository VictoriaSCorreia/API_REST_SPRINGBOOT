package com.project.MALocacao.controllers;

import com.project.MALocacao.dtos.DispatchDto;
import com.project.MALocacao.models.DispatchModel;
import com.project.MALocacao.models.ProductModel;
import com.project.MALocacao.services.DispatchService;
import com.project.MALocacao.services.ProductService;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/dispatch")
public class DispatchController {

    final DispatchService dispatchService;
    final ProductService productService;

    public DispatchController(DispatchService dispatchService, ProductService productService) {
        this.dispatchService = dispatchService;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> saveDispatch(@RequestBody @Valid DispatchDto dispatchDto, @RequestParam(value = "productId") Long productId){
        var dispatch = new DispatchModel();
        BeanUtils.copyProperties(dispatchDto, dispatch);
        // Usa-se o método create e não o save pois há verificações necessárias com relação ao (product) 
        return ResponseEntity.status(HttpStatus.CREATED).body(dispatchService.createDispatch(dispatch, productId));
    }

    @GetMapping
    public ResponseEntity<Page<DispatchModel>> getAllDispatches(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(dispatchService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneDispatch(@PathVariable(value = "id") Long id){
        Optional<DispatchModel> dispatchModelOptional = dispatchService.findById(id);
        return dispatchModelOptional.<ResponseEntity<Object>>map(dispatchModel -> ResponseEntity.status(HttpStatus.OK).body(dispatchModel)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Saída não encontrada."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDispatch(@PathVariable(value = "id") Long id){
        dispatchService.validateDispatchExists(id);
        /*
        dispatchService.deleteById(id); */
        return ResponseEntity.status(HttpStatus.OK).body("Saída não pode ser deletada, pois está associada a um produto.");  
        
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDispatch(@PathVariable(value = "id") Long id,
                                                @RequestBody @Valid DispatchDto dispatchDto) {
        dispatchService.validateDispatchExists(id);
        
        Optional<DispatchModel> dispatchModelOptional = dispatchService.findById(id);
        DispatchModel dispatch = dispatchModelOptional.get();
        ProductModel product = dispatch.getProduct();
        dispatchService.validateQuantityUpdate(dispatch.getQuantity(), dispatchDto.getQuantity(), product.getStockCount()); 

        return ResponseEntity.status(HttpStatus.OK).body(dispatchService.updateDispatch(id, dispatchDto));
    }
}

