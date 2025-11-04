/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Repository;

import InfinityTech_proyecto.Domain.Factura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jose daniel
 * Historias de usuario HU9 y HU10
 */

@Repository
public interface FacturaRepository extends CrudRepository<Factura, Long> {

    Factura findByTicketId(Long idTicket);
}
