module org.example.eiscuno {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.eiscuno to javafx.fxml;
    exports org.example.eiscuno;
}