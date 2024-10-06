package com.project.MALocacao.controllers;

import java.util.List;

import com.project.MALocacao.models.EntradaModel;
import com.project.MALocacao.models.ProdutoModel;

public record ProdutoEntradas(Long produtoId, List<EntradaModel> entradas) {};