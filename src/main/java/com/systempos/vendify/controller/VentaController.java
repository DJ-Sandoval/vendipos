package com.systempos.vendify.controller;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMethod;
import com.itextpdf.text.DocumentException;
import com.systempos.vendify.model.Producto;
import com.systempos.vendify.model.Venta;
import com.systempos.vendify.reports.ReporteService;
import com.systempos.vendify.reports.TicketService;
import com.systempos.vendify.repository.ProductoRepository;
import com.systempos.vendify.repository.VentaRepository;
import com.systempos.vendify.service.VentaService;

@Controller
@RequestMapping("/ventas")
public class VentaController {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;
    
    @Autowired
    private VentaService ventaService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/nueva")
    public String mostrarFormularioVenta(Model model) {
        model.addAttribute("venta", new Venta());
        model.addAttribute("productos", productoRepository.findAll()); // Cargar productos en el modelo
        return "nueva-venta";
    }

    @GetMapping("/producto/{id}")
    @ResponseBody
    public Producto obtenerProducto(@PathVariable Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @GetMapping("/inicio")
    public String verPaginaVenta(Model model) {
        List<Venta> ventas = ventaRepository.findAll();
        model.addAttribute("ventas", ventas);
        return "ventas";
    }
    

    @PostMapping("/guardar")
public String guardarVenta(@ModelAttribute Venta venta) {
    if (venta.getConceptos() == null || venta.getConceptos().isEmpty()) {
        throw new RuntimeException("Debe agregar al menos un producto ala venta");
    }
    // Asegúrate de que el cliente se está vinculando correctamente
    if (venta.getCliente() == null || venta.getCliente().getId() == null) {
        // Manejar el caso cuando el cliente no está presente
        return "error"; // O alguna página de error
    }

    // Crear la venta
    Venta ventaGuardada = ventaService.crearVenta(venta);

    // Generar el ticket después de guardar la venta
    try {
        ticketService.generarTicketPDF(ventaGuardada);
    } catch (DocumentException | IOException e) {
        e.printStackTrace();
        // Manejar el error de generación del ticket
    }

        return "redirect:/ventas/nueva";
    }

    @GetMapping("/historial")
    public String mostrarHistorial(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                               Model model) {
        if (fechaInicio == null) {
            fechaInicio = LocalDate.now().minusMonths(1); // por defecto ultimos 30 dias
        }
        if (fechaFin == null) {
            fechaFin = LocalDate.now();
        }
        List<Venta> ventas = ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
        model.addAttribute("ventas", ventas);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        return "historial-ventas";
    }
    
    @GetMapping("/reporte")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> generarReporteVentas() {
        try {
            byte[] pdf = reporteService.generarReporteVentas();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ReporteVentas.pdf");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String eliminarVenta(@PathVariable Long id, RedirectAttributes redirect) {
        Venta venta = ventaRepository.getById(id);
        ventaRepository.delete(venta);
        redirect.addFlashAttribute("msgExito", "La venta ha sido eliminada con exito");
        return "redirect:/ventas/inicio";
    
    }



}

