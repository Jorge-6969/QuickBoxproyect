package cargarPanel;

import recursos.Validar;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import recursos.Conectadb;



public class LoginController implements Initializable {
    private Conectadb accedeDb;
    private ResultSet res;
    md5 encriptar = new md5();
    
    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button loginBtn;

    
    private double x= 0 ;
    private double y= 0;
    
    public void loginAdmin() throws SQLException{       
        String usuariost = txtUsuario.getText();
        String consulta = "select * from usuarios where usuario = '" + usuariost + "';";         
        System.out.println(consulta);
        
       
        
         
        
        try{
            Alert alert;
            if(txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty()){
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Campos vacios");
                alert.showAndWait();
            }else{
                
            
             
              res = accedeDb.getDato(consulta);
             
              
              boolean usuarioExiste = res.next();
              
            if (usuarioExiste){
                String passwordDB = res.getString("contrasena");
                String nom = res.getString("nombre");

                     if (txtPassword.getText().equals(passwordDB)) {
                         alert = new Alert(AlertType.INFORMATION);
                         alert.setTitle("Información");
                         alert.setHeaderText(null);
                         alert.setContentText("Acceso correcto");
                         alert.showAndWait();
                         
                         Validar usuarioSingleton = Validar.getInstance();
                         usuarioSingleton.setUsuario(nom);

                        loginBtn.getScene().getWindow().hide();
                        Parent root = FXMLLoader.load(getClass().getResource("Principal.fxml"));

                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.getIcons().add(new Image("cargarPanel/imagenes/Box.jpeg"));
                        

                        root.setOnMousePressed((MouseEvent event) -> {
                         x = event.getSceneX();
                         y = event.getSceneY();
                         });

                         root.setOnMouseDragged((MouseEvent event) -> {
                         stage.setX(event.getScreenX() - x);
                         stage.setY(event.getScreenY() - y);
                         });

                    stage.initStyle(StageStyle.TRANSPARENT);
                    stage.setScene(scene);
                    stage.show();

            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Contraseña incorrecta");
                alert.showAndWait();
                }

             } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Usuario inexistente");
                alert.showAndWait();
                }

         }
            
            
        }catch(IOException e){
        }
        
    }
    
    public void close(){
        System.exit(0);
    }
    
    //LETS NAME THE COMPONENTS ON LOGIN FORM : )
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accedeDb = new Conectadb();
        // TODO
    }    
    
}
