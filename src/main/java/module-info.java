/**
 * @author Ayman Abu Awad
 */

module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.prefs;
    requires java.logging;
    requires java.sql;
    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}