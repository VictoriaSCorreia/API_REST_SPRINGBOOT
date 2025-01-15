package com.project.MALocacao.controllers;

import java.util.List;

import com.project.MALocacao.models.InboundModel;

public record ProductInbounds(Long productId, List<InboundModel> inbounds) {};