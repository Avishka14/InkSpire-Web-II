package model;

import java.math.BigInteger;
import java.security.MessageDigest;

public class PayHereUtil {

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) hashtext = "0" + hashtext;
            return hashtext.toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generatePayHereHash(String merchantId, String orderId, String amount, String currency, String merchantSecret) {
        String secretHash = getMd5(merchantSecret);
        String rawHash = merchantId + orderId + amount + currency + secretHash;
        return getMd5(rawHash);
    }
}
