/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cargarPanel.vistas;


import cargarPanel.md5;
import recursos.*;
import java.util.Optional;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;



/**
 *
 * @author Julia
 */
public class CrudController implements Initializable {
    
    private Conectadb accedeDb;
    private ResultSet res;
    md5 encriptar = new md5();
    @FXML
    private TextField id, usuarios, nombre, aPaterno, aMaterno,txtcontrasena1;
    @FXML
    private TextField txtcontrasena;

    
    @FXML
    private TextArea area;
    @FXML
    private Button btnUpdate, btnDelete;
    @FXML
    private DatePicker fecha;
    @FXML
    private Label usuarioSesion;
    
    private Validar usuario;




    
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
            usuarios.setText(res.getString("usuario"));
            txtcontrasena.setText("****");           
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
                            .append(res.getString("usuario")).append(", ").append(res.getString("contrasena1")).append("\n");
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
        String usuariostr = usuarios.getText();
        String nombrestr = nombre.getText();
        String aPaternostr = aPaterno.getText();
        String aMaternostr = aMaterno.getText();
        String contrasena = txtcontrasena.getText();
        String contrasena1 = txtcontrasena1.getText();
       
        
        if(txtcontrasena1.getText().equals(txtcontrasena.getText())){
            String contraE = encriptar.tomd5(contrasena);
            String consulta = "INSERT INTO usuarios (nombre, aPaterno, aMaterno, usuario, contrasena, contrasena1) VALUES ('" +
            nombrestr + "', '" + aPaternostr + "', '" + aMaternostr + "', '" + usuariostr + "', '" +
             contrasena + "', '" + contraE + "');";
        
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
        String usuariostr = usuarios.getText();
        String nombrestr = nombre.getText();
        String aPaternostr = aPaterno.getText();
        String aMaternostr = aMaterno.getText();
        String contrasena = txtcontrasena.getText();
        int idInt = Integer.parseInt(id.getText());
        String consulta = "UPDATE usuarios SET " +
        "nombre = '" + nombrestr + "', " +
        "aPaterno = '" + aPaternostr + "', " +
        "aMaterno = '" + aMaternostr + "', " +
        "usuario = '" + usuariostr + "', " +
        "contrasena = '" + contrasena + "', " +
        "contrasena1 = '" + encriptar.tomd5(contrasena) + "' " +
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
        fecha.setValue(LocalDate.now());
        usuario = Validar.getInstance();
        usuarioSesion.setText(usuario.getUsuario());
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
