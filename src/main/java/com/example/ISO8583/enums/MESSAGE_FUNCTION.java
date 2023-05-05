package com.example.ISO8583.enums;


public enum MESSAGE_FUNCTION {

    Request("0"),
    RequestResponse("1"),
    Advice("2"),
    AdviceResponse("3"),
    Notification("4"),
    ResponseAcknowledgment("8"),
    NegativeAcknowledgment("9");

    private final String code;

    MESSAGE_FUNCTION(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
