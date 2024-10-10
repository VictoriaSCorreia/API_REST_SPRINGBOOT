package com.project.MALocacao.controllers;

import java.util.List;

import com.project.MALocacao.models.SaidaModel;

public record ProdutoSaidas(Long produtoId, List<SaidaModel> saidas) {};