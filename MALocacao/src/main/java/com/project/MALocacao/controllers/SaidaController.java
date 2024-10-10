package com.project.MALocacao.controllers;

import com.project.MALocacao.dtos.SaidaDto;
import com.project.MALocacao.models.SaidaModel;
import com.project.MALocacao.models.ProdutoModel;
import com.project.MALocacao.services.SaidaService;
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
@RequestMapping("/saida")
public class SaidaController {

    final SaidaService saidaService;
    final ProdutoService produtoService;

    public SaidaController(SaidaService saidaService, ProdutoService produtoService) {
        this.saidaService = saidaService;
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<Object> saveSaida(@RequestBody @Valid SaidaDto saidaDto, @RequestParam(value = "produtoId") Long produtoId){
        var saida = new SaidaModel();
        BeanUtils.copyProperties(saidaDto, saida);
        try {
            // Usa-se o método create e não o save pois há verificações necessárias com relação ao (produto) 
            return ResponseEntity.status(HttpStatus.CREATED).body(saidaService.createSaida(saida, produtoId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<SaidaModel>> getAllSaidas(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(saidaService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneSaida(@PathVariable(value = "id") Long id){
        Optional<SaidaModel> saidaModelOptional = saidaService.findById(id);
        return saidaModelOptional.<ResponseEntity<Object>>map(saidaModel -> ResponseEntity.status(HttpStatus.OK).body(saidaModel)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Saída não encontrada."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSaida(@PathVariable(value = "id") Long id){
        Optional<SaidaModel> saidaModelOptional = saidaService.findById(id);
        if (!saidaModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Saida não encontrada.");
        }
        saidaService.delete(saidaModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Saida deletada.");
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSaida(@PathVariable(value = "id") Long id,
                                                @RequestBody @Valid SaidaDto saidaDto) {
        Optional<SaidaModel> saidaModelOptional = saidaService.findById(id);
        if (!saidaModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Saida não encontrada.");
        }

        SaidaModel saida = saidaModelOptional.get();
        ProdutoModel produto = saida.getProduto(); 

        if (saidaDto.getQuantidade() > produto.getQuantidadeEmEstoque() || saidaDto.getQuantidade() < 0){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Quantidade solicitada inválida ou maior que o estoque disponível do produto.");
        }

        // Pega a quantidade anterior vinda na Saída e a nova
        Long quantidadeAnterior = saida.getQuantidade();
        Long novaQuantidade = saidaDto.getQuantidade();
    
        /* Pega as informações do DTO que veio no corpo da requisição e altera 
        a SaidaModel 
        (semelhante ao BeanUtils.copyProperties(produtoDto, produtoModel) em Produto Controller) */
        saida.setData(saidaDto.getData());
        saida.setQuantidade(novaQuantidade);
        saida.setSolicitante(saidaDto.getSolicitante());
        saida.setRequisicao(saidaDto.getRequisicao());
        saida.setLocacao(saidaDto.getLocacao());
    
        // Subtrai ou adiciona (unidades) em Produto dependendo da alteração feita em (quantidade) na Saida
        if (novaQuantidade > quantidadeAnterior) {
            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() - (novaQuantidade - quantidadeAnterior));
        } else if (novaQuantidade < quantidadeAnterior) {
            produto.setQuantidadeEmEstoque(produto.getQuantidadeEmEstoque() + (quantidadeAnterior - novaQuantidade));
        }

        // Salva alterações
        produtoService.save(produto); 
        return ResponseEntity.status(HttpStatus.OK).body(saidaService.save(saida));
    }
}

