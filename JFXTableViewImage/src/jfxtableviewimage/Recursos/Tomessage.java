package jfxtableviewimage.Recursos;

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
}