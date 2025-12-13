/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.controller;

import InfinityTech_proyecto.domain.Ticket;
import InfinityTech_proyecto.domain.TicketForm;
import InfinityTech_proyecto.service.TicketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final TicketService ticketService;

    public ClienteController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public String homeCliente() {
        return "cliente/index";
    }

    @GetMapping("/solicitar")
    public String formSolicitud(Model model) {
        model.addAttribute("ticketForm", new TicketForm());
        return "cliente/solicitar";
    }

    @PostMapping("/solicitar")
    public String crearSolicitud(@ModelAttribute TicketForm form, HttpSession session, Model model) {
        String clienteNombre = (String) session.getAttribute("usuario");
        Ticket t = ticketService.crearSolicitud(clienteNombre, form.getEquipo(), form.getProblema());
        model.addAttribute("ticket", t);
        return "cliente/creado";
    }

    @GetMapping("/consultar")
    public String consultar(@RequestParam(name = "folio", required = false) String folio, Model model) {
        if (folio == null || folio.isBlank()) {
            return "cliente/consultar";
        }
        try {
            Ticket t = ticketService.buscarPorFolio(folio.trim());
            model.addAttribute("ticket", t);
            return "cliente/resultado";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "cliente/consultar";
        }
    }
}
