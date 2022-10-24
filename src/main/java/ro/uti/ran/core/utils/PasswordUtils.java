package ro.uti.ran.core.utils;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Author Vitalie Babălungă on 12.02.2016.
 */
public class PasswordUtils {


    public static String generatePassword(){
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
