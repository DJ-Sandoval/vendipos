package com.systempos.vendify.controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.systempos.vendify.model.Cliente;
import com.systempos.vendify.reports.ReporteService;
import com.systempos.vendify.repository.ClienteRepository;

import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/api/clientes")
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/inicio")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String verPaginaClientes(Model model) {
        List<Cliente> clientes = clienteRepository.findAll();
        model.addAttribute("clientes", clientes);
        return "cliente";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String mostrarFormularioRegistroCliente(Model model) {
        model.addAttribute("cliente",new Cliente());
        return "nuevoCliente";
    }

    @PostMapping("/nuevo")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String guardarCliente(@Validated Cliente cliente, RedirectAttributes redirect, BindingResult binding, Model model) {
        if (binding.hasErrors()) {
            model.addAttribute("cliente", cliente);
        }
        clienteRepository.save(cliente);
        redirect.addFlashAttribute("msgExito", "El cliente se ha guardado con exito");
        return "redirect:/api/clientes/inicio";

    }

    @GetMapping("/{id}/editar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String mostrarFormularioEditarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + id));
        model.addAttribute("cliente", cliente);
        return "nuevoCliente";
    }

    @PostMapping("/{id}/editar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String actualizarCliente(@PathVariable Long id, @Validated Cliente cliente, RedirectAttributes redirect, BindingResult binding, Model model) {
        Cliente clienteDB = clienteRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + id));
    
        if (binding.hasErrors()) {
            model.addAttribute("cliente", cliente);
            return "nuevoCliente";
        }
        clienteDB.setNombre(cliente.getNombre());
        clienteRepository.save(clienteDB);
        redirect.addFlashAttribute("msgExito", "El cliente ha sido actualizado con éxito");
        return "redirect:/api/clientes/inicio";
    }

    @PostMapping("/{id}/eliminar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirect) {
        Cliente cliente = clienteRepository.getById(id);
        clienteRepository.delete(cliente);
        redirect.addFlashAttribute("msgExito", "El cliente ha sido eliminado con éxito");
        return "redirect:/api/clientes/inicio";
    }

    @GetMapping("/reporte")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<byte[]> generarReporteCliente() {
        try {
            byte[] pdf = reporteService.generarReporteClientes();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ReporteClientes.pdf");
            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

