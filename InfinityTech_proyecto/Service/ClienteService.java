/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Cliente;
import java.util.List;

public interface ClienteService {

    List<Cliente> listar();

    Cliente buscarPorId(Long id);

    void guardar(Cliente cliente);

    void eliminar(Long id);

    boolean cedulaYaExiste(String cedula);
}
