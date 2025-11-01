/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InfinityTech_proyecto.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String folio;
    private String clienteNombre;
    private String tipoEquipo;
    private String marcaModelo;
    private String fallaReportada;
    private String accesoriosRecibidos;
    private String estado; // Recibido, Diagnóstico, Presupuesto, Reparación, Listo, Entregado, Cancelado

    public Ticket() {
    }

    public Ticket(String folio, String clienteNombre, String tipoEquipo, String marcaModelo, String fallaReportada, String accesoriosRecibidos, String estado) {
        this.folio = folio;
        this.clienteNombre = clienteNombre;
        this.tipoEquipo = tipoEquipo;
        this.marcaModelo = marcaModelo;
        this.fallaReportada = fallaReportada;
        this.accesoriosRecibidos = accesoriosRecibidos;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getMarcaModelo() {
        return marcaModelo;
    }

    public void setMarcaModelo(String marcaModelo) {
        this.marcaModelo = marcaModelo;
    }

    public String getFallaReportada() {
        return fallaReportada;
    }

    public void setFallaReportada(String fallaReportada) {
        this.fallaReportada = fallaReportada;
    }

    public String getAccesoriosRecibidos() {
        return accesoriosRecibidos;
    }

    public void setAccesoriosRecibidos(String accesoriosRecibidos) {
        this.accesoriosRecibidos = accesoriosRecibidos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

