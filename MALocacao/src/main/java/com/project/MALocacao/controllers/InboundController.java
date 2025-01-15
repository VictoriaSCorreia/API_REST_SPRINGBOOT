package com.project.MALocacao.controllers;

import com.project.MALocacao.dtos.InboundDto;
import com.project.MALocacao.models.InboundModel;
import com.project.MALocacao.services.InboundService;
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
@RequestMapping("/inbound")
public class InboundController {

    final InboundService inboundService;
    final ProductService productService;

    public InboundController(InboundService inboundService, ProductService productService) {
        this.inboundService = inboundService;
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Object> saveInbound(@RequestBody @Valid InboundDto inboundDto,
            @RequestParam(value = "productId") Long productId) {
        var inbound = new InboundModel();
        BeanUtils.copyProperties(inboundDto, inbound);
        // Usa-se o método create e não o save pois há verificações necessárias com
        // relação ao (product)
        return ResponseEntity.status(HttpStatus.CREATED).body(inboundService.createInbound(inbound, productId));
    }

    @GetMapping
    public ResponseEntity<Page<InboundModel>> getAllInbounds(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(inboundService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneInbound(@PathVariable(value = "id") Long id) {
        Optional<InboundModel> inboundModelOptional = inboundService.findById(id);
        return inboundModelOptional
                .<ResponseEntity<Object>>map(inboundModel -> ResponseEntity.status(HttpStatus.OK).body(inboundModel))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Inbound não encontrada."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteInbound(@PathVariable Long id) {
        inboundService.validarInboundExiste(id);
        /* 
        inboundService.deleteById(id); */
        return ResponseEntity.status(HttpStatus.OK).body("Inbound não pode ser deletada, pois está associada a um product.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateInbound(@PathVariable(value = "id") Long id,
            @RequestBody @Valid InboundDto inboundDto) {
        inboundService.validarInboundExiste(id);
        inboundService.validarQuantidade(inboundDto.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(inboundService.updateInbound(id, inboundDto));
    }
}
