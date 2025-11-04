/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Controller;

import InfinityTech_proyecto.Domain.*;
import InfinityTech_proyecto.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author jose daniel
 * Historias de usuario HU9 y HU10
 */

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private PagoService pagoService;

    @GetMapping("/generar/{idTicket}")
    public String generarFactura(@PathVariable Long idTicket, Model model) {
        Ticket ticket = ticketService.buscarPorId(idTicket);

        if (ticket == null) {
            model.addAttribute("errorMensaje", "Ticket no encontrado");
            return "facturacion/factura-form";
        }

        Cliente cliente = clienteService.listar().stream()
                .filter(c -> c.getNombre().equals(ticket.getClienteNombre()))
                .findFirst().orElse(null);

        if (cliente == null || cliente.getCedula() == null) {
            model.addAttribute("errorMensaje", "Complete la información de facturación del cliente.");
            return "facturacion/factura-form";
        }

        Factura factura = new Factura();
        factura.setTicket(ticket);
        factura.setCliente(cliente);
        factura.setSubtotal(10000.0);
        factura.setImpuestos(factura.getSubtotal() * 0.13);
        factura.setTotal(factura.getSubtotal() + factura.getImpuestos());
        facturaService.guardar(factura);

        model.addAttribute("factura", factura);
        model.addAttribute("titulo", "HU9 - Facturación y Pago");
        return "facturacion/factura-form";
    }

    @PostMapping("/registrarPago")
    public String registrarPago(@RequestParam Long idFactura,
                                @RequestParam String metodoPago,
                                Model model) {
        Factura factura = facturaService.buscarPorId(idFactura);
        if (factura == null) {
            model.addAttribute("errorMensaje", "Factura no encontrada");
            return "facturacion/factura-form";
        }

        Pago pago = new Pago();
        pago.setFactura(factura);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(factura.getTotal());
        pago.setEstado("Aprobado");

        pagoService.guardar(pago);

        factura.setPagada(true);
        facturaService.guardar(factura);

        Ticket ticket = factura.getTicket();
        ticket.setEstado("Entregado");
        ticketService.guardar(ticket);

        model.addAttribute("mensajeExito", "Pago registrado correctamente");
        model.addAttribute("factura", factura);
        return "facturacion/factura-detalle";
    }
}
