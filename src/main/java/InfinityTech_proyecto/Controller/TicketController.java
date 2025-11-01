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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/tickets")
    public String listarTickets(Model model) {
        model.addAttribute("titulo", "HU2 - Solicitudes de reparación");
        model.addAttribute("tickets", ticketService.listar());
        return "tickets-lista";
    }

    @GetMapping("/tickets/nuevo")
    public String nuevoTicket(Model model) {
        Ticket ticket = new Ticket();
        ticket.setEstado("Recibido");
        model.addAttribute("ticket", ticket);
        model.addAttribute("titulo", "Crear ticket");
        return "tickets-form";
    }

    @PostMapping("/tickets/guardar")
    public String guardarTicket(Ticket ticket, BindingResult result, Model model,
                                @RequestParam(name = "modoEdicion", required = false) String modoEdicion) {

        if (ticket.getFolio() == null || ticket.getFolio().trim().isEmpty()) {
            model.addAttribute("errorMensaje", "El folio es obligatorio");
            model.addAttribute("ticket", ticket);
            return "tickets-form";
        }

        if (modoEdicion == null && ticketService.folioYaExiste(ticket.getFolio())) {
            model.addAttribute("errorMensaje", "El folio ya existe");
            model.addAttribute("ticket", ticket);
            return "tickets-form";
        }

        ticketService.guardar(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/tickets/editar/{id}")
    public String editarTicket(@PathVariable("id") Long id, Model model) {
        Ticket ticket = ticketService.buscarPorId(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("modoEdicion", "true");
        model.addAttribute("titulo", "Editar ticket");
        return "tickets-form";
    }

    @GetMapping("/tickets/eliminar/{id}")
    public String eliminarTicket(@PathVariable("id") Long id) {
        ticketService.eliminar(id);
        return "redirect:/tickets";
    }
}
