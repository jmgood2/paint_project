module com.example.paint_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens com.example.paint_project to javafx.fxml;
    exports com.example.paint_project;
}