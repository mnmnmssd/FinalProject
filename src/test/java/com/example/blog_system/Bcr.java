package com.example.blog_system;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 笑的心酸 - Red4Lion - mnmnmssd
 * @date 2021.10.19
 */
public class Bcr {
    public static String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    @Test
    public void BcrTest(){
        System.out.println(Bcr.encodePassword("123456"));
    }
}
