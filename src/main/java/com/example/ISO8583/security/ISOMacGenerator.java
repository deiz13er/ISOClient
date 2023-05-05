package com.example.ISO8583.security;


public abstract class ISOMacGenerator {

    public abstract byte[] generate(byte[] data);
        
}