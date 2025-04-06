package com.example.showsyncbackend.seguridad.config;

import java.util.Base64;

public class SecretKeyValidator {
    public static void validate(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        if (decodedKey.length < 32) {
            throw new IllegalArgumentException("La clave secreta debe tener al menos 256 bits.");
        }
        System.out.println("La clave secreta es válida y tiene al menos 256 bits.");
    }

    public static void main(String[] args) {
        String base64Key = "Y2FuZXJhdG9ya2V5Y29tYXBsaW9uZXltYXRjaGVkZ2Zvc3RocGxhd2dqZnZhbW9uY2Fk";
        validate(base64Key);
    }
}



