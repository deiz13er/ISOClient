package com.example.isoclient;

import com.example.DataSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingController {
    @FXML
    private javafx.scene.control.Button prev_button;
    @FXML
    private javafx.scene.control.Label time_to_delay_label;
    @FXML
    private javafx.scene.control.Slider time_to_delay_slider;
    @FXML
    private javafx.scene.control.Slider mistake_bitmap_slider;
    @FXML
    private javafx.scene.control.Slider mistake_field_slider;
    @FXML
    private javafx.scene.control.Slider num_messages_slider;
    @FXML
    private javafx.scene.control.ComboBox type_message_combobox;
    @FXML
    private javafx.scene.control.CheckBox incorrect_mti_checkbox;
    @FXML
    private javafx.scene.control.CheckBox out_of_range_mti_checkbox;
    @FXML
    private javafx.scene.control.CheckBox incorrect_processing_code_checkbox;

    DataSingleton data = DataSingleton.getInstance();


    public void initialize() {
        time_to_delay_slider.setValue(data.getTime_to_delay());
        time_to_delay_label.setText(String.format("%.2f", data.getTime_to_delay())+"s");
        mistake_bitmap_slider.setValue(data.getMistake_bitmap());
        mistake_field_slider.setValue(data.getMistake_field());
        num_messages_slider.setValue(data.getNum_messages());
        incorrect_mti_checkbox.setSelected(data.isIncorrect_mti_checkbox());
        out_of_range_mti_checkbox.setSelected(data.isOut_of_range_mti_checkbox());
        incorrect_processing_code_checkbox.setSelected(data.isIncorrect_processing_code_checkbox());
        time_to_delay_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            data.setTime_to_delay(time_to_delay_slider.getValue());
            time_to_delay_label.setText(String.format("%.2f", data.getTime_to_delay())+"s");
        });
        mistake_bitmap_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            data.setMistake_bitmap((int)mistake_bitmap_slider.getValue());
        });
        mistake_field_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            data.setMistake_field((int)mistake_field_slider.getValue());
        });
        num_messages_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            data.setNum_messages((int)num_messages_slider.getValue());
        });
        incorrect_mti_checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            data.setIncorrect_mti_checkbox(newValue);
        });
        out_of_range_mti_checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            data.setOut_of_range_mti_checkbox(newValue);
        });
        incorrect_processing_code_checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            data.setIncorrect_processing_code_checkbox(newValue);
        });

        ObservableList<String> types = FXCollections.observableArrayList("Echo Request", "Purchase Request", "Network Key Change Request",
                "Network MAC Key Change Request", "Return Refund Request", "Return Reversal Request", "Purchase Reversal Request");
        type_message_combobox.setItems(types);
        type_message_combobox.setValue(data.getType_message());
        type_message_combobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            data.setType_message(type_message_combobox.getValue().toString());
        });

    }

    @FXML
    protected void onPrevButtonClick() throws IOException {
        Stage stage = (Stage) prev_button.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage.setTitle("ISO Client");
        stage.setResizable(false);
        stage.setScene(new Scene(root, 400, 400));
    }

}
