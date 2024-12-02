package com.project.MALocacao.controllers;

import com.project.MALocacao.dtos.ProdutoDto;
import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.services.EntradaService;
import com.project.MALocacao.services.ProdutoService;
import com.project.MALocacao.services.SaidaService;

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
@RequestMapping("/produto")
public class ProdutoController {

    final ProdutoService produtoService;
    final EntradaService entradaService; 
    final SaidaService saidaService;

    public ProdutoController(ProdutoService produtoService, EntradaService entradaService, SaidaService saidaService) {
        this.produtoService = produtoService;
        this.entradaService = entradaService;
        this.saidaService = saidaService;
    }

    @PostMapping
    public ResponseEntity<Object> saveProduto(@RequestBody @Valid ProdutoDto produtoDto){
        // Pega as informações do DTO que veio no corpo da requisição e altera o ProdutoModel
        var produto = new ProdutoModel();
        BeanUtils.copyProperties(produtoDto, produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.create(produto));
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoModel>> getAllProdutos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProduto(@PathVariable(value = "id") Long id){
        Optional<ProdutoModel> produtoModelOptional = produtoService.findById(id);
        return produtoModelOptional
                .<ResponseEntity<Object>>map(produtoModel -> ResponseEntity.status(HttpStatus.OK).body(produtoModel))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable(value = "id") Long id) {
        produtoService.validarProdutoExiste(id);
        
        // valida se há entradas associadas a esse produto. Caso haja, ele não pode ser excluído
        produtoService.validarEntradasAssociadas(entradaService, id);
        // valida se há saidas associadas pois um produto pode ser criado já com algo no estoque, sem entradas
        produtoService.validarSaidasAssociadas(saidaService, id);

        produtoService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduto(@PathVariable(value = "id") Long id,
                                                    @RequestBody @Valid ProdutoDto produtoDto){
        produtoService.validarProdutoExiste(id); 
        Optional<ProdutoModel> produtoModelOptional = produtoService.findById(id);                                              
        var produtoModel = produtoModelOptional.get(); 
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.update(produtoModel, produtoModelOptional, produtoDto)); 
    }

    @GetMapping("/{produtoId}/entradas")
    public ResponseEntity<ProdutoEntradas> getProdutoEntradas(@PathVariable(value = "produtoId") Long produtoId) {
        return produtoService.findById(produtoId)
          .map(produto -> {
              ProdutoEntradas produtoEntradas = entradaService.getProdutoEntradas(produtoId);
              return ResponseEntity.ok(produtoEntradas);
          })
          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{produtoId}/saidas")
    public ResponseEntity<ProdutoSaidas> getProdutoSaidas(@PathVariable(value = "produtoId") Long produtoId) {
        return produtoService.findById(produtoId)
          .map(produto -> {
              ProdutoSaidas produtoSaidas = saidaService.getProdutoSaidas(produtoId);
              return ResponseEntity.ok(produtoSaidas);
          })
          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
