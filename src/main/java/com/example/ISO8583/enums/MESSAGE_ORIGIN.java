package com.example.ISO8583.enums;

public enum MESSAGE_ORIGIN {

    Acquirer("0"),
    AcquirerRepeat("1"),
    Issuer("2"),
    IssuerRepeat("3"),
    Other("4"),
    OtherRepeat("5")
    ;

    private final String code;

    MESSAGE_ORIGIN(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
