/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Ticket;
import java.util.List;

public interface TicketService {

    List<Ticket> listar();

    Ticket buscarPorId(Long id);

    void guardar(Ticket ticket);

    void eliminar(Long id);

    boolean folioYaExiste(String folio);
}
