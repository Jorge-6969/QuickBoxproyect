package recursos;

public class CarritoTemporal {
    private String codigo;
    private String nombre;
    private double precio;
    private int cantidad;
    private double subtotal;

    public CarritoTemporal(String codigo, String nombre, double precio, int cantidad, double subtotal) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return subtotal; }
}
