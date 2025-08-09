package recursos;

public class Validar {

    private String usuario;


    

    
    private static Validar valido = new Validar();

    private Validar() {
    }

    public static Validar getInstance() {
        return valido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public static Validar getValido() {
        return valido;
    }

    public static void setValido(Validar valido) {
        Validar.valido = valido;
    }
}