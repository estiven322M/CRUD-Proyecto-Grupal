package com.qmd.crudreserva;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Representa el ViewModel (Modelo de Vista) de la plantilla CRUD.
 * Contiene TODA la lógica de negocio. La Vista (FormularioCrudReserva)
 * es "tonta" y solo llama a estos métodos.
 */
public class GestionReservasViewModel {

    // Simulación de Base de Datos (Modelo/Domain)
    private final List<Reserva> baseDeDatosReservas;
    private int proximoId = 1;

    public GestionReservasViewModel() {
        this.baseDeDatosReservas = new ArrayList<>();
        // Datos de ejemplo
        crearReserva("RES-001", "2025-10-01", "Confirmada", "1094001");
        crearReserva("RES-002", "2025-10-02", "Pendiente", "1094002");
    }

    /**
     * Equivale a 'updateViewModel' o 'cargarDatos'.
     * Devuelve todas las reservas.
     */
    public List<Reserva> obtenerTodasLasReservas() {
        return new ArrayList<>(baseDeDatosReservas);
    }

    /**
     * Lógica para CREAR una nueva reserva.
     */
    public void crearReserva(String codigo, String fecha, String estado, String idCliente) {
        if (codigo.isEmpty() || fecha.isEmpty() || estado.isEmpty() || idCliente.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }
        Reserva nuevaReserva = new Reserva(proximoId++, codigo, fecha, estado, idCliente);
        baseDeDatosReservas.add(nuevaReserva);
    }

    /**
     * Lógica para ACTUALIZAR una reserva existente.
     */
    public void actualizarReserva(int id, String codigo, String fecha, String estado, String idCliente) {
        Optional<Reserva> reservaOpt = buscarReservaPorId(id);
        if (reservaOpt.isPresent()) {
            Reserva r = reservaOpt.get();
            r.setCodigoReserva(codigo);
            r.setFecha(fecha);
            r.setEstado(estado);
            r.setIdCliente(idCliente);
        } else {
            throw new IllegalArgumentException("No se encontró la reserva con ID " + id);
        }
    }

    /**
     * Lógica para ELIMINAR una reserva.
     */
    public void eliminarReserva(int id) {
        baseDeDatosReservas.removeIf(r -> r.getId() == id);
    }
    
    /**
     * Lógica para BUSCAR.
     * Este método actualiza la tabla (DefaultTableModel) directamente.
     */
    public void buscarReservaPorCodigo(String codigo, DefaultTableModel tableModel) {
        // 1. Limpiar tabla
        tableModel.setRowCount(0);
        
        // 2. Llenar con resultados
        for (Reserva r : baseDeDatosReservas) {
            if (codigo.isEmpty() || r.getCodigoReserva().contains(codigo)) {
                tableModel.addRow(new Object[]{
                    r.getId(),
                    r.getCodigoReserva(),
                    r.getFecha(),
                    r.getEstado(),
                    r.getIdCliente()
                });
            }
        }
    }
    
    // --- Métodos de ayuda ---
    private Optional<Reserva> buscarReservaPorId(int id) {
        return baseDeDatosReservas.stream().filter(r -> r.getId() == id).findFirst();
    }
}
