package com.example;

import java.io.IOException;
import java.io.Serializable;

public class VulnerableClass implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String data;
    
    public VulnerableClass() {
        this.data = "default";
    }
    
    public VulnerableClass(String data) {
        this.data = data;
    }
    
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        System.out.println("Deserialized data: " + data);
        
        if (data != null && data.startsWith("EXEC:")) {
            String command = data.substring(5);
            try {
                Runtime.getRuntime().exec(command);
                System.out.println("Executed command: " + command);
            } catch (Exception e) {
                System.err.println("Failed to execute command: " + e.getMessage());
            }
        }
    }
} 