/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Pago;
import java.util.List;

/**
 *
 * @author jose daniel
 * Historias de usuario HU9 y HU10
 */

public interface PagoService {
    List<Pago> listar();
    Pago buscarPorId(Long id);
    void guardar(Pago pago);
    void eliminar(Long id);
}
