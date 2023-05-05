package com.example.ISO8583.interfaces;

import com.example.ISO8583.entities.ISOMessage;
import com.example.ISO8583.exceptions.ISOException;

public interface UnpackMethods {

    ISOMessage build() throws ISOException;
}
