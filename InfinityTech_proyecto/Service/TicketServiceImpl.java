/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Ticket;
import InfinityTech_proyecto.Repository.TicketRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> listar() {
        return (List<Ticket>) ticketRepository.findAll();
    }

    @Override
    public Ticket buscarPorId(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public void guardar(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Override
    public void eliminar(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public boolean folioYaExiste(String folio) {
        return ticketRepository.existsByFolio(folio);
    }
}
