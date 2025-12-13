/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.service;

import InfinityTech_proyecto.domain.Repuesto;
import InfinityTech_proyecto.repository.RepuestoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RepuestoService {

    private final RepuestoRepository repo;

    public RepuestoService(RepuestoRepository repo) {
        this.repo = repo;
    }

    public List<Repuesto> listar() {
        return repo.findAll();
    }

    @Transactional
    public Repuesto crearSiNoExiste(String nombre, int stockInicial) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre de repuesto requerido.");
        if (stockInicial < 0) throw new IllegalArgumentException("Stock inicial inválido.");

        return repo.findByNombre(nombre.trim())
                .orElseGet(() -> repo.save(new Repuesto(nombre.trim(), stockInicial)));
    }

    @Transactional
    public Repuesto solicitar(Integer idRepuesto, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad a solicitar debe ser > 0.");

        Repuesto r = repo.findById(idRepuesto)
                .orElseThrow(() -> new IllegalArgumentException("Repuesto no existe: " + idRepuesto));

        if (r.getStock() < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + r.getStock());
        }

        r.setStock(r.getStock() - cantidad);
        return repo.save(r);
    }

    @Transactional
    public Repuesto devolver(Integer idRepuesto, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad a devolver debe ser > 0.");

        Repuesto r = repo.findById(idRepuesto)
                .orElseThrow(() -> new IllegalArgumentException("Repuesto no existe: " + idRepuesto));

        r.setStock(r.getStock() + cantidad);
        return repo.save(r);
    }
}
