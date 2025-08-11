package recursos;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Conectadb {
  private final String driver = "com.mysql.jdbc.Driver";
  private String url = "jdbc:mysql://localhost:3306/appjavafx";
    
  private String user;
  private String paswd;
  private Connection mysqlCon = null;
  private Statement stmt;
  private ResultSet res;
  private Validar singleUP;
  private Tomessage tmg;
  
  public Conectadb(){
    singleUP = Validar.getInstance();
    tmg = new Tomessage();
    user = "root"; paswd = "morasdulces123";//mysql root
    //user = "useriot"; paswd = "consult1";//mysql consultorio
  }
  public boolean setConexion(){
    try {
      Class.forName(driver);
       mysqlCon = DriverManager.getConnection(url, user, paswd);
       stmt = mysqlCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
       System.out.println("conexion");
      }catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, " " + tmg.setPoderver(ex.toString()),"Conexión", 2);
        System.out.println("NO conexion");
        return false;
      }catch (ClassNotFoundException e) {
        JOptionPane.showMessageDialog(null, " " + tmg.setPoderver(e.toString()),"Del driver", 2);
        System.out.println("NO conexion");
        return false;
     }
    return true ;
  }
  public Connection getConection(){
      return mysqlCon;
  }
   public void setDato(String sqlDato){
    setConexion();
    try {
      stmt.execute(sqlDato);
      stmt.close();
      mysqlCon.close();
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, " " + tmg.setPoderver(ex.toString()),"El método set", 2);
      Logger.getLogger(Conectadb.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  public ResultSet getDato(String sqlDato){
    setConexion();
    try {
      res = stmt.executeQuery(sqlDato);
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null," " + tmg.setPoderver(ex.toString()),"El método Get", 2);
      Logger.getLogger(Conectadb.class.getName()).log(Level.SEVERE, null, ex);
    }
    return res;
  }
  public void setCerrar(){
    try {
      mysqlCon.close();
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(null, "Problema tipo: \n " + ex.toString());
      Logger.getLogger(Conectadb.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}