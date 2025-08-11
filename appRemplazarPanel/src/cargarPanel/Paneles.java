package cargarPanel;

import recursos.Validar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Paneles extends Application {
    private Validar usuario;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        usuario = Validar.getInstance();
        usuario.setUsuario("Jorge Osornio Plancate");
        
        
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("cargarPanel/imagenes/Box.jpeg"));
        stage.show();
        
        
    }

    public static void main(String[] args) {
        launch(args);
    }    
}