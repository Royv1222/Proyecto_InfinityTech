package InfinityTech_proyecto.Repository;

import InfinityTech_proyecto.Domain.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    Optional<Presupuesto> findByCodigo(String codigo);
}
