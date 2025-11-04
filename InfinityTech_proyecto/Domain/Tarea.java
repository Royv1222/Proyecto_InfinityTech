/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Tarea implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne con Diagnostico (muchas tareas para un diagnóstico)
    @ManyToOne
    @JoinColumn(name = "id_diagnostico")
    private Diagnostico diagnostico; // Vincula a qué diagnóstico pertenece
    
    private String descripcion;
    private Double horasEstimadas; // Para el cálculo del presupuesto
    private boolean completada = false;

    public Tarea() {
    }

    // Constructor, Getters y Setters (Debes añadir todos los métodos requeridos)
    public Long getId() {
        return id;
    }
    // ... (rest of getters and setters)
}