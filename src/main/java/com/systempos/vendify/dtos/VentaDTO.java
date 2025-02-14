package com.systempos.vendify.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {
    private Long id;
    private LocalDate fecha;
    private Long idCliente;
    private Long idUsuario;
    private List<ConceptoDTO> conceptos = new ArrayList<>();
    private double total;
    private double montoPagado;
}

