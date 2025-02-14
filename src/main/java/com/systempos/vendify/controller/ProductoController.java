package com.systempos.vendify.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.systempos.vendify.model.Producto;
import com.systempos.vendify.reports.ReporteService;
import com.systempos.vendify.repository.ProductoRepository;

@Controller
@RequestMapping("/api/productos")
public class ProductoController {   

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/inicio")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String verPaginaProducto(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        return "producto";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String mostrarFormularioRegistroProductos(Model model) {
        model.addAttribute("producto", new Producto());
        return "nuevoProducto";
    }

    @PostMapping("/nuevo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String guardarProducto(@Validated Producto producto, RedirectAttributes redirect, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("producto", producto);
        }
        productoRepository.save(producto);
        redirect.addFlashAttribute("msgExito", "El producto se ha guardado con exito");
        return "redirect:/api/productos/inicio";
    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String mostrarFormularioEditarProducto(@PathVariable Long id, Model model) {
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));
        model.addAttribute("producto", producto);
        return "nuevoProducto";
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String actualizarProducto(@PathVariable Long id, @Validated Producto producto, RedirectAttributes redirect, BindingResult binding, Model model) {
        Producto productoDB = productoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));
        if (binding.hasErrors()) {
            model.addAttribute("producto", producto);
            return "nuevoProducto";
        }
        productoDB.setNombre(producto.getNombre());
        productoDB.setPrecioUnitario(producto.getPrecioUnitario());
        productoDB.setCosto(producto.getCosto());
        productoRepository.save(productoDB);
        redirect.addFlashAttribute("msgExito", "El producto ha sido actualizado correctamente");
        return "redirect:/api/productos/inicio";
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirect) {
        Producto producto = productoRepository.getById(id);
        productoRepository.delete(producto);
        redirect.addFlashAttribute("msgExito", "El producto ha sido eliminado correctamente");
        return "redirect:/api/productos/inicio";
    }

    @GetMapping("/precio/{id}")
    public ResponseEntity<Map<String, Double>> obtenerPrecio(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Map<String, Double> response = new HashMap<>();
            response.put("precioUnitario", productoOpt.get().getPrecioUnitario());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/reporte")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> generarReporteProductos() {
        try {
            byte[] pdf = reporteService.generarReporteProductos();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ReporteProductos.pdf");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

