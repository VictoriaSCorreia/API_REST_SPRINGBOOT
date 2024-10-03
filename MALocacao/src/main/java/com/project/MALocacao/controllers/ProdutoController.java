package com.project.MALocacao.controllers;

import com.project.MALocacao.dtos.ProdutoDto;
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
@RequestMapping("/produto")
public class ProdutoController {

    
    final ProdutoService produtoService;
    final EntradaService entradaService; // Adicione esta linha

    // Modifique o construtor para incluir EntradaService
    public ProdutoController(ProdutoService produtoService, EntradaService entradaService) {
        this.produtoService = produtoService;
        this.entradaService = entradaService; // Adicione esta linha
    }

    @PostMapping
    public ResponseEntity<Object> saveProduto(@RequestBody @Valid ProdutoDto produtoDto){
        if(produtoService.existsByNome(produtoDto.getNome())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esse tipo de produto já existe!");
        }
        var produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDto, produtoModel);;
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.save(produtoModel));
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoModel>> getAllProdutos(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.findAll(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneProduto(@PathVariable(value = "id") Long id){
        Optional<ProdutoModel> produtoModelOptional = produtoService.findById(id);
        if (!produtoModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(produtoModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable(value = "id") Long id) {
        Optional<ProdutoModel> produtoModelOptional = produtoService.findById(id);
        if (!produtoModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        if (entradaService.existsByProdutoId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Não é possível excluir este produto porque ele possui entradas associadas.");
        }
        produtoService.delete(produtoModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduto(@PathVariable(value = "id") Long id,
                                                    @RequestBody @Valid ProdutoDto produtoDto){
        Optional<ProdutoModel> produtoModelOptional = produtoService.findById(id);
        if (!produtoModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        if (produtoService.existsByNome(produtoDto.getNome()) && !produtoModelOptional.get().getNome().equals(produtoDto.getNome())) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Esse nome de produto já está em uso por outro produto.");
        }
        
        ProdutoModel produtoModel1 = produtoModelOptional.get();
        Long nUnidadesAnterior = produtoModel1.getNumUnidades();

        var produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDto, produtoModel);

        produtoModel.setId(produtoModelOptional.get().getId());
        produtoModel.setNumUnidades(nUnidadesAnterior);
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.save(produtoModel));
    }

}

