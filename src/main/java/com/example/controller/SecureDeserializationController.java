package com.example.controller;

import com.example.VulnerableClass;
import com.example.secure.SecureDeserializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/api/secure")
@CrossOrigin(origins = "*")
public class SecureDeserializationController {

    private static final int MAX_PAYLOAD_SIZE = 10000;

    @PostMapping("/deserialize")
    public ResponseEntity<String> deserializeObject(@RequestBody String base64Data) {
        try {
            if (!SecureDeserializer.isValidBase64(base64Data)) {
                return ResponseEntity.badRequest().body("Invalid base64 format");
            }
            
            if (base64Data.length() > MAX_PAYLOAD_SIZE) {
                return ResponseEntity.badRequest().body("Payload too large");
            }
            
            byte[] serializedData = Base64.getDecoder().decode(base64Data);
            
            Object obj = SecureDeserializer.deserializeWithValidation(serializedData, MAX_PAYLOAD_SIZE);
            
            if (obj instanceof VulnerableClass) {
                VulnerableClass vulnerable = (VulnerableClass) obj;
                return ResponseEntity.ok("Safely deserialized: " + vulnerable.getData());
            } else {
                return ResponseEntity.ok("Deserialized object of type: " + obj.getClass().getName());
            }
            
        } catch (SecurityException e) {
            return ResponseEntity.badRequest().body("Security violation: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Deserialization failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Secure deserializer is running");
    }
} 