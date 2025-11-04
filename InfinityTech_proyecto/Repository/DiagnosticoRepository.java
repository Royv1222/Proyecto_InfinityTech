/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package InfinityTech_proyecto.Repository;

import InfinityTech_proyecto.Domain.Diagnostico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DiagnosticoRepository extends CrudRepository<Diagnostico, Long> {
    
    // Método para buscar si ya hay un diagnóstico para un ticket específico
    Optional<Diagnostico> findByTicketId(Long idTicket);
}