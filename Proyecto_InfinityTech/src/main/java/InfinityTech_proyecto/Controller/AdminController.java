/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.controller;

import InfinityTech_proyecto.domain.EstadoTicket;
import InfinityTech_proyecto.service.RepuestoService;
import InfinityTech_proyecto.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TicketService ticketService;
    private final RepuestoService repuestoService;

    public AdminController(TicketService ticketService, RepuestoService repuestoService) {
        this.ticketService = ticketService;
        this.repuestoService = repuestoService;
    }

    @GetMapping("/tickets")
    public String listar(Model model) {
        model.addAttribute("tickets", ticketService.listarTodos());
        return "admin/lista";
    }

    @GetMapping("/tickets/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        model.addAttribute("ticket", ticketService.buscarPorId(id));
        model.addAttribute("estados", EstadoTicket.values());
        return "admin/detalle";
    }

    @PostMapping("/tickets/{id}/asignar")
    public String asignar(@PathVariable Integer id, @RequestParam String tecnico) {
        ticketService.asignarTecnico(id, tecnico);
        return "redirect:/admin/tickets/" + id;
    }

    @PostMapping("/tickets/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, @RequestParam EstadoTicket estado) {
        ticketService.actualizarDiagnostico(id, null, estado, null);
        return "redirect:/admin/tickets/" + id;
    }

    @PostMapping("/tickets/{id}/eliminar")
    public String eliminar(@PathVariable Integer id) {
        ticketService.eliminar(id);
        return "redirect:/admin/tickets";
    }

    // ✅ Admin también puede ver/gestionar repuestos con pocas rutas
    @GetMapping("/repuestos")
    public String repuestos(Model model) {
        model.addAttribute("repuestos", repuestoService.listar());
        return "admin/repuestos";
    }

    @PostMapping("/repuestos/crear")
    public String crearRepuesto(@RequestParam String nombre, @RequestParam int stock) {
        repuestoService.crearSiNoExiste(nombre, stock);
        return "redirect:/admin/repuestos";
    }

    @PostMapping("/repuestos/{id}/sumar")
    public String sumarStock(@PathVariable Integer id, @RequestParam int cantidad) {
        repuestoService.devolver(id, cantidad);
        return "redirect:/admin/repuestos";
    }
}
