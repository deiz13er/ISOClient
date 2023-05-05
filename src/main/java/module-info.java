module com.example.isoclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.all;


    opens com.example.isoclient to javafx.fxml;
    exports com.example.isoclient;
}