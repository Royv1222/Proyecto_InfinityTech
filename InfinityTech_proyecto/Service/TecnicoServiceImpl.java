/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Tecnico;
import InfinityTech_proyecto.Repository.TecnicoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TecnicoServiceImpl implements TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    @Override
    public List<Tecnico> listar() {
        return (List<Tecnico>) tecnicoRepository.findAll();
    }

    @Override
    public Tecnico buscarPorId(Long id) {
        return tecnicoRepository.findById(id).orElse(null);
    }

    @Override
    public void guardar(Tecnico tecnico) {
        tecnicoRepository.save(tecnico);
    }

    @Override
    public void eliminar(Long id) {
        tecnicoRepository.deleteById(id);
    }

    @Override
    public boolean cedulaYaExiste(String cedula) {
        return tecnicoRepository.existsByCedula(cedula);
    }
    
    // Implementación del método clave para la HU3
    @Override
    public List<Tecnico> listarTecnicosDisponibles() {
        // En este ejemplo, "disponibles" son los que están activos y marcados como 'disponible=true'.
        // Aquí podrías añadir lógica más compleja (ej. cargaTrabajo < 5 tickets).
        return tecnicoRepository.findByActivoIsTrueAndDisponibleIsTrue();
    }
}