/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Controller;

import InfinityTech_proyecto.Domain.Cliente;
import InfinityTech_proyecto.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("titulo", "HU1 - Administrar clientes");
        model.addAttribute("clientes", clienteService.listar());
        return "clientes-lista";
    }

    @GetMapping("/clientes/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("titulo", "Nuevo cliente");
        return "clientes-form";
    }

    @PostMapping("/clientes/guardar")
    public String guardarCliente(Cliente cliente, BindingResult result, Model model,
                                 @RequestParam(name = "modoEdicion", required = false) String modoEdicion) {

        if (cliente.getCedula() == null || cliente.getCedula().trim().isEmpty()) {
            model.addAttribute("errorMensaje", "La cédula es obligatoria");
            model.addAttribute("cliente", cliente);
            return "clientes-form";
        }

        if (modoEdicion == null && clienteService.cedulaYaExiste(cliente.getCedula())) {
            model.addAttribute("errorMensaje", "La cédula ya está registrada");
            model.addAttribute("cliente", cliente);
            return "clientes-form";
        }

        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    public String editarCliente(@PathVariable("id") Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        model.addAttribute("cliente", cliente);
        model.addAttribute("modoEdicion", "true");
        model.addAttribute("titulo", "Editar cliente");
        return "clientes-form";
    }

    @GetMapping("/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Long id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }
}
