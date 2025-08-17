package cargarPanel.vistas;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import recursos.Registro;
import recursos.Conectadb;
import recursos.Validar;

public class HistorialVenta implements Initializable {

    @FXML private TableView<Registro> tablaHistorial;
    @FXML private TableColumn<Registro,Integer> colId;
    @FXML private TableColumn<Registro,String>  colCodigo;
    @FXML private TableColumn<Registro,String>  colNombre;
    @FXML private TableColumn<Registro,Double>  colPrecio;
    @FXML private TableColumn<Registro,Integer> colCantidad;
    @FXML private TableColumn<Registro,Double>  colSubtotal;
    @FXML private TableColumn<Registro,String>  colFecha;
    @FXML private TextField tfBuscar;
    @FXML
    private DatePicker fecha;
    @FXML
    private Label usuarioSesion;
     private Validar usuario;

    private final ObservableList<Registro> datos = FXCollections.observableArrayList();
    private final Conectadb db = new Conectadb();
    private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fecha.setValue(LocalDate.now());
        usuario = Validar.getInstance();
         usuarioSesion.setText(usuario.getUsuario());
        // Enlazar columnas con getters de recursos.Registro
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        tablaHistorial.setItems(datos);
        cargarTodo(); // carga inicial
    }

    // --- BOTONES ---

    @FXML
    private void refrescar() {
        cargarTodo();
    }

    @FXML
    private void verTodo() {
        tfBuscar.clear();
        cargarTodo();
    }

    @FXML
    private void buscar() {
        String q = (tfBuscar.getText() == null) ? "" : tfBuscar.getText().trim();
        if (q.isEmpty()) { cargarTodo(); return; }

        String sql =
            "SELECT id, codigo, nombre, precio, cantidad, subtotal, fecha " +
            "FROM registros " +
            "WHERE codigo LIKE '%" + esc(q) + "%' OR nombre LIKE '%" + esc(q) + "%' " +
            "ORDER BY fecha DESC, id DESC";
        cargarDesdeSQL(sql);
    }

    // --- CARGA DE DATOS ---

    private void cargarTodo() {
        String sql =
            "SELECT id, codigo, nombre, precio, cantidad, subtotal, fecha " +
            "FROM registros ORDER BY fecha DESC, id DESC";
        cargarDesdeSQL(sql);
    }

    private void cargarDesdeSQL(String sql) {
        datos.clear();
        ResultSet rs = null;
        try {
            rs = db.getDato(sql);
            while (rs != null && rs.next()) {
                int id          = rs.getInt("id");
                String codigo   = rs.getString("codigo");
                String nombre   = rs.getString("nombre");
                double precio   = rs.getDouble("precio");
                int cantidad    = rs.getInt("cantidad");
                double subtotal = rs.getDouble("subtotal");

                String fechaTxt;
                try {
                    java.sql.Timestamp ts = rs.getTimestamp("fecha");
                    fechaTxt = (ts != null) ? fmt.format(ts) : "";
                } catch (SQLException e) {
                    fechaTxt = "";
                }

                datos.add(new Registro(id, codigo, nombre, precio, cantidad, subtotal, fechaTxt));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error al cargar historial:\n" + e.getMessage()).showAndWait();
        } finally {
            // Cierra recursos (Conectadb deja abierta la conexión en getDato)
            try {
                if (rs != null) {
                    if (rs.getStatement() != null && rs.getStatement().getConnection() != null)
                        rs.getStatement().getConnection().close();
                    rs.close();
                }
            } catch (SQLException ignore) {}
        }
    }

    // Escapa comillas simples para evitar romper el LIKE si el usuario escribe '
    private String esc(String s) {
        return s.replace("'", "''");
    }
}
