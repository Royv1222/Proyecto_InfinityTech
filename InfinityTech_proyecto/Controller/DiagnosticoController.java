/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Controller;

import InfinityTech_proyecto.Domain.Diagnostico;
import InfinityTech_proyecto.Domain.Ticket;
import InfinityTech_proyecto.Service.DiagnosticoService;
import InfinityTech_proyecto.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/diagnosticos")
public class DiagnosticoController {

    @Autowired
    private DiagnosticoService diagnosticoService;
    
    @Autowired
    private TicketService ticketService; 

    @GetMapping("/registrar/{idTicket}")
    public String mostrarFormularioDiagnostico(@PathVariable("idTicket") Long idTicket, Model model) {
        Ticket ticket = ticketService.buscarPorId(idTicket);
        
        Diagnostico diagnostico = diagnosticoService.buscarPorTicketId(idTicket)
                                    .orElseGet(() -> {
                                        Diagnostico d = new Diagnostico();
                                        d.setTicket(ticket);
                                        return d;
                                    });
        
        model.addAttribute("ticket", ticket);
        model.addAttribute("diagnostico", diagnostico);
        model.addAttribute("titulo", "HU4 - Registrar Diagnóstico");
        return "diagnosticos/diagnostico-form"; 
    }

    @PostMapping("/guardar")
    public String guardarDiagnostico(@ModelAttribute Diagnostico diagnostico, Model model) {
        
        if (diagnostico.getHallazgos() == null || diagnostico.getHallazgos().trim().isEmpty()) {
            model.addAttribute("errorMensaje", "El campo de Hallazgos es obligatorio.");
            model.addAttribute("diagnostico", diagnostico);
            model.addAttribute("ticket", diagnostico.getTicket());
            return "diagnosticos/diagnostico-form";
        }
        
        diagnosticoService.guardar(diagnostico);
        
        Ticket ticket = diagnostico.getTicket();
        if (!"Diagnóstico".equals(ticket.getEstado())) { 
            ticket.setEstado("Diagnóstico");
            ticketService.guardar(ticket);
        }
        
        return "redirect:/diagnosticos/detalle/" + diagnostico.getId(); 
    }
    
    @GetMapping("/detalle/{idDiagnostico}")
    public String verDetalleDiagnostico(@PathVariable("idDiagnostico") Long idDiagnostico, Model model) {
        Diagnostico diagnostico = diagnosticoService.buscarPorId(idDiagnostico);

        model.addAttribute("diagnostico", diagnostico);
        model.addAttribute("titulo", "HU4 - Recomendación y Presupuesto");
        return "diagnosticos/detalle-recomendacion";
    }
}