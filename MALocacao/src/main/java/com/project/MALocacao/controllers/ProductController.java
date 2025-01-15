package com.project.MALocacao.controllers;

import com.project.MALocacao.dtos.ProductDto;
import com.project.MALocacao.models.ProductModel;
import com.project.MALocacao.services.InboundService;
import com.project.MALocacao.services.ProductService;
import com.project.MALocacao.services.DispatchService;

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
@RequestMapping("/product")
public class ProductController {

    final ProductService productService;
    final InboundService inboundService; 
    final DispatchService dispatchService;

    public ProductController(ProductService productService, InboundService inboundService, DispatchService dispatchService) {
        this.productService = productService;
        this.inboundService = inboundService;
        this.dispatchService = dispatchService;
    }

    @PostMapping
    public ResponseEntity<Object> saveProduct(@RequestBody @Valid ProductDto productDto){
        // Pega as informações do DTO que veio no corpo da requisição e altera o ProductModel
        var product = new ProductModel();
        BeanUtils.copyProperties(productDto, product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(product));
    }

    @GetMapping
    public ResponseEntity<Page<ProductModel>> getAllProducts(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProduct(@PathVariable(value = "id") Long id){
        Optional<ProductModel> productModelOptional = productService.findById(id);
        return productModelOptional
                .<ResponseEntity<Object>>map(productModel -> ResponseEntity.status(HttpStatus.OK).body(productModel))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product não encontrado."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") Long id) {
        productService.validateProductExists(id);
        
        // valida se há inbounds associadas a esse product. Caso haja, ele não pode ser excluído
        productService.validateRelatedInbounds(inboundService, id);
        // valida se há dispatches associadas pois um product pode ser criado já com algo no estoque, sem inbounds
        productService.validateRelatedDispatches(dispatchService, id);

        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product deletado.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") Long id,
                                                    @RequestBody @Valid ProductDto productDto){
        productService.validateProductExists(id); 
        Optional<ProductModel> productModelOptional = productService.findById(id);                                              
        var productModel = productModelOptional.get(); 
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.update(productModel, productModelOptional, productDto)); 
    }

    @GetMapping("/{productId}/inbounds")
    public ResponseEntity<ProductInbounds> getProductInbounds(@PathVariable(value = "productId") Long productId) {
        return productService.findById(productId)
          .map(product -> {
              ProductInbounds productInbounds = inboundService.getProductInbounds(productId);
              return ResponseEntity.ok(productInbounds);
          })
          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{productId}/dispatches")
    public ResponseEntity<ProductDispatches> getProductDispatches(@PathVariable(value = "productId") Long productId) {
        return productService.findById(productId)
          .map(product -> {
              ProductDispatches productDispatches = dispatchService.getProductDispatches(productId);
              return ResponseEntity.ok(productDispatches);
          })
          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
