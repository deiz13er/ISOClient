package com.example.ISO8583.interfaces;

import com.example.ISO8583.enums.MESSAGE_FUNCTION;
import com.example.ISO8583.enums.MESSAGE_ORIGIN;

public interface MessagePacker<T> {

    ProcessCode<T> mti(MESSAGE_FUNCTION mFunction, MESSAGE_ORIGIN mOrigin);

    MessagePacker<T> setLeftPadding(byte character);

    MessagePacker<T> setRightPadding(byte character);
}
