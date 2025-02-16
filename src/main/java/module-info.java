module com.tower_maze {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tower_maze to javafx.fxml;
    exports com.tower_maze;
}
