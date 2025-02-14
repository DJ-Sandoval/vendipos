package com.systempos.vendify.service;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systempos.vendify.model.Concepto;
import com.systempos.vendify.model.Producto;
import com.systempos.vendify.model.Venta;
import com.systempos.vendify.repository.ConceptoRepository;
import com.systempos.vendify.repository.ProductoRepository;
import com.systempos.vendify.repository.VentaRepository;



@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ConceptoRepository conceptoRepository;

    

    @Autowired
    private ProductoRepository productoRepository;

    public Venta crearVenta(Venta venta) {
        venta.setFecha(LocalDate.now());
        double total = 0;

        // Asignar idVenta a cada concepto
        for (Concepto concepto : venta.getConceptos()) {
            Producto producto = productoRepository.findById(concepto.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            concepto.setPrecioUnitario(producto.getPrecioUnitario());
            concepto.setImporte(concepto.getCantidad() * concepto.getPrecioUnitario());
            concepto.setVenta(venta);  // Vincula el concepto a la venta
            total += concepto.getImporte();
        }

        venta.setTotal(total);
        // Guardar la venta y los conceptos
        Venta ventaGuardada = ventaRepository.save(venta);
        
        // Asegurarse de que los conceptos se guardan con el idVenta correcto
        for (Concepto concepto : venta.getConceptos()) {
            conceptoRepository.save(concepto);
        }

        return ventaGuardada;
    }

    public void eliminarVenta(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada");
        }
        ventaRepository.deleteById(id);
    }

}
