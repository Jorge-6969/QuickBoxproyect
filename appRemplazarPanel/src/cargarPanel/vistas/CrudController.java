/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cargarPanel.vistas;


import java.util.Optional;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import recursos.*;



/**
 *
 * @author Julia
 */
public class CrudController implements Initializable {
    
    private Conectadb accedeDb;
    private ResultSet res;
    @FXML
    private TextField id, usuario, nombre, aPaterno, aMaterno;
    
    @FXML
    private PasswordField contrasena,contrasena1;
    
    @FXML
    private TextArea area;
    @FXML
    private Button btnUpdate, btnDelete;


    
    @FXML
    private void selectOne(ActionEvent event){
        int idInt = Integer.parseInt(id.getText());
        String consulta = "select * from usuarios where id = "+ idInt + ";";
        res = accedeDb.getDato(consulta);
        try {
            if (res.next()){
            nombre.setText(res.getString("nombre"));
            aPaterno.setText(res.getString("aPaterno"));
            aMaterno.setText(res.getString("aMaterno"));
            usuario.setText(res.getString("usuario"));
            contrasena.setText(String.valueOf(res.getInt("contrasena")));
            contrasena1.setText(String.valueOf(res.getInt("contrasena")));

            } else {
                JOptionPane.showMessageDialog(null, "Error");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "setActioner: \n " + ex.toString());
            Logger.getLogger(CrudController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            accedeDb.setCerrar();
        }
    }

    @FXML
    public void selectAll(ActionEvent event) {
        String consulta = "select * from usuarios;";
        System.out.println(consulta);
        res = accedeDb.getDato(consulta);
        
        StringBuilder datos = new StringBuilder();
        try {
            if (res.next()) {
                do {
                    datos.append(res.getInt("id")).append(", ").append(res.getString("nombre")).append(" ").append(res.getString("aPaterno")).append(" ").append(res.getString("aMaterno")).append(", ")
                            .append(res.getString("usuario")).append(", ").append(res.getInt("contrasena")).append("\n");
                    area.setText(datos.toString());
                } while (res.next());
            } else {
                JOptionPane.showMessageDialog(null, "Error");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "setActioner: \n " + ex.toString());
            Logger.getLogger(CrudController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            accedeDb.setCerrar();
        }
    }
    
    @FXML
    private void insert(ActionEvent event) {
        String usuariostr = usuario.getText();
        String nombrestr = nombre.getText();
        String aPaternostr = aPaterno.getText();
        String aMaternostr = aMaterno.getText();
        int contrasenaInt = Integer.parseInt(contrasena.getText());
        int contrasenaIn = Integer.parseInt(contrasena1.getText());
        
        if(contrasenaInt == contrasenaIn){
        String consulta = "insert into usuarios " + "(nombre, aPaterno, aMaterno, usuario, contrasena) values ('" + nombrestr + "', '" + aPaternostr + "', '" + aMaternostr + "', '" + usuariostr + "', '" + contrasenaInt + "');";
        
        System.out.println(consulta);
        accedeDb.setDato(consulta);
        accedeDb.setCerrar();
        }
        else{
            area.setText("Contraseña incorrecta");
        }
    }
    
    
    @FXML
    private void update(ActionEvent event) {
        String usuariostr = usuario.getText();
        String nombrestr = nombre.getText();
        String aPaternostr = aPaterno.getText();
        String aMaternostr = aMaterno.getText();
        int idInt = Integer.parseInt(id.getText());
        String consulta = "UPDATE usuarios SET " +
        "nombre = '" + nombrestr + "', " +
        "aPaterno = '" + aPaternostr + "', " +
        "aMaterno = '" + aMaternostr + "', " +
        "usuario = '" + usuariostr + "' " +
        "WHERE id = " + idInt + ";";
        System.out.println(consulta);
        accedeDb.setDato(consulta);
        accedeDb.setCerrar();
        
     selectAll(new ActionEvent());

    }

    
    @FXML
    private void delete(ActionEvent event) {
        if(confirmacion());
        int idInt = Integer.parseInt(id.getText());
        String consulta = "DELETE FROM usuarios WHERE id =" + idInt + ";";
        System.out.println(consulta);
        accedeDb.setDato(consulta);
        accedeDb.setCerrar();
    }
    
        public void habilitarPorId() {
        boolean campoId = id.getText().isEmpty();
        btnUpdate.setDisable(campoId);
        btnDelete.setDisable(campoId);
    }
    private boolean confirmacion() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Confirmación");
        alert.setContentText("¿Confirma acción registro?");
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.isPresent() && buttonType.get().equals(ButtonType.OK);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accedeDb = new Conectadb();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        
        id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                id.setText(oldValue);
            }
        }
        );
        
    }    
    
}
