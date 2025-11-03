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

    @GetMapping("/tickets/{id}/cambiar-estado")
    public String viewCambiarEstado(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.buscarPorId(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("titulo", "Cambiar estado");
        return "cambiar_estado";
    }

    @PostMapping("/tickets/{id}/estado")
    public String postCambiarEstado(@PathVariable Long id,
            @RequestParam("nuevo") String nuevo,
            Model model) {
        Ticket t = ticketService.buscarPorId(id);

        if (!transicionValida(t.getEstado(), nuevo)) {
            model.addAttribute("ticket", t);
            model.addAttribute("errorMensaje", "Transición de estado no permitida");
            model.addAttribute("titulo", "Cambiar estado");
            return "cambiar_estado";
        }

        t.setEstado(nuevo);
        ticketService.guardar(t);

        model.addAttribute("okMensaje", "Estado actualizado a " + nuevo);
        return "redirect:/tickets/editar/" + id;
    }

    private boolean transicionValida(String actual, String nuevo) {
        actual = actual == null ? "" : actual;
        switch (actual) {
            case "Recibido":
            case "Diagnóstico":
            case "Presupuesto":
                return nuevo.equals("Reparación");
            case "Reparación":
                return nuevo.equals("Listo");
            case "Listo":
                return false;
            default:
                return false;
        }
    }

    @GetMapping("/tickets/{id}/notificar-estado")
    public String viewNotificarEstado(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.buscarPorId(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("titulo", "Notificar estado");
        return "notificar_estado";
    }

    @PostMapping("/tickets/{id}/notificar")
    public String postNotificarEstado(@PathVariable Long id, Model model) {
        Ticket t = ticketService.buscarPorId(id);

        if (!"Listo".equals(t.getEstado())) {
            model.addAttribute("ticket", t);
            model.addAttribute("errorMensaje", "Solo se notifica automáticamente cuando el estado es 'Listo'.");
            model.addAttribute("titulo", "Notificar estado");
            return "notificar_estado";
        }

        model.addAttribute("okMensaje", "Notificación disparada al cliente.");
        return "redirect:/tickets/editar/" + id;
    }

    @GetMapping("/tickets/{id}/registrar-entrega")
    public String viewRegistrarEntrega(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.buscarPorId(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("titulo", "Registrar entrega");
        return "registrar_entrega";
    }

    @PostMapping("/tickets/{id}/entregar")
    public String postRegistrarEntrega(@PathVariable Long id,
            @RequestParam("numeroFactura") String numeroFactura,
            Model model) {
        Ticket t = ticketService.buscarPorId(id);

        if (!"Listo".equals(t.getEstado())) {
            model.addAttribute("ticket", t);
            model.addAttribute("errorMensaje", "Solo se puede entregar si el estado es 'Listo'.");
            model.addAttribute("titulo", "Registrar entrega");
            return "registrar_entrega";
        }

        t.setEstado("Entregado");
        ticketService.guardar(t);

        return "redirect:/tickets/editar/" + id;
    }

    @GetMapping("/tickets/{id}/gestionar-garantia")
    public String viewGestionarGarantia(@PathVariable Long id, Model model) {
        Ticket ticket = ticketService.buscarPorId(id);
        model.addAttribute("ticket", ticket);

        boolean enGarantia = estaEnGarantia(ticket);
        int diasRestantes = diasRestantesGarantia(ticket);

        model.addAttribute("enGarantia", enGarantia);
        model.addAttribute("diasRestantesGarantia", diasRestantes);
        model.addAttribute("titulo", "Gestionar garantía");
        return "gestionar_garantia";
    }

    @PostMapping("/tickets/{id}/garantia")
    public String postCrearGarantia(@PathVariable Long id,
            @RequestParam(name = "fallaReportada", required = false) String fallaReportada,
            Model model) {
        Ticket original = ticketService.buscarPorId(id);

        if (!estaEnGarantia(original)) {
            model.addAttribute("ticket", original);
            model.addAttribute("enGarantia", false);
            model.addAttribute("errorMensaje", "La garantía ha expirado.");
            model.addAttribute("titulo", "Gestionar garantía");
            return "gestionar_garantia";
        }

        Ticket g = new Ticket();
        g.setFolio(original.getFolio() + "-G");
        g.setEstado("Recibido");
        g.setClienteNombre(original.getClienteNombre());
        g.setTipoEquipo(original.getTipoEquipo());
        g.setMarcaModelo(original.getMarcaModelo());
        g.setFallaReportada((fallaReportada == null || fallaReportada.isBlank())
                ? "Garantía de ticket " + original.getFolio()
                : fallaReportada);

        ticketService.guardar(g);

        return "redirect:/tickets/editar/" + g.getId();
    }

    private boolean estaEnGarantia(Ticket t) {
        return "Entregado".equals(t.getEstado());
    }

    private int diasRestantesGarantia(Ticket t) {
        return "Entregado".equals(t.getEstado()) ? 90 : 0;
    }

}
