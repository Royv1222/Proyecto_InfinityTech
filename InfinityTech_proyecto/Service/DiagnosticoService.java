/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Diagnostico;
import java.util.Optional;

public interface DiagnosticoService {
    
    Diagnostico buscarPorId(Long id);

    void guardar(Diagnostico diagnostico);

    // Permite buscar un diagnóstico por el ID del Ticket asociado
    Optional<Diagnostico> buscarPorTicketId(Long idTicket);
}
