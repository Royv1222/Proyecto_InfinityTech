package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Repuesto;
import InfinityTech_proyecto.Domain.Presupuesto;
import InfinityTech_proyecto.Repository.RepuestoRepository;
import InfinityTech_proyecto.Repository.PresupuestoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class InventarioService {

    private final RepuestoRepository repuestoRepo;
    private final PresupuestoRepository presupuestoRepo;

    public InventarioService(RepuestoRepository repuestoRepo, PresupuestoRepository presupuestoRepo) {
        this.repuestoRepo = repuestoRepo;
        this.presupuestoRepo = presupuestoRepo;
    }

    // HU5 – Reservar repuesto
    public String reservarRepuesto(String codigo, int cantidad) {
        Optional<Repuesto> repuestoOpt = repuestoRepo.findByCodigo(codigo);
        if (repuestoOpt.isEmpty()) return "Repuesto no encontrado";

        Repuesto repuesto = repuestoOpt.get();
        if (repuesto.getStock() >= cantidad) {
            repuesto.setStock(repuesto.getStock() - cantidad);
            repuesto.setEstado(Repuesto.EstadoRepuesto.RESERVADO);
            repuestoRepo.save(repuesto);
            return "Repuesto reservado";
        } else {
            return "Repuesto no disponible en inventario";
        }
    }

    // HU5 – Consumir repuesto
    public String consumirRepuesto(String codigo, boolean ticketAprobado) {
        Optional<Repuesto> repuestoOpt = repuestoRepo.findByCodigo(codigo);
        if (repuestoOpt.isEmpty()) return "Repuesto no encontrado";

        Repuesto repuesto = repuestoOpt.get();
        if (!ticketAprobado) return "No se puede consumir antes de la aprobación";

        repuesto.setEstado(Repuesto.EstadoRepuesto.CONSUMIDO);
        repuestoRepo.save(repuesto);
        return "Repuesto consumido";
    }

    // HU5 – Devolver repuesto
    public String devolverRepuesto(String codigo, int cantidad) {
        Optional<Repuesto> repuestoOpt = repuestoRepo.findByCodigo(codigo);
        if (repuestoOpt.isEmpty()) return "Repuesto no encontrado";

        Repuesto repuesto = repuestoOpt.get();
        repuesto.setStock(repuesto.getStock() + cantidad);
        repuesto.setEstado(Repuesto.EstadoRepuesto.DISPONIBLE);
        repuestoRepo.save(repuesto);
        return "Repuesto devuelto al inventario";
    }

    // HU6 – Generar presupuesto
    public Presupuesto generarPresupuesto(BigDecimal manoObra, BigDecimal repuestos, BigDecimal impuestos) {
        Presupuesto p = new Presupuesto();
        p.setManoObra(manoObra);
        p.setRepuestos(repuestos);
        p.setImpuestos(impuestos);
        p.setTotal(manoObra.add(repuestos).add(impuestos));
        p.setEstado(Presupuesto.EstadoPresupuesto.PENDIENTE);
        p.setFechaCreacion(java.time.LocalDateTime.now());
        return presupuestoRepo.save(p);
    }

    // HU6 – Aprobar/Rechazar presupuesto
    public String aprobarPresupuesto(Long id, boolean aprobado) {
        Optional<Presupuesto> pOpt = presupuestoRepo.findById(id);
        if (pOpt.isEmpty()) return "Presupuesto no encontrado";

        Presupuesto p = pOpt.get();
        p.setEstado(aprobado ? Presupuesto.EstadoPresupuesto.APROBADO : Presupuesto.EstadoPresupuesto.RECHAZADO);
        presupuestoRepo.save(p);
        return aprobado ? "Presupuesto aprobado" : "Presupuesto rechazado";
    }
}
