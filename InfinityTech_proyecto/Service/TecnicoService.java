/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Tecnico;
import java.util.List;

public interface TecnicoService {

    List<Tecnico> listar();

    Tecnico buscarPorId(Long id);

    void guardar(Tecnico tecnico);

    void eliminar(Long id);

    boolean cedulaYaExiste(String cedula);
    
    // Método clave para la HU3: Balanceo de carga
    List<Tecnico> listarTecnicosDisponibles();
}
