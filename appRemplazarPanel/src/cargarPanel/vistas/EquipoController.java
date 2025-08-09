package cargarPanel.vistas;

import recursos.Validar;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class EquipoController extends StackPane implements Initializable {

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
}