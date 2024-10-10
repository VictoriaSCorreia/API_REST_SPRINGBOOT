package com.project.MALocacao.controllers;

import java.util.List;

import com.project.MALocacao.models.EntradaModel;

public record ProdutoEntradas(Long produtoId, List<EntradaModel> entradas) {};