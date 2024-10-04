package com.project.MALocacao.controllers;

import com.project.MALocacao.dtos.EntradaDto;
import com.project.MALocacao.models.EntradaModel;
import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.services.EntradaService;
import com.project.MALocacao.services.ProdutoService;

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
@RequestMapping("/entrada")
public class EntradaController {

    final EntradaService entradaService;
    final ProdutoService produtoService;

    public EntradaController(EntradaService entradaService, ProdutoService produtoService) {
        this.entradaService = entradaService;
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Object> saveEntrada(@RequestBody @Valid EntradaDto entradaDto, @RequestParam(value = "produtoId") Long produtoId){
        var entradaModel = new EntradaModel();
        BeanUtils.copyProperties(entradaDto, entradaModel);;
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(entradaService.createEntrada(entradaModel, produtoId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<EntradaModel>> getAllEntradas(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(entradaService.findAll(pageable));
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneEntrada(@PathVariable(value = "id") Long id){
        Optional<EntradaModel> entradaModelOptional = entradaService.findById(id);
        if (!entradaModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrada não encontrada.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(entradaModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEntrada(@PathVariable(value = "id") Long id){
        Optional<EntradaModel> entradaModelOptional = entradaService.findById(id);
        if (!entradaModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrada não encontrada.");
        }
        entradaService.delete(entradaModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Entrada deletada.");
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEntrada(@PathVariable(value = "id") Long id,
                                                @RequestBody @Valid EntradaDto entradaDto) {
        Optional<EntradaModel> entradaModelOptional = entradaService.findById(id);
        if (!entradaModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrada não encontrada.");
        }
    
        EntradaModel entradaModel = entradaModelOptional.get();
        ProdutoModel produto = entradaModel.getProduto(); 

        Long quantidadeAnterior = entradaModel.getQuantidade();
        Long novaQuantidade = entradaDto.getQuantidade();
    
        entradaModel.setData(entradaDto.getData());
        entradaModel.setQuantidade(novaQuantidade);
    
        if (novaQuantidade > quantidadeAnterior) {
            produto.setNumUnidades(produto.getNumUnidades() + (novaQuantidade - quantidadeAnterior));
        } else if (novaQuantidade < quantidadeAnterior) {
            produto.setNumUnidades(produto.getNumUnidades() - (quantidadeAnterior - novaQuantidade));
        }
    
        produtoService.save(produto); 
        return ResponseEntity.status(HttpStatus.OK).body(entradaService.save(entradaModel));
    }
}

