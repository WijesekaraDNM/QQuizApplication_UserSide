module com.example.onlinequiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

//    requires org.controlsfx.controls;
//    requires org.kordamp.bootstrapfx.core;
//    requires com.almasb.fxgl.all;
//    requires annotations;

    opens com.example.onlinequiz to javafx.fxml;
    exports com.example.onlinequiz;
}