package cargarPanel;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import recursos.Conectadb;

public class PrincipalController implements Initializable {

    @FXML
    private BorderPane bp;
    
    private Conectadb accedeDb;
    private ResultSet res;
    
    
    
    public void loginAdmin() throws SQLException{
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) { 

      accedeDb = new Conectadb();
    
    }
     
    
         
       

    @FXML
    private void Usuario(ActionEvent event) {
        loadPage("vistas/Equipo.fxml");
    }
    
    @FXML
    private void Venta(ActionEvent event) {
        loadPage("vistas/Crud.fxml");
    }
    
    @FXML
    private void Inventario(ActionEvent event) {
        loadPage("vistas/Vistazo.fxml");
    }
    
    @FXML
    private void cerrar(ActionEvent event) {
        System.exit(0);
    }
    
    private void loadPage(String page){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(page));
        } catch (IOException ex) {
            Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        bp.setCenter(root);
    }   


}