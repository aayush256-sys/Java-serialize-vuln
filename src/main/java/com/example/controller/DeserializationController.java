package com.example.controller;

import com.example.VulnerableClass;
import com.example.util.SerializationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DeserializationController {

    @PostMapping("/deserialize")
    public ResponseEntity<String> deserializeObject(@RequestBody String base64Data) {
        try {
            byte[] serializedData = Base64.getDecoder().decode(base64Data);
            
            ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
            ObjectInputStream ois = new ObjectInputStream(bis);
            
            Object obj = ois.readObject();
            ois.close();
            
            if (obj instanceof VulnerableClass) {
                VulnerableClass vulnerable = (VulnerableClass) obj;
                return ResponseEntity.ok("Deserialized successfully: " + vulnerable.getData());
            } else {
                return ResponseEntity.ok("Deserialized object of type: " + obj.getClass().getName());
            }
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Deserialization failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Server is running");
    }
    
    @PostMapping("/generate-safe-payload")
    public ResponseEntity<String> generateSafePayload(@RequestBody PayloadRequest request) {
        try {
            String payload = SerializationUtil.createSafePayload(request.getData());
            return ResponseEntity.ok(payload);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate payload: " + e.getMessage());
        }
    }
    
    @PostMapping("/generate-exploit-payload")
    public ResponseEntity<String> generateExploitPayload(@RequestBody PayloadRequest request) {
        try {
            String payload = SerializationUtil.createExploitPayload(request.getCommand());
            return ResponseEntity.ok(payload);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate payload: " + e.getMessage());
        }
    }
    
    public static class PayloadRequest {
        private String data;
        private String command;
        
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
        public String getCommand() { return command; }
        public void setCommand(String command) { this.command = command; }
    }
} 