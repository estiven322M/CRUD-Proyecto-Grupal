package com.qmd.crudreserva;

/**
 * Representa la entidad del Dominio: Reserva.
 * (Basado en el Diagrama de Dominio)
 */
public class Reserva {
    private int id;
    private String codigoReserva;
    private String fecha;
    private String estado;
    private String idCliente;

    public Reserva(int id, String codigoReserva, String fecha, String estado, String idCliente) {
        this.id = id;
        this.codigoReserva = codigoReserva;
        this.fecha = fecha;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigoReserva() { return codigoReserva; }
    public void setCodigoReserva(String codigoReserva) { this.codigoReserva = codigoReserva; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }

    @Override
    public String toString() {
        return "Reserva{" + "id=" + id + ", codigoReserva='" + codigoReserva + '\'' + '}';
    }
}