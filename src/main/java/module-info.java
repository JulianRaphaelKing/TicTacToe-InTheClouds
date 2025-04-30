module com.example.portfilioproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.portfilioproject to javafx.fxml;
    exports com.example.portfilioproject;
}