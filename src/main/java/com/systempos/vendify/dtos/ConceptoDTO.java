package com.systempos.vendify.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConceptoDTO {
    private Long id;
    private Long idVenta;
    private int cantidad;
    private double precioUnitario;
    private double importe;
    private Long idProducto;
}