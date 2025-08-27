
package model;

import java.util.Random;

public class Util {
    
     public static boolean isEmailValid(String email){
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
     
   public static boolean isPasswordValid(String password){
       return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$");
   }
      
   public static String generateCode(){
       Random random = new Random();
       StringBuilder sb = new StringBuilder();
       for(int i = 0; i < 4 ; i++){
           sb.append(random.nextInt(10));
       }
       return sb.toString();
   }
   
    
}
