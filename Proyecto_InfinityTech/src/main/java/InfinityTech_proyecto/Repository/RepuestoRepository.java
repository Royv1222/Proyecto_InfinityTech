/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.repository;

import InfinityTech_proyecto.domain.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepuestoRepository extends JpaRepository<Repuesto, Integer> {
    Optional<Repuesto> findByNombre(String nombre);
}
