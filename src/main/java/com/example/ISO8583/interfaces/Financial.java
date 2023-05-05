package com.example.ISO8583.interfaces;

import java.math.BigDecimal;

public interface Financial<T> {


    DataElement<T> setAmount(BigDecimal amount);
}
