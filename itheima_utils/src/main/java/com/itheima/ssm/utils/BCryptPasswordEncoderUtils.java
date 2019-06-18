package com.itheima.ssm.utils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public  class BCryptPasswordEncoderUtils {

    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public static String passwordEncode(String password){
        String encode = bCryptPasswordEncoder.encode(password);
        return encode;
    }

    public static void main(String[] args) {
        String encode = bCryptPasswordEncoder.encode("zjx123");
        System.out.println(encode);
        //$2a$10$JuV6UfkZltSQvqmVEH9laOb5HqmaHzjd9cr9xEbMd81S5eaab.0cm
    }
}
