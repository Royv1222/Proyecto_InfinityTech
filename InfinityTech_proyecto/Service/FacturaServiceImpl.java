/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Service;


import InfinityTech_proyecto.Domain.Factura;
import InfinityTech_proyecto.Repository.FacturaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jose daniel
 * Historias de usuario HU9 y HU10
 */

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public List<Factura> listar() {
        return (List<Factura>) facturaRepository.findAll();
    }

    @Override
    public Factura buscarPorId(Long id) {
        return facturaRepository.findById(id).orElse(null);
    }

    @Override
    public Factura buscarPorTicket(Long idTicket) {
        return facturaRepository.findByTicketId(idTicket);
    }

    @Override
    public void guardar(Factura factura) {
        facturaRepository.save(factura);
    }

    @Override
    public void eliminar(Long id) {
        facturaRepository.deleteById(id);
    }
}
