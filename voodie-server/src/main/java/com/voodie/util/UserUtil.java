package com.voodie.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Voodie
 * User: MikeD
 */
public class UserUtil {

    public static String encodePassword(String plainText){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(plainText);
    }
}
