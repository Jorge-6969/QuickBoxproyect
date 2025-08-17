package recursos;

public class Registro {
    private int id;
    private String codigo, nombre, fecha;
    private double precio, subtotal;
    private int cantidad;

    public Registro(int id, String codigo, String nombre, double precio, int cantidad, double subtotal, String fecha) {
        this.id = id; this.codigo = codigo; this.nombre = nombre;
        this.precio = precio; this.cantidad = cantidad; this.subtotal = subtotal; this.fecha = fecha;
    }
    public int getId(){ return id; }
    public String getCodigo(){ return codigo; }
    public String getNombre(){ return nombre; }
    public double getPrecio(){ return precio; }
    public int getCantidad(){ return cantidad; }
    public double getSubtotal(){ return subtotal; }
    public String getFecha(){ return fecha; }
}
