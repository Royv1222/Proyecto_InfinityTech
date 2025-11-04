/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Controller;


import InfinityTech_proyecto.Domain.Presupuesto;
import InfinityTech_proyecto.Service.InventarioService;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    // HU5
    @PostMapping("/repuesto/reservar")
    public String reservar(@RequestParam String codigo, @RequestParam int cantidad) {
        return service.reservarRepuesto(codigo, cantidad);
    }

    @PostMapping("/repuesto/consumir")
    public String consumir(@RequestParam String codigo, @RequestParam boolean ticketAprobado) {
        return service.consumirRepuesto(codigo, ticketAprobado);
    }

    @PostMapping("/repuesto/devolver")
    public String devolver(@RequestParam String codigo, @RequestParam int cantidad) {
        return service.devolverRepuesto(codigo, cantidad);
    }

    // HU6
    @PostMapping("/presupuesto/generar")
    public Presupuesto generar(@RequestParam BigDecimal manoObra, @RequestParam BigDecimal repuestos, @RequestParam BigDecimal impuestos) {
        return service.generarPresupuesto(manoObra, repuestos, impuestos);
    }

    @PostMapping("/presupuesto/decidir")
    public String decidir(@RequestParam Long id, @RequestParam boolean aprobado) {
        return service.aprobarPresupuesto(id, aprobado);
    }
}
