module com.example.portfilioproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.portfilioproject to javafx.fxml;
    exports com.example.portfilioproject;
}