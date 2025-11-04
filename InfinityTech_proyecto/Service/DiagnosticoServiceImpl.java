/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Diagnostico;
import InfinityTech_proyecto.Repository.DiagnosticoRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiagnosticoServiceImpl implements DiagnosticoService {

    @Autowired
    private DiagnosticoRepository diagnosticoRepository;

    @Override
    public Diagnostico buscarPorId(Long id) {
        return diagnosticoRepository.findById(id).orElse(null);
    }

    @Override
    public void guardar(Diagnostico diagnostico) {
        diagnosticoRepository.save(diagnostico);
    }
    
    @Override
    public Optional<Diagnostico> buscarPorTicketId(Long idTicket) {
        return diagnosticoRepository.findByTicketId(idTicket);
    }
}
