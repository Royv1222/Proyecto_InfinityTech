/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package InfinityTech_proyecto.Repository;

import InfinityTech_proyecto.Domain.Tecnico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TecnicoRepository extends CrudRepository<Tecnico, Long> {

    // Método para validar que la cédula del técnico no se repita.
    boolean existsByCedula(String cedula);
    
    // Método clave para la HU3: Encontrar técnicos que estén activos y disponibles.
    List<Tecnico> findByActivoIsTrueAndDisponibleIsTrue();
}
