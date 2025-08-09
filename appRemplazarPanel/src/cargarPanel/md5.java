package cargarPanel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class md5 {

    public static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("MD5 algorithm not found: " + e.getMessage());
            return null;
        }
    }
    public String tomd5(String text) {
        return generateMD5(text);
    }


    public static void main(String[] args) {
        String text = "Hello, MD5!";
        String text2 = "hello, MD5!";
        
        String md5Hash = generateMD5(text);
        String md5Hash2 = generateMD5(text2);

        if (md5Hash != null) {
            System.out.println("MD5 Hash of \"" + text + "\": " + md5Hash);
            System.out.println("MD5 Hash of \"" + text2 + "\": " + md5Hash2);
        }
    }

    

    

}