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
    public ResponseEntity<Object> saveEntrada(@RequestBody @Valid EntradaDto entradaDto, @RequestParam(value = "produtoId") Long produtoId) {
        var entrada = new EntradaModel();
        BeanUtils.copyProperties(entradaDto, entrada);
        try {
            // Usa-se o método create e não o save pois há verificações necessárias com relação ao (produto) 
            return ResponseEntity.status(HttpStatus.CREATED).body(entradaService.createEntrada(entrada, produtoId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<EntradaModel>> getAllEntradas(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(entradaService.findAll(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneEntrada(@PathVariable(value = "id") Long id) {
        Optional<EntradaModel> entradaModelOptional = entradaService.findById(id);
        return entradaModelOptional.<ResponseEntity<Object>>map(entradaModel -> ResponseEntity.status(HttpStatus.OK).body(entradaModel)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrada não encontrada."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEntrada(@PathVariable(value = "id") Long id) {
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

        EntradaModel entrada = entradaModelOptional.get();
        ProdutoModel produto = entrada.getProduto();

        if (entradaDto.getQuantidade() < 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade solicitada inválida ou maior que o estoque disponível do produto.");
        }

        // Pega a quantidade anterior vinda na Entrada e a nova
        Long quantidadeAnterior = entrada.getQuantidade();
        Long novaQuantidade = entradaDto.getQuantidade();

        /* Pega as informações do DTO que veio no corpo da requisição e altera 
        a EntradaModel 
        (semelhante ao BeanUtils.copyProperties(produtoDto, produtoModel) em Produto Controller) */
        entrada.setData(entradaDto.getData());
        entrada.setQuantidade(novaQuantidade);
        entrada.setNotaFiscal(entradaDto.getNotaFiscal());

        // Subtrai ou adiciona (unidades) em Produto dependendo da alteração feita em (quantidade) na Entrada
        if (novaQuantidade > quantidadeAnterior) {
            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() + (novaQuantidade - quantidadeAnterior));
        } else if (novaQuantidade < quantidadeAnterior) {
            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - (quantidadeAnterior - novaQuantidade));
        }

        // Salva alterações
        produtoService.save(produto);
        return ResponseEntity.status(HttpStatus.OK).body(entradaService.save(entrada));
    }
}



