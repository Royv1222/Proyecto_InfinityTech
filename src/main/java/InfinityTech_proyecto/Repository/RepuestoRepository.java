package InfinityTech_proyecto.Repository;

import InfinityTech_proyecto.Domain.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RepuestoRepository extends JpaRepository<Repuesto, Long> {
    Optional<Repuesto> findByCodigo(String codigo);
}

