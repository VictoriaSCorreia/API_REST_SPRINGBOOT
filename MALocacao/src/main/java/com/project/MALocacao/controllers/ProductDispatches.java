package com.project.MALocacao.controllers;

import java.util.List;

import com.project.MALocacao.models.DispatchModel;

public record ProductDispatches(Long productId, List<DispatchModel> dispatches) {};