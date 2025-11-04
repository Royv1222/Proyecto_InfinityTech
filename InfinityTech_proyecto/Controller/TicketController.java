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
import InfinityTech_proyecto.Domain.Tecnico; 
import InfinityTech_proyecto.Service.TecnicoService; 
import java.util.List; 

@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired // Necesario para la HU3
    private TecnicoService tecnicoService;
    
    @GetMapping("/tickets")
    public String listarTickets(Model model) {
        model.addAttribute("titulo", "HU2 - Solicitudes de reparación");
        model.addAttribute("tickets", ticketService.listar());
        return "tickets-lista";
    }

    @GetMapping("/tickets/asignar/{id}")
    public String mostrarFormularioAsignacion(@PathVariable("id") Long id, Model model) {
        Ticket ticket = ticketService.buscarPorId(id);
        
        
        List<Tecnico> tecnicosDisponibles = tecnicoService.listarTecnicosDisponibles(); 
        
        model.addAttribute("ticket", ticket);
        model.addAttribute("tecnicos", tecnicosDisponibles);
        model.addAttribute("titulo", "HU3 - Asignar Técnico");
        return "tickets/asignar-form"; 
    }
    
    @PostMapping("/tickets/guardarAsignacion")
    public String guardarAsignacion(@RequestParam("idTicket") Long idTicket, 
                                    @RequestParam("idTecnico") Long idTecnico) {

        Ticket ticket = ticketService.buscarPorId(idTicket);
        Tecnico tecnicoNuevo = tecnicoService.buscarPorId(idTecnico);

        
        if (ticket.getTecnicoAsignado() != null && !ticket.getTecnicoAsignado().getId().equals(idTecnico)) {
             System.out.println("REASIGNACIÓN: Ticket " + idTicket + " cambió de técnico.");
        }
               
        ticket.setTecnicoAsignado(tecnicoNuevo); 
        
        if ("Recibido".equals(ticket.getEstado())) {
            ticket.setEstado("Asignado"); 
        }
        
        ticketService.guardar(ticket);

        return "redirect:/tickets";
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
