/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "repuestos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "nombre")
})
public class Repuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false)
    private int stock;

    @Version
    private Long version;

    public Repuesto() {}

    public Repuesto(String nombre, int stock) {
        this.nombre = nombre;
        this.stock = stock;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
