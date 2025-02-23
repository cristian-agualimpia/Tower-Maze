module com.tower_maze {
    // Módulos que necesitas
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics; 
    // o el resto que uses de JavaFX

    // Para que JavaFX pueda acceder reflexivamente a las clases en com.tower_maze.view
    opens com.tower_maze.view to javafx.fxml, javafx.graphics;

    // Para que puedas usar esas clases si se importan desde fuera
    exports com.tower_maze.view;
    exports com.tower_maze.controller;
    exports com.tower_maze.model;
}
