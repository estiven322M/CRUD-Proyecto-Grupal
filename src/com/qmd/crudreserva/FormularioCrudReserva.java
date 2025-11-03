package com.qmd.crudreserva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Representa la Vista (ViewPart) de la plantilla CRUD.
 * Esta clase solo maneja los componentes visuales y delega toda la lógica
 * al ViewModel.
 */
public class FormularioCrudReserva extends JFrame {

    // Componentes de la UI (Interfaz)
    private JTextField txtId;
    private JTextField txtCodigo;
    private JTextField txtFecha;
    private JTextField txtEstado;
    private JTextField txtIdCliente;
    private JTextField txtBuscar;

    private JButton btnNuevo;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnBuscar;

    private JTable tablaReservas;
    private DefaultTableModel tableModel;

    // Referencia al ViewModel
    private final GestionReservasViewModel viewModel;

    public FormularioCrudReserva() {
        // --- 1. Crear instancia del ViewModel ---
        // El ViewModel se encarga de toda la lógica.
        this.viewModel = new GestionReservasViewModel();

        // --- 2. Configuración de la Ventana (JFrame) ---
        setTitle("Gestión de Reservas (CRUD) - Jhon Stivenson Méndez");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 3. Panel de Formulario (Izquierda) ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la Reserva"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; txtId = new JTextField(15); txtId.setEditable(false); panelFormulario.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(new JLabel("Código Reserva:"), gbc);
        gbc.gridx = 1; txtCodigo = new JTextField(15); panelFormulario.add(txtCodigo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; txtFecha = new JTextField(15); panelFormulario.add(txtFecha, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; txtEstado = new JTextField(15); panelFormulario.add(txtEstado, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; panelFormulario.add(new JLabel("ID Cliente:"), gbc);
        gbc.gridx = 1; txtIdCliente = new JTextField(15); panelFormulario.add(txtIdCliente, gbc);

        // --- 4. Panel de Botones (Formulario) ---
        JPanel panelBotonesForm = new JPanel();
        btnNuevo = new JButton("Nuevo");
        btnGuardar = new JButton("Guardar (Crear/Actualizar)");
        btnEliminar = new JButton("Eliminar");
        panelBotonesForm.add(btnNuevo);
        panelBotonesForm.add(btnGuardar);
        panelBotonesForm.add(btnEliminar);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(panelBotonesForm, gbc);

        add(panelFormulario, BorderLayout.WEST);

        // --- 5. Panel de Tabla (Derecha) ---
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Reservas Existentes"));

        // Panel de Búsqueda
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBuscar.add(new JLabel("Buscar por Código:"));
        txtBuscar = new JTextField(15);
        btnBuscar = new JButton("Buscar");
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelTabla.add(panelBuscar, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID", "Código Reserva", "Fecha", "Estado", "ID Cliente"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hace que la tabla no sea editable
            }
        };
        tablaReservas = new JTable(tableModel);
        panelTabla.add(new JScrollPane(tablaReservas), BorderLayout.CENTER);

        add(panelTabla, BorderLayout.CENTER);

        // --- 6. Conectar la Vista al ViewModel (EVENTOS) ---
        
        // Esto es lo que el profesor llama "btnNuevo_Selection_LogicalEvent"
        btnNuevo.addActionListener(e -> limpiarCampos());

        // Esto es lo que llama "btnActualizar_Selection_LogicalEvent"
        btnGuardar.addActionListener(e -> guardarReserva());

        // Evento para el botón Eliminar
        btnEliminar.addActionListener(e -> eliminarReserva());
        
        // Evento para el botón Buscar
        btnBuscar.addActionListener(e -> buscarReserva());

        // Evento para seleccionar de la tabla
        tablaReservas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarReservaDeTabla();
            }
        });

        // --- 7. Cargar datos "updateViewModel" ---
        // Equivalente de TM-BUID de "updateViewModel()" en el constructor.
        actualizarVista();
    }

    /**
     * Este método es el equivalente a "updateViewModel" de TM-BUID.
     * Refresca todos los datos de la vista.
     */
    private void actualizarVista() {
        // 1. Limpiar la tabla
        tableModel.setRowCount(0);

        // 2. Pedir los datos al ViewModel
        for (Reserva r : viewModel.obtenerTodasLasReservas()) {
            tableModel.addRow(new Object[]{
                r.getId(),
                r.getCodigoReserva(),
                r.getFecha(),
                r.getEstado(),
                r.getIdCliente()
            });
        }
    }

    /**
     * Llama al ViewModel para limpiar los campos del formulario.
     */
    private void limpiarCampos() {
        txtId.setText("");
        txtCodigo.setText("");
        txtFecha.setText("");
        txtEstado.setText("");
        txtIdCliente.setText("");
        txtCodigo.requestFocus();
    }

    /**
     * Recoge los datos de FormularioCrudReserva y los envía al ViewModel para guardar.
     */
    private void guardarReserva() {
        try {
            // 1. Recoger datos 
            String idStr = txtId.getText();
            String codigo = txtCodigo.getText();
            String fecha = txtFecha.getText();
            String estado = txtEstado.getText();
            String idCliente = txtIdCliente.getText();
            
            // 2. Lógica de crear y actualizar
            if (idStr.isEmpty()) {
                // CREAR (no hay ID)
                viewModel.crearReserva(codigo, fecha, estado, idCliente);
                JOptionPane.showMessageDialog(this, "Reserva Creada Exitosamente");
            } else {
                // ACTUALIZAR (hay ID)
                int id = Integer.parseInt(idStr);
                viewModel.actualizarReserva(id, codigo, fecha, estado, idCliente);
                JOptionPane.showMessageDialog(this, "Reserva Actualizada Exitosamente");
            }

            // 3. Refrescar la vista
            actualizarVista();
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Pide al ViewModel que elimine la reserva seleccionada.
     */
    private void eliminarReserva() {
        try {
            int id = Integer.parseInt(txtId.getText());
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "¿Está seguro de que desea eliminar la reserva con ID " + id + "?", 
                    "Confirmar Eliminación", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                viewModel.eliminarReserva(id);
                actualizarVista();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "Reserva Eliminada");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Pide al ViewModel que busque y actualice la tabla.
     */
    private void buscarReserva() {
        String codigoBusqueda = txtBuscar.getText();
        viewModel.buscarReservaPorCodigo(codigoBusqueda, tableModel);
        // El ViewModel actualiza la tabla directamente en este caso.
    }

    /**
     * Rellena el formulario cuando se selecciona una fila de la tabla.
     */
    private void seleccionarReservaDeTabla() {
        int filaSeleccionada = tablaReservas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            txtId.setText(tableModel.getValueAt(filaSeleccionada, 0).toString());
            txtCodigo.setText(tableModel.getValueAt(filaSeleccionada, 1).toString());
            txtFecha.setText(tableModel.getValueAt(filaSeleccionada, 2).toString());
            txtEstado.setText(tableModel.getValueAt(filaSeleccionada, 3).toString());
            txtIdCliente.setText(tableModel.getValueAt(filaSeleccionada, 4).toString());
        }
    }

    // --- Main para ejecutar ---
    public static void main(String[] args) {
        // Asegura que la UI se cree en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            new FormularioCrudReserva().setVisible(true);
        });
    }
}
