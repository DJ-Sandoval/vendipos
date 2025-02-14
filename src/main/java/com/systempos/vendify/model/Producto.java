package com.systempos.vendify.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "necesita insertar el nombre")
    private String nombre;

    @Min(value = 0, message = "El precio unitario no puede ser negativo")
    private double precioUnitario;
    private double costo;

    @AssertTrue(message = "El precio de venta debe ser mayor o igual al costo")
    private boolean isPrecioValido() {
        return precioUnitario >= costo;
    }
}

