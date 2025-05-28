package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.servicios.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "http://localhost:4200") // ajusta esto si tu frontend está en otra URL
public class ImageController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al subir la imagen: " + e.getMessage());
        }
    }
}
