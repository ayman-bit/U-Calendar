module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.prefs;
    requires java.logging;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}