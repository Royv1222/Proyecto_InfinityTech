/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.controller;

import InfinityTech_proyecto.domain.EstadoTicket;
import InfinityTech_proyecto.domain.Repuesto;
import InfinityTech_proyecto.domain.Ticket;
import InfinityTech_proyecto.service.RepuestoService;
import InfinityTech_proyecto.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/tecnico")
public class TecnicoController {

    private final TicketService ticketService;
    private final RepuestoService repuestoService;

    public TecnicoController(TicketService ticketService, RepuestoService repuestoService) {
        this.ticketService = ticketService;
        this.repuestoService = repuestoService;
    }

    @GetMapping("/tickets")
    public String listar(Model model) {
        model.addAttribute("tickets", ticketService.listarTodos());
        return "tecnico/lista";
    }

    @GetMapping("/tickets/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        model.addAttribute("ticket", ticketService.buscarPorId(id));
        model.addAttribute("estados", EstadoTicket.values());
        return "tecnico/detalle";
    }

    // Diagnostico presupuesto
    @PostMapping("/tickets/{id}/diagnostico")
    public String guardarDiagnostico(
            @PathVariable Integer id,
            @RequestParam(required = false) String diagnostico,
            @RequestParam(required = false) EstadoTicket estado,
            @RequestParam(required = false) BigDecimal presupuesto,
            HttpSession session
    ) {
        String tecnico = (String) session.getAttribute("usuario");

        // auto-asignarse si no hay técnico asignado
        Ticket t = ticketService.buscarPorId(id);
        if (t.getTecnicoAsignado() == null || t.getTecnicoAsignado().isBlank()) {
            ticketService.asignarTecnico(id, tecnico);
        }

        ticketService.actualizarDiagnostico(id, diagnostico, estado, presupuesto);
        return "redirect:/tecnico/tickets/" + id;
    }

    // =========================
    // ✅ Catálogo de repuestos
    // =========================

    @GetMapping("/repuestos")
    public String catalogoRepuestos(Model model) {
        List<Repuesto> repuestos = repuestoService.listar();
        model.addAttribute("repuestos", repuestos);
        return "tecnico/repuestos";
    }

    // crear repuesto rápido (para no hacer CRUD completo)
    @PostMapping("/repuestos/crear")
    public String crearRepuesto(@RequestParam String nombre, @RequestParam int stock) {
        repuestoService.crearSiNoExiste(nombre, stock);
        return "redirect:/tecnico/repuestos";
    }

    @PostMapping("/repuestos/{id}/solicitar")
    public String solicitarRepuesto(@PathVariable Integer id, @RequestParam int cantidad) {
        repuestoService.solicitar(id, cantidad);
        return "redirect:/tecnico/repuestos";
    }

    @PostMapping("/repuestos/{id}/devolver")
    public String devolverRepuesto(@PathVariable Integer id, @RequestParam int cantidad) {
        repuestoService.devolver(id, cantidad);
        return "redirect:/tecnico/repuestos";
    }
}
