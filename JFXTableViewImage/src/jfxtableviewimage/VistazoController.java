package jfxtableviewimage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;
import jfxtableviewimage.Recursos.Conectadb;

public class VistazoController implements Initializable {
    private Conectadb accedeDb;
    private ResultSet res;
    
    
    @FXML
    private TextField codigos;
    @FXML
    private TextField nombres;
    @FXML
    private TextField precios;
    @FXML
    private TextField descripcions;
    
    @FXML private TableView<Vehiculos> tableview;
    @FXML private TableColumn<Vehiculos, String> codigo;
    @FXML private TableColumn<Vehiculos, String> nombre;
    @FXML private TableColumn<Vehiculos, String> precio;
    @FXML private TableColumn<Vehiculos, String> descripcion;
    @FXML private TableColumn<Vehiculos, String> imagen;

   
    private final ObservableList<Vehiculos> data = FXCollections.observableArrayList();
    
    
    @FXML
    private void selectAll(ActionEvent event){
        tableview.getItems().clear();

        ShowAll();
    }
        void ShowAll(){
        String consulta = "select * from producto;";
        String salida = "";
        System.out.println(consulta);
        res = accedeDb.getDato(consulta);
        try {
            if (res.next()) {
                do{
                Vehiculos aMostrar = new Vehiculos(
                res.getString("codigo"),
                res.getString("nombre"),
                res.getString("precio"),
                res.getString("descripcion")               
                                       
                );
                ObservableList<Vehiculos> elementos = tableview.getItems();
                elementos.add(aMostrar);
                tableview.setItems(elementos);


                } while (res.next());
            } else {
                JOptionPane.showMessageDialog(null, "Error");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "setActioner: \n " + ex.toString());
            Logger.getLogger(VistazoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    
    }
        
    @FXML
    private void update(ActionEvent event){
        if (!descripcions.getText().isEmpty() && !nombres.getText().isEmpty() && precios.getText().isEmpty()) {
                  
            String Codigo = codigos.getText();
            String consulta = "UPDATE producto SET"
                    + " nombre = '" + nombres.getText().trim() + "',"
                    + " precio = '" + precios.getText().trim() + "',"
                    + " descripcion = '" + descripcions.getText().trim() + "',"
                    + " WHERE id = " + codigos.getText().trim() + ";";
            System.out.println(consulta);
            accedeDb.setDato(consulta);
            ShowAll();
        
    }
    }
    @FXML
    private void delete(ActionEvent event){
        if (confirmacion()) {
            int Codigo = Integer.parseInt(codigos.getText());
            String consulta = "DELETE FROM producto WHERE codigo = " + Codigo + ";";
            System.out.println(consulta);
            accedeDb.setDato(consulta);
        }
      

        
    } 
    @FXML
    private void selectF(MouseEvent event) {
        
        nombres.setText(tableview.getSelectionModel().getSelectedItem().getNombre());
        precios.setText(tableview.getSelectionModel().getSelectedItem().getPrecio());
        descripcions.setText(tableview.getSelectionModel().getSelectedItem().getDescripcion());
        codigos.setText(tableview.getSelectionModel().getSelectedItem().getCodigo());
        
     
    }
        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        accedeDb = new Conectadb();
        
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        imagen.setPrefWidth(80); 
        imagen.setCellValueFactory(new PropertyValueFactory<>("imagen"));

        ImageView imagen1 = new ImageView(new Image(this.getClass().getResourceAsStream("imagenes/cAzul.png")));
        ImageView imagen2 = new ImageView(new Image(this.getClass().getResourceAsStream("imagenes/cVerde.png")));
        ImageView imagen3 = new ImageView(new Image(this.getClass().getResourceAsStream("imagenes/cNaranja.png")));
        
        
        codigo.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("codigo"));      
        nombre.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("nombre"));
        precio.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("precio"));
        descripcion.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("descripcion"));
        imagen.setCellValueFactory(new PropertyValueFactory<Vehiculos, String>("imagen"));
        
        
      tableview.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.SINGLE);
      tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
    if (newSelection != null) {
        nombres.setText(newSelection.getNombre());
        precios.setText(newSelection.getPrecio());
        descripcions.setText(newSelection.getDescripcion());
        codigos.setText(newSelection.getCodigo());
        
       
       
    }
    });
               
     

        tableview.setItems(data);
    }  
    
    
    @FXML
    void agregar(ActionEvent event) {
        
        
        Vehiculos aMostrar = new Vehiculos(
                codigo.getId(),
                nombre.getText(),
                precio.getText(),
                descripcion.getText()
                );
                
                
        
        
        
        
        ObservableList<Vehiculos> elementos = tableview.getItems();
        elementos.add(aMostrar);
        tableview.setItems(elementos);
    }
    
    @FXML
    private void insert(ActionEvent event) {
        String nombrestr = nombres.getText();
        String preciostr = precios.getText();
        String descripcionstr = descripcions.getText();
       
        String consulta = "insert into producto " + "(nombre, precio, descripcion) values ('" + nombrestr + "', '" + preciostr + "', '" + descripcionstr + "');";
        
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