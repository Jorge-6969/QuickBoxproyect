package cargarPanel.vistas;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import recursos.Vehiculos;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;
import recursos.*;


public class VistazoController implements Initializable {
    private Conectadb accedeDb;
    private ResultSet res;
    
    
    @FXML
    private TextField codigos;
    @FXML
    private TextField nombres;
    @FXML
    private TextField existencias;
    @FXML
    private TextField precios;
    @FXML
    private TextField descripcions;
    @FXML
    private TextField txtFoto;
    @FXML
    private DatePicker fecha;
    @FXML
    private Label usuarioSesion;
    @FXML
    private AnchorPane pane;
    
    
    private Validar usuario;
    
    @FXML private TableView<Vehiculos> tableview;
    @FXML private TableColumn<Vehiculos, String> codigo;
    @FXML private TableColumn<Vehiculos, String> nombre;
    @FXML private TableColumn<Vehiculos, String> existencia;
    @FXML private TableColumn<Vehiculos, String> precio;
    @FXML private TableColumn<Vehiculos, String> descripcion;
    @FXML private TableColumn<Vehiculos, String> fotoCol;

    private final ObservableList<Vehiculos> data = FXCollections.observableArrayList();

   
    
    
    @FXML
    private void selectAll(ActionEvent event){
        tableview.getItems().clear();

        ShowAll();
    }
        void ShowAll(){
        String consulta = "select * from productos;";
        String salida = "";
        System.out.println(consulta);
        res = accedeDb.getDato(consulta);
        try {
            while (res.next()) {                
                String f = res.getString("foto");
                Vehiculos aMostrar = new Vehiculos(
                res.getString("codigo"),
                res.getString("nombre"),
                res.getString("existencias"), 
                res.getString("precio"),
                res.getString("descripcion"),
                f
                                   
                );
                ObservableList<Vehiculos> elementos = tableview.getItems();
                elementos.add(aMostrar);
                tableview.setItems(elementos);


                } 
            }
         catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "setActioner: \n " + ex.toString());
            Logger.getLogger(VistazoController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }  

        
   @FXML
private void update(ActionEvent event) {
    String codigo = codigos.getText().trim();
    if (codigo.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Debe ingresar un código para actualizar.");
        return;
    }

    StringBuilder consulta = new StringBuilder("UPDATE productos SET ");
    boolean hayCambios = false;

    if (!nombres.getText().trim().isEmpty()) {
        consulta.append("nombre = '").append(nombres.getText().trim()).append("', ");
        hayCambios = true;
    }
    if (!existencias.getText().trim().isEmpty()) {
        consulta.append("existencias = '").append(existencias.getText().trim()).append("', ");
        hayCambios = true;
    }
    if (!precios.getText().trim().isEmpty()) {
        consulta.append("precio = '").append(precios.getText().trim()).append("', ");
        hayCambios = true;
    }
    if (!descripcions.getText().trim().isEmpty()) {
        consulta.append("descripcion = '").append(descripcions.getText().trim()).append("', ");
        hayCambios = true;
    }
    if (!txtFoto.getText().trim().isEmpty()) {
        consulta.append("foto = '").append(txtFoto.getText().trim()).append("', ");
        hayCambios = true;
    }

    if (!hayCambios) {
        JOptionPane.showMessageDialog(null, "No hay campos modificados para actualizar.");
        return;
    }

    // Eliminar la última coma y espacio
    consulta.setLength(consulta.length() - 2);

    // Agregar cláusula WHERE
    consulta.append(" WHERE codigo = '").append(codigo).append("';");

    System.out.println(consulta.toString());
    accedeDb.setDato(consulta.toString());
    tableview.getItems().clear();
    ShowAll();
}
    @FXML
    private void delete(ActionEvent event){
        if (confirmacion()) {
            int Codigo = Integer.parseInt(codigos.getText());
            String consulta = "DELETE FROM productos WHERE codigo = " + Codigo + ";";
            System.out.println(consulta);
            accedeDb.setDato(consulta);
        }
        tableview.getItems().clear();
      ShowAll();

        
    } 
    @FXML
    private void selectF(MouseEvent event) {
    Vehiculos seleccionado = tableview.getSelectionModel().getSelectedItem();
    if (seleccionado != null) {
        nombres.setText(seleccionado.getNombre());
        existencias.setText(seleccionado.getExistencia());
        precios.setText(seleccionado.getPrecio());
        descripcions.setText(seleccionado.getDescripcion());
        codigos.setText(seleccionado.getCodigo());
        txtFoto.setText(seleccionado.getFoto());

        // Agregar al carrito compartido
       // CarritoTemporal.agregarProducto(seleccionado);
    }
}
    
    
    // ---------- Botón: Elegir foto ----------
    @FXML
    private void elegirFoto(ActionEvent e) {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Seleccionar imagen");
            fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File file = fc.showOpenDialog(txtFoto.getScene().getWindow());
            if (file == null) return;
            Path rel = copyToImages(file);
            txtFoto.setText(rel.toString().replace("\\", "/"));
        } catch (Exception ex) {
            txtFoto.setText("");
        }
    }

    // ---------- Utils ----------
    private static boolean isImg(String name) {
        String n = name.toLowerCase();
        return n.endsWith(".png") || n.endsWith(".jpg") || n.endsWith(".jpeg") || n.endsWith(".gif");
    }
    private static Path copyToImages(File file) throws Exception {
        Path imagesDir = (Path) Paths.get(System.getProperty("user.dir"), "imagenes");
        Files.createDirectories(imagesDir);
        Path dest = imagesDir.resolve(file.getName());
        Files.copy(file.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
        // devolvemos ruta relativa (imagenes/xxx.jpg)
        return (Path) Paths.get("imagenes").resolve(file.getName());
    }
    private static String value(TextField tf) { return tf.getText() == null ? "" : tf.getText().trim(); }
    private static String esc(String s) { return s.replace("'", "''"); } // escapar comillas simples
        
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {         
        accedeDb = new Conectadb();
        fecha.setValue(LocalDate.now());
        usuario = Validar.getInstance();
        usuarioSesion.setText(usuario.getUsuario());
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        existencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        fotoCol.setCellValueFactory(new PropertyValueFactory<>("foto"));
        
        tableview.setOnMousePressed(event -> selectF(event));
       /* imagen.setPrefWidth(80); 
        imagen.setCellValueFactory(new PropertyValueFactory<>("foto"));

        /*ImageView imagen1 = new ImageView(new Image(this.getClass().getResourceAsStream("imagenes/cAzul.png")));
        ImageView imagen2 = new ImageView(new Image(this.getClass().getResourceAsStream("imagenes/cVerde.png")));
        ImageView imagen3 = new ImageView(new Image(this.getClass().getResourceAsStream("imagenes/cNaranja.png")));*/
        
        /*
        codigo.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("codigo"));      
        nombre.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("nombre"));
        marca.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("marca"));
        precio.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("precio"));
        descripcion.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("descripcion"));*/
      
        
        
      tableview.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
      tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
    if (newSelection != null) {
        nombres.setText(newSelection.getNombre());
        precios.setText(newSelection.getPrecio());
        descripcions.setText(newSelection.getDescripcion());
        codigos.setText(newSelection.getCodigo());
        
       
       
    }
    });
               
     fotoCol.setCellFactory((TableColumn<Vehiculos, String> col) -> new TableCell<Vehiculos, String>() {
    private final ImageView imageView = new ImageView();

    @Override
    protected void updateItem(String rutaRelativa, boolean empty) {
        super.updateItem(rutaRelativa, empty);
        if (empty || rutaRelativa == null || rutaRelativa.isEmpty()) {
            setGraphic(null);
        } else {
            try {
                // Construir ruta absoluta desde la ruta relativa
                Path rutaAbsoluta = Paths.get(System.getProperty("user.dir")).resolve(rutaRelativa);
                File archivo = rutaAbsoluta.toFile();

                if (archivo.exists()) {
                    imageView.setImage(new javafx.scene.image.Image(archivo.toURI().toString()));
                } else {
                    // Imagen por defecto si no existe el archivo
                    imageView.setImage(new javafx.scene.image.Image(getClass().getResource("/recursos/img_default.png").toExternalForm()));
                }

                imageView.setFitWidth(60);
                imageView.setFitHeight(60);
                imageView.setPreserveRatio(true);
                setGraphic(imageView);
            } catch (Exception e) {
                System.out.println("Error cargando imagen: " + e.getMessage());
                setGraphic(null);
            }
        }
    }

            private void setGraphic(Object object) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        tableview.setItems(data);
    }  
    
    
    @FXML
    void agregar(ActionEvent event) {
        
        
        Vehiculos aMostrar = new Vehiculos(
                codigo.getId(),
                nombre.getText(),
                existencia.getText(),
                precio.getText(),
                descripcion.getText(),
                fotoCol.getText()
                );
                
                
        
        
        
        
        ObservableList<Vehiculos> elementos = tableview.getItems();
        elementos.add(aMostrar);
        tableview.setItems(elementos);
    }
    @FXML
    void Buscar(ActionEvent event) {
    String codigoBuscar = codigos.getText().trim();
    String nombreBuscar = nombres.getText().trim();

    if (codigoBuscar.isEmpty() && nombreBuscar.isEmpty()) {
        Vacio(); // Muestra alerta si ambos campos están vacíos
        return;
    }

    String consulta = "SELECT * FROM productos WHERE ";
    if (!codigoBuscar.isEmpty() && !nombreBuscar.isEmpty()) {
        consulta += "codigo = '" + codigoBuscar + "' OR nombre LIKE '%" + nombreBuscar + "%'";
    } else if (!codigoBuscar.isEmpty()) {
        consulta += "codigo = '" + codigoBuscar + "'";
    } else {
        consulta += "nombre LIKE '%" + nombreBuscar + "%'";
    }

    System.out.println(consulta);
    res = accedeDb.getDato(consulta);
    tableview.getItems().clear();

    try {
        boolean encontrado = false;
        while (res.next()) {
            Vehiculos encontradoVehiculo = new Vehiculos(
                res.getString("codigo"),
                res.getString("nombre"),
                res.getString("precio"),
                res.getString("descripcion"),
                res.getString("foto"),
                res.getString("existencias")
            );

            tableview.getItems().add(encontradoVehiculo);
            encontrado = true;
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontraron productos.");
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al buscar: \n" + ex.toString());
        Logger.getLogger(VistazoController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
    @FXML
    private void insert(ActionEvent event) {
        String nombrestr = nombres.getText();
        String preciostr = precios.getText();
        String descripcionstr = descripcions.getText();
        String fotostr = value(txtFoto).replace("\\","/");
        String existenciastr = existencias.getText();
       
        String consulta = "insert into productos " + "(nombre, precio, descripcion, foto, existencias) values ('" + nombrestr + "', '" + preciostr + "', '" + descripcionstr + "');";
        
        System.out.println(consulta);
        accedeDb.setDato(consulta);
        accedeDb.setCerrar();
        
        tableview.getItems().clear();


        ShowAll();
        
    }

    @FXML
    void removeCustomer(ActionEvent event) {
        int selectedID = tableview.getSelectionModel().getSelectedIndex();
        System.out.println(selectedID);
        tableview.getItems().remove(selectedID);
    }
    private void Vacio() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText("Campos Vacios");
        alert.show();
    }
    
    private boolean confirmacion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmación");
        alert.setContentText("¿Confirma acción registro?");
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.isPresent() && buttonType.get().equals(ButtonType.OK);
    }
    
}