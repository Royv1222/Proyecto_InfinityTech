package InfinityTech_proyecto.Domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Repuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String descripcion;
    private int stock;
    private BigDecimal costo;

    @Enumerated(EnumType.STRING)
    private EstadoRepuesto estado;

    public int getStock() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setStock(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setEstado(EstadoRepuesto estadoRepuesto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public enum EstadoRepuesto {
        DISPONIBLE,
        RESERVADO,
        CONSUMIDO
    }

    // Getters y Setters
}

