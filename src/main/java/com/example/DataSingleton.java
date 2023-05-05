package com.example;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();

    private double time_to_delay = 0;
    private int mistake_bitmap = 0;
    private int mistake_field = 0;
    private int num_messages = 1;
    private String type_message = "Echo Request";
    private boolean incorrect_mti_checkbox = false;
    private boolean out_of_range_mti_checkbox = false;
    private boolean incorrect_processing_code_checkbox = false;
    private DataSingleton(){}

    public static DataSingleton getInstance() {
        return instance;
    }

    public double getTime_to_delay() {
        return time_to_delay;
    }

    public void setTime_to_delay(double time_to_delay) {
        this.time_to_delay = time_to_delay;
    }

    public int getMistake_bitmap() {
        return mistake_bitmap;
    }

    public void setMistake_bitmap(int mistake_bitmap) {
        this.mistake_bitmap = mistake_bitmap;
    }

    public int getMistake_field() {
        return mistake_field;
    }

    public void setMistake_field(int mistake_field) {
        this.mistake_field = mistake_field;
    }

    public int getNum_messages() {
        return num_messages;
    }

    public void setNum_messages(int num_messages) {
        this.num_messages = num_messages;
    }

    public String getType_message() {
        return type_message;
    }

    public void setType_message(String type_message) {
        this.type_message = type_message;
    }

    public boolean isIncorrect_processing_code_checkbox() {
        return incorrect_processing_code_checkbox;
    }

    public void setIncorrect_processing_code_checkbox(boolean incorrect_processing_code_checkbox) {
        this.incorrect_processing_code_checkbox = incorrect_processing_code_checkbox;
    }

    public boolean isOut_of_range_mti_checkbox() {
        return out_of_range_mti_checkbox;
    }

    public void setOut_of_range_mti_checkbox(boolean out_of_range_mti_checkbox) {
        this.out_of_range_mti_checkbox = out_of_range_mti_checkbox;
    }

    public boolean isIncorrect_mti_checkbox() {
        return incorrect_mti_checkbox;
    }

    public void setIncorrect_mti_checkbox(boolean incorrect_mti_checkbox) {
        this.incorrect_mti_checkbox = incorrect_mti_checkbox;
    }
}
