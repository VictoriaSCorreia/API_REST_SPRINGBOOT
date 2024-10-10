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
        // Usa-se o método create e não o save pois há verificações necessárias com relação ao (produto) 
        return ResponseEntity.status(HttpStatus.CREATED).body(saidaService.createSaida(saida, produtoId));
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
        saidaService.validarSaidaExiste(id);
        saidaService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Saida deletada.");  
        
    }
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSaida(@PathVariable(value = "id") Long id,
                                                @RequestBody @Valid SaidaDto saidaDto) {
        saidaService.validarSaidaExiste(id);
        
        Optional<SaidaModel> saidaModelOptional = saidaService.findById(id);
        SaidaModel saida = saidaModelOptional.get();
        ProdutoModel produto = saida.getProduto();
        saidaService.validarQuantidadeUpdate(saida.getQuantidade(), saidaDto.getQuantidade(), produto.getQuantidadeEmEstoque()); 

        return ResponseEntity.status(HttpStatus.OK).body(saidaService.updateSaida(id, saidaDto));
    }
}

