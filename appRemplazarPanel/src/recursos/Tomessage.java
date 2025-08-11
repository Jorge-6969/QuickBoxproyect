package recursos;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Tomessage {

    private String noVisible;
    private String[] temps;
    private String[] tempss;

    public Tomessage() {
    }

    public String setPoderver(String s) {
        temps = s.split(":");
        if (temps.length > 1) {
            noVisible = temps[0] + "\n";
            for (int i = 1; i < temps.length; i++) {
                tempss = temps[i].split(" y ");
                if (tempss.length > 0) {
                    for (int j = 0; j < tempss.length; j++) {
                        noVisible = noVisible + tempss[j] + "\n";
                    }
                } else {
                    noVisible = noVisible + temps[i] + "\n";
                }
            }
        } else {
            noVisible = s;
        }
        return noVisible;
    }

    public void setPrint() {
        String file = "/Factura/ReciboPC.pdf";
        try {
            Runtime.getRuntime().exec("cmd.exe /C start acrord32 /P /h" + file);
        } catch (IOException exio) {
            JOptionPane.showMessageDialog(null, "Verique el siguiente error: \n " + setPoderver(exio.toString()), "Al imprimir hay un detalle", 1);
            Logger.getLogger(Tomessage.class.getName()).log(Level.SEVERE, null, exio);
        }
        JOptionPane.showMessageDialog(null, "Verifique la impresión ...", "Para imprimir", 1);
    }

    public void setCloseacroba() {
        try {
            Runtime.getRuntime().exec("taskkill /f  /im  AcroRd32.exe");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Aviso: \n " + setPoderver(ex.toString()), "Al cerrar el Acroba", 1);
            Logger.getLogger(Tomessage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
