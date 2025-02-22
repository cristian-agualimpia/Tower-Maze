module com.tower_maze {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tower_maze.view to javafx.fxml;
    exports com.tower_maze;
}