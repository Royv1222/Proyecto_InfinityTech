/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Service;

import InfinityTech_proyecto.Domain.Factura;
import java.util.List;

/**
 *
 * @author jose daniel
 * Historias de usuario HU9 y HU10
 */

public interface FacturaService {
    List<Factura> listar();
    Factura buscarPorId(Long id);
    Factura buscarPorTicket(Long idTicket);
    void guardar(Factura factura);
    void eliminar(Long id);
}
