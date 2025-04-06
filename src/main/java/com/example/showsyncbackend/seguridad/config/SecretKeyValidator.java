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
        String base64Key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c3VhcmlvQGV4YW1wbGUuY29tIiwiaWF0IjoxNjg5MTQ2NjAwLCJleHBpcmF0aW9uIjoxNjg5MjQwNjAwfQ.NFSem2kkj9UFR72zEfe7lV-UO4DgeUE0HhqxBxl6eU4";
        validate(base64Key);
    }
}

