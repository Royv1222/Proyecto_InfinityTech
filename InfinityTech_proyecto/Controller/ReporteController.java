/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Controller;

import InfinityTech_proyecto.Domain.Ticket;
import InfinityTech_proyecto.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author jose daniel
 * Historias de usuario HU9 y HU10
 */

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String mostrarFormularioFiltros(Model model) {
        model.addAttribute("titulo", "HU10 - Reportes Operativos");
        return "reportes/reportes-form";
    }

    @PostMapping("/generar")
    public String generarReporte(@RequestParam(required = false) String estado,
                                 Model model) {
        List<Ticket> todos = ticketService.listar();

        List<Ticket> filtrados = (estado == null || estado.isEmpty())
                ? todos
                : todos.stream().filter(t -> estado.equals(t.getEstado())).collect(Collectors.toList());

        model.addAttribute("tickets", filtrados);
        model.addAttribute("titulo", "HU10 - Reportes Generados");

        if (filtrados.isEmpty()) {
            model.addAttribute("mensajeVacio", "Sin resultados para el filtro.");
        }

        return "reportes/reportes-lista";
    }
}
