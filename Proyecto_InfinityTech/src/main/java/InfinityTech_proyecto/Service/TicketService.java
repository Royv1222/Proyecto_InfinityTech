/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.service;

import InfinityTech_proyecto.domain.EstadoTicket;
import InfinityTech_proyecto.domain.Ticket;
import InfinityTech_proyecto.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repo;
    private final SecureRandom random = new SecureRandom();

    public TicketService(TicketRepository repo) {
        this.repo = repo;
    }

    public List<Ticket> listarTodos() {
        return repo.findAll();
    }

    public Ticket buscarPorId(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ticket no encontrado: " + id));
    }

    public Ticket buscarPorFolio(String folio) {
        validarFolio3(folio);
        return repo.findByFolio(folio)
                .orElseThrow(() -> new IllegalArgumentException("No existe ticket con folio: " + folio));
    }

    @Transactional
    public Ticket crearSolicitud(String clienteNombre, String equipo, String problema) {
        if (clienteNombre == null || clienteNombre.isBlank()) throw new IllegalArgumentException("Cliente requerido.");
        if (equipo == null || equipo.isBlank()) throw new IllegalArgumentException("Equipo requerido.");
        if (problema == null || problema.isBlank()) throw new IllegalArgumentException("Problema requerido.");

        Ticket t = new Ticket();
        t.setClienteNombre(clienteNombre.trim());
        t.setEquipo(equipo.trim());
        t.setProblema(problema.trim());
        t.setEstado(EstadoTicket.SOLICITADO);

        t.setFolio(generarFolio3Unico());
        return repo.save(t);
    }

    @Transactional
    public Ticket asignarTecnico(Integer idTicket, String tecnicoNombre) {
        if (tecnicoNombre == null || tecnicoNombre.isBlank()) {
            throw new IllegalArgumentException("Nombre del técnico requerido.");
        }
        Ticket t = buscarPorId(idTicket);
        t.setTecnicoAsignado(tecnicoNombre.trim());
        if (t.getEstado() == EstadoTicket.SOLICITADO) {
            t.setEstado(EstadoTicket.EN_REVISION);
        }
        return repo.save(t);
    }

    // ✅ Diagnóstico + estado opcional + presupuesto opcional
    @Transactional
    public Ticket actualizarDiagnostico(Integer idTicket,
                                        String diagnostico,
                                        EstadoTicket nuevoEstadoOpcional,
                                        BigDecimal presupuestoOpcional) {

        Ticket t = buscarPorId(idTicket);

        if (diagnostico != null && !diagnostico.isBlank()) {
            t.setDiagnostico(diagnostico.trim());
            if (t.getEstado() == EstadoTicket.EN_REVISION || t.getEstado() == EstadoTicket.SOLICITADO) {
                t.setEstado(EstadoTicket.DIAGNOSTICADO);
            }
        }

        if (presupuestoOpcional != null) {
            if (presupuestoOpcional.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El presupuesto no puede ser negativo.");
            }
            t.setPresupuesto(presupuestoOpcional);
        }

        if (nuevoEstadoOpcional != null) {
            t.setEstado(nuevoEstadoOpcional);
        }

        return repo.save(t);
    }

    @Transactional
    public void eliminar(Integer idTicket) {
        repo.deleteById(idTicket);
    }

    // ===== Helpers =====

    private void validarFolio3(String folio) {
        if (folio == null || !folio.matches("\\d{3}")) {
            throw new IllegalArgumentException("El folio debe ser exactamente de 3 dígitos (000-999).");
        }
    }

    private String generarFolio3Unico() {
        for (int i = 0; i < 1200; i++) {
            int n = random.nextInt(1000);
            String folio = String.format("%03d", n);
            if (!repo.existsByFolio(folio)) return folio;
        }
        throw new IllegalStateException("No hay folios disponibles (000-999).");
    }
}
