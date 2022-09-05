module com.example.circleapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.circleapp to javafx.fxml;
    exports com.example.circleapp;
    exports com.example.circleapp.server;
    opens com.example.circleapp.server to javafx.fxml;
}