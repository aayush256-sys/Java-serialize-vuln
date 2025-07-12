package com.example.secure;

import java.io.*;
import java.util.Set;

public class SecureDeserializer {
    
    private static final Set<String> ALLOWED_CLASSES = Set.of(
        "com.example.VulnerableClass",
        "java.lang.String",
        "java.util.HashMap",
        "java.util.ArrayList"
    );
    
    public static Object deserialize(byte[] data) throws Exception {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            ObjectInputStream secureOis = new ObjectInputStream(bis) {
                @Override
                protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                    String className = desc.getName();
                    
                    if (!ALLOWED_CLASSES.contains(className)) {
                        throw new SecurityException("Class not allowed: " + className);
                    }
                    
                    return super.resolveClass(desc);
                }
            };
            
            return secureOis.readObject();
        }
    }
    
    public static Object deserializeWithValidation(byte[] data, int maxSize) throws Exception {
        if (data.length > maxSize) {
            throw new SecurityException("Payload too large: " + data.length + " bytes");
        }
        
        return deserialize(data);
    }
    
    public static boolean isValidBase64(String base64Data) {
        if (base64Data == null || base64Data.isEmpty()) {
            return false;
        }
        
        return base64Data.matches("^[A-Za-z0-9+/]*={0,2}$");
    }
} 