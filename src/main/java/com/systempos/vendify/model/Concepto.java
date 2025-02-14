package com.systempos.vendify.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "concepto")
@Data
@Table(name = "conceptos")
@NoArgsConstructor
@AllArgsConstructor
public class Concepto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Venta venta;
    private int cantidad;
    private double precioUnitario;
    private double importe;
    @ManyToOne
    private Producto producto;
}
