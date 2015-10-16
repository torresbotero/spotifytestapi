package com.spotifytest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Camilo on 15/10/2015.
 */
public class ValidationUtil {

    /**
     * Metodo utilizado para validar si un String cumple con la expresion regular
     * enviada como paramtro
     * @param regularExpression
     * @param data
     * @return
     */
    public static boolean isValid(String regularExpression, String data){
        if(data==null || data.isEmpty()){
            return false;
        }else{
            data=data.trim();
            Pattern pat = Pattern.compile(regularExpression);
            Matcher mat = pat.matcher(data);
            if (mat.matches()) {
                return true;
            } else {
                return false;
            }
        }
    }

}
