
package model;

public class Util {
    
     public static boolean isEmailValid(String email){
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
    
}
