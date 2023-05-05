package com.example.ISO8583.interfaces;

public interface UnpackMessage {

    UnpackMethods setMessage(byte[] message);
    UnpackMethods setMessage(String message);

}
