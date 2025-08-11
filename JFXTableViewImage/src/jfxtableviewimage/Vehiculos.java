package jfxtableviewimage;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class Vehiculos {

    private final StringProperty codigo;
    private final StringProperty nombre;
    private final StringProperty precio;
    private final StringProperty descripcion;
    private ImageView imagen;

    Vehiculos(String codigo, String nombre, String precio, String descripcion) {
        this.codigo = new SimpleStringProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.precio = new SimpleStringProperty(precio);
        this.descripcion = new SimpleStringProperty(descripcion);

      
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
    
    public ImageView getImagen() {
        return imagen;
    }

    public void setImagen(ImageView img) {
        this.imagen = img;
    }
}