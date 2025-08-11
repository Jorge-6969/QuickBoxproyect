package cargarPanel.vistas;

import recursos.Conectadb;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import org.controlsfx.control.textfield.TextFields;

public class UsuarioController extends StackPane implements Initializable {

    @FXML
    private ComboBox<String> Opciones;    
    @FXML
    private TextField AutoCompletar;
    @FXML
    private Label Etiqueta;
   



    private Conectadb accedeDb;
    private ResultSet res;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accedeDb = new Conectadb();

        ArrayList<String> nombres = new ArrayList<String>();
        res = accedeDb.getDato("SELECT nombre FROM tblusuarios");
        try {
            if (res.first()) {
                do {
                    String s1 = res.getString("nombre");
                    nombres.add(s1);
                } while (res.next());
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no registrado en la base de datos", "Usuario no encontrado", 1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "De buscar: \n " + ex.toString());
        } finally {
            accedeDb.setCerrar();
        }
        TextFields.bindAutoCompletion(AutoCompletar, nombres);
        
        Opciones.getItems().addAll("Femenino", "Masculino");
        Opciones.setValue("Femenino");
    }
    
    @FXML
    private void mostrar(ActionEvent event) {
        Etiqueta.setText(Opciones.getSelectionModel().getSelectedItem());
        
    }    
}