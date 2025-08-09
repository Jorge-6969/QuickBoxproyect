package cargarPanel.vistas;

import recursos.Validar;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.xml.bind.DatatypeConverter;

public class ContrasenaController extends StackPane implements Initializable {

    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField passConf;
    @FXML
    private TextField txtNoUsuario;
    @FXML
    private TextField txtMd5;
    @FXML
    private Button btnBuscar;
    @FXML
    private Label usuarioSesion;
    @FXML
    private DatePicker fecha;
  

       

    private Validar usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fecha.setValue(LocalDate.now());
        usuario = Validar.getInstance();
        usuarioSesion.setText(usuario.getUsuario());
    }
    

    @FXML
    private void encriptar(ActionEvent event) {
        txtMd5.setText(encriptar(pass.getText()));

    }

    String encriptar(String encr) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(encr.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}