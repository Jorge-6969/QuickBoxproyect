package jfxtableviewimage.Recursos;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Conectadb {

    //private final String driver = "com.mysql.jdbc.Driver";
    final private String url = "jdbc:mysql://localhost:3306/appjavafx";
    final private String user;
    final private String paswd;
    private Connection mysqlCon = null;
    private Statement stmt;
    private ResultSet res;
    private final Tomessage tmg = new Tomessage();

    public Conectadb() {
        user = "root";
        paswd = "morasdulces123";
    }

    public boolean setConexion() {
        try {
            //Class.forName(driver);
            mysqlCon = DriverManager.getConnection(url, user, paswd);
            stmt = mysqlCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("conexion");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Sin Conexión", "Sin Conexión", 2);
            return false;
        }
        return true;
    }

    public Connection getConection() {
        return mysqlCon;
    }

    public void setDato(String sqlDato) {
        setConexion();
        try {
            stmt.execute(sqlDato);
            stmt.close();
            mysqlCon.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + tmg.setPoderver(ex.toString()), "El método set", 2);
            Logger.getLogger(Conectadb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet getDato(String sqlDato) {
        setConexion();
        try {
            res = stmt.executeQuery(sqlDato);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + tmg.setPoderver(ex.toString()), "El método Get", 2);
            Logger.getLogger(Conectadb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

    public void setCerrar() {
        try {
            mysqlCon.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Problema tipo: \n " + ex.toString());
            Logger.getLogger(Conectadb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}