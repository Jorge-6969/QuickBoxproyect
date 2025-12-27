package recursos;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Vehiculos {

    private final StringProperty codigo;
    private final StringProperty nombre;
    private final StringProperty existencia;
    private final StringProperty precio;
    private final StringProperty descripcion;
    private final ImageView imageView = new ImageView();
    private final StringProperty foto;
    

   

     

 public Vehiculos(String codigo, String nombre,String existencia, String precio, String descripcion, String foto) {
        this.codigo = new SimpleStringProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.existencia = new SimpleStringProperty(existencia);
        this.precio = new SimpleStringProperty(precio);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.foto = new SimpleStringProperty(foto);



      
    }

    
    
    
      
    public String setCodigo(String cod){
        this.codigo.set(cod);
        return cod;
    }
    
    public String getCodigo(){
        return codigo.get();
    }


    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nom) {
        nombre.set(nom);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String des) {
        descripcion.set(des);
    }

    public String getPrecio() {
        return precio.get();
    }

    public void setPrecio(String pre) {
        this.precio.set(pre);
    }
    public String getExistencia() {
        return existencia.get();
    }

    public void setExistencia(String exs) {
        this.existencia.set(exs);
    }
    public String getFoto() {
        return foto.get();
    }
    public void setFoto(String img) {
        this.foto.set(img);
    }


   
}