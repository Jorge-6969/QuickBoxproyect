package cargarPanel.vistas;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import recursos.CarritoTemporal;
import recursos.Conectadb;
import recursos.Validar;
import recursos.VentaService;

public class VentaController implements Initializable {

    // Inputs
    @FXML private TextField tfCodigo;
    @FXML private TextField tfNombre;
    @FXML private TextField tfPrecio;
    @FXML private TextField tfCantidad;
    @FXML
    private DatePicker fecha;
    @FXML
    private Label usuarioSesion;
     private Validar usuario;

    // Tabla
    @FXML private TableView<CarritoTemporal> tablaCarrito;
    @FXML private TableColumn<CarritoTemporal, String>  colCodigo;
    @FXML private TableColumn<CarritoTemporal, String>  colNombre;
    @FXML private TableColumn<CarritoTemporal, Double>  colPrecio;
    @FXML private TableColumn<CarritoTemporal, Integer> colCantidad;
    @FXML private TableColumn<CarritoTemporal, Double>  colSubtotal;

    // Botones (opcional si los manejas desde el FXML directo)
    @FXML private Button btnBuscar;
    @FXML private Button btnAgregar;
    @FXML private Button btnQuitar;
    @FXML private Button btnVender;

    // Estado interno
    private final ObservableList<CarritoTemporal> carrito = FXCollections.observableArrayList();
    private final Conectadb accedeDb = new Conectadb();
    private final VentaService ventaService = new VentaService();
    private double total = 0.0;
    private int existenciasActual = 0;  // 👈 en vez de lblExistencias

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fecha.setValue(LocalDate.now());
        usuario = Validar.getInstance();
         usuarioSesion.setText(usuario.getUsuario());
        // Enlace columnas -> getters de CarritoTemporal
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tablaCarrito.setItems(carrito);

        tfNombre.setEditable(false);
        tfPrecio.setEditable(false);
    }

    @FXML
    private void buscarProducto(ActionEvent e) {
        String cod = tfCodigo.getText() == null ? "" : tfCodigo.getText().trim();
        if (cod.isEmpty()) { warn("Escribe un código."); return; }

        ResultSet rs = null;
        try {
            String sql = "SELECT codigo, nombre, precio, existencias FROM productos WHERE codigo = '" + cod + "'";
            rs = accedeDb.getDato(sql);

            if (rs != null && rs.next()) {
                tfNombre.setText(rs.getString("nombre"));
                tfPrecio.setText(rs.getString("precio"));
                existenciasActual = rs.getInt("existencias");  // 👈 guardamos en variable
                tfCantidad.requestFocus();
            } else {
                warn("No se encontró el producto " + cod);
                limpiarProducto();
            }
        } catch (SQLException ex) {
            error("Error al buscar: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    if (rs.getStatement() != null && rs.getStatement().getConnection() != null) {
                        rs.getStatement().getConnection().close();
                    }
                    rs.close();
                }
            } catch (SQLException ignore) {}
        }
    }

    @FXML
    private void agregarAlCarrito(ActionEvent e) {
        if (tfNombre.getText() == null || tfNombre.getText().isEmpty()) {
            warn("Busca un producto primero.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(tfCantidad.getText().trim());
        } catch (Exception ex) {
            warn("Cantidad inválida.");
            return;
        }

        if (cantidad <= 0 || cantidad > existenciasActual) {
            warn("La cantidad debe ser > 0 y ≤ existencias.");
            return;
        }

        double precio = Double.parseDouble(tfPrecio.getText());
        double subtotal = precio * cantidad;

        carrito.add(new CarritoTemporal(
                tfCodigo.getText().trim(),
                tfNombre.getText(),
                precio,
                cantidad,
                subtotal
        ));

        total += subtotal;
        info("Producto agregado. Total actual: $" + String.format("%.2f", total));
        limpiarProducto();
    }

    @FXML
    private void quitarSeleccion(ActionEvent e) {
        CarritoTemporal sel = tablaCarrito.getSelectionModel().getSelectedItem();
        if (sel == null) { warn("Selecciona un renglón."); return; }

        carrito.remove(sel);
        total -= sel.getSubtotal();
        if (total < 0) total = 0;
        info("Producto quitado. Total actual: $" + String.format("%.2f", total));
    }

    @FXML
    private void vender(ActionEvent e) {
        if (carrito.isEmpty()) { warn("Agrega productos al carrito."); return; }

        try {
            // 1) Registrar venta y detalles
            ventaService.registrarVenta(carrito, total);

            // 2) Descontar existencias en DB
            for (CarritoTemporal it : carrito) {
                String upd = "UPDATE productos SET existencias = existencias - " + it.getCantidad()
                           + " WHERE codigo = '" + it.getCodigo() + "'";
                accedeDb.setDato(upd);
            }

            for (recursos.CarritoTemporal it : carrito) {
    String ins = "INSERT INTO registros (codigo, nombre, precio, cantidad, subtotal) VALUES (" +
                 "'" + it.getCodigo() + "', '" + it.getNombre() + "', " +
                 it.getPrecio() + ", " + it.getCantidad() + ", " + it.getSubtotal() + ")";
    accedeDb.setDato(ins);
}
 carrito.clear();
            total = 0.0;
            info("Venta registrada con éxito.");
        } catch (Exception ex) {
            error("No se completó la venta: " + ex.getMessage());
        }
    }

    // Utilidades
    private void limpiarProducto() {
        tfCodigo.clear();
        tfNombre.clear();
        tfPrecio.clear();
        tfCantidad.clear();
        existenciasActual = 0; // reinicia existencias
    }

    private void warn(String m){ new Alert(Alert.AlertType.WARNING, m).showAndWait(); }
    private void info(String m){ new Alert(Alert.AlertType.INFORMATION, m).showAndWait(); }
    private void error(String m){ new Alert(Alert.AlertType.ERROR, m).showAndWait(); }
}
